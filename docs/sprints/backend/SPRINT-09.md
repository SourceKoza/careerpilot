# SPRINT-09

# MCP Server Foundation

| Field    | Value               |
| -------- | ------------------- |
| Sprint   | 09                  |
| Epic     | EPIC-03 AI Platform |
| Priority | Critical            |
| Status   | Planned             |

---

# Goal

Introduce the Model Context Protocol (MCP) Server into CareerPilot AI.

This sprint establishes the communication layer between AI agents and the backend business services.

No AI workflows or autonomous agents will be implemented.

The objective is only to build the MCP Server infrastructure.

---

# Why This Sprint Exists

CareerPilot AI will eventually contain multiple AI agents.

Instead of allowing agents to directly call Spring services or repositories, every AI interaction will occur through MCP tools.

Benefits:

* Standardized communication
* Better security
* Controlled access
* Tool discoverability
* Easier testing
* Future compatibility with different LLMs

---

# Business Requirements

The application must expose an MCP Server.

The server must start successfully.

The server must register MCP tools.

The server must integrate with the existing Spring Boot application.

Business logic should remain inside existing services.

MCP tools act only as adapters.

---

# Architecture

AI Agent

↓

MCP Client

↓

MCP Server

↓

MCP Tool

↓

Spring Service

↓

Repository

Never allow MCP tools to access repositories directly.

---

# Initial MCP Tools

Create simple proof-of-concept tools.

## Health Tool

Returns application status.

Example:

* status
* version
* timestamp

---

## Greeting Tool

Simple tool used only to verify MCP communication.

Example:

* greeting message
* application name

---

These tools exist only to validate the infrastructure.

---

# Package Structure

Suggested package:

mcp/

Sub-packages:

* config
* tools
* model

Keep MCP isolated from business modules.

---

# Configuration

Configure MCP Server.

Register all tools using the project's preferred Spring AI MCP approach.

Externalize configuration.

---

# Logging

Log:

* MCP Server startup
* Tool registration
* Incoming tool requests
* Tool execution success/failure

Do not log sensitive data.

---

# Security

Do not expose repositories.

Do not bypass services.

Future authentication will be added later.

---

# Out of Scope

Do NOT implement:

* AI Agents
* LLM integration
* Ollama calls
* OpenAI
* Claude
* LangGraph
* CrewAI
* Resume generation
* Job search
* Auto apply
* Memory
* Vector database
* RAG

---

# Acceptance Criteria

* MCP Server starts successfully.
* Tools are discoverable.
* Greeting Tool works.
* Health Tool works.
* Existing application remains functional.
* Build passes.
* No business logic duplicated.

---

# Definition of Done

* MCP Server integrated.
* Clean package structure.
* Reuses existing engineering standards.
* mvn clean verify passes.
* Ready for Sprint-10.

---

# Future Sprints

Sprint-10

Business MCP Tools

* Resume Tool
* Job Tool
* Application Tool

Sprint-11

AI Orchestrator

Sprint-12+

AI Agents begin consuming MCP tools.
