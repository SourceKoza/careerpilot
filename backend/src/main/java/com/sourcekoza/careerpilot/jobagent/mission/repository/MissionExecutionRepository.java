package com.sourcekoza.careerpilot.jobagent.mission.repository;

import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for MissionExecution entities.
 *
 * @since Sprint-15
 */
public interface MissionExecutionRepository extends JpaRepository<MissionExecution, UUID> {

    Page<MissionExecution> findByMissionIdOrderByStartedAtDesc(UUID missionId, Pageable pageable);
}
