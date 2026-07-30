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
 * Certification entry within a Resume.
 *
 * <p>Represents a professional certification or license with issuing
 * organization, dates, and optional credential verification details.</p>
 */
@Entity
@Table(name = "certifications")
public class Certification extends BaseEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "issuing_organization", nullable = false, length = 100)
    private String issuingOrganization;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "credential_id", length = 100)
    private String credentialId;

    @Column(name = "credential_url", length = 500)
    private String credentialUrl;

    public Certification() {
        // JPA requires a no-arg constructor
    }

    private Certification(Builder builder) {
        this.resume = builder.resume;
        this.name = builder.name;
        this.issuingOrganization = builder.issuingOrganization;
        this.issueDate = builder.issueDate;
        this.expiryDate = builder.expiryDate;
        this.credentialId = builder.credentialId;
        this.credentialUrl = builder.credentialUrl;
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

    public String getIssuingOrganization() {
        return issuingOrganization;
    }

    public void setIssuingOrganization(String issuingOrganization) {
        this.issuingOrganization = issuingOrganization;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public String getCredentialUrl() {
        return credentialUrl;
    }

    public void setCredentialUrl(String credentialUrl) {
        this.credentialUrl = credentialUrl;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Resume resume;
        private String name;
        private String issuingOrganization;
        private LocalDate issueDate;
        private LocalDate expiryDate;
        private String credentialId;
        private String credentialUrl;

        public Builder resume(Resume resume) {
            this.resume = resume;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder issuingOrganization(String issuingOrganization) {
            this.issuingOrganization = issuingOrganization;
            return this;
        }

        public Builder issueDate(LocalDate issueDate) {
            this.issueDate = issueDate;
            return this;
        }

        public Builder expiryDate(LocalDate expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Builder credentialId(String credentialId) {
            this.credentialId = credentialId;
            return this;
        }

        public Builder credentialUrl(String credentialUrl) {
            this.credentialUrl = credentialUrl;
            return this;
        }

        public Certification build() {
            return new Certification(this);
        }
    }
}
