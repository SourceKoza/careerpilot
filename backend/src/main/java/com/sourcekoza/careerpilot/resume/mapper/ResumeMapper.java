package com.sourcekoza.careerpilot.resume.mapper;

import com.sourcekoza.careerpilot.resume.domain.Certification;
import com.sourcekoza.careerpilot.resume.domain.Education;
import com.sourcekoza.careerpilot.resume.domain.Experience;
import com.sourcekoza.careerpilot.resume.domain.Language;
import com.sourcekoza.careerpilot.resume.domain.Project;
import com.sourcekoza.careerpilot.resume.domain.Resume;
import com.sourcekoza.careerpilot.resume.domain.ResumeVersion;
import com.sourcekoza.careerpilot.resume.domain.Skill;
import com.sourcekoza.careerpilot.resume.dto.CertificationRequest;
import com.sourcekoza.careerpilot.resume.dto.CertificationResponse;
import com.sourcekoza.careerpilot.resume.dto.CreateResumeRequest;
import com.sourcekoza.careerpilot.resume.dto.EducationRequest;
import com.sourcekoza.careerpilot.resume.dto.EducationResponse;
import com.sourcekoza.careerpilot.resume.dto.ExperienceRequest;
import com.sourcekoza.careerpilot.resume.dto.ExperienceResponse;
import com.sourcekoza.careerpilot.resume.dto.LanguageRequest;
import com.sourcekoza.careerpilot.resume.dto.LanguageResponse;
import com.sourcekoza.careerpilot.resume.dto.ProjectRequest;
import com.sourcekoza.careerpilot.resume.dto.ProjectResponse;
import com.sourcekoza.careerpilot.resume.dto.ResumeResponse;
import com.sourcekoza.careerpilot.resume.dto.ResumeSummaryResponse;
import com.sourcekoza.careerpilot.resume.dto.ResumeVersionResponse;
import com.sourcekoza.careerpilot.resume.dto.SkillRequest;
import com.sourcekoza.careerpilot.resume.dto.SkillResponse;
import com.sourcekoza.careerpilot.resume.dto.UpdateResumeRequest;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for converting between Resume domain entities and DTOs.
 *
 * <p>Generates compile-time mapping code with zero reflection overhead.
 * Handles nested entity-to-DTO conversions automatically via method discovery.</p>
 *
 * <p>Builder pattern is disabled so MapStruct uses the no-arg constructor + setters,
 * which correctly handles BaseEntity fields managed by JPA.</p>
 */
@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ResumeMapper {

    // ===== Resume mappings =====

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "versions", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Resume toEntity(CreateResumeRequest request);

    ResumeResponse toResponse(Resume resume);

    ResumeSummaryResponse toSummaryResponse(Resume resume);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "versions", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(UpdateResumeRequest request, @MappingTarget Resume resume);

    // ===== Experience mappings =====

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "resume", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Experience toEntity(ExperienceRequest request);

    ExperienceResponse toResponse(Experience experience);

    // ===== Education mappings =====

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "resume", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Education toEntity(EducationRequest request);

    EducationResponse toResponse(Education education);

    // ===== Skill mappings =====

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "resume", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Skill toEntity(SkillRequest request);

    SkillResponse toResponse(Skill skill);

    // ===== Certification mappings =====

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "resume", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Certification toEntity(CertificationRequest request);

    CertificationResponse toResponse(Certification certification);

    // ===== Project mappings =====

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "resume", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Project toEntity(ProjectRequest request);

    ProjectResponse toResponse(Project project);

    // ===== Language mappings =====

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "resume", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Language toEntity(LanguageRequest request);

    LanguageResponse toResponse(Language language);

    // ===== Version mapping =====

    @Mapping(source = "resume.id", target = "resumeId")
    ResumeVersionResponse toVersionResponse(ResumeVersion version);
}
