package com.sourcekoza.careerpilot.jobagent.mission.dto;

import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Full mission response returned to clients.
 *
 * @since Sprint-15
 */
public record MissionResponse(
        UUID id,
        UUID userId,
        String name,
        String keywords,
        String preferredTitle,
        String experienceLevel,
        String location,
        boolean remote,
        boolean hybrid,
        Integer salaryMin,
        String currency,
        String employmentType,
        List<String> platforms,
        UUID resumeId,
        String schedule,
        String timezone,
        MissionStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
