package com.sourcekoza.careerpilot.job.dto;

import com.sourcekoza.careerpilot.job.domain.EmploymentType;
import com.sourcekoza.careerpilot.job.domain.ExperienceLevel;
import com.sourcekoza.careerpilot.job.domain.SourcePlatform;
import com.sourcekoza.careerpilot.job.domain.WorkplaceType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Full response DTO for a job posting.
 */
public record JobResponse(
    UUID id,
    String title,
    String companyName,
    String location,
    EmploymentType employmentType,
    WorkplaceType workplaceType,
    ExperienceLevel experienceLevel,
    BigDecimal salaryMin,
    BigDecimal salaryMax,
    String currency,
    String description,
    String requirements,
    String applicationUrl,
    SourcePlatform sourcePlatform,
    String externalJobId,
    boolean active,
    Instant createdAt,
    Instant updatedAt
) {}
