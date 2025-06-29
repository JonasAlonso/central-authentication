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
- [ ] User management (create, list, persist)
- [ ] Secure form-based login
- [ ] Self-service registration
- [ ] JWT issuance with custom claims
- [ ] Integration with protected backend endpoints
- [ ] Token refresh flow
- [ ] Dockerized local dev setup (optional)

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
| User entity + repo/service    | ⏳ To do |
| Secure login w/ Thymeleaf     | ⏳ To do |
| Self-service registration     | ⏳ To do |
| Integration test: Token flow  | ⏳ To do |
| Docker Compose setup          | ⏳ To do |

---

## 💡 Usage

1. Clone the repo and update your DB credentials in `application.yml`.
2. Run the app locally via:
   ```bash
   ./mvnw spring-boot:run
3. Access authorization endpoints under /oauth2/authorize, /oauth2/token, etc.

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
| User entity + repo/service layer     | ⏳ 0%     |
| Secure login with Thymeleaf UI       | ⏳ 0%     |
| Self-service user registration       | ⏳ 0%     |
| Integration test: token flow         | ⏳ 10%    |
| Total Estimated Completion           | **≈ 45%** ✅ |

Nearly halfway there. Up next ->  get user management + the UI + integration tests, to be in MVP territory.
