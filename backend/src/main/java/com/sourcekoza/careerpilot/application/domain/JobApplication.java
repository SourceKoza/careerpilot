package com.sourcekoza.careerpilot.application.domain;

import com.sourcekoza.careerpilot.auth.domain.User;
import com.sourcekoza.careerpilot.common.BaseEntity;
import com.sourcekoza.careerpilot.job.domain.Job;
import com.sourcekoza.careerpilot.resume.domain.ResumeVersion;
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

/**
 * JobApplication aggregate root entity.
 *
 * <p>Represents a user's application to a specific job posting using a
 * particular resume version. This is the central business entity that links
 * User, Job, and ResumeVersion together.</p>
 *
 * <p>The ResumeVersion reference must never be modified after the application
 * has been submitted, preserving historical integrity.</p>
 *
 * <p>Uses optimistic locking and JPA auditing via {@link BaseEntity}.</p>
 */
@Entity
@Table(name = "job_applications", indexes = {
    @Index(name = "idx_application_user_id", columnList = "user_id"),
    @Index(name = "idx_application_job_id", columnList = "job_id"),
    @Index(name = "idx_application_status", columnList = "status"),
    @Index(name = "idx_application_applied_at", columnList = "applied_at")
})
public class JobApplication extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_version_id", nullable = false, updatable = false)
    private ResumeVersion resumeVersion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationStatus status = ApplicationStatus.DRAFT;

    @Column(name = "applied_at")
    private Instant appliedAt;

    @Column(length = 2000)
    private String notes;

    @Column(name = "external_application_id", length = 200)
    private String externalApplicationId;

    public JobApplication() {
        // JPA requires a no-arg constructor
    }

    // Getters and Setters

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public ResumeVersion getResumeVersion() {
        return resumeVersion;
    }

    public void setResumeVersion(ResumeVersion resumeVersion) {
        this.resumeVersion = resumeVersion;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public Instant getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Instant appliedAt) {
        this.appliedAt = appliedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getExternalApplicationId() {
        return externalApplicationId;
    }

    public void setExternalApplicationId(String externalApplicationId) {
        this.externalApplicationId = externalApplicationId;
    }
}
