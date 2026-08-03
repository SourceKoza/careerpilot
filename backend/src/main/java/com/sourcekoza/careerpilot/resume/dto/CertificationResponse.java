package com.sourcekoza.careerpilot.resume.dto;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Response DTO for a certification entry.
 */
public record CertificationResponse(
    UUID id,
    String name,
    String issuingOrganization,
    LocalDate issueDate,
    LocalDate expiryDate,
    String credentialId,
    String credentialUrl
) {}
