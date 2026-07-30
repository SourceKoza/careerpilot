package com.sourcekoza.careerpilot.job.search.site;

import org.springframework.stereotype.Component;

/**
 * Wellfound (formerly AngelList) job site strategy implementation.
 *
 * <p>TODO: Implement Wellfound-specific search logic in a future sprint.</p>
 *
 * @since Sprint-13
 */
@Component
public class WellfoundJobSite implements JobSite {

    private static final String NAME = "wellfound";
    private static final String BASE_URL = "https://www.wellfound.com";

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
        // TODO: Enable when Wellfound search logic is implemented
        return false;
    }
}
