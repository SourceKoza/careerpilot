package com.sourcekoza.careerpilot.ai.model;

/**
 * Represents a single step in a workflow — an MCP tool invocation.
 *
 * @param toolName  the MCP tool name to invoke
 * @param arguments JSON-encoded arguments to pass to the tool
 */
public record WorkflowStep(
        String toolName,
        String arguments
) {

    /**
     * Creates a step with no arguments.
     */
    public static WorkflowStep of(String toolName) {
        return new WorkflowStep(toolName, "{}");
    }

    /**
     * Creates a step with the given JSON arguments.
     */
    public static WorkflowStep of(String toolName, String arguments) {
        return new WorkflowStep(toolName, arguments);
    }
}
