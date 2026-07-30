package com.sourcekoza.careerpilot.job.dto;

import com.sourcekoza.careerpilot.job.domain.EmploymentType;
import com.sourcekoza.careerpilot.job.domain.ExperienceLevel;
import com.sourcekoza.careerpilot.job.domain.SourcePlatform;
import com.sourcekoza.careerpilot.job.domain.WorkplaceType;
import com.sourcekoza.careerpilot.job.validation.ValidSalaryRange;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

/**
 * Request DTO for creating a new job posting.
 */
@ValidSalaryRange
public record JobCreateRequest(

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    String title,

    @NotBlank(message = "Company name is required")
    @Size(max = 200, message = "Company name must not exceed 200 characters")
    String companyName,

    @Size(max = 200, message = "Location must not exceed 200 characters")
    String location,

    EmploymentType employmentType,

    WorkplaceType workplaceType,

    ExperienceLevel experienceLevel,

    @DecimalMin(value = "0.0", inclusive = true, message = "Salary minimum cannot be negative")
    BigDecimal salaryMin,

    @DecimalMin(value = "0.0", inclusive = true, message = "Salary maximum cannot be negative")
    BigDecimal salaryMax,

    @Size(max = 10, message = "Currency must not exceed 10 characters")
    String currency,

    String description,

    String requirements,

    @URL(message = "Application URL must be a valid URL")
    @Size(max = 2048, message = "Application URL must not exceed 2048 characters")
    String applicationUrl,

    SourcePlatform sourcePlatform,

    @Size(max = 200, message = "External job ID must not exceed 200 characters")
    String externalJobId,

    Boolean active
) {}
