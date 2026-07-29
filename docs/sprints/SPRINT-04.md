# SPRINT-04

# Shared Foundation

| Field    | Value              |
| -------- | ------------------ |
| Sprint   | 04                 |
| Epic     | EPIC-01 Foundation |
| Priority | High               |
| Status   | Planned            |

---

# Goal

Build the shared foundation that all future modules will use.

This sprint creates reusable infrastructure instead of business functionality.

The objective is to avoid duplication and establish common patterns before implementing Resume, Job, AI Agent, and MCP modules.

---

# Business Value

Provide a consistent engineering foundation for every future feature.

Reduce duplicated code and improve maintainability.

---

# Functional Requirements

Implement the following shared components:

* BaseEntity
* Auditing (CreatedAt, UpdatedAt)
* Common API Response model
* Pagination request/response models
* Error response model
* Common constants
* Utility classes (only where justified)

---

# Technical Requirements

Use:

* Spring Data JPA Auditing
* Java Time API
* Bean Validation
* Clean Architecture
* SOLID Principles

---

# Deliverables

## Common Module

Create a `common` package containing:

* BaseEntity
* ApiResponse
* ErrorResponse
* PageResponse
* Constants
* Utility classes (only if required)

---

## Configuration

Enable:

* JPA Auditing
* Common exception model
* Standard API response structure

---

## Entity Foundation

All future entities should inherit from `BaseEntity`.

Example audit fields:

* createdAt
* updatedAt

---

# Acceptance Criteria

* JPA auditing works.
* API responses are consistent.
* Error responses follow one format.
* Future entities can extend `BaseEntity`.
* Project builds successfully.

---

# Out of Scope

Do NOT implement:

* Resume module
* Job module
* AI Agents
* MCP
* Kafka
* Redis features
* Business logic

---

# Definition of Done

* Shared foundation is reusable.
* No duplicated infrastructure.
* Build passes.
* Ready for future business modules.

---

# Kiro Instructions

Implement only Sprint-04.

Do not introduce business functionality.

Do not implement placeholder modules.

Focus on reusable infrastructure that will be used by every future feature.
