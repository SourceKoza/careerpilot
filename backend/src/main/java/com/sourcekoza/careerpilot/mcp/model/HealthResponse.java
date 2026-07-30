package com.sourcekoza.careerpilot.mcp.model;

/**
 * Response model for the MCP Health Tool.
 *
 * @param status      application health status
 * @param application application name
 * @param version     application version
 * @param timestamp   current server timestamp in ISO-8601 format
 */
public record HealthResponse(
        String status,
        String application,
        String version,
        String timestamp
) {
}
