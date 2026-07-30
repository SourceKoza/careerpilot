# Pull Request: Sprint-05 — Resume Domain Foundation

## Summary

This PR implements the complete Resume Domain Foundation for CareerPilot AI. It establishes the core business model following DDD principles with a production-grade REST API, proper validation, MapStruct mapping, and comprehensive test coverage.

### What was implemented:
- **Resume Aggregate Root** with 7 child entities (Experience, Education, Skill, Certification, Project, Language, ResumeVersion)
- **REST API** — full CRUD + versioning (6 endpoints) with OpenAPI/Swagger documentation
- **MapStruct Mapper** — compile-time entity ↔ DTO mapping
- **Custom Validation** — `@ValidDateRange` for date consistency
- **Soft Delete** — `deletedAt` timestamp on Resume (not physical deletion)
- **Version Snapshots** — automatic version creation before updates (JSONB storage)
- **Domain Documentation** — comprehensive `docs/DOMAIN_MODEL.md`

---

## Design Decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Aggregate Root | Resume | All child entities have no independent lifecycle |
| User reference | `userId` (UUID column) | Module boundary separation — no JPA coupling to auth module |
| Soft Delete | `Instant deletedAt` | Auditability + restore capability; only on aggregate root |
| Version Storage | JSONB column | Immutable snapshots; schema-evolution-friendly |
| Cascade Strategy | `ALL` + orphanRemoval on children; `PERSIST,MERGE` on versions | Children follow Resume lifecycle; versions are permanent audit trail |
| Fetching | LAZY everywhere | Prevent N+1; use `@EntityGraph` for targeted eager loading |
| DTO Mapping | MapStruct 1.5.5 | Compile-time safety, zero reflection, boilerplate reduction |
| Date Fields | `LocalDate` for user dates, `Instant` for system timestamps | Calendar dates avoid timezone confusion |
| Ordering | `@OrderColumn` for Experience/Education/Project; unordered Sets for Skill/Cert/Language | Preserves user-arranged order where it matters |

---

## Testing

### Unit Tests (7 tests)
- `ResumeServiceTest` — Mockito-based tests for all service methods (create, get, update, delete, list, versions, ownership verification)

### Repository Integration Tests (6 tests)
- `ResumeRepositoryTest` — `@DataJpaTest` with H2 testing entity graph queries, pagination, soft-delete filtering, cascade persist, orphan removal

### Controller Integration Tests (7 tests)
- `ResumeControllerTest` — `@WebMvcTest` testing HTTP status codes, validation errors, authentication enforcement, all endpoint contracts

### Total: 21 tests, all passing

---

## Breaking Changes

**None.** This is a new module. Existing auth functionality is untouched and all previous tests continue to pass.

---

## Future Improvements

- [ ] Flyway migrations for production schema (currently using Hibernate auto-DDL for dev)
- [ ] H2 JSONB compatibility — add `MODE=PostgreSQL` or custom dialect for test environment
- [ ] Resume export (PDF/DOCX) — Sprint-06+
- [ ] AI Resume parsing and analysis — Sprint-07+
- [ ] Pagination cursor-based for large datasets
- [ ] Rate limiting on Resume API endpoints
- [ ] Resume sharing / public link feature
- [ ] Full-text search across resume content

---

## Checklist

- [x] Build passes (`./mvnw clean verify`)
- [x] All 21 tests pass
- [x] No compilation errors
- [x] Swagger documentation updated (OpenAPI annotations on all endpoints)
- [x] Domain model documented (`docs/DOMAIN_MODEL.md`)
- [x] No entities exposed directly through REST (all via DTOs)
- [x] Feature-based architecture maintained
- [x] Conventional Commits used
- [x] No TODOs without explanation
- [x] Existing auth tests unaffected
- [x] Application context loads successfully

---

## Commit History

```
docs: add Resume domain model documentation
build: add MapStruct dependency and annotation processor
feat: implement Resume aggregate root and child entities
feat: add custom date range validator
feat: add Resume request and response DTOs
feat: implement MapStruct Resume mapper
feat: add Resume and ResumeVersion repositories
feat: implement Resume service with CRUD and versioning
feat: expose Resume REST API with OpenAPI documentation
test: add Resume service, repository, and controller tests
docs: add Sprint-05 planning document
docs: add PR summary and session handover
```
