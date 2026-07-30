package com.sourcekoza.careerpilot.mcp.model;

/**
 * Response model for the MCP Greeting Tool.
 *
 * @param message     the personalized greeting message
 * @param application the application name
 */
public record GreetingResponse(
        String message,
        String application
) {
}
