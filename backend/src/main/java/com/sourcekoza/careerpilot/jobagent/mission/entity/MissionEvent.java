package com.sourcekoza.careerpilot.jobagent.mission.entity;

import com.sourcekoza.careerpilot.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Persistent record of a mission lifecycle event.
 *
 * @since Sprint-15
 */
@Entity
@Table(name = "mission_events", indexes = {
        @Index(name = "idx_event_mission_id", columnList = "mission_id"),
        @Index(name = "idx_event_execution_id", columnList = "execution_id"),
        @Index(name = "idx_event_type", columnList = "event_type")
})
public class MissionEvent extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Column(name = "execution_id")
    private UUID executionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 30)
    private MissionEventType eventType;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(name = "event_time", nullable = false)
    private Instant eventTime;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    public MissionEvent() {
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public UUID getExecutionId() {
        return executionId;
    }

    public void setExecutionId(UUID executionId) {
        this.executionId = executionId;
    }

    public MissionEventType getEventType() {
        return eventType;
    }

    public void setEventType(MissionEventType eventType) {
        this.eventType = eventType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getEventTime() {
        return eventTime;
    }

    public void setEventTime(Instant eventTime) {
        this.eventTime = eventTime;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
