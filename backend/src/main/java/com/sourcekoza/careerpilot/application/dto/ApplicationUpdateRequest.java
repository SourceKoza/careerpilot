package com.sourcekoza.careerpilot.application.dto;

import com.sourcekoza.careerpilot.application.domain.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating an existing job application.
 *
 * <p>Note: Job and ResumeVersion cannot be changed after creation.
 * Only status, notes, and externalApplicationId are updatable.</p>
 */
public record ApplicationUpdateRequest(

    @NotNull(message = "Status is required")
    ApplicationStatus status,

    @Size(max = 2000, message = "Notes must not exceed 2000 characters")
    String notes,

    @Size(max = 200, message = "External application ID must not exceed 200 characters")
    String externalApplicationId
) {}
