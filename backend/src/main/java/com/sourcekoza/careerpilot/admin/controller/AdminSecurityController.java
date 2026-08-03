package com.sourcekoza.careerpilot.admin.controller;

import com.sourcekoza.careerpilot.admin.dto.SecurityConfigResponse;
import com.sourcekoza.careerpilot.admin.dto.SecurityConfigUpdateRequest;
import com.sourcekoza.careerpilot.admin.service.AdminSecurityService;
import com.sourcekoza.careerpilot.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin security configuration endpoints.
 *
 * @since Sprint-17
 */
@RestController
@RequestMapping("/api/v1/admin/security")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin - Security", description = "Security configuration (admin only)")
@SecurityRequirement(name = "bearerAuth")
public class AdminSecurityController {

    private final AdminSecurityService securityService;

    public AdminSecurityController(AdminSecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping
    @Operation(summary = "Get all security configs")
    public ApiResponse<List<SecurityConfigResponse>> getAll() {
        return ApiResponse.success(securityService.getAll());
    }

    @PutMapping("/{key}")
    @Operation(summary = "Update security config")
    public ApiResponse<SecurityConfigResponse> update(@PathVariable String key,
                                                       @RequestBody SecurityConfigUpdateRequest request) {
        return ApiResponse.success(securityService.update(key, request.value(), request.enabled()),
                "Security config updated");
    }
}
