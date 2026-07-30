package com.sourcekoza.careerpilot.ai.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Execution context that accumulates results across workflow steps.
 *
 * <p>Each step's output is stored by tool name, allowing subsequent steps
 * to reference previous results. This enables data flow between steps
 * in future workflows.</p>
 */
public final class ExecutionContext {

    private final Map<String, String> stepOutputs;

    public ExecutionContext() {
        this.stepOutputs = new LinkedHashMap<>();
    }

    /**
     * Records the output of a completed step.
     *
     * @param toolName the tool that produced the output
     * @param output   the JSON output from the tool
     */
    public void recordOutput(String toolName, String output) {
        stepOutputs.put(toolName, output);
    }

    /**
     * Retrieves the output from a previously executed step.
     *
     * @param toolName the tool whose output to retrieve
     * @return the output string, or null if not found
     */
    public String getOutput(String toolName) {
        return stepOutputs.get(toolName);
    }

    /**
     * Returns an unmodifiable view of all step outputs.
     */
    public Map<String, String> getAllOutputs() {
        return Collections.unmodifiableMap(stepOutputs);
    }

    /**
     * Returns the number of recorded step outputs.
     */
    public int size() {
        return stepOutputs.size();
    }
}
