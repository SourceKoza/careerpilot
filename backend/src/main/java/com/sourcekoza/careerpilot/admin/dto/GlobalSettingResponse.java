package com.sourcekoza.careerpilot.admin.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * @since Sprint-17
 */
public record GlobalSettingResponse(
        UUID id,
        String settingKey,
        String settingValue,
        String category,
        String description,
        Instant updatedAt
) {}
