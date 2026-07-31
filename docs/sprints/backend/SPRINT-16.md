# Sprint-BE-16 — Resume Tailoring + Auto-Apply Pipeline

## Goal

Implement the AI-powered resume tailoring and automated job application pipeline. When a mission discovers jobs, the system automatically tailors the user's resume for each job using the LLM, generates a downloadable DOCX file, and sends application emails with the tailored resume attached.

Users can choose between Semi-Automatic (review before sending) and Full-Automatic (AI sends without approval) modes.

---

## Objective

Current Flow (Sprint 15)

Mission Started → Jobs Discovered → LLM Scores Jobs → Contacts Extracted → Done

Target Flow (Sprint 16)

Mission Started → Jobs Discovered → LLM Scores Jobs → Resume Tailored Per Job → Re-scored → Email Sent with DOCX Attached → Job Status: APPLIED

---

## Architecture

```
MissionOrchestrator
        ↓
JobSearchAgent (existing)
        ↓
ResumeTailoringAgent (NEW)
        ↓
AutoApplyPipeline (NEW)
        ↓
EmailOutreachAgent (enhanced)
        ↓
DocxGeneratorService (NEW)
```

---

## Apply Mode

Two modes available. Default: SEMI_AUTO.

### SEMI_AUTO (Default)

- AI discovers jobs, scores them, tailors resumes
- User sees each job with tailored resume PREVIEW
- User can: Approve & Send | Request Changes | Skip
- "Request Changes" → user types feedback → LLM regenerates
- Only sends email when user explicitly approves

### FULL_AUTO

- AI discovers jobs, scores them, tailors resumes
- Automatically sends emails for jobs scoring ≥ 80%
- No human approval needed
- User can review sent applications after the fact

User selects mode during mission creation AND can change it in settings.

Dashboard shows current mode prominently.

---

## Scoring Rules

| Score Range | Action |
|-------------|--------|
| ≥ 80% | Ready to apply (use master resume or minor tailoring) |
| 60-79% | LLM tailors resume → re-scores → if new score ≥ 75% → apply |
| < 60% | Ignored completely (too far from fit) |

---

## Resume Tailoring Agent

### Input
- User's master resume (from DB: skills, experience, education, summary)
- Job description
- Job title and company

### LLM Instructions
- ✅ Rewrite professional summary targeting the specific job
- ✅ Reorder skills (put matching ones first)
- ✅ Highlight relevant experience bullets
- ✅ Add target role keyword alignment
- ❌ NEVER add skills the user doesn't have
- ❌ NEVER fabricate experience or projects
- ❌ NEVER change education or certifications
- Only use REAL data from the user's stored resume

### Output
- Tailored resume content (structured JSON: summary, skills, experience, education)
- Stored in `tailored_resumes` table linked to the job
- New match score after tailoring

---

## DOCX Generation

Using Apache POI library:
- Professional formatting
- Sections: Name/Contact, Summary, Skills, Experience, Education, Projects
- Clean ATS-friendly layout
- Stored on disk: `uploads/tailored/{userId}/{jobId}.docx`
- ~50KB file size

---

## Semi-Auto Review UI

### Job Card (Applications page)

```
┌──────────────────────────────────────┐
│ Senior Java Engineer at Stripe  85%  │
│ Remote | $180K-$230K | GREENHOUSE    │
│                                      │
│ 📄 Tailored Resume Ready             │
│ ┌──────────────────────────────────┐ │
│ │ Summary: Experienced backend     │ │
│ │ engineer with 8 years building   │ │
│ │ distributed payment systems...   │ │
│ │                                  │ │
│ │ Top Skills: Java, Spring Boot,   │ │
│ │ Kafka, Redis, PostgreSQL         │ │
│ └──────────────────────────────────┘ │
│                                      │
│ [✅ Approve & Send] [✏️ Change] [❌] │
└──────────────────────────────────────┘
```

### Request Changes Flow

User clicks "Change" → text input appears:
- "What would you like to change?"
- e.g. "Add more about my Kafka experience" or "Make summary shorter"
- LLM regenerates with feedback
- Shows new preview
- Can iterate until satisfied

---

## Database

### New Table: tailored_resumes

| Column | Type |
|--------|------|
| id | UUID PK |
| mission_id | UUID FK |
| job_id | UUID FK (discovered_jobs) |
| user_id | UUID |
| summary | TEXT |
| skills_json | TEXT (ordered list) |
| experience_json | TEXT (highlighted bullets) |
| education_json | TEXT |
| tailored_score | INTEGER |
| original_score | INTEGER |
| status | ENUM: DRAFT, APPROVED, SENT, REJECTED |
| feedback | TEXT (user's change request) |
| file_path | VARCHAR (path to generated DOCX) |
| created_at | TIMESTAMP |
| updated_at | TIMESTAMP |

### Modified: missions table

Add column: `apply_mode VARCHAR(20) DEFAULT 'SEMI_AUTO'`

### Modified: discovered_jobs table

Add column: `tailored_resume_id UUID` (FK to tailored_resumes)

---

## REST APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /missions/{id}/jobs/{jobId}/tailored-resume | Get tailored resume preview |
| POST | /missions/{id}/jobs/{jobId}/tailor | Trigger resume tailoring for a job |
| POST | /missions/{id}/jobs/{jobId}/approve | Approve & send email with resume |
| POST | /missions/{id}/jobs/{jobId}/regenerate | Regenerate with user feedback |
| POST | /missions/{id}/jobs/{jobId}/skip | Skip this job |
| GET | /missions/{id}/jobs/{jobId}/resume-download | Download tailored DOCX |
| PUT | /missions/{id}/apply-mode | Change apply mode |

---

## Email Enhancement

EmailOutreachAgent (enhanced from Sprint 15):
- Now attaches DOCX file to the email
- LLM generates personalized email body referencing the tailored resume
- Includes: why candidate is a good fit, 2-3 matching skills, call to action
- Subject line personalized per job

---

## Frontend Components

1. Mode toggle in Mission Wizard (Step 4)
2. Mode indicator in Dashboard header
3. Job card with tailored resume preview section
4. Approve/Change/Skip action buttons
5. Change request text input + regenerate
6. Download DOCX button
7. Status badges: DRAFT → APPROVED → SENT

---

## Packages

```
com.sourcekoza.careerpilot.jobagent.agents.tailoring/
    ResumeTailoringAgent.java
    TailoredResumeContent.java

com.sourcekoza.careerpilot.jobagent.agents.email/
    EmailOutreachAgent.java (enhanced)
    EmailService.java (enhanced - attachments)

com.sourcekoza.careerpilot.jobagent.mission.entity/
    TailoredResume.java
    ApplyMode.java

com.sourcekoza.careerpilot.jobagent.mission.service/
    ResumeTailoringService.java
    DocxGeneratorService.java
    AutoApplyPipeline.java
```

---

## Dependencies

- Apache POI (DOCX generation): `org.apache.poi:poi-ooxml:5.2.5`
- Spring Boot Mail (already added in Sprint 15)

---

## Unit Tests

- ResumeTailoringAgent (LLM generates valid content, respects constraints)
- DocxGeneratorService (generates valid DOCX file)
- AutoApplyPipeline (orchestrates scoring → tailoring → sending)
- EmailOutreachAgent (sends with attachment)
- API endpoints (tailored resume CRUD)

---

## Out of Scope

- PDF generation (DOCX only for performance)
- Rich text editor in frontend (simple preview + feedback input)
- Multiple resume templates/themes
- Cover letter generation (separate future sprint)

---

## Definition of Done

✓ ResumeTailoringAgent generates tailored content using LLM
✓ LLM ONLY uses real user data (no fabrication)
✓ DOCX file generated with professional formatting
✓ Semi-auto: User can preview, approve, request changes, skip
✓ Full-auto: Emails sent automatically for high-score jobs
✓ Mode toggle available in mission wizard + dashboard
✓ Email sent with DOCX attached
✓ Job status updated to APPLIED after sending
✓ Tailored resume stored in DB with scores
✓ User feedback → LLM regenerates
✓ All APIs working
✓ Frontend review UI functional
✓ Build passes (mvn clean package)
✓ Unit tests pass
