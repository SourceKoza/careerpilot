package com.sourcekoza.careerpilot.ai.agent;

import com.sourcekoza.careerpilot.ai.model.WorkflowRequest;
import com.sourcekoza.careerpilot.ai.model.WorkflowResult;
import com.sourcekoza.careerpilot.ai.model.WorkflowStep;
import com.sourcekoza.careerpilot.ai.orchestrator.OrchestratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * System Agent — validates the end-to-end execution chain.
 *
 * <p>This demonstration agent executes the Health Check workflow through
 * the AI Orchestrator. It verifies the complete path:
 * Agent → Orchestrator → MCP Tool → Service.</p>
 *
 * <p>Supported actions:</p>
 * <ul>
 *   <li>{@code health-check} — executes health and greeting tools.</li>
 * </ul>
 */
@Component
public class SystemAgent implements Agent {

    private static final Logger log = LoggerFactory.getLogger(SystemAgent.class);

    private static final String ACTION_HEALTH_CHECK = "health-check";
    private static final List<String> SUPPORTED_ACTIONS = List.of(ACTION_HEALTH_CHECK);

    private final OrchestratorService orchestratorService;

    public SystemAgent(OrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
        log.info("SystemAgent initialized");
    }

    @Override
    public AgentType getAgentType() {
        return AgentType.SYSTEM;
    }

    @Override
    public List<String> getSupportedActions() {
        return SUPPORTED_ACTIONS;
    }

    @Override
    public AgentResponse execute(AgentRequest request) {
        log.info("SystemAgent executing: action='{}'", request.action());
        Instant startedAt = Instant.now();

        if (!SUPPORTED_ACTIONS.contains(request.action())) {
            log.warn("SystemAgent: unsupported action '{}'", request.action());
            return AgentResponse.validationFailure(
                    AgentType.SYSTEM, request.action(),
                    String.format("Unsupported action: '%s'. Supported: %s", request.action(), SUPPORTED_ACTIONS),
                    startedAt);
        }

        return switch (request.action()) {
            case ACTION_HEALTH_CHECK -> executeHealthCheck(request, startedAt);
            default -> AgentResponse.validationFailure(
                    AgentType.SYSTEM, request.action(), "Unknown action", startedAt);
        };
    }

    private AgentResponse executeHealthCheck(AgentRequest request, Instant startedAt) {
        String name = request.getParameter("name", "System Agent");

        List<WorkflowStep> steps = List.of(
                WorkflowStep.of("getHealth"),
                WorkflowStep.of("greet", String.format("{\"name\":\"%s\"}", name))
        );

        WorkflowRequest workflowRequest = WorkflowRequest.of("system-health-check", steps);
        WorkflowResult workflowResult = orchestratorService.execute(workflowRequest);

        Duration duration = Duration.between(startedAt, Instant.now());

        if (workflowResult.isSuccess()) {
            log.info("SystemAgent health-check completed successfully: duration={}ms", duration.toMillis());
            return AgentResponse.success(
                    AgentType.SYSTEM, ACTION_HEALTH_CHECK,
                    "Health check completed successfully. All systems operational.",
                    workflowResult, duration, startedAt);
        } else {
            log.warn("SystemAgent health-check failed: duration={}ms", duration.toMillis());
            return AgentResponse.failure(
                    AgentType.SYSTEM, ACTION_HEALTH_CHECK,
                    "Health check failed. See workflow result for details.",
                    workflowResult, duration, startedAt);
        }
    }
}
