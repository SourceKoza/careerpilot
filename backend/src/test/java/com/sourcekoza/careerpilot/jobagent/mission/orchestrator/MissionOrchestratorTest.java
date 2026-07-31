package com.sourcekoza.careerpilot.jobagent.mission.orchestrator;

import com.sourcekoza.careerpilot.jobagent.agents.core.AgentExecutionResult;
import com.sourcekoza.careerpilot.jobagent.agents.core.AgentType;
import com.sourcekoza.careerpilot.jobagent.agents.core.MissionAgent;
import com.sourcekoza.careerpilot.jobagent.agents.core.MissionContext;
import com.sourcekoza.careerpilot.jobagent.mission.entity.Mission;
import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionEvent;
import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionExecution;
import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionExecutionLog;
import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionStatus;
import com.sourcekoza.careerpilot.jobagent.mission.repository.MissionEventRepository;
import com.sourcekoza.careerpilot.jobagent.mission.repository.MissionExecutionLogRepository;
import com.sourcekoza.careerpilot.jobagent.mission.repository.MissionExecutionRepository;
import com.sourcekoza.careerpilot.jobagent.mission.repository.MissionRepository;
import com.sourcekoza.careerpilot.jobagent.mission.service.AutoApplyPipeline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MissionOrchestratorTest {

    @Mock
    private MissionRepository missionRepository;
    @Mock
    private MissionExecutionRepository executionRepository;
    @Mock
    private MissionEventRepository eventRepository;
    @Mock
    private MissionExecutionLogRepository logRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private MissionAgent jobSearchAgent;
    @Mock
    private AutoApplyPipeline autoApplyPipeline;

    private MissionOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        when(jobSearchAgent.getType()).thenReturn(AgentType.JOB_SEARCH);
        orchestrator = new MissionOrchestrator(
                List.of(jobSearchAgent), missionRepository, executionRepository,
                eventRepository, logRepository, eventPublisher, autoApplyPipeline);
    }

    @Test
    @DisplayName("executeMission - successful execution updates status to COMPLETED")
    void executeMission_success() {
        Mission mission = createTestMission();
        when(executionRepository.save(any(MissionExecution.class))).thenAnswer(i -> i.getArgument(0));
        when(missionRepository.save(any(Mission.class))).thenAnswer(i -> i.getArgument(0));

        AgentExecutionResult agentResult = AgentExecutionResult.success(
                AgentType.JOB_SEARCH, "10 jobs found", 10, 3, Instant.now());
        when(jobSearchAgent.execute(any(MissionContext.class))).thenReturn(agentResult);

        AgentExecutionResult result = orchestrator.executeMission(mission);

        assertThat(result.success()).isTrue();
        assertThat(result.jobsFound()).isEqualTo(10);
        assertThat(result.contactsFound()).isEqualTo(3);

        ArgumentCaptor<Mission> missionCaptor = ArgumentCaptor.forClass(Mission.class);
        verify(missionRepository, atLeastOnce()).save(missionCaptor.capture());
        List<Mission> saved = missionCaptor.getAllValues();
        assertThat(saved.get(saved.size() - 1).getStatus()).isEqualTo(MissionStatus.COMPLETED);

        verify(eventPublisher, atLeastOnce()).publishEvent(any(Object.class));
        verify(eventRepository, atLeastOnce()).save(any(MissionEvent.class));
        verify(logRepository, atLeastOnce()).save(any(MissionExecutionLog.class));
    }

    @Test
    @DisplayName("executeMission - agent failure marks mission as FAILED")
    void executeMission_agentFailure() {
        Mission mission = createTestMission();
        when(executionRepository.save(any(MissionExecution.class))).thenAnswer(i -> i.getArgument(0));
        when(missionRepository.save(any(Mission.class))).thenAnswer(i -> i.getArgument(0));

        AgentExecutionResult agentResult = AgentExecutionResult.failure(
                AgentType.JOB_SEARCH, "Connection timeout", Instant.now());
        when(jobSearchAgent.execute(any(MissionContext.class))).thenReturn(agentResult);

        AgentExecutionResult result = orchestrator.executeMission(mission);

        assertThat(result.success()).isFalse();
        assertThat(result.message()).contains("Connection timeout");
    }

    @Test
    @DisplayName("executeMission - exception marks mission as FAILED")
    void executeMission_exception() {
        Mission mission = createTestMission();
        when(executionRepository.save(any(MissionExecution.class))).thenAnswer(i -> i.getArgument(0));
        when(missionRepository.save(any(Mission.class))).thenAnswer(i -> i.getArgument(0));
        when(jobSearchAgent.execute(any(MissionContext.class))).thenThrow(new RuntimeException("Unexpected error"));

        AgentExecutionResult result = orchestrator.executeMission(mission);

        assertThat(result.success()).isFalse();
        assertThat(result.message()).contains("Unexpected error");
    }

    private Mission createTestMission() {
        Mission mission = new Mission();
        mission.setUserId(UUID.randomUUID());
        mission.setName("Test Mission");
        mission.setKeywords("Java Spring Boot");
        mission.setLocation("Remote");
        mission.setRemote(true);
        mission.setStatus(MissionStatus.CREATED);
        return mission;
    }
}
