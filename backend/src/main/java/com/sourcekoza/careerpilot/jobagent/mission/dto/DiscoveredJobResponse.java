package com.sourcekoza.careerpilot.jobagent.mission.dto;

import com.sourcekoza.careerpilot.jobagent.mission.entity.DiscoveredJobStatus;
import com.sourcekoza.careerpilot.jobagent.mission.entity.PlatformType;

import java.time.Instant;
import java.util.UUID;

/**
 * Response for a discovered job with match score.
 *
 * @since Sprint-15
 */
public record DiscoveredJobResponse(
        UUID id,
        UUID missionId,
        PlatformType platform,
        String externalJobId,
        String title,
        String company,
        String location,
        String salary,
        String description,
        String jobUrl,
        DiscoveredJobStatus jobStatus,
        Integer matchScore,
        String matchReason,
        UUID tailoredResumeId,
        Instant createdAt
) {
}
