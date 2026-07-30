package com.sourcekoza.careerpilot.browser.model;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.PlaywrightException;
import com.sourcekoza.careerpilot.browser.exception.BrowserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Playwright-based implementation of {@link BrowserElement}.
 *
 * <p>Wraps a Playwright {@link Locator} behind the technology-agnostic
 * BrowserElement interface. All Playwright-specific details remain internal
 * to this class — consumers never see Locator, ElementHandle, or any
 * Playwright types.</p>
 *
 * <p>This class translates BrowserElement operations into Playwright
 * Locator calls and wraps Playwright exceptions into {@link BrowserException}.</p>
 *
 * @since Sprint-14
 */
public class PlaywrightBrowserElement implements BrowserElement {

    private static final Logger log = LoggerFactory.getLogger(PlaywrightBrowserElement.class);

    private final Locator locator;

    public PlaywrightBrowserElement(Locator locator) {
        this.locator = locator;
    }

    @Override
    public String text() {
        try {
            return locator.innerText();
        } catch (PlaywrightException ex) {
            throw new BrowserException("Failed to get text content: " + ex.getMessage(), ex);
        }
    }

    @Override
    public String html() {
        try {
            return locator.innerHTML();
        } catch (PlaywrightException ex) {
            throw new BrowserException("Failed to get inner HTML: " + ex.getMessage(), ex);
        }
    }

    @Override
    public String attribute(String name) {
        try {
            return locator.getAttribute(name);
        } catch (PlaywrightException ex) {
            throw new BrowserException(
                    "Failed to get attribute '" + name + "': " + ex.getMessage(), ex);
        }
    }

    @Override
    public void click() {
        try {
            locator.click();
        } catch (PlaywrightException ex) {
            throw new BrowserException("Failed to click element: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void type(String value) {
        try {
            locator.fill(value);
        } catch (PlaywrightException ex) {
            throw new BrowserException("Failed to type into element: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean isVisible() {
        try {
            return locator.isVisible();
        } catch (PlaywrightException ex) {
            log.debug("Visibility check failed, treating as not visible: {}", ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean exists() {
        try {
            return locator.count() > 0;
        } catch (PlaywrightException ex) {
            log.debug("Existence check failed, treating as not existing: {}", ex.getMessage());
            return false;
        }
    }

    @Override
    public BrowserElement querySelector(String selector) {
        try {
            Locator child = locator.locator(selector).first();
            if (child.count() == 0) {
                return null;
            }
            return new PlaywrightBrowserElement(child);
        } catch (PlaywrightException ex) {
            throw new BrowserException(
                    "Failed to query selector '" + selector + "': " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<BrowserElement> querySelectorAll(String selector) {
        try {
            Locator children = locator.locator(selector);
            int count = children.count();
            List<BrowserElement> elements = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                elements.add(new PlaywrightBrowserElement(children.nth(i)));
            }
            return elements;
        } catch (PlaywrightException ex) {
            throw new BrowserException(
                    "Failed to query all for selector '" + selector + "': " + ex.getMessage(), ex);
        }
    }
}
