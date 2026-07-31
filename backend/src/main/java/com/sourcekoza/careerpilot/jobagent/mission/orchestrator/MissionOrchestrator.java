package com.sourcekoza.careerpilot.jobagent.mission.orchestrator;

import com.sourcekoza.careerpilot.jobagent.agents.core.AgentExecutionResult;
import com.sourcekoza.careerpilot.jobagent.agents.core.AgentType;
import com.sourcekoza.careerpilot.jobagent.agents.core.MissionAgent;
import com.sourcekoza.careerpilot.jobagent.agents.core.MissionContext;
import com.sourcekoza.careerpilot.jobagent.mission.entity.ExecutionStatus;
import com.sourcekoza.careerpilot.jobagent.mission.entity.LogLevel;
import com.sourcekoza.careerpilot.jobagent.mission.entity.Mission;
import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionEvent;
import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionEventType;
import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionExecution;
import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionExecutionLog;
import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionStatus;
import com.sourcekoza.careerpilot.jobagent.mission.events.JobsDiscoveredEvent;
import com.sourcekoza.careerpilot.jobagent.mission.events.MissionCompletedEvent;
import com.sourcekoza.careerpilot.jobagent.mission.events.MissionStartedEvent;
import com.sourcekoza.careerpilot.jobagent.mission.repository.MissionEventRepository;
import com.sourcekoza.careerpilot.jobagent.mission.repository.MissionExecutionLogRepository;
import com.sourcekoza.careerpilot.jobagent.mission.repository.MissionExecutionRepository;
import com.sourcekoza.careerpilot.jobagent.mission.repository.MissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mission Orchestrator — coordinates mission execution pipeline.
 *
 * <p>Future agents plug in through the MissionAgent interface without
 * modifying this orchestrator's code.</p>
 *
 * @since Sprint-15
 */
@Service
public class MissionOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(MissionOrchestrator.class);

    private final Map<AgentType, MissionAgent> agentRegistry;
    private final MissionRepository missionRepository;
    private final MissionExecutionRepository executionRepository;
    private final MissionEventRepository eventRepository;
    private final MissionExecutionLogRepository logRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MissionOrchestrator(List<MissionAgent> agents,
                                MissionRepository missionRepository,
                                MissionExecutionRepository executionRepository,
                                MissionEventRepository eventRepository,
                                MissionExecutionLogRepository logRepository,
                                ApplicationEventPublisher eventPublisher) {
        this.agentRegistry = agents.stream()
                .collect(Collectors.toMap(MissionAgent::getType, Function.identity()));
        this.missionRepository = missionRepository;
        this.executionRepository = executionRepository;
        this.eventRepository = eventRepository;
        this.logRepository = logRepository;
        this.eventPublisher = eventPublisher;
        log.info("MissionOrchestrator initialized with {} agents: {}",
                agentRegistry.size(), agentRegistry.keySet());
    }

    @Transactional
    public AgentExecutionResult executeMission(Mission mission) {
        log.info("Mission execution starting: missionId={}, name='{}'",
                mission.getId(), mission.getName());

        MissionExecution execution = createExecution(mission);
        UUID executionId = execution.getId();

        mission.setStatus(MissionStatus.RUNNING);
        missionRepository.save(mission);

        MissionContext context = new MissionContext(mission, executionId, mission.getUserId());

        persistEvent(mission, executionId, MissionEventType.MISSION_STARTED, "Mission execution started");
        persistLog(mission, executionId, LogLevel.INFO, "Mission Started");
        eventPublisher.publishEvent(new MissionStartedEvent(mission.getId(), executionId, mission.getUserId()));

        try {
            persistEvent(mission, executionId, MissionEventType.SEARCH_STARTED, "Job search starting");
            persistLog(mission, executionId, LogLevel.INFO, "Searching job platforms...");

            MissionAgent jobSearchAgent = agentRegistry.get(AgentType.JOB_SEARCH);
            if (jobSearchAgent == null) {
                throw new IllegalStateException("JobSearchAgent not registered");
            }

            AgentExecutionResult result = jobSearchAgent.execute(context);

            if (result.success()) {
                persistEvent(mission, executionId, MissionEventType.JOBS_DISCOVERED,
                        String.format("%d jobs discovered", result.jobsFound()));
                persistEvent(mission, executionId, MissionEventType.CONTACTS_DISCOVERED,
                        String.format("%d contacts discovered", result.contactsFound()));
                persistLog(mission, executionId, LogLevel.INFO,
                        String.format("%d Jobs Found", result.jobsFound()));
                persistLog(mission, executionId, LogLevel.INFO,
                        String.format("%d Contacts Extracted", result.contactsFound()));

                eventPublisher.publishEvent(new JobsDiscoveredEvent(
                        mission.getId(), executionId, result.jobsFound()));

                execution.markCompleted(result.jobsFound(), result.contactsFound());
                executionRepository.save(execution);

                mission.setStatus(MissionStatus.COMPLETED);
                missionRepository.save(mission);

                persistEvent(mission, executionId, MissionEventType.MISSION_COMPLETED, "Mission completed");
                persistLog(mission, executionId, LogLevel.INFO, "Mission Completed");
                eventPublisher.publishEvent(new MissionCompletedEvent(
                        mission.getId(), executionId, result.jobsFound(), result.contactsFound()));

                return result;
            } else {
                return handleFailure(mission, execution, executionId, result.message());
            }
        } catch (Exception e) {
            log.error("Mission execution failed: missionId={}, error='{}'", mission.getId(), e.getMessage(), e);
            return handleFailure(mission, execution, executionId, e.getMessage());
        }
    }

    private AgentExecutionResult handleFailure(Mission mission, MissionExecution execution,
                                                UUID executionId, String errorMessage) {
        execution.markFailed(errorMessage);
        executionRepository.save(execution);
        mission.setStatus(MissionStatus.FAILED);
        missionRepository.save(mission);
        persistEvent(mission, executionId, MissionEventType.MISSION_FAILED, "Mission failed: " + errorMessage);
        persistLog(mission, executionId, LogLevel.ERROR, "Mission Failed: " + errorMessage);
        return AgentExecutionResult.failure(AgentType.JOB_SEARCH, errorMessage, execution.getStartedAt());
    }

    private MissionExecution createExecution(Mission mission) {
        MissionExecution execution = new MissionExecution();
        execution.setMission(mission);
        execution.setStatus(ExecutionStatus.RUNNING);
        execution.setStartedAt(Instant.now());
        return executionRepository.save(execution);
    }

    private void persistEvent(Mission mission, UUID executionId, MissionEventType eventType, String message) {
        MissionEvent event = new MissionEvent();
        event.setMission(mission);
        event.setExecutionId(executionId);
        event.setEventType(eventType);
        event.setMessage(message);
        event.setEventTime(Instant.now());
        eventRepository.save(event);
    }

    private void persistLog(Mission mission, UUID executionId, LogLevel level, String message) {
        MissionExecutionLog logEntry = new MissionExecutionLog();
        logEntry.setMission(mission);
        logEntry.setExecutionId(executionId);
        logEntry.setLevel(level);
        logEntry.setMessage(message);
        logEntry.setLogTime(Instant.now());
        logRepository.save(logEntry);
    }
}
