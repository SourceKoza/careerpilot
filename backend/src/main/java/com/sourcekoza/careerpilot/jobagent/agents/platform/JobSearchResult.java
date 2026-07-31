package com.sourcekoza.careerpilot.jobagent.agents.platform;

import com.sourcekoza.careerpilot.jobagent.mission.entity.PlatformType;

/**
 * Normalized result from a platform adapter search.
 *
 * @since Sprint-15
 */
public record JobSearchResult(
        PlatformType platform,
        String externalJobId,
        String title,
        String company,
        String location,
        String salary,
        String description,
        String jobUrl,
        String recruiterName,
        String recruiterLinkedIn,
        String recruiterRole
) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private PlatformType platform;
        private String externalJobId;
        private String title;
        private String company;
        private String location;
        private String salary;
        private String description;
        private String jobUrl;
        private String recruiterName;
        private String recruiterLinkedIn;
        private String recruiterRole;

        private Builder() {
        }

        public Builder platform(PlatformType platform) { this.platform = platform; return this; }
        public Builder externalJobId(String v) { this.externalJobId = v; return this; }
        public Builder title(String v) { this.title = v; return this; }
        public Builder company(String v) { this.company = v; return this; }
        public Builder location(String v) { this.location = v; return this; }
        public Builder salary(String v) { this.salary = v; return this; }
        public Builder description(String v) { this.description = v; return this; }
        public Builder jobUrl(String v) { this.jobUrl = v; return this; }
        public Builder recruiterName(String v) { this.recruiterName = v; return this; }
        public Builder recruiterLinkedIn(String v) { this.recruiterLinkedIn = v; return this; }
        public Builder recruiterRole(String v) { this.recruiterRole = v; return this; }

        public JobSearchResult build() {
            return new JobSearchResult(platform, externalJobId, title, company,
                    location, salary, description, jobUrl,
                    recruiterName, recruiterLinkedIn, recruiterRole);
        }
    }
}
