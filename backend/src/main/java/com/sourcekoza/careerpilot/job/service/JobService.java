package com.sourcekoza.careerpilot.job.service;

import com.sourcekoza.careerpilot.exception.ResourceNotFoundException;
import com.sourcekoza.careerpilot.job.domain.Job;
import com.sourcekoza.careerpilot.job.dto.JobCreateRequest;
import com.sourcekoza.careerpilot.job.dto.JobResponse;
import com.sourcekoza.careerpilot.job.dto.JobSummaryResponse;
import com.sourcekoza.careerpilot.job.dto.JobUpdateRequest;
import com.sourcekoza.careerpilot.job.mapper.JobMapper;
import com.sourcekoza.careerpilot.job.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Business logic service for Job CRUD operations.
 *
 * <p>Manages the lifecycle of Job entities including creation, retrieval,
 * update, deletion, and paginated listing. Delegates entity-DTO mapping
 * to {@link JobMapper}.</p>
 */
@Service
@Transactional(readOnly = true)
public class JobService {

    private static final Logger log = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    public JobService(JobRepository jobRepository, JobMapper jobMapper) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
    }

    /**
     * Creates a new job posting.
     *
     * @param request the job creation request
     * @return the created job response
     */
    @Transactional
    public JobResponse createJob(JobCreateRequest request) {
        Job job = jobMapper.toEntity(request);

        // Default active to true if not explicitly provided
        if (request.active() != null) {
            job.setActive(request.active());
        }

        Job saved = jobRepository.save(job);
        log.info("Job created: id={}, title='{}', company='{}'",
                saved.getId(), saved.getTitle(), saved.getCompanyName());
        return jobMapper.toResponse(saved);
    }

    /**
     * Retrieves a single job by ID.
     *
     * @param id the job ID
     * @return the full job response
     * @throws ResourceNotFoundException if the job does not exist
     */
    public JobResponse getJob(UUID id) {
        Job job = findJobOrThrow(id);
        return jobMapper.toResponse(job);
    }

    /**
     * Updates an existing job posting.
     *
     * @param id      the job ID to update
     * @param request the update request
     * @return the updated job response
     * @throws ResourceNotFoundException if the job does not exist
     */
    @Transactional
    public JobResponse updateJob(UUID id, JobUpdateRequest request) {
        Job job = findJobOrThrow(id);
        jobMapper.updateEntity(request, job);

        // Handle active field explicitly (MapStruct maps Boolean to boolean)
        if (request.active() != null) {
            job.setActive(request.active());
        }

        Job saved = jobRepository.save(job);
        log.info("Job updated: id={}, title='{}'", saved.getId(), saved.getTitle());
        return jobMapper.toResponse(saved);
    }

    /**
     * Deletes a job posting.
     *
     * @param id the job ID to delete
     * @throws ResourceNotFoundException if the job does not exist
     */
    @Transactional
    public void deleteJob(UUID id) {
        Job job = findJobOrThrow(id);
        jobRepository.delete(job);
        log.info("Job deleted: id={}, title='{}'", id, job.getTitle());
    }

    /**
     * Lists all jobs with pagination and sorting support.
     *
     * @param pageable pagination and sorting parameters
     * @return a page of job summary responses
     */
    public Page<JobSummaryResponse> listJobs(Pageable pageable) {
        Page<Job> jobs = jobRepository.findAll(pageable);
        return jobs.map(jobMapper::toSummaryResponse);
    }

    private Job findJobOrThrow(UUID id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));
    }
}
