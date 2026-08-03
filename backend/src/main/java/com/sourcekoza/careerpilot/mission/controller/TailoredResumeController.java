package com.sourcekoza.careerpilot.mission.controller;

import com.sourcekoza.careerpilot.auth.domain.User;
import com.sourcekoza.careerpilot.auth.repository.UserRepository;
import com.sourcekoza.careerpilot.common.ApiResponse;
import com.sourcekoza.careerpilot.mission.dto.ApplyModeRequest;
import com.sourcekoza.careerpilot.mission.dto.RegenerateRequest;
import com.sourcekoza.careerpilot.mission.dto.TailoredResumeResponse;
import com.sourcekoza.careerpilot.mission.entity.ApplyMode;
import com.sourcekoza.careerpilot.mission.entity.Mission;
import com.sourcekoza.careerpilot.mission.entity.TailoredResume;
import com.sourcekoza.careerpilot.mission.repository.MissionRepository;
import com.sourcekoza.careerpilot.mission.service.ResumeTailoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.UUID;

/**
 * REST controller for resume tailoring and auto-apply operations.
 *
 * @since Sprint-16
 */
@RestController
@RequestMapping("/api/v1/missions")
@Tag(name = "Resume Tailoring", description = "Resume tailoring and auto-apply pipeline")
@SecurityRequirement(name = "bearerAuth")
public class TailoredResumeController {

    private final ResumeTailoringService tailoringService;
    private final MissionRepository missionRepository;
    private final UserRepository userRepository;

    public TailoredResumeController(ResumeTailoringService tailoringService,
                                     MissionRepository missionRepository,
                                     UserRepository userRepository) {
        this.tailoringService = tailoringService;
        this.missionRepository = missionRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{missionId}/jobs/{jobId}/tailored-resume")
    @Operation(summary = "Get tailored resume preview")
    public ApiResponse<TailoredResumeResponse> getTailoredResume(
            @PathVariable UUID missionId, @PathVariable UUID jobId) {
        UUID userId = getAuthenticatedUserId();
        TailoredResume tailored = tailoringService.getTailoredResume(userId, missionId, jobId);
        if (tailored == null) {
            return ApiResponse.success(null, "No tailored resume found for this job");
        }
        return ApiResponse.success(toResponse(tailored));
    }

    @PostMapping("/{missionId}/jobs/{jobId}/tailor")
    @Operation(summary = "Trigger resume tailoring for a job")
    public ApiResponse<TailoredResumeResponse> tailorResume(
            @PathVariable UUID missionId, @PathVariable UUID jobId) {
        UUID userId = getAuthenticatedUserId();
        TailoredResume tailored = tailoringService.tailorForJob(userId, missionId, jobId);
        return ApiResponse.success(toResponse(tailored), "Resume tailored successfully");
    }

    @PostMapping("/{missionId}/jobs/{jobId}/approve")
    @Operation(summary = "Approve and send email with tailored resume")
    public ApiResponse<TailoredResumeResponse> approveAndSend(
            @PathVariable UUID missionId, @PathVariable UUID jobId) {
        UUID userId = getAuthenticatedUserId();
        TailoredResume tailored = tailoringService.approveAndSend(userId, missionId, jobId);
        return ApiResponse.success(toResponse(tailored), "Application sent successfully");
    }

    @PostMapping("/{missionId}/jobs/{jobId}/regenerate")
    @Operation(summary = "Regenerate tailored resume with user feedback")
    public ApiResponse<TailoredResumeResponse> regenerate(
            @PathVariable UUID missionId, @PathVariable UUID jobId,
            @Valid @RequestBody RegenerateRequest request) {
        UUID userId = getAuthenticatedUserId();
        TailoredResume tailored = tailoringService.regenerate(userId, missionId, jobId, request.feedback());
        return ApiResponse.success(toResponse(tailored), "Resume regenerated successfully");
    }

    @PostMapping("/{missionId}/jobs/{jobId}/skip")
    @Operation(summary = "Skip this job")
    public ApiResponse<Void> skipJob(
            @PathVariable UUID missionId, @PathVariable UUID jobId) {
        UUID userId = getAuthenticatedUserId();
        tailoringService.skipJob(userId, missionId, jobId);
        return ApiResponse.success(null, "Job skipped");
    }

    @GetMapping("/{missionId}/jobs/{jobId}/resume-download")
    @Operation(summary = "Download tailored DOCX resume")
    public ResponseEntity<Resource> downloadResume(
            @PathVariable UUID missionId, @PathVariable UUID jobId) {
        UUID userId = getAuthenticatedUserId();
        String filePath = tailoringService.getDocxFilePath(userId, missionId, jobId);

        if (filePath == null) {
            return ResponseEntity.notFound().build();
        }

        File file = new File(filePath);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"tailored_resume.docx\"")
                .body(resource);
    }

    @PutMapping("/{missionId}/apply-mode")
    @Operation(summary = "Change apply mode for a mission")
    public ApiResponse<String> changeApplyMode(
            @PathVariable UUID missionId,
            @Valid @RequestBody ApplyModeRequest request) {
        UUID userId = getAuthenticatedUserId();
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("Mission not found"));
        if (!mission.getUserId().equals(userId)) {
            throw new SecurityException("Mission does not belong to user");
        }

        mission.setApplyMode(ApplyMode.valueOf(request.applyMode()));
        missionRepository.save(mission);

        return ApiResponse.success(request.applyMode(), "Apply mode updated");
    }

    private TailoredResumeResponse toResponse(TailoredResume entity) {
        return new TailoredResumeResponse(
                entity.getId(),
                entity.getMission().getId(),
                entity.getJob().getId(),
                entity.getSummary(),
                entity.getSkillsJson(),
                entity.getExperienceJson(),
                entity.getEducationJson(),
                entity.getTailoredScore(),
                entity.getOriginalScore(),
                entity.getStatus().name(),
                entity.getFeedback(),
                entity.getFilePath(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return user.getId();
    }
}
