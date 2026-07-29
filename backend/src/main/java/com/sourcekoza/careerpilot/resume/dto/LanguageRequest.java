package com.sourcekoza.careerpilot.resume.dto;

import com.sourcekoza.careerpilot.resume.domain.LanguageProficiency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for a language proficiency entry.
 */
public record LanguageRequest(
    @NotBlank @Size(max = 50) String name,
    @NotNull LanguageProficiency proficiency
) {}
