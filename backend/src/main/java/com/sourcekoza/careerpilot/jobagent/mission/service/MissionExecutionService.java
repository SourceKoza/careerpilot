package com.sourcekoza.careerpilot.jobagent.mission.service;

import com.sourcekoza.careerpilot.jobagent.agents.core.AgentExecutionResult;
import com.sourcekoza.careerpilot.jobagent.mission.dto.MissionEventResponse;
import com.sourcekoza.careerpilot.jobagent.mission.dto.MissionExecutionResponse;
import com.sourcekoza.careerpilot.jobagent.mission.dto.MissionLogResponse;
import com.sourcekoza.careerpilot.jobagent.mission.dto.MissionStartResponse;
import com.sourcekoza.careerpilot.jobagent.mission.entity.ExecutionStatus;
import com.sourcekoza.careerpilot.jobagent.mission.entity.Mission;
import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionStatus;
import com.sourcekoza.careerpilot.jobagent.mission.mapper.MissionMapper;
import com.sourcekoza.careerpilot.jobagent.mission.orchestrator.MissionOrchestrator;
import com.sourcekoza.careerpilot.jobagent.mission.repository.MissionEventRepository;
import com.sourcekoza.careerpilot.jobagent.mission.repository.MissionExecutionLogRepository;
import com.sourcekoza.careerpilot.jobagent.mission.repository.MissionExecutionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service for mission execution operations.
 *
 * @since Sprint-15
 */
@Service
@Transactional(readOnly = true)
public class MissionExecutionService {

    private static final Logger log = LoggerFactory.getLogger(MissionExecutionService.class);

    private final MissionService missionService;
    private final MissionOrchestrator orchestrator;
    private final MissionExecutionRepository executionRepository;
    private final MissionEventRepository eventRepository;
    private final MissionExecutionLogRepository logRepository;
    private final com.sourcekoza.careerpilot.jobagent.mission.repository.DiscoveredJobRepository discoveredJobRepository;
    private final MissionMapper missionMapper;
    private final com.sourcekoza.careerpilot.jobagent.agents.email.EmailOutreachAgent emailOutreachAgent;

    public MissionExecutionService(MissionService missionService,
                                    MissionOrchestrator orchestrator,
                                    MissionExecutionRepository executionRepository,
                                    MissionEventRepository eventRepository,
                                    MissionExecutionLogRepository logRepository,
                                    com.sourcekoza.careerpilot.jobagent.mission.repository.DiscoveredJobRepository discoveredJobRepository,
                                    MissionMapper missionMapper,
                                    com.sourcekoza.careerpilot.jobagent.agents.email.EmailOutreachAgent emailOutreachAgent) {
        this.missionService = missionService;
        this.orchestrator = orchestrator;
        this.executionRepository = executionRepository;
        this.eventRepository = eventRepository;
        this.logRepository = logRepository;
        this.discoveredJobRepository = discoveredJobRepository;
        this.missionMapper = missionMapper;
        this.emailOutreachAgent = emailOutreachAgent;
    }

    /**
     * Starts a mission — returns immediately, execution runs in background.
     */
    @Transactional
    public MissionStartResponse startMission(UUID userId, UUID missionId) {
        log.info("Starting mission: userId={}, missionId={}", userId, missionId);
        Mission mission = missionService.findMissionEntity(userId, missionId);

        if (mission.getStatus() == MissionStatus.RUNNING) {
            throw new IllegalStateException("Mission is already running");
        }

        // Run execution asynchronously
        executeAsync(mission);

        return new MissionStartResponse(missionId, null, ExecutionStatus.RUNNING,
                "Mission execution started. Check /executions for progress.");
    }

    /**
     * Runs the mission orchestrator asynchronously so the API returns instantly.
     */
    @Async
    @Transactional
    public void executeAsync(Mission mission) {
        try {
            orchestrator.executeMission(mission);
        } catch (Exception e) {
            log.error("Async mission execution failed: {}", e.getMessage(), e);
        }
    }

    public Page<MissionExecutionResponse> getExecutions(UUID userId, UUID missionId, Pageable pageable) {
        missionService.findMissionEntity(userId, missionId);
        return executionRepository.findByMissionIdOrderByStartedAtDesc(missionId, pageable)
                .map(missionMapper::toExecutionResponse);
    }

    public Page<MissionEventResponse> getEvents(UUID userId, UUID missionId, Pageable pageable) {
        missionService.findMissionEntity(userId, missionId);
        return eventRepository.findByMissionIdOrderByEventTimeDesc(missionId, pageable)
                .map(missionMapper::toEventResponse);
    }

    public Page<MissionLogResponse> getLogs(UUID userId, UUID missionId, Pageable pageable) {
        missionService.findMissionEntity(userId, missionId);
        return logRepository.findByMissionIdOrderByLogTimeDesc(missionId, pageable)
                .map(missionMapper::toLogResponse);
    }

    /**
     * Runs the email outreach agent for high-match jobs in a mission.
     */
    @Transactional
    public MissionStartResponse runEmailOutreach(UUID userId, UUID missionId) {
        log.info("Running email outreach: userId={}, missionId={}", userId, missionId);
        Mission mission = missionService.findMissionEntity(userId, missionId);

        com.sourcekoza.careerpilot.jobagent.agents.core.MissionContext context =
                new com.sourcekoza.careerpilot.jobagent.agents.core.MissionContext(
                        mission, UUID.randomUUID(), userId);

        var result = emailOutreachAgent.execute(context);
        ExecutionStatus status = result.success() ? ExecutionStatus.COMPLETED : ExecutionStatus.FAILED;
        return new MissionStartResponse(missionId, null, status, result.message());
    }

    public Page<com.sourcekoza.careerpilot.jobagent.mission.dto.DiscoveredJobResponse> getDiscoveredJobs(
            UUID userId, UUID missionId, Pageable pageable) {
        missionService.findMissionEntity(userId, missionId);
        return discoveredJobRepository.findByMissionId(missionId, pageable)
                .map(missionMapper::toDiscoveredJobResponse);
    }
}
