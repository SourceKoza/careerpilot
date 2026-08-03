# Sprint FE-04

# AI Search Missions

| Field | Value |
|--------|--------|
| Sprint | FE-04 |
| Epic | AI Automation |
| Priority | Critical |
| Status | Planned |

---

# Goal

Build the AI Search Mission module.

Users do **NOT** manually search jobs.

Instead, they create an AI Search Mission.

The AI searches multiple job platforms, tracks progress, discovers opportunities, and presents the results.

CareerPilot AI is an AI Employee, not a job portal.

---

# Product Vision

Traditional Job Portal

User → Search → Browse → Apply

CareerPilot AI

User → Create Mission → AI Searches → AI Filters → AI Applies → User Monitors

---

# Mission Dashboard

Display all AI Search Missions.

Each Mission Card should show:

- Mission Name
- Current Status
- Created Date
- Last Run
- Next Scheduled Run
- Platforms Enabled
- Jobs Found
- Applications Submitted
- Success Rate

Actions:

- View
- Pause
- Resume
- Run Now
- Duplicate
- Delete

---

# Create New Mission

Beautiful multi-step wizard.

## Step 1

Mission Details

- Mission Name
- Keywords
- Preferred Job Title
- Experience Level

---

## Step 2

Location

- Country
- City
- Remote Only
- Hybrid
- Onsite

---

## Step 3

Salary

- Minimum Salary
- Currency
- Employment Type

---

## Step 4

Platforms

Allow selecting:

- LinkedIn
- Indeed
- Wellfound
- Naukri
- Company Career Pages

Display platform icons.

---

## Step 5

Resume

Choose uploaded resume.

Default resume pre-selected.

---

## Step 6

Schedule

- Run Once
- Daily
- Weekly
- Every Morning

Timezone selection.

---

## Step 7

Review

Display complete mission summary.

Start Mission button.

---

# Mission Details

Display:

Mission Status

Current Stage

Execution Time

Platforms

Jobs Found

Applications Submitted

Skipped Jobs

Errors

Timeline

---

# Live Mission Progress

Beautiful progress screen.

Example:

LinkedIn

████████████

52 Jobs Found

Indeed

██████████

18 Jobs Found

Wellfound

████████

7 Jobs Found

Company Sites

██████████████

41 Jobs Found

Overall Progress

74%

Current Activity

"Analyzing LinkedIn Results..."

---

# Mission Timeline

Timeline items:

Mission Created

Mission Started

Searching LinkedIn

Searching Indeed

Resume Tailoring

Applications Submitted

Mission Completed

---

# Mission Statistics

Cards

Total Jobs Found

Applications Submitted

Jobs Skipped

Platform Coverage

Duration

---

# Empty State

Illustration

"No AI Search Missions Yet"

Button

Create First Mission

---

# Components

MissionCard

MissionWizard

MissionTimeline

MissionProgress

PlatformSelector

ScheduleSelector

MissionStatistics

MissionStatusBadge

MissionSummary

EmptyState

LoadingSkeleton

---

# Backend Integration

Integrate with Mission APIs if available.

Otherwise create MissionService with mock implementations.

Components must communicate only through MissionService.

---

# UI Theme

Continue using:

- Black
- Deep Purple
- Violet
- Glassmorphism
- Premium AI SaaS

Animations:

- Framer Motion
- Smooth transitions
- Progress animations
- Card hover effects

---

# Accessibility

Keyboard navigation

ARIA labels

Screen reader support

---

# Responsive

Desktop

Tablet

Mobile

---

# Code Quality

Feature-based architecture

Reusable components

Strict TypeScript

SOLID

Clean Architecture

No duplicate code

---

# Out of Scope

Discovered Jobs

Application Tracking

Resume Tailoring

Auto Apply Configuration

Email Notifications

AI Chat

These belong to future sprints.

---

# Acceptance Criteria

✓ Create AI Mission

✓ Mission Wizard

✓ Mission Dashboard

✓ Mission Details

✓ Live Progress UI

✓ Mission Timeline

✓ Mission Statistics

✓ Responsive

✓ Backend integration or mock service

✓ Build passes

---

# Definition of Done

Users can create and manage AI Search Missions through a premium SaaS interface.

The UI clearly communicates that CareerPilot AI performs job searching automatically on behalf of the user.

---

# Next Sprint

Sprint FE-05

Discovered Jobs

The AI has completed the search.

Users review the jobs discovered by the AI instead of manually searching.