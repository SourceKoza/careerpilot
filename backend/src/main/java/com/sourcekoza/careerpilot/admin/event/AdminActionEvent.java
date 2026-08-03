package com.sourcekoza.careerpilot.admin.event;

import java.util.UUID;

/**
 * Event published when an admin performs any action.
 * Consumed by AuditService to persist to audit_logs.
 *
 * @since Sprint-17
 */
public record AdminActionEvent(
        UUID adminId,
        String action,
        String targetType,
        String targetId,
        String details,
        String ipAddress
) {}
