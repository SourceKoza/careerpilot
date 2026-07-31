# SPRINT-07

# Job Management

| Field    | Value                 |
| -------- | --------------------- |
| Sprint   | 07                    |
| Epic     | EPIC-02 Core Business |
| Priority | High                  |
| Status   | Planned               |

---

# Goal

Implement the Job Management module.

This sprint introduces the Job domain that represents employment opportunities imported from external job platforms such as LinkedIn, Indeed, Greenhouse, Lever, Workday, and company career portals.

The Job module is responsible for storing, searching, and managing job information.

No AI functionality will be implemented in this sprint.

---

# Business Requirements

The system must be able to store jobs collected from multiple external sources.

A Job represents a snapshot of a posting at the time it was imported.

Jobs may originate from different platforms but should follow one consistent internal model.

The module should support searching and filtering in future sprints.

---

# Domain Model

## Job

Represents a single job posting.

Suggested fields:

* id
* title
* companyName
* location
* employmentType
* workplaceType (Remote / Hybrid / Onsite)
* experienceLevel
* salaryMin
* salaryMax
* currency
* description
* requirements
* applicationUrl
* sourcePlatform
* externalJobId
* active

Inherit from BaseEntity.

---

# Relationships

Current Sprint

Job is an independent aggregate.

No relationships with JobApplication yet.

Those will be introduced in Sprint-08.

---

# REST APIs

## Job

POST

/api/v1/jobs

Create Job

---

GET

/api/v1/jobs

List Jobs

Support pagination.

---

GET

/api/v1/jobs/{id}

Retrieve Job

---

PUT

/api/v1/jobs/{id}

Update Job

---

DELETE

/api/v1/jobs/{id}

Delete Job

---

# Search Support

The repository should support future filtering.

At minimum implement:

* pagination
* sorting

Do not implement advanced searching yet.

---

# DTOs

JobCreateRequest

JobUpdateRequest

JobResponse

JobSummaryResponse

---

# Repository

JobRepository

---

# Service

JobService

---

# Controller

JobController

---

# Validation

Title is required.

Company name is required.

Application URL must be valid.

Salary values cannot be negative.

Maximum salary must be greater than or equal to minimum salary.

Employment type must use an enum.

Workplace type must use an enum.

Experience level must use an enum.

---

# Suggested Enums

EmploymentType

* FULL_TIME
* PART_TIME
* CONTRACT
* INTERN
* FREELANCE

---

WorkplaceType

* REMOTE
* HYBRID
* ONSITE

---

ExperienceLevel

* INTERN
* JUNIOR
* MID
* SENIOR
* STAFF
* PRINCIPAL

---

SourcePlatform

* LINKEDIN
* INDEED
* GREENHOUSE
* LEVER
* WORKDAY
* COMPANY_WEBSITE
* OTHER

---

# API Standards

Use:

* ApiResponse<T>
* PageResponse<T>
* ErrorResponse

Follow the project's exception handling strategy.

---

# Database

Use Spring Data JPA.

Use lazy loading where relationships exist.

Do not write native SQL.

Do not optimize prematurely.

---

# Production Considerations

A Job represents a snapshot of an external posting.

Do not assume external jobs remain unchanged.

Future synchronization with external platforms will be handled by dedicated import services.

This sprint focuses only on the internal Job domain.

---

# Out of Scope

Do NOT implement:

* AI job matching
* Job scraping
* LinkedIn integration
* Greenhouse API integration
* Workday API integration
* MCP
* Kafka
* Redis
* Email
* Notifications
* Job recommendations
* Resume matching

---

# Acceptance Criteria

* CRUD operations implemented.
* Pagination supported.
* Validation implemented.
* Swagger documentation available.
* Feature-based package structure followed.
* Uses BaseEntity.
* Uses optimistic locking.
* Uses ApiResponse<T>.
* Project builds successfully.

---

# Definition of Done

* Clean architecture.
* No duplicated business logic.
* Consistent with previous sprints.
* `mvn clean verify` passes.
* Ready for Sprint-08.

---

# Kiro Instructions

Implement only Sprint-07.

Reuse existing project patterns.

Do not introduce speculative abstractions.

Do not implement future integrations.

Follow KIRO_RULES, ENGINEERING_PRINCIPLES, and all established coding standards.

If implementation decisions are ambiguous, prefer the simplest production-ready solution that keeps the design extensible for future AI agents, MCP integration, and external job import services.
