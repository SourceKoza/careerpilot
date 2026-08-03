package com.sourcekoza.careerpilot.mission.events;

import java.util.UUID;

/**
 * Published when a mission execution completes successfully.
 *
 * @since Sprint-15
 */
public record MissionCompletedEvent(UUID missionId, UUID executionId, int jobsFound, int contactsFound) {
}
