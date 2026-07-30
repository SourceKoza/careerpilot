package com.sourcekoza.careerpilot.job.dto;

import com.sourcekoza.careerpilot.job.domain.EmploymentType;
import com.sourcekoza.careerpilot.job.domain.ExperienceLevel;
import com.sourcekoza.careerpilot.job.domain.SourcePlatform;
import com.sourcekoza.careerpilot.job.domain.WorkplaceType;

import java.time.Instant;
import java.util.UUID;

/**
 * Lightweight response DTO for job listings — excludes description and requirements.
 */
public record JobSummaryResponse(
    UUID id,
    String title,
    String companyName,
    String location,
    EmploymentType employmentType,
    WorkplaceType workplaceType,
    ExperienceLevel experienceLevel,
    SourcePlatform sourcePlatform,
    boolean active,
    Instant createdAt,
    Instant updatedAt
) {}
