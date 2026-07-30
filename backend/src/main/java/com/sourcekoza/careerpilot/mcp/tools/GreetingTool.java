package com.sourcekoza.careerpilot.mcp.tools;

import com.sourcekoza.careerpilot.mcp.model.GreetingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

/**
 * MCP Greeting Tool.
 *
 * <p>A simple proof-of-concept tool used to verify that MCP communication
 * is working correctly. Returns a greeting message with the application name.</p>
 */
@Service
public class GreetingTool {

    private static final Logger log = LoggerFactory.getLogger(GreetingTool.class);

    private static final String APPLICATION_NAME = "CareerPilot AI";

    public GreetingTool() {
        log.info("MCP GreetingTool initialized");
    }

    @Tool(description = "Returns a personalized greeting message to verify MCP communication")
    public GreetingResponse greet(
            @ToolParam(description = "Name of the person to greet") String name) {
        log.debug("MCP GreetingTool invoked: name={}", name);

        String message = String.format("Hello, %s! Welcome to %s.", name, APPLICATION_NAME);

        GreetingResponse response = new GreetingResponse(message, APPLICATION_NAME);

        log.debug("MCP GreetingTool response: message={}", response.message());
        return response;
    }
}
