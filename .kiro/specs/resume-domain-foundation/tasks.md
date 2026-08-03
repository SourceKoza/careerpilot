# Tasks

## Task 1: Git Branch Setup and MapStruct Dependency
- [x] 1.1 Create feature branch `feature/sprint-05-resume-domain` from `develop`
- [x] 1.2 Add MapStruct dependency (org.mapstruct:mapstruct:1.5.5.Final) to backend/pom.xml properties and dependencies
- [x] 1.3 Add MapStruct annotation processor (org.mapstruct:mapstruct-processor:1.5.5.Final) to maven-compiler-plugin configuration
- [x] 1.4 Verify build passes with `./mvnw clean compile` — no errors from new dependency

## Task 2: Domain Model Documentation
- [x] 2.1 Create `docs/DOMAIN_MODEL.md` with Business Goal section explaining why the Resume domain exists
- [x] 2.2 Document Aggregate Root identification (Resume) with justification
- [x] 2.3 Document entity relationships with cardinality (User → Resume → Experience/Education/Skill/Certification/Project/Language, Resume → ResumeVersion)
- [x] 2.4 Document design decisions with alternatives and trade-offs for each relationship (cascade strategies, fetch types, soft delete, userId reference vs @ManyToOne, JSONB versioning)
- [x] 2.5 Document Future AI Considerations — how the domain supports resume parsing, AI improvements, version history, resume comparison, job matching, and resume export without model changes

## Task 3: Resume Domain Entities and Enums [depends on: Task 1]
- [x] 3.1 Create package structure: `com.sourcekoza.careerpilot.resume.domain` with package-info.java
- [x] 3.2 Implement `SkillProficiency` enum (BEGINNER, INTERMEDIATE, ADVANCED, EXPERT)
- [x] 3.3 Implement `LanguageProficiency` enum (BASIC, CONVERSATIONAL, PROFESSIONAL, NATIVE)
- [x] 3.4 Implement `Resume` entity extending BaseEntity — aggregate root with userId (UUID), title, summary, targetRole, deletedAt, and all @OneToMany collections with appropriate cascade and fetch strategies
- [x] 3.5 Implement `Experience` entity extending BaseEntity — companyName, position, location, startDate, endDate, currentlyWorking, description with @ManyToOne(LAZY) to Resume
- [x] 3.6 Implement `Education` entity extending BaseEntity — institution, degree, fieldOfStudy, startDate, endDate, grade, description with @ManyToOne(LAZY) to Resume
- [x] 3.7 Implement `Skill` entity extending BaseEntity — name, proficiency (SkillProficiency), category with @ManyToOne(LAZY) to Resume
- [x] 3.8 Implement `Certification` entity extending BaseEntity — name, issuingOrganization, issueDate, expiryDate, credentialId, credentialUrl with @ManyToOne(LAZY) to Resume
- [x] 3.9 Implement `Project` entity extending BaseEntity — name, description, technologiesUsed, projectUrl, startDate, endDate with @ManyToOne(LAZY) to Resume
- [x] 3.10 Implement `Language` entity extending BaseEntity — name, proficiency (LanguageProficiency) with @ManyToOne(LAZY) to Resume
- [x] 3.11 Implement `ResumeVersion` entity extending BaseEntity — versionNumber, content (JSONB), changeSummary with @ManyToOne(LAZY) to Resume
- [x] 3.12 Add @Table annotations with indexes (idx_resume_user_id, idx_resume_deleted_at, idx_version_resume_id) and @OrderColumn where needed
- [x] 3.13 Verify build compiles cleanly with all entities

## Task 4: Custom Validation [depends on: Task 3]
- [x] 4.1 Create package `com.sourcekoza.careerpilot.resume.validation`
- [x] 4.2 Implement `@ValidDateRange` custom constraint annotation (applicable to types with startDate/endDate)
- [x] 4.3 Implement `DateRangeValidator` (ConstraintValidator) — validates endDate >= startDate when both are present
- [x] 4.4 Verify custom validator compiles and integrates with Bean Validation

## Task 5: Request and Response DTOs [depends on: Task 3]
- [x] 5.1 Create package `com.sourcekoza.careerpilot.resume.dto`
- [x] 5.2 Implement `CreateResumeRequest` record with @NotBlank, @Size, @Valid on nested collections
- [x] 5.3 Implement `UpdateResumeRequest` record with same shape as CreateResumeRequest
- [x] 5.4 Implement `ExperienceRequest` record with Bean Validation annotations and @ValidDateRange
- [x] 5.5 Implement `EducationRequest` record with Bean Validation annotations and @ValidDateRange
- [x] 5.6 Implement `SkillRequest`, `CertificationRequest`, `ProjectRequest`, `LanguageRequest` records with Bean Validation
- [x] 5.7 Implement `ResumeResponse` record with all nested child response records
- [x] 5.8 Implement `ResumeSummaryResponse` record (id, title, summary, targetRole, createdAt, updatedAt — no children)
- [x] 5.9 Implement `ResumeVersionResponse` record (id, versionNumber, changeSummary, createdAt)
- [x] 5.10 Implement `ExperienceResponse`, `EducationResponse`, `SkillResponse`, `CertificationResponse`, `ProjectResponse`, `LanguageResponse` records
- [x] 5.11 Verify all DTOs compile cleanly

## Task 6: MapStruct Mapper [depends on: Task 3, Task 5]
- [x] 6.1 Create package `com.sourcekoza.careerpilot.resume.mapper`
- [x] 6.2 Implement `ResumeMapper` interface with @Mapper(componentModel = "spring")
- [x] 6.3 Add `toEntity(CreateResumeRequest)` mapping method
- [x] 6.4 Add `toResponse(Resume)` mapping method with nested child entity conversions
- [x] 6.5 Add `toSummaryResponse(Resume)` mapping method
- [x] 6.6 Add `updateEntity(UpdateResumeRequest, @MappingTarget Resume)` method for partial updates
- [x] 6.7 Add child entity mapping methods (Experience/Education/Skill/Certification/Project/Language request→entity and entity→response)
- [x] 6.8 Verify MapStruct generates implementation at compile time without errors

## Task 7: Repository Layer [depends on: Task 3]
- [x] 7.1 Create package `com.sourcekoza.careerpilot.resume.repository`
- [x] 7.2 Implement `ResumeRepository` extending JpaRepository<Resume, UUID> with: findByIdAndUserIdAndDeletedAtIsNull (with @EntityGraph for full load), findByUserIdAndDeletedAtIsNull (paginated), countByUserIdAndDeletedAtIsNull
- [x] 7.3 Implement `ResumeVersionRepository` extending JpaRepository<ResumeVersion, UUID> with: findByResumeIdOrderByVersionNumberDesc, countByResumeId
- [x] 7.4 Verify repositories compile and Spring Data can derive queries

## Task 8: Service Layer [depends on: Task 4, Task 6, Task 7]
- [x] 8.1 Create package `com.sourcekoza.careerpilot.resume.service`
- [x] 8.2 Implement `ResumeService` with @Service and @Transactional annotations
- [x] 8.3 Implement `createResume(UUID userId, CreateResumeRequest)` — map, set userId, save, return response
- [x] 8.4 Implement `getResume(UUID userId, UUID resumeId)` — find with ownership check, map to response
- [x] 8.5 Implement `updateResume(UUID userId, UUID resumeId, UpdateResumeRequest)` — ownership check, create version snapshot before update, update entity, save, return response
- [x] 8.6 Implement `deleteResume(UUID userId, UUID resumeId)` — ownership check, set deletedAt = Instant.now(), save
- [x] 8.7 Implement `listResumes(UUID userId, Pageable)` — return paginated summary responses (no children loaded)
- [x] 8.8 Implement `getVersions(UUID userId, UUID resumeId)` — ownership check, return version history
- [x] 8.9 Add proper exception handling — ResourceNotFoundException for missing/unauthorized resumes (reuse existing exception patterns)

## Task 9: REST Controller [depends on: Task 8]
- [x] 9.1 Create package `com.sourcekoza.careerpilot.resume.controller`
- [x] 9.2 Implement `ResumeController` with @RestController, @RequestMapping("/api/v1/resumes")
- [x] 9.3 Implement POST `/api/v1/resumes` — extract userId from SecurityContext, @Valid request body, return 201 with ApiResponse<ResumeResponse>
- [x] 9.4 Implement GET `/api/v1/resumes/{id}` — return 200 with ApiResponse<ResumeResponse>
- [x] 9.5 Implement PUT `/api/v1/resumes/{id}` — @Valid request body, return 200 with ApiResponse<ResumeResponse>
- [x] 9.6 Implement DELETE `/api/v1/resumes/{id}` — return 204 No Content
- [x] 9.7 Implement GET `/api/v1/resumes` — pagination params (page, size), return 200 with ApiResponse<PageResponse<ResumeSummaryResponse>>
- [x] 9.8 Implement GET `/api/v1/resumes/{id}/versions` — return 200 with ApiResponse<List<ResumeVersionResponse>>
- [x] 9.9 Add OpenAPI annotations (@Tag, @Operation, @ApiResponse) to all endpoints for Swagger documentation
- [x] 9.10 Update SecurityConfig to permit/authenticate resume endpoints appropriately

## Task 10: Unit Tests — Service Layer [depends on: Task 8]
- [x] 10.1 Create `ResumeServiceTest` in test source tree with Mockito mocks for ResumeRepository, ResumeVersionRepository, ResumeMapper
- [x] 10.2 Test createResume — verify entity created, userId set, saved, response mapped
- [x] 10.3 Test getResume — verify ownership check passes and fails (ResourceNotFoundException)
- [x] 10.4 Test updateResume — verify version snapshot created before update, entity updated and saved
- [x] 10.5 Test deleteResume — verify soft delete sets deletedAt, does not physically remove
- [x] 10.6 Test listResumes — verify pagination and summary mapping
- [x] 10.7 Test getVersions — verify ownership check and version list returned

## Task 11: Repository Integration Tests [depends on: Task 7]
- [x] 11.1 Create `ResumeRepositoryTest` with @DataJpaTest annotation using H2
- [x] 11.2 Test findByIdAndUserIdAndDeletedAtIsNull — returns resume for correct user, empty for wrong user, empty for soft-deleted resume
- [x] 11.3 Test findByUserIdAndDeletedAtIsNull with pagination — correct page size and content
- [x] 11.4 Test countByUserIdAndDeletedAtIsNull — counts only active resumes for user
- [x] 11.5 Test cascade persist — saving Resume with child entities persists all children
- [x] 11.6 Test orphan removal — removing a child from collection deletes it from DB

## Task 12: Controller Integration Tests [depends on: Task 9]
- [x] 12.1 Create `ResumeControllerTest` with @WebMvcTest and mocked ResumeService
- [x] 12.2 Test POST /api/v1/resumes — valid request returns 201, invalid request returns 400 with validation errors
- [x] 12.3 Test GET /api/v1/resumes/{id} — returns 200 with resume, returns 404 for non-existent
- [x] 12.4 Test PUT /api/v1/resumes/{id} — valid update returns 200, invalid returns 400
- [x] 12.5 Test DELETE /api/v1/resumes/{id} — returns 204
- [x] 12.6 Test GET /api/v1/resumes — returns paginated list
- [x] 12.7 Test authentication — unauthenticated requests return 401

## Task 13: Build Verification and Swagger Check [depends on: Task 10, Task 11, Task 12]
- [x] 13.1 Run full build with `./mvnw clean verify` — all tests pass, no compilation errors
- [x] 13.2 Verify Swagger UI loads at /swagger-ui.html and shows all Resume endpoints with correct schemas
- [x] 13.3 Verify existing auth tests still pass — no regressions
- [x] 13.4 Verify application starts successfully and health endpoint responds
- [x] 13.5 Commit all changes with conventional commit messages and prepare PR summary
