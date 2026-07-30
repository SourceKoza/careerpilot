package com.sourcekoza.careerpilot.resume.dto;

import com.sourcekoza.careerpilot.resume.validation.ValidDateRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Request DTO for a work experience entry.
 */
@ValidDateRange(startDateField = "startDate", endDateField = "endDate")
public record ExperienceRequest(
    @NotBlank @Size(max = 100) String companyName,
    @NotBlank @Size(max = 100) String position,
    @Size(max = 100) String location,
    @NotNull LocalDate startDate,
    LocalDate endDate,
    boolean currentlyWorking,
    @Size(max = 2000) String description
) {}
