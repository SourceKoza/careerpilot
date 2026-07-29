package com.sourcekoza.careerpilot.resume.domain;

import com.sourcekoza.careerpilot.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Resume aggregate root entity.
 *
 * <p>Represents a user's resume with all associated sections (experience,
 * education, skills, certifications, projects, languages) and version history.
 * All child entities are managed through the Resume's lifecycle.</p>
 *
 * <p>Uses soft delete via {@code deletedAt} timestamp. A non-null value
 * indicates the resume has been logically deleted.</p>
 */
@Entity
@Table(name = "resumes", indexes = {
    @Index(name = "idx_resume_user_id", columnList = "user_id"),
    @Index(name = "idx_resume_deleted_at", columnList = "deleted_at")
})
public class Resume extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String summary;

    @Column(name = "target_role", length = 100)
    private String targetRole;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "display_order")
    private List<Experience> experiences = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "display_order")
    private List<Education> educations = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Skill> skills = new HashSet<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Certification> certifications = new HashSet<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "display_order")
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Language> languages = new HashSet<>();

    @OneToMany(mappedBy = "resume", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @OrderBy("createdAt DESC")
    private List<ResumeVersion> versions = new ArrayList<>();

    public Resume() {
        // JPA requires a no-arg constructor
    }

    private Resume(Builder builder) {
        this.userId = builder.userId;
        this.title = builder.title;
        this.summary = builder.summary;
        this.targetRole = builder.targetRole;
        this.deletedAt = builder.deletedAt;
        this.experiences = builder.experiences;
        this.educations = builder.educations;
        this.skills = builder.skills;
        this.certifications = builder.certifications;
        this.projects = builder.projects;
        this.languages = builder.languages;
        this.versions = builder.versions;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public Set<Certification> getCertifications() {
        return certifications;
    }

    public void setCertifications(Set<Certification> certifications) {
        this.certifications = certifications;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public Set<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    public List<ResumeVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<ResumeVersion> versions) {
        this.versions = versions;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID userId;
        private String title;
        private String summary;
        private String targetRole;
        private Instant deletedAt;
        private List<Experience> experiences = new ArrayList<>();
        private List<Education> educations = new ArrayList<>();
        private Set<Skill> skills = new HashSet<>();
        private Set<Certification> certifications = new HashSet<>();
        private List<Project> projects = new ArrayList<>();
        private Set<Language> languages = new HashSet<>();
        private List<ResumeVersion> versions = new ArrayList<>();

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder targetRole(String targetRole) {
            this.targetRole = targetRole;
            return this;
        }

        public Builder deletedAt(Instant deletedAt) {
            this.deletedAt = deletedAt;
            return this;
        }

        public Builder experiences(List<Experience> experiences) {
            this.experiences = experiences;
            return this;
        }

        public Builder educations(List<Education> educations) {
            this.educations = educations;
            return this;
        }

        public Builder skills(Set<Skill> skills) {
            this.skills = skills;
            return this;
        }

        public Builder certifications(Set<Certification> certifications) {
            this.certifications = certifications;
            return this;
        }

        public Builder projects(List<Project> projects) {
            this.projects = projects;
            return this;
        }

        public Builder languages(Set<Language> languages) {
            this.languages = languages;
            return this;
        }

        public Builder versions(List<ResumeVersion> versions) {
            this.versions = versions;
            return this;
        }

        public Resume build() {
            return new Resume(this);
        }
    }
}
