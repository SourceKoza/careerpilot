# CareerPilot AI

# 00 - START HERE

**Version:** 1.0

**Company:** SourceKoza Labs

**Product:** CareerPilot AI

---

# Welcome

Welcome to the CareerPilot AI engineering repository.

This repository is not a tutorial project or an AI-generated demo. It is a production-style engineering project built to demonstrate how modern backend systems are designed, implemented, tested, reviewed, and evolved.

Every architectural decision should have a clear reason.

Every implementation should follow the approved architecture.

Understanding **why** is more important than writing code.

---

# Project Mission

CareerPilot AI is an AI-powered Multi-Agent Job Search & Application Automation Platform.

The mission of Version 1 is to automate the repetitive tasks involved in searching, analysing, tailoring resumes for, and applying to software engineering jobs.

This project also serves as a production-grade portfolio demonstrating:

* Senior Java Backend Engineering
* Spring Boot
* AI Engineering
* Multi-Agent Systems
* Model Context Protocol (MCP)
* Clean Architecture
* Docker
* Observability
* Production Engineering

---

# Engineering Philosophy

We build software the same way professional engineering teams do.

Our priorities are:

1. Understand before implementing.
2. Architecture before code.
3. Documentation before implementation.
4. Build incrementally.
5. Prefer simple solutions until complexity is justified.
6. Every design decision must have a documented reason.
7. Optimise for maintainability rather than cleverness.
8. Treat AI as an engineering assistant, not an architect.

---

# Team Responsibilities

## Principal / Staff Engineer (ChatGPT)

Responsibilities:

* Product vision
* Architecture
* System design
* Technology selection
* Engineering standards
* Sprint planning
* Production best practices
* Code reviews
* Design reviews
* Interview preparation
* Mentoring

ChatGPT owns:

**WHAT** we build and **WHY** we build it.

---

## Implementation Engineer (Kiro)

Responsibilities:

* Sprint implementation
* Project scaffolding
* Boilerplate generation
* Docker configuration
* Configuration files
* Unit tests
* Integration tests
* Documentation updates
* Build scripts

Kiro owns:

**HOW** to implement the approved Sprint.

Kiro must never redesign the architecture.

---

## Project Owner (Developer)

Responsibilities:

* Learning
* Running the application
* Testing
* Reviewing implementation
* Asking questions
* Approving architecture
* Final engineering decisions

---

# AI Usage Policy

AI is used to eliminate repetitive work.

AI is **not** responsible for engineering decisions.

Always use the appropriate tool.

| Task                  | Owner   |
| --------------------- | ------- |
| Architecture          | ChatGPT |
| Design Decisions      | ChatGPT |
| Production Discussion | ChatGPT |
| Learning              | ChatGPT |
| Project Setup         | Kiro    |
| Boilerplate           | Kiro    |
| CRUD Generation       | Kiro    |
| Configuration         | Kiro    |
| Testing Skeletons     | Kiro    |

---

# Sprint-Based Development

The project is built one Sprint at a time.

Every Sprint must be completed before the next Sprint begins.

Future Sprint work must never be implemented early.

Each Sprint includes:

* Goal
* Requirements
* Deliverables
* Acceptance Criteria

---

# Engineering Workflow

Every Sprint follows the same lifecycle.

```
Architecture Discussion
        ↓
Engineering Documentation
        ↓
Sprint Specification
        ↓
Implementation (Kiro)
        ↓
Developer Testing
        ↓
Engineering Review
        ↓
Refactoring (if required)
        ↓
Merge
        ↓
Next Sprint
```

Code is a validation of the architecture.

Architecture is never a consequence of the code.

---

# Implementation Rules

Before implementing any Sprint:

* Read this document.
* Read the current Sprint document.
* Read any referenced standards.
* Implement only the approved requirements.
* Do not assume future functionality.
* Stop and ask for clarification if requirements are incomplete.

---

# Architecture Rules

Business logic must never directly depend on:

* AI providers
* External APIs
* Third-party services
* Database implementations

Everything should be accessed through abstractions and interfaces.

Follow:

* SOLID
* Clean Architecture
* Dependency Inversion
* High Cohesion
* Low Coupling

---

# Coding Standards

Always prefer:

* Constructor Injection
* Small classes
* Small methods
* Clear naming
* Immutable DTOs where appropriate
* Validation at boundaries
* Structured logging
* Meaningful exception handling

Avoid:

* Field Injection
* God Classes
* Business logic in Controllers
* Duplicate code
* Hard-coded configuration
* Premature optimisation

---

# Documentation Standards

Documentation is part of the product.

Whenever architecture changes:

* Update documentation.
* Update ADRs if required.
* Keep Sprint documents accurate.
* Keep diagrams synchronised.

Documentation should always explain:

* Why
* Alternatives
* Trade-offs
* Production considerations

---

# Code Review Standards

Every implementation should be reviewed for:

* Readability
* Maintainability
* SOLID compliance
* Production readiness
* Testability
* Security
* Performance
* Consistency

---

# Definition of Done

A Sprint is complete only when:

* Requirements are implemented.
* Acceptance criteria are satisfied.
* Tests pass.
* Documentation is updated.
* Code review is complete.
* No critical issues remain.

---

# Kiro Operating Rules

Kiro must:

* Implement only the approved Sprint.
* Never redesign the architecture.
* Never implement future Sprints.
* Never add features that were not requested.
* Prefer readability over cleverness.
* Follow coding standards.
* Ask for clarification rather than making assumptions.

---

# Repository Philosophy

This repository is designed to resemble a real production engineering organisation.

It should demonstrate:

* Engineering thinking
* Architectural discipline
* Production practices
* Documentation quality
* Maintainability
* Scalability

The objective is not to create the largest codebase.

The objective is to create a codebase that another engineer can understand, extend, and maintain with confidence.

---

# Success Criteria

CareerPilot AI is successful when:

* Every architectural decision has a documented reason.
* Every Sprint is independently understandable.
* New engineers can onboard quickly.
* The codebase remains maintainable as it grows.
* The platform demonstrates senior-level backend engineering practices.
* The project serves as a reference implementation for AI-native backend systems.

---

# Final Principle

**Think before building.**

Software engineering is not measured by the amount of code written.

It is measured by the quality of the decisions behind that code.

At SourceKoza Labs, architecture guides implementation, and understanding always comes before automation.
