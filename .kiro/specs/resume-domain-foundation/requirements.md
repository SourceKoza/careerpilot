# Requirements Document

## Introduction

The Resume Domain Foundation establishes the core business model for resumes within the CareerPilot AI platform. This sprint designs and implements a production-grade Resume domain model that will later support AI-powered features. This sprint is NOT about AI — it focuses on domain design, persistence, validation, and REST API.

## Glossary

- **Resume_Module**: The feature-based module responsible for all resume-related domain logic, persistence, validation, and REST API endpoints
- **Resume**: The aggregate root entity representing a user's professional resume
- **ResumeVersion**: An entity representing a version snapshot of a resume
- **Experience**: An entity representing a professional work experience entry
- **Education**: An entity representing an educational qualification entry
- **Skill**: An entity representing a professional skill
- **Certification**: An entity representing a professional certification
- **Project**: An entity representing a notable project
- **Language**: An entity representing a spoken/written language proficiency
- **User**: The authenticated user entity from the auth module who owns resumes
- **Aggregate_Root**: A DDD pattern where one entity controls access to and enforces invariants over its child entities
- **BaseEntity**: The shared abstract entity providing UUID primary key, optimistic locking version, and audit timestamps
- **DTO**: Data Transfer Object used to decouple the REST API from JPA entity internals

## Requirements

### Requirement 1: Domain Model Documentation

**User Story:** As a developer, I want a comprehensive domain model document, so that I can understand the Resume domain design decisions before implementation begins.

#### Acceptance Criteria

1. THE Resume_Module SHALL have a domain model document at `docs/DOMAIN_MODEL.md` explaining the business goal of the Resume domain
2. THE domain model document SHALL identify the Aggregate_Root with justification
3. THE domain model document SHALL describe entity relationships with cardinality between User, Resume, ResumeVersion, Experience, Education, Skill, Certification, Project, and Language
4. THE domain model document SHALL document design decisions with alternatives considered and trade-offs for each relationship
5. THE domain model document SHALL explain how the domain model supports future AI features (resume parsing, AI resume improvements, version history, resume comparison, job matching, resume export) without changing the domain model

### Requirement 2: Resume Domain Entities

**User Story:** As a developer, I want a Resume aggregate root and child entities, so that I can manage the complete resume lifecycle following DDD principles.

#### Acceptance Criteria

1. THE Resume_Module SHALL implement entities: Resume, ResumeVersion, Experience, Education, Skill, Certification, Project, and Language
2. ALL entities SHALL extend BaseEntity (UUID primary key, optimistic locking, Instant timestamps)
3. THE Resume entity SHALL serve as the Aggregate_Root for the resume domain
4. THE Resume_Module SHALL use appropriate JPA mappings with LAZY fetching by default
5. THE Resume_Module SHALL explain every cascade strategy chosen
6. THE Resume_Module SHALL use feature-based package structure: resume/controller, resume/domain, resume/dto, resume/mapper, resume/repository, resume/service, resume/validation

### Requirement 3: Validation

**User Story:** As a developer, I want proper validation on all resume data, so that the system enforces data integrity at boundaries.

#### Acceptance Criteria

1. THE Resume_Module SHALL use Bean Validation for required fields, length limits, date consistency, and collection sizes where appropriate
2. THE Resume_Module SHALL NOT place business logic inside controllers
3. WHEN a start date and end date are both provided, THE Resume_Module SHALL validate that the end date is not before the start date

### Requirement 4: DTOs and Mapping

**User Story:** As a developer, I want separate request and response DTOs, so that JPA entities are never exposed through the REST API.

#### Acceptance Criteria

1. THE Resume_Module SHALL define separate Request DTOs and Response DTOs
2. THE Resume_Module SHALL NOT expose JPA entities directly through REST
3. THE Resume_Module SHALL prefer MapStruct for mapping if it reduces boilerplate; if manual mapping is chosen, the reason SHALL be explained

### Requirement 5: Resume REST API

**User Story:** As a user, I want a REST API to manage my resumes, so that I can create, view, update, and delete resumes.

#### Acceptance Criteria

1. THE Resume_Module SHALL implement REST endpoints for: Create Resume, Get Resume, Update Resume, Delete Resume, List User Resumes, Get Resume Versions
2. THE Delete Resume endpoint SHALL use logical delete (the design decision SHALL be explained)
3. THE Resume_Module SHALL NOT implement any AI functionality
4. THE Resume_Module SHALL reuse existing ApiResponse, ErrorResponse, and GlobalExceptionHandler — no duplicate response models

### Requirement 6: Testing

**User Story:** As a developer, I want comprehensive tests for the Resume module, so that I can verify correctness beyond just application startup.

#### Acceptance Criteria

1. THE Resume_Module SHALL have unit tests for services
2. THE Resume_Module SHALL have repository tests
3. THE Resume_Module SHALL have controller integration tests where appropriate

### Requirement 7: Swagger Documentation

**User Story:** As a developer, I want updated Swagger/OpenAPI documentation, so that API consumers can discover the Resume endpoints.

#### Acceptance Criteria

1. THE Resume_Module SHALL update Swagger documentation for all new endpoints
2. THE Resume_Module controller SHALL use appropriate OpenAPI annotations

### Requirement 8: Build and Compatibility

**User Story:** As a developer, I want the build to pass and existing functionality to remain intact, so that the resume domain integrates cleanly.

#### Acceptance Criteria

1. THE project build SHALL pass after implementation
2. THE Resume_Module SHALL NOT break existing functionality
3. THE Resume domain SHALL follow DDD principles where practical
4. THE Resume_Module SHALL maintain feature-based architecture (no layer-based packages)

### Requirement 9: Git Workflow and Delivery

**User Story:** As a developer, I want proper git workflow followed, so that the implementation meets SourceKoza Labs engineering standards.

#### Acceptance Criteria

1. THE implementation SHALL use a feature branch (`feature/sprint-05-resume-domain`) — never directly on main
2. THE implementation SHALL use small, meaningful commits with Conventional Commits prefixes (feat:, fix:, docs:, refactor:, test:, etc.)
3. WHEN implementation is complete, THE sprint SHALL include a Pull Request summary with: Summary, Design Decisions, Testing, Breaking Changes, Future Improvements, and Checklist
4. WHEN implementation is complete, THE sprint SHALL include: Implementation Summary, Design Decisions, Assumptions, Known Limitations, Technical Debt, Recommended Sprint-06, and Session Handover
