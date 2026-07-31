package com.sourcekoza.careerpilot.jobagent.mission.service;

import com.sourcekoza.careerpilot.exception.ResourceNotFoundException;
import com.sourcekoza.careerpilot.jobagent.mission.dto.MissionCreateRequest;
import com.sourcekoza.careerpilot.jobagent.mission.dto.MissionResponse;
import com.sourcekoza.careerpilot.jobagent.mission.entity.Mission;
import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionStatus;
import com.sourcekoza.careerpilot.jobagent.mission.mapper.MissionMapper;
import com.sourcekoza.careerpilot.jobagent.mission.repository.MissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service for mission CRUD operations.
 *
 * @since Sprint-15
 */
@Service
@Transactional(readOnly = true)
public class MissionService {

    private static final Logger log = LoggerFactory.getLogger(MissionService.class);

    private final MissionRepository missionRepository;
    private final MissionMapper missionMapper;

    public MissionService(MissionRepository missionRepository, MissionMapper missionMapper) {
        this.missionRepository = missionRepository;
        this.missionMapper = missionMapper;
    }

    @Transactional
    public MissionResponse createMission(UUID userId, MissionCreateRequest request) {
        Mission mission = missionMapper.toEntity(request);
        mission.setUserId(userId);
        mission.setStatus(MissionStatus.CREATED);
        Mission saved = missionRepository.save(mission);
        log.info("Mission created: id={}, name='{}', userId={}", saved.getId(), saved.getName(), userId);
        return missionMapper.toResponse(saved);
    }

    public MissionResponse getMission(UUID userId, UUID missionId) {
        Mission mission = findMissionOrThrow(userId, missionId);
        return missionMapper.toResponse(mission);
    }

    public Page<MissionResponse> listMissions(UUID userId, Pageable pageable) {
        return missionRepository.findByUserId(userId, pageable).map(missionMapper::toResponse);
    }

    @Transactional
    public MissionResponse pauseMission(UUID userId, UUID missionId) {
        Mission mission = findMissionOrThrow(userId, missionId);
        if (mission.getStatus() != MissionStatus.RUNNING && mission.getStatus() != MissionStatus.CREATED) {
            throw new IllegalStateException("Mission can only be paused when RUNNING or CREATED");
        }
        mission.setStatus(MissionStatus.PAUSED);
        Mission saved = missionRepository.save(mission);
        log.info("Mission paused: id={}", missionId);
        return missionMapper.toResponse(saved);
    }

    @Transactional
    public MissionResponse resumeMission(UUID userId, UUID missionId) {
        Mission mission = findMissionOrThrow(userId, missionId);
        if (mission.getStatus() != MissionStatus.PAUSED) {
            throw new IllegalStateException("Mission can only be resumed when PAUSED");
        }
        mission.setStatus(MissionStatus.CREATED);
        Mission saved = missionRepository.save(mission);
        log.info("Mission resumed: id={}", missionId);
        return missionMapper.toResponse(saved);
    }

    public Mission findMissionEntity(UUID userId, UUID missionId) {
        return findMissionOrThrow(userId, missionId);
    }

    private Mission findMissionOrThrow(UUID userId, UUID missionId) {
        return missionRepository.findByIdAndUserId(missionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Mission", "id", missionId));
    }
}
