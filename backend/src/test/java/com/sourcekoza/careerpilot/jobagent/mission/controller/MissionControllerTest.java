package com.sourcekoza.careerpilot.jobagent.mission.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcekoza.careerpilot.auth.domain.Role;
import com.sourcekoza.careerpilot.auth.domain.User;
import com.sourcekoza.careerpilot.auth.repository.UserRepository;
import com.sourcekoza.careerpilot.auth.service.JwtService;
import com.sourcekoza.careerpilot.jobagent.mission.dto.MissionCreateRequest;
import com.sourcekoza.careerpilot.jobagent.mission.dto.MissionResponse;
import com.sourcekoza.careerpilot.jobagent.mission.entity.MissionStatus;
import com.sourcekoza.careerpilot.jobagent.mission.service.MissionExecutionService;
import com.sourcekoza.careerpilot.jobagent.mission.service.MissionService;
import com.sourcekoza.careerpilot.security.CustomUserDetailsService;
import com.sourcekoza.careerpilot.security.JwtAuthenticationEntryPoint;
import com.sourcekoza.careerpilot.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MissionController.class)
@AutoConfigureMockMvc(addFilters = false)
class MissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MissionService missionService;

    @MockitoBean
    private MissionExecutionService executionService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final UUID userId = UUID.randomUUID();
    private final UUID missionId = UUID.randomUUID();
    private final String testEmail = "test@example.com";

    @BeforeEach
    void setUp() throws Exception {
        User user = User.builder()
                .firstName("Test")
                .lastName("User")
                .email(testEmail)
                .password("encoded")
                .role(Role.ROLE_USER)
                .build();
        Field idField = user.getClass().getSuperclass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, userId);

        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(user));
    }

    @Test
    @DisplayName("POST /api/v1/missions - creates mission successfully")
    @WithMockUser(username = "test@example.com")
    void createMission_success() throws Exception {
        MissionCreateRequest request = new MissionCreateRequest(
                "Test Mission", "Java Spring Boot", "Senior Engineer", "Senior",
                "Remote", true, false, 150000, "USD", "Full-time",
                List.of("LinkedIn", "Indeed"), null, "Daily", "UTC", null);

        MissionResponse response = new MissionResponse(
                missionId, userId, "Test Mission", "Java Spring Boot", "Senior Engineer",
                "Senior", "Remote", true, false, 150000, "USD", "Full-time",
                List.of("LinkedIn", "Indeed"), null, "Daily", "UTC",
                MissionStatus.CREATED, "SEMI_AUTO", Instant.now(), Instant.now());

        when(missionService.createMission(any(UUID.class), any(MissionCreateRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/missions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Test Mission"));
    }

    @Test
    @DisplayName("GET /api/v1/missions/{id} - returns mission details")
    @WithMockUser(username = "test@example.com")
    void getMission_success() throws Exception {
        MissionResponse response = new MissionResponse(
                missionId, userId, "Test Mission", "Java", null, null,
                "Remote", true, false, null, null, null,
                List.of(), null, null, null,
                MissionStatus.CREATED, "SEMI_AUTO", Instant.now(), Instant.now());

        when(missionService.getMission(any(UUID.class), eq(missionId))).thenReturn(response);

        mockMvc.perform(get("/api/v1/missions/" + missionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(missionId.toString()));
    }
}
