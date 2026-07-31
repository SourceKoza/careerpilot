package com.sourcekoza.careerpilot.jobagent.agents.jobsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcekoza.careerpilot.ai.llm.LlmService;
import com.sourcekoza.careerpilot.jobagent.agents.core.AgentExecutionResult;
import com.sourcekoza.careerpilot.jobagent.agents.core.AgentType;
import com.sourcekoza.careerpilot.jobagent.agents.core.MissionContext;
import com.sourcekoza.careerpilot.jobagent.agents.platform.JobPlatformAdapter;
import com.sourcekoza.careerpilot.jobagent.agents.platform.JobSearchRequest;
import com.sourcekoza.careerpilot.jobagent.agents.platform.JobSearchResult;
import com.sourcekoza.careerpilot.jobagent.mission.entity.DiscoveredJob;
import com.sourcekoza.careerpilot.jobagent.mission.entity.Mission;
import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionContact;
import com.sourcekoza.careerpilot.jobagent.mission.entity.PlatformType;
import com.sourcekoza.careerpilot.jobagent.mission.repository.DiscoveredJobRepository;
import com.sourcekoza.careerpilot.jobagent.mission.repository.MissionContactRepository;
import com.sourcekoza.careerpilot.resume.repository.ResumeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MissionJobSearchAgentTest {

    @Mock
    private JobPlatformAdapter adapter1;
    @Mock
    private JobPlatformAdapter adapter2;
    @Mock
    private DiscoveredJobRepository discoveredJobRepository;
    @Mock
    private MissionContactRepository contactRepository;
    @Mock
    private ResumeRepository resumeRepository;
    @Mock
    private LlmService llmService;

    private MissionJobSearchAgent agent;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        when(adapter1.platform()).thenReturn(PlatformType.INDEED);
        when(adapter2.platform()).thenReturn(PlatformType.GREENHOUSE);
        agent = new MissionJobSearchAgent(
                List.of(adapter1, adapter2), discoveredJobRepository, contactRepository,
                resumeRepository, llmService, objectMapper);
    }

    @Test
    @DisplayName("getType returns JOB_SEARCH")
    void getType() {
        assertThat(agent.getType()).isEqualTo(AgentType.JOB_SEARCH);
    }

    @Test
    @DisplayName("execute - uses LLM for keyword optimization and job scoring")
    void execute_usesLlm() {
        Mission mission = new Mission();
        mission.setUserId(UUID.randomUUID());
        mission.setKeywords("Java Spring Boot");
        mission.setLocation("Remote");
        mission.setRemote(true);
        mission.setPreferredTitle("Senior Backend Engineer");
        MissionContext context = new MissionContext(mission, UUID.randomUUID(), mission.getUserId());

        // Mock: no resume found
        when(resumeRepository.findByUserIdAndDeletedAtIsNull(any(UUID.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        // Mock: LLM returns optimized keywords
        when(llmService.chat(any(), any()))
                .thenReturn("Java Spring Boot Microservices Senior Backend")  // keyword optimization
                .thenReturn("{\"score\": 85, \"reason\": \"Strong match for Java skills\"}")  // job 1 scoring
                .thenReturn("{\"score\": 72, \"reason\": \"Partial match\"}")  // job 2 scoring
                .thenReturn("none")  // email extraction for job 1
                .thenReturn("none"); // email extraction for job 2

        List<JobSearchResult> results1 = List.of(
                JobSearchResult.builder().platform(PlatformType.INDEED).title("Java Dev")
                        .company("Corp A").description("Java role").recruiterName("John").build());
        List<JobSearchResult> results2 = List.of(
                JobSearchResult.builder().platform(PlatformType.GREENHOUSE).title("Spring Engineer")
                        .company("Corp B").description("Spring role").build());

        when(adapter1.search(any(JobSearchRequest.class))).thenReturn(results1);
        when(adapter2.search(any(JobSearchRequest.class))).thenReturn(results2);
        when(discoveredJobRepository.save(any(DiscoveredJob.class))).thenAnswer(i -> i.getArgument(0));
        when(contactRepository.save(any(MissionContact.class))).thenAnswer(i -> i.getArgument(0));

        AgentExecutionResult result = agent.execute(context);

        assertThat(result.success()).isTrue();
        assertThat(result.jobsFound()).isEqualTo(2);
        assertThat(result.contactsFound()).isEqualTo(1);

        // Verify LLM was called: 1 for keywords + 2 for scoring + 2 for email extraction = 5 calls
        verify(llmService, times(5)).chat(anyString(), anyString());
        verify(discoveredJobRepository, times(2)).save(any(DiscoveredJob.class));
    }

    @Test
    @DisplayName("execute - gracefully handles LLM failure")
    void execute_llmFailure() {
        Mission mission = new Mission();
        mission.setUserId(UUID.randomUUID());
        mission.setKeywords("Python");
        mission.setRemote(false);
        MissionContext context = new MissionContext(mission, UUID.randomUUID(), mission.getUserId());

        when(resumeRepository.findByUserIdAndDeletedAtIsNull(any(UUID.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        // LLM fails
        when(llmService.chat(any(), any())).thenThrow(new RuntimeException("LLM timeout"));

        when(adapter1.search(any(JobSearchRequest.class))).thenReturn(List.of(
                JobSearchResult.builder().platform(PlatformType.INDEED).title("Python Dev")
                        .company("Corp").description("Python role").build()));
        when(adapter2.search(any(JobSearchRequest.class))).thenReturn(List.of());
        when(discoveredJobRepository.save(any(DiscoveredJob.class))).thenAnswer(i -> i.getArgument(0));

        AgentExecutionResult result = agent.execute(context);

        // Should still succeed — LLM failure is non-fatal
        assertThat(result.success()).isTrue();
        assertThat(result.jobsFound()).isEqualTo(1);
    }
}
