# Session Handover — Sprint-05: Resume Domain Foundation

## Implementation Summary

Sprint-05 successfully implements the complete Resume Domain Foundation for CareerPilot AI. The module follows DDD principles with Resume as the Aggregate Root, feature-based package architecture, and clean separation of concerns via DTOs and MapStruct mapping.

### Delivered Components:
- 8 JPA entities (Resume + 7 child entities)
- 2 enums (SkillProficiency, LanguageProficiency)
- 2 repositories (ResumeRepository, ResumeVersionRepository)
- 1 service (ResumeService with 6 business methods)
- 1 REST controller (6 endpoints, JWT-authenticated)
- 18 DTO records (request + response)
- 1 MapStruct mapper interface
- 1 custom validator (@ValidDateRange + DateRangeValidator)
- 21 passing tests (unit + integration)

---

## Design Decisions

1. **Resume as Aggregate Root** — All child entities accessed only through Resume. No independent repositories for children.
2. **userId as UUID column** (not @ManyToOne) — Maintains module boundary between auth and resume modules. Portable for future microservice split.
3. **Soft Delete via `Instant deletedAt`** — Only on Resume (children follow parent lifecycle). Preserves temporal audit data.
4. **JSONB Version Snapshots** — ResumeVersion stores serialized JSON. Immutable history; schema-evolution-friendly.
5. **MapStruct** — Compile-time mapping eliminates runtime reflection. Generated code is debuggable.
6. **LAZY everywhere + @EntityGraph** — Prevents unnecessary joins on list queries; targeted eager load for detail views.
7. **@OrderColumn for ordered collections** — Experience, Education, Project preserve user arrangement.

---

## Assumptions

1. **H2 for tests, PostgreSQL for production** — JSONB column works in production; H2 tests skip version-related DDL (warning logged, not blocking).
2. **Authentication via Spring Security** — User email extracted from SecurityContext, looked up in UserRepository to get UUID.
3. **No Flyway yet** — Hibernate `ddl-auto` handles schema in development. Flyway migrations should be introduced before production deployment.
4. **Single-user ownership** — Resumes belong to exactly one user. No sharing/collaboration model.
5. **Version created on every update** — Even minor edits trigger a version snapshot. Could be optimized with dirty-checking in future.

---

## Known Limitations

1. **H2 JSONB incompatibility** — `resume_versions` table fails to create in H2 test environment. Repository tests for versions would need PostgreSQL testcontainers or MODE=PostgreSQL H2 setting.
2. **No pagination metadata in version history** — `getVersions` returns a flat list. For resumes with many versions, pagination should be added.
3. **No bulk operations** — No batch delete, batch export, or batch update endpoints.
4. **No file upload** — Resume import (PDF/DOCX parsing) is out of scope.
5. **Sequential version numbering** — Could have race conditions under concurrent updates. Acceptable for single-user access pattern.

---

## Technical Debt

| Item | Priority | Notes |
|------|----------|-------|
| Flyway migrations | High | Must be added before production deployment |
| Testcontainers for PostgreSQL | Medium | Enables testing JSONB, full-text search, and production-parity |
| ResumeVersion integration tests | Medium | Skipped due to H2 JSONB limitation |
| API pagination for versions endpoint | Low | Not critical until users have many versions |
| Audit log for delete operations | Low | Soft delete provides basic audit; full log is future work |
| Input sanitization (XSS) | Medium | Currently relies on Spring's default; should add explicit sanitization |

---

## Recommended Sprint-06

### Option A: AI Resume Parsing (High Value)
- Integrate AI (OpenAI/Anthropic) to parse uploaded PDF/DOCX into Resume entity
- Implement file upload endpoint
- Structured extraction into domain model

### Option B: Resume Export (User-Facing)
- PDF generation from Resume entity
- Multiple template support
- DOCX export option

### Option C: Infrastructure Hardening
- Flyway migrations
- Testcontainers setup
- Rate limiting
- Caching layer (Redis)
- CI/CD pipeline enhancement

### Recommendation: Sprint-06 = Flyway Migrations + Resume Export (PDF)
Flyway is a prerequisite for production. PDF export delivers immediate user value. AI features can follow in Sprint-07 once the foundation is hardened.

---

## Session Handover Notes

- **Branch**: `feature/sprint-05-resume-domain` (local only, not pushed to remote)
- **Build Status**: GREEN — `./mvnw clean verify` passes with 21 tests
- **Git Commits**: 12 atomic commits following Conventional Commits
- **Swagger**: All endpoints annotated; accessible at `/swagger-ui.html` when app runs
- **No changes to existing modules** — auth, common, config, security all untouched
- **The `.kiro/` directory is untracked** — contains spec files for this sprint, not committed to git

### Quick Verification:
```bash
cd backend
JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home ./mvnw clean verify
# Expected: BUILD SUCCESS, 21 tests, 0 failures
```
