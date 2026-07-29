package com.sourcekoza.careerpilot.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

/**
 * Standardised error response returned by the API.
 *
 * <p>All error responses across the application follow this format for consistency.
 * Includes a traceId for request correlation in logs and observability tools.</p>
 *
 * @param status    HTTP status code
 * @param error     short error description
 * @param message   human-readable error message
 * @param path      request path that caused the error
 * @param timestamp time the error occurred
 * @param traceId   unique identifier for request tracing and correlation
 * @param errors    optional list of field-level validation errors
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        int status,
        String error,
        String message,
        String path,
        Instant timestamp,
        String traceId,
        List<FieldError> errors
) {

    /**
     * Field-level validation error detail.
     *
     * @param field   the field that failed validation
     * @param message the validation error message
     */
    public record FieldError(String field, String message) {
    }
}
