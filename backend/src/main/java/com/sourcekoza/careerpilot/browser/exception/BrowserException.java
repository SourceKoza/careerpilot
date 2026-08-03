package com.sourcekoza.careerpilot.browser.exception;

/**
 * Exception thrown when a browser automation operation fails.
 *
 * <p>Wraps implementation-specific exceptions (e.g. Playwright errors)
 * into an application-level exception. Handled by the GlobalExceptionHandler.</p>
 */
public class BrowserException extends RuntimeException {

    public BrowserException(String message) {
        super(message);
    }

    public BrowserException(String message, Throwable cause) {
        super(message, cause);
    }
}
