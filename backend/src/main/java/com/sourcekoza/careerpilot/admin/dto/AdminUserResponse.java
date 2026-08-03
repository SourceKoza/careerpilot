package com.sourcekoza.careerpilot.admin.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * @since Sprint-17
 */
public record AdminUserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String role,
        boolean enabled,
        Instant createdAt
) {}
