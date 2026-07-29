# SPRINT-03

# Authentication Foundation

| Field    | Value              |
| -------- | ------------------ |
| Sprint   | 03                 |
| Epic     | EPIC-01 Foundation |
| Priority | High               |
| Status   | Planned            |

---

# Goal

Build a production-ready authentication and authorization foundation for CareerPilot AI using Spring Security and JWT.

This sprint establishes the security infrastructure that future modules will use.

---

# Business Value

Provide secure authentication so users can register, log in, and access protected resources.

This foundation will be reused by all future modules.

---

# Functional Requirements

Implement:

* User registration
* User login
* JWT generation
* JWT validation
* Password encryption using BCrypt
* Role-based authorization foundation
* Current authenticated user endpoint (`/api/v1/users/me`)

---

# Technical Requirements

Technology:

* Java 21
* Spring Boot 3.x
* Spring Security 6
* JWT
* BCrypt
* PostgreSQL
* Bean Validation

Architecture:

* Clean Architecture
* SOLID Principles
* Constructor Injection only

---

# Deliverables

## Domain

* User Entity
* Role Enum

---

## DTO

* RegisterRequest
* LoginRequest
* AuthResponse
* UserResponse

---

## Repository

* UserRepository

---

## Service

* AuthenticationService
* JwtService
* UserService

---

## Security

* SecurityConfig
* JwtAuthenticationFilter
* JwtAuthenticationEntryPoint
* JwtAuthenticationProvider

---

## Controller

Authentication endpoints:

POST /api/v1/auth/register

POST /api/v1/auth/login

GET /api/v1/users/me

---

# Validation

Validate:

* Email format
* Password length
* Required fields

Passwords must never be returned in API responses.

---

# Security Rules

Public Endpoints:

* /api/v1/auth/register
* /api/v1/auth/login
* /actuator/health
* Swagger/OpenAPI

Protected Endpoints:

Everything else.

---

# Acceptance Criteria

* User can register.
* User can log in.
* Password is encrypted using BCrypt.
* JWT is generated after successful login.
* Protected endpoints require authentication.
* Invalid JWT returns HTTP 401.
* Validation errors return HTTP 400.
* Application starts successfully.

---

# Out of Scope

Do NOT implement:

* OAuth2
* Google Login
* GitHub Login
* Refresh Tokens
* Email Verification
* Password Reset
* MFA
* Social Login
* AI Features
* MCP
* Business Modules

---

# Definition of Done

* Project builds successfully.
* Authentication works.
* JWT validation works.
* Passwords are encrypted.
* Security configuration is production-ready.
* No build errors.
* No unrelated functionality added.

---

# Kiro Instructions

Implement **only Sprint-03**.

Rules:

* Do not implement future security features.
* Do not redesign the project architecture.
* Use constructor injection only.
* Separate Entity, DTO, Service, Repository, and Controller layers.
* Never expose password fields.
* Follow Clean Architecture.
* Follow existing project structure.
* If any requirement is unclear, stop and report it instead of making assumptions.
