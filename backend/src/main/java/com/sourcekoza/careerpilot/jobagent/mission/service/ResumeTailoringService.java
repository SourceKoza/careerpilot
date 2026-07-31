package com.sourcekoza.careerpilot.jobagent.mission.service;

import com.sourcekoza.careerpilot.ai.llm.LlmService;
import com.sourcekoza.careerpilot.jobagent.agents.email.EmailService;
import com.sourcekoza.careerpilot.jobagent.agents.tailoring.ResumeTailoringAgent;
import com.sourcekoza.careerpilot.jobagent.mission.entity.DiscoveredJob;
import com.sourcekoza.careerpilot.jobagent.mission.entity.DiscoveredJobStatus;
import com.sourcekoza.careerpilot.jobagent.mission.entity.Mission;
import com.sourcekoza.careerpilot.jobagent.mission.entity.TailoredResume;
import com.sourcekoza.careerpilot.jobagent.mission.entity.TailoredResumeStatus;
import com.sourcekoza.careerpilot.jobagent.mission.repository.DiscoveredJobRepository;
import com.sourcekoza.careerpilot.jobagent.mission.repository.MissionRepository;
import com.sourcekoza.careerpilot.jobagent.mission.repository.TailoredResumeRepository;
import com.sourcekoza.careerpilot.resume.domain.Resume;
import com.sourcekoza.careerpilot.resume.repository.ResumeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service layer for resume tailoring operations — triggered by REST API.
 *
 * <p>Handles tailoring, regeneration, approval, skipping, and DOCX download.
 * Keeps the controller thin.</p>
 *
 * @since Sprint-16
 */
@Service
public class ResumeTailoringService {

    private static final Logger log = LoggerFactory.getLogger(ResumeTailoringService.class);

    private final MissionRepository missionRepository;
    private final DiscoveredJobRepository jobRepository;
    private final TailoredResumeRepository tailoredResumeRepository;
    private final ResumeRepository resumeRepository;
    private final ResumeTailoringAgent tailoringAgent;
    private final DocxGeneratorService docxGeneratorService;
    private final EmailService emailService;
    private final LlmService llmService;

    public ResumeTailoringService(MissionRepository missionRepository,
                                   DiscoveredJobRepository jobRepository,
                                   TailoredResumeRepository tailoredResumeRepository,
                                   ResumeRepository resumeRepository,
                                   ResumeTailoringAgent tailoringAgent,
                                   DocxGeneratorService docxGeneratorService,
                                   EmailService emailService,
                                   LlmService llmService) {
        this.missionRepository = missionRepository;
        this.jobRepository = jobRepository;
        this.tailoredResumeRepository = tailoredResumeRepository;
        this.resumeRepository = resumeRepository;
        this.tailoringAgent = tailoringAgent;
        this.docxGeneratorService = docxGeneratorService;
        this.emailService = emailService;
        this.llmService = llmService;
    }

    /**
     * Gets the tailored resume for a specific job.
     */
    public TailoredResume getTailoredResume(UUID userId, UUID missionId, UUID jobId) {
        validateMissionOwnership(userId, missionId);
        return tailoredResumeRepository.findByJobIdAndUserId(jobId, userId).orElse(null);
    }

    /**
     * Triggers resume tailoring for a specific job.
     */
    @Transactional
    public TailoredResume tailorForJob(UUID userId, UUID missionId, UUID jobId) {
        Mission mission = validateMissionOwnership(userId, missionId);
        DiscoveredJob job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + jobId));

        // Check if already tailored
        TailoredResume existing = tailoredResumeRepository.findByJobIdAndUserId(jobId, userId).orElse(null);
        if (existing != null) {
            return existing;
        }

        Resume resume = loadResume(mission, userId);
        if (resume == null) {
            throw new IllegalStateException("No resume found for tailoring");
        }

        TailoredResume tailored = tailoringAgent.tailorResumeForJob(mission, job, resume, userId);
        if (tailored == null) {
            throw new RuntimeException("LLM failed to generate tailored resume");
        }

        // Generate DOCX file
        docxGeneratorService.generateDocx(tailored, resume.getTitle());

        return tailored;
    }

    /**
     * Approves a tailored resume and sends the application email with DOCX attached.
     */
    @Transactional
    public TailoredResume approveAndSend(UUID userId, UUID missionId, UUID jobId) {
        validateMissionOwnership(userId, missionId);
        TailoredResume tailored = tailoredResumeRepository.findByJobIdAndUserId(jobId, userId)
                .orElseThrow(() -> new IllegalArgumentException("No tailored resume found for job: " + jobId));

        DiscoveredJob job = tailored.getJob();

        // Generate email using LLM
        String emailBody = generateApplicationEmail(job, tailored);
        String subject = "Application: " + job.getTitle() + " at " + job.getCompany();

        // Ensure DOCX is generated
        if (tailored.getFilePath() == null) {
            Resume resume = loadResume(tailored.getMission(), userId);
            docxGeneratorService.generateDocx(tailored, resume != null ? resume.getTitle() : "Resume");
        }

        // Send email with attachment
        String contactEmail = findContactEmail(job);
        String attachmentName = "Resume_" + job.getCompany().replaceAll("[^a-zA-Z0-9]", "") + ".docx";

        boolean sent;
        if (tailored.getFilePath() != null) {
            sent = emailService.sendEmailWithAttachment(
                    contactEmail, subject, emailBody,
                    tailored.getFilePath(), attachmentName);
        } else {
            sent = emailService.sendEmail(contactEmail, subject, emailBody);
        }

        if (sent) {
            tailored.setStatus(TailoredResumeStatus.SENT);
            tailoredResumeRepository.save(tailored);
            job.setJobStatus(DiscoveredJobStatus.APPLIED);
            jobRepository.save(job);
            log.info("Application sent for job '{}' at {}", job.getTitle(), job.getCompany());
        }

        return tailored;
    }

    /**
     * Regenerates a tailored resume with user feedback.
     */
    @Transactional
    public TailoredResume regenerate(UUID userId, UUID missionId, UUID jobId, String feedback) {
        Mission mission = validateMissionOwnership(userId, missionId);
        TailoredResume existing = tailoredResumeRepository.findByJobIdAndUserId(jobId, userId)
                .orElseThrow(() -> new IllegalArgumentException("No tailored resume found for job: " + jobId));

        Resume resume = loadResume(mission, userId);
        if (resume == null) {
            throw new IllegalStateException("No resume found for regeneration");
        }

        DiscoveredJob job = existing.getJob();
        TailoredResume regenerated = tailoringAgent.regenerateWithFeedback(existing, job, resume, feedback);

        // Regenerate DOCX
        docxGeneratorService.generateDocx(regenerated, resume.getTitle());

        return regenerated;
    }

    /**
     * Skips a job (marks tailored resume as rejected).
     */
    @Transactional
    public void skipJob(UUID userId, UUID missionId, UUID jobId) {
        validateMissionOwnership(userId, missionId);

        TailoredResume tailored = tailoredResumeRepository.findByJobIdAndUserId(jobId, userId).orElse(null);
        if (tailored != null) {
            tailored.setStatus(TailoredResumeStatus.REJECTED);
            tailoredResumeRepository.save(tailored);
        }

        DiscoveredJob job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + jobId));
        job.setJobStatus(DiscoveredJobStatus.IGNORED);
        jobRepository.save(job);
    }

    /**
     * Returns the DOCX file path for download.
     */
    public String getDocxFilePath(UUID userId, UUID missionId, UUID jobId) {
        validateMissionOwnership(userId, missionId);
        TailoredResume tailored = tailoredResumeRepository.findByJobIdAndUserId(jobId, userId)
                .orElseThrow(() -> new IllegalArgumentException("No tailored resume found"));
        return tailored.getFilePath();
    }

    private Mission validateMissionOwnership(UUID userId, UUID missionId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("Mission not found: " + missionId));
        if (!mission.getUserId().equals(userId)) {
            throw new SecurityException("Mission does not belong to user");
        }
        return mission;
    }

    private Resume loadResume(Mission mission, UUID userId) {
        UUID resumeId = mission.getResumeId();
        if (resumeId != null) {
            return resumeRepository.findByIdAndUserIdAndDeletedAtIsNull(resumeId, userId).orElse(null);
        }
        return resumeRepository.findByUserIdAndDeletedAtIsNull(userId, PageRequest.of(0, 1))
                .stream().findFirst().orElse(null);
    }

    private String generateApplicationEmail(DiscoveredJob job, TailoredResume tailored) {
        try {
            String systemPrompt = """
                    Write a professional job application email body. Keep it concise (under 150 words).
                    Include: why the candidate is a good fit, 2-3 matching skills, and a call to action.
                    Do NOT include subject line. Return ONLY the email body text.
                    """;

            String userPrompt = String.format("""
                    Job: %s at %s
                    Job Description: %s
                    
                    Candidate Summary: %s
                    Top Skills: %s
                    
                    Write the email body:""",
                    job.getTitle(), job.getCompany(),
                    job.getDescription() != null ? job.getDescription() : "Not available",
                    tailored.getSummary() != null ? tailored.getSummary() : "",
                    tailored.getSkillsJson() != null ? tailored.getSkillsJson() : "");

            return llmService.chat(systemPrompt, userPrompt);
        } catch (Exception e) {
            log.warn("Failed to generate email body via LLM: {}", e.getMessage());
            return String.format("Dear Hiring Manager,\n\nI am writing to express my interest in the %s position at %s. "
                    + "Please find my tailored resume attached.\n\nBest regards", job.getTitle(), job.getCompany());
        }
    }

    private String findContactEmail(DiscoveredJob job) {
        // In a real scenario, look up from MissionContact table
        // For now, use a placeholder that will be logged in dry-run mode
        return "hr@" + job.getCompany().toLowerCase().replaceAll("[^a-z0-9]", "") + ".com";
    }
}
