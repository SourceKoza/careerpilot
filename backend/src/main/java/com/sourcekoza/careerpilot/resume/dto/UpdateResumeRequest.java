package com.sourcekoza.careerpilot.resume.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

/**
 * Request DTO for updating an existing resume.
 * Same shape as {@link CreateResumeRequest}.
 */
public record UpdateResumeRequest(
    @NotBlank @Size(max = 100) String title,
    @Size(max = 500) String summary,
    @Size(max = 100) String targetRole,
    @Valid List<ExperienceRequest> experiences,
    @Valid List<EducationRequest> educations,
    @Valid Set<SkillRequest> skills,
    @Valid Set<CertificationRequest> certifications,
    @Valid List<ProjectRequest> projects,
    @Valid Set<LanguageRequest> languages
) {}
