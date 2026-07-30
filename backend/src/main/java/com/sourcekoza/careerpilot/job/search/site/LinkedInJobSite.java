package com.sourcekoza.careerpilot.job.search.site;

import org.springframework.stereotype.Component;

/**
 * LinkedIn job site strategy implementation.
 *
 * <p>Encapsulates all LinkedIn-specific knowledge: URLs, selectors,
 * navigation patterns, and search workflows. Keeps LinkedIn details
 * out of both the AI agents and the browser automation layer.</p>
 *
 * <p>Note: Search logic will be implemented in Sprint-14 (Job Search Agent).
 * This class currently provides site metadata only.</p>
 *
 * @since Sprint-13
 */
@Component
public class LinkedInJobSite implements JobSite {

    private static final String NAME = "linkedin";
    private static final String BASE_URL = "https://www.linkedin.com";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getBaseUrl() {
        return BASE_URL;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
