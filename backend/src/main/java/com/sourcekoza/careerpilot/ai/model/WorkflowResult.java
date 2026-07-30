package com.sourcekoza.careerpilot.ai.model;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * The aggregated result of a complete workflow execution.
 *
 * @param workflowName  the name of the executed workflow
 * @param status        final status of the workflow
 * @param stepResults   results of each step (in execution order)
 * @param totalDuration total time taken to execute all steps
 * @param startedAt     when execution began
 * @param completedAt   when execution ended
 */
public record WorkflowResult(
        String workflowName,
        WorkflowStatus status,
        List<StepResult> stepResults,
        Duration totalDuration,
        Instant startedAt,
        Instant completedAt
) {

    /**
     * Convenience method to check if the workflow completed successfully.
     */
    public boolean isSuccess() {
        return status == WorkflowStatus.COMPLETED;
    }

    /**
     * Returns the number of steps that executed successfully.
     */
    public long successCount() {
        return stepResults.stream().filter(StepResult::success).count();
    }

    /**
     * Returns the number of steps that failed.
     */
    public long failureCount() {
        return stepResults.stream().filter(r -> !r.success()).count();
    }
}
