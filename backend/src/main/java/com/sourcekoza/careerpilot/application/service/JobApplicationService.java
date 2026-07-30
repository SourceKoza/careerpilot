package com.sourcekoza.careerpilot.application.service;

import com.sourcekoza.careerpilot.application.domain.ApplicationStatus;
import com.sourcekoza.careerpilot.application.domain.JobApplication;
import com.sourcekoza.careerpilot.application.dto.ApplicationCreateRequest;
import com.sourcekoza.careerpilot.application.dto.ApplicationResponse;
import com.sourcekoza.careerpilot.application.dto.ApplicationSummaryResponse;
import com.sourcekoza.careerpilot.application.dto.ApplicationUpdateRequest;
import com.sourcekoza.careerpilot.application.mapper.JobApplicationMapper;
import com.sourcekoza.careerpilot.application.repository.JobApplicationRepository;
import com.sourcekoza.careerpilot.auth.domain.User;
import com.sourcekoza.careerpilot.auth.repository.UserRepository;
import com.sourcekoza.careerpilot.exception.ResourceNotFoundException;
import com.sourcekoza.careerpilot.job.domain.Job;
import com.sourcekoza.careerpilot.job.repository.JobRepository;
import com.sourcekoza.careerpilot.resume.domain.ResumeVersion;
import com.sourcekoza.careerpilot.resume.repository.ResumeVersionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Business logic service for Job Application CRUD operations.
 *
 * <p>Enforces user ownership on all operations, validates that referenced
 * entities (Job, ResumeVersion) exist, and ensures the ResumeVersion
 * is never modified after creation.</p>
 */
@Service
@Transactional(readOnly = true)
public class JobApplicationService {

    private static final Logger log = LoggerFactory.getLogger(JobApplicationService.class);

    private final JobApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final ResumeVersionRepository resumeVersionRepository;
    private final UserRepository userRepository;
    private final JobApplicationMapper applicationMapper;

    public JobApplicationService(JobApplicationRepository applicationRepository,
                                 JobRepository jobRepository,
                                 ResumeVersionRepository resumeVersionRepository,
                                 UserRepository userRepository,
                                 JobApplicationMapper applicationMapper) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.resumeVersionRepository = resumeVersionRepository;
        this.userRepository = userRepository;
        this.applicationMapper = applicationMapper;
    }

    /**
     * Creates a new job application for the authenticated user.
     *
     * @param userId  the authenticated user's ID
     * @param request the application creation request
     * @return the created application response
     * @throws ResourceNotFoundException if the user, job, or resume version does not exist
     */
    @Transactional
    public ApplicationResponse createApplication(UUID userId, ApplicationCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Job job = jobRepository.findById(request.jobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", request.jobId()));

        ResumeVersion resumeVersion = resumeVersionRepository.findById(request.resumeVersionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ResumeVersion", "id", request.resumeVersionId()));

        JobApplication application = new JobApplication();
        application.setUser(user);
        application.setJob(job);
        application.setResumeVersion(resumeVersion);
        application.setStatus(request.status() != null ? request.status() : ApplicationStatus.DRAFT);
        application.setNotes(request.notes());
        application.setExternalApplicationId(request.externalApplicationId());

        // Set appliedAt when status is SUBMITTED
        if (application.getStatus() == ApplicationStatus.SUBMITTED) {
            application.setAppliedAt(Instant.now());
        }

        JobApplication saved = applicationRepository.save(application);
        log.info("Application created: id={}, userId={}, jobId={}",
                saved.getId(), userId, request.jobId());
        return applicationMapper.toResponse(saved);
    }

    /**
     * Retrieves a single application by ID, verifying ownership.
     *
     * @param userId        the authenticated user's ID
     * @param applicationId the application ID
     * @return the full application response
     * @throws ResourceNotFoundException if the application does not exist or belongs to another user
     */
    public ApplicationResponse getApplication(UUID userId, UUID applicationId) {
        JobApplication application = findApplicationOrThrow(userId, applicationId);
        return applicationMapper.toResponse(application);
    }

    /**
     * Updates an existing application. Only status, notes, and externalApplicationId
     * can be changed. Job and ResumeVersion are immutable after creation.
     *
     * @param userId        the authenticated user's ID
     * @param applicationId the application ID to update
     * @param request       the update request
     * @return the updated application response
     * @throws ResourceNotFoundException if the application does not exist or belongs to another user
     */
    @Transactional
    public ApplicationResponse updateApplication(UUID userId, UUID applicationId,
                                                  ApplicationUpdateRequest request) {
        JobApplication application = findApplicationOrThrow(userId, applicationId);

        // Set appliedAt when transitioning to SUBMITTED for the first time
        if (request.status() == ApplicationStatus.SUBMITTED && application.getAppliedAt() == null) {
            application.setAppliedAt(Instant.now());
        }

        application.setStatus(request.status());
        application.setNotes(request.notes());
        application.setExternalApplicationId(request.externalApplicationId());

        JobApplication saved = applicationRepository.save(application);
        log.info("Application updated: id={}, status={}", saved.getId(), saved.getStatus());
        return applicationMapper.toResponse(saved);
    }

    /**
     * Deletes a job application.
     *
     * @param userId        the authenticated user's ID
     * @param applicationId the application ID to delete
     * @throws ResourceNotFoundException if the application does not exist or belongs to another user
     */
    @Transactional
    public void deleteApplication(UUID userId, UUID applicationId) {
        JobApplication application = findApplicationOrThrow(userId, applicationId);
        applicationRepository.delete(application);
        log.info("Application deleted: id={}, userId={}", applicationId, userId);
    }

    /**
     * Lists all applications for the authenticated user with pagination.
     *
     * @param userId   the authenticated user's ID
     * @param pageable pagination and sorting parameters
     * @return a page of application summary responses
     */
    public Page<ApplicationSummaryResponse> listApplications(UUID userId, Pageable pageable) {
        Page<JobApplication> applications = applicationRepository.findByUserId(userId, pageable);
        return applications.map(applicationMapper::toSummaryResponse);
    }

    private JobApplication findApplicationOrThrow(UUID userId, UUID applicationId) {
        return applicationRepository.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "JobApplication", "id", applicationId));
    }
}
