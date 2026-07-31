package com.sourcekoza.careerpilot.agent.email;

import com.sourcekoza.careerpilot.ai.llm.LlmService;
import com.sourcekoza.careerpilot.agent.core.AgentExecutionResult;
import com.sourcekoza.careerpilot.agent.core.AgentType;
import com.sourcekoza.careerpilot.agent.core.MissionAgent;
import com.sourcekoza.careerpilot.agent.core.MissionContext;
import com.sourcekoza.careerpilot.mission.entity.ContactSource;
import com.sourcekoza.careerpilot.mission.entity.DiscoveredJob;
import com.sourcekoza.careerpilot.mission.entity.DiscoveredJobStatus;
import com.sourcekoza.careerpilot.mission.entity.MissionContact;
import com.sourcekoza.careerpilot.mission.repository.DiscoveredJobRepository;
import com.sourcekoza.careerpilot.mission.repository.MissionContactRepository;
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
 * Email Outreach Agent — generates and sends personalized cold emails
 * to recruiters for high-match jobs.
 *
 * <p>Flow:</p>
 * <ol>
 *   <li>Find discovered jobs with match score &gt;= 70% and status NEW</li>
 *   <li>Find associated contacts with emails</li>
 *   <li>Use LLM to generate personalized cold email (based on job + resume)</li>
 *   <li>Send email via EmailService</li>
 *   <li>Update job status to APPLIED</li>
 * </ol>
 *
 * @since Sprint-15
 */
@Component
public class EmailOutreachAgent implements MissionAgent {

    private static final Logger log = LoggerFactory.getLogger(EmailOutreachAgent.class);
    private static final int MIN_MATCH_SCORE = 70;

    private final DiscoveredJobRepository jobRepository;
    private final MissionContactRepository contactRepository;
    private final ResumeRepository resumeRepository;
    private final LlmService llmService;
    private final EmailService emailService;

    public EmailOutreachAgent(DiscoveredJobRepository jobRepository,
                               MissionContactRepository contactRepository,
                               ResumeRepository resumeRepository,
                               LlmService llmService,
                               EmailService emailService) {
        this.jobRepository = jobRepository;
        this.contactRepository = contactRepository;
        this.resumeRepository = resumeRepository;
        this.llmService = llmService;
        this.emailService = emailService;
        log.info("EmailOutreachAgent initialized");
    }

    @Override
    public AgentType getType() {
        return AgentType.EMAIL_OUTREACH;
    }

    @Override
    public AgentExecutionResult execute(MissionContext context) {
        log.info("EmailOutreachAgent executing: missionId={}", context.getMission().getId());
        Instant startedAt = Instant.now();

        UUID missionId = context.getMission().getId();

        // Load user's resume summary for personalization
        String resumeSummary = loadResumeSummary(context.getUserId());

        // Find high-match jobs that haven't been applied to
        List<DiscoveredJob> eligibleJobs = jobRepository.findByMissionId(missionId, PageRequest.of(0, 50))
                .getContent().stream()
                .filter(j -> j.getMatchScore() != null && j.getMatchScore() >= MIN_MATCH_SCORE)
                .filter(j -> j.getJobStatus() == DiscoveredJobStatus.NEW)
                .toList();

        log.info("Found {} eligible jobs (score >= {}%, status=NEW)", eligibleJobs.size(), MIN_MATCH_SCORE);

        // Find contacts with emails
        List<MissionContact> contacts = contactRepository.findByMissionId(missionId, PageRequest.of(0, 50))
                .getContent().stream()
                .filter(c -> c.getEmail() != null && !c.getEmail().isBlank())
                .toList();

        log.info("Found {} contacts with emails", contacts.size());

        int emailsSent = 0;

        for (DiscoveredJob job : eligibleJobs) {
            // Find a contact for this job (match by company or just use any available)
            MissionContact contact = findContactForJob(job, contacts);
            if (contact == null) {
                log.debug("No email contact for job: '{}'", job.getTitle());
                continue;
            }

            // Generate personalized email using LLM
            GeneratedEmail email = generateEmail(job, contact, resumeSummary, context.getMission().getName());
            if (email == null) continue;

            // Send email
            boolean sent = emailService.sendEmail(contact.getEmail(), email.subject(), email.body());
            if (sent) {
                job.setJobStatus(DiscoveredJobStatus.APPLIED);
                jobRepository.save(job);
                emailsSent++;
                log.info("Email sent for job: '{}' at {} → {}", job.getTitle(), job.getCompany(), contact.getEmail());
            }
        }

        String message = String.format("Email outreach completed: %d emails sent for %d eligible jobs",
                emailsSent, eligibleJobs.size());
        log.info("EmailOutreachAgent completed: sent={}", emailsSent);

        return AgentExecutionResult.success(AgentType.EMAIL_OUTREACH, message, emailsSent, 0, startedAt);
    }

    private GeneratedEmail generateEmail(DiscoveredJob job, MissionContact contact,
                                          String resumeSummary, String missionName) {
        try {
            String systemPrompt = """
                    You are a professional job application email writer. Write a concise, personalized cold email
                    to apply for a job. The email should be:
                    - Professional but warm
                    - Mention 2-3 specific skills that match the job
                    - Under 150 words for the body
                    - No generic templates — make it specific to this job
                    
                    Return ONLY in this format (no markdown, no extra text):
                    SUBJECT: <email subject line>
                    BODY:
                    <email body text>
                    """;

            String userPrompt = String.format("""
                    Job Title: %s
                    Company: %s
                    Job Description: %s
                    Recruiter/Contact Name: %s
                    Recruiter Role: %s
                    
                    My Resume Summary:
                    %s
                    
                    Write the application email:""",
                    job.getTitle(), job.getCompany(),
                    job.getDescription() != null ? job.getDescription() : "Not available",
                    contact.getName(),
                    contact.getRole() != null ? contact.getRole() : "Recruiter",
                    resumeSummary);

            String response = llmService.chat(systemPrompt, userPrompt);
            return parseEmailResponse(response);
        } catch (Exception e) {
            log.warn("LLM email generation failed for '{}': {}", job.getTitle(), e.getMessage());
            return null;
        }
    }

    private GeneratedEmail parseEmailResponse(String response) {
        String subject = "Job Application";
        String body = response;

        if (response.contains("SUBJECT:") && response.contains("BODY:")) {
            int subjectStart = response.indexOf("SUBJECT:") + 8;
            int bodyStart = response.indexOf("BODY:");
            subject = response.substring(subjectStart, bodyStart).trim();
            body = response.substring(bodyStart + 5).trim();
        }

        return new GeneratedEmail(subject, body);
    }

    private MissionContact findContactForJob(DiscoveredJob job, List<MissionContact> contacts) {
        // Try to match by execution ID first
        for (MissionContact c : contacts) {
            if (c.getExecutionId() != null && c.getExecutionId().equals(job.getExecutionId())) {
                return c;
            }
        }
        // Fallback: return first available contact with email
        return contacts.isEmpty() ? null : contacts.get(0);
    }

    private String loadResumeSummary(UUID userId) {
        Resume resume = resumeRepository.findByUserIdAndDeletedAtIsNull(userId, PageRequest.of(0, 1))
                .stream().findFirst().orElse(null);

        if (resume == null) return "Experienced software engineer";

        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(resume.getTitle()).append("\n");
        if (resume.getSummary() != null) sb.append(resume.getSummary()).append("\n");
        if (!resume.getSkills().isEmpty()) {
            sb.append("Skills: ").append(
                    resume.getSkills().stream().map(Skill::getName).collect(Collectors.joining(", ")));
        }
        return sb.toString();
    }

    private record GeneratedEmail(String subject, String body) {
    }
}
