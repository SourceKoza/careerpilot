# ADR-006: Internal Agent Communication

| Field    | Value                         |
| -------- | ----------------------------- |
| Status   | Accepted                      |
| Date     | 2026-07-30                    |
| Sprint   | Sprint-13                     |
| Decision | Internal AI Agent integration |

---

## Context

CareerPilot AI uses a multi-agent architecture where internal AI Agents perform automated workflows such as job searching, resume tailoring, and application submission. These agents need to communicate with the application's business logic layer.

The system also exposes MCP (Model Context Protocol) for external AI integrations and REST APIs for frontend and external HTTP clients.

A clear separation is needed to avoid architectural confusion between internal and external communication paths.

---

## Decision

Internal AI Agents communicate directly with **Application Services**.

### Internal AI Agents MUST NOT:

- Call MCP Tools
- Call REST APIs
- Call Controllers
- Access Repositories directly

### Communication Boundaries:

| Consumer          | Integration Point     |
| ----------------- | --------------------- |
| Frontend / HTTP   | REST Controllers      |
| External AI / LLM | MCP Tools             |
| Internal AI Agent | Application Services  |

### Architecture Diagram:

```text
Frontend
        │
 REST Controllers
        │
        ▼
Application Services  ◄──── Internal AI Agents
        │
        ▼
  Repositories / Infrastructure
```

---

## Rationale

1. **Single Responsibility**: Controllers handle HTTP concerns (validation, serialisation, authentication). Agents should not go through these layers unnecessarily.

2. **Performance**: Direct service calls avoid HTTP overhead, serialisation/deserialisation, and network latency.

3. **Testability**: Agent-to-service communication is easier to unit test without mocking HTTP layers.

4. **Security Boundary**: REST and MCP endpoints enforce external security (JWT, rate limiting). Internal agents operate within the trusted application boundary.

5. **Consistency**: A single entry point (Application Services) for business logic ensures all consumers get the same behaviour and validation.

6. **Replaceability**: Implementation details (Playwright, LLM providers) can be swapped without affecting agents, since agents only depend on service interfaces.

---

## Alternatives Considered

### 1. Agents call REST APIs internally

- Rejected: Adds unnecessary HTTP overhead and coupling to transport concerns.
- Agents would need to handle HTTP authentication, serialisation, and error codes.

### 2. Agents call MCP Tools

- Rejected: MCP is designed for external AI integration with its own protocol overhead.
- Internal agents are already within the application boundary.

### 3. Agents access Repositories directly

- Rejected: Bypasses business logic, validation, and domain rules.
- Creates tight coupling between agents and persistence layer.

---

## Consequences

### Positive

- Clean separation between internal and external communication paths.
- Agents are simpler — they only depend on service interfaces.
- Business logic is centralised in Application Services.
- Easy to test agents in isolation.

### Negative

- Agents and services must be in the same deployment unit (monolith).
- If the system moves to microservices, internal communication would need to change.

---

## Compliance

All future AI Agent implementations must follow this decision. Code reviews should verify that agents do not call Controllers, REST APIs, MCP Tools, or Repositories directly.
