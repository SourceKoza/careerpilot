# ENGINEERING_PRINCIPLES.md

# SourceKoza Labs

**Version:** 1.0

**Purpose:** Engineering principles and decision-making guidelines.

---

# Mission

We build software that is:

* Simple
* Maintainable
* Scalable
* Secure
* Observable
* Production-ready

We optimise for long-term engineering quality instead of short-term speed.

---

# Engineering Philosophy

Our objective is not to write more code.

Our objective is to make better engineering decisions.

Every decision should answer:

* Why?
* What alternatives were considered?
* What are the trade-offs?
* How will this behave in production?
* Can another engineer understand this six months later?

---

# Core Principles

## 1. Understand Before Implementing

Never start coding until the problem is understood.

If the problem is unclear, ask questions before implementation.

---

## 2. Architecture Before Code

Architecture defines the system.

Code implements the architecture.

Code must never define the architecture.

---

## 3. Documentation Before Implementation

Every major feature should be documented before implementation.

Documentation should explain:

* Why it exists
* Design decisions
* Constraints
* Trade-offs

---

## 4. Build Incrementally

Large systems are built one small step at a time.

Every sprint should produce a working, testable increment.

Avoid implementing future features early.

---

## 5. Keep It Simple

Prefer the simplest solution that satisfies today's requirements.

Avoid unnecessary abstraction.

Avoid unnecessary frameworks.

Avoid premature optimisation.

---

## 6. Clean Architecture

Business rules should not depend on frameworks.

Frameworks should support the business.

The domain should remain independent of:

* Spring Boot
* Database
* External APIs
* AI Providers
* Infrastructure

---

## 7. SOLID Principles

Every module should follow SOLID principles.

* Single Responsibility
* Open/Closed
* Liskov Substitution
* Interface Segregation
* Dependency Inversion

These principles improve maintainability and testability.

---

## 8. Feature-Based Modules

Organise code by business capability rather than technical layer.

Example:

```text
auth/
user/
job/
resume/
application/
agent/
mcp/
notification/
```

Avoid large shared packages such as:

```text
controller/
service/
repository/
```

containing unrelated features.

---

## 9. High Cohesion

A class should have one clear responsibility.

Related behaviour belongs together.

---

## 10. Low Coupling

Modules should depend on abstractions.

Avoid direct dependencies between unrelated modules.

---

## 11. Dependency Injection

Prefer constructor injection.

Never use field injection.

Dependencies should be explicit.

---

## 12. Security by Default

Security is never an afterthought.

Protect data from the beginning.

Never:

* Store plain-text passwords
* Hardcode secrets
* Trust client input

---

## 13. Observability by Default

Every production system must be observable.

Include:

* Structured logging
* Health checks
* Metrics
* Tracing (future sprint)

If a system cannot be observed, it cannot be maintained.

---

## 14. Production-First Thinking

Every implementation should consider:

* Scalability
* Performance
* Security
* Failure handling
* Monitoring
* Recovery

Ask:

"What happens when this runs for one year in production?"

---

## 15. AI is an Engineering Assistant

AI accelerates development.

AI does not replace engineering judgement.

Responsibilities:

### ChatGPT

* Architecture
* Design
* Reviews
* Mentoring
* Production discussions

### Kiro

* Code generation
* Boilerplate
* Configuration
* Testing support

### Developer

* Understanding
* Decision making
* Validation
* Final approval

---

## 16. Technology Selection

Technology is selected based on engineering needs.

Not popularity.

Not trends.

Every technology must solve a real problem.

---

## 17. Testing Philosophy

Test behaviour.

Not implementation details.

Prefer:

* Unit tests
* Integration tests
* End-to-end tests

Testing increases confidence during change.

---

## 18. Code Review Philosophy

A review should answer:

* Is it correct?
* Is it understandable?
* Is it maintainable?
* Is it secure?
* Is it testable?
* Is it production-ready?

Code review improves the product, not the ego.

---

## 19. Refactoring

Refactor when it improves:

* Readability
* Maintainability
* Simplicity
* Testability

Do not refactor without a clear benefit.

---

## 20. Documentation Philosophy

Documentation is part of the product.

Code explains "how."

Documentation explains "why."

Keep documentation updated with architectural changes.

---

# Decision Checklist

Before implementing any feature, answer:

* What problem are we solving?
* Why is this solution appropriate?
* What alternatives exist?
* What trade-offs are accepted?
* What production risks exist?
* How will this scale?
* How will it be tested?
* How will it be monitored?

---

# SourceKoza Engineering Principles

1. Think before building.
2. Architecture before implementation.
3. Simplicity over cleverness.
4. Production before optimisation.
5. Automation over repetition.
6. Documentation over assumptions.
7. Measure before optimising.
8. Security by default.
9. Observability by default.
10. Continuous learning.

---

# Engineering Culture

At SourceKoza Labs:

* We ask "Why?" before "How?"
* We challenge ideas, not people.
* We value clarity over complexity.
* We optimise for maintainability.
* We build software that another engineer can confidently extend.

---

# Final Principle

> **Software engineering is not measured by the amount of code written.**

> **It is measured by the quality of the decisions behind that code.**

Every line of code should exist for a reason.

Every architectural decision should be explainable.

Every sprint should leave the system better than before.
