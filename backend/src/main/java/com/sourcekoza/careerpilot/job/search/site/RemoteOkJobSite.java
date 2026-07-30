package com.sourcekoza.careerpilot.job.search.site;

import org.springframework.stereotype.Component;

/**
 * RemoteOK job site strategy implementation.
 *
 * <p>TODO: Implement RemoteOK-specific search logic in a future sprint.</p>
 *
 * @since Sprint-13
 */
@Component
public class RemoteOkJobSite implements JobSite {

    private static final String NAME = "remoteok";
    private static final String BASE_URL = "https://remoteok.com";

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
        // TODO: Enable when RemoteOK search logic is implemented
        return false;
    }
}
