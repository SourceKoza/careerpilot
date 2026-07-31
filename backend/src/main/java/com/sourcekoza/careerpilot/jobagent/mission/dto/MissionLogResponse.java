package com.sourcekoza.careerpilot.jobagent.mission.dto;

import com.sourcekoza.careerpilot.jobagent.mission.entity.LogLevel;

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
