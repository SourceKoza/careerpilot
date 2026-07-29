package com.sourcekoza.careerpilot.exception;

/**
 * Thrown when a business rule is violated.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
