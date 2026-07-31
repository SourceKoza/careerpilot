package com.sourcekoza.careerpilot.mission.controller;

import com.sourcekoza.careerpilot.auth.domain.User;
import com.sourcekoza.careerpilot.auth.repository.UserRepository;
import com.sourcekoza.careerpilot.common.ApiResponse;
import com.sourcekoza.careerpilot.common.PageResponse;
import com.sourcekoza.careerpilot.mission.dto.MissionCreateRequest;
import com.sourcekoza.careerpilot.mission.dto.MissionEventResponse;
import com.sourcekoza.careerpilot.mission.dto.MissionExecutionResponse;
import com.sourcekoza.careerpilot.mission.dto.MissionLogResponse;
import com.sourcekoza.careerpilot.mission.dto.MissionResponse;
import com.sourcekoza.careerpilot.mission.dto.MissionStartResponse;
import com.sourcekoza.careerpilot.mission.service.MissionExecutionService;
import com.sourcekoza.careerpilot.mission.service.MissionService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller for mission operations.
 *
 * @since Sprint-15
 */
@RestController
@RequestMapping("/api/v1/missions")
@Tag(name = "Mission", description = "Mission execution and management")
@SecurityRequirement(name = "bearerAuth")
public class MissionController {

    private final MissionService missionService;
    private final MissionExecutionService executionService;
    private final UserRepository userRepository;

    public MissionController(MissionService missionService,
                              MissionExecutionService executionService,
                              UserRepository userRepository) {
        this.missionService = missionService;
        this.executionService = executionService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new mission")
    public ApiResponse<MissionResponse> createMission(@Valid @RequestBody MissionCreateRequest request) {
        UUID userId = getAuthenticatedUserId();
        MissionResponse response = missionService.createMission(userId, request);
        return ApiResponse.success(response, "Mission created successfully");
    }

    @GetMapping
    @Operation(summary = "List missions")
    public ApiResponse<PageResponse<MissionResponse>> listMissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        UUID userId = getAuthenticatedUserId();
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<MissionResponse> missions = missionService.listMissions(userId, PageRequest.of(page, size, sort));
        return ApiResponse.success(PageResponse.from(missions));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get mission details")
    public ApiResponse<MissionResponse> getMission(@PathVariable UUID id) {
        UUID userId = getAuthenticatedUserId();
        MissionResponse response = missionService.getMission(userId, id);
        return ApiResponse.success(response);
    }

    @PostMapping("/{id}/start")
    @Operation(summary = "Start mission execution")
    public ApiResponse<MissionStartResponse> startMission(@PathVariable UUID id) {
        UUID userId = getAuthenticatedUserId();
        MissionStartResponse response = executionService.startMission(userId, id);
        return ApiResponse.success(response, "Mission execution initiated");
    }

    @PostMapping("/{id}/apply")
    @Operation(summary = "Send outreach emails",
            description = "Uses AI to generate and send personalized emails for high-match jobs")
    public ApiResponse<MissionStartResponse> applyToJobs(@PathVariable UUID id) {
        UUID userId = getAuthenticatedUserId();
        MissionStartResponse response = executionService.runEmailOutreach(userId, id);
        return ApiResponse.success(response, "Email outreach initiated");
    }

    @PostMapping("/{id}/pause")
    @Operation(summary = "Pause mission")
    public ApiResponse<MissionResponse> pauseMission(@PathVariable UUID id) {
        UUID userId = getAuthenticatedUserId();
        MissionResponse response = missionService.pauseMission(userId, id);
        return ApiResponse.success(response, "Mission paused");
    }

    @PostMapping("/{id}/resume")
    @Operation(summary = "Resume mission")
    public ApiResponse<MissionResponse> resumeMission(@PathVariable UUID id) {
        UUID userId = getAuthenticatedUserId();
        MissionResponse response = missionService.resumeMission(userId, id);
        return ApiResponse.success(response, "Mission resumed");
    }

    @GetMapping("/{id}/executions")
    @Operation(summary = "Get mission executions")
    public ApiResponse<PageResponse<MissionExecutionResponse>> getExecutions(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UUID userId = getAuthenticatedUserId();
        Page<MissionExecutionResponse> executions = executionService.getExecutions(userId, id, PageRequest.of(page, size));
        return ApiResponse.success(PageResponse.from(executions));
    }

    @GetMapping("/{id}/events")
    @Operation(summary = "Get mission events")
    public ApiResponse<PageResponse<MissionEventResponse>> getEvents(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        UUID userId = getAuthenticatedUserId();
        Page<MissionEventResponse> events = executionService.getEvents(userId, id, PageRequest.of(page, size));
        return ApiResponse.success(PageResponse.from(events));
    }

    @GetMapping("/{id}/logs")
    @Operation(summary = "Get mission logs")
    public ApiResponse<PageResponse<MissionLogResponse>> getLogs(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        UUID userId = getAuthenticatedUserId();
        Page<MissionLogResponse> logs = executionService.getLogs(userId, id, PageRequest.of(page, size));
        return ApiResponse.success(PageResponse.from(logs));
    }

    @GetMapping("/{id}/jobs")
    @Operation(summary = "Get discovered jobs",
            description = "Returns jobs discovered during mission execution with LLM match scores")
    public ApiResponse<PageResponse<com.sourcekoza.careerpilot.mission.dto.DiscoveredJobResponse>> getDiscoveredJobs(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        UUID userId = getAuthenticatedUserId();
        Page<com.sourcekoza.careerpilot.mission.dto.DiscoveredJobResponse> jobs =
                executionService.getDiscoveredJobs(userId, id, PageRequest.of(page, size));
        return ApiResponse.success(PageResponse.from(jobs));
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return user.getId();
    }
}
