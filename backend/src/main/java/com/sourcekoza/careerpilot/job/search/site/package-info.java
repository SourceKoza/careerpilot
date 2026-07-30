/**
 * Job Site Strategy implementations.
 *
 * <p>Each {@link com.sourcekoza.careerpilot.job.search.site.JobSite} implementation
 * encapsulates all website-specific knowledge for a particular job portal.
 * This keeps portal details out of both the AI agents and the browser automation service.</p>
 *
 * <p>Architecture:</p>
 * <pre>
 * JobSearchAgent → JobSite → BrowserAutomationService → BrowserSession → Playwright
 * </pre>
 *
 * @since Sprint-13
 */
package com.sourcekoza.careerpilot.job.search.site;
