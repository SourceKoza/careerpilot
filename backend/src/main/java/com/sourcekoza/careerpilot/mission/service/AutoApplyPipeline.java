package com.sourcekoza.careerpilot.mission.service;

import com.sourcekoza.careerpilot.agent.tailoring.ResumeTailoringAgent;
import com.sourcekoza.careerpilot.mission.entity.ApplyMode;
import com.sourcekoza.careerpilot.mission.entity.DiscoveredJob;
import com.sourcekoza.careerpilot.mission.entity.DiscoveredJobStatus;
import com.sourcekoza.careerpilot.mission.entity.Mission;
import com.sourcekoza.careerpilot.mission.entity.TailoredResume;
import com.sourcekoza.careerpilot.mission.entity.TailoredResumeStatus;
import com.sourcekoza.careerpilot.mission.repository.DiscoveredJobRepository;
import com.sourcekoza.careerpilot.mission.repository.TailoredResumeRepository;
import com.sourcekoza.careerpilot.resume.domain.Resume;
import com.sourcekoza.careerpilot.resume.repository.ResumeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Auto-Apply Pipeline — orchestrates the full flow from scoring to tailoring to sending.
 *
 * <p>In FULL_AUTO mode, automatically sends emails for high-score jobs.
 * In SEMI_AUTO mode, creates DRAFT tailored resumes for user review.</p>
 *
 * <p>Scoring Rules:</p>
 * <ul>
 *   <li>&gt;= 80%: Ready to apply (use master resume or minor tailoring)</li>
 *   <li>60-79%: LLM tailors resume → re-scores → if new score &gt;= 75% → apply</li>
 *   <li>&lt; 60%: Ignored completely</li>
 * </ul>
 *
 * @since Sprint-16
 */
@Service
public class AutoApplyPipeline {

    private static final Logger log = LoggerFactory.getLogger(AutoApplyPipeline.class);

    private static final int HIGH_SCORE_THRESHOLD = 80;
    private static final int MID_SCORE_MIN = 60;
    private static final int POST_TAILORING_MIN = 75;

    private final DiscoveredJobRepository jobRepository;
    private final TailoredResumeRepository tailoredResumeRepository;
    private final ResumeRepository resumeRepository;
    private final ResumeTailoringAgent tailoringAgent;
    private final DocxGeneratorService docxGeneratorService;
    private final ResumeTailoringService tailoringService;

    public AutoApplyPipeline(DiscoveredJobRepository jobRepository,
                              TailoredResumeRepository tailoredResumeRepository,
                              ResumeRepository resumeRepository,
                              ResumeTailoringAgent tailoringAgent,
                              DocxGeneratorService docxGeneratorService,
                              ResumeTailoringService tailoringService) {
        this.jobRepository = jobRepository;
        this.tailoredResumeRepository = tailoredResumeRepository;
        this.resumeRepository = resumeRepository;
        this.tailoringAgent = tailoringAgent;
        this.docxGeneratorService = docxGeneratorService;
        this.tailoringService = tailoringService;
    }

    /**
     * Runs the auto-apply pipeline for a mission after job discovery.
     *
     * @param mission the mission to process
     * @param userId the user ID
     * @return number of jobs processed
     */
    @Transactional
    public int runPipeline(Mission mission, UUID userId) {
        log.info("AutoApplyPipeline starting: missionId={}, mode={}", mission.getId(), mission.getApplyMode());

        Resume resume = loadResume(mission, userId);
        if (resume == null) {
            log.warn("No resume found, skipping auto-apply pipeline");
            return 0;
        }

        List<DiscoveredJob> eligibleJobs = jobRepository.findByMissionId(mission.getId(), PageRequest.of(0, 50))
                .getContent().stream()
                .filter(j -> j.getMatchScore() != null && j.getMatchScore() >= MID_SCORE_MIN)
                .filter(j -> j.getJobStatus() == DiscoveredJobStatus.NEW)
                .filter(j -> j.getTailoredResumeId() == null)
                .toList();

        log.info("Pipeline found {} eligible jobs (score >= {}%)", eligibleJobs.size(), MID_SCORE_MIN);

        int processed = 0;
        for (DiscoveredJob job : eligibleJobs) {
            try {
                processJob(mission, job, resume, userId);
                processed++;
            } catch (Exception e) {
                log.warn("Pipeline failed for job '{}': {}", job.getTitle(), e.getMessage());
            }
        }

        log.info("AutoApplyPipeline completed: {} jobs processed", processed);
        return processed;
    }

    private void processJob(Mission mission, DiscoveredJob job, Resume resume, UUID userId) {
        int score = job.getMatchScore();
        boolean isHighScore = score >= HIGH_SCORE_THRESHOLD;

        // Step 1: Tailor the resume
        TailoredResume tailored = tailoringAgent.tailorResumeForJob(mission, job, resume, userId);
        if (tailored == null) {
            log.debug("Tailoring failed for job '{}', skipping", job.getTitle());
            return;
        }

        // Step 2: Check if tailored score meets threshold for mid-range jobs
        if (!isHighScore && tailored.getTailoredScore() != null
                && tailored.getTailoredScore() < POST_TAILORING_MIN) {
            log.debug("Job '{}' tailored score {} < {} threshold, leaving as DRAFT",
                    job.getTitle(), tailored.getTailoredScore(), POST_TAILORING_MIN);
            return;
        }

        // Step 3: Generate DOCX
        docxGeneratorService.generateDocx(tailored, resume.getTitle());

        // Step 4: Handle based on apply mode
        if (mission.getApplyMode() == ApplyMode.FULL_AUTO && isHighScore) {
            // Auto-send for high-score jobs
            tailoringService.approveAndSend(userId, mission.getId(), job.getId());
            log.info("FULL_AUTO: Application sent for '{}' (score={}%)", job.getTitle(), score);
        } else {
            // SEMI_AUTO or mid-range: leave as DRAFT for user review
            log.info("SEMI_AUTO: Tailored resume ready for review: '{}' (score={}%, tailored={}%)",
                    job.getTitle(), score, tailored.getTailoredScore());
        }
    }

    private Resume loadResume(Mission mission, UUID userId) {
        UUID resumeId = mission.getResumeId();
        if (resumeId != null) {
            return resumeRepository.findByIdAndUserIdAndDeletedAtIsNull(resumeId, userId).orElse(null);
        }
        return resumeRepository.findByUserIdAndDeletedAtIsNull(userId, PageRequest.of(0, 1))
                .stream().findFirst().orElse(null);
    }
}
