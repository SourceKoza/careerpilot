# Sprint FE-06 — AI Job Matching

## Sprint Goal

Build the AI Job Matching module that compares a user's resume against available jobs and provides an intelligent compatibility score, missing skills, strengths, and personalized recommendations.

This sprint focuses on frontend architecture using realistic mock data. No backend or AI integration will be implemented.

---

# Business Value

Traditional job portals force users to manually determine whether they are a good fit for a job.

CareerPilot automatically analyzes compatibility between a user's resume and job descriptions, helping users prioritize applications with the highest chance of success.

This feature becomes one of the platform's primary AI capabilities.

---

# User Story

As a job seeker,

I want to instantly know how well my resume matches a job,

so that I can apply to the most relevant opportunities first and improve weak areas before applying.

---

# Features

## AI Match Dashboard

Display

- Overall Match Score
- ATS Compatibility
- Skill Match
- Experience Match
- Education Match
- Seniority Match

---

## Match Summary

Display

- Excellent Match
- Good Match
- Moderate Match
- Poor Match

Include AI-generated explanation (mock).

Example

"You satisfy most backend requirements. Adding Terraform and Kubernetes experience would significantly improve your chances."

---

## Skill Analysis

Display

Matched Skills

Missing Skills

Recommended Skills

Preferred Skills

Highlight

Required vs Preferred skills.

---

## Strength Analysis

Display

Strengths identified from resume.

Examples

- Spring Boot
- Kafka
- Redis
- REST APIs
- Microservices
- AWS

---

## Gap Analysis

Display

Technical Gaps

Experience Gaps

Certification Suggestions

Project Suggestions

---

## Recommended Improvements

Examples

- Mention Kubernetes project
- Add measurable achievements
- Improve professional summary
- Include cloud deployment experience

---

## Job Compatibility Breakdown

Display

Technical Skills

Experience

Education

Keywords

Domain Knowledge

Soft Skills

Each category displays its own score.

---

## Apply Readiness

Display

Ready to Apply

Needs Minor Improvements

Needs Resume Updates

Not Recommended Yet

---

# Architecture

Feature First Architecture

src/

features/

job-matching/

components/

hooks/

services/

types/

---

# Components

JobMatchCard

MatchScoreCard

MatchSummary

SkillComparison

StrengthCard

GapAnalysisCard

CompatibilityBreakdown

RecommendationCard

ApplyReadinessBadge

JobMatchDashboard

LoadingSkeleton

EmptyState

ErrorState

---

# Types

JobMatch

MatchScore

SkillComparison

Strength

GapAnalysis

Recommendation

CompatibilityScore

ApplyReadiness

---

# Services

job-matching.service.ts

Methods

getJobMatch()

getSkillComparison()

getRecommendations()

getCompatibilityBreakdown()

analyzeJobMatch()

Mock responses only.

---

# React Query Hooks

useJobMatch()

useSkillComparison()

useRecommendations()

useCompatibility()

---

# Mock Data

Create realistic enterprise mock responses.

Example

Overall Match

92%

Matched Skills

- Java
- Spring Boot
- Kafka
- Redis
- Docker

Missing Skills

- Terraform
- AWS Lambda

Strengths

- Backend Architecture
- Distributed Systems
- API Design

Recommendations

- Add AI project
- Mention Docker production deployment
- Include measurable business impact

---

# UI Requirements

Responsive

Desktop

Tablet

Mobile

Dark Mode

Loading States

Error States

Skeleton Loading

Accessible Components

Reusable Cards

Consistent spacing

---

# Technical Requirements

Use Feature First Architecture.

No hardcoded values inside UI.

Strict TypeScript.

No duplicated logic.

Reusable components.

Follow existing design system.

Use TanStack Query.

Separate service layer.

Separate types.

Follow existing project architecture.

---

# Out of Scope

Backend APIs

OpenAI

LLMs

Resume parsing

Real AI

Database

Authentication changes

---

# Acceptance Criteria

✓ Job Match Dashboard implemented

✓ Match Score displayed

✓ Skill Comparison completed

✓ Gap Analysis completed

✓ Recommendation Cards completed

✓ Compatibility Breakdown completed

✓ Apply Readiness implemented

✓ React Query integrated

✓ Mock service layer completed

✓ Responsive

✓ Dark Mode

✓ Build passes

✓ Lint passes

✓ No TypeScript errors

✓ Production-quality code

---

# Deliverables

- Complete AI Job Matching module
- Mock service layer
- Responsive UI
- Enterprise architecture
- Clean reusable components