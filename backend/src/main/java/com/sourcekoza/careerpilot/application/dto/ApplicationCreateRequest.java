package com.sourcekoza.careerpilot.application.dto;

import com.sourcekoza.careerpilot.application.domain.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Request DTO for creating a new job application.
 */
public record ApplicationCreateRequest(

    @NotNull(message = "Job ID is required")
    UUID jobId,

    @NotNull(message = "Resume version ID is required")
    UUID resumeVersionId,

    ApplicationStatus status,

    @Size(max = 2000, message = "Notes must not exceed 2000 characters")
    String notes,

    @Size(max = 200, message = "External application ID must not exceed 200 characters")
    String externalApplicationId
) {}
