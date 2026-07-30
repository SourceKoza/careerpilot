package com.sourcekoza.careerpilot.application.dto;

import com.sourcekoza.careerpilot.application.domain.ApplicationStatus;

import java.time.Instant;
import java.util.UUID;

/**
 * Lightweight response DTO for application listings.
 */
public record ApplicationSummaryResponse(
    UUID id,
    UUID jobId,
    String jobTitle,
    String companyName,
    ApplicationStatus status,
    Instant appliedAt,
    Instant createdAt
) {}
