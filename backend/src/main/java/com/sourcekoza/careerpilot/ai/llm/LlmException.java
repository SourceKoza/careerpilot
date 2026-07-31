package com.sourcekoza.careerpilot.ai.llm;

/**
 * Exception thrown when an LLM interaction fails.
 *
 * @since Sprint-15
 */
public class LlmException extends RuntimeException {

    public LlmException(String message, Throwable cause) {
        super(message, cause);
    }
}
