package com.sourcekoza.careerpilot.mission.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Request DTO for changing mission apply mode.
 *
 * @since Sprint-16
 */
public record ApplyModeRequest(
        @NotNull(message = "Apply mode is required")
        @Pattern(regexp = "SEMI_AUTO|FULL_AUTO", message = "Apply mode must be SEMI_AUTO or FULL_AUTO")
        String applyMode
) {
}
