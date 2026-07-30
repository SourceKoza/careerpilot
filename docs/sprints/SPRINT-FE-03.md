# Sprint FE-03

# Resume Management Module

| Field | Value |
|--------|--------|
| Sprint | FE-03 |
| Epic | Frontend |
| Priority | High |
| Status | Planned |

---

# Goal

Build a professional Resume Management module that allows users to upload, organize, preview, download, and manage resumes.

This sprint focuses on user experience and backend integration. AI resume optimization is out of scope.

---

# Backend Integration

Integrate with the Resume APIs created by the backend.

Support:

- Upload Resume
- List Resumes
- View Resume Details
- Download Resume
- Delete Resume

If APIs are not yet available, use mock services with a clear abstraction layer so they can be replaced later.

---

# Resume Dashboard

Display:

- Total resumes
- Default resume
- Last updated
- Upload count

---

# Resume List

Responsive table/grid.

Each card shows:

- Resume Name
- Version
- File Type
- File Size
- Upload Date
- Status

Actions:

- Preview
- Download
- Rename
- Delete
- Set Default

---

# Upload Resume

Support:

- Drag & Drop
- Click Upload
- Progress Bar
- Upload Validation
- Error Handling

Supported files:

- PDF
- DOCX

Maximum size configurable.

---

# Resume Preview

Preview metadata and document information.

If PDF preview is available, display inline.

Otherwise show file information and download option.

---

# Empty State

Beautiful illustration.

Message:

"No resumes uploaded yet."

Button:

Upload Resume

---

# Resume Actions

Allow:

- Upload
- Download
- Delete
- Rename
- Set Default

Confirm destructive actions.

---

# Search

Search resumes by name.

---

# Filters

Filter by:

- File Type
- Upload Date

---

# Sorting

Support sorting by:

- Name
- Date
- Size

---

# Components

Create reusable components:

ResumeCard

ResumeTable

ResumeUploader

ResumePreview

UploadProgress

DeleteDialog

EmptyState

---

# Loading

Skeleton loaders.

Progress indicators.

---

# Accessibility

Keyboard support.

Screen readers.

Proper ARIA labels.

---

# Responsive

Desktop

Tablet

Mobile

---

# Code Quality

Feature-based architecture.

Reusable components.

Strict TypeScript.

---

# Out of Scope

AI Resume Optimization

Resume Scoring

Resume Parsing

Cover Letter Generation

Version Diff

---

# Acceptance Criteria

✓ Resume upload

✓ Resume download

✓ Resume delete

✓ Resume list

✓ Resume preview

✓ Responsive UI

✓ Backend integration (or service abstraction)

✓ Build passes

---

# Next Sprint

Sprint FE-04

Job Search Experience