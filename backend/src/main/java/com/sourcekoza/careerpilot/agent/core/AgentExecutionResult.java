package com.sourcekoza.careerpilot.agent.core;

import java.time.Duration;
import java.time.Instant;

/**
 * Result of a mission agent execution.
 *
 * @since Sprint-15
 */
public record AgentExecutionResult(
        AgentType agentType,
        boolean success,
        String message,
        int jobsFound,
        int contactsFound,
        Duration duration,
        Instant startedAt,
        Instant completedAt
) {

    public static AgentExecutionResult success(AgentType agentType, String message,
                                                int jobsFound, int contactsFound,
                                                Instant startedAt) {
        Instant completedAt = Instant.now();
        return new AgentExecutionResult(
                agentType, true, message, jobsFound, contactsFound,
                Duration.between(startedAt, completedAt), startedAt, completedAt
        );
    }

    public static AgentExecutionResult failure(AgentType agentType, String message,
                                                Instant startedAt) {
        Instant completedAt = Instant.now();
        return new AgentExecutionResult(
                agentType, false, message, 0, 0,
                Duration.between(startedAt, completedAt), startedAt, completedAt
        );
    }
}
