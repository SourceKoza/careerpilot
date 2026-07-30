package com.sourcekoza.careerpilot.browser.dto;

/**
 * Response model for browser navigation operations.
 *
 * @param success       whether navigation completed successfully
 * @param finalUrl      the final URL after navigation (may differ from request due to redirects)
 * @param pageTitle     the title of the loaded page
 * @param executionTime time taken for the navigation in milliseconds
 */
public record BrowserNavigationResponse(
        boolean success,
        String finalUrl,
        String pageTitle,
        long executionTime
) {
}
