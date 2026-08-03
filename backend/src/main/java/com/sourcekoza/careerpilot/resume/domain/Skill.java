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
 * Skill entry within a Resume.
 *
 * <p>Represents a single technical or professional skill with a named
 * proficiency level and optional category for grouping.</p>
 */
@Entity
@Table(name = "skills")
public class Skill extends BaseEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SkillProficiency proficiency;

    @Column(length = 50)
    private String category;

    public Skill() {
        // JPA requires a no-arg constructor
    }

    private Skill(Builder builder) {
        this.resume = builder.resume;
        this.name = builder.name;
        this.proficiency = builder.proficiency;
        this.category = builder.category;
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

    public SkillProficiency getProficiency() {
        return proficiency;
    }

    public void setProficiency(SkillProficiency proficiency) {
        this.proficiency = proficiency;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Resume resume;
        private String name;
        private SkillProficiency proficiency;
        private String category;

        public Builder resume(Resume resume) {
            this.resume = resume;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder proficiency(SkillProficiency proficiency) {
            this.proficiency = proficiency;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Skill build() {
            return new Skill(this);
        }
    }
}
