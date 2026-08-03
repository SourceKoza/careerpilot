package com.sourcekoza.careerpilot.browser.controller;

import com.sourcekoza.careerpilot.browser.dto.BrowserNavigationRequest;
import com.sourcekoza.careerpilot.browser.dto.BrowserNavigationResponse;
import com.sourcekoza.careerpilot.browser.service.BrowserAutomationService;
import com.sourcekoza.careerpilot.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for manual browser automation testing.
 *
 * <p>This endpoint exists only to validate browser automation functionality.
 * AI Agents will call {@link BrowserAutomationService} directly per ADR-006,
 * not this controller.</p>
 *
 * @since Sprint-13
 */
@RestController
@RequestMapping("/api/v1/browser")
@Tag(name = "Browser Automation", description = "Browser automation validation endpoint")
public class BrowserController {

    private final BrowserAutomationService browserAutomationService;

    public BrowserController(BrowserAutomationService browserAutomationService) {
        this.browserAutomationService = browserAutomationService;
    }

    @PostMapping("/navigate")
    @Operation(
            summary = "Navigate to a URL",
            description = "Launches a browser, navigates to the specified URL, and returns page metadata. " +
                    "This endpoint is for manual testing only. AI Agents use BrowserAutomationService directly."
    )
    public ResponseEntity<ApiResponse<BrowserNavigationResponse>> navigate(
            @Valid @RequestBody BrowserNavigationRequest request) {
        BrowserNavigationResponse response = browserAutomationService.navigate(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Navigation completed successfully"));
    }
}
