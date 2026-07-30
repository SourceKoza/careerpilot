package com.sourcekoza.careerpilot.browser.model;

/**
 * Represents an active browser session with interaction capabilities.
 *
 * <p>Provides a technology-agnostic interface for browser interactions.
 * Implementations hide the underlying browser engine (Playwright, Selenium, etc.).</p>
 *
 * <p>Sessions are created by {@link com.sourcekoza.careerpilot.browser.service.BrowserAutomationService}
 * and must be closed after use to release browser resources.</p>
 *
 * <p>Lifecycle:</p>
 * <pre>
 * BrowserAutomationService
 *         ↓
 *   BrowserSession (created)
 *         ↓
 *   navigate() / click() / type() / waitFor()
 *         ↓
 *   getPageTitle() / getCurrentUrl()
 *         ↓
 *   close()
 * </pre>
 *
 * @since Sprint-13
 */
public interface BrowserSession extends AutoCloseable {

    /**
     * Returns the unique identifier for this session.
     */
    String getSessionId();

    /**
     * Navigates to the specified URL and waits for page load.
     *
     * @param url the target URL (must be HTTP or HTTPS)
     */
    void navigate(String url);

    /**
     * Clicks an element identified by the given selector.
     *
     * @param selector CSS or other selector identifying the target element
     */
    void click(String selector);

    /**
     * Types text into an element identified by the given selector.
     *
     * @param selector CSS or other selector identifying the target input element
     * @param text     the text to type
     */
    void type(String selector, String text);

    /**
     * Waits for an element matching the selector to appear on the page.
     *
     * @param selector CSS or other selector to wait for
     */
    void waitFor(String selector);

    /**
     * Returns the title of the current page.
     *
     * @return the page title
     */
    String getPageTitle();

    /**
     * Returns the current URL of the page (may differ from navigation target due to redirects).
     *
     * @return the current URL
     */
    String getCurrentUrl();

    /**
     * Returns whether this session is still active.
     *
     * @return true if the session has not been closed
     */
    boolean isActive();

    /**
     * Closes the browser session and releases all resources.
     */
    @Override
    void close();
}
