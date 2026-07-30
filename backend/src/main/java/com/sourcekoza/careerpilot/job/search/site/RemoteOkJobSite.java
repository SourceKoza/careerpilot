package com.sourcekoza.careerpilot.job.search.site;

import com.sourcekoza.careerpilot.browser.model.BrowserSession;
import com.sourcekoza.careerpilot.job.search.model.JobSearchCriteria;
import com.sourcekoza.careerpilot.job.search.model.JobSearchResult;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RemoteOK job site strategy implementation.
 *
 * <p>Placeholder — search logic will be implemented in a future sprint.</p>
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
        return false;
    }

    @Override
    public List<JobSearchResult> search(BrowserSession session, JobSearchCriteria criteria) {
        throw new UnsupportedOperationException("RemoteOK search is not yet implemented");
    }
}
