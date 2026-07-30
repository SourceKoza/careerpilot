package com.sourcekoza.careerpilot.job.search.site;

import com.sourcekoza.careerpilot.browser.model.BrowserSession;

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
 * JobSite (e.g. LinkedInJobSite)
 *       ↓
 * BrowserAutomationService
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
}
