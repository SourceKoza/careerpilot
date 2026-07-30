package com.sourcekoza.careerpilot.browser.service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;
import com.sourcekoza.careerpilot.browser.dto.BrowserNavigationRequest;
import com.sourcekoza.careerpilot.browser.dto.BrowserNavigationResponse;
import com.sourcekoza.careerpilot.browser.exception.BrowserException;
import com.sourcekoza.careerpilot.browser.model.BrowserSession;
import com.sourcekoza.careerpilot.browser.model.PlaywrightBrowserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Playwright-based implementation of {@link BrowserAutomationService}.
 *
 * <p>Manages browser lifecycle using Chromium. All Playwright details are
 * completely hidden behind the service interface. Future implementations
 * can replace this with Browser Use, Stagehand, Selenium Grid, or a
 * Remote Browser Service without affecting any consuming code.</p>
 *
 * <p>This service is responsible only for browser automation. It must NOT
 * contain knowledge about specific websites, CSS selectors for job portals,
 * or any business logic.</p>
 *
 * @since Sprint-13
 */
@Service
public class PlaywrightBrowserAutomationService implements BrowserAutomationService {

    private static final Logger log = LoggerFactory.getLogger(PlaywrightBrowserAutomationService.class);

    @Override
    public BrowserSession createSession() {
        log.info("Creating new browser session");
        try {
            Playwright playwright = Playwright.create();
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();

            PlaywrightBrowserSession session = new PlaywrightBrowserSession(playwright, browser, page);
            log.info("Browser session created: sessionId={}", session.getSessionId());
            return session;
        } catch (PlaywrightException ex) {
            log.error("Failed to create browser session: {}", ex.getMessage());
            throw new BrowserException("Failed to launch browser: " + ex.getMessage(), ex);
        }
    }

    @Override
    public BrowserNavigationResponse navigate(BrowserNavigationRequest request) {
        String targetUrl = request.url();
        log.info("Browser navigation starting: url={}", targetUrl);

        long startTime = System.currentTimeMillis();

        try (BrowserSession session = createSession()) {
            session.navigate(targetUrl);

            String finalUrl = session.getCurrentUrl();
            String pageTitle = session.getPageTitle();

            long executionTime = System.currentTimeMillis() - startTime;

            log.info("Browser navigation completed: finalUrl={}, pageTitle={}, executionTime={}ms",
                    finalUrl, pageTitle, executionTime);

            return new BrowserNavigationResponse(true, finalUrl, pageTitle, executionTime);
        } catch (BrowserException ex) {
            throw ex;
        } catch (Exception ex) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("Unexpected browser error: url={}, executionTime={}ms",
                    targetUrl, executionTime, ex);
            throw new BrowserException("Unexpected browser automation error: " + ex.getMessage(), ex);
        }
    }
}
