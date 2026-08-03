package com.sourcekoza.careerpilot.resume.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sourcekoza.careerpilot.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Language proficiency entry within a Resume.
 *
 * <p>Represents a spoken/written language with a proficiency level
 * using recruiter-friendly categories.</p>
 */
@Entity
@Table(name = "languages")
public class Language extends BaseEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LanguageProficiency proficiency;

    public Language() {
        // JPA requires a no-arg constructor
    }

    private Language(Builder builder) {
        this.resume = builder.resume;
        this.name = builder.name;
        this.proficiency = builder.proficiency;
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

    public LanguageProficiency getProficiency() {
        return proficiency;
    }

    public void setProficiency(LanguageProficiency proficiency) {
        this.proficiency = proficiency;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Resume resume;
        private String name;
        private LanguageProficiency proficiency;

        public Builder resume(Resume resume) {
            this.resume = resume;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder proficiency(LanguageProficiency proficiency) {
            this.proficiency = proficiency;
            return this;
        }

        public Language build() {
            return new Language(this);
        }
    }
}
