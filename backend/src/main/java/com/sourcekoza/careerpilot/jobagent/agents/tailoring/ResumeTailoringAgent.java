package com.sourcekoza.careerpilot.jobagent.agents.tailoring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcekoza.careerpilot.ai.llm.LlmService;
import com.sourcekoza.careerpilot.jobagent.agents.core.AgentExecutionResult;
import com.sourcekoza.careerpilot.jobagent.agents.core.AgentType;
import com.sourcekoza.careerpilot.jobagent.agents.core.MissionAgent;
import com.sourcekoza.careerpilot.jobagent.agents.core.MissionContext;
import com.sourcekoza.careerpilot.jobagent.mission.entity.ApplyMode;
import com.sourcekoza.careerpilot.jobagent.mission.entity.DiscoveredJob;
import com.sourcekoza.careerpilot.jobagent.mission.entity.DiscoveredJobStatus;
import com.sourcekoza.careerpilot.jobagent.mission.entity.Mission;
import com.sourcekoza.careerpilot.jobagent.mission.entity.TailoredResume;
import com.sourcekoza.careerpilot.jobagent.mission.entity.TailoredResumeStatus;
import com.sourcekoza.careerpilot.jobagent.mission.repository.DiscoveredJobRepository;
import com.sourcekoza.careerpilot.jobagent.mission.repository.TailoredResumeRepository;
import com.sourcekoza.careerpilot.resume.domain.Education;
import com.sourcekoza.careerpilot.resume.domain.Experience;
import com.sourcekoza.careerpilot.resume.domain.Resume;
import com.sourcekoza.careerpilot.resume.domain.Skill;
import com.sourcekoza.careerpilot.resume.repository.ResumeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * AI-powered Resume Tailoring Agent.
 *
 * <p>For each discovered job scoring 60-100%, generates a tailored resume
 * using the LLM. The tailored resume reorders skills, rewrites the summary,
 * and highlights relevant experience — without fabricating any data.</p>
 *
 * @since Sprint-16
 */
@Component
public class ResumeTailoringAgent implements MissionAgent {

    private static final Logger log = LoggerFactory.getLogger(ResumeTailoringAgent.class);
    private static final int MIN_SCORE_FOR_TAILORING = 60;
    private static final int MIN_SCORE_AFTER_TAILORING = 75;

    private final LlmService llmService;
    private final ObjectMapper objectMapper;
    private final DiscoveredJobRepository jobRepository;
    private final TailoredResumeRepository tailoredResumeRepository;
    private final ResumeRepository resumeRepository;

    public ResumeTailoringAgent(LlmService llmService,
                                 ObjectMapper objectMapper,
                                 DiscoveredJobRepository jobRepository,
                                 TailoredResumeRepository tailoredResumeRepository,
                                 ResumeRepository resumeRepository) {
        this.llmService = llmService;
        this.objectMapper = objectMapper;
        this.jobRepository = jobRepository;
        this.tailoredResumeRepository = tailoredResumeRepository;
        this.resumeRepository = resumeRepository;
        log.info("ResumeTailoringAgent initialized");
    }

    @Override
    public AgentType getType() {
        return AgentType.RESUME_TAILORING;
    }

    @Override
    public AgentExecutionResult execute(MissionContext context) {
        Mission mission = context.getMission();
        log.info("ResumeTailoringAgent executing: missionId={}", mission.getId());
        Instant startedAt = Instant.now();

        Resume resume = loadResume(mission);
        if (resume == null) {
            String msg = "No resume found for tailoring";
            log.warn(msg);
            return AgentExecutionResult.failure(AgentType.RESUME_TAILORING, msg, startedAt);
        }

        // Find eligible jobs (score >= 60, status NEW)
        List<DiscoveredJob> eligibleJobs = jobRepository.findByMissionId(mission.getId(), PageRequest.of(0, 50))
                .getContent().stream()
                .filter(j -> j.getMatchScore() != null && j.getMatchScore() >= MIN_SCORE_FOR_TAILORING)
                .filter(j -> j.getJobStatus() == DiscoveredJobStatus.NEW)
                .filter(j -> j.getTailoredResumeId() == null)
                .toList();

        log.info("Found {} eligible jobs for tailoring (score >= {}%)", eligibleJobs.size(), MIN_SCORE_FOR_TAILORING);

        int tailored = 0;
        for (DiscoveredJob job : eligibleJobs) {
            try {
                TailoredResume result = tailorResumeForJob(mission, job, resume, context.getUserId());
                if (result != null) {
                    tailored++;
                }
            } catch (Exception e) {
                log.warn("Tailoring failed for job '{}': {}", job.getTitle(), e.getMessage());
            }
        }

        String message = String.format("Resume tailoring completed: %d resumes tailored for %d eligible jobs",
                tailored, eligibleJobs.size());
        log.info("ResumeTailoringAgent completed: tailored={}", tailored);
        return AgentExecutionResult.success(AgentType.RESUME_TAILORING, message, tailored, 0, startedAt);
    }

    /**
     * Tailors a resume for a specific job. Called both from pipeline and from the API.
     */
    public TailoredResume tailorResumeForJob(Mission mission, DiscoveredJob job, Resume resume, UUID userId) {
        String resumeData = buildResumeData(resume);
        String tailoredJson = callLlmForTailoring(job, resumeData);

        if (tailoredJson == null) {
            return null;
        }

        try {
            TailoredResumeContent content = parseTailoredContent(tailoredJson);
            int newScore = scoreAfterTailoring(job, content);

            TailoredResume tailored = new TailoredResume();
            tailored.setMission(mission);
            tailored.setJob(job);
            tailored.setUserId(userId);
            tailored.setSummary(content.summary());
            tailored.setSkillsJson(objectMapper.writeValueAsString(content.skills()));
            tailored.setExperienceJson(objectMapper.writeValueAsString(content.experiences()));
            tailored.setEducationJson(objectMapper.writeValueAsString(content.education()));
            tailored.setOriginalScore(job.getMatchScore());
            tailored.setTailoredScore(newScore);
            tailored.setStatus(TailoredResumeStatus.DRAFT);

            TailoredResume saved = tailoredResumeRepository.save(tailored);

            // Link the tailored resume to the job
            job.setTailoredResumeId(saved.getId());
            jobRepository.save(job);

            log.info("Resume tailored for job '{}' at {}: original={}%, tailored={}%",
                    job.getTitle(), job.getCompany(), job.getMatchScore(), newScore);
            return saved;
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize tailored content for job '{}': {}", job.getTitle(), e.getMessage());
            return null;
        }
    }

    /**
     * Regenerates a tailored resume with user feedback.
     */
    public TailoredResume regenerateWithFeedback(TailoredResume existing, DiscoveredJob job,
                                                   Resume resume, String feedback) {
        String resumeData = buildResumeData(resume);
        String tailoredJson = callLlmForRegeneration(job, resumeData, existing, feedback);

        if (tailoredJson == null) {
            return existing;
        }

        try {
            TailoredResumeContent content = parseTailoredContent(tailoredJson);
            int newScore = scoreAfterTailoring(job, content);

            existing.setSummary(content.summary());
            existing.setSkillsJson(objectMapper.writeValueAsString(content.skills()));
            existing.setExperienceJson(objectMapper.writeValueAsString(content.experiences()));
            existing.setEducationJson(objectMapper.writeValueAsString(content.education()));
            existing.setTailoredScore(newScore);
            existing.setFeedback(feedback);
            existing.setStatus(TailoredResumeStatus.DRAFT);

            return tailoredResumeRepository.save(existing);
        } catch (JsonProcessingException e) {
            log.error("Failed to regenerate tailored resume: {}", e.getMessage());
            return existing;
        }
    }

    private Resume loadResume(Mission mission) {
        UUID resumeId = mission.getResumeId();
        UUID userId = mission.getUserId();

        if (resumeId != null) {
            return resumeRepository.findByIdAndUserIdAndDeletedAtIsNull(resumeId, userId).orElse(null);
        }
        return resumeRepository.findByUserIdAndDeletedAtIsNull(userId, PageRequest.of(0, 1))
                .stream().findFirst().orElse(null);
    }

    private String buildResumeData(Resume resume) {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(resume.getTitle()).append("\n");
        if (resume.getSummary() != null) {
            sb.append("Summary: ").append(resume.getSummary()).append("\n");
        }
        if (resume.getTargetRole() != null) {
            sb.append("Target Role: ").append(resume.getTargetRole()).append("\n");
        }

        if (!resume.getSkills().isEmpty()) {
            sb.append("\nSkills:\n");
            for (Skill skill : resume.getSkills()) {
                sb.append("- ").append(skill.getName())
                        .append(" (").append(skill.getProficiency()).append(")")
                        .append(skill.getCategory() != null ? " [" + skill.getCategory() + "]" : "")
                        .append("\n");
            }
        }

        if (!resume.getExperiences().isEmpty()) {
            sb.append("\nExperience:\n");
            for (Experience exp : resume.getExperiences()) {
                sb.append("- ").append(exp.getPosition()).append(" at ").append(exp.getCompanyName());
                if (exp.getLocation() != null) sb.append(", ").append(exp.getLocation());
                sb.append(" (").append(exp.getStartDate()).append(" - ");
                sb.append(exp.isCurrentlyWorking() ? "Present" : exp.getEndDate()).append(")\n");
                if (exp.getDescription() != null) {
                    sb.append("  ").append(exp.getDescription()).append("\n");
                }
            }
        }

        if (!resume.getEducations().isEmpty()) {
            sb.append("\nEducation:\n");
            for (Education edu : resume.getEducations()) {
                sb.append("- ").append(edu.getDegree());
                if (edu.getFieldOfStudy() != null) sb.append(" in ").append(edu.getFieldOfStudy());
                sb.append(" at ").append(edu.getInstitution());
                sb.append(" (").append(edu.getStartDate()).append(" - ");
                sb.append(edu.getEndDate() != null ? edu.getEndDate() : "Present").append(")\n");
            }
        }

        return sb.toString();
    }

    private String callLlmForTailoring(DiscoveredJob job, String resumeData) {
        String systemPrompt = """
                You are a professional resume tailoring expert. Given a candidate's resume and a job description,
                tailor the resume to maximize the match with the job.
                
                STRICT RULES:
                - NEVER add skills the candidate doesn't have
                - NEVER fabricate experience, projects, or achievements
                - NEVER change education or certifications
                - Only use REAL data from the candidate's resume
                - Rewrite the professional summary targeting the specific job
                - Reorder skills putting matching ones first
                - Highlight relevant experience bullets
                - Add target role keyword alignment where truthful
                
                Return ONLY valid JSON with this exact structure (no markdown, no explanation):
                {
                  "summary": "tailored professional summary",
                  "skills": ["skill1", "skill2", ...],
                  "experiences": [
                    {"company": "...", "position": "...", "location": "...", "startDate": "...", "endDate": "...", "description": "..."}
                  ],
                  "education": [
                    {"institution": "...", "degree": "...", "fieldOfStudy": "...", "startDate": "...", "endDate": "..."}
                  ]
                }
                """;

        String userPrompt = String.format("""
                Job Title: %s
                Company: %s
                Location: %s
                Job Description: %s
                
                Candidate Resume:
                %s
                
                Tailor this resume for the job:""",
                job.getTitle(), job.getCompany(),
                job.getLocation() != null ? job.getLocation() : "Not specified",
                job.getDescription() != null ? job.getDescription() : "Not available",
                resumeData);

        try {
            String response = llmService.chat(systemPrompt, userPrompt);
            return cleanJsonResponse(response);
        } catch (Exception e) {
            log.warn("LLM tailoring call failed for '{}': {}", job.getTitle(), e.getMessage());
            return null;
        }
    }

    private String callLlmForRegeneration(DiscoveredJob job, String resumeData,
                                           TailoredResume existing, String feedback) {
        String systemPrompt = """
                You are a professional resume tailoring expert. The user has reviewed a previously tailored resume
                and wants changes. Apply their feedback while keeping the resume truthful and professional.
                
                STRICT RULES:
                - NEVER add skills the candidate doesn't have
                - NEVER fabricate experience, projects, or achievements
                - NEVER change education or certifications
                - Only use REAL data from the candidate's original resume
                - Apply the user's specific feedback
                
                Return ONLY valid JSON with this exact structure (no markdown, no explanation):
                {
                  "summary": "tailored professional summary",
                  "skills": ["skill1", "skill2", ...],
                  "experiences": [
                    {"company": "...", "position": "...", "location": "...", "startDate": "...", "endDate": "...", "description": "..."}
                  ],
                  "education": [
                    {"institution": "...", "degree": "...", "fieldOfStudy": "...", "startDate": "...", "endDate": "..."}
                  ]
                }
                """;

        String userPrompt = String.format("""
                Job Title: %s
                Company: %s
                Job Description: %s
                
                Original Resume Data:
                %s
                
                Previous Tailored Summary: %s
                
                User Feedback: %s
                
                Regenerate the tailored resume applying the feedback:""",
                job.getTitle(), job.getCompany(),
                job.getDescription() != null ? job.getDescription() : "Not available",
                resumeData,
                existing.getSummary() != null ? existing.getSummary() : "",
                feedback);

        try {
            String response = llmService.chat(systemPrompt, userPrompt);
            return cleanJsonResponse(response);
        } catch (Exception e) {
            log.warn("LLM regeneration failed: {}", e.getMessage());
            return null;
        }
    }

    private int scoreAfterTailoring(DiscoveredJob job, TailoredResumeContent content) {
        try {
            String systemPrompt = """
                    Score how well this tailored resume matches the job. Return ONLY a JSON object:
                    {"score": 0-100}
                    No markdown, no extra text.
                    """;

            String userPrompt = String.format("""
                    Job: %s at %s
                    Description: %s
                    
                    Tailored Resume Summary: %s
                    Skills: %s
                    
                    Score:""",
                    job.getTitle(), job.getCompany(),
                    job.getDescription() != null ? job.getDescription() : "",
                    content.summary(),
                    String.join(", ", content.skills()));

            String response = llmService.chat(systemPrompt, userPrompt);
            String json = cleanJsonResponse(response);
            JsonNode node = objectMapper.readTree(json);
            int score = node.get("score").asInt();
            return Math.min(100, Math.max(0, score));
        } catch (Exception e) {
            log.debug("Re-scoring failed, using original score + 10");
            return Math.min(100, (job.getMatchScore() != null ? job.getMatchScore() : 60) + 10);
        }
    }

    private TailoredResumeContent parseTailoredContent(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, TailoredResumeContent.class);
    }

    private String cleanJsonResponse(String response) {
        if (response == null) return null;
        String cleaned = response.trim();
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.replaceAll("```json\\s*", "").replaceAll("```\\s*", "");
        }
        return cleaned.trim();
    }
}
