package com.sourcekoza.careerpilot.jobagent.mission.repository;

import com.sourcekoza.careerpilot.jobagent.mission.entity.DiscoveredJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for DiscoveredJob entities.
 *
 * @since Sprint-15
 */
public interface DiscoveredJobRepository extends JpaRepository<DiscoveredJob, UUID> {

    Page<DiscoveredJob> findByMissionId(UUID missionId, Pageable pageable);

    long countByMissionId(UUID missionId);
}
