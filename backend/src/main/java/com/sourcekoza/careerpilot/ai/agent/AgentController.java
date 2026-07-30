package com.sourcekoza.careerpilot.ai.agent;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * REST controller for AI Agent operations.
 *
 * <p>Exposes endpoints to invoke agents and inspect their capabilities.</p>
 */
@RestController
@RequestMapping("/api/v1/agents")
@Tag(name = "AI Agents", description = "AI Agent invocation and management")
public class AgentController {

    private final SystemAgent systemAgent;

    public AgentController(SystemAgent systemAgent) {
        this.systemAgent = systemAgent;
    }

    @PostMapping("/system/health-check")
    @Operation(summary = "Execute system health check",
            description = "Invokes the SystemAgent to validate the end-to-end execution chain: Agent → Orchestrator → MCP Tool → Service")
    public ResponseEntity<AgentResponse> executeSystemHealthCheck(
            @RequestParam(defaultValue = "System Agent") String name) {
        AgentRequest request = AgentRequest.of(
                AgentType.SYSTEM, "health-check", Map.of("name", name));
        AgentResponse response = systemAgent.execute(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/system/actions")
    @Operation(summary = "List SystemAgent actions",
            description = "Returns the list of actions supported by the SystemAgent")
    public ResponseEntity<List<String>> listSystemActions() {
        return ResponseEntity.ok(systemAgent.getSupportedActions());
    }
}
