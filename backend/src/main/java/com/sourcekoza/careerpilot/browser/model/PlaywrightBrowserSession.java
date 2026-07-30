package com.sourcekoza.careerpilot.browser.model;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;
import com.sourcekoza.careerpilot.browser.exception.BrowserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Playwright-based implementation of {@link BrowserSession}.
 *
 * <p>Manages the lifecycle of a single Playwright browser instance.
 * This class is package-private to the browser module — consumers
 * interact only through the {@link BrowserSession} interface.</p>
 *
 * @since Sprint-13
 */
public class PlaywrightBrowserSession implements BrowserSession {

    private static final Logger log = LoggerFactory.getLogger(PlaywrightBrowserSession.class);

    private final String sessionId;
    private final Playwright playwright;
    private final Browser browser;
    private final Page page;
    private boolean active;

    public PlaywrightBrowserSession(Playwright playwright, Browser browser, Page page) {
        this.sessionId = UUID.randomUUID().toString();
        this.playwright = playwright;
        this.browser = browser;
        this.page = page;
        this.active = true;
        log.debug("Browser session created: sessionId={}", sessionId);
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public void navigate(String url) {
        ensureActive();
        try {
            log.debug("Session {} navigating to: {}", sessionId, url);
            page.navigate(url);
            page.waitForLoadState();
            log.debug("Session {} navigation complete: finalUrl={}", sessionId, page.url());
        } catch (PlaywrightException ex) {
            throw new BrowserException("Navigation failed: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void click(String selector) {
        ensureActive();
        try {
            log.debug("Session {} clicking: {}", sessionId, selector);
            page.click(selector);
        } catch (PlaywrightException ex) {
            throw new BrowserException("Click failed on selector '" + selector + "': " + ex.getMessage(), ex);
        }
    }

    @Override
    public void type(String selector, String text) {
        ensureActive();
        try {
            log.debug("Session {} typing into: {}", sessionId, selector);
            page.fill(selector, text);
        } catch (PlaywrightException ex) {
            throw new BrowserException("Type failed on selector '" + selector + "': " + ex.getMessage(), ex);
        }
    }

    @Override
    public void waitFor(String selector) {
        ensureActive();
        try {
            log.debug("Session {} waiting for: {}", sessionId, selector);
            page.waitForSelector(selector);
        } catch (PlaywrightException ex) {
            throw new BrowserException("Wait failed for selector '" + selector + "': " + ex.getMessage(), ex);
        }
    }

    @Override
    public String getPageTitle() {
        ensureActive();
        return page.title();
    }

    @Override
    public String getCurrentUrl() {
        ensureActive();
        return page.url();
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void close() {
        if (!active) {
            return;
        }
        active = false;
        try {
            browser.close();
            playwright.close();
            log.debug("Browser session closed: sessionId={}", sessionId);
        } catch (Exception ex) {
            log.warn("Error closing browser session {}: {}", sessionId, ex.getMessage());
        }
    }

    private void ensureActive() {
        if (!active) {
            throw new BrowserException("Browser session is closed: " + sessionId);
        }
    }
}
