package com.sourcekoza.careerpilot.admin.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * @since Sprint-17
 */
public record AuditLogResponse(
        UUID id,
        UUID adminId,
        String action,
        String targetType,
        String targetId,
        String details,
        String ipAddress,
        Instant createdAt
) {}
