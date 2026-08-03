# Admin Section Architecture

## Overview

The Admin Section provides a secure, role-restricted area for platform administrators to manage global settings, user permissions, security configurations, system health, and operational controls.

Only users with `ROLE_ADMIN` can access admin APIs and UI pages.

---

## Goals

- Centralized platform management for administrators
- Global settings control (LLM config, email, platform toggles)
- User management (view, disable, promote/demote roles)
- Security controls (API rate limits, session management, IP allowlists)
- System monitoring (agent health, mission stats, error rates)
- Audit trail for all admin actions

---

## Access Control

| Role | Access |
|------|--------|
| ROLE_USER | No access to admin APIs or pages |
| ROLE_ADMIN | Full access to all admin features |

Admin routes are protected at both:
1. **Backend**: Spring Security `@PreAuthorize("hasRole('ADMIN')")` on all admin controllers
2. **Frontend**: Route guard that redirects non-admin users

---

## Backend Package Structure

```
com.sourcekoza.careerpilot.admin/
├── controller/
│   ├── AdminUserController.java        — User management APIs
│   ├── AdminSettingsController.java     — Global settings APIs
│   ├── AdminSecurityController.java     — Security config APIs
│   ├── AdminDashboardController.java    — System stats/health APIs
│   └── AdminAuditController.java        — Audit log APIs
├── dto/
│   ├── AdminUserResponse.java
│   ├── AdminUserUpdateRequest.java
│   ├── GlobalSettingResponse.java
│   ├── GlobalSettingUpdateRequest.java
│   ├── SecurityConfigResponse.java
│   ├── SecurityConfigUpdateRequest.java
│   ├── SystemStatsResponse.java
│   └── AuditLogResponse.java
├── entity/
│   ├── GlobalSetting.java              — Key-value global config store
│   ├── AuditLog.java                   — Tracks all admin actions
│   └── SecurityConfig.java             — Rate limits, IP rules, etc.
├── repository/
│   ├── GlobalSettingRepository.java
│   ├── AuditLogRepository.java
│   └── SecurityConfigRepository.java
├── service/
│   ├── AdminUserService.java
│   ├── GlobalSettingService.java
│   ├── AdminSecurityService.java
│   ├── AdminDashboardService.java
│   └── AuditService.java
└── event/
    └── AdminActionEvent.java           — Published on every admin action
```

---

## Frontend Structure

```
frontend/src/
├── app/admin/
│   ├── layout.tsx                      — Admin layout with guard
│   ├── page.tsx                        — Admin dashboard (stats overview)
│   ├── users/page.tsx                  — User management
│   ├── settings/page.tsx               — Global settings
│   ├── security/page.tsx               — Security configuration
│   └── audit/page.tsx                  — Audit logs
├── features/admin/
│   ├── components/
│   │   ├── admin-sidebar.tsx
│   │   ├── admin-guard.tsx             — Role check wrapper
│   │   ├── user-table.tsx
│   │   ├── settings-form.tsx
│   │   ├── security-panel.tsx
│   │   ├── audit-log-table.tsx
│   │   └── system-stats.tsx
│   ├── hooks/
│   │   └── use-admin.ts
│   └── services/
│       └── admin.service.ts
```

---

## Database Tables

### global_settings
| Column | Type | Description |
|--------|------|-------------|
| id | UUID PK | |
| setting_key | VARCHAR(100) UNIQUE | e.g. `llm.model`, `email.enabled` |
| setting_value | TEXT | JSON or plain value |
| category | VARCHAR(50) | GROUP: `ai`, `email`, `security`, `platform` |
| description | VARCHAR(500) | Human-readable explanation |
| created_at | TIMESTAMP | |
| updated_at | TIMESTAMP | |

### audit_logs
| Column | Type | Description |
|--------|------|-------------|
| id | UUID PK | |
| admin_id | UUID FK (users) | Who performed the action |
| action | VARCHAR(100) | e.g. `USER_DISABLED`, `SETTING_CHANGED` |
| target_type | VARCHAR(50) | e.g. `USER`, `SETTING`, `SECURITY` |
| target_id | VARCHAR(200) | ID of affected resource |
| details | TEXT | JSON with before/after values |
| ip_address | VARCHAR(50) | Admin's IP |
| created_at | TIMESTAMP | |

### security_configs
| Column | Type | Description |
|--------|------|-------------|
| id | UUID PK | |
| config_key | VARCHAR(100) UNIQUE | e.g. `rate_limit.per_minute` |
| config_value | TEXT | |
| enabled | BOOLEAN | Toggle on/off |
| created_at | TIMESTAMP | |
| updated_at | TIMESTAMP | |

---

## REST APIs

All prefixed with `/api/v1/admin/` and require `ROLE_ADMIN`.

### User Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /admin/users | List all users (paginated) |
| GET | /admin/users/{id} | Get user details |
| PUT | /admin/users/{id}/role | Change user role |
| PUT | /admin/users/{id}/disable | Disable user account |
| PUT | /admin/users/{id}/enable | Re-enable user account |

### Global Settings
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /admin/settings | List all settings (grouped by category) |
| GET | /admin/settings/{key} | Get single setting |
| PUT | /admin/settings/{key} | Update setting value |
| POST | /admin/settings | Create new setting |

### Security
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /admin/security | Get all security configs |
| PUT | /admin/security/{key} | Update security config |
| GET | /admin/security/sessions | Active sessions |
| DELETE | /admin/security/sessions/{id} | Revoke a session |

### Dashboard / Stats
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /admin/dashboard/stats | System overview stats |
| GET | /admin/dashboard/agents | Agent health status |
| GET | /admin/dashboard/errors | Recent error summary |

### Audit
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /admin/audit | Audit logs (paginated, filterable) |

---

## Global Settings Categories

| Category | Example Keys |
|----------|-------------|
| ai | `ai.model`, `ai.temperature`, `ai.max_tokens`, `ai.provider` |
| email | `email.enabled`, `email.from`, `email.daily_limit` |
| platform | `platform.linkedin.enabled`, `platform.indeed.enabled` |
| security | `security.max_login_attempts`, `security.session_timeout_minutes` |
| mission | `mission.max_concurrent`, `mission.auto_apply_threshold` |

---

## Security Considerations

1. All admin endpoints protected by `ROLE_ADMIN` at method level
2. Every admin action logged to `audit_logs` with IP, user, before/after
3. Admin section accessible only via separate `/admin` route prefix
4. Rate limiting applied to admin APIs (prevent brute-force role escalation)
5. Admin password changes require current password confirmation

---

## Implementation Notes

- Follow existing patterns: BaseEntity, constructor injection, @Transactional
- Use Spring Security's `@PreAuthorize` annotation on controller methods
- Audit logging via Spring Events (publish `AdminActionEvent`, listener persists)
- Frontend admin guard checks user role from auth store before rendering
- Settings are cached in-memory with TTL, refreshed on update
