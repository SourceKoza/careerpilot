package com.sourcekoza.careerpilot.resume.dto;

import com.sourcekoza.careerpilot.resume.domain.LanguageProficiency;

import java.util.UUID;

/**
 * Response DTO for a language proficiency entry.
 */
public record LanguageResponse(
    UUID id,
    String name,
    LanguageProficiency proficiency
) {}
