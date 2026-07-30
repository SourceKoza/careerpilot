package com.sourcekoza.careerpilot.browser.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents an active browser session.
 *
 * <p>Encapsulates session metadata for tracking browser lifecycle.
 * This model is implementation-agnostic and does not expose Playwright internals.</p>
 */
public class BrowserSession {

    private final String sessionId;
    private final Instant createdAt;
    private boolean active;

    public BrowserSession() {
        this.sessionId = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.active = true;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void close() {
        this.active = false;
    }
}
