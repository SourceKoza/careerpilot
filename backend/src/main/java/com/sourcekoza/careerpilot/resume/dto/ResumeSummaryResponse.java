package com.sourcekoza.careerpilot.resume.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * Lightweight response DTO for resume listings — no child collections.
 */
public record ResumeSummaryResponse(
    UUID id,
    String title,
    String summary,
    String targetRole,
    Instant createdAt,
    Instant updatedAt
) {}
