package com.sourcekoza.careerpilot.resume.dto;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Response DTO for a work experience entry.
 */
public record ExperienceResponse(
    UUID id,
    String companyName,
    String position,
    String location,
    LocalDate startDate,
    LocalDate endDate,
    boolean currentlyWorking,
    String description
) {}
