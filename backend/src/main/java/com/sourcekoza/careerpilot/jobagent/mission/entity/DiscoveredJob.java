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

import java.util.UUID;

/**
 * A job discovered during a mission execution.
 *
 * @since Sprint-15
 */
@Entity
@Table(name = "discovered_jobs", indexes = {
        @Index(name = "idx_disc_job_mission_id", columnList = "mission_id"),
        @Index(name = "idx_disc_job_platform", columnList = "platform"),
        @Index(name = "idx_disc_job_external_id", columnList = "external_job_id")
})
public class DiscoveredJob extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Column(name = "execution_id")
    private UUID executionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PlatformType platform;

    @Column(name = "external_job_id", length = 200)
    private String externalJobId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 200)
    private String company;

    @Column(length = 200)
    private String location;

    @Column(length = 100)
    private String salary;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "job_url", length = 2048)
    private String jobUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_status", nullable = false, length = 20)
    private DiscoveredJobStatus jobStatus = DiscoveredJobStatus.NEW;

    @Column(name = "match_score")
    private Integer matchScore;

    @Column(name = "match_reason", length = 500)
    private String matchReason;

    @Column(name = "tailored_resume_id")
    private UUID tailoredResumeId;

    public DiscoveredJob() {
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

    public PlatformType getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformType platform) {
        this.platform = platform;
    }

    public String getExternalJobId() {
        return externalJobId;
    }

    public void setExternalJobId(String externalJobId) {
        this.externalJobId = externalJobId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    public DiscoveredJobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(DiscoveredJobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Integer getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Integer matchScore) {
        this.matchScore = matchScore;
    }

    public String getMatchReason() {
        return matchReason;
    }

    public void setMatchReason(String matchReason) {
        this.matchReason = matchReason;
    }

    public UUID getTailoredResumeId() {
        return tailoredResumeId;
    }

    public void setTailoredResumeId(UUID tailoredResumeId) {
        this.tailoredResumeId = tailoredResumeId;
    }
}
