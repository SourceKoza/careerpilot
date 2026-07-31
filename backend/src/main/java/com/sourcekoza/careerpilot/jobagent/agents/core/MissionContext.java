package com.sourcekoza.careerpilot.jobagent.agents.core;

import com.sourcekoza.careerpilot.jobagent.mission.entity.Mission;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Context object passed to each agent during mission execution.
 *
 * @since Sprint-15
 */
public class MissionContext {

    private final Mission mission;
    private final UUID executionId;
    private final UUID userId;
    private final Map<String, Object> variables;

    public MissionContext(Mission mission, UUID executionId, UUID userId) {
        this.mission = mission;
        this.executionId = executionId;
        this.userId = userId;
        this.variables = new HashMap<>();
    }

    public Mission getMission() {
        return mission;
    }

    public UUID getExecutionId() {
        return executionId;
    }

    public UUID getUserId() {
        return userId;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariable(String key, Object value) {
        this.variables.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getVariable(String key) {
        return (T) this.variables.get(key);
    }

    public String getKeywords() {
        return mission.getKeywords();
    }

    public String getLocation() {
        return mission.getLocation();
    }

    public String getExperienceLevel() {
        return mission.getExperienceLevel();
    }

    public boolean isRemotePreferred() {
        return mission.isRemote();
    }
}
