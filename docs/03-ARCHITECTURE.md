# 03 - Architecture

**Version:** 1.1

**Company:** SourceKoza Labs

**Product:** CareerPilot AI

---

## Overview

CareerPilot AI follows Clean Architecture principles with feature-based packaging. The system is organized into distinct layers with clear dependency rules.

---

## Layer Architecture

```text
┌─────────────────────────────────────────────────┐
│              External Consumers                  │
│   (Frontend, External AI, HTTP Clients)         │
└───────────────┬─────────────────────────────────┘
                │
┌───────────────▼─────────────────────────────────┐
│           Interface Layer                        │
│   REST Controllers │ MCP Server                 │
└───────────────┬─────────────────────────────────┘
                │
┌───────────────▼─────────────────────────────────┐
│         Application Services                     │
│   (Business Logic, Orchestration)               │
└───────────────┬──────────────▲──────────────────┘
                │              │
                │     ┌────────┴──────────────────┐
                │     │   Internal AI Agents       │
                │     │   (per ADR-006)            │
                │     └───────────────────────────┘
┌───────────────▼─────────────────────────────────┐
│        Infrastructure Layer                      │
│   Repositories │ Browser Automation │ External  │
└─────────────────────────────────────────────────┘
```

---

## Communication Rules (ADR-006)

| Consumer            | Integration Point    |
| ------------------- | -------------------- |
| Frontend / HTTP     | REST Controllers     |
| External AI / LLM   | MCP Tools            |
| Internal AI Agents  | Application Services |

Internal AI Agents MUST NOT call Controllers, REST APIs, MCP Tools, or Repositories directly.

---

## Browser Automation Architecture

### BrowserAutomationService

The browser automation layer provides a technology-agnostic interface for all browser interactions. The underlying engine (Playwright) is completely hidden.

**Responsibilities:**
- Browser lifecycle management (launch, close)
- Creating browser sessions
- Simple one-shot navigation (convenience method)

**Must NOT contain:**
- Knowledge of specific websites (LinkedIn, Indeed, etc.)
- CSS selectors for job portals
- Business logic of any kind

### BrowserSession

A `BrowserSession` represents one active browser instance with interaction capabilities.

**Lifecycle:**
```text
BrowserAutomationService.createSession()
        ↓
BrowserSession (active)
        ↓
navigate(url)
click(selector)
type(selector, text)
waitFor(selector)
getPageTitle()
getCurrentUrl()
        ↓
close()
```

**Design:**
- Implements `AutoCloseable` for try-with-resources usage
- Technology-agnostic interface — consumers never see Playwright
- One session = one browser instance
- Must be closed after use to release resources

**Usage Pattern:**
```java
try (BrowserSession session = browserAutomationService.createSession()) {
    session.navigate("https://www.linkedin.com/jobs");
    session.type("#keywords", "java developer");
    session.click(".search-button");
    session.waitFor(".job-results");
    String title = session.getPageTitle();
}
```

---

## Job Site Strategy

### Purpose

Each job portal (LinkedIn, Indeed, Naukri, etc.) has unique:
- URL structures
- CSS selectors
- Navigation patterns
- Authentication requirements
- Search workflows

The Job Site Strategy pattern isolates all website-specific knowledge into dedicated implementations.

### Architecture

```text
JobSearchAgent
      ↓
JobSite (strategy)
      ↓
BrowserAutomationService
      ↓
BrowserSession
      ↓
Playwright (hidden)
```

### Interface

`JobSite` provides a common interface for all job portals:
- `getName()` — site identifier
- `getBaseUrl()` — portal base URL
- `isEnabled()` — whether the implementation is operational

### Implementations

| Implementation      | Status      | Portal         |
| ------------------- | ----------- | -------------- |
| LinkedInJobSite     | Enabled     | LinkedIn       |
| IndeedJobSite       | Placeholder | Indeed         |
| NaukriJobSite       | Placeholder | Naukri         |
| WellfoundJobSite    | Placeholder | Wellfound      |
| RemoteOkJobSite     | Placeholder | RemoteOK       |

### Benefits

1. **Open/Closed Principle**: Add new portals without modifying existing code.
2. **Single Responsibility**: Each site owns its selectors and navigation logic.
3. **Testability**: Sites can be tested independently.
4. **Replaceability**: BrowserAutomationService can be swapped without touching any site.

---

## Dependency Direction

```text
Controller → Service → Repository
                ↑
          AI Agent
                ↓
          JobSite → BrowserAutomationService → BrowserSession
```

All dependencies point inward. Infrastructure details are always behind interfaces.
