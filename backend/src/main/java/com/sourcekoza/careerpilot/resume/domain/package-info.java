/**
 * Resume domain model — entities, value objects, and enums for the Resume aggregate.
 *
 * <p>The Resume entity is the aggregate root. All child entities (Experience, Education,
 * Skill, Certification, Project, Language) exist only within the context of a Resume
 * and are managed through its lifecycle.</p>
 *
 * <p>ResumeVersion stores immutable JSONB snapshots for version history.</p>
 */
package com.sourcekoza.careerpilot.resume.domain;
