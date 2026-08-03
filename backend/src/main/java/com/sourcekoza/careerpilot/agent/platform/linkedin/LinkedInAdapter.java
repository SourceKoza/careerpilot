package com.sourcekoza.careerpilot.agent.platform.linkedin;

import com.sourcekoza.careerpilot.browser.model.BrowserElement;
import com.sourcekoza.careerpilot.browser.model.BrowserSession;
import com.sourcekoza.careerpilot.browser.service.BrowserAutomationService;
import com.sourcekoza.careerpilot.agent.platform.JobPlatformAdapter;
import com.sourcekoza.careerpilot.agent.platform.JobSearchRequest;
import com.sourcekoza.careerpilot.agent.platform.JobSearchResult;
import com.sourcekoza.careerpilot.mission.entity.PlatformType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * LinkedIn platform adapter using real browser automation.
 *
 * <p>Navigates LinkedIn's public job search pages, executes the search
 * based on mission keywords/location/remote preferences, and extracts
 * job data (title, company, location, description, URL) from the DOM.</p>
 *
 * <p>Uses the BrowserAutomationService → BrowserSession → BrowserElement
 * abstraction stack. No Playwright details leak into this class.</p>
 *
 * @since Sprint-15
 */
@Component
public class LinkedInAdapter implements JobPlatformAdapter {

    private static final Logger log = LoggerFactory.getLogger(LinkedInAdapter.class);

    private static final String JOBS_SEARCH_URL = "https://www.linkedin.com/jobs/search";

    // LinkedIn public job search page selectors
    private static final String SELECTOR_RESULTS_CONTAINER = ".jobs-search__results-list";
    private static final String SELECTOR_JOB_CARDS = ".jobs-search__results-list li";
    private static final String SELECTOR_JOB_TITLE = ".base-search-card__title";
    private static final String SELECTOR_JOB_COMPANY = ".base-search-card__subtitle";
    private static final String SELECTOR_JOB_LOCATION = ".job-search-card__location";
    private static final String SELECTOR_JOB_LINK = "a.base-card__full-link";
    private static final String SELECTOR_JOB_DATE = "time";
    private static final String SELECTOR_JOB_SALARY = ".job-search-card__salary-info";

    private final BrowserAutomationService browserAutomationService;

    public LinkedInAdapter(BrowserAutomationService browserAutomationService) {
        this.browserAutomationService = browserAutomationService;
    }

    @Override
    public PlatformType platform() {
        return PlatformType.LINKEDIN;
    }

    @Override
    public List<JobSearchResult> search(JobSearchRequest request) {
        log.info("LinkedIn adapter searching: keywords='{}', location='{}', remote={}",
                request.keywords(), request.location(), request.remoteOnly());

        String searchUrl = buildSearchUrl(request);
        log.debug("LinkedIn search URL: {}", searchUrl);

        List<JobSearchResult> results = new ArrayList<>();

        try (BrowserSession session = browserAutomationService.createSession()) {
            // Navigate to LinkedIn jobs search
            session.navigate(searchUrl);
            log.info("LinkedIn page loaded: {}", session.getPageTitle());

            // Wait for results to render
            try {
                session.waitFor(SELECTOR_RESULTS_CONTAINER);
            } catch (Exception e) {
                log.warn("LinkedIn results container not found — page may have no results or structure changed");
                return results;
            }

            // Extract all job cards from the page
            List<BrowserElement> jobCards = session.querySelectorAll(SELECTOR_JOB_CARDS);
            log.info("LinkedIn found {} job card elements", jobCards.size());

            int limit = Math.min(jobCards.size(), request.maxResults());
            for (int i = 0; i < limit; i++) {
                BrowserElement card = jobCards.get(i);
                JobSearchResult result = extractJobFromCard(card);
                if (result != null) {
                    results.add(result);
                }
            }

            log.info("LinkedIn adapter extracted {} valid jobs from {} cards", results.size(), jobCards.size());

        } catch (Exception e) {
            log.error("LinkedIn browser search failed: {}", e.getMessage(), e);
        }

        return results;
    }

    /**
     * Extracts job data from a single LinkedIn job card DOM element.
     */
    private JobSearchResult extractJobFromCard(BrowserElement card) {
        try {
            // Extract title
            BrowserElement titleEl = card.querySelector(SELECTOR_JOB_TITLE);
            String title = titleEl != null ? titleEl.text().trim() : null;
            if (title == null || title.isBlank()) {
                return null;
            }

            // Extract company name
            BrowserElement companyEl = card.querySelector(SELECTOR_JOB_COMPANY);
            String company = companyEl != null ? companyEl.text().trim() : "Unknown";

            // Extract location
            BrowserElement locationEl = card.querySelector(SELECTOR_JOB_LOCATION);
            String location = locationEl != null ? locationEl.text().trim() : null;

            // Extract job URL
            BrowserElement linkEl = card.querySelector(SELECTOR_JOB_LINK);
            String jobUrl = linkEl != null ? linkEl.attribute("href") : null;

            // Extract salary if available
            BrowserElement salaryEl = card.querySelector(SELECTOR_JOB_SALARY);
            String salary = salaryEl != null ? salaryEl.text().trim() : null;

            // Extract posted date
            BrowserElement dateEl = card.querySelector(SELECTOR_JOB_DATE);
            String postedDate = dateEl != null ? dateEl.text().trim() : null;

            // Extract external job ID from URL
            String externalJobId = extractJobIdFromUrl(jobUrl);

            log.debug("Extracted job: title='{}', company='{}', location='{}'", title, company, location);

            return JobSearchResult.builder()
                    .platform(PlatformType.LINKEDIN)
                    .externalJobId(externalJobId)
                    .title(title)
                    .company(company)
                    .location(location)
                    .salary(salary)
                    .jobUrl(jobUrl)
                    .description(postedDate != null ? "Posted: " + postedDate : null)
                    .build();

        } catch (Exception e) {
            log.debug("Failed to extract job from card: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Builds the LinkedIn job search URL from the request parameters.
     */
    private String buildSearchUrl(JobSearchRequest request) {
        StringBuilder url = new StringBuilder(JOBS_SEARCH_URL);
        url.append("?keywords=").append(encodeParam(request.keywords()));

        if (request.location() != null && !request.location().isBlank()) {
            url.append("&location=").append(encodeParam(request.location()));
        }

        if (request.remoteOnly()) {
            url.append("&f_WT=2"); // LinkedIn remote filter
        }

        if (request.experienceLevel() != null && !request.experienceLevel().isBlank()) {
            String levelCode = mapExperienceLevel(request.experienceLevel());
            if (levelCode != null) {
                url.append("&f_E=").append(levelCode);
            }
        }

        if (request.employmentType() != null && !request.employmentType().isBlank()) {
            String typeCode = mapEmploymentType(request.employmentType());
            if (typeCode != null) {
                url.append("&f_JT=").append(typeCode);
            }
        }

        return url.toString();
    }

    private String mapExperienceLevel(String level) {
        return switch (level.toLowerCase()) {
            case "intern", "internship" -> "1";
            case "entry", "entry level", "junior" -> "2";
            case "associate" -> "3";
            case "mid", "mid-senior", "senior" -> "4";
            case "director" -> "5";
            case "executive" -> "6";
            default -> null;
        };
    }

    private String mapEmploymentType(String type) {
        return switch (type.toLowerCase()) {
            case "full-time", "full_time" -> "F";
            case "part-time", "part_time" -> "P";
            case "contract" -> "C";
            case "temporary" -> "T";
            case "internship" -> "I";
            default -> null;
        };
    }

    private String extractJobIdFromUrl(String url) {
        if (url == null) return null;
        // LinkedIn URLs typically contain /view/JOBID
        int viewIdx = url.indexOf("/view/");
        if (viewIdx >= 0) {
            String afterView = url.substring(viewIdx + 6);
            int endIdx = afterView.indexOf('/');
            if (endIdx < 0) endIdx = afterView.indexOf('?');
            if (endIdx < 0) endIdx = afterView.length();
            return "li-" + afterView.substring(0, endIdx);
        }
        return null;
    }

    private String encodeParam(String value) {
        return value.replace(" ", "%20").replace(",", "%2C");
    }
}
