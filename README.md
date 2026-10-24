# TaskMaster API

TaskMaster is a stateless Spring Boot 3 backend for collaborative task tracking. It provides JWT authentication, team-scoped task management, comments, attachment metadata, WebSocket notifications, and an optional GenAI task-description endpoint.

## Requirements

- Java 17 or newer
- Maven 3.9+
- MySQL 8+

## Running locally

1. Create a database user and database (the JDBC URL creates the database when permitted). Choose a strong local password and use the same value for `DB_PASSWORD`:

   ```sql
   CREATE USER 'taskmaster'@'localhost' IDENTIFIED BY '<your-local-database-password>';
   GRANT ALL PRIVILEGES ON taskmaster.* TO 'taskmaster'@'localhost';
   ```

2. Set environment variables as needed:

   | Variable | Required | Purpose |
   |---|---|---|
   | `DB_URL` | Yes | MySQL JDBC URL |
   | `DB_USERNAME` | Yes | Database user |
   | `DB_PASSWORD` | Yes | Database password |
   | `JWT_SECRET` | Yes | At least 32 random characters |
   | `JWT_EXPIRATION_MS` | No | Token lifetime; defaults to 24 hours |
   | `GENAI_BASE_URL` | No | OpenAI-compatible `/chat/completions` URL |
   | `GENAI_API_KEY` | No | GenAI provider bearer token |
   | `GENAI_MODEL` | No | Provider model name; defaults to `gpt-4o-mini` |

3. Start the service:

   ```bash
   mvn spring-boot:run
   ```

The API is available at `http://localhost:8080`. Build a deployable jar with `mvn clean package`. The application intentionally fails fast when `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, or `JWT_SECRET` is missing.

## API

All endpoints except registration, login, and the WebSocket handshake require:

```text
Authorization: Bearer <JWT>
```

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register and receive a token |
| POST | `/api/auth/login` | Authenticate and receive a token |
| GET | `/api/auth/me` | Read the current profile |
| PUT | `/api/auth/me` | Update the current display name |

### Teams

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/teams` | Create a team; creator becomes owner and member |
| GET | `/api/teams` | List teams for the current user |
| POST | `/api/teams/{id}/members?email=` | Owner invites an existing user |

### Tasks

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/tasks` | Create a task in a team |
| GET | `/api/tasks?teamId=` | Search and page tasks |
| PUT | `/api/tasks/{id}` | Update title, description, status, due date, or assignee |
| DELETE | `/api/tasks/{id}` | Delete a task |
| POST | `/api/tasks/{id}/comments` | Add a comment |
| POST | `/api/tasks/{id}/attachments` | Store attachment metadata |

Task search accepts `search`, `status`, `assigneeId`, `page`, `size`, and `sort` (`title`, `dueDate`, `createdAt`, `updatedAt`, or `status`). Every task operation verifies team membership; assignees must already belong to the team.

### GenAI

`POST /api/genai/task-description` with `{"prompt":"..."}` generates a concise actionable description. If `GENAI_BASE_URL` is unset, a deterministic local description is returned, so development does not require an external provider. When configured, the service calls an OpenAI-compatible endpoint.

### WebSocket

Connect a STOMP client to `/ws`, subscribe to `/topic/teams/{teamId}/tasks`, and receive task create/update payloads. Send to `/app/teams/{teamId}/subscribe` to publish a presence event to `/topic/teams/{teamId}/presence`.

## Architecture

The application follows a layered design:

- `entity`: JPA persistence model, relationships, indexes, and lifecycle timestamps.
- `repository`: Spring Data repositories and a parameterized task search query.
- `service`: transaction/business boundaries, authorization, mapping, notifications, and GenAI integration.
- `controller`: REST and STOMP endpoints with Bean Validation.
- `security`: BCrypt authentication, JWT creation/parsing, and a stateless request filter.
- `exception`: consistent JSON errors through `@RestControllerAdvice`.

For production, replace `ddl-auto: update` with a migration strategy, use a secrets manager for credentials, restrict WebSocket origins, and place uploaded files in object storage. This service intentionally stores attachment metadata and URLs rather than binary content.
