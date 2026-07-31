package com.sourcekoza.careerpilot.jobagent.agents.jobsearch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcekoza.careerpilot.ai.llm.LlmService;
import com.sourcekoza.careerpilot.jobagent.agents.core.AgentExecutionResult;
import com.sourcekoza.careerpilot.jobagent.agents.core.AgentType;
import com.sourcekoza.careerpilot.jobagent.agents.core.MissionAgent;
import com.sourcekoza.careerpilot.jobagent.agents.core.MissionContext;
import com.sourcekoza.careerpilot.jobagent.agents.platform.JobPlatformAdapter;
import com.sourcekoza.careerpilot.jobagent.agents.platform.JobSearchRequest;
import com.sourcekoza.careerpilot.jobagent.agents.platform.JobSearchResult;
import com.sourcekoza.careerpilot.jobagent.mission.entity.ContactSource;
import com.sourcekoza.careerpilot.jobagent.mission.entity.DiscoveredJob;
import com.sourcekoza.careerpilot.jobagent.mission.entity.Mission;
import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionContact;
import com.sourcekoza.careerpilot.jobagent.mission.repository.DiscoveredJobRepository;
import com.sourcekoza.careerpilot.jobagent.mission.repository.MissionContactRepository;
import com.sourcekoza.careerpilot.resume.domain.Resume;
import com.sourcekoza.careerpilot.resume.domain.Skill;
import com.sourcekoza.careerpilot.resume.repository.ResumeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * LLM-powered Mission Job Search Agent.
 *
 * <p>Uses the local Ollama LLM for:</p>
 * <ul>
 *   <li><strong>Before search</strong>: Reads user's resume + mission preferences
 *       to generate optimized search keywords</li>
 *   <li><strong>After search</strong>: Scores each discovered job against the
 *       user's resume (match %) with explanation</li>
 * </ul>
 *
 * <p>The agent uses these inputs from the mission:</p>
 * <ul>
 *   <li>Mission keywords, location, experience level, remote preference</li>
 *   <li>Mission's linked resume (skills, experience, target role)</li>
 *   <li>Employment type and platform preferences</li>
 * </ul>
 *
 * @since Sprint-15
 */
@Component
public class MissionJobSearchAgent implements MissionAgent {

    private static final Logger log = LoggerFactory.getLogger(MissionJobSearchAgent.class);

    private final List<JobPlatformAdapter> platformAdapters;
    private final DiscoveredJobRepository discoveredJobRepository;
    private final MissionContactRepository contactRepository;
    private final ResumeRepository resumeRepository;
    private final LlmService llmService;
    private final ObjectMapper objectMapper;

    public MissionJobSearchAgent(List<JobPlatformAdapter> platformAdapters,
                                  DiscoveredJobRepository discoveredJobRepository,
                                  MissionContactRepository contactRepository,
                                  ResumeRepository resumeRepository,
                                  LlmService llmService,
                                  ObjectMapper objectMapper) {
        this.platformAdapters = platformAdapters;
        this.discoveredJobRepository = discoveredJobRepository;
        this.contactRepository = contactRepository;
        this.resumeRepository = resumeRepository;
        this.llmService = llmService;
        this.objectMapper = objectMapper;
        log.info("MissionJobSearchAgent initialized with LLM support and {} platform adapters: {}",
                platformAdapters.size(),
                platformAdapters.stream().map(a -> a.platform().name()).toList());
    }

    @Override
    public AgentType getType() {
        return AgentType.JOB_SEARCH;
    }

    @Override
    public AgentExecutionResult execute(MissionContext context) {
        Mission mission = context.getMission();
        log.info("MissionJobSearchAgent executing: missionId={}, keywords='{}', resumeId={}",
                mission.getId(), context.getKeywords(), mission.getResumeId());
        Instant startedAt = Instant.now();

        // Step 1: Load resume data if mission has a linked resume
        String resumeSummary = loadResumeSummary(mission);

        // Step 2: Use LLM to generate optimized search keywords from mission + resume
        String optimizedKeywords = generateOptimizedKeywords(mission, resumeSummary);
        log.info("LLM optimized keywords: '{}'", optimizedKeywords);

        // Step 3: Build search request using optimized keywords
        JobSearchRequest request = new JobSearchRequest(
                optimizedKeywords,
                mission.getLocation(),
                mission.getExperienceLevel(),
                mission.isRemote(),
                mission.getEmploymentType(),
                25
        );

        // Step 4: Search all platform adapters
        List<JobSearchResult> allResults = new ArrayList<>();
        for (JobPlatformAdapter adapter : platformAdapters) {
            try {
                log.info("Searching platform: {}", adapter.platform());
                List<JobSearchResult> results = adapter.search(request);
                allResults.addAll(results);
                log.info("Platform {} returned {} results", adapter.platform(), results.size());
            } catch (Exception e) {
                log.error("Platform {} search failed: {}", adapter.platform(), e.getMessage());
            }
        }

        // Step 5: Persist jobs and use LLM to score each against resume
        int persistedJobs = persistAndScoreJobs(context, allResults, resumeSummary);
        int totalContacts = persistContacts(context, allResults);

        context.setVariable("jobsFound", persistedJobs);
        context.setVariable("contactsFound", totalContacts);
        context.setVariable("optimizedKeywords", optimizedKeywords);

        String message = String.format(
                "Job search completed: %d jobs found (LLM-scored), %d contacts extracted across %d platforms. Keywords used: '%s'",
                persistedJobs, totalContacts, platformAdapters.size(), optimizedKeywords);
        log.info("MissionJobSearchAgent completed: jobs={}, contacts={}", persistedJobs, totalContacts);

        return AgentExecutionResult.success(AgentType.JOB_SEARCH, message,
                persistedJobs, totalContacts, startedAt);
    }

    /**
     * Loads the user's resume and builds a text summary for the LLM.
     */
    private String loadResumeSummary(Mission mission) {
        UUID resumeId = mission.getResumeId();
        UUID userId = mission.getUserId();

        Resume resume = null;
        if (resumeId != null) {
            resume = resumeRepository.findById(resumeId).orElse(null);
        }
        if (resume == null) {
            // Fallback: find any resume for this user
            resume = resumeRepository.findByUserIdAndDeletedAtIsNull(userId, PageRequest.of(0, 1))
                    .stream().findFirst().orElse(null);
        }

        if (resume == null) {
            log.info("No resume found for mission, using mission keywords only");
            return "No resume available. Target role: " + (mission.getPreferredTitle() != null
                    ? mission.getPreferredTitle() : "Software Engineer");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Target Role: ").append(resume.getTargetRole() != null ? resume.getTargetRole() : "Software Engineer").append("\n");
        if (resume.getSummary() != null) sb.append("Summary: ").append(resume.getSummary()).append("\n");

        if (!resume.getSkills().isEmpty()) {
            sb.append("Skills: ").append(
                    resume.getSkills().stream().map(Skill::getName).collect(Collectors.joining(", "))
            ).append("\n");
        }

        if (!resume.getExperiences().isEmpty()) {
            sb.append("Experience: ");
            resume.getExperiences().forEach(exp ->
                    sb.append(exp.getPosition()).append(" at ").append(exp.getCompanyName()).append("; "));
            sb.append("\n");
        }

        log.info("Resume loaded for LLM context: {} chars", sb.length());
        return sb.toString();
    }

    /**
     * Uses LLM to generate optimized search keywords based on mission + resume.
     */
    private String generateOptimizedKeywords(Mission mission, String resumeSummary) {
        String systemPrompt = """
                You are a job search optimization expert. Given a user's mission preferences and resume summary,
                generate the BEST search keywords to find relevant job postings.
                
                Rules:
                - Return ONLY the search keywords as a single line (no explanation, no quotes, no formatting)
                - Combine the mission keywords with resume skills to create effective search terms
                - Keep it concise (max 5-6 keywords/phrases separated by spaces)
                - Focus on the most marketable and specific terms
                """;

        String userPrompt = String.format("""
                Mission Name: %s
                Mission Keywords: %s
                Preferred Title: %s
                Experience Level: %s
                Location: %s
                Remote: %s
                Employment Type: %s
                
                Resume:
                %s
                
                Generate optimized search keywords:""",
                mission.getName(),
                mission.getKeywords(),
                mission.getPreferredTitle() != null ? mission.getPreferredTitle() : "Not specified",
                mission.getExperienceLevel() != null ? mission.getExperienceLevel() : "Not specified",
                mission.getLocation() != null ? mission.getLocation() : "Any",
                mission.isRemote() ? "Yes" : "No",
                mission.getEmploymentType() != null ? mission.getEmploymentType() : "Any",
                resumeSummary);

        try {
            String result = llmService.chat(systemPrompt, userPrompt);
            // Clean up — LLM might add quotes or newlines
            String cleaned = result.trim().replace("\"", "").replace("\n", " ").strip();
            if (cleaned.isEmpty() || cleaned.length() > 200) {
                return mission.getKeywords(); // Fallback to original
            }
            return cleaned;
        } catch (Exception e) {
            log.warn("LLM keyword optimization failed, using mission keywords: {}", e.getMessage());
            return mission.getKeywords();
        }
    }

    /**
     * Persists jobs and uses LLM to score each job against the user's resume.
     */
    private int persistAndScoreJobs(MissionContext context, List<JobSearchResult> results, String resumeSummary) {
        int persisted = 0;

        for (JobSearchResult result : results) {
            try {
                DiscoveredJob job = new DiscoveredJob();
                job.setMission(context.getMission());
                job.setExecutionId(context.getExecutionId());
                job.setPlatform(result.platform());
                job.setExternalJobId(result.externalJobId());
                job.setTitle(result.title() != null ? result.title() : "Untitled");
                job.setCompany(result.company() != null ? result.company() : "Unknown");
                job.setLocation(result.location());
                job.setSalary(result.salary());
                job.setDescription(result.description());
                job.setJobUrl(result.jobUrl());

                // Use LLM to score this job against the resume
                scoreJobWithLlm(job, resumeSummary);

                discoveredJobRepository.save(job);
                persisted++;
            } catch (Exception e) {
                log.warn("Failed to persist job: title='{}', error='{}'", result.title(), e.getMessage());
            }
        }
        return persisted;
    }

    /**
     * Asks the LLM to score how well a job matches the user's resume.
     */
    private void scoreJobWithLlm(DiscoveredJob job, String resumeSummary) {
        try {
            String systemPrompt = """
                    You are a job matching expert. Score how well this job matches the candidate's resume.
                    Return ONLY a JSON object with exactly: {"score": 0-100, "reason": "one sentence explanation"}
                    No markdown, no extra text. Just the raw JSON.
                    """;

            String userPrompt = String.format("""
                    Job: %s at %s
                    Location: %s
                    Description: %s
                    
                    Candidate Resume:
                    %s
                    
                    Score this match:""",
                    job.getTitle(), job.getCompany(),
                    job.getLocation() != null ? job.getLocation() : "Not specified",
                    job.getDescription() != null ? job.getDescription() : "No description",
                    resumeSummary);

            String response = llmService.chat(systemPrompt, userPrompt);
            String json = response.trim();
            if (json.startsWith("```")) {
                json = json.replaceAll("```json\\s*", "").replaceAll("```\\s*", "");
            }

            JsonNode node = objectMapper.readTree(json);
            int score = node.get("score").asInt();
            String reason = node.get("reason").asText();

            job.setMatchScore(Math.min(100, Math.max(0, score)));
            job.setMatchReason(reason.length() > 500 ? reason.substring(0, 500) : reason);

            log.debug("Job scored: '{}' at {} → {}% ({})", job.getTitle(), job.getCompany(), score, reason);
        } catch (Exception e) {
            log.debug("LLM scoring failed for '{}', skipping: {}", job.getTitle(), e.getMessage());
            // Don't fail the job — just leave matchScore as null
        }
    }

    private int persistContacts(MissionContext context, List<JobSearchResult> results) {
        int persisted = 0;
        for (JobSearchResult result : results) {
            // Try to extract contact from recruiter field first
            String contactName = result.recruiterName();
            String contactEmail = null;
            String contactLinkedIn = result.recruiterLinkedIn();
            String contactRole = result.recruiterRole();

            // Use LLM to extract email/contact from job description
            if (result.description() != null && !result.description().isBlank()) {
                try {
                    String extracted = llmService.chat(
                            "Extract any email address from this text. Return ONLY the email address (e.g. john@company.com). If no email found, return 'none'.",
                            result.description()
                    );
                    String cleaned = extracted.trim().toLowerCase();
                    if (cleaned.contains("@") && !cleaned.equals("none")) {
                        contactEmail = cleaned.replaceAll("[^a-z0-9@._\\-]", "");
                    }
                } catch (Exception e) {
                    // LLM extraction failed — not critical
                }
            }

            if (contactName == null || contactName.isBlank()) {
                if (contactEmail == null) continue; // No useful contact data
                contactName = contactEmail.split("@")[0].replace(".", " ");
            }

            try {
                MissionContact contact = new MissionContact();
                contact.setMission(context.getMission());
                contact.setExecutionId(context.getExecutionId());
                contact.setName(contactName);
                contact.setEmail(contactEmail);
                contact.setLinkedInUrl(contactLinkedIn);
                contact.setRole(contactRole);
                contact.setConfidenceScore(contactEmail != null ? 0.9 : 0.5);
                contact.setSource(contactEmail != null ? ContactSource.EMAIL
                        : contactLinkedIn != null ? ContactSource.LINKEDIN
                        : ContactSource.COMPANY_SITE);
                contactRepository.save(contact);
                persisted++;
            } catch (Exception e) {
                log.warn("Failed to persist contact: name='{}', error='{}'", contactName, e.getMessage());
            }
        }
        return persisted;
    }
}
