package com.sourcekoza.careerpilot.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * @since Sprint-17
 */
public record RoleUpdateRequest(
        @NotBlank @Pattern(regexp = "ROLE_USER|ROLE_ADMIN") String role
) {}
