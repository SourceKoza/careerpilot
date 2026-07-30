package com.sourcekoza.careerpilot.job.search.site;

import com.sourcekoza.careerpilot.browser.model.BrowserSession;
import com.sourcekoza.careerpilot.job.search.model.JobSearchCriteria;
import com.sourcekoza.careerpilot.job.search.model.JobSearchResult;

import java.util.List;

/**
 * Strategy interface representing a job portal site.
 *
 * <p>Each implementation encapsulates all website-specific knowledge
 * (URLs, selectors, navigation patterns) for a particular job portal.
 * This keeps website details out of both the AI agents and the
 * browser automation service.</p>
 *
 * <p>Target architecture:</p>
 * <pre>
 * JobSearchAgent
 *       ↓
 * JobSiteManager
 *       ↓
 * JobSite (e.g. LinkedInJobSite)
 *       ↓
 * BrowserSession
 *       ↓
 * Playwright (hidden)
 * </pre>
 *
 * <p>The JobSite receives a {@link BrowserSession} and uses it to interact
 * with the specific portal. BrowserAutomationService knows nothing about
 * LinkedIn, Indeed, or any other site.</p>
 *
 * @since Sprint-13
 */
public interface JobSite {

    /**
     * Returns the unique name identifying this job site.
     *
     * @return the site name (e.g. "linkedin", "indeed", "naukri")
     */
    String getName();

    /**
     * Returns the base URL for this job site.
     *
     * @return the base URL
     */
    String getBaseUrl();

    /**
     * Returns whether this job site implementation is fully operational.
     *
     * <p>Placeholder implementations should return false until
     * their search logic is complete.</p>
     *
     * @return true if this site can perform searches
     */
    boolean isEnabled();

    /**
     * Executes a job search on this site using the provided browser session.
     *
     * <p>The implementation is responsible for navigating the site, entering
     * search criteria, and extracting job listings from the results page.
     * All results must be normalized into {@link JobSearchResult} objects.</p>
     *
     * @param session  an active browser session to use for interaction
     * @param criteria the search criteria
     * @return a list of normalized job search results
     * @since Sprint-14
     */
    List<JobSearchResult> search(BrowserSession session, JobSearchCriteria criteria);
}
