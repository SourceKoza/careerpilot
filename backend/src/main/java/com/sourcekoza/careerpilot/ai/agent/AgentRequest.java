package com.sourcekoza.careerpilot.ai.agent;

import java.util.Map;

/**
 * A request to an AI agent.
 *
 * <p>Generic container for agent input — agents interpret the parameters
 * based on their own domain requirements.</p>
 *
 * @param agentType  the type of agent to invoke
 * @param action     the specific action the agent should perform
 * @param parameters key-value parameters for the action
 */
public record AgentRequest(
        AgentType agentType,
        String action,
        Map<String, String> parameters
) {

    /**
     * Creates a request with an action and parameters.
     */
    public static AgentRequest of(AgentType agentType, String action, Map<String, String> parameters) {
        return new AgentRequest(agentType, action, Map.copyOf(parameters));
    }

    /**
     * Creates a request with an action and no parameters.
     */
    public static AgentRequest of(AgentType agentType, String action) {
        return new AgentRequest(agentType, action, Map.of());
    }

    /**
     * Retrieves a parameter value by key.
     *
     * @param key the parameter key
     * @return the value, or null if not present
     */
    public String getParameter(String key) {
        return parameters.get(key);
    }

    /**
     * Retrieves a parameter value with a default fallback.
     */
    public String getParameter(String key, String defaultValue) {
        return parameters.getOrDefault(key, defaultValue);
    }
}
