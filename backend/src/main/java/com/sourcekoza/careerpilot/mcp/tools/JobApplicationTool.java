package com.sourcekoza.careerpilot.mcp.tools;

import com.sourcekoza.careerpilot.application.domain.ApplicationStatus;
import com.sourcekoza.careerpilot.application.dto.ApplicationCreateRequest;
import com.sourcekoza.careerpilot.application.dto.ApplicationResponse;
import com.sourcekoza.careerpilot.application.dto.ApplicationSummaryResponse;
import com.sourcekoza.careerpilot.application.dto.ApplicationUpdateRequest;
import com.sourcekoza.careerpilot.application.service.JobApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * MCP Job Application Tool.
 *
 * <p>Exposes job application operations as MCP-discoverable tools for AI agents.
 * Delegates all business logic to {@link JobApplicationService}.</p>
 */
@Service
public class JobApplicationTool {

    private static final Logger log = LoggerFactory.getLogger(JobApplicationTool.class);

    private final JobApplicationService applicationService;

    public JobApplicationTool(JobApplicationService applicationService) {
        this.applicationService = applicationService;
        log.info("MCP JobApplicationTool initialized");
    }

    @Tool(description = "Creates a new job application for a user. Links a job and resume version together.")
    public ApplicationResponse createApplication(
            @ToolParam(description = "The user ID (UUID) submitting the application") String userId,
            @ToolParam(description = "The job ID (UUID) being applied to") String jobId,
            @ToolParam(description = "The resume version ID (UUID) used for the application") String resumeVersionId,
            @ToolParam(description = "Application status: DRAFT, SUBMITTED, UNDER_REVIEW, INTERVIEW, OFFERED, REJECTED, WITHDRAWN, ACCEPTED") String status,
            @ToolParam(description = "Optional notes about the application") String notes) {
        log.debug("MCP JobApplicationTool.createApplication invoked: userId={}, jobId={}", userId, jobId);

        ApplicationStatus appStatus = (status != null && !status.isBlank())
                ? ApplicationStatus.valueOf(status)
                : ApplicationStatus.DRAFT;

        ApplicationCreateRequest request = new ApplicationCreateRequest(
                UUID.fromString(jobId),
                UUID.fromString(resumeVersionId),
                appStatus,
                notes,
                null);

        ApplicationResponse response = applicationService.createApplication(
                UUID.fromString(userId), request);
        log.debug("MCP JobApplicationTool.createApplication completed: applicationId={}", response.id());
        return response;
    }

    @Tool(description = "Retrieves a job application by ID for a specific user.")
    public ApplicationResponse getApplication(
            @ToolParam(description = "The user ID (UUID) who owns the application") String userId,
            @ToolParam(description = "The application ID (UUID) to retrieve") String applicationId) {
        log.debug("MCP JobApplicationTool.getApplication invoked: userId={}, applicationId={}", userId, applicationId);

        ApplicationResponse response = applicationService.getApplication(
                UUID.fromString(userId), UUID.fromString(applicationId));
        log.debug("MCP JobApplicationTool.getApplication completed: status={}", response.status());
        return response;
    }

    @Tool(description = "Lists all job applications for a user with pagination.")
    public Page<ApplicationSummaryResponse> listApplications(
            @ToolParam(description = "The user ID (UUID) who owns the applications") String userId,
            @ToolParam(description = "Page number (0-based)") int page,
            @ToolParam(description = "Page size (number of items per page)") int size) {
        log.debug("MCP JobApplicationTool.listApplications invoked: userId={}, page={}, size={}", userId, page, size);

        Page<ApplicationSummaryResponse> result = applicationService.listApplications(
                UUID.fromString(userId), PageRequest.of(page, size));
        log.debug("MCP JobApplicationTool.listApplications completed: totalElements={}", result.getTotalElements());
        return result;
    }

    @Tool(description = "Updates the status of a job application. Only status, notes, and external ID can be changed.")
    public ApplicationResponse updateApplicationStatus(
            @ToolParam(description = "The user ID (UUID) who owns the application") String userId,
            @ToolParam(description = "The application ID (UUID) to update") String applicationId,
            @ToolParam(description = "New status: DRAFT, SUBMITTED, UNDER_REVIEW, INTERVIEW, OFFERED, REJECTED, WITHDRAWN, ACCEPTED") String status,
            @ToolParam(description = "Updated notes (optional)") String notes) {
        log.debug("MCP JobApplicationTool.updateApplicationStatus invoked: applicationId={}, status={}", applicationId, status);

        ApplicationUpdateRequest request = new ApplicationUpdateRequest(
                ApplicationStatus.valueOf(status),
                notes,
                null);

        ApplicationResponse response = applicationService.updateApplication(
                UUID.fromString(userId), UUID.fromString(applicationId), request);
        log.debug("MCP JobApplicationTool.updateApplicationStatus completed: newStatus={}", response.status());
        return response;
    }
}
