package com.sourcekoza.careerpilot.application.repository;

import com.sourcekoza.careerpilot.application.domain.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for the JobApplication aggregate root.
 *
 * <p>Provides pagination and user-scoped queries. Uses {@link EntityGraph}
 * to eagerly fetch the Job relationship needed for response mapping.</p>
 */
public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {

    @EntityGraph(attributePaths = {"job", "resumeVersion"})
    Optional<JobApplication> findByIdAndUserId(UUID id, UUID userId);

    @EntityGraph(attributePaths = {"job"})
    Page<JobApplication> findByUserId(UUID userId, Pageable pageable);
}
