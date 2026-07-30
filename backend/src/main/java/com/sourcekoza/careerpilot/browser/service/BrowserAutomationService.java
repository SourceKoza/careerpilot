package com.sourcekoza.careerpilot.browser.service;

import com.sourcekoza.careerpilot.browser.dto.BrowserNavigationRequest;
import com.sourcekoza.careerpilot.browser.dto.BrowserNavigationResponse;
import com.sourcekoza.careerpilot.browser.model.BrowserSession;

/**
 * Application service interface for browser automation operations.
 *
 * <p>Provides a technology-agnostic abstraction over browser automation.
 * The implementation detail (Playwright, Selenium, etc.) is completely
 * hidden behind this interface.</p>
 *
 * <p>Per ADR-006, internal AI Agents communicate directly with this
 * Application Service. They must NOT call REST APIs, MCP Tools,
 * Controllers, or Repositories directly.</p>
 *
 * <p>Two usage patterns are supported:</p>
 * <ul>
 *   <li><strong>Simple navigation</strong>: Use {@link #navigate(BrowserNavigationRequest)}
 *       for one-shot navigation with automatic session lifecycle.</li>
 *   <li><strong>Session-based</strong>: Use {@link #createSession()} for multi-step
 *       interactions (navigate, click, type, wait). Callers must close the session.</li>
 * </ul>
 *
 * @since Sprint-13
 */
public interface BrowserAutomationService {

    /**
     * Creates a new browser session for multi-step interactions.
     *
     * <p>The caller is responsible for closing the session after use.
     * Use try-with-resources for automatic cleanup:</p>
     * <pre>
     * try (BrowserSession session = browserAutomationService.createSession()) {
     *     session.navigate("https://example.com");
     *     session.click(".search-btn");
     *     session.type("#query", "java developer");
     *     String title = session.getPageTitle();
     * }
     * </pre>
     *
     * @return a new active browser session
     * @throws com.sourcekoza.careerpilot.browser.exception.BrowserException
     *         if the browser cannot be launched
     */
    BrowserSession createSession();

    /**
     * Navigates a browser to the specified URL and returns page metadata.
     *
     * <p>Convenience method that creates a session, navigates, extracts
     * metadata, and closes the session automatically.</p>
     *
     * @param request the navigation request containing the target URL
     * @return the navigation response with page metadata and timing
     * @throws com.sourcekoza.careerpilot.browser.exception.BrowserException
     *         if navigation fails for any reason
     */
    BrowserNavigationResponse navigate(BrowserNavigationRequest request);
}
