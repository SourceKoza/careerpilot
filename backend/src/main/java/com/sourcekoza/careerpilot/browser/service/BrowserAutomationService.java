package com.sourcekoza.careerpilot.browser.service;

import com.sourcekoza.careerpilot.browser.dto.BrowserNavigationRequest;
import com.sourcekoza.careerpilot.browser.dto.BrowserNavigationResponse;

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
 * @since Sprint-13
 */
public interface BrowserAutomationService {

    /**
     * Navigates a browser to the specified URL and returns page metadata.
     *
     * <p>Launches a browser, navigates to the target URL, waits for the
     * page to load, extracts metadata, and closes the browser cleanly.</p>
     *
     * @param request the navigation request containing the target URL
     * @return the navigation response with page metadata and timing
     * @throws com.sourcekoza.careerpilot.browser.exception.BrowserException
     *         if navigation fails for any reason
     */
    BrowserNavigationResponse navigate(BrowserNavigationRequest request);
}
