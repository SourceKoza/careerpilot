package com.sourcekoza.careerpilot.job.search.site;

import org.springframework.stereotype.Component;

/**
 * Indeed job site strategy implementation.
 *
 * <p>TODO: Implement Indeed-specific search logic in a future sprint.</p>
 *
 * @since Sprint-13
 */
@Component
public class IndeedJobSite implements JobSite {

    private static final String NAME = "indeed";
    private static final String BASE_URL = "https://www.indeed.com";

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
        // TODO: Enable when Indeed search logic is implemented
        return false;
    }
}
