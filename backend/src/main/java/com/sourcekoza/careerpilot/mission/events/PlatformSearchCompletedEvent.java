package com.sourcekoza.careerpilot.mission.events;

import com.sourcekoza.careerpilot.mission.entity.PlatformType;

import java.util.UUID;

/**
 * Published when a platform search completes.
 *
 * @since Sprint-15
 */
public record PlatformSearchCompletedEvent(UUID missionId, UUID executionId, PlatformType platform, int jobsFound) {
}
