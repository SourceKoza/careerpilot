# SPRINT-10

# Business MCP Tools

| Field    | Value               |
| -------- | ------------------- |
| Sprint   | 10                  |
| Epic     | EPIC-03 AI Platform |
| Priority | Critical            |
| Status   | Planned             |

---

# Goal

Expose the existing business modules through MCP Tools.

This sprint transforms CareerPilot AI from a simple MCP-enabled application into an AI-ready platform.

The MCP Server already exists.

Now expose business operations as discoverable tools.

No AI agents or orchestration will be implemented.

---

# Why This Sprint Exists

The REST API is designed for frontend applications.

MCP Tools are designed for AI agents.

Both should reuse the same business services.

Architecture:

AI Agent

↓

MCP Tool

↓

Spring Service

↓

Repository

REST Controller

↓

Spring Service

↓

Repository

Controllers and MCP Tools are simply different entry points.

Business logic must remain inside the Service layer.

---

# Tools to Implement

## Resume MCP Tool

Expose resume operations.

Suggested tools:

* createResume
* getResume
* listResumes
* createResumeVersion
* listResumeVersions

Reuse ResumeService.

---

## Job MCP Tool

Expose job operations.

Suggested tools:

* createJob
* getJob
* listJobs

Reuse JobService.

---

## Job Application MCP Tool

Expose application operations.

Suggested tools:

* createApplication
* getApplication
* listApplications
* updateApplicationStatus

Reuse JobApplicationService.

---

# Tool Design Rules

Every MCP Tool:

* Calls existing services.
* Performs no business logic.
* Does not access repositories.
* Returns clean DTOs.
* Uses existing validation.

MCP Tools are adapters only.

---

# Package Structure

Suggested package:

mcp/tools/

Examples:

* ResumeTool
* JobTool
* JobApplicationTool

Do not duplicate business code.

---

# Error Handling

Reuse existing exception handling strategy.

Return meaningful MCP errors.

Do not expose stack traces.

---

# Logging

Log:

* Tool execution
* Execution duration
* Tool failures

Do not log sensitive user information.

---

# Security

Use existing authentication and authorization where applicable.

Do not bypass service-layer security.

Future agent authentication will be added later.

---

# Performance

Avoid N+1 queries.

Reuse pagination.

Return lightweight DTOs.

Avoid returning unnecessary entity graphs.

---

# Out of Scope

Do NOT implement:

* AI Orchestrator
* LangGraph
* CrewAI
* Ollama
* OpenAI
* Claude
* Prompt generation
* Resume tailoring
* Job search automation
* Email automation
* RAG
* Vector database

---

# Acceptance Criteria

* Resume tools available.
* Job tools available.
* Job Application tools available.
* Tools appear in MCP discovery.
* All tools execute successfully.
* Existing REST APIs remain unchanged.
* No duplicated business logic.
* Build passes.

---

# Definition of Done

* Business services exposed through MCP.
* Clean architecture maintained.
* No repository access from MCP.
* Existing engineering standards followed.
* mvn clean verify passes.
* Ready for Sprint-11.

---

# Future Sprint

Sprint-11 introduces the AI Orchestrator that will consume these MCP tools instead of calling business services directly.
