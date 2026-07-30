package com.sourcekoza.careerpilot.application.dto;

import com.sourcekoza.careerpilot.application.domain.ApplicationStatus;

import java.time.Instant;
import java.util.UUID;

/**
 * Full response DTO for a job application.
 */
public record ApplicationResponse(
    UUID id,
    UUID userId,
    UUID jobId,
    String jobTitle,
    String companyName,
    UUID resumeVersionId,
    ApplicationStatus status,
    Instant appliedAt,
    String notes,
    String externalApplicationId,
    Instant createdAt,
    Instant updatedAt
) {}
