package com.sourcekoza.careerpilot.mission.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for regenerating a tailored resume with user feedback.
 *
 * @since Sprint-16
 */
public record RegenerateRequest(
        @NotBlank(message = "Feedback is required")
        @Size(max = 1000, message = "Feedback must be under 1000 characters")
        String feedback
) {
}
