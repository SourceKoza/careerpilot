# Sprint FE-05 — Resume Intelligence

## Sprint Goal

Build a production-ready Resume Intelligence module that allows users to upload a master resume, manage multiple resume versions, analyze resumes using AI, and prepare the architecture for future AI resume tailoring.

---

# Business Value

Instead of uploading a different resume for every job, users maintain one Master Resume.

The platform will later generate optimized resumes for every job automatically.

This sprint builds the complete frontend architecture required for that future AI capability.

---

# Features

## Resume Dashboard

Display

- Master Resume
- Resume Versions
- ATS Score
- Missing Skills
- Keyword Match
- AI Suggestions

---

## Resume Upload

Allow user to

- Upload PDF
- Upload DOCX
- Replace Existing Resume
- Show upload progress
- Validate file type
- Validate size

Mock upload only.

---

## Resume Versions

Support

- Master Resume
- Java Backend Resume
- Spring Boot Resume
- AI Engineer Resume
- Staff Engineer Resume

Display

- Version Name
- Last Updated
- Active Badge

---

## Resume Analysis

Mock AI analysis response

Example

ATS Score

89%

Missing Skills

- Docker
- Terraform
- AWS Lambda

Strengths

- Java
- Spring Boot
- Kafka
- Redis

Suggestions

- Add quantified achievements
- Mention AI Agent Platform
- Improve Summary

---

## Resume Preview

Display

- Resume Metadata
- File Name
- Size
- Upload Date

Preview panel placeholder.

---

## Architecture

Follow Feature First Architecture

src/

features/

resume/

components/

hooks/

services/

types/

pages/

---

# Components

ResumeUploader

ResumeCard

ResumePreview

ResumeVersions

ATSScoreCard

MissingSkillsCard

KeywordMatchCard

AISuggestionsCard

ResumeAnalysis

LoadingSkeleton

EmptyState

---

# Types

Resume

ResumeVersion

ResumeAnalysis

ATSScore

SkillGap

ResumeSuggestion

UploadResponse

---

# Service

resume.service.ts

Methods

getResume()

uploadResume()

replaceResume()

analyzeResume()

generateResumeVersion()

getResumeVersions()

---

# React Query

useResume()

useUploadResume()

useResumeAnalysis()

useResumeVersions()

---

# UI States

Loading

Empty

Success

Error

Uploading

---

# Mock Data

Create realistic enterprise mock data.

No hardcoded values inside components.

---

# Acceptance Criteria

✓ Upload works

✓ Resume dashboard displays

✓ Resume analysis card works

✓ ATS score card works

✓ Resume versions list works

✓ React Query implemented

✓ Services separated

✓ Types centralized

✓ Fully responsive

✓ Dark mode supported

✓ No TypeScript errors

✓ No ESLint warnings

✓ Production-quality code

---

# Out of Scope

Actual AI

OCR

Resume parsing

OpenAI integration

Backend APIs

Authentication changes

These will be implemented in later sprints.

---

# Deliverables

- Feature complete Resume module
- Mock service layer
- Responsive UI
- Clean architecture
- Production-quality code