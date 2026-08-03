package com.sourcekoza.careerpilot.mission.dto;

import com.sourcekoza.careerpilot.mission.entity.ExecutionStatus;

import java.time.Instant;
import java.util.UUID;

/**
 * Response representing a single mission execution.
 *
 * @since Sprint-15
 */
public record MissionExecutionResponse(
        UUID id,
        UUID missionId,
        ExecutionStatus status,
        Instant startedAt,
        Instant completedAt,
        Long durationMs,
        int jobsFound,
        int contactsFound,
        String errorMessage
) {
}
