package com.sourcekoza.careerpilot.mcp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Externalized configuration properties for the MCP Server.
 *
 * <p>Allows customization of MCP server metadata through
 * application configuration files.</p>
 */
@ConfigurationProperties(prefix = "app.mcp")
public record McpServerProperties(
        String name,
        String version
) {

    /**
     * Provides default values when properties are not explicitly configured.
     */
    public McpServerProperties {
        if (name == null || name.isBlank()) {
            name = "careerpilot-mcp-server";
        }
        if (version == null || version.isBlank()) {
            version = "0.1.0";
        }
    }
}
