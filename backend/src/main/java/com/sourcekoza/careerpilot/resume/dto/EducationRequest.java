package com.sourcekoza.careerpilot.resume.dto;

import com.sourcekoza.careerpilot.resume.validation.ValidDateRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Request DTO for an education entry.
 */
@ValidDateRange(startDateField = "startDate", endDateField = "endDate")
public record EducationRequest(
    @NotBlank @Size(max = 150) String institution,
    @NotBlank @Size(max = 100) String degree,
    @Size(max = 100) String fieldOfStudy,
    @NotNull LocalDate startDate,
    LocalDate endDate,
    @Size(max = 20) String grade,
    @Size(max = 1000) String description
) {}
