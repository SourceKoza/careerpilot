package com.sourcekoza.careerpilot.resume.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sourcekoza.careerpilot.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

/**
 * Project entry within a Resume.
 *
 * <p>Represents a personal, academic, or professional project with
 * description, technologies used, and optional URL for reference.</p>
 */
@Entity
@Table(name = "projects")
public class Project extends BaseEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 1500)
    private String description;

    @Column(name = "technologies_used", length = 300)
    private String technologiesUsed;

    @Column(name = "project_url", length = 500)
    private String projectUrl;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public Project() {
        // JPA requires a no-arg constructor
    }

    private Project(Builder builder) {
        this.resume = builder.resume;
        this.name = builder.name;
        this.description = builder.description;
        this.technologiesUsed = builder.technologiesUsed;
        this.projectUrl = builder.projectUrl;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnologiesUsed() {
        return technologiesUsed;
    }

    public void setTechnologiesUsed(String technologiesUsed) {
        this.technologiesUsed = technologiesUsed;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Resume resume;
        private String name;
        private String description;
        private String technologiesUsed;
        private String projectUrl;
        private LocalDate startDate;
        private LocalDate endDate;

        public Builder resume(Resume resume) {
            this.resume = resume;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder technologiesUsed(String technologiesUsed) {
            this.technologiesUsed = technologiesUsed;
            return this;
        }

        public Builder projectUrl(String projectUrl) {
            this.projectUrl = projectUrl;
            return this;
        }

        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Project build() {
            return new Project(this);
        }
    }
}
