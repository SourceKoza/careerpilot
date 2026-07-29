package com.sourcekoza.careerpilot.resume.dto;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Full response DTO for a resume including all child sections.
 */
public record ResumeResponse(
    UUID id,
    UUID userId,
    String title,
    String summary,
    String targetRole,
    List<ExperienceResponse> experiences,
    List<EducationResponse> educations,
    Set<SkillResponse> skills,
    Set<CertificationResponse> certifications,
    List<ProjectResponse> projects,
    Set<LanguageResponse> languages,
    Instant createdAt,
    Instant updatedAt
) {}
