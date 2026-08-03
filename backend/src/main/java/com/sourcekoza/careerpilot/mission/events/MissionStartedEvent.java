package com.sourcekoza.careerpilot.mission.events;

import java.util.UUID;

/**
 * Published when a mission execution begins.
 *
 * @since Sprint-15
 */
public record MissionStartedEvent(UUID missionId, UUID executionId, UUID userId) {
}
