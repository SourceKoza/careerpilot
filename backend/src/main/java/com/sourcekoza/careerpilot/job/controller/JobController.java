package com.sourcekoza.careerpilot.job.controller;

import com.sourcekoza.careerpilot.common.ApiResponse;
import com.sourcekoza.careerpilot.common.PageResponse;
import com.sourcekoza.careerpilot.job.dto.JobCreateRequest;
import com.sourcekoza.careerpilot.job.dto.JobResponse;
import com.sourcekoza.careerpilot.job.dto.JobSummaryResponse;
import com.sourcekoza.careerpilot.job.dto.JobUpdateRequest;
import com.sourcekoza.careerpilot.job.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller for Job CRUD operations.
 *
 * <p>All endpoints require JWT authentication. Supports pagination and sorting
 * for the list endpoint.</p>
 */
@RestController
@RequestMapping("/api/v1/jobs")
@Tag(name = "Job", description = "Job management operations")
@SecurityRequirement(name = "bearerAuth")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new job",
            description = "Creates a new job posting")
    public ApiResponse<JobResponse> createJob(@Valid @RequestBody JobCreateRequest request) {
        JobResponse response = jobService.createJob(request);
        return ApiResponse.success(response, "Job created successfully");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a job by ID",
            description = "Retrieves a single job posting with all details")
    public ApiResponse<JobResponse> getJob(@PathVariable UUID id) {
        JobResponse response = jobService.getJob(id);
        return ApiResponse.success(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a job",
            description = "Updates an existing job posting")
    public ApiResponse<JobResponse> updateJob(@PathVariable UUID id,
                                              @Valid @RequestBody JobUpdateRequest request) {
        JobResponse response = jobService.updateJob(id, request);
        return ApiResponse.success(response, "Job updated successfully");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a job",
            description = "Permanently deletes a job posting")
    public void deleteJob(@PathVariable UUID id) {
        jobService.deleteJob(id);
    }

    @GetMapping
    @Operation(summary = "List jobs",
            description = "Returns a paginated and sortable list of jobs")
    public ApiResponse<PageResponse<JobSummaryResponse>> listJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Page<JobSummaryResponse> jobs = jobService.listJobs(PageRequest.of(page, size, sort));
        return ApiResponse.success(PageResponse.from(jobs));
    }
}
