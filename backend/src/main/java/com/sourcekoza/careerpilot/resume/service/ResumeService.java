package com.sourcekoza.careerpilot.resume.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcekoza.careerpilot.exception.ResourceNotFoundException;
import com.sourcekoza.careerpilot.resume.domain.Resume;
import com.sourcekoza.careerpilot.resume.domain.ResumeVersion;
import com.sourcekoza.careerpilot.resume.dto.CreateResumeRequest;
import com.sourcekoza.careerpilot.resume.dto.ResumeResponse;
import com.sourcekoza.careerpilot.resume.dto.ResumeSummaryResponse;
import com.sourcekoza.careerpilot.resume.dto.ResumeVersionResponse;
import com.sourcekoza.careerpilot.resume.dto.UpdateResumeRequest;
import com.sourcekoza.careerpilot.resume.mapper.ResumeMapper;
import com.sourcekoza.careerpilot.resume.repository.ResumeRepository;
import com.sourcekoza.careerpilot.resume.repository.ResumeVersionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Business logic service for Resume CRUD operations.
 *
 * <p>Enforces ownership (userId must match), handles soft delete,
 * creates version snapshots before updates, and delegates entity-DTO
 * mapping to {@link ResumeMapper}.</p>
 */
@Service
@Transactional(readOnly = true)
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ResumeVersionRepository resumeVersionRepository;
    private final ResumeMapper resumeMapper;
    private final ObjectMapper objectMapper;

    public ResumeService(ResumeRepository resumeRepository,
                         ResumeVersionRepository resumeVersionRepository,
                         ResumeMapper resumeMapper,
                         ObjectMapper objectMapper) {
        this.resumeRepository = resumeRepository;
        this.resumeVersionRepository = resumeVersionRepository;
        this.resumeMapper = resumeMapper;
        this.objectMapper = objectMapper;
    }

    /**
     * Creates a new resume for the given user.
     *
     * @param userId  the authenticated user's ID
     * @param request the resume creation request
     * @return the created resume response
     */
    @Transactional
    public ResumeResponse createResume(UUID userId, CreateResumeRequest request) {
        Resume resume = resumeMapper.toEntity(request);
        resume.setUserId(userId);

        // Set parent reference on all child entities (MapStruct cannot do this)
        resume.getExperiences().forEach(e -> e.setResume(resume));
        resume.getEducations().forEach(e -> e.setResume(resume));
        resume.getSkills().forEach(s -> s.setResume(resume));
        resume.getCertifications().forEach(c -> c.setResume(resume));
        resume.getProjects().forEach(p -> p.setResume(resume));
        resume.getLanguages().forEach(l -> l.setResume(resume));

        Resume saved = resumeRepository.save(resume);
        return resumeMapper.toResponse(saved);
    }

    /**
     * Retrieves a single resume by ID, verifying ownership.
     *
     * @param userId   the authenticated user's ID
     * @param resumeId the resume ID to retrieve
     * @return the resume response with all child sections
     * @throws ResourceNotFoundException if the resume does not exist or belongs to another user
     */
    public ResumeResponse getResume(UUID userId, UUID resumeId) {
        Resume resume = findResumeOrThrow(userId, resumeId);
        return resumeMapper.toResponse(resume);
    }

    /**
     * Updates an existing resume. Creates a version snapshot before applying changes.
     *
     * @param userId   the authenticated user's ID
     * @param resumeId the resume ID to update
     * @param request  the update request
     * @return the updated resume response
     * @throws ResourceNotFoundException if the resume does not exist or belongs to another user
     */
    @Transactional
    public ResumeResponse updateResume(UUID userId, UUID resumeId, UpdateResumeRequest request) {
        Resume resume = findResumeOrThrow(userId, resumeId);

        // Create version snapshot BEFORE applying update
        createVersionSnapshot(resume);

        // Apply update via MapStruct
        resumeMapper.updateEntity(request, resume);

        // Re-set parent references for new/replaced children
        resume.getExperiences().forEach(e -> e.setResume(resume));
        resume.getEducations().forEach(e -> e.setResume(resume));
        resume.getSkills().forEach(s -> s.setResume(resume));
        resume.getCertifications().forEach(c -> c.setResume(resume));
        resume.getProjects().forEach(p -> p.setResume(resume));
        resume.getLanguages().forEach(l -> l.setResume(resume));

        Resume saved = resumeRepository.save(resume);
        return resumeMapper.toResponse(saved);
    }

    /**
     * Deletes a resume and all associated versions.
     *
     * <p>Sprint-06 specifies physical delete is acceptable.</p>
     *
     * @param userId   the authenticated user's ID
     * @param resumeId the resume ID to delete
     * @throws ResourceNotFoundException if the resume does not exist or belongs to another user
     */
    @Transactional
    public void deleteResume(UUID userId, UUID resumeId) {
        Resume resume = findResumeOrThrow(userId, resumeId);
        resumeRepository.delete(resume);
    }

    /**
     * Lists all active (non-deleted) resumes for a user with pagination.
     *
     * @param userId   the authenticated user's ID
     * @param pageable pagination parameters
     * @return a page of resume summary responses
     */
    public Page<ResumeSummaryResponse> listResumes(UUID userId, Pageable pageable) {
        Page<Resume> resumes = resumeRepository.findByUserIdAndDeletedAtIsNull(userId, pageable);
        return resumes.map(resumeMapper::toSummaryResponse);
    }

    /**
     * Retrieves the version history for a resume, verifying ownership first.
     *
     * @param userId   the authenticated user's ID
     * @param resumeId the resume ID
     * @return list of version responses ordered by version number descending
     * @throws ResourceNotFoundException if the resume does not exist or belongs to another user
     */
    public List<ResumeVersionResponse> getVersions(UUID userId, UUID resumeId) {
        // Verify ownership
        findResumeOrThrow(userId, resumeId);
        List<ResumeVersion> versions = resumeVersionRepository.findByResumeIdOrderByVersionNumberDesc(resumeId);
        return versions.stream().map(resumeMapper::toVersionResponse).toList();
    }

    private Resume findResumeOrThrow(UUID userId, UUID resumeId) {
        return resumeRepository.findByIdAndUserIdAndDeletedAtIsNull(resumeId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume", "id", resumeId));
    }

    private void createVersionSnapshot(Resume resume) {
        try {
            String content = objectMapper.writeValueAsString(resumeMapper.toResponse(resume));
            long versionCount = resumeVersionRepository.countByResumeId(resume.getId());
            ResumeVersion version = ResumeVersion.builder()
                    .resume(resume)
                    .versionNumber((int) (versionCount + 1))
                    .markdownContent(content)
                    .changeSummary("Auto-snapshot before update")
                    .build();
            resumeVersionRepository.save(version);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create version snapshot", e);
        }
    }
}
