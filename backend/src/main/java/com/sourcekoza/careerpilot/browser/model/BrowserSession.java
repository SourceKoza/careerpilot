package com.sourcekoza.careerpilot.browser.model;

import java.util.List;

/**
 * Represents an active browser session with interaction and DOM inspection capabilities.
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
 *   querySelector() / querySelectorAll()
 *         ↓
 *   BrowserElement — text() / html() / attribute() / click() / type()
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

    // --- DOM Inspection Methods (Sprint-14) ---

    /**
     * Finds the first element on the page matching the given CSS selector.
     *
     * <p>Returns a {@link BrowserElement} abstraction that allows inspecting
     * the element's text, HTML, attributes, and child elements without
     * exposing the underlying browser engine.</p>
     *
     * @param selector CSS selector to match
     * @return the first matching element, or null if not found
     * @since Sprint-14
     */
    BrowserElement querySelector(String selector);

    /**
     * Finds all elements on the page matching the given CSS selector.
     *
     * <p>Returns a list of {@link BrowserElement} abstractions. Each element
     * can be further queried for child elements, text, attributes, etc.</p>
     *
     * <p>Usage example:</p>
     * <pre>
     * List&lt;BrowserElement&gt; cards = session.querySelectorAll(".job-card");
     * for (BrowserElement card : cards) {
     *     String title = card.querySelector(".title").text();
     *     String company = card.querySelector(".company").text();
     *     String url = card.querySelector("a").attribute("href");
     * }
     * </pre>
     *
     * @param selector CSS selector to match
     * @return list of matching elements (may be empty, never null)
     * @since Sprint-14
     */
    List<BrowserElement> querySelectorAll(String selector);

    /**
     * Checks whether an element matching the selector exists on the page.
     *
     * <p>This is a convenience method equivalent to checking if
     * {@code querySelector(selector) != null}, but may be more efficient
     * as it avoids constructing a full BrowserElement.</p>
     *
     * @param selector CSS selector to check
     * @return true if at least one matching element exists
     * @since Sprint-14
     */
    boolean exists(String selector);

    /**
     * Closes the browser session and releases all resources.
     */
    @Override
    void close();
}
