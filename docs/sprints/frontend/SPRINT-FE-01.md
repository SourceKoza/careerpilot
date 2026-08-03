# SPRINT-FE-01

# Frontend Foundation + Landing Page + Authentication

| Field    | Value    |
| -------- | -------- |
| Sprint   | FE-01    |
| Epic     | Frontend |
| Priority | Critical |
| Status   | Planned  |

---

# Goal

Build the production-ready frontend foundation for CareerPilot AI.

This sprint establishes the design system, application architecture, public website, and authentication flow.

The objective is to create a premium AI SaaS user experience that integrates with the existing Spring Boot backend.

---

# Vision

CareerPilot AI should feel like a modern AI startup.

The interface should be comparable in quality to products such as:

* Linear
* Vercel
* Stripe
* OpenAI
* Notion

Do not copy any existing product.

Create an original, premium experience inspired by modern SaaS design principles.

---

# Technology Stack

* Next.js 15 (App Router)
* React 19
* TypeScript
* Tailwind CSS v4
* shadcn/ui
* Lucide Icons
* Framer Motion
* React Hook Form
* Zod
* TanStack Query
* Axios
* Zustand
* next-themes

---

# Frontend Architecture

Follow feature-based architecture.

Suggested structure:

frontend/src/

* app/
* components/
* features/
* hooks/
* lib/
* services/
* stores/
* types/
* styles/

Do not organize by MVC.

---

# Theme

Primary palette:

* Black
* Deep Purple
* Violet
* Electric Blue accents

Style:

* Glassmorphism
* Soft gradients
* Premium cards
* Rounded corners
* Smooth animations
* Large whitespace
* Responsive layouts

Dark mode is the default theme.

---

# Public Pages

Implement:

* Home (/)
* Login
* Register
* Forgot Password
* Privacy Policy
* Terms & Conditions
* Contact

---

# Landing Page

Create a premium landing page containing:

## Hero

Headline:

"Your AI Career Copilot"

Sub-heading describing AI-powered job search, resume optimization, and automated applications.

Buttons:

* Start Free
* Watch Demo

Include animated background and premium hero illustration.

---

## Features

Feature cards:

* AI Job Search
* Resume Optimizer
* Auto Apply
* AI Agents
* Analytics Dashboard
* Interview Assistant

---

## How It Works

Three-step animated process.

---

## Why CareerPilot AI

Highlight platform advantages.

---

## Statistics

Animated counters.

---

## Testimonials

Animated carousel with avatars and ratings.

---

## FAQ

Accordion component.

---

## Call To Action

Encourage registration.

---

## Footer

Professional SaaS footer.

---

# Authentication

Create:

* Login
* Register
* Forgot Password

Features:

* React Hook Form
* Zod Validation
* JWT Authentication
* Loading states
* Error handling
* Success notifications
* Password visibility toggle
* Remember Me
* Social login placeholders (UI only)

---

# Backend Integration

Connect to existing backend endpoints:

POST /api/v1/auth/register

POST /api/v1/auth/login

Persist JWT securely.

Protect authenticated routes.

Redirect unauthenticated users to Login.

---

# Dashboard Foundation

After authentication create the dashboard shell only.

Include:

* Sidebar
* Top Navigation
* Breadcrumb
* User Menu
* Notification Icon
* Theme Toggle

Navigation items:

* Dashboard
* Jobs
* Resume
* Applications
* AI Agents
* Settings

All pages except Dashboard may remain placeholders.

---

# UI Quality

Requirements:

* Responsive
* Accessible
* WCAG friendly
* Keyboard navigation
* Smooth animations
* Optimized images
* Lazy loading
* Clean typography
* Production quality

---

# Performance

* Use Server Components where appropriate.
* Keep Client Components minimal.
* Optimize bundle size.
* Use dynamic imports where beneficial.

---

# Code Quality

Follow:

* SOLID
* Clean Architecture
* Reusable Components
* Feature-based organization
* Strict TypeScript
* No duplicate code

---

# Out of Scope

Do NOT implement:

* Job Management UI
* Resume Builder
* AI Agent Screens
* Job Search UI
* Auto Apply UI
* Charts
* Analytics

These belong to future frontend sprints.

---

# Acceptance Criteria

* Frontend foundation created.
* Landing page completed.
* Authentication integrated.
* Dashboard shell completed.
* JWT authentication working.
* Responsive across desktop, tablet, and mobile.
* Dark theme implemented.
* Build passes.

---

# Definition of Done

* Production-ready frontend foundation completed.
* Premium AI SaaS landing page completed.
* Authentication fully integrated.
* Ready for FE-02 Dashboard implementation.

---

# Next Sprint

Sprint-FE-02

Dashboard & Design System Expansion
