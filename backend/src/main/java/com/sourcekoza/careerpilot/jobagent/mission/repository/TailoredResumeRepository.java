package com.sourcekoza.careerpilot.jobagent.mission.repository;

import com.sourcekoza.careerpilot.jobagent.mission.entity.TailoredResume;
import com.sourcekoza.careerpilot.jobagent.mission.entity.TailoredResumeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for TailoredResume entities.
 *
 * @since Sprint-16
 */
public interface TailoredResumeRepository extends JpaRepository<TailoredResume, UUID> {

    Optional<TailoredResume> findByJobIdAndUserId(UUID jobId, UUID userId);

    List<TailoredResume> findByMissionIdAndStatus(UUID missionId, TailoredResumeStatus status);

    List<TailoredResume> findByMissionId(UUID missionId);
}
