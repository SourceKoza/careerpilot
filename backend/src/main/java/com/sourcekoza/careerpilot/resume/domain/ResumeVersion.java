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
 * <p>Each version stores the resume content as Markdown text at a specific
 * point in time. Versions are historical records and are never modified
 * or deleted through normal operations.</p>
 *
 * <p>Every future Job Application references a ResumeVersion rather than
 * the Resume itself, preserving the exact state used during application.</p>
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

    @Column(name = "markdown_content", columnDefinition = "TEXT", nullable = false)
    private String markdownContent;

    @Column(name = "pdf_path", length = 500)
    private String pdfPath;

    @Column(name = "change_summary", length = 500)
    private String changeSummary;

    public ResumeVersion() {
        // JPA requires a no-arg constructor
    }

    private ResumeVersion(Builder builder) {
        this.resume = builder.resume;
        this.versionNumber = builder.versionNumber;
        this.markdownContent = builder.markdownContent;
        this.pdfPath = builder.pdfPath;
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

    public String getMarkdownContent() {
        return markdownContent;
    }

    public String getPdfPath() {
        return pdfPath;
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
        private String markdownContent;
        private String pdfPath;
        private String changeSummary;

        public Builder resume(Resume resume) {
            this.resume = resume;
            return this;
        }

        public Builder versionNumber(Integer versionNumber) {
            this.versionNumber = versionNumber;
            return this;
        }

        public Builder markdownContent(String markdownContent) {
            this.markdownContent = markdownContent;
            return this;
        }

        public Builder pdfPath(String pdfPath) {
            this.pdfPath = pdfPath;
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
