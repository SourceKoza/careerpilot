package com.sourcekoza.careerpilot.jobagent.mission.events;

import java.util.UUID;

/**
 * Published when jobs have been discovered and persisted.
 *
 * @since Sprint-15
 */
public record JobsDiscoveredEvent(UUID missionId, UUID executionId, int totalJobs) {
}
