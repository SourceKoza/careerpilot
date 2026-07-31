package com.sourcekoza.careerpilot.jobagent.mission.scheduler;

import java.util.UUID;

/**
 * Interface for dispatching mission executions (future implementation).
 *
 * @since Sprint-15
 */
public interface MissionDispatcher {

    void dispatch(UUID missionId, UUID userId);

    void dispatchDelayed(UUID missionId, UUID userId, long delaySeconds);
}
