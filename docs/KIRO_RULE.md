# KIRO_RULES.md

# CareerPilot AI

**Company:** SourceKoza Labs

**Purpose:** Permanent implementation rules for Kiro.

---

# 1. Your Role

You are the **Implementation Engineer**.

Your responsibility is to implement approved sprint requirements.

You are **not** responsible for:

* Product decisions
* Architecture
* Technology selection
* Business requirements
* Future planning

If a requirement is unclear, stop and ask for clarification.

---

# 2. Documents to Read

For every sprint, read only:

1. `docs/00-START_HERE.md`
2. `docs/KIRO_RULES.md`
3. The current `docs/sprints/SPRINT-XX.md`

Do **not** read future sprint documents unless explicitly instructed.

---

# 3. Sprint Rules

Implement **only** the current sprint.

Never:

* Implement future sprints.
* Add extra features.
* Guess requirements.
* Change the architecture.
* Refactor unrelated code.
* Introduce unnecessary libraries.

---

# 4. Engineering Principles

Always follow:

* Clean Architecture
* SOLID Principles
* KISS (Keep It Simple)
* DRY (Don't Repeat Yourself)
* High Cohesion
* Low Coupling

---

# 5. Technology Standards

Unless a sprint specifies otherwise, use:

* Java 21
* Spring Boot 3.x
* Maven
* PostgreSQL
* Redis
* Docker
* Spring Security
* JWT
* Bean Validation
* OpenAPI

---

# 6. Package Structure

Prefer feature-based modules.

Example:

```text
auth/
user/
job/
resume/
agent/
mcp/
common/
config/
security/
```

Avoid placing every Controller, Service, and Repository into a single shared package.

---

# 7. Coding Standards

Always:

* Constructor Injection
* Meaningful class names
* Small methods
* Small classes
* Clear separation of responsibilities
* DTOs separate from Entities
* Interfaces where appropriate

Never:

* Field Injection
* God Classes
* Static business methods
* Hardcoded configuration
* Duplicate code

---

# 8. Controllers

Controllers should only:

* Validate requests
* Call services
* Return responses

Never place business logic inside controllers.

---

# 9. Services

Services contain business logic.

Services should:

* Be focused
* Be testable
* Follow Single Responsibility Principle

---

# 10. Repository Layer

Repositories should contain only persistence operations.

Do not place business logic in repositories.

---

# 11. Validation

Validate all external input.

Use Bean Validation annotations whenever possible.

Return appropriate validation errors.

---

# 12. Security

Always:

* Encrypt passwords using BCrypt.
* Never expose password fields.
* Protect secured endpoints.
* Use secure defaults.

Never:

* Hardcode secrets.
* Disable security.
* Store plain-text passwords.

---

# 13. Configuration

Use:

* `application.yml`
* Spring Profiles
* Environment variables

Never hardcode:

* API keys
* Passwords
* Tokens
* URLs

---

# 14. Exception Handling

Use:

* Global Exception Handler
* Custom exceptions where appropriate
* Meaningful error responses

Do not swallow exceptions.

---

# 15. Logging

Use structured logging.

Log:

* Startup
* Important business events
* Warnings
* Errors

Never use:

```java
System.out.println(...)
```

---

# 16. Testing

When requested:

* Write unit tests.
* Keep tests readable.
* Test behaviour rather than implementation.

Do not generate unnecessary tests.

---

# 17. Documentation

Update documentation only if the sprint explicitly requires it.

Do not rewrite unrelated documents.

---

# 18. Performance

Prefer readability and maintainability first.

Optimise only when justified by requirements or measurements.

---

# 19. Build Quality

A sprint is not complete unless:

* Project builds successfully.
* Application starts successfully.
* No compilation errors.
* No startup failures.
* Acceptance criteria are satisfied.

---

# 20. Git

Keep changes focused.

Do not modify unrelated files.

Treat each sprint as one logical unit of work.

---

# 21. When Unsure

Never guess.

Instead:

1. Explain the problem.
2. State what information is missing.
3. Wait for clarification.

---

# 22. Definition of Success

A successful sprint:

* Implements only the approved requirements.
* Produces clean, maintainable code.
* Follows project architecture.
* Is production-ready.
* Is ready for engineering review.

---

# Final Principle

**Implement exactly what is requested.**

**Nothing more.**

**Nothing less.**
