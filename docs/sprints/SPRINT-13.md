# SPRINT-13

# Browser Automation Foundation

| Field    | Value               |
| -------- | ------------------- |
| Sprint   | 13                  |
| Epic     | EPIC-03 AI Platform |
| Priority | Critical            |
| Status   | Planned             |

---

# Goal

Build the Browser Automation Foundation for CareerPilot AI.

This sprint introduces a reusable browser automation layer that future AI Agents will use for interacting with job portals.

No job-search business logic should be implemented.

The objective is to build a production-ready browser automation service.

---

# Architecture Decision (ADR-006)

This sprint introduces the following permanent architecture decision.

## Internal AI Communication

Internal AI Agents communicate directly with **Application Services**.

Internal AI Agents MUST NOT:

* Call MCP Tools
* Call REST APIs
* Call Controllers
* Access Repositories directly

Application Services are the internal API of the application.

MCP remains available only for external AI integrations.

REST remains available only for frontend and external HTTP clients.

---

# Architecture

```text
Frontend
        │
 REST Controllers
        │
        ▼
Application Services
        ▲
        │
 AI Agent Framework
        │
 AI Orchestrator
        │
 BrowserAutomationService
        │
 PlaywrightBrowserAutomationService
        │
 Chromium
        │
 Target Website
```

---

# Why This Sprint Exists

AI Agents should focus on business workflows.

Browser automation is infrastructure.

Separating browser automation behind an application service allows us to replace Playwright in the future without changing any AI agent.

Possible future implementations:

* Playwright
* Browser Use
* Stagehand
* Selenium Grid
* Remote Browser Service

---

# Business Requirements

Create a reusable Browser Automation module.

The module must:

* Launch a browser
* Open a page
* Navigate to a URL
* Wait for page load
* Return page metadata
* Close the browser cleanly

This validates the browser automation foundation.

---

# Package Structure

Suggested package:

browser/

Suggested classes:

* BrowserAutomationService
* PlaywrightBrowserAutomationService
* BrowserSession
* BrowserNavigationRequest
* BrowserNavigationResponse
* BrowserException

---

# Responsibilities

BrowserAutomationService is responsible for:

* Browser lifecycle
* Navigation
* Waiting for page load
* Basic metadata extraction
* Error handling

It must NOT contain business logic.

It must NOT know anything about LinkedIn, Indeed, Naukri, or job searching.

---

# Validation Endpoint

Create a simple REST endpoint for manual testing.

Example:

POST

/api/v1/browser/navigate

Request:

* url

Response:

* success
* finalUrl
* pageTitle
* executionTime

This endpoint exists only to validate browser automation.

AI Agents will call BrowserAutomationService directly, not this endpoint.

---

# Logging

Log:

* Browser startup
* Navigation start
* Navigation completion
* Execution duration
* Browser shutdown
* Errors

Never log sensitive information.

---

# Error Handling

Create BrowserException.

Convert Playwright exceptions into application exceptions.

Reuse the existing GlobalExceptionHandler.

---

# Security

Allow navigation only to HTTP/HTTPS URLs.

Reject unsupported protocols.

Validate all input.

---

# Documentation

Create:

docs/adr/ADR-006-INTERNAL_AGENT_COMMUNICATION.md

Document the architecture decision introduced in this sprint.

---

# Out of Scope

Do NOT implement:

* LinkedIn scraping
* Job extraction
* Login automation
* Resume upload
* Auto Apply
* Form filling
* Screenshots
* Multi-tab workflows
* LLM integration
* MCP integration for browser automation

---

# Acceptance Criteria

* BrowserAutomationService implemented.
* PlaywrightBrowserAutomationService implemented.
* Browser launches successfully.
* Browser navigates successfully.
* Page title returned.
* Browser closes correctly.
* REST validation endpoint works.
* ADR-006 created.
* Build passes.

---

# Definition of Done

* Browser automation abstraction completed.
* Playwright isolated behind the service interface.
* Internal architecture follows ADR-006.
* Ready for Job Search Agent.
* mvn clean verify passes.

---

# Next Sprint

Sprint-14

Job Search Agent

The JobSearchAgent will use BrowserAutomationService directly to search job portals and persist jobs through JobService.
