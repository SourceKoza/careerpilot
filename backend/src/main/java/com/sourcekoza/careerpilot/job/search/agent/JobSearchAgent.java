package com.sourcekoza.careerpilot.job.search.agent;

import com.sourcekoza.careerpilot.ai.agent.Agent;
import com.sourcekoza.careerpilot.ai.agent.AgentRequest;
import com.sourcekoza.careerpilot.ai.agent.AgentResponse;
import com.sourcekoza.careerpilot.ai.agent.AgentType;
import com.sourcekoza.careerpilot.job.domain.SourcePlatform;
import com.sourcekoza.careerpilot.job.domain.WorkplaceType;
import com.sourcekoza.careerpilot.job.dto.JobCreateRequest;
import com.sourcekoza.careerpilot.job.dto.JobResponse;
import com.sourcekoza.careerpilot.job.search.manager.JobSiteManager;
import com.sourcekoza.careerpilot.job.search.model.JobSearchCriteria;
import com.sourcekoza.careerpilot.job.search.model.JobSearchResult;
import com.sourcekoza.careerpilot.job.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Job Search Agent — the first production AI business agent.
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Validate the search request</li>
 *   <li>Invoke JobSiteManager to execute searches</li>
 *   <li>Persist discovered jobs via JobService</li>
 *   <li>Return a structured AgentResponse</li>
 * </ul>
 *
 * <p>Per ADR-006, this agent communicates directly with Application Services.
 * It never calls MCP tools, REST APIs, Controllers, or Repositories directly.</p>
 *
 * <p>The agent does not know about individual job sites — that responsibility
 * belongs entirely to the JobSiteManager.</p>
 *
 * @since Sprint-14
 */
@Component
public class JobSearchAgent implements Agent {

    private static final Logger log = LoggerFactory.getLogger(JobSearchAgent.class);

    private static final String ACTION_SEARCH = "search";
    private static final List<String> SUPPORTED_ACTIONS = List.of(ACTION_SEARCH);

    private final JobSiteManager jobSiteManager;
    private final JobService jobService;

    public JobSearchAgent(JobSiteManager jobSiteManager, JobService jobService) {
        this.jobSiteManager = jobSiteManager;
        this.jobService = jobService;
        log.info("JobSearchAgent initialized");
    }

    @Override
    public AgentType getAgentType() {
        return AgentType.JOB_SEARCH;
    }

    @Override
    public List<String> getSupportedActions() {
        return SUPPORTED_ACTIONS;
    }

    @Override
    public AgentResponse execute(AgentRequest request) {
        log.info("JobSearchAgent executing: action='{}'", request.action());
        Instant startedAt = Instant.now();

        if (!SUPPORTED_ACTIONS.contains(request.action())) {
            log.warn("JobSearchAgent: unsupported action '{}'", request.action());
            return AgentResponse.validationFailure(
                    AgentType.JOB_SEARCH, request.action(),
                    String.format("Unsupported action: '%s'. Supported: %s", request.action(), SUPPORTED_ACTIONS),
                    startedAt);
        }

        return executeSearch(request, startedAt);
    }

    /**
     * Executes the job search workflow using the provided criteria.
     *
     * @param criteria the search criteria
     * @return a response containing execution summary, total jobs, and the job list
     */
    public JobSearchAgentResponse executeSearch(JobSearchCriteria criteria) {
        log.info("JobSearchAgent search starting: keyword='{}', location='{}'",
                criteria.keyword(), criteria.location());
        Instant startedAt = Instant.now();

        // Validate criteria
        if (criteria.keyword() == null || criteria.keyword().isBlank()) {
            return JobSearchAgentResponse.failure("Keyword is required for job search",
                    Duration.ZERO, startedAt);
        }

        // Execute search via JobSiteManager
        List<JobSearchResult> searchResults = jobSiteManager.search(criteria);

        // Persist results via JobService
        List<JobResponse> persistedJobs = persistJobs(searchResults);

        Duration duration = Duration.between(startedAt, Instant.now());

        log.info("JobSearchAgent search completed: found={}, persisted={}, duration={}ms",
                searchResults.size(), persistedJobs.size(), duration.toMillis());

        return JobSearchAgentResponse.success(
                searchResults,
                persistedJobs.size(),
                jobSiteManager.getEnabledSiteNames(),
                duration,
                startedAt);
    }

    private AgentResponse executeSearch(AgentRequest request, Instant startedAt) {
        String keyword = request.getParameter("keyword");
        if (keyword == null || keyword.isBlank()) {
            return AgentResponse.validationFailure(
                    AgentType.JOB_SEARCH, ACTION_SEARCH,
                    "Parameter 'keyword' is required",
                    startedAt);
        }

        JobSearchCriteria criteria = new JobSearchCriteria(
                keyword,
                request.getParameter("location"),
                parseBooleanParam(request.getParameter("remoteOnly")),
                request.getParameter("employmentType"),
                parseIntegerParam(request.getParameter("page")),
                parseIntegerParam(request.getParameter("size"))
        );

        // Execute search via JobSiteManager
        List<JobSearchResult> searchResults = jobSiteManager.search(criteria);

        // Persist results via JobService
        List<JobResponse> persistedJobs = persistJobs(searchResults);

        Duration duration = Duration.between(startedAt, Instant.now());

        String message = String.format("Job search completed: %d jobs found, %d persisted from %s",
                searchResults.size(), persistedJobs.size(), jobSiteManager.getEnabledSiteNames());

        log.info("JobSearchAgent completed: found={}, persisted={}, duration={}ms",
                searchResults.size(), persistedJobs.size(), duration.toMillis());

        return AgentResponse.success(
                AgentType.JOB_SEARCH, ACTION_SEARCH,
                message, null, duration, startedAt);
    }

    private List<JobResponse> persistJobs(List<JobSearchResult> searchResults) {
        List<JobResponse> persisted = new ArrayList<>();

        for (JobSearchResult result : searchResults) {
            try {
                JobCreateRequest createRequest = mapToCreateRequest(result);
                JobResponse response = jobService.createJob(createRequest);
                persisted.add(response);
                log.debug("Job persisted: title='{}', company='{}'",
                        result.title(), result.company());
            } catch (Exception e) {
                log.warn("Failed to persist job: title='{}', company='{}', error='{}'",
                        result.title(), result.company(), e.getMessage());
            }
        }

        log.info("Job persistence summary: attempted={}, successful={}",
                searchResults.size(), persisted.size());
        return persisted;
    }

    private JobCreateRequest mapToCreateRequest(JobSearchResult result) {
        return new JobCreateRequest(
                result.title() != null ? result.title() : "Untitled",
                result.company() != null ? result.company() : "Unknown",
                result.location(),
                null, // employmentType — not always available from search
                result.remote() ? WorkplaceType.REMOTE : null,
                null, // experienceLevel — not available from search
                null, // salaryMin
                null, // salaryMax
                null, // currency
                result.description(),
                null, // requirements
                result.url(),
                mapSourcePlatform(result.source()),
                null, // externalJobId
                true  // active
        );
    }

    private SourcePlatform mapSourcePlatform(String source) {
        if (source == null) {
            return SourcePlatform.OTHER;
        }
        return switch (source.toLowerCase()) {
            case "linkedin" -> SourcePlatform.LINKEDIN;
            case "indeed" -> SourcePlatform.INDEED;
            default -> SourcePlatform.OTHER;
        };
    }

    private Boolean parseBooleanParam(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Boolean.parseBoolean(value);
    }

    private Integer parseIntegerParam(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
