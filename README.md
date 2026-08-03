# CareerPilot AI

AI-powered Job Search and Application Automation Platform.

## Tech Stack

- **Backend:** Spring Boot 4.1, Java 21, Maven
- **Database:** PostgreSQL 16
- **Cache:** Redis 7
- **Object Storage:** MinIO
- **LLM:** Ollama (local inference)
- **Browser Automation:** Playwright

---

## Development Setup (Docker)

The project is fully dockerized for local development with **hot reload** — no image rebuild required when you change code.

### Prerequisites

- [Docker](https://docs.docker.com/get-docker/) & Docker Compose v2+
- (Optional) Copy `.env.example` to `.env` and customize values

### First Time Setup

```bash
# Build the dev image and start all services
docker compose up --build
```

This will:
1. Build the Spring Boot dev container with Maven + Java 21
2. Download all Maven dependencies (cached in a named volume)
3. Start PostgreSQL, Redis, MinIO, and Ollama
4. Start the Spring Boot application with DevTools hot reload

### Daily Development Workflow

```bash
# Start all services (detached)
docker compose up -d

# View application logs
docker compose logs -f app

# Stop all services
docker compose down

# Stop and remove volumes (full reset)
docker compose down -v
```

### Hot Reload

Once `docker compose up -d` is running:

1. Edit any Java source file in `backend/src/`
2. Spring Boot DevTools automatically detects the change
3. The application restarts inside the container (~2-5 seconds)
4. **No `docker build` or `docker compose restart` needed**

DevTools monitors:
- `src/main/java/**` — Java source changes
- `src/main/resources/**` — Configuration and resource file changes

### Service Ports

| Service      | Port  | URL                              |
|-------------|-------|----------------------------------|
| Spring Boot | 8080  | http://localhost:8080             |
| Swagger UI  | 8080  | http://localhost:8080/swagger-ui.html |
| PostgreSQL  | 5433  | `localhost:5433`                 |
| Redis       | 6379  | `localhost:6379`                 |
| MinIO API   | 9000  | http://localhost:9000            |
| MinIO Console | 9001 | http://localhost:9001           |
| Ollama      | 11434 | http://localhost:11434           |

### Environment Variables

Default values are provided in `docker-compose.yml`. Override them by creating a `.env` file in the project root (see `.env.example`).

### Troubleshooting

**App won't start (dependency resolution):**
```bash
# Clear the Maven cache volume and rebuild
docker compose down
docker volume rm careerpilot-maven-cache
docker compose up --build
```

**PostgreSQL connection refused:**
```bash
# Check if postgres is healthy
docker compose ps
docker compose logs postgres
```

**Hot reload not working:**
- Ensure you're editing files in `backend/src/` (mounted into the container)
- Check that DevTools is active in logs: look for `Restarting` messages
- Verify file is saved (DevTools uses polling with 2s interval)

---

## Running Without Docker

If you prefer running the backend natively:

```bash
cd backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Requires a local PostgreSQL instance on port 5433 (see `application-local.yml`).

---

## License

See [LICENSE](LICENSE) for details.
