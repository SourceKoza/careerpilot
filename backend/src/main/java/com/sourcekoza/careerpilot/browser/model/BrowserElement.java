package com.sourcekoza.careerpilot.browser.model;

import java.util.List;

/**
 * Technology-agnostic abstraction representing a single DOM element.
 *
 * <p>Provides a clean interface for interacting with page elements without
 * exposing the underlying browser engine (Playwright Locator, Selenium
 * WebElement, etc.).</p>
 *
 * <p>Consumers (e.g. JobSite implementations) use this interface to inspect
 * and interact with elements found via {@link BrowserSession#querySelector(String)}
 * or {@link BrowserSession#querySelectorAll(String)}.</p>
 *
 * <p>Usage:</p>
 * <pre>
 * BrowserElement card = session.querySelector(".job-card");
 * String title = card.text();
 * String href = card.attribute("href");
 *
 * List&lt;BrowserElement&gt; cards = session.querySelectorAll(".job-card");
 * for (BrowserElement c : cards) {
 *     String company = c.querySelector(".company").text();
 * }
 * </pre>
 *
 * @since Sprint-14
 */
public interface BrowserElement {

    /**
     * Returns the visible text content of this element.
     *
     * @return the text content, or empty string if none
     */
    String text();

    /**
     * Returns the inner HTML of this element.
     *
     * @return the inner HTML string
     */
    String html();

    /**
     * Returns the value of the specified attribute.
     *
     * @param name the attribute name (e.g. "href", "class", "data-id")
     * @return the attribute value, or null if not present
     */
    String attribute(String name);

    /**
     * Clicks this element.
     */
    void click();

    /**
     * Types text into this element (clears existing content first).
     *
     * @param value the text to type
     */
    void type(String value);

    /**
     * Returns whether this element is visible on the page.
     *
     * @return true if the element is visible
     */
    boolean isVisible();

    /**
     * Returns whether this element exists in the DOM.
     *
     * <p>An element may exist but not be visible. Use {@link #isVisible()}
     * to check visibility separately.</p>
     *
     * @return true if the element is present in the DOM
     */
    boolean exists();

    /**
     * Finds the first child element matching the given CSS selector.
     *
     * @param selector CSS selector relative to this element
     * @return the matching element, or null if not found
     */
    BrowserElement querySelector(String selector);

    /**
     * Finds all child elements matching the given CSS selector.
     *
     * @param selector CSS selector relative to this element
     * @return list of matching elements (may be empty)
     */
    List<BrowserElement> querySelectorAll(String selector);
}
