package com.sourcekoza.careerpilot.job.search.agent;

import com.sourcekoza.careerpilot.job.search.model.JobSearchCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for the Job Search Agent.
 *
 * <p>Exposes the job search endpoint that triggers the JobSearchAgent
 * to search across enabled job sites, normalize results, and persist them.</p>
 *
 * <p>This controller only validates the request, delegates to the agent,
 * and returns the structured response. No business logic resides here.</p>
 *
 * @since Sprint-14
 */
@RestController
@RequestMapping("/api/v1/agents")
@Tag(name = "AI Agents", description = "AI Agent invocation and management")
public class JobSearchAgentController {

    private static final Logger log = LoggerFactory.getLogger(JobSearchAgentController.class);

    private final JobSearchAgent jobSearchAgent;

    public JobSearchAgentController(JobSearchAgent jobSearchAgent) {
        this.jobSearchAgent = jobSearchAgent;
    }

    /**
     * Executes a job search across all enabled job sites.
     *
     * <p>The agent searches enabled job portals using the provided criteria,
     * normalizes results into a common format, persists them through JobService,
     * and returns a structured response with execution summary and results.</p>
     *
     * @param criteria the search criteria (keyword required)
     * @return the job search response with execution summary and job list
     */
    @PostMapping("/job-search")
    @Operation(
            summary = "Execute job search",
            description = "Invokes the JobSearchAgent to search across enabled job portals, "
                    + "normalize results, persist jobs, and return a structured response"
    )
    public ResponseEntity<JobSearchAgentResponse> executeJobSearch(
            @Valid @RequestBody JobSearchCriteria criteria) {
        log.info("Job search request received: keyword='{}'", criteria.keyword());

        JobSearchAgentResponse response = jobSearchAgent.executeSearch(criteria);

        if (response.success()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
