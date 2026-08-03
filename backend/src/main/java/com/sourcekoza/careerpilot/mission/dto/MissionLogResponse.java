package com.sourcekoza.careerpilot.mission.dto;

import com.sourcekoza.careerpilot.mission.entity.LogLevel;

import java.time.Instant;
import java.util.UUID;

/**
 * Response representing a mission execution log entry.
 *
 * @since Sprint-15
 */
public record MissionLogResponse(
        UUID id,
        UUID executionId,
        LogLevel level,
        String message,
        Instant logTime
) {
}
