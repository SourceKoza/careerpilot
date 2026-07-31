package com.sourcekoza.careerpilot.jobagent.mission.service;

import com.sourcekoza.careerpilot.jobagent.agents.core.AgentExecutionResult;
import com.sourcekoza.careerpilot.jobagent.agents.core.AgentType;
import com.sourcekoza.careerpilot.jobagent.mission.dto.MissionStartResponse;
import com.sourcekoza.careerpilot.jobagent.mission.entity.ExecutionStatus;
import com.sourcekoza.careerpilot.jobagent.mission.entity.Mission;
import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionStatus;
import com.sourcekoza.careerpilot.jobagent.mission.mapper.MissionMapper;
import com.sourcekoza.careerpilot.jobagent.mission.orchestrator.MissionOrchestrator;
import com.sourcekoza.careerpilot.jobagent.mission.repository.MissionEventRepository;
import com.sourcekoza.careerpilot.jobagent.mission.repository.MissionExecutionLogRepository;
import com.sourcekoza.careerpilot.jobagent.mission.repository.MissionExecutionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MissionExecutionServiceTest {

    @Mock
    private MissionService missionService;
    @Mock
    private MissionOrchestrator orchestrator;
    @Mock
    private MissionExecutionRepository executionRepository;
    @Mock
    private MissionEventRepository eventRepository;
    @Mock
    private MissionExecutionLogRepository logRepository;
    @Mock
    private com.sourcekoza.careerpilot.jobagent.mission.repository.DiscoveredJobRepository discoveredJobRepository;
    @Mock
    private MissionMapper missionMapper;
    @Mock
    private com.sourcekoza.careerpilot.jobagent.agents.email.EmailOutreachAgent emailOutreachAgent;

    @InjectMocks
    private MissionExecutionService executionService;

    private final UUID userId = UUID.randomUUID();
    private final UUID missionId = UUID.randomUUID();

    @Test
    @DisplayName("startMission - returns RUNNING when execution starts")
    void startMission_success() {
        Mission mission = new Mission();
        mission.setStatus(MissionStatus.CREATED);
        mission.setKeywords("Java");
        when(missionService.findMissionEntity(userId, missionId)).thenReturn(mission);

        MissionStartResponse response = executionService.startMission(userId, missionId);

        assertThat(response.status()).isEqualTo(ExecutionStatus.RUNNING);
        assertThat(response.missionId()).isEqualTo(missionId);
    }

    @Test
    @DisplayName("startMission - returns RUNNING for any valid mission")
    void startMission_failure() {
        Mission mission = new Mission();
        mission.setStatus(MissionStatus.COMPLETED);
        mission.setKeywords("Java");
        when(missionService.findMissionEntity(userId, missionId)).thenReturn(mission);

        MissionStartResponse response = executionService.startMission(userId, missionId);

        assertThat(response.status()).isEqualTo(ExecutionStatus.RUNNING);
    }

    @Test
    @DisplayName("startMission - throws when mission is already RUNNING")
    void startMission_alreadyRunning() {
        Mission mission = new Mission();
        mission.setStatus(MissionStatus.RUNNING);
        when(missionService.findMissionEntity(userId, missionId)).thenReturn(mission);

        assertThatThrownBy(() -> executionService.startMission(userId, missionId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already running");
    }
}
