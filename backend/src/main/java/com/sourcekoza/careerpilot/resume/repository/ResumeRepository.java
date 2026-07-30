package com.sourcekoza.careerpilot.resume.repository;

import com.sourcekoza.careerpilot.resume.domain.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for the Resume aggregate root.
 *
 * <p>All queries filter on {@code deletedAt IS NULL} to exclude soft-deleted resumes.
 * The full entity graph is loaded via {@link EntityGraph} only for single-resume retrieval.</p>
 */
public interface ResumeRepository extends JpaRepository<Resume, UUID> {

    @EntityGraph(attributePaths = {"experiences", "educations", "skills", "certifications", "projects", "languages"})
    Optional<Resume> findByIdAndUserIdAndDeletedAtIsNull(UUID id, UUID userId);

    Page<Resume> findByUserIdAndDeletedAtIsNull(UUID userId, Pageable pageable);

    long countByUserIdAndDeletedAtIsNull(UUID userId);
}
