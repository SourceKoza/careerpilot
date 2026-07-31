package com.sourcekoza.careerpilot.jobagent.mission.repository;

import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for MissionEvent entities.
 *
 * @since Sprint-15
 */
public interface MissionEventRepository extends JpaRepository<MissionEvent, UUID> {

    Page<MissionEvent> findByMissionIdOrderByEventTimeDesc(UUID missionId, Pageable pageable);
}
