package com.sourcekoza.careerpilot.ai.model;

import java.util.List;

/**
 * A request to execute a workflow — a named sequence of MCP tool invocations.
 *
 * @param workflowName a human-readable name for the workflow
 * @param steps        the ordered list of tool invocations to execute
 */
public record WorkflowRequest(
        String workflowName,
        List<WorkflowStep> steps
) {

    /**
     * Creates a workflow request with the given name and steps.
     */
    public static WorkflowRequest of(String workflowName, List<WorkflowStep> steps) {
        return new WorkflowRequest(workflowName, List.copyOf(steps));
    }
}
