package com.sourcekoza.careerpilot.resume.dto;

import com.sourcekoza.careerpilot.resume.domain.SkillProficiency;

import java.util.UUID;

/**
 * Response DTO for a skill entry.
 */
public record SkillResponse(
    UUID id,
    String name,
    SkillProficiency proficiency,
    String category
) {}
