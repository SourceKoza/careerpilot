package com.sourcekoza.careerpilot.resume.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * Response DTO for a resume version.
 */
public record ResumeVersionResponse(
    UUID id,
    UUID resumeId,
    Integer versionNumber,
    String markdownContent,
    String pdfPath,
    String changeSummary,
    Instant createdAt
) {}
