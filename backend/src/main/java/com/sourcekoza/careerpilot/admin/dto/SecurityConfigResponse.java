package com.sourcekoza.careerpilot.admin.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * @since Sprint-17
 */
public record SecurityConfigResponse(
        UUID id,
        String configKey,
        String configValue,
        boolean enabled,
        Instant updatedAt
) {}
