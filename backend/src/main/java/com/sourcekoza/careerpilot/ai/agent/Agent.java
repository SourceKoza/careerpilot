package com.sourcekoza.careerpilot.ai.agent;

/**
 * Generic AI Agent interface.
 *
 * <p>All AI agents must implement this interface. An agent receives a request,
 * builds a workflow, delegates execution to the AI Orchestrator, and returns
 * a structured response.</p>
 *
 * <p>Agents must never:</p>
 * <ul>
 *   <li>Call MCP tools directly.</li>
 *   <li>Call business services directly.</li>
 *   <li>Access repositories directly.</li>
 * </ul>
 *
 * <p>All execution flows through: Agent → Orchestrator → MCP Tools → Services.</p>
 */
public interface Agent {

    /**
     * Returns the agent type.
     */
    AgentType getAgentType();

    /**
     * Executes the agent with the given request.
     *
     * @param request the agent request containing action and parameters
     * @return the structured agent response
     */
    AgentResponse execute(AgentRequest request);

    /**
     * Returns the list of actions this agent supports.
     */
    java.util.List<String> getSupportedActions();
}
