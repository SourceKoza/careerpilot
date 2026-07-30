package com.sourcekoza.careerpilot.job.search.model;

/**
 * Normalized result from a job site search.
 *
 * <p>All JobSite implementations must convert their portal-specific
 * data into this common model. This ensures the JobSearchAgent and
 * JobSiteManager never see site-specific data structures.</p>
 *
 * @param title       job title
 * @param company     company name
 * @param location    job location
 * @param salary      salary information (free text, may be null)
 * @param remote      whether the position is remote
 * @param source      the job site name that produced this result (e.g. "linkedin")
 * @param url         direct URL to the job posting
 * @param postedDate  when the job was posted (free text, may be null)
 * @param description job description (may be null or truncated)
 * @since Sprint-14
 */
public record JobSearchResult(
        String title,
        String company,
        String location,
        String salary,
        boolean remote,
        String source,
        String url,
        String postedDate,
        String description
) {

    /**
     * Builder for constructing JobSearchResult instances.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String title;
        private String company;
        private String location;
        private String salary;
        private boolean remote;
        private String source;
        private String url;
        private String postedDate;
        private String description;

        private Builder() {
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder company(String company) {
            this.company = company;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder salary(String salary) {
            this.salary = salary;
            return this;
        }

        public Builder remote(boolean remote) {
            this.remote = remote;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder postedDate(String postedDate) {
            this.postedDate = postedDate;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public JobSearchResult build() {
            return new JobSearchResult(title, company, location, salary, remote, source, url, postedDate, description);
        }
    }
}
