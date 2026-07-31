package com.sourcekoza.careerpilot.mission.dto;

import com.sourcekoza.careerpilot.mission.entity.MissionEventType;

import java.time.Instant;
import java.util.UUID;

/**
 * Response representing a mission event.
 *
 * @since Sprint-15
 */
public record MissionEventResponse(
        UUID id,
        UUID missionId,
        UUID executionId,
        MissionEventType eventType,
        String message,
        Instant eventTime
) {
}
