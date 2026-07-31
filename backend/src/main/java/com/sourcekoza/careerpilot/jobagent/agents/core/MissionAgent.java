package com.sourcekoza.careerpilot.jobagent.agents.core;

/**
 * Contract for all mission agents.
 *
 * <p>Every AI agent that participates in mission execution must implement
 * this interface. The MissionOrchestrator discovers and invokes agents
 * through this contract.</p>
 *
 * @since Sprint-15
 */
public interface MissionAgent {

    AgentType getType();

    AgentExecutionResult execute(MissionContext context);
}
