package com.sourcekoza.careerpilot.ai.model;

import java.time.Duration;

/**
 * The result of executing a single workflow step.
 *
 * @param toolName the MCP tool that was invoked
 * @param success  whether the tool execution succeeded
 * @param output   the tool's output (JSON string) or error message
 * @param duration time taken to execute the step
 */
public record StepResult(
        String toolName,
        boolean success,
        String output,
        Duration duration
) {

    /**
     * Creates a successful step result.
     */
    public static StepResult success(String toolName, String output, Duration duration) {
        return new StepResult(toolName, true, output, duration);
    }

    /**
     * Creates a failed step result.
     */
    public static StepResult failure(String toolName, String errorMessage, Duration duration) {
        return new StepResult(toolName, false, errorMessage, duration);
    }
}
