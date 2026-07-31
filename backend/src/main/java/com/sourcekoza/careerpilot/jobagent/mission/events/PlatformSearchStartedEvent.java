package com.sourcekoza.careerpilot.jobagent.mission.events;

import com.sourcekoza.careerpilot.jobagent.mission.entity.PlatformType;

import java.util.UUID;

/**
 * Published when a platform search begins.
 *
 * @since Sprint-15
 */
public record PlatformSearchStartedEvent(UUID missionId, UUID executionId, PlatformType platform) {
}
