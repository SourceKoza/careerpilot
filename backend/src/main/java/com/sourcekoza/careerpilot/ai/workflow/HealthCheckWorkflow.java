package com.sourcekoza.careerpilot.ai.workflow;

import com.sourcekoza.careerpilot.ai.model.WorkflowRequest;
import com.sourcekoza.careerpilot.ai.model.WorkflowResult;
import com.sourcekoza.careerpilot.ai.model.WorkflowStep;
import com.sourcekoza.careerpilot.ai.orchestrator.OrchestratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Health Check Workflow.
 *
 * <p>A demonstration workflow that validates the orchestration infrastructure
 * by executing the Health and Greeting MCP tools in sequence.</p>
 *
 * <p>Steps:</p>
 * <ol>
 *   <li>Execute Health Tool — verify system status.</li>
 *   <li>Execute Greeting Tool — verify tool communication.</li>
 *   <li>Aggregate results into a single workflow response.</li>
 * </ol>
 */
@Component
public class HealthCheckWorkflow {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckWorkflow.class);

    private static final String WORKFLOW_NAME = "health-check";

    private final OrchestratorService orchestratorService;

    public HealthCheckWorkflow(OrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
        log.info("HealthCheckWorkflow registered");
    }

    /**
     * Executes the health check workflow.
     *
     * @param greetingName the name to use for the greeting tool
     * @return the aggregated workflow result
     */
    public WorkflowResult execute(String greetingName) {
        log.info("HealthCheckWorkflow executing with greetingName='{}'", greetingName);

        List<WorkflowStep> steps = List.of(
                WorkflowStep.of("getHealth"),
                WorkflowStep.of("greet", String.format("{\"name\":\"%s\"}", greetingName))
        );

        WorkflowRequest request = WorkflowRequest.of(WORKFLOW_NAME, steps);
        return orchestratorService.execute(request);
    }
}
