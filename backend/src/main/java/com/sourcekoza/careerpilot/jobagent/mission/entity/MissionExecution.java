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

import java.time.Duration;
import java.time.Instant;

/**
 * Records a single execution run of a mission.
 *
 * @since Sprint-15
 */
@Entity
@Table(name = "mission_executions", indexes = {
        @Index(name = "idx_execution_mission_id", columnList = "mission_id"),
        @Index(name = "idx_execution_status", columnList = "status")
})
public class MissionExecution extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ExecutionStatus status = ExecutionStatus.RUNNING;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Column(name = "jobs_found")
    private int jobsFound;

    @Column(name = "contacts_found")
    private int contactsFound;

    @Column(name = "error_message", length = 2000)
    private String errorMessage;

    public MissionExecution() {
    }

    public void markCompleted(int jobsFound, int contactsFound) {
        this.status = ExecutionStatus.COMPLETED;
        this.completedAt = Instant.now();
        this.durationMs = Duration.between(this.startedAt, this.completedAt).toMillis();
        this.jobsFound = jobsFound;
        this.contactsFound = contactsFound;
    }

    public void markFailed(String errorMessage) {
        this.status = ExecutionStatus.FAILED;
        this.completedAt = Instant.now();
        this.durationMs = Duration.between(this.startedAt, this.completedAt).toMillis();
        this.errorMessage = errorMessage;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }

    public int getJobsFound() {
        return jobsFound;
    }

    public void setJobsFound(int jobsFound) {
        this.jobsFound = jobsFound;
    }

    public int getContactsFound() {
        return contactsFound;
    }

    public void setContactsFound(int contactsFound) {
        this.contactsFound = contactsFound;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
