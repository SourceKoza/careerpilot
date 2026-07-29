package com.sourcekoza.careerpilot.resume.repository;

import com.sourcekoza.careerpilot.resume.domain.ResumeVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for resume version history.
 *
 * <p>Versions are immutable snapshots created on each resume update.
 * They are never deleted through normal operations.</p>
 */
public interface ResumeVersionRepository extends JpaRepository<ResumeVersion, UUID> {

    List<ResumeVersion> findByResumeIdOrderByVersionNumberDesc(UUID resumeId);

    long countByResumeId(UUID resumeId);
}
