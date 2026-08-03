package com.sourcekoza.careerpilot.mcp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * MCP Server configuration.
 *
 * <p>Enables MCP server properties. Tool registration will be added
 * when MCP tools are implemented in a future sprint.</p>
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
}
