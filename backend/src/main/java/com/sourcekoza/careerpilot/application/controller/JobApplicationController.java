package com.sourcekoza.careerpilot.application.controller;

import com.sourcekoza.careerpilot.application.dto.ApplicationCreateRequest;
import com.sourcekoza.careerpilot.application.dto.ApplicationResponse;
import com.sourcekoza.careerpilot.application.dto.ApplicationSummaryResponse;
import com.sourcekoza.careerpilot.application.dto.ApplicationUpdateRequest;
import com.sourcekoza.careerpilot.application.service.JobApplicationService;
import com.sourcekoza.careerpilot.auth.domain.User;
import com.sourcekoza.careerpilot.auth.repository.UserRepository;
import com.sourcekoza.careerpilot.common.ApiResponse;
import com.sourcekoza.careerpilot.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
 * REST controller for Job Application CRUD operations.
 *
 * <p>All endpoints require JWT authentication. Applications are scoped
 * to the authenticated user.</p>
 */
@RestController
@RequestMapping("/api/v1/applications")
@Tag(name = "Job Application", description = "Job application management operations")
@SecurityRequirement(name = "bearerAuth")
public class JobApplicationController {

    private final JobApplicationService applicationService;
    private final UserRepository userRepository;

    public JobApplicationController(JobApplicationService applicationService,
                                    UserRepository userRepository) {
        this.applicationService = applicationService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new application",
            description = "Creates a new job application for the authenticated user")
    public ApiResponse<ApplicationResponse> createApplication(
            @Valid @RequestBody ApplicationCreateRequest request) {
        UUID userId = getAuthenticatedUserId();
        ApplicationResponse response = applicationService.createApplication(userId, request);
        return ApiResponse.success(response, "Application created successfully");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an application by ID",
            description = "Retrieves a single job application with all details")
    public ApiResponse<ApplicationResponse> getApplication(@PathVariable UUID id) {
        UUID userId = getAuthenticatedUserId();
        ApplicationResponse response = applicationService.getApplication(userId, id);
        return ApiResponse.success(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an application",
            description = "Updates application status, notes, and external ID. Job and resume version are immutable.")
    public ApiResponse<ApplicationResponse> updateApplication(
            @PathVariable UUID id,
            @Valid @RequestBody ApplicationUpdateRequest request) {
        UUID userId = getAuthenticatedUserId();
        ApplicationResponse response = applicationService.updateApplication(userId, id, request);
        return ApiResponse.success(response, "Application updated successfully");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an application",
            description = "Permanently deletes a job application")
    public void deleteApplication(@PathVariable UUID id) {
        UUID userId = getAuthenticatedUserId();
        applicationService.deleteApplication(userId, id);
    }

    @GetMapping
    @Operation(summary = "List applications",
            description = "Returns a paginated list of the authenticated user's job applications")
    public ApiResponse<PageResponse<ApplicationSummaryResponse>> listApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        UUID userId = getAuthenticatedUserId();
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Page<ApplicationSummaryResponse> applications =
                applicationService.listApplications(userId, PageRequest.of(page, size, sort));
        return ApiResponse.success(PageResponse.from(applications));
    }

    /**
     * Extracts the authenticated user's UUID from the Spring Security context.
     */
    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with email: " + email));
        return user.getId();
    }
}
