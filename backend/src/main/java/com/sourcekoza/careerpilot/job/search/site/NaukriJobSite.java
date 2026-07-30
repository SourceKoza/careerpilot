package com.sourcekoza.careerpilot.job.search.site;

import org.springframework.stereotype.Component;

/**
 * Naukri job site strategy implementation.
 *
 * <p>TODO: Implement Naukri-specific search logic in a future sprint.</p>
 *
 * @since Sprint-13
 */
@Component
public class NaukriJobSite implements JobSite {

    private static final String NAME = "naukri";
    private static final String BASE_URL = "https://www.naukri.com";

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
        // TODO: Enable when Naukri search logic is implemented
        return false;
    }
}
