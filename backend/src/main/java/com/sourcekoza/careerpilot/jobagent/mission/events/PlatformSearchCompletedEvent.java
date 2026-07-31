package com.sourcekoza.careerpilot.jobagent.mission.events;

import com.sourcekoza.careerpilot.jobagent.mission.entity.PlatformType;

import java.util.UUID;

/**
 * Published when a platform search completes.
 *
 * @since Sprint-15
 */
public record PlatformSearchCompletedEvent(UUID missionId, UUID executionId, PlatformType platform, int jobsFound) {
}
