package com.sourcekoza.careerpilot.job.search.manager;

import com.sourcekoza.careerpilot.browser.model.BrowserSession;
import com.sourcekoza.careerpilot.browser.service.BrowserAutomationService;
import com.sourcekoza.careerpilot.job.search.model.JobSearchCriteria;
import com.sourcekoza.careerpilot.job.search.model.JobSearchResult;
import com.sourcekoza.careerpilot.job.search.site.JobSite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Manages job site discovery, execution, and result aggregation.
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Discover enabled JobSite implementations via Spring injection</li>
 *   <li>Execute searches against all enabled sites</li>
 *   <li>Aggregate results from multiple sites</li>
 *   <li>Deduplicate results by URL</li>
 *   <li>Return normalized job search results</li>
 * </ul>
 *
 * <p>The JobSearchAgent delegates all site-specific logic to this manager.
 * This class owns the JobSite lifecycle and ensures the agent never needs
 * to know about individual job portals.</p>
 *
 * @since Sprint-14
 */
@Component
public class JobSiteManager {

    private static final Logger log = LoggerFactory.getLogger(JobSiteManager.class);

    private final List<JobSite> jobSites;
    private final BrowserAutomationService browserAutomationService;

    public JobSiteManager(List<JobSite> jobSites, BrowserAutomationService browserAutomationService) {
        this.jobSites = jobSites;
        this.browserAutomationService = browserAutomationService;

        List<String> enabledSites = jobSites.stream()
                .filter(JobSite::isEnabled)
                .map(JobSite::getName)
                .toList();
        log.info("JobSiteManager initialized: total={}, enabled={}, sites={}",
                jobSites.size(), enabledSites.size(), enabledSites);
    }

    /**
     * Executes a job search across all enabled job sites.
     *
     * <p>For each enabled site, a new browser session is created, the search
     * is executed, and results are collected. Results are deduplicated by URL
     * before being returned.</p>
     *
     * @param criteria the search criteria
     * @return aggregated and deduplicated results from all enabled sites
     */
    public List<JobSearchResult> search(JobSearchCriteria criteria) {
        List<JobSite> enabledSites = getEnabledSites();

        if (enabledSites.isEmpty()) {
            log.warn("No enabled job sites available for search");
            return List.of();
        }

        log.info("Job search starting: keyword='{}', location='{}', sites={}",
                criteria.keyword(), criteria.location(),
                enabledSites.stream().map(JobSite::getName).toList());

        List<JobSearchResult> aggregatedResults = new ArrayList<>();

        for (JobSite site : enabledSites) {
            List<JobSearchResult> siteResults = executeSearch(site, criteria);
            aggregatedResults.addAll(siteResults);
        }

        List<JobSearchResult> deduplicatedResults = deduplicate(aggregatedResults);

        log.info("Job search completed: total={}, deduplicated={}",
                aggregatedResults.size(), deduplicatedResults.size());

        return deduplicatedResults;
    }

    /**
     * Returns the names of all enabled job sites.
     */
    public List<String> getEnabledSiteNames() {
        return getEnabledSites().stream()
                .map(JobSite::getName)
                .toList();
    }

    private List<JobSite> getEnabledSites() {
        return jobSites.stream()
                .filter(JobSite::isEnabled)
                .toList();
    }

    private List<JobSearchResult> executeSearch(JobSite site, JobSearchCriteria criteria) {
        log.info("Searching site: name='{}', baseUrl='{}'", site.getName(), site.getBaseUrl());
        long startTime = System.currentTimeMillis();

        try (BrowserSession session = browserAutomationService.createSession()) {
            List<JobSearchResult> results = site.search(session, criteria);
            long duration = System.currentTimeMillis() - startTime;
            log.info("Site search completed: site='{}', results={}, duration={}ms",
                    site.getName(), results.size(), duration);
            return results;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Site search failed: site='{}', error='{}', duration={}ms",
                    site.getName(), e.getMessage(), duration);
            return List.of();
        }
    }

    private List<JobSearchResult> deduplicate(List<JobSearchResult> results) {
        Set<String> seenUrls = new LinkedHashSet<>();
        List<JobSearchResult> unique = new ArrayList<>();

        for (JobSearchResult result : results) {
            String key = result.url() != null ? result.url() : generateKey(result);
            if (seenUrls.add(key)) {
                unique.add(result);
            } else {
                log.debug("Duplicate job removed: url='{}'", key);
            }
        }

        return unique;
    }

    private String generateKey(JobSearchResult result) {
        return result.title() + "|" + result.company() + "|" + result.source();
    }
}
