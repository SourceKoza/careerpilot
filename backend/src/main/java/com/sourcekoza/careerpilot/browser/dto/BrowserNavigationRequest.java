package com.sourcekoza.careerpilot.browser.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Request model for browser navigation operations.
 *
 * @param url the target URL to navigate to (must be HTTP or HTTPS)
 */
public record BrowserNavigationRequest(

        @NotBlank(message = "URL is required")
        @Pattern(
                regexp = "^https?://.*",
                message = "Only HTTP and HTTPS protocols are supported"
        )
        String url
) {
}
