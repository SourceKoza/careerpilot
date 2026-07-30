package com.sourcekoza.careerpilot.ai.agent;

/**
 * Identifies the type of AI agent.
 *
 * <p>Each agent type corresponds to a specific domain capability.
 * Future agents will add their types here.</p>
 */
public enum AgentType {

    /** System agent for health checks and infrastructure validation. */
    SYSTEM,

    /** Job search agent (future). */
    JOB_SEARCH,

    /** Resume tailoring agent (future). */
    RESUME_TAILORING,

    /** Application submission agent (future). */
    AUTO_APPLY
}
