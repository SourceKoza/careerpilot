package com.sourcekoza.careerpilot.admin.controller;

import com.sourcekoza.careerpilot.admin.dto.SystemStatsResponse;
import com.sourcekoza.careerpilot.admin.service.AdminDashboardService;
import com.sourcekoza.careerpilot.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin system dashboard endpoints.
 *
 * @since Sprint-17
 */
@RestController
@RequestMapping("/api/v1/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin - Dashboard", description = "System statistics (admin only)")
@SecurityRequirement(name = "bearerAuth")
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    public AdminDashboardController(AdminDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    @Operation(summary = "Get system statistics")
    public ApiResponse<SystemStatsResponse> getStats() {
        return ApiResponse.success(dashboardService.getStats());
    }
}
