package com.sourcekoza.careerpilot.application.mapper;

import com.sourcekoza.careerpilot.application.domain.JobApplication;
import com.sourcekoza.careerpilot.application.dto.ApplicationResponse;
import com.sourcekoza.careerpilot.application.dto.ApplicationSummaryResponse;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between JobApplication domain entity and DTOs.
 *
 * <p>Handles the extraction of nested entity IDs and denormalized fields
 * (job title, company name) for response DTOs.</p>
 */
@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface JobApplicationMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "job.id", target = "jobId")
    @Mapping(source = "job.title", target = "jobTitle")
    @Mapping(source = "job.companyName", target = "companyName")
    @Mapping(source = "resumeVersion.id", target = "resumeVersionId")
    ApplicationResponse toResponse(JobApplication application);

    @Mapping(source = "job.id", target = "jobId")
    @Mapping(source = "job.title", target = "jobTitle")
    @Mapping(source = "job.companyName", target = "companyName")
    ApplicationSummaryResponse toSummaryResponse(JobApplication application);
}
