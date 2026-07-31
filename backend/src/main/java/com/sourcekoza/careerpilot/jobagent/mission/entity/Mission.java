package com.sourcekoza.careerpilot.jobagent.mission.entity;

import com.sourcekoza.careerpilot.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.util.UUID;

/**
 * Mission entity representing a user's automated job search mission.
 *
 * <p>A mission encapsulates the user's intent: what kind of jobs to search for,
 * on which platforms, and with what criteria. The mission execution engine
 * processes missions by invoking AI agents in sequence.</p>
 *
 * @since Sprint-15
 */
@Entity
@Table(name = "missions", indexes = {
        @Index(name = "idx_mission_user_id", columnList = "user_id"),
        @Index(name = "idx_mission_status", columnList = "status")
})
public class Mission extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 200)
    private String keywords;

    @Column(name = "preferred_title", length = 200)
    private String preferredTitle;

    @Column(name = "experience_level", length = 50)
    private String experienceLevel;

    @Column(length = 200)
    private String location;

    @Column(nullable = false)
    private boolean remote;

    @Column(nullable = false)
    private boolean hybrid;

    @Column(name = "salary_min")
    private Integer salaryMin;

    @Column(length = 10)
    private String currency;

    @Column(name = "employment_type", length = 30)
    private String employmentType;

    @Column(length = 500)
    private String platforms;

    @Column(name = "resume_id")
    private UUID resumeId;

    @Column(length = 50)
    private String schedule;

    @Column(length = 50)
    private String timezone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MissionStatus status = MissionStatus.CREATED;

    @Enumerated(EnumType.STRING)
    @Column(name = "apply_mode", nullable = false, length = 20)
    private ApplyMode applyMode = ApplyMode.SEMI_AUTO;

    public Mission() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getPreferredTitle() {
        return preferredTitle;
    }

    public void setPreferredTitle(String preferredTitle) {
        this.preferredTitle = preferredTitle;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public boolean isHybrid() {
        return hybrid;
    }

    public void setHybrid(boolean hybrid) {
        this.hybrid = hybrid;
    }

    public Integer getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(Integer salaryMin) {
        this.salaryMin = salaryMin;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getPlatforms() {
        return platforms;
    }

    public void setPlatforms(String platforms) {
        this.platforms = platforms;
    }

    public UUID getResumeId() {
        return resumeId;
    }

    public void setResumeId(UUID resumeId) {
        this.resumeId = resumeId;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public MissionStatus getStatus() {
        return status;
    }

    public void setStatus(MissionStatus status) {
        this.status = status;
    }

    public ApplyMode getApplyMode() {
        return applyMode;
    }

    public void setApplyMode(ApplyMode applyMode) {
        this.applyMode = applyMode;
    }
}
