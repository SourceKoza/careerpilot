package com.sourcekoza.careerpilot.mcp.tools;

import com.sourcekoza.careerpilot.mcp.config.McpServerProperties;
import com.sourcekoza.careerpilot.mcp.model.HealthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * MCP Health Tool.
 *
 * <p>Returns the current application status, version, and timestamp.
 * Used to verify that the MCP Server is running and responsive.</p>
 */
@Service
public class HealthTool {

    private static final Logger log = LoggerFactory.getLogger(HealthTool.class);

    private final McpServerProperties properties;

    public HealthTool(McpServerProperties properties) {
        this.properties = properties;
        log.info("MCP HealthTool initialized");
    }

    @Tool(description = "Returns application health status, version, and current timestamp")
    public HealthResponse getHealth() {
        log.debug("MCP HealthTool invoked");

        HealthResponse response = new HealthResponse(
                "UP",
                "CareerPilot AI",
                properties.version(),
                Instant.now().toString()
        );

        log.debug("MCP HealthTool response: status={}", response.status());
        return response;
    }
}
