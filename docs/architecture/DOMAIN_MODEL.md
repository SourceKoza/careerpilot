# Resume Domain Model

**Module:** `com.sourcekoza.careerpilot.resume`
**Sprint:** 05 — Resume Domain Foundation
**Author:** SourceKoza Labs Engineering
**Last Updated:** 2025

---

## Business Goal

The Resume domain exists as the **core business capability** of the CareerPilot AI platform.

Every feature in CareerPilot revolves around a user's resume:

- **AI Resume Analysis** reads structured resume data to provide improvement suggestions
- **Resume Tailoring** modifies resume content to match specific job descriptions
- **Cover Letter Generation** uses resume context to produce personalized cover letters
- **Job Matching** compares structured skills and experience against job requirements
- **Resume Export** transforms resume data into PDF/DOCX formats
- **MCP Tools** expose resume operations to AI agents

Without a well-designed Resume domain, none of these features can exist reliably.

### Why a Dedicated Domain?

The Resume is not a "profile page." It is a **structured, versioned, AI-readable document** that:

1. **Has its own lifecycle** — created, edited, versioned, soft-deleted, restored, exported
2. **Contains rich structured data** — experiences, education, skills, certifications, projects, languages
3. **Evolves independently of the User** — a user may have multiple resumes tailored for different roles
4. **Serves as input to AI systems** — parsing, analysis, tailoring, comparison, matching

The Resume domain is designed to be **complete and stable before AI features are introduced**. AI features consume the domain; they do not define it.

---

## Aggregate Root

### Resume is the Aggregate Root

The **Resume** entity is the single aggregate root for the resume bounded context.

### Why Resume?

An aggregate root is the single entry point for all modifications within a consistency boundary. Resume qualifies because:

1. **Child entities have no independent lifecycle.** An Experience, Skill, or Education entry has no meaning outside the Resume it belongs to. You never query "all experiences across all resumes" — you always access them through a specific Resume.

2. **Transactional consistency.** When a user updates their resume, all child modifications (add a skill, remove an experience, reorder education) must succeed or fail as a single unit. The Resume is the transaction boundary.

3. **Invariant enforcement.** Business rules span children — for example, date consistency (endDate ≥ startDate) and collection constraints. Only the aggregate root can enforce these invariants because it sees the full picture.

4. **Identity boundary.** External systems (AI, export, versioning) reference the Resume by its ID. They never reference a Skill or Experience independently.

### What This Means in Practice

```
┌─────────────────────────────────────────────────────────┐
│                    RESUME (Aggregate Root)                │
│                                                          │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │ Experience  │  │  Education  │  │    Skill    │     │
│  │  (ordered)  │  │  (ordered)  │  │ (unordered) │     │
│  └─────────────┘  └─────────────┘  └─────────────┘     │
│                                                          │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │Certification│  │   Project   │  │  Language   │     │
│  │ (unordered) │  │  (ordered)  │  │ (unordered) │     │
│  └─────────────┘  └─────────────┘  └─────────────┘     │
│                                                          │
│  ┌──────────────────────────────────────────────────┐   │
│  │         ResumeVersion (audit trail)               │   │
│  │         — NOT orphan-removed on edit —            │   │
│  └──────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

- All reads go through the Resume repository
- All writes go through the Resume service
- No child entity has its own repository or service
- Versions are the one exception — they have a repository for query convenience, but are still created only through the Resume aggregate

---

## Entity Relationships with Cardinality

### Relationship Diagram

```
User (1)
  │
  │  userId (UUID reference — no JPA relationship)
  │
  ▼
Resume (Many)                    ── Aggregate Root
  │
  ├──→ Experience (Many)         ── CascadeType.ALL, orphanRemoval=true, @OrderColumn
  │
  ├──→ Education (Many)          ── CascadeType.ALL, orphanRemoval=true, @OrderColumn
  │
  ├──→ Skill (Many)              ── CascadeType.ALL, orphanRemoval=true
  │
  ├──→ Certification (Many)      ── CascadeType.ALL, orphanRemoval=true
  │
  ├──→ Project (Many)            ── CascadeType.ALL, orphanRemoval=true, @OrderColumn
  │
  ├──→ Language (Many)           ── CascadeType.ALL, orphanRemoval=true
  │
  └──→ ResumeVersion (Many)      ── CascadeType.PERSIST + MERGE, NO orphanRemoval
```

### Cardinality Summary

| Parent | Relationship | Child | Cardinality | Collection Type |
|--------|-------------|-------|-------------|-----------------|
| User | references | Resume | 1 : Many | — (cross-module) |
| Resume | owns | Experience | 1 : Many | `List` (ordered) |
| Resume | owns | Education | 1 : Many | `List` (ordered) |
| Resume | owns | Skill | 1 : Many | `Set` (unordered) |
| Resume | owns | Certification | 1 : Many | `Set` (unordered) |
| Resume | owns | Project | 1 : Many | `List` (ordered) |
| Resume | owns | Language | 1 : Many | `Set` (unordered) |
| Resume | records | ResumeVersion | 1 : Many | `List` (time-ordered) |

### Why List vs Set?

- **List (ordered):** Experience, Education, and Project entries have a user-defined display order. The user arranges them deliberately — chronologically, by importance, or strategically for a specific role. `@OrderColumn(name = "display_order")` persists this arrangement.

- **Set (unordered):** Skills, Certifications, and Languages have no inherent order in the domain. Presentation order (alphabetical, by proficiency) is a UI concern handled at the DTO/response level.

---

## Design Decisions

### Decision 1: userId as UUID Column — Not @ManyToOne

**Choice:** Store `userId` as a plain `UUID` column on Resume. No JPA relationship to the User entity.

**Why:**
- **Module boundary preservation.** The auth module and resume module are separate feature modules. A `@ManyToOne` creates a compile-time dependency between them — the resume package would need to import `com.sourcekoza.careerpilot.auth.domain.User`.
- **Independent lifecycle.** Resume queries never need the full User entity. The userId is sufficient for ownership filtering (`WHERE user_id = ?`).
- **Microservice readiness.** If these modules are later split into separate services, a UUID reference is portable. A JPA foreign key across service boundaries is impossible.
- **Query simplicity.** An indexed UUID column supports all access patterns: find by user, count by user, paginate by user.

**Alternatives Considered:**
| Alternative | Why Rejected |
|-------------|-------------|
| `@ManyToOne(fetch = LAZY)` to User | Couples modules at compile time. Breaks feature-based architecture. Creates technical debt for future service split. |
| Shared kernel with common User reference | Over-engineered for current needs. Adds indirection without solving a real problem. |
| String userId | Loses type safety. UUID enforces format at the Java level. |

**Trade-offs:**
- No Hibernate-level cascade from User deletion. User account deletion must be handled via application-level event or database trigger (future sprint).
- No JPA-enforced referential integrity at the Hibernate level. Database-level FK constraint is still applied via Flyway migration.

**Production Consideration:** An index on `user_id` is critical. Without it, every "list my resumes" query becomes a full table scan. The `@Index` annotation ensures this from day one.

---

### Decision 2: Soft Delete via `Instant deletedAt`

**Choice:** Use `Instant deletedAt` (nullable) on the Resume entity only. Null means active; non-null means soft-deleted.

**Why:**
- **Auditability over boolean.** `deletedAt` tells us *when* it was deleted — critical for compliance, debugging, time-based restore policies ("restore within 30 days"), and audit trails.
- **Query simplicity.** `WHERE deleted_at IS NULL` is clean, indexable, and explicit.
- **Only on aggregate root.** Child entities don't need their own soft delete. When a Resume is soft-deleted, its children are implicitly hidden — they're only ever accessed through the Resume.

**Alternatives Considered:**
| Alternative | Why Rejected |
|-------------|-------------|
| `boolean deleted` | Loses temporal information. Cannot implement time-based restore. Slightly smaller column but negligible trade-off. |
| `@Where("deleted_at IS NULL")` | Auto-filters globally. Makes it impossible to query deleted resumes for admin restore features. Implicit magic hurts debugging. |
| `@SQLDelete` override | Hijacks Hibernate's delete mechanism. Surprises developers, complicates debugging, breaks standard expectations. |
| Separate "trash" table | Breaks referential integrity. Complicates restore logic. Adds schema complexity for no real benefit. |

**Trade-offs:**
- Every repository method must explicitly filter `deletedAt IS NULL`. This is intentional — explicit filtering is safer than implicit annotation magic.
- Query methods are named clearly: `findByUserIdAndDeletedAtIsNull`.

**Production Consideration:** The `idx_resume_deleted_at` index supports efficient queries for both active resumes (NULL) and admin queries for deleted resumes (NOT NULL). Partial index on `deleted_at IS NULL` would be even more efficient for the common case — can be added via Flyway if needed.

---

### Decision 3: ResumeVersion as JSONB Snapshot

**Choice:** Store version snapshots as a JSONB column (`content`) in the `resume_versions` table. The content is the serialized `ResumeResponse` DTO.

**Why:**
- **Immutability.** A version captures the resume state at a point in time. It is never updated, never partially queried at the field level. It's a frozen snapshot.
- **Schema evolution friendly.** When the Resume entity gains new fields in future sprints, old versions remain valid — they simply have the JSON structure from when they were created. No migration of historical data needed.
- **AI integration ready.** Future AI features (comparison, diff, analysis) will consume the full resume as JSON. Storing it pre-serialized is ideal — no reconstruction cost.
- **Performance.** One column read vs joining 7+ child tables to reconstruct a historical snapshot.

**Alternatives Considered:**
| Alternative | Why Rejected |
|-------------|-------------|
| Duplicate normalized tables (`experience_versions`, `skill_versions`, etc.) | Massive schema bloat. Every new field requires mirroring in version tables. Maintenance nightmare. |
| Event sourcing | Elegant in theory but extreme over-engineering for resume CRUD. Adds operational complexity (event store, projections, replays) with no proportional benefit. |
| Copy entire entity graph into new rows with `version_number` | Wastes storage for unchanged children. Complicates queries. Hard to reconstruct a single version. |

**Trade-offs:**
- Cannot query inside version content with standard JPA. Need native queries or PostgreSQL JSONB operators for any future "search in version history" feature. Acceptable because version queries are rare and read-only.
- JSON size grows with resume complexity. For a resume with many entries, a single version JSON could be 10-50 KB. Acceptable for PostgreSQL JSONB storage.

**Production Consideration:** Version creation happens on every update. Over time, a heavily-edited resume may accumulate many versions. Consider a future retention policy (keep last N versions, or versions older than X days are archived) — but this is a future optimization, not a day-one concern.

---

### Decision 4: Cascade ALL + orphanRemoval for Children; PERSIST/MERGE for Versions

**Choice:** Deliberate cascade strategy per relationship type.

#### Children (Experience, Education, Skill, Certification, Project, Language)

```
CascadeType.ALL + orphanRemoval = true
```

**Why ALL:** Children have no independent lifecycle. When we persist a Resume, its children persist. When we merge, they merge. When we remove... we soft-delete the Resume (not hard-delete), so `CascadeType.REMOVE` effectively never fires — but having it simplifies test cleanup.

**Why orphanRemoval:** When a user updates a resume and removes a skill from the list, JPA must detect the orphan and delete it from the database. Without orphanRemoval, removed children would become detached rows with no parent reference — data pollution.

#### Versions (ResumeVersion)

```
CascadeType.PERSIST + CascadeType.MERGE — NO orphanRemoval
```

**Why NOT ALL:** `CascadeType.REMOVE` would delete all versions when the Resume is removed. Versions are audit trail — they must survive resume deletion (even hard deletion in tests).

**Why NO orphanRemoval:** We never want to accidentally lose version history. If a version is somehow removed from the collection, it should remain in the database as a historical record.

**Why PERSIST + MERGE:** When we create a new version (on resume update), persisting the Resume cascades the new ResumeVersion insert. Merge covers the case where both are detached.

---

### Decision 5: LAZY Fetching with @EntityGraph for Targeted Loading

**Choice:** All `@OneToMany` relationships use `FetchType.LAZY` by default. No exceptions.

**Why:**
- A Resume with 5 experiences, 3 education entries, 10 skills, 2 certifications, 3 projects, 2 languages, and 20 versions would trigger 7+ JOINs on every load.
- The "List User Resumes" endpoint only needs title, summary, and dates — loading all children is catastrophically wasteful.
- LAZY means we pay for what we use. If we need children, we ask explicitly.

**Mitigation of N+1 Problem:**
- **@EntityGraph** on specific repository methods for the "Get Single Resume" endpoint, which loads all children in a single query.
- The "List Resumes" endpoint uses a simple query that only loads Resume root fields.
- No EAGER exceptions exist in this domain. Every child load is explicit and intentional.

**Alternatives Considered:**
| Alternative | Why Rejected |
|-------------|-------------|
| EAGER on frequently-accessed children | Premature optimization. Kills list endpoint performance. What seems "frequently accessed" changes over time. |
| Batch fetching (`@BatchSize`) | Solves N+1 but still loads data you may not need. Less predictable than EntityGraph. |
| Join fetch in every query | Forces all callers to pay the join cost even when they don't need children. |

**Production Consideration:** Monitor Hibernate query logs in development. N+1 issues surface quickly in integration tests. EntityGraph ensures the "get single resume" path is one query, not eight.

---

### Decision 6: @OrderColumn for Experience/Education/Project Ordering

**Choice:** Use `@OrderColumn(name = "display_order")` on ordered collections (Experience, Education, Project). Use unordered `Set` for Skills, Certifications, and Languages.

**Why @OrderColumn over @OrderBy:**
- `@OrderBy("startDate DESC")` re-sorts on every load based on a field value. It doesn't preserve user intent.
- Users arrange resume entries strategically — they may want to highlight a recent contract role above a longer-tenure position, regardless of chronology.
- `@OrderColumn` persists the user's explicit arrangement as a `display_order` integer column.

**Why Set for Skills/Certifications/Languages:**
- These entries have no inherent user-defined order in the domain model.
- Presentation sorting (alphabetical, by proficiency) is a view concern, not a domain concern.
- Using `Set` communicates domain intent: "these are an unordered collection of unique entries."

**Trade-offs:**
- `@OrderColumn` adds a column to the database and requires `List` (not `Set`), which means JPA manages insertion order.
- Reordering requires updating `display_order` for multiple rows. For typical resume sizes (5-15 entries), this is negligible.

---

### Decision 7: MapStruct for Compile-Time Safe Mapping

**Choice:** Use MapStruct for all entity ↔ DTO conversions.

**Why:**
- **Compile-time safety.** Field name mismatches, type incompatibilities, and missing mappings are caught at build time, not runtime.
- **Zero reflection.** Generated code is plain Java method calls — no runtime performance overhead.
- **Boilerplate reduction.** 8 entities × 2 directions (request→entity, entity→response) = 16+ mapping methods. Manual mapping would be 200+ lines of trivial assignments.
- **Nested mapping.** MapStruct automatically handles Resume → ResumeResponse with nested Experience → ExperienceResponse, Education → EducationResponse, etc.

**Alternatives Considered:**
| Alternative | Why Rejected |
|-------------|-------------|
| Manual mapping methods | Full control but verbose, error-prone, and hard to maintain as fields are added. |
| ModelMapper / Dozer | Runtime reflection-based. Slower, harder to debug, field renames break silently at runtime instead of compile time. |
| Kotlin data class copy | Not applicable — project is Java. |

**Trade-offs:**
- Adds annotation processor to build pipeline. Slightly increases compile time (negligible in practice).
- Generated code is in `target/` — developers must know to check generated source for debugging.
- MapStruct `1.5.x` integrates cleanly with Spring Boot 3.x and Java records.

---

### Decision 8: LocalDate for User Dates, Instant for System Timestamps

**Choice:**
- `LocalDate` for human-meaningful dates: startDate, endDate, issueDate, expiryDate
- `Instant` for system timestamps: createdAt, updatedAt, deletedAt (inherited from BaseEntity)

**Why:**
- Experience start/end dates are calendar dates ("March 2020"), not precise moments in time. A user says "I started this job in March 2020" — that's a date, not a timestamp.
- `LocalDate` avoids timezone confusion. "2020-03-01" means March 1st everywhere — no server timezone interpretation.
- System timestamps (`createdAt`, `updatedAt`) represent precise moments and are correctly `Instant` (UTC-based, timezone-aware).

**Alternatives Considered:**
| Alternative | Why Rejected |
|-------------|-------------|
| `Instant` for everything | Forces timezone handling for user-facing dates. API would return "2020-03-01T00:00:00Z" which is confusing and misleading. |
| `String` dates | Loses type safety, validation, and comparison operations. |
| `YearMonth` | More semantically correct for experience ("March 2020"), but limits precision — some users know exact start dates. `LocalDate` is more flexible. |

---

### Decision 9: Enum-Based Proficiency Levels (Not Numeric)

**Choice:** Define proficiency as named enums, not numeric scales.

```
SkillProficiency:    BEGINNER → INTERMEDIATE → ADVANCED → EXPERT
LanguageProficiency: BASIC → CONVERSATIONAL → PROFESSIONAL → NATIVE
```

**Why:**
- **Unambiguous semantics.** "What does 7/10 in Java mean?" is subjective. "ADVANCED" has a clearer shared meaning across users and AI systems.
- **AI interpretability.** Named levels are easier for AI models to parse, compare, and generate than arbitrary numbers.
- **Validation simplicity.** Enum constraint is automatic — invalid values are rejected at deserialization.
- **Display flexibility.** The UI can map enums to any visual representation (stars, bars, percentages) without domain changes.

**Why not CEFR (A1-C2) for languages?** CEFR is well-known for formal language assessment, but most job applications use simpler categories. "Professional working proficiency" is universally understood by recruiters. If CEFR is needed later, it can be an optional additional field.

---

### Decision 10: technologiesUsed as Plain String (Not Normalized)

**Choice:** Store `technologiesUsed` on Project as a simple `String` (max 300 chars), not a normalized join table or `@ElementCollection`.

**Why:**
- **Display-only data.** These are presentation tags, not queryable entities. No feature requires "find all projects using React" across all users.
- **Simplicity.** A join table or `@ElementCollection` adds schema complexity (extra table, extra queries, extra mapping) for zero business value at this stage.
- **AI handles structure.** If future AI features need structured technology data, the AI layer will parse and index them separately. The resume domain remains simple — it stores what the user typed.

**Alternatives Considered:**
| Alternative | Why Rejected |
|-------------|-------------|
| `@ElementCollection` (List<String>) | Creates a separate table. Extra join on every project load. No business query requires it. |
| Normalized Technology entity with join table | Over-engineering. Implies global technology management, deduplication, taxonomy — none of which are Sprint 05 requirements. |
| JSON array column | Better than join table but still adds serialization complexity for a display-only field. |

**Trade-offs:**
- No structured querying of technologies. Acceptable — this is not a requirement.
- Comma-separated or free-text format is user-defined. The AI layer can parse it when needed.

---

## Future AI Considerations

This domain model is designed to support the following AI features **without structural changes** to the entity model, database schema, or API contracts.

### Resume Parsing (AI → Domain)

**How it works:** An AI service parses an uploaded PDF/DOCX and produces structured data.

**Why the model supports it:** The AI parser output maps directly to `CreateResumeRequest` DTO. The parser extracts company names, positions, dates, skills, etc., and populates the same request structure that a user would manually fill. The Resume domain doesn't know or care whether the data came from manual input or AI parsing.

```
PDF → AI Parser → CreateResumeRequest → ResumeService.createResume() → Database
```

No domain changes required. The parser is a separate module that produces the same DTO the REST API consumes.

---

### AI Resume Improvements (Domain → AI → Domain)

**How it works:** AI reads a resume, suggests improvements (better descriptions, stronger action verbs, quantified achievements), and the user saves the improved version.

**Why the model supports it:**
- **Read:** `ResumeResponse` DTO provides the complete resume in a structured, AI-readable format.
- **Write:** The improved resume is saved via `UpdateResumeRequest`, which triggers version creation automatically.
- **Versioning:** The pre-improvement state is preserved as a `ResumeVersion` snapshot. The user can always compare or revert.

```
ResumeResponse → AI Improver → UpdateResumeRequest → ResumeService.updateResume()
                                                         ↓
                                                   (auto-creates version snapshot)
```

---

### Version History and Comparison

**How it works:** Users or AI compare two versions of a resume to see what changed.

**Why the model supports it:**
- `ResumeVersion.content` stores the complete resume state as JSONB at each point in time.
- Comparing two versions is a JSON diff operation — no complex multi-table reconstruction needed.
- Version numbers are sequential per resume, making "version 3 vs version 5" queries simple.
- The JSONB format is self-contained — each version has all the data needed for display or comparison without joining other tables.

```
Version A (JSON) ←→ JSON Diff ←→ Version B (JSON) → Structured Delta
```

---

### Resume Comparison (Two Versions Side-by-Side)

**How it works:** A user or AI compares resume version 3 with version 7 to understand how the resume evolved.

**Why the model supports it:**
- Both versions are stored as complete JSONB snapshots in the `resume_versions` table.
- Each snapshot follows the `ResumeResponse` DTO structure — consistent, predictable, parseable.
- A comparison service can deserialize both JSONs, diff them field-by-field, and present additions/removals/modifications.
- No schema migration is needed to support this — the data is already there.

---

### Job Matching (Domain → AI Scoring)

**How it works:** AI compares a user's resume against job requirements to produce a match score.

**Why the model supports it:**
- **Skills** are structured with name, proficiency level, and category — perfect for matching against job skill requirements.
- **Experience** entries have company name, position, dates, and description — AI can extract years of experience, industry, and seniority.
- **Education** entries provide degree, field of study, and institution — matchable against job education requirements.
- **Proficiency enums** provide categorical comparison: "Job requires ADVANCED Java, candidate has EXPERT" → strong match.

```
ResumeResponse.skills     ←→ JobRequirement.requiredSkills     → Skill Match Score
ResumeResponse.experiences ←→ JobRequirement.experienceYears   → Experience Match
ResumeResponse.educations  ←→ JobRequirement.educationLevel    → Education Match
                                                                → Composite Score
```

No domain changes needed. The structured data is already queryable and comparable.

---

### Resume Export (Domain → PDF/DOCX)

**How it works:** Transform resume data into formatted PDF or DOCX documents using templates.

**Why the model supports it:**
- `ResumeResponse` DTO provides ALL resume data in a single, nested response structure.
- An export service receives the `ResumeResponse` and feeds it to a template engine (Thymeleaf, JasperReports, Apache POI).
- The ordered collections (Experience, Education, Project) preserve user's display arrangement — the export renders them in the intended order.
- No additional queries or data transformation needed — the response DTO is the complete export input.

```
ResumeService.getResume() → ResumeResponse → Export Template Engine → PDF/DOCX
```

---

### Summary: Why No Structural Changes Are Needed

| AI Feature | Input From Domain | Output To Domain | Domain Change Required |
|------------|-------------------|------------------|----------------------|
| Resume Parsing | — | `CreateResumeRequest` | None |
| AI Improvements | `ResumeResponse` | `UpdateResumeRequest` | None |
| Version History | `ResumeVersion.content` | — | None |
| Resume Comparison | Two `ResumeVersion.content` JSONs | — | None |
| Job Matching | `ResumeResponse` (skills, experience) | — | None |
| Resume Export | `ResumeResponse` | — | None |

The domain model is **AI-ready by being well-structured**, not by being AI-aware. Clean DTOs, structured enums, JSONB versioning, and complete response objects provide everything AI features need to read and write resume data through the existing API surface.

---

## Appendix: Entity Field Reference

### Resume (Aggregate Root)

| Field | Type | Constraints | Notes |
|-------|------|-------------|-------|
| id | UUID | PK, auto-generated | Inherited from BaseEntity |
| userId | UUID | NOT NULL, indexed | Cross-module reference |
| title | String(100) | NOT NULL | e.g., "Senior Java Developer Resume" |
| summary | String(500) | nullable | Professional summary |
| targetRole | String(100) | nullable | e.g., "Backend Engineer" |
| deletedAt | Instant | nullable | Soft delete timestamp |
| createdAt | Instant | auto | Inherited from BaseEntity |
| updatedAt | Instant | auto | Inherited from BaseEntity |
| version | Long | optimistic lock | Inherited from BaseEntity |

### Child Entity Shared Pattern

All child entities follow:
- Extend `BaseEntity` (id, createdAt, updatedAt, version)
- `@ManyToOne(fetch = LAZY)` back to Resume
- No independent repository or service
- Accessed only through the Resume aggregate

---

*This document is a living artifact. It will be updated as the domain evolves in future sprints.*
