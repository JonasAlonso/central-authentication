# 🛡 Central Authentication Service

A Spring Boot 3.5.3 application using Spring Authorization Server 1.5.1 to act as a **central authentication gateway** for multiple backend services. It supports secure login, role-based access control, JWT issuance, OAuth2 client registration, and is production-ready for modern Spring ecosystems.

---

## 🚀 Tech Stack

- Java 21
- Spring Boot 3.5.3
- Spring Authorization Server 1.5.1
- Spring Security
- Spring Data JPA
- PostgreSQL
- Liquibase
- WebFlux (used for non-reactive WebClient)
- Docker / OH YES!

---

## 📚 Features

- ✅ Spring Authorization Server setup
- ✅ OAuth2 DB schema via Liquibase (`oauth2_registered_client`, etc.)
- ✅ Secure login with Thymeleaf (form-based)
- ✅ Role-based security using `@PreAuthorize`
- ✅ Self-service user registration
- ✅ Custom `JwtAuthenticationConverter` to extract authorities
- ✅ JWT issuance with custom claims  - on its way
- ✅ Admin endpoints for OAuth2 client CRUD (`client-admin.html`)
- ✅ Integration with protected backend services - kinda
- ✅ Token refresh flow
- ✅ Controller logging via `ControllerLoggingAspect`
- ✅ Global exception handling via `GlobalExceptionHandler`

---

## 🛠 Infrastructure

Infrastructure setup is powered by a flexible and reusable `Makefile` system, enabling quick local spin-ups and project templating. This design enables:

- Dynamic PostgreSQL container bootstrapping
- Consistent network and volume naming via environment variables
- Seamless init script execution for schema provisioning
- Reusability across services with minimal configuration

### 📦 Example: DB Bootstrap Task

```makefile
db-bootstrap: 
	docker volume create $(VOLUME)
	docker network create $(NETWORK) || true
	docker run -d \
		--name $(DB_CONTAINER) \
		--restart unless-stopped \
		--health-cmd="pg_isready -U $(DB)" \
		--health-interval=10s \
		--health-timeout=5s \
		--health-retries=5 \
		-p 5432:5432 \
		-e POSTGRES_DB=$(DB) \
		-e POSTGRES_USER=$(DB) \
		-e POSTGRES_PASSWORD=$(DB_PASSWORD) \
		-v $(VOLUME):/var/lib/postgresql/data \
		--network $(NETWORK) \
		$(DRIVER):$(DRIVER_VERSION)
```

- `DB_CONTAINER`: `pg-4kgbbad-17.5`
- Docker Compose beautifully defines:
```yaml
volumes:
  pg-4kgbbad-17.5-volume:
    name: pg-4kgbbad-17.5-volume

networks:
  4kgbbad-net:
    name: 4kgbbad-net
```

### ⚡ Init Task Example

```makefile
run-init:
	docker run --rm --network 4kgbbad-net \
	-e PGPASSWORD=---- This shall not be seen \
	-v $(pwd)/cen-at-init.sql:/init.sql:ro \
	postgres:17.5 psql -h pg-4kgbbad-1 -U postgres -d postgres -f /init.sql
```

All of this is designed to be **repeatable**, **configurable**, and **team-friendly**. <3

---

## 🗂️ Project Structure (EBC pattern) - fake it until you learn it

```
com.baerchen.central.authentication
├── authentication   # JWT converter, PasswordEncoder, filter chains
├── oauth            # Registered client management + config
├── userregister     # Registration endpoint & service
├── user             # User entity, roles, repo, service
├── config           # Liquibase, data sources, common config
├── logging          # ControllerLoggingAspect - this doesnt work
└── exception        # GlobalExceptionHandler - because im lazy 
```

---

## 🧪 Test Strategy

- Java-based integration tests  - the first is still waiting for fixes
- Token issuance and validation
- Access control via roles

---

## 🧭 Roadmap to MVP

| Feature                            | Status |
|------------------------------------|--------|
| Spring Authorization Server        | ✅ Done |
| OAuth2 DB Schema (Liquibase)       | ✅ Done |
| JWT with custom claims             | ✅ Done |
| Custom `JwtAuthenticationConverter`| ✅ Done |
| Role-based endpoint security       | ✅ Done |
| User entity + repo/service         | ✅ Done |
| Secure login w/ Thymeleaf          | ✅ Done |
| Self-service registration          | ✅ Done |
| Integration test: Token flow       | ⏳ To do |
| Docker Compose setup               | ✅ Done |

---

## 💡 Usage

1. Clone the repo and update your DB credentials in `application.yml`
2. Start PostgreSQL via Docker Compose:
   ```bash
   docker compose up -d
   ```
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Access endpoints under:
   - `/oauth2/authorize`
   - `/oauth2/token`
   - `/register`
   - `/login`
5. For hands-on flow examples, see `docs/CreateUserFlow.md`

---

## 🧠 Notes

- Built for multi-tenant or multi-service ecosystems
- Can act as a shared **authentication gateway**
- Extensible to **OpenID Connect** with minimal changes - when i do it

---

## 📊 MVP Progress Estimate

| Task                                 | Progress |
|--------------------------------------|----------|
| Auth Server setup + DB + JWT config  | ✅ 100%   |
| Custom JWT converter + roles         | ✅ 100%   |
| User entity + repo/service layer     | ✅ 100%   |
| Secure login with Thymeleaf UI       | ✅ 100%   |
| Self-service user registration       | ✅ 100%   |
| Integration test: token flow         | ⏳ 10%    |
| **Total Estimated Completion**       | **≈ 85%** ✅ |

---

🛠️ Built with ♥ by a dev who's not afraid of `make`, `jwt`, or a little Spring Security sorcery.
