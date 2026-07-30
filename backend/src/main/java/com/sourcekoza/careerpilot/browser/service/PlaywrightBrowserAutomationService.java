package com.sourcekoza.careerpilot.browser.service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;
import com.sourcekoza.careerpilot.browser.dto.BrowserNavigationRequest;
import com.sourcekoza.careerpilot.browser.dto.BrowserNavigationResponse;
import com.sourcekoza.careerpilot.browser.exception.BrowserException;
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
 * @since Sprint-13
 */
@Service
public class PlaywrightBrowserAutomationService implements BrowserAutomationService {

    private static final Logger log = LoggerFactory.getLogger(PlaywrightBrowserAutomationService.class);

    @Override
    public BrowserNavigationResponse navigate(BrowserNavigationRequest request) {
        String targetUrl = request.url();
        log.info("Browser navigation starting: url={}", targetUrl);

        long startTime = System.currentTimeMillis();

        try (Playwright playwright = Playwright.create()) {
            log.debug("Playwright instance created");

            try (Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(true))) {
                log.debug("Chromium browser launched (headless)");

                Page page = browser.newPage();
                page.navigate(targetUrl);
                page.waitForLoadState();

                String finalUrl = page.url();
                String pageTitle = page.title();

                long executionTime = System.currentTimeMillis() - startTime;

                log.info("Browser navigation completed: finalUrl={}, pageTitle={}, executionTime={}ms",
                        finalUrl, pageTitle, executionTime);

                return new BrowserNavigationResponse(true, finalUrl, pageTitle, executionTime);
            }
        } catch (PlaywrightException ex) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("Browser navigation failed: url={}, executionTime={}ms, error={}",
                    targetUrl, executionTime, ex.getMessage());
            throw new BrowserException("Browser navigation failed: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("Unexpected browser error: url={}, executionTime={}ms",
                    targetUrl, executionTime, ex);
            throw new BrowserException("Unexpected browser automation error: " + ex.getMessage(), ex);
        }
    }
}
