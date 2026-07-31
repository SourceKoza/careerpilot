# SPRINT-05

# Resume Domain Foundation

| Field    | Value                        |
| -------- | ---------------------------- |
| Sprint   | 05                           |
| Epic     | EPIC-02 Core Business Domain |
| Priority | High                         |
| Status   | Planned                      |

---

# Goal

Design and implement the Resume Domain.

This sprint establishes the core business model for resumes that will later support:

* AI Resume Analysis
* Resume Tailoring
* Cover Letter Generation
* Job Matching
* Job Applications
* MCP Tools
* Multi-Agent Workflows

This sprint is **NOT** about AI.

It is about creating a production-grade Resume domain model.

---

# IMPORTANT

Before writing any code:

Design the complete domain.

Think like a Senior Backend Engineer.

Challenge the model.

Do not blindly generate entities.

If a design decision is questionable, explain the alternatives and recommend the best option.

---

# Phase 1 — Domain Design

Create:

docs/DOMAIN_MODEL.md

The document should include:

## Business Goal

Explain why the Resume domain exists.

---

## Aggregate Root

Identify the Aggregate Root.

Explain why.

---

## Entity Relationships

Design relationships for:

* User
* Resume
* ResumeVersion
* Experience
* Education
* Skill
* Certification
* Project
* Language

Include cardinality.

Example:

User (1)
↓

Resume (Many)

Resume (1)

↓

Experience (Many)

---

## Design Decisions

For every relationship explain:

Why?

Alternatives?

Trade-offs?

Production considerations?

---

## Future AI Considerations

Explain how this design supports:

* Resume parsing
* AI resume improvements
* Version history
* Resume comparison
* Job matching
* Resume export

without changing the domain model.

---

# Phase 2 — Implementation

After the domain design is complete and validated, implement the Resume module.

---

# Package Structure

Use feature-based architecture.

Example:

resume/

controller/

domain/

dto/

mapper/

repository/

service/

validation/

package-info.java

Do not introduce layer-based packages.

---

# Entity Design

Implement:

Resume

ResumeVersion

Experience

Education

Skill

Certification

Project

Language

All entities must extend BaseEntity.

Use UUID.

Use optimistic locking inherited from BaseEntity.

Use Instant for timestamps.

---

# Relationships

Use appropriate JPA mappings.

Avoid unnecessary EAGER fetching.

Default to LAZY unless there is a strong reason otherwise.

Explain every cascade strategy chosen.

---

# Validation

Use Bean Validation.

Validate:

* Required fields
* Length limits
* Date consistency
* Collection sizes where appropriate

Do not put business logic inside controllers.

---

# DTOs

Separate:

Request DTOs

Response DTOs

Do not expose JPA entities directly.

---

# Mapping

Prefer MapStruct if it reduces boilerplate.

If manual mapping is chosen, explain why.

---

# API

Implement REST endpoints for:

Create Resume

Get Resume

Update Resume

Delete Resume (logical decision must be explained)

List User Resumes

Get Resume Versions

Do not implement AI functionality.

---

# Error Handling

Reuse:

ApiResponse

ErrorResponse

GlobalExceptionHandler

Do not create duplicate response models.

---

# Testing

Implement:

* Unit tests for services
* Repository tests
* Controller integration tests where appropriate

Do not stop at "application starts".

---

# Acceptance Criteria

* Resume domain follows DDD principles where practical.
* Feature-based architecture maintained.
* No entity is exposed directly through REST.
* Build passes.
* Existing functionality is not broken.
* Swagger documentation updated.
* Domain model documented.

---

# Out of Scope

Do NOT implement:

* Resume parsing
* PDF generation
* DOCX export
* AI analysis
* AI tailoring
* Job matching
* MCP tools
* Multi-Agent workflows

These belong to future sprints.

---

# Deliverables

* docs/DOMAIN_MODEL.md
* Resume module
* REST API
* DTOs
* Validation
* Repository layer
* Tests
* Updated Swagger documentation

---

# Kiro Instructions

Before coding:

1. Read:

   * docs/00-START_HERE.md
   * docs/KIRO_RULES.md
   * docs/ENGINEERING_PRINCIPLES.md
   * docs/03-ARCHITECTURE.md

2. Design first.

3. Explain assumptions before implementation.

4. Challenge weak design decisions.

5. If a better production design exists, recommend it before writing code.

6. Keep implementation incremental.

7. At the end of the sprint, provide:

   * Implementation Summary
   * Design Decisions
   * Assumptions
   * Known Limitations
   * Technical Debt
   * Recommended Sprint-06
   * Session Handover

# Git Workflow (Mandatory)

Every implementation must follow SourceKoza Labs engineering workflow.

Do **not** consider the sprint complete until the Git workflow below is finished.

---

## Branch Strategy

Never implement directly on `main`.

Workflow:

main

↓

develop

↓

feature/sprint-XX-<feature-name>

Example:

feature/sprint-05-resume-domain

---

## Commit Rules

Create small, meaningful commits.

Do not create one massive commit.

Group related changes together.

Examples:

docs: add Resume domain model

feat: implement Resume aggregate root

feat: add Experience and Education entities

feat: expose Resume REST API

test: add Resume service tests

refactor: improve Resume validation

fix: resolve Resume update validation issue

Avoid commits like:

update

changes

fixed issue

work

final

done

---

## Commit Message Convention

Use Conventional Commits.

Allowed prefixes:

feat:

fix:

docs:

refactor:

test:

perf:

build:

ci:

chore:

Examples:

feat: implement resume aggregate root

docs: add domain model documentation

test: add repository integration tests

refactor: simplify resume mapper

---

## Pull Request Preparation

When implementation is complete, generate a Pull Request summary.

Include:

### Summary

What was implemented.

---

### Design Decisions

Explain important architectural choices.

---

### Testing

List:

* Unit Tests
* Integration Tests
* Manual Testing

---

### Breaking Changes

State whether any breaking changes exist.

---

### Future Improvements

List items intentionally postponed.

---

### Checklist

* Build passes
* Tests pass
* Swagger updated
* Documentation updated
* No duplicated code
* No TODOs without explanation

---

## Sprint Completion

Before ending the sprint, generate:

* Sprint Implementation Summary
* Git Commit Summary
* Recommended Commit Messages
* Pull Request Description
* Technical Debt
* Known Limitations
* Next Sprint Recommendation

Do not continue to the next sprint automatically.

Wait for a new sprint request.




Do not continue beyond Sprint-05.
