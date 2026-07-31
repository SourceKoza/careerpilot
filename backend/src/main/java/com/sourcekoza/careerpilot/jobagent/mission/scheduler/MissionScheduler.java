package com.sourcekoza.careerpilot.jobagent.mission.scheduler;

import java.util.UUID;

/**
 * Interface for mission scheduling capabilities (future implementation).
 *
 * @since Sprint-15
 */
public interface MissionScheduler {

    void schedule(UUID missionId, String cronExpression);

    void cancel(UUID missionId);

    boolean isScheduled(UUID missionId);
}
