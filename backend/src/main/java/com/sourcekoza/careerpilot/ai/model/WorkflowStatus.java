package com.sourcekoza.careerpilot.ai.model;

/**
 * Represents the possible states of a workflow execution.
 */
public enum WorkflowStatus {

    /** Workflow has been created but not yet started. */
    PENDING,

    /** Workflow is currently executing steps. */
    RUNNING,

    /** All steps completed successfully. */
    COMPLETED,

    /** One or more steps failed; execution was halted. */
    FAILED
}
