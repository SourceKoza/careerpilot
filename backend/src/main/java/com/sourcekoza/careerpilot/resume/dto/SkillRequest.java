package com.sourcekoza.careerpilot.resume.dto;

import com.sourcekoza.careerpilot.resume.domain.SkillProficiency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for a skill entry.
 */
public record SkillRequest(
    @NotBlank @Size(max = 50) String name,
    @NotNull SkillProficiency proficiency,
    @Size(max = 50) String category
) {}
