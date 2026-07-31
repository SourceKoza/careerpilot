package com.sourcekoza.careerpilot.mission.mapper;

import com.sourcekoza.careerpilot.mission.dto.MissionCreateRequest;
import com.sourcekoza.careerpilot.mission.dto.DiscoveredJobResponse;
import com.sourcekoza.careerpilot.mission.dto.MissionEventResponse;
import com.sourcekoza.careerpilot.mission.dto.MissionExecutionResponse;
import com.sourcekoza.careerpilot.mission.dto.MissionLogResponse;
import com.sourcekoza.careerpilot.mission.dto.MissionResponse;
import com.sourcekoza.careerpilot.mission.entity.Mission;
import com.sourcekoza.careerpilot.mission.entity.ApplyMode;
import com.sourcekoza.careerpilot.mission.entity.MissionEvent;
import com.sourcekoza.careerpilot.mission.entity.MissionExecution;
import com.sourcekoza.careerpilot.mission.entity.MissionExecutionLog;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Manual mapper for mission domain objects.
 *
 * @since Sprint-15
 */
@Component
public class MissionMapper {

    public Mission toEntity(MissionCreateRequest request) {
        Mission mission = new Mission();
        mission.setName(request.name());
        mission.setKeywords(request.keywords());
        mission.setPreferredTitle(request.preferredTitle());
        mission.setExperienceLevel(request.experienceLevel());
        mission.setLocation(request.location());
        mission.setRemote(request.remote());
        mission.setHybrid(request.hybrid());
        mission.setSalaryMin(request.salaryMin());
        mission.setCurrency(request.currency());
        mission.setEmploymentType(request.employmentType());
        mission.setPlatforms(request.platforms() != null ? String.join(",", request.platforms()) : null);
        mission.setResumeId(parseUuid(request.resumeId()));
        mission.setSchedule(request.schedule());
        mission.setTimezone(request.timezone());
        if (request.applyMode() != null) {
            try {
                mission.setApplyMode(ApplyMode.valueOf(request.applyMode()));
            } catch (IllegalArgumentException e) {
                // Default stays SEMI_AUTO
            }
        }
        return mission;
    }

    public MissionResponse toResponse(Mission mission) {
        return new MissionResponse(
                mission.getId(),
                mission.getUserId(),
                mission.getName(),
                mission.getKeywords(),
                mission.getPreferredTitle(),
                mission.getExperienceLevel(),
                mission.getLocation(),
                mission.isRemote(),
                mission.isHybrid(),
                mission.getSalaryMin(),
                mission.getCurrency(),
                mission.getEmploymentType(),
                parsePlatforms(mission.getPlatforms()),
                mission.getResumeId(),
                mission.getSchedule(),
                mission.getTimezone(),
                mission.getStatus(),
                mission.getApplyMode().name(),
                mission.getCreatedAt(),
                mission.getUpdatedAt()
        );
    }

    public MissionExecutionResponse toExecutionResponse(MissionExecution execution) {
        return new MissionExecutionResponse(
                execution.getId(),
                execution.getMission().getId(),
                execution.getStatus(),
                execution.getStartedAt(),
                execution.getCompletedAt(),
                execution.getDurationMs(),
                execution.getJobsFound(),
                execution.getContactsFound(),
                execution.getErrorMessage()
        );
    }

    public MissionEventResponse toEventResponse(MissionEvent event) {
        return new MissionEventResponse(
                event.getId(),
                event.getMission().getId(),
                event.getExecutionId(),
                event.getEventType(),
                event.getMessage(),
                event.getEventTime()
        );
    }

    public MissionLogResponse toLogResponse(MissionExecutionLog log) {
        return new MissionLogResponse(
                log.getId(),
                log.getExecutionId(),
                log.getLevel(),
                log.getMessage(),
                log.getLogTime()
        );
    }

    public DiscoveredJobResponse toDiscoveredJobResponse(
            com.sourcekoza.careerpilot.mission.entity.DiscoveredJob job) {
        return new DiscoveredJobResponse(
                job.getId(),
                job.getMission().getId(),
                job.getPlatform(),
                job.getExternalJobId(),
                job.getTitle(),
                job.getCompany(),
                job.getLocation(),
                job.getSalary(),
                job.getDescription(),
                job.getJobUrl(),
                job.getJobStatus(),
                job.getMatchScore(),
                job.getMatchReason(),
                job.getTailoredResumeId(),
                job.getCreatedAt()
        );
    }

    private List<String> parsePlatforms(String platforms) {
        if (platforms == null || platforms.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.asList(platforms.split(","));
    }

    private UUID parseUuid(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
