package com.sourcekoza.careerpilot.mission.repository;

import com.sourcekoza.careerpilot.mission.entity.Mission;
import com.sourcekoza.careerpilot.mission.entity.MissionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Mission entities.
 *
 * @since Sprint-15
 */
public interface MissionRepository extends JpaRepository<Mission, UUID> {

    Page<Mission> findByUserId(UUID userId, Pageable pageable);

    Optional<Mission> findByIdAndUserId(UUID id, UUID userId);

    Page<Mission> findByUserIdAndStatus(UUID userId, MissionStatus status, Pageable pageable);
}
