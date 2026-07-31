package com.sourcekoza.careerpilot.jobagent.mission.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Request to create a new mission.
 *
 * @since Sprint-15
 */
public record MissionCreateRequest(

        @NotBlank(message = "Mission name is required")
        @Size(max = 200, message = "Name must not exceed 200 characters")
        String name,

        @NotBlank(message = "Keywords are required")
        @Size(max = 200, message = "Keywords must not exceed 200 characters")
        String keywords,

        String preferredTitle,
        String experienceLevel,
        String location,
        boolean remote,
        boolean hybrid,
        Integer salaryMin,
        String currency,
        String employmentType,
        List<String> platforms,
        String resumeId,
        String schedule,
        String timezone,
        String applyMode
) {
}
