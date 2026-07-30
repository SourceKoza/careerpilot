# SPRINT-14

# Job Search Agent

| Field    | Value       |
| -------- | ----------- |
| Sprint   | 14          |
| Epic     | AI Platform |
| Priority | Critical    |
| Status   | Planned     |

---

# Goal

Build the first production AI business agent.

The JobSearchAgent receives search criteria, delegates the search to JobSiteManager, normalizes results, persists jobs through JobService, and returns a structured response.

This sprint delivers the first real end-to-end AI business capability.

---

# Architecture

JobSearchAgent

↓

JobSiteManager

↓

JobSite Strategy

↓

BrowserAutomationService

↓

BrowserSession

↓

Playwright

↓

Job Portal

↓

JobService

↓

Database

---

# Responsibilities

## JobSearchAgent

Responsibilities:

* Validate request
* Invoke JobSiteManager
* Persist jobs via JobService
* Return AgentResponse

The agent must not know individual job sites.

---

## JobSiteManager

Responsibilities:

* Discover enabled JobSite implementations
* Execute searches
* Aggregate results
* Deduplicate results
* Return normalized jobs

The manager owns the JobSite lifecycle.

---

## JobSite

Each implementation is responsible for one website only.

Example:

* LinkedInJobSite

Future:

* IndeedJobSite
* NaukriJobSite
* WellfoundJobSite
* RemoteOkJobSite

---

## BrowserAutomationService

Responsible only for browser automation.

No business logic.

---

# Search Request

Create:

JobSearchCriteria

Fields:

* keyword
* location
* remoteOnly
* employmentType
* page
* size

---

# Search Result

Create:

JobSearchResult

Fields:

* title
* company
* location
* salary
* remote
* source
* url
* postedDate
* description

All JobSite implementations must return this model.

---

# Persistence

Persist jobs using the existing JobService.

Never access repositories directly.

---

# REST Endpoint

POST

/api/v1/agents/job-search

Returns:

* execution summary
* total jobs
* normalized job list

---

# Logging

Log:

* search request
* job site execution
* jobs found
* persistence summary
* execution duration

---

# Out of Scope

Do NOT implement:

* Resume tailoring
* Auto Apply
* Email
* Scheduling
* LLM
* Memory

---

# Acceptance Criteria

* JobSearchAgent implemented.
* JobSiteManager implemented.
* LinkedInJobSite integrated.
* BrowserAutomationService reused.
* Jobs normalized.
* Jobs persisted.
* Structured response returned.
* Build passes.

---

# Definition of Done

The first production AI Agent is fully operational.

Ready for Sprint-15.
