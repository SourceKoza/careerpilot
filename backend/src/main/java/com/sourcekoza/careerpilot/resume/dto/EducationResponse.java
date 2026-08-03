package com.sourcekoza.careerpilot.resume.dto;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Response DTO for an education entry.
 */
public record EducationResponse(
    UUID id,
    String institution,
    String degree,
    String fieldOfStudy,
    LocalDate startDate,
    LocalDate endDate,
    String grade,
    String description
) {}
