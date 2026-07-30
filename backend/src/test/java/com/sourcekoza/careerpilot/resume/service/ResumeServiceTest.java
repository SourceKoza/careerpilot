package com.sourcekoza.careerpilot.resume.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcekoza.careerpilot.exception.ResourceNotFoundException;
import com.sourcekoza.careerpilot.resume.domain.Resume;
import com.sourcekoza.careerpilot.resume.domain.ResumeVersion;
import com.sourcekoza.careerpilot.resume.dto.CreateResumeRequest;
import com.sourcekoza.careerpilot.resume.dto.ResumeResponse;
import com.sourcekoza.careerpilot.resume.dto.ResumeSummaryResponse;
import com.sourcekoza.careerpilot.resume.dto.ResumeVersionResponse;
import com.sourcekoza.careerpilot.resume.dto.UpdateResumeRequest;
import com.sourcekoza.careerpilot.resume.mapper.ResumeMapper;
import com.sourcekoza.careerpilot.resume.repository.ResumeRepository;
import com.sourcekoza.careerpilot.resume.repository.ResumeVersionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResumeServiceTest {

    @Mock
    private ResumeRepository resumeRepository;

    @Mock
    private ResumeVersionRepository resumeVersionRepository;

    @Mock
    private ResumeMapper resumeMapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ResumeService resumeService;

    private final UUID userId = UUID.randomUUID();
    private final UUID resumeId = UUID.randomUUID();

    @Test
    @DisplayName("createResume - maps request to entity, sets userId, saves, and returns response")
    void createResume_success() {
        // Arrange
        CreateResumeRequest request = new CreateResumeRequest(
                "Software Engineer Resume", "Summary text", "Senior Engineer",
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptySet(), Collections.emptySet(),
                Collections.emptyList(), Collections.emptySet());

        Resume entity = Resume.builder().title("Software Engineer Resume").build();
        Resume savedEntity = Resume.builder().userId(userId).title("Software Engineer Resume").build();
        ResumeResponse expectedResponse = new ResumeResponse(
                resumeId, userId, "Software Engineer Resume", "Summary text", "Senior Engineer",
                List.of(), List.of(), Collections.emptySet(), Collections.emptySet(),
                List.of(), Collections.emptySet(), Instant.now(), Instant.now());

        when(resumeMapper.toEntity(request)).thenReturn(entity);
        when(resumeRepository.save(entity)).thenReturn(savedEntity);
        when(resumeMapper.toResponse(savedEntity)).thenReturn(expectedResponse);

        // Act
        ResumeResponse result = resumeService.createResume(userId, request);

        // Assert
        verify(resumeMapper).toEntity(request);
        verify(resumeRepository).save(entity);
        verify(resumeMapper).toResponse(savedEntity);
        assertThat(entity.getUserId()).isEqualTo(userId);
        assertThat(result).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("getResume - returns response when resume found for user")
    void getResume_success() {
        // Arrange
        Resume resume = Resume.builder().userId(userId).title("My Resume").build();
        ResumeResponse expectedResponse = new ResumeResponse(
                resumeId, userId, "My Resume", null, null,
                List.of(), List.of(), Collections.emptySet(), Collections.emptySet(),
                List.of(), Collections.emptySet(), Instant.now(), Instant.now());

        when(resumeRepository.findByIdAndUserIdAndDeletedAtIsNull(resumeId, userId))
                .thenReturn(Optional.of(resume));
        when(resumeMapper.toResponse(resume)).thenReturn(expectedResponse);

        // Act
        ResumeResponse result = resumeService.getResume(userId, resumeId);

        // Assert
        verify(resumeRepository).findByIdAndUserIdAndDeletedAtIsNull(resumeId, userId);
        verify(resumeMapper).toResponse(resume);
        assertThat(result).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("getResume - throws ResourceNotFoundException when resume not found")
    void getResume_notFound() {
        // Arrange
        when(resumeRepository.findByIdAndUserIdAndDeletedAtIsNull(resumeId, userId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> resumeService.getResume(userId, resumeId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resume");
    }

    @Test
    @DisplayName("updateResume - creates version snapshot, updates entity, saves, returns response")
    void updateResume_success() throws Exception {
        // Arrange
        UpdateResumeRequest request = new UpdateResumeRequest(
                "Updated Title", "Updated Summary", "CTO",
                Collections.emptyList(), Collections.emptyList(),
                Collections.emptySet(), Collections.emptySet(),
                Collections.emptyList(), Collections.emptySet());

        Resume resume = Resume.builder().userId(userId).title("Original Title").build();
        Resume savedResume = Resume.builder().userId(userId).title("Updated Title").build();
        ResumeResponse responseBeforeUpdate = new ResumeResponse(
                resumeId, userId, "Original Title", null, null,
                List.of(), List.of(), Collections.emptySet(), Collections.emptySet(),
                List.of(), Collections.emptySet(), Instant.now(), Instant.now());
        ResumeResponse expectedResponse = new ResumeResponse(
                resumeId, userId, "Updated Title", "Updated Summary", "CTO",
                List.of(), List.of(), Collections.emptySet(), Collections.emptySet(),
                List.of(), Collections.emptySet(), Instant.now(), Instant.now());

        when(resumeRepository.findByIdAndUserIdAndDeletedAtIsNull(resumeId, userId))
                .thenReturn(Optional.of(resume));
        when(resumeMapper.toResponse(resume)).thenReturn(responseBeforeUpdate);
        when(objectMapper.writeValueAsString(responseBeforeUpdate)).thenReturn("{\"title\":\"Original\"}");
        when(resumeVersionRepository.countByResumeId(any())).thenReturn(0L);
        when(resumeRepository.save(resume)).thenReturn(savedResume);
        when(resumeMapper.toResponse(savedResume)).thenReturn(expectedResponse);

        // Act
        ResumeResponse result = resumeService.updateResume(userId, resumeId, request);

        // Assert
        verify(resumeVersionRepository).save(any(ResumeVersion.class));
        verify(resumeMapper).updateEntity(eq(request), eq(resume));
        verify(resumeRepository).save(resume);
        assertThat(result).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("deleteResume - performs physical delete")
    void deleteResume_success() {
        // Arrange
        Resume resume = Resume.builder().userId(userId).title("To Delete").build();

        when(resumeRepository.findByIdAndUserIdAndDeletedAtIsNull(resumeId, userId))
                .thenReturn(Optional.of(resume));

        // Act
        resumeService.deleteResume(userId, resumeId);

        // Assert
        verify(resumeRepository).delete(resume);
    }

    @Test
    @DisplayName("listResumes - calls repository with pageable and maps results")
    void listResumes_success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Resume resume = Resume.builder().userId(userId).title("Resume 1").build();
        Page<Resume> page = new PageImpl<>(List.of(resume), pageable, 1);
        ResumeSummaryResponse summaryResponse = new ResumeSummaryResponse(
                resumeId, "Resume 1", null, null, Instant.now(), Instant.now());

        when(resumeRepository.findByUserIdAndDeletedAtIsNull(userId, pageable)).thenReturn(page);
        when(resumeMapper.toSummaryResponse(resume)).thenReturn(summaryResponse);

        // Act
        Page<ResumeSummaryResponse> result = resumeService.listResumes(userId, pageable);

        // Assert
        verify(resumeRepository).findByUserIdAndDeletedAtIsNull(userId, pageable);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(summaryResponse);
    }

    @Test
    @DisplayName("getVersions - verifies ownership then returns versions ordered by version number desc")
    void getVersions_success() {
        // Arrange
        Resume resume = Resume.builder().userId(userId).title("Resume").build();
        ResumeVersion version = ResumeVersion.builder()
                .resume(resume).versionNumber(1).markdownContent("# Resume").build();
        ResumeVersionResponse versionResponse = new ResumeVersionResponse(
                UUID.randomUUID(), resumeId, 1, "# Resume", null, null, Instant.now());

        when(resumeRepository.findByIdAndUserIdAndDeletedAtIsNull(resumeId, userId))
                .thenReturn(Optional.of(resume));
        when(resumeVersionRepository.findByResumeIdOrderByVersionNumberDesc(resumeId))
                .thenReturn(List.of(version));
        when(resumeMapper.toVersionResponse(version)).thenReturn(versionResponse);

        // Act
        List<ResumeVersionResponse> result = resumeService.getVersions(userId, resumeId);

        // Assert
        verify(resumeRepository).findByIdAndUserIdAndDeletedAtIsNull(resumeId, userId);
        verify(resumeVersionRepository).findByResumeIdOrderByVersionNumberDesc(resumeId);
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(versionResponse);
    }
}
