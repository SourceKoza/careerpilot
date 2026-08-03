package com.sourcekoza.careerpilot.admin.controller;

import com.sourcekoza.careerpilot.admin.dto.AuditLogResponse;
import com.sourcekoza.careerpilot.admin.service.AuditService;
import com.sourcekoza.careerpilot.common.ApiResponse;
import com.sourcekoza.careerpilot.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Admin audit log endpoints.
 *
 * @since Sprint-17
 */
@RestController
@RequestMapping("/api/v1/admin/audit")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin - Audit", description = "Audit logs (admin only)")
@SecurityRequirement(name = "bearerAuth")
public class AdminAuditController {

    private final AuditService auditService;

    public AdminAuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    @Operation(summary = "Get audit logs")
    public ApiResponse<PageResponse<AuditLogResponse>> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        Page<AuditLogResponse> logs = auditService.getAuditLogs(PageRequest.of(page, size));
        return ApiResponse.success(PageResponse.from(logs));
    }
}
