package com.sourcekoza.careerpilot.admin.controller;

import com.sourcekoza.careerpilot.admin.dto.AdminUserResponse;
import com.sourcekoza.careerpilot.admin.dto.RoleUpdateRequest;
import com.sourcekoza.careerpilot.admin.event.AdminActionEvent;
import com.sourcekoza.careerpilot.admin.service.AdminUserService;
import com.sourcekoza.careerpilot.auth.domain.Role;
import com.sourcekoza.careerpilot.common.ApiResponse;
import com.sourcekoza.careerpilot.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Admin user management endpoints.
 *
 * @since Sprint-17
 */
@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin - Users", description = "User management (admin only)")
@SecurityRequirement(name = "bearerAuth")
public class AdminUserController {

    private final AdminUserService userService;
    private final ApplicationEventPublisher eventPublisher;

    public AdminUserController(AdminUserService userService, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping
    @Operation(summary = "List all users")
    public ApiResponse<PageResponse<AdminUserResponse>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AdminUserResponse> users = userService.listUsers(PageRequest.of(page, size));
        return ApiResponse.success(PageResponse.from(users));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user details")
    public ApiResponse<AdminUserResponse> getUser(@PathVariable UUID id) {
        return ApiResponse.success(userService.getUser(id));
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "Change user role")
    public ApiResponse<AdminUserResponse> changeRole(@PathVariable UUID id,
                                                      @Valid @RequestBody RoleUpdateRequest request,
                                                      HttpServletRequest httpRequest) {
        AdminUserResponse result = userService.changeRole(id, Role.valueOf(request.role()));
        publishAudit("ROLE_CHANGED", "USER", id.toString(),
                String.format("{\"newRole\":\"%s\"}", request.role()), httpRequest);
        return ApiResponse.success(result, "Role updated");
    }

    @PutMapping("/{id}/disable")
    @Operation(summary = "Disable user account")
    public ApiResponse<AdminUserResponse> disableUser(@PathVariable UUID id, HttpServletRequest httpRequest) {
        AdminUserResponse result = userService.disableUser(id);
        publishAudit("USER_DISABLED", "USER", id.toString(), null, httpRequest);
        return ApiResponse.success(result, "User disabled");
    }

    @PutMapping("/{id}/enable")
    @Operation(summary = "Enable user account")
    public ApiResponse<AdminUserResponse> enableUser(@PathVariable UUID id, HttpServletRequest httpRequest) {
        AdminUserResponse result = userService.enableUser(id);
        publishAudit("USER_ENABLED", "USER", id.toString(), null, httpRequest);
        return ApiResponse.success(result, "User enabled");
    }

    private void publishAudit(String action, String targetType, String targetId,
                               String details, HttpServletRequest request) {
        UUID adminId = getAdminId();
        String ip = request.getRemoteAddr();
        eventPublisher.publishEvent(new AdminActionEvent(adminId, action, targetType, targetId, details, ip));
    }

    private UUID getAdminId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // The principal name is the email — we'd need to resolve to UUID
        // For simplicity, return a placeholder; in production, resolve via UserRepository
        return UUID.fromString("00000000-0000-0000-0000-000000000000");
    }
}
