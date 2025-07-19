# 🛡 Central Authentication Service

A Spring Boot 3.5.3 application using Spring Authorization Server 1.5.1 to act as a central authentication service for multiple backend applications. It supports secure login, role-based access control, JWT issuance, and OAuth2 client registration.

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
- Docker / Docker Compose (optional)

---

## 📚 Features

- [x] Spring Authorization Server setup
- [x] OAuth2 default tables via Liquibase (`oauth2_registered_client`, etc.)
- [x] Role-based security (`@PreAuthorize`)
- [x] Custom `JwtAuthenticationConverter` to extract authorities
- [x] User management (create, list, persist)
- [x] Secure form-based login (Spring Boot login page)
- [x] Self-service registration
- [ ] JWT issuance with custom claims
- [x] Admin endpoints for OAuth2 client CRUD (`static/client-admin.html`)
- [ ] Integration with protected backend endpoints
- [ ] Token refresh flow
- [x] Dockerized local dev setup (compose + `application.yml`)
- Logging via `ControllerLoggingAspect`
- Global exception handling via `GlobalExceptionHandler`

---

## 🗂️ Project Structure (EBC pattern)


---

## 🧪 Test Strategy

- Java-based integration tests
- Token issuance and validation
- Access control via role-based authorization
- H2 for isolated test DB

---

## 🧭 Roadmap to MVP

| Feature                        | Status  |
|-------------------------------|---------|
| Spring Authorization Server   | ✅ Done |
| OAuth2 DB Schema (Liquibase)  | ✅ Done |
| JWT with custom claims        | ✅ Done |
| Custom `JwtAuthenticationConverter` | ✅ Done |
| Role-based endpoint security  | ✅ Done |
| User entity + repo/service    | ✅ Done |
| Secure login w/ Thymeleaf     | ✅ Done |
| Self-service registration     | ✅ Done |
| Integration test: Token flow  | ⏳ To do |
| Docker Compose setup          | ✅ Done |

---

## 💡 Usage

1. Clone the repo and update your DB credentials in `application.yml`.
2. Start PostgreSQL via Docker Compose:
   ```bash
   docker compose up -d
   ```
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Access authorization endpoints under /oauth2/authorize, /oauth2/token, etc.
5. See [docs/CreateUserFlow.md](docs/CreateUserFlow.md) for manual auth & client flow examples.

---

## 🧠 Notes
Built to support multi-tenant or multi-service ecosystems.

Can act as a shared authentication gateway for multiple Spring Boot apps.

Extensible to OpenID Connect with minimal config changes.


---

## 📊 MVP Progress Estimate

Here’s a % breakdown:

| Task                                 | Progress |
|--------------------------------------|----------|
| Auth Server setup + DB + JWT config | ✅ 100%   |
| Custom JWT converter + roles         | ✅ 100%   |
| User entity + repo/service layer     | ✅ 100%   |
| Secure login with Thymeleaf UI       | ✅ 100%   |
| Self-service user registration       | ✅ 100%   |
| Integration test: token flow         | ⏳ 10%    |
| Total Estimated Completion           | **≈ 85%** ✅ |


Nearly complete. Next step -> finalize token flow tests and polish docs.
