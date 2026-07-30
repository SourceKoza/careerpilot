package com.sourcekoza.careerpilot.resume.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcekoza.careerpilot.auth.domain.Role;
import com.sourcekoza.careerpilot.auth.domain.User;
import com.sourcekoza.careerpilot.auth.repository.UserRepository;
import com.sourcekoza.careerpilot.auth.service.JwtService;
import com.sourcekoza.careerpilot.exception.ResourceNotFoundException;
import com.sourcekoza.careerpilot.resume.dto.CreateResumeRequest;
import com.sourcekoza.careerpilot.resume.dto.ResumeResponse;
import com.sourcekoza.careerpilot.resume.dto.ResumeSummaryResponse;
import com.sourcekoza.careerpilot.resume.dto.UpdateResumeRequest;
import com.sourcekoza.careerpilot.resume.service.ResumeService;
import com.sourcekoza.careerpilot.security.CustomUserDetailsService;
import com.sourcekoza.careerpilot.security.JwtAuthenticationEntryPoint;
import com.sourcekoza.careerpilot.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResumeController.class)
@AutoConfigureMockMvc(addFilters = false)
class ResumeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ResumeService resumeService;

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
    private final UUID resumeId = UUID.randomUUID();
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
        // Set id via reflection since BaseEntity.id is managed by JPA
        Field idField = user.getClass().getSuperclass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, userId);

        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(user));
    }

    @Test
    @DisplayName("POST /api/v1/resumes - valid request returns 201")
    @WithMockUser(username = "test@example.com")
    void createResume_valid() throws Exception {
        // Arrange
        CreateResumeRequest request = new CreateResumeRequest(
                "My Resume", "Summary", "Engineer",
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptySet(), Collections.emptySet(),
                Collections.emptyList(), Collections.emptySet());

        ResumeResponse response = new ResumeResponse(
                resumeId, userId, "My Resume", "Summary", "Engineer",
                List.of(), List.of(), Collections.emptySet(), Collections.emptySet(),
                List.of(), Collections.emptySet(), Instant.now(), Instant.now());

        when(resumeService.createResume(any(UUID.class), any(CreateResumeRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/resumes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("My Resume"));
    }

    @Test
    @DisplayName("POST /api/v1/resumes - blank title returns 400")
    @WithMockUser(username = "test@example.com")
    void createResume_invalidBlankTitle() throws Exception {
        // Arrange
        CreateResumeRequest request = new CreateResumeRequest(
                "", null, null,
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptySet(), Collections.emptySet(),
                Collections.emptyList(), Collections.emptySet());

        // Act & Assert
        mockMvc.perform(post("/api/v1/resumes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v1/resumes/{id} - found returns 200")
    @WithMockUser(username = "test@example.com")
    void getResume_found() throws Exception {
        // Arrange
        ResumeResponse response = new ResumeResponse(
                resumeId, userId, "My Resume", "Summary", "Engineer",
                List.of(), List.of(), Collections.emptySet(), Collections.emptySet(),
                List.of(), Collections.emptySet(), Instant.now(), Instant.now());

        when(resumeService.getResume(any(UUID.class), eq(resumeId))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/resumes/{id}", resumeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(resumeId.toString()));
    }

    @Test
    @DisplayName("GET /api/v1/resumes/{id} - not found returns 404")
    @WithMockUser(username = "test@example.com")
    void getResume_notFound() throws Exception {
        // Arrange
        when(resumeService.getResume(any(UUID.class), eq(resumeId)))
                .thenThrow(new ResourceNotFoundException("Resume", "id", resumeId));

        // Act & Assert
        mockMvc.perform(get("/api/v1/resumes/{id}", resumeId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/v1/resumes/{id} - valid update returns 200")
    @WithMockUser(username = "test@example.com")
    void updateResume_valid() throws Exception {
        // Arrange
        UpdateResumeRequest request = new UpdateResumeRequest(
                "Updated Title", "Updated Summary", "CTO",
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptySet(), Collections.emptySet(),
                Collections.emptyList(), Collections.emptySet());

        ResumeResponse response = new ResumeResponse(
                resumeId, userId, "Updated Title", "Updated Summary", "CTO",
                List.of(), List.of(), Collections.emptySet(), Collections.emptySet(),
                List.of(), Collections.emptySet(), Instant.now(), Instant.now());

        when(resumeService.updateResume(any(UUID.class), eq(resumeId), any(UpdateResumeRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/api/v1/resumes/{id}", resumeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Updated Title"));
    }

    @Test
    @DisplayName("DELETE /api/v1/resumes/{id} - returns 204")
    @WithMockUser(username = "test@example.com")
    void deleteResume_success() throws Exception {
        // Arrange
        doNothing().when(resumeService).deleteResume(any(UUID.class), eq(resumeId));

        // Act & Assert
        mockMvc.perform(delete("/api/v1/resumes/{id}", resumeId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/resumes - returns 200 with paginated response")
    @WithMockUser(username = "test@example.com")
    void listResumes_success() throws Exception {
        // Arrange
        ResumeSummaryResponse summary = new ResumeSummaryResponse(
                resumeId, "My Resume", "Summary", "Engineer", Instant.now(), Instant.now());
        Page<ResumeSummaryResponse> page = new PageImpl<>(
                List.of(summary), PageRequest.of(0, 10), 1);

        when(resumeService.listResumes(any(UUID.class), any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/v1/resumes")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].title").value("My Resume"))
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }
}
