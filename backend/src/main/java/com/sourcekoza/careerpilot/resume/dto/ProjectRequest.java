package com.sourcekoza.careerpilot.resume.dto;

import com.sourcekoza.careerpilot.resume.validation.ValidDateRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Request DTO for a project entry.
 */
@ValidDateRange(startDateField = "startDate", endDateField = "endDate")
public record ProjectRequest(
    @NotBlank @Size(max = 100) String name,
    @Size(max = 1500) String description,
    @Size(max = 300) String technologiesUsed,
    @Size(max = 500) String projectUrl,
    LocalDate startDate,
    LocalDate endDate
) {}
