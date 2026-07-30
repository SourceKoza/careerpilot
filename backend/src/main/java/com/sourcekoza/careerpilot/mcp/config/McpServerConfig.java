package com.sourcekoza.careerpilot.mcp.config;

import com.sourcekoza.careerpilot.mcp.tools.GreetingTool;
import com.sourcekoza.careerpilot.mcp.tools.HealthTool;
import com.sourcekoza.careerpilot.mcp.tools.JobApplicationTool;
import com.sourcekoza.careerpilot.mcp.tools.JobTool;
import com.sourcekoza.careerpilot.mcp.tools.ResumeTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MCP Server configuration.
 *
 * <p>Enables MCP server properties and registers MCP tools via
 * {@link ToolCallbackProvider}. The Spring AI MCP Server auto-configuration
 * picks up these beans and exposes them as discoverable MCP tools.</p>
 */
@Configuration
@EnableConfigurationProperties(McpServerProperties.class)
public class McpServerConfig {

    private static final Logger log = LoggerFactory.getLogger(McpServerConfig.class);

    private final McpServerProperties properties;

    public McpServerConfig(McpServerProperties properties) {
        this.properties = properties;
        log.info("MCP Server configured: name={}, version={}", properties.name(), properties.version());
    }

    /**
     * Registers MCP tools with the MCP Server.
     *
     * <p>The auto-configuration will detect this bean and register
     * all {@code @Tool} annotated methods as MCP tools.</p>
     */
    @Bean
    public ToolCallbackProvider mcpToolCallbackProvider(
            HealthTool healthTool,
            GreetingTool greetingTool,
            ResumeTool resumeTool,
            JobTool jobTool,
            JobApplicationTool jobApplicationTool) {
        log.info("Registering MCP tools: health, greeting, resume, job, jobApplication");
        return MethodToolCallbackProvider.builder()
                .toolObjects(healthTool, greetingTool, resumeTool, jobTool, jobApplicationTool)
                .build();
    }
}
