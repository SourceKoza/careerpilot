package com.sourcekoza.careerpilot.mission.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * Response DTO for tailored resume data.
 *
 * @since Sprint-16
 */
public record TailoredResumeResponse(
        UUID id,
        UUID missionId,
        UUID jobId,
        String summary,
        String skillsJson,
        String experienceJson,
        String educationJson,
        Integer tailoredScore,
        Integer originalScore,
        String status,
        String feedback,
        String filePath,
        Instant createdAt,
        Instant updatedAt
) {
}
