package com.sourcekoza.careerpilot.mission.entity;

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
 * Detailed execution log entry for a mission run.
 *
 * @since Sprint-15
 */
@Entity
@Table(name = "mission_execution_logs", indexes = {
        @Index(name = "idx_log_mission_id", columnList = "mission_id"),
        @Index(name = "idx_log_execution_id", columnList = "execution_id")
})
public class MissionExecutionLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Column(name = "execution_id", nullable = false)
    private UUID executionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private LogLevel level = LogLevel.INFO;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(name = "log_time", nullable = false)
    private Instant logTime;

    public MissionExecutionLog() {
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

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getLogTime() {
        return logTime;
    }

    public void setLogTime(Instant logTime) {
        this.logTime = logTime;
    }
}
