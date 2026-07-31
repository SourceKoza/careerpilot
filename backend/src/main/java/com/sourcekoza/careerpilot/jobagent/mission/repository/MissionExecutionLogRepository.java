package com.sourcekoza.careerpilot.jobagent.mission.repository;

import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionExecutionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for MissionExecutionLog entities.
 *
 * @since Sprint-15
 */
public interface MissionExecutionLogRepository extends JpaRepository<MissionExecutionLog, UUID> {

    Page<MissionExecutionLog> findByMissionIdOrderByLogTimeDesc(UUID missionId, Pageable pageable);

    Page<MissionExecutionLog> findByExecutionIdOrderByLogTimeAsc(UUID executionId, Pageable pageable);
}
