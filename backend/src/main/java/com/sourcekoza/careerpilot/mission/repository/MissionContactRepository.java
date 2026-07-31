package com.sourcekoza.careerpilot.mission.repository;

import com.sourcekoza.careerpilot.mission.entity.MissionContact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for MissionContact entities.
 *
 * @since Sprint-15
 */
public interface MissionContactRepository extends JpaRepository<MissionContact, UUID> {

    Page<MissionContact> findByMissionId(UUID missionId, Pageable pageable);

    long countByMissionId(UUID missionId);
}
