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

import java.util.UUID;

/**
 * A recruiter or hiring manager contact discovered during a mission.
 *
 * @since Sprint-15
 */
@Entity
@Table(name = "mission_contacts", indexes = {
        @Index(name = "idx_contact_mission_id", columnList = "mission_id"),
        @Index(name = "idx_contact_job_id", columnList = "job_id")
})
public class MissionContact extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Column(name = "job_id")
    private UUID jobId;

    @Column(name = "execution_id")
    private UUID executionId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 255)
    private String email;

    @Column(name = "linkedin_url", length = 500)
    private String linkedInUrl;

    @Column(length = 100)
    private String role;

    @Column(name = "confidence_score")
    private Double confidenceScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ContactSource source = ContactSource.UNKNOWN;

    public MissionContact() {
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public UUID getExecutionId() {
        return executionId;
    }

    public void setExecutionId(UUID executionId) {
        this.executionId = executionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    public void setLinkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public ContactSource getSource() {
        return source;
    }

    public void setSource(ContactSource source) {
        this.source = source;
    }
}
