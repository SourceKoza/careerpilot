package com.sourcekoza.careerpilot.ai.llm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * Service wrapping the Spring AI ChatClient for LLM interactions.
 *
 * <p>Provides a clean interface for all AI agents to call the local
 * Ollama LLM. This abstraction allows swapping the underlying model
 * (Ollama, OpenAI, Claude) without changing agent code.</p>
 *
 * @since Sprint-15
 */
@Service
public class LlmService {

    private static final Logger log = LoggerFactory.getLogger(LlmService.class);

    private final ChatClient chatClient;

    public LlmService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
        log.info("LlmService initialized with Ollama chat client");
    }

    /**
     * Sends a prompt to the LLM and returns the response text.
     *
     * @param systemPrompt the system/role instruction
     * @param userPrompt the user message
     * @return the LLM response text
     */
    public String chat(String systemPrompt, String userPrompt) {
        log.debug("LLM request: system='{}...', user='{}...'",
                truncate(systemPrompt, 50), truncate(userPrompt, 50));

        try {
            String response = chatClient.prompt()
                    .system(systemPrompt)
                    .user(userPrompt)
                    .call()
                    .content();

            log.debug("LLM response length: {} chars", response != null ? response.length() : 0);
            return response;
        } catch (Exception e) {
            log.error("LLM call failed: {}", e.getMessage());
            throw new LlmException("Failed to get LLM response: " + e.getMessage(), e);
        }
    }

    /**
     * Simple prompt without system instruction.
     */
    public String prompt(String userPrompt) {
        try {
            return chatClient.prompt()
                    .user(userPrompt)
                    .call()
                    .content();
        } catch (Exception e) {
            log.error("LLM prompt failed: {}", e.getMessage());
            throw new LlmException("Failed to get LLM response: " + e.getMessage(), e);
        }
    }

    private String truncate(String text, int maxLen) {
        if (text == null) return "";
        return text.length() > maxLen ? text.substring(0, maxLen) : text;
    }
}
