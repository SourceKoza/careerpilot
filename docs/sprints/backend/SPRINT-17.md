# Sprint-BE-17 — Admin Section

## Goal

Implement the admin section with global settings management, user management, security configuration, system dashboard, and audit logging. Only users with `ROLE_ADMIN` can access these features.

---

## Objective

Provide administrators with a centralized control panel to:
- Manage users (view, disable, change roles)
- Configure global platform settings (AI model, email, platform toggles)
- View system health and agent statistics
- Review audit trail of all admin actions
- Manage security settings (rate limits, session controls)

---

## Architecture

See: `docs/architecture/10-ADMIN_ARCHITECTURE.md`

---

## Package

```
com.sourcekoza.careerpilot.admin/
├── controller/     — REST APIs (all @PreAuthorize ROLE_ADMIN)
├── dto/            — Request/Response records
├── entity/         — GlobalSetting, AuditLog, SecurityConfig
├── repository/     — JPA repositories
├── service/        — Business logic
└── event/          — AdminActionEvent for audit logging
```

---

## Entities

### GlobalSetting

```java
@Entity
@Table(name = "global_settings")
public class GlobalSetting extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String settingKey;
    private String settingValue;
    private String category;       // ai, email, platform, security, mission
    private String description;
}
```

### AuditLog

```java
@Entity
@Table(name = "audit_logs")
public class AuditLog extends BaseEntity {
    private UUID adminId;
    private String action;         // USER_DISABLED, SETTING_CHANGED, etc.
    private String targetType;     // USER, SETTING, SECURITY
    private String targetId;
    @Column(columnDefinition = "TEXT")
    private String details;        // JSON: {before: ..., after: ...}
    private String ipAddress;
}
```

### SecurityConfig

```java
@Entity
@Table(name = "security_configs")
public class SecurityConfig extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String configKey;
    private String configValue;
    private boolean enabled;
}
```

---

## REST APIs

All under `/api/v1/admin/` with `@PreAuthorize("hasRole('ADMIN')")`.

### Users
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /admin/users?page=0&size=20 | List users |
| GET | /admin/users/{id} | User details |
| PUT | /admin/users/{id}/role | Change role (body: {role: "ROLE_ADMIN"}) |
| PUT | /admin/users/{id}/disable | Disable account |
| PUT | /admin/users/{id}/enable | Enable account |

### Settings
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /admin/settings | All settings grouped by category |
| GET | /admin/settings/{key} | Single setting |
| PUT | /admin/settings/{key} | Update (body: {value: "..."}) |
| POST | /admin/settings | Create (body: {key, value, category, description}) |

### Security
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /admin/security | All security configs |
| PUT | /admin/security/{key} | Update config |

### Dashboard
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /admin/dashboard/stats | {totalUsers, totalMissions, totalJobs, activeAgents} |

### Audit
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /admin/audit?page=0&size=50 | Paginated audit logs |

---

## Audit Logging

Every admin action publishes an `AdminActionEvent`:

```java
public record AdminActionEvent(
    UUID adminId,
    String action,
    String targetType,
    String targetId,
    String details,
    String ipAddress
) {}
```

An `@EventListener` persists this to `audit_logs` table asynchronously.

---

## Default Settings (seeded on first run)

| Key | Value | Category |
|-----|-------|----------|
| ai.model | qwen2.5:7b | ai |
| ai.temperature | 0.3 | ai |
| ai.provider | ollama | ai |
| email.enabled | false | email |
| email.from | careerpilot@sourcekoza.com | email |
| email.daily_limit | 50 | email |
| platform.linkedin.enabled | true | platform |
| platform.indeed.enabled | true | platform |
| platform.greenhouse.enabled | true | platform |
| security.max_login_attempts | 5 | security |
| security.session_timeout_minutes | 1440 | security |
| mission.max_concurrent | 3 | mission |
| mission.auto_apply_threshold | 80 | mission |

---

## User Role Changes

Current `Role` enum:
```java
public enum Role {
    ROLE_USER,
    ROLE_ADMIN
}
```

Admin can promote users to ROLE_ADMIN or demote to ROLE_USER.

---

## Frontend Pages

1. `/admin` — Dashboard with system stats
2. `/admin/users` — User table with role/status management
3. `/admin/settings` — Settings editor grouped by category
4. `/admin/security` — Security toggles and configs
5. `/admin/audit` — Searchable audit log table

---

## Definition of Done

✓ GlobalSetting, AuditLog, SecurityConfig entities created
✓ Admin REST APIs implemented and secured with @PreAuthorize
✓ Every admin action logged to audit_logs
✓ Default settings seeded on application startup
✓ User management: list, view, disable, enable, role change
✓ Frontend: admin layout, guard, dashboard, user management
✓ Frontend: settings editor, audit log viewer
✓ Only ROLE_ADMIN users can access /admin/* routes
✓ Build passes (mvn clean package)
✓ Frontend: npx tsc --noEmit passes
