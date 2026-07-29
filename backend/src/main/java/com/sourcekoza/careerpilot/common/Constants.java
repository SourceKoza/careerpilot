package com.sourcekoza.careerpilot.common;

/**
 * Application-wide constants.
 *
 * <p>Centralised location for magic values used across multiple modules.</p>
 */
public final class Constants {

    private Constants() {
        // Prevent instantiation
    }

    // --- Pagination Defaults ---
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    // --- API Versioning ---
    public static final String API_V1 = "/api/v1";

    // --- Validation ---
    public static final int EMAIL_MAX_LENGTH = 255;
    public static final int NAME_MAX_LENGTH = 100;
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 128;
}
