package com.sourcekoza.careerpilot.job.repository;

import com.sourcekoza.careerpilot.job.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for the Job aggregate root.
 *
 * <p>Provides standard CRUD operations and Spring Data pagination/sorting support.
 * Future sprints may add custom query methods for advanced filtering.</p>
 */
public interface JobRepository extends JpaRepository<Job, UUID> {
}
