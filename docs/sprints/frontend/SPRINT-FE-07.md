Continue the existing CareerPilot AI project.

Implement Sprint-FE-07 only.

Do NOT implement future sprints.

------------------------------------------------

Read First

1. docs/00-START_HERE.md

2. docs/KIRO_RULES.md

3. docs/03-ARCHITECTURE.md

4. docs/sprints/SPRINT-FE-06.md

5. docs/sprints/SPRINT-FE-07.md

------------------------------------------------

Project Context

CareerPilot is an enterprise-grade AI-powered job platform.

Completed Features

✓ Authentication

✓ Dashboard

✓ AI Job Discovery

✓ AI Missions

✓ Resume Intelligence

✓ AI Job Matching

Reuse the existing design system, feature-first architecture, shared components, utilities, and coding conventions.

Do not duplicate functionality or modify unrelated features.

------------------------------------------------

Objective

Build the AI Resume Tailoring module.

The feature should generate a job-specific resume experience using realistic mock AI responses.

No backend or AI integration is required.

------------------------------------------------

Create

src/features/resume-tailoring/

components/

hooks/

services/

types/

------------------------------------------------

Implement

- ResumeTailoringDashboard
- ResumeComparison
- TailoredResumePreview
- ATSImprovementCard
- KeywordOptimizationCard
- ResumeChangesCard
- ResumeVersionHistory
- ExportActions
- AISuggestionsCard
- LoadingSkeleton
- EmptyState
- ErrorState

------------------------------------------------

Services

resume-tailoring.service.ts

Methods

generateTailoredResume()

compareResume()

getResumeVersions()

saveResumeVersion()

exportResume()

------------------------------------------------

Hooks

useTailoredResume()

useResumeComparison()

useResumeVersions()

useExportResume()

------------------------------------------------

Dashboard

Display

- Original Resume
- Tailored Resume
- ATS Improvement
- Resume Comparison
- Keyword Optimization
- Resume Changes
- AI Suggestions
- Version History
- Export Actions

------------------------------------------------

Quality

Strict TypeScript

TanStack Query

Reusable Components

Feature First Architecture

SOLID Principles

Responsive

Dark Mode

Skeleton Loading

No hardcoded data in UI

------------------------------------------------

Definition of Done

npm run lint

Must pass.

npm run build

Must pass.

Provide

1. Summary
2. Folder Tree
3. Files Created
4. Files Modified
5. Features Implemented
6. Remaining TODOs

Do not stop until Sprint-FE-07 is fully complete.