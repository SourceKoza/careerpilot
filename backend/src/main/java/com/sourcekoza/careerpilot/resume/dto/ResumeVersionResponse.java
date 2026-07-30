package com.sourcekoza.careerpilot.resume.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * Response DTO for a resume version history entry.
 */
public record ResumeVersionResponse(
    UUID id,
    Integer versionNumber,
    String changeSummary,
    Instant createdAt
) {}
