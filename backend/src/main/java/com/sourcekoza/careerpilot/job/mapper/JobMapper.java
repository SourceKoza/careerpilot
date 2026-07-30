package com.sourcekoza.careerpilot.job.mapper;

import com.sourcekoza.careerpilot.job.domain.Job;
import com.sourcekoza.careerpilot.job.dto.JobCreateRequest;
import com.sourcekoza.careerpilot.job.dto.JobResponse;
import com.sourcekoza.careerpilot.job.dto.JobSummaryResponse;
import com.sourcekoza.careerpilot.job.dto.JobUpdateRequest;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for converting between Job domain entity and DTOs.
 *
 * <p>Generates compile-time mapping code. Builder pattern is disabled
 * so MapStruct uses no-arg constructor + setters, correctly handling
 * BaseEntity fields managed by JPA.</p>
 */
@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface JobMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Job toEntity(JobCreateRequest request);

    JobResponse toResponse(Job job);

    JobSummaryResponse toSummaryResponse(Job job);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(JobUpdateRequest request, @MappingTarget Job job);
}
