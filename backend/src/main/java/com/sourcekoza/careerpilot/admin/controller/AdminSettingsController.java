package com.sourcekoza.careerpilot.admin.controller;

import com.sourcekoza.careerpilot.admin.dto.GlobalSettingCreateRequest;
import com.sourcekoza.careerpilot.admin.dto.GlobalSettingResponse;
import com.sourcekoza.careerpilot.admin.dto.GlobalSettingUpdateRequest;
import com.sourcekoza.careerpilot.admin.event.AdminActionEvent;
import com.sourcekoza.careerpilot.admin.service.GlobalSettingService;
import com.sourcekoza.careerpilot.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Admin global settings endpoints.
 *
 * @since Sprint-17
 */
@RestController
@RequestMapping("/api/v1/admin/settings")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin - Settings", description = "Global settings management (admin only)")
@SecurityRequirement(name = "bearerAuth")
public class AdminSettingsController {

    private final GlobalSettingService settingService;
    private final ApplicationEventPublisher eventPublisher;

    public AdminSettingsController(GlobalSettingService settingService,
                                    ApplicationEventPublisher eventPublisher) {
        this.settingService = settingService;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping
    @Operation(summary = "Get all settings grouped by category")
    public ApiResponse<Map<String, List<GlobalSettingResponse>>> getAll() {
        return ApiResponse.success(settingService.getAllGrouped());
    }

    @GetMapping("/{key}")
    @Operation(summary = "Get single setting by key")
    public ApiResponse<GlobalSettingResponse> getByKey(@PathVariable String key) {
        return ApiResponse.success(settingService.getByKey(key));
    }

    @PutMapping("/{key}")
    @Operation(summary = "Update setting value")
    public ApiResponse<GlobalSettingResponse> update(@PathVariable String key,
                                                      @Valid @RequestBody GlobalSettingUpdateRequest request,
                                                      HttpServletRequest httpRequest) {
        GlobalSettingResponse result = settingService.updateByKey(key, request.value());
        eventPublisher.publishEvent(new AdminActionEvent(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                "SETTING_CHANGED", "SETTING", key,
                String.format("{\"newValue\":\"%s\"}", request.value()),
                httpRequest.getRemoteAddr()));
        return ApiResponse.success(result, "Setting updated");
    }

    @PostMapping
    @Operation(summary = "Create new setting")
    public ApiResponse<GlobalSettingResponse> create(@Valid @RequestBody GlobalSettingCreateRequest request,
                                                      HttpServletRequest httpRequest) {
        GlobalSettingResponse result = settingService.create(request);
        eventPublisher.publishEvent(new AdminActionEvent(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                "SETTING_CREATED", "SETTING", request.key(), null,
                httpRequest.getRemoteAddr()));
        return ApiResponse.success(result, "Setting created");
    }
}
