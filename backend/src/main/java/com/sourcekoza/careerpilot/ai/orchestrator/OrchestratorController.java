package com.sourcekoza.careerpilot.ai.orchestrator;

import com.sourcekoza.careerpilot.ai.model.WorkflowResult;
import com.sourcekoza.careerpilot.ai.workflow.HealthCheckWorkflow;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for AI Orchestrator operations.
 *
 * <p>Provides endpoints to trigger workflow execution and
 * inspect available tools.</p>
 */
@RestController
@RequestMapping("/api/v1/orchestrator")
@Tag(name = "AI Orchestrator", description = "Workflow orchestration and tool management")
public class OrchestratorController {

    private final OrchestratorService orchestratorService;
    private final HealthCheckWorkflow healthCheckWorkflow;

    public OrchestratorController(OrchestratorService orchestratorService,
                                  HealthCheckWorkflow healthCheckWorkflow) {
        this.orchestratorService = orchestratorService;
        this.healthCheckWorkflow = healthCheckWorkflow;
    }

    @GetMapping("/tools")
    @Operation(summary = "List available MCP tools",
            description = "Returns the names of all MCP tools available to the orchestrator")
    public ResponseEntity<List<String>> listTools() {
        return ResponseEntity.ok(orchestratorService.getAvailableTools());
    }

    @GetMapping("/workflows/health-check")
    @Operation(summary = "Execute health check workflow",
            description = "Runs the Health and Greeting tools in sequence to validate orchestration")
    public ResponseEntity<WorkflowResult> executeHealthCheck(
            @RequestParam(defaultValue = "AI Agent") String name) {
        WorkflowResult result = healthCheckWorkflow.execute(name);
        return ResponseEntity.ok(result);
    }
}
