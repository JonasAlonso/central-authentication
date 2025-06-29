# Central Authentication Service

This repository contains a Spring Boot based authentication and registration service.

## Requirements
- Java 21 or later
- Maven Wrapper (`./mvnw`)
- Docker (for running the provided `compose.yaml` database)

## Running Locally
1. Start the PostgreSQL database:
   ```bash
   docker compose up -d
   ```
2. Build and run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The service exposes registration endpoints and OAuth2 authentication via Spring Authorization Server.

## Tests
Run the unit tests with:
```bash
./mvnw test
```

## License
This project is licensed under the GNU GPL v3. See [LICENSE](LICENSE) for details.
