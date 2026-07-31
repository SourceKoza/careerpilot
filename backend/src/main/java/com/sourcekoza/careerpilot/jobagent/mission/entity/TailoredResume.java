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
 * A resume tailored for a specific discovered job using the LLM.
 *
 * <p>Stores the AI-generated tailored content (summary, skills, experience, education)
 * as structured JSON text. Linked to both a mission and a specific discovered job.</p>
 *
 * @since Sprint-16
 */
@Entity
@Table(name = "tailored_resumes", indexes = {
        @Index(name = "idx_tailored_resume_mission_id", columnList = "mission_id"),
        @Index(name = "idx_tailored_resume_job_id", columnList = "job_id"),
        @Index(name = "idx_tailored_resume_user_id", columnList = "user_id")
})
public class TailoredResume extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private DiscoveredJob job;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "skills_json", columnDefinition = "TEXT")
    private String skillsJson;

    @Column(name = "experience_json", columnDefinition = "TEXT")
    private String experienceJson;

    @Column(name = "education_json", columnDefinition = "TEXT")
    private String educationJson;

    @Column(name = "tailored_score")
    private Integer tailoredScore;

    @Column(name = "original_score")
    private Integer originalScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TailoredResumeStatus status = TailoredResumeStatus.DRAFT;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "file_path", length = 500)
    private String filePath;

    public TailoredResume() {
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public DiscoveredJob getJob() {
        return job;
    }

    public void setJob(DiscoveredJob job) {
        this.job = job;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSkillsJson() {
        return skillsJson;
    }

    public void setSkillsJson(String skillsJson) {
        this.skillsJson = skillsJson;
    }

    public String getExperienceJson() {
        return experienceJson;
    }

    public void setExperienceJson(String experienceJson) {
        this.experienceJson = experienceJson;
    }

    public String getEducationJson() {
        return educationJson;
    }

    public void setEducationJson(String educationJson) {
        this.educationJson = educationJson;
    }

    public Integer getTailoredScore() {
        return tailoredScore;
    }

    public void setTailoredScore(Integer tailoredScore) {
        this.tailoredScore = tailoredScore;
    }

    public Integer getOriginalScore() {
        return originalScore;
    }

    public void setOriginalScore(Integer originalScore) {
        this.originalScore = originalScore;
    }

    public TailoredResumeStatus getStatus() {
        return status;
    }

    public void setStatus(TailoredResumeStatus status) {
        this.status = status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
