package com.sourcekoza.careerpilot.resume.controller;

import com.sourcekoza.careerpilot.auth.domain.User;
import com.sourcekoza.careerpilot.auth.repository.UserRepository;
import com.sourcekoza.careerpilot.common.ApiResponse;
import com.sourcekoza.careerpilot.resume.dto.ResumeAnalysisResponse;
import com.sourcekoza.careerpilot.resume.dto.ResumeFileUploadResponse;
import com.sourcekoza.careerpilot.resume.service.ResumeIntelligenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * REST controller for Resume Intelligence operations.
 *
 * <p>Provides file upload, download, and AI-powered analysis endpoints
 * for the frontend Resume Intelligence section.</p>
 *
 * @since Sprint-15
 */
@RestController
@RequestMapping("/api/v1/resume-intelligence")
@Tag(name = "Resume Intelligence", description = "Resume upload, analysis, and intelligence")
@SecurityRequirement(name = "bearerAuth")
public class ResumeIntelligenceController {

    private final ResumeIntelligenceService intelligenceService;
    private final UserRepository userRepository;

    public ResumeIntelligenceController(ResumeIntelligenceService intelligenceService,
                                         UserRepository userRepository) {
        this.intelligenceService = intelligenceService;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Upload resume file",
            description = "Uploads a PDF or DOCX resume file for the authenticated user")
    public ApiResponse<ResumeFileUploadResponse> uploadResume(
            @RequestParam("file") MultipartFile file) throws IOException {
        UUID userId = getAuthenticatedUserId();
        ResumeFileUploadResponse response = intelligenceService.uploadResume(userId, file);
        return ApiResponse.success(response, "Resume uploaded successfully");
    }

    @PostMapping(value = "/replace", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Replace resume file",
            description = "Replaces the existing resume with a new file")
    public ApiResponse<ResumeFileUploadResponse> replaceResume(
            @RequestParam("file") MultipartFile file) throws IOException {
        UUID userId = getAuthenticatedUserId();
        ResumeFileUploadResponse response = intelligenceService.uploadResume(userId, file);
        return ApiResponse.success(response, "Resume replaced successfully");
    }

    @GetMapping("/current")
    @Operation(summary = "Get current resume",
            description = "Returns the current resume file info for the authenticated user")
    public ApiResponse<ResumeFileUploadResponse> getCurrentResume() {
        UUID userId = getAuthenticatedUserId();
        ResumeFileUploadResponse response = intelligenceService.getResumeFile(userId);
        return ApiResponse.success(response);
    }

    @PostMapping("/analyze")
    @Operation(summary = "Analyze resume",
            description = "Performs AI analysis on the resume: ATS scoring, skill gaps, and suggestions")
    public ApiResponse<ResumeAnalysisResponse> analyzeResume() {
        UUID userId = getAuthenticatedUserId();
        ResumeAnalysisResponse response = intelligenceService.analyzeResume(userId);
        return ApiResponse.success(response, "Resume analysis completed");
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return user.getId();
    }
}
