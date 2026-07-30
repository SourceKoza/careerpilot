package com.sourcekoza.careerpilot.resume.controller;

import com.sourcekoza.careerpilot.auth.domain.User;
import com.sourcekoza.careerpilot.auth.repository.UserRepository;
import com.sourcekoza.careerpilot.common.ApiResponse;
import com.sourcekoza.careerpilot.common.PageResponse;
import com.sourcekoza.careerpilot.resume.dto.CreateResumeRequest;
import com.sourcekoza.careerpilot.resume.dto.ResumeResponse;
import com.sourcekoza.careerpilot.resume.dto.ResumeSummaryResponse;
import com.sourcekoza.careerpilot.resume.dto.ResumeVersionResponse;
import com.sourcekoza.careerpilot.resume.dto.UpdateResumeRequest;
import com.sourcekoza.careerpilot.resume.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import java.util.List;
import java.util.UUID;

/**
 * REST controller for Resume CRUD operations.
 *
 * <p>All endpoints require JWT authentication. The authenticated user's ID
 * is extracted from the SecurityContext to enforce ownership.</p>
 */
@RestController
@RequestMapping("/api/v1/resumes")
@Tag(name = "Resume", description = "Resume CRUD operations")
@SecurityRequirement(name = "bearerAuth")
public class ResumeController {

    private final ResumeService resumeService;
    private final UserRepository userRepository;

    public ResumeController(ResumeService resumeService, UserRepository userRepository) {
        this.resumeService = resumeService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new resume",
            description = "Creates a new resume for the authenticated user")
    public ApiResponse<ResumeResponse> createResume(@Valid @RequestBody CreateResumeRequest request) {
        UUID userId = getAuthenticatedUserId();
        ResumeResponse response = resumeService.createResume(userId, request);
        return ApiResponse.success(response, "Resume created successfully");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a resume by ID",
            description = "Retrieves a single resume with all sections")
    public ApiResponse<ResumeResponse> getResume(@PathVariable UUID id) {
        UUID userId = getAuthenticatedUserId();
        ResumeResponse response = resumeService.getResume(userId, id);
        return ApiResponse.success(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a resume",
            description = "Updates an existing resume. Creates a version snapshot before applying changes.")
    public ApiResponse<ResumeResponse> updateResume(@PathVariable UUID id,
                                                     @Valid @RequestBody UpdateResumeRequest request) {
        UUID userId = getAuthenticatedUserId();
        ResumeResponse response = resumeService.updateResume(userId, id, request);
        return ApiResponse.success(response, "Resume updated successfully");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a resume",
            description = "Soft-deletes a resume (sets deletedAt timestamp)")
    public void deleteResume(@PathVariable UUID id) {
        UUID userId = getAuthenticatedUserId();
        resumeService.deleteResume(userId, id);
    }

    @GetMapping
    @Operation(summary = "List user resumes",
            description = "Returns a paginated list of active resumes for the authenticated user")
    public ApiResponse<PageResponse<ResumeSummaryResponse>> listResumes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UUID userId = getAuthenticatedUserId();
        Page<ResumeSummaryResponse> resumes = resumeService.listResumes(userId, PageRequest.of(page, size));
        return ApiResponse.success(PageResponse.from(resumes));
    }

    @GetMapping("/{id}/versions")
    @Operation(summary = "Get resume versions",
            description = "Returns the version history for a resume")
    public ApiResponse<List<ResumeVersionResponse>> getVersions(@PathVariable UUID id) {
        UUID userId = getAuthenticatedUserId();
        List<ResumeVersionResponse> versions = resumeService.getVersions(userId, id);
        return ApiResponse.success(versions);
    }

    /**
     * Extracts the authenticated user's UUID from the Spring Security context.
     *
     * <p>The security filter stores the user's email as the principal name.
     * We look up the User entity by email to obtain the UUID.</p>
     *
     * @return the authenticated user's UUID
     * @throws UsernameNotFoundException if the user cannot be found
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
