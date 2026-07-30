package com.sourcekoza.careerpilot.mcp.tools;

import com.sourcekoza.careerpilot.job.dto.JobCreateRequest;
import com.sourcekoza.careerpilot.job.dto.JobResponse;
import com.sourcekoza.careerpilot.job.dto.JobSummaryResponse;
import com.sourcekoza.careerpilot.job.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * MCP Job Tool.
 *
 * <p>Exposes job operations as MCP-discoverable tools for AI agents.
 * Delegates all business logic to {@link JobService}.</p>
 */
@Service
public class JobTool {

    private static final Logger log = LoggerFactory.getLogger(JobTool.class);

    private final JobService jobService;

    public JobTool(JobService jobService) {
        this.jobService = jobService;
        log.info("MCP JobTool initialized");
    }

    @Tool(description = "Creates a new job posting. Returns the full job response.")
    public JobResponse createJob(
            @ToolParam(description = "Job title") String title,
            @ToolParam(description = "Company name") String companyName,
            @ToolParam(description = "Job location (optional)") String location,
            @ToolParam(description = "Job description (optional)") String description,
            @ToolParam(description = "Job requirements (optional)") String requirements,
            @ToolParam(description = "Application URL (optional)") String applicationUrl) {
        log.debug("MCP JobTool.createJob invoked: title={}, company={}", title, companyName);

        JobCreateRequest request = new JobCreateRequest(
                title, companyName, location,
                null, null, null,
                null, null, null,
                description, requirements,
                applicationUrl, null, null, true);

        JobResponse response = jobService.createJob(request);
        log.debug("MCP JobTool.createJob completed: jobId={}", response.id());
        return response;
    }

    @Tool(description = "Retrieves a job posting by ID. Returns the full job details.")
    public JobResponse getJob(
            @ToolParam(description = "The job ID (UUID) to retrieve") String jobId) {
        log.debug("MCP JobTool.getJob invoked: jobId={}", jobId);

        JobResponse response = jobService.getJob(UUID.fromString(jobId));
        log.debug("MCP JobTool.getJob completed: title={}", response.title());
        return response;
    }

    @Tool(description = "Lists all job postings with pagination. Returns lightweight summaries.")
    public Page<JobSummaryResponse> listJobs(
            @ToolParam(description = "Page number (0-based)") int page,
            @ToolParam(description = "Page size (number of items per page)") int size) {
        log.debug("MCP JobTool.listJobs invoked: page={}, size={}", page, size);

        Page<JobSummaryResponse> result = jobService.listJobs(PageRequest.of(page, size));
        log.debug("MCP JobTool.listJobs completed: totalElements={}", result.getTotalElements());
        return result;
    }
}
