package com.sourcekoza.careerpilot.ai.agent;

import com.sourcekoza.careerpilot.ai.model.WorkflowResult;

import java.time.Duration;
import java.time.Instant;

/**
 * The response from an AI agent execution.
 *
 * <p>Wraps the workflow result with agent-level metadata including
 * the agent type, action performed, and timing information.</p>
 *
 * @param agentType      the agent that produced this response
 * @param action         the action that was performed
 * @param success        whether the agent execution succeeded
 * @param message        a human-readable summary of the result
 * @param workflowResult the underlying workflow result (may be null on validation failures)
 * @param duration       total agent execution time
 * @param executedAt     when the agent execution started
 */
public record AgentResponse(
        AgentType agentType,
        String action,
        boolean success,
        String message,
        WorkflowResult workflowResult,
        Duration duration,
        Instant executedAt
) {

    /**
     * Creates a successful agent response.
     */
    public static AgentResponse success(AgentType agentType, String action, String message,
                                        WorkflowResult workflowResult, Duration duration, Instant executedAt) {
        return new AgentResponse(agentType, action, true, message, workflowResult, duration, executedAt);
    }

    /**
     * Creates a failed agent response.
     */
    public static AgentResponse failure(AgentType agentType, String action, String message,
                                        WorkflowResult workflowResult, Duration duration, Instant executedAt) {
        return new AgentResponse(agentType, action, false, message, workflowResult, duration, executedAt);
    }

    /**
     * Creates a failed agent response without a workflow result (e.g., validation failure).
     */
    public static AgentResponse validationFailure(AgentType agentType, String action, String message,
                                                  Instant executedAt) {
        return new AgentResponse(agentType, action, false, message, null, Duration.ZERO, executedAt);
    }
}
