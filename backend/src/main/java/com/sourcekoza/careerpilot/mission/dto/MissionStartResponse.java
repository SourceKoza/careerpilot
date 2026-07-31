package com.sourcekoza.careerpilot.mission.dto;

import com.sourcekoza.careerpilot.mission.entity.ExecutionStatus;

import java.util.UUID;

/**
 * Response after starting a mission execution.
 *
 * @since Sprint-15
 */
public record MissionStartResponse(
        UUID missionId,
        UUID executionId,
        ExecutionStatus status,
        String message
) {
}
