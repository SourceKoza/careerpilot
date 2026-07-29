package com.sourcekoza.careerpilot.resume.dto;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Response DTO for a project entry.
 */
public record ProjectResponse(
    UUID id,
    String name,
    String description,
    String technologiesUsed,
    String projectUrl,
    LocalDate startDate,
    LocalDate endDate
) {}
