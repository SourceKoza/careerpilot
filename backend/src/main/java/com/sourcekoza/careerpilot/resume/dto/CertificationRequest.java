package com.sourcekoza.careerpilot.resume.dto;

import com.sourcekoza.careerpilot.resume.validation.ValidDateRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Request DTO for a certification entry.
 */
@ValidDateRange(startDateField = "issueDate", endDateField = "expiryDate")
public record CertificationRequest(
    @NotBlank @Size(max = 150) String name,
    @NotBlank @Size(max = 100) String issuingOrganization,
    LocalDate issueDate,
    LocalDate expiryDate,
    @Size(max = 100) String credentialId,
    @Size(max = 500) String credentialUrl
) {}
