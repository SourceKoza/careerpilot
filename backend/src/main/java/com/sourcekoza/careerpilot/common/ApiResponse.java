package com.sourcekoza.careerpilot.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

/**
 * Standard API response wrapper providing consistent response structure.
 *
 * <p>All successful API responses should use this model for consistency.</p>
 *
 * @param <T> the type of the response data payload
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        Instant timestamp
) {

    /**
     * Creates a successful response with data.
     *
     * @param data    the response payload
     * @param message a human-readable success message
     * @param <T>     the type of the data
     * @return a successful ApiResponse
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data, Instant.now());
    }

    /**
     * Creates a successful response with data and a default message.
     *
     * @param data the response payload
     * @param <T>  the type of the data
     * @return a successful ApiResponse
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data, Instant.now());
    }

    /**
     * Creates a successful response with no data.
     *
     * @param message a human-readable success message
     * @return a successful ApiResponse with null data
     */
    public static <Void> ApiResponse<Void> success(String message) {
        return new ApiResponse<>(true, message, null, Instant.now());
    }

    /**
     * Creates an error response.
     *
     * @param message a human-readable error message
     * @param <T>     the expected data type
     * @return an error ApiResponse
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, Instant.now());
    }
}
