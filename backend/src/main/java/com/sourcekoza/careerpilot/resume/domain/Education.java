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
 * Education entry within a Resume.
 *
 * <p>Represents a single educational qualification including institution,
 * degree, field of study, dates, and optional grade information.</p>
 */
@Entity
@Table(name = "educations")
public class Education extends BaseEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(nullable = false, length = 150)
    private String institution;

    @Column(nullable = false, length = 100)
    private String degree;

    @Column(name = "field_of_study", length = 100)
    private String fieldOfStudy;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(length = 20)
    private String grade;

    @Column(length = 1000)
    private String description;

    public Education() {
        // JPA requires a no-arg constructor
    }

    private Education(Builder builder) {
        this.resume = builder.resume;
        this.institution = builder.institution;
        this.degree = builder.degree;
        this.fieldOfStudy = builder.fieldOfStudy;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.grade = builder.grade;
        this.description = builder.description;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Resume resume;
        private String institution;
        private String degree;
        private String fieldOfStudy;
        private LocalDate startDate;
        private LocalDate endDate;
        private String grade;
        private String description;

        public Builder resume(Resume resume) {
            this.resume = resume;
            return this;
        }

        public Builder institution(String institution) {
            this.institution = institution;
            return this;
        }

        public Builder degree(String degree) {
            this.degree = degree;
            return this;
        }

        public Builder fieldOfStudy(String fieldOfStudy) {
            this.fieldOfStudy = fieldOfStudy;
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

        public Builder grade(String grade) {
            this.grade = grade;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Education build() {
            return new Education(this);
        }
    }
}
