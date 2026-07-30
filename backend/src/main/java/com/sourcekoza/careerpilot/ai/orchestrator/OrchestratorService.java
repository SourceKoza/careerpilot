package com.sourcekoza.careerpilot.ai.orchestrator;

import com.sourcekoza.careerpilot.ai.model.ExecutionContext;
import com.sourcekoza.careerpilot.ai.model.StepResult;
import com.sourcekoza.careerpilot.ai.model.WorkflowRequest;
import com.sourcekoza.careerpilot.ai.model.WorkflowResult;
import com.sourcekoza.careerpilot.ai.model.WorkflowStatus;
import com.sourcekoza.careerpilot.ai.model.WorkflowStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AI Orchestrator Service.
 *
 * <p>Coordinates workflow execution by invoking MCP tools in sequence.
 * Communicates exclusively through the MCP tool layer — never calls
 * repositories or business services directly.</p>
 *
 * <p>Steps are executed sequentially. If a step fails, execution halts
 * and a structured failure result is returned.</p>
 */
@Service
public class OrchestratorService {

    private static final Logger log = LoggerFactory.getLogger(OrchestratorService.class);

    private final Map<String, ToolCallback> toolRegistry;

    public OrchestratorService(ToolCallbackProvider toolCallbackProvider) {
        this.toolRegistry = Arrays.stream(toolCallbackProvider.getToolCallbacks())
                .collect(Collectors.toMap(
                        tc -> tc.getToolDefinition().name(),
                        tc -> tc
                ));
        log.info("OrchestratorService initialized with {} available tools: {}",
                toolRegistry.size(), toolRegistry.keySet());
    }

    /**
     * Executes a workflow by invoking each step's MCP tool in order.
     *
     * <p>Execution stops on the first failure. Results are aggregated
     * into a single {@link WorkflowResult}.</p>
     *
     * @param request the workflow to execute
     * @return the aggregated workflow result
     */
    public WorkflowResult execute(WorkflowRequest request) {
        log.info("Workflow started: name='{}', steps={}", request.workflowName(), request.steps().size());
        Instant startedAt = Instant.now();

        List<StepResult> stepResults = new ArrayList<>();
        ExecutionContext context = new ExecutionContext();
        WorkflowStatus finalStatus = WorkflowStatus.RUNNING;

        for (WorkflowStep step : request.steps()) {
            StepResult result = executeStep(step);
            stepResults.add(result);

            if (result.success()) {
                context.recordOutput(step.toolName(), result.output());
            } else {
                finalStatus = WorkflowStatus.FAILED;
                log.warn("Workflow '{}' failed at step '{}': {}",
                        request.workflowName(), step.toolName(), result.output());
                break;
            }
        }

        if (finalStatus != WorkflowStatus.FAILED) {
            finalStatus = WorkflowStatus.COMPLETED;
        }

        Instant completedAt = Instant.now();
        Duration totalDuration = Duration.between(startedAt, completedAt);

        WorkflowResult workflowResult = new WorkflowResult(
                request.workflowName(),
                finalStatus,
                List.copyOf(stepResults),
                totalDuration,
                startedAt,
                completedAt
        );

        log.info("Workflow completed: name='{}', status={}, steps={}/{}, duration={}ms",
                request.workflowName(), finalStatus,
                workflowResult.successCount(), request.steps().size(),
                totalDuration.toMillis());

        return workflowResult;
    }

    /**
     * Returns the names of all available MCP tools.
     */
    public List<String> getAvailableTools() {
        return List.copyOf(toolRegistry.keySet());
    }

    private StepResult executeStep(WorkflowStep step) {
        log.debug("Executing step: tool='{}', args='{}'", step.toolName(), step.arguments());
        Instant stepStart = Instant.now();

        ToolCallback tool = toolRegistry.get(step.toolName());
        if (tool == null) {
            Duration duration = Duration.between(stepStart, Instant.now());
            String error = String.format("Tool not found: '%s'", step.toolName());
            log.error(error);
            return StepResult.failure(step.toolName(), error, duration);
        }

        try {
            String output = tool.call(step.arguments());
            Duration duration = Duration.between(stepStart, Instant.now());
            log.debug("Step completed: tool='{}', duration={}ms", step.toolName(), duration.toMillis());
            return StepResult.success(step.toolName(), output, duration);
        } catch (Exception e) {
            Duration duration = Duration.between(stepStart, Instant.now());
            String error = String.format("Tool execution failed: %s", e.getMessage());
            log.error("Step failed: tool='{}', error='{}', duration={}ms",
                    step.toolName(), e.getMessage(), duration.toMillis());
            return StepResult.failure(step.toolName(), error, duration);
        }
    }
}
