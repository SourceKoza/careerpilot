package com.sourcekoza.careerpilot.resume.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sourcekoza.careerpilot.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Immutable version snapshot of a Resume.
 *
 * <p>Stores the full resume state as a JSONB column at the time of update.
 * Versions are historical records and are never modified or deleted through
 * normal operations.</p>
 */
@Entity
@Table(name = "resume_versions", indexes = {
    @Index(name = "idx_version_resume_id", columnList = "resume_id")
})
public class ResumeVersion extends BaseEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Column(columnDefinition = "jsonb", nullable = false)
    private String content;

    @Column(name = "change_summary", length = 500)
    private String changeSummary;

    public ResumeVersion() {
        // JPA requires a no-arg constructor
    }

    private ResumeVersion(Builder builder) {
        this.resume = builder.resume;
        this.versionNumber = builder.versionNumber;
        this.content = builder.content;
        this.changeSummary = builder.changeSummary;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public String getContent() {
        return content;
    }

    public String getChangeSummary() {
        return changeSummary;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Resume resume;
        private Integer versionNumber;
        private String content;
        private String changeSummary;

        public Builder resume(Resume resume) {
            this.resume = resume;
            return this;
        }

        public Builder versionNumber(Integer versionNumber) {
            this.versionNumber = versionNumber;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder changeSummary(String changeSummary) {
            this.changeSummary = changeSummary;
            return this;
        }

        public ResumeVersion build() {
            return new ResumeVersion(this);
        }
    }
}
