# ==== ENVIRONMENT CONFIGURATION ====
include .env
export

COUNTER := 5
# Derived identifiers
VOLUME_NAME      := $(POSTGRES_PREFIX)-$(MACHINE)-vol
DB_CONTAINER     := $(POSTGRES_PREFIX)-$(MACHINE)-$(COUNTER)
APP_CONTAINER    := $(APP)-$(COUNTER)
NETWORK_NAME     := $(MACHINE)-net

SPRING_DATASOURCE_URL := jdbc:postgresql://$(POSTGRES_PREFIX)-$(MACHINE)-$(COUNTER)/postgres
LIQUIBASE_URL := jdbc:postgresql://$(POSTGRES_PREFIX)-$(MACHINE)-$(COUNTER)/postgres


# ==== Create a clean Postgres container ====
db-bootstrap:
	@echo "🐘 Creating Postgres container: $(DB_CONTAINER)"
	docker volume create $(VOLUME_NAME)
	docker network create $(NETWORK_NAME) || true
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
		-v $(VOLUME_NAME):/var/lib/postgresql/data \
		--network $(NETWORK_NAME) \
		$(DRIVER):$(DRIVER_VERSION)

# ==== Dynamically generate the init.sql based on vars ====
generate-init-sql:
	@echo "🧬 Generating $(INIT_SQL) for app: $(APP)"
	@mkdir -p $(OUT_DIR)
	@echo "DROP SCHEMA IF EXISTS $(APP) CASCADE;"                          >  $(INIT_SQL)
	@echo "DROP ROLE IF EXISTS $(APP);"                                  >> $(INIT_SQL)
	@echo "CREATE ROLE $(APP) LOGIN PASSWORD '$(APP_PASSWORD)';"         >> $(INIT_SQL)
	@echo "CREATE SCHEMA $(APP) AUTHORIZATION $(APP);"                   >> $(INIT_SQL)
	@echo "REVOKE ALL ON SCHEMA $(APP) FROM PUBLIC;"                     >> $(INIT_SQL)
	@echo "GRANT USAGE ON SCHEMA $(APP) TO $(APP);"                      >> $(INIT_SQL)
	@echo "GRANT CREATE ON SCHEMA $(APP) TO $(APP);"                     >> $(INIT_SQL)
	@echo "GRANT USAGE ON SCHEMA $(APP) TO postgres;"                    >> $(INIT_SQL)
	@echo "GRANT CREATE ON SCHEMA $(APP) TO postgres;"                   >> $(INIT_SQL)
	@echo "ALTER DEFAULT PRIVILEGES IN SCHEMA $(APP) GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO $(APP);" >> $(INIT_SQL)
	@echo "ALTER DEFAULT PRIVILEGES IN SCHEMA $(APP) GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO postgres;" >> $(INIT_SQL)

# ==== Mount and execute the init.sql in transient psql container ====
# ==== Dynamically generate the init.sql based on vars ====
run-template-init:
	@echo "📤 Injecting schema for $(APP)..."
	@sed \
		-e "s/__APP__/$(APP)/g" \
		-e "s/__APP_PASSWORD__/$(APP_PASSWORD)/g" \
		./init-template.sql | tee /dev/tty | docker run --rm -i \
		--network $(NETWORK_NAME) \
		-e PGPASSWORD=$(DB_PASSWORD) \
		$(DRIVER):$(DRIVER_VERSION) \
		psql -h $(DB_CONTAINER) -U $(DB) -d $(DB)


verify-init:
	@echo "🔍 Verifying existence of schema \`$(APP)\` and role \`$(APP)\`..."
	@docker run --rm \
		--network $(NETWORK_NAME) \
		-e PGPASSWORD=$(DB_PASSWORD) \
		$(DRIVER):$(DRIVER_VERSION) \
		psql -h $(DB_CONTAINER) -U postgres -d postgres \
		-c "SELECT schema_name FROM information_schema.schemata WHERE schema_name = '$(APP)';" \
		-c "SELECT usename FROM pg_user WHERE usename = '$(APP)';"


run-cmd-preview:
	@echo "💡 Paste this into IntelliJ Run Configuration's 'Environment variables':"
	@echo ""
	@echo "APP=$(APP);"
	@echo "APP_PASSWORD=$(APP_PASSWORD);"
	@echo "DB=$(DB);"
	@echo "DB_PASSWORD=$(DB_PASSWORD);"
	@echo "SPRING_DATASOURCE_URL=jdbc:postgresql://$(POSTGRES_PREFIX)-$(MACHINE)-$(COUNTER):5432/postgres;"
	@echo "LIQUIBASE_URL=jdbc:postgresql://$(POSTGRES_PREFIX)-$(MACHINE)-$(COUNTER):5432/postgres;"
	@echo "SPRING_PROFILES_ACTIVE=dev"

run-docker-build-preview:
	@echo "🚧 Docker Build Command:"
	@echo ""
	@echo "docker buildx build \\"
	@echo "  --build-arg SPRING_DATASOURCE_URL=jdbc:postgresql://$(POSTGRES_PREFIX)-$(MACHINE)-$(COUNTER):5432/postgres \\"
	@echo "  --build-arg SPRING_DATASOURCE_USERNAME=$(APP) \\"
	@echo "  --build-arg SPRING_DATASOURCE_PASSWORD=$(APP_PASSWORD) \\"
	@echo "  --build-arg LIQUIBASE_URL=jdbc:postgresql://$(POSTGRES_PREFIX)-$(MACHINE)-$(COUNTER):5432/postgres \\"
	@echo "  --build-arg DB=$(DB) \\"
	@echo "  --build-arg DB_PASSWORD=$(DB_PASSWORD) \\"
	@echo "  -t $(APP)-app:latest ."
	@echo ""
	@echo "🚀 Run Container Command:"
	@echo ""
	@echo "docker run --rm \\"
	@echo "  --name $(APP)-app \\"
	@echo "  --network $(NETWORK) \\"
	@echo "  -e SPRING_PROFILES_ACTIVE=dev \\"
	@echo "  -e SPRING_DATASOURCE_URL=jdbc:postgresql://$(POSTGRES_PREFIX)-$(MACHINE)-$(COUNTER):5432/postgres \\"
	@echo "  -e SPRING_DATASOURCE_USERNAME=$(APP) \\"
	@echo "  -e SPRING_DATASOURCE_PASSWORD=$(APP_PASSWORD) \\"
	@echo "  -e LIQUIBASE_URL=jdbc:postgresql://$(POSTGRES_PREFIX)-$(MACHINE)-$(COUNTER):5432/postgres \\"
	@echo "  -e DB=$(DB) \\"
	@echo "  -e DB_PASSWORD=$(DB_PASSWORD) \\"
	@echo "  $(APP)-app:latest"
