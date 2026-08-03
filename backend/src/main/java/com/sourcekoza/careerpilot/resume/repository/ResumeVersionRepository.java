package com.sourcekoza.careerpilot.resume.repository;

import com.sourcekoza.careerpilot.resume.domain.ResumeVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for resume version history.
 *
 * <p>Versions are immutable snapshots created when the user explicitly
 * creates a new version. They are never modified or deleted through
 * normal operations.</p>
 */
public interface ResumeVersionRepository extends JpaRepository<ResumeVersion, UUID> {

    List<ResumeVersion> findByResumeIdOrderByVersionNumberDesc(UUID resumeId);

    Optional<ResumeVersion> findByIdAndResumeId(UUID id, UUID resumeId);

    long countByResumeId(UUID resumeId);
}
