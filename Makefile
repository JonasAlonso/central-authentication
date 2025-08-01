# ─────────────────────────────────────
# CONFIGURATION
# ─────────────────────────────────────
PROJECT_NAME := central-auth
NETWORK := pg-4kgbbad-net
DB_VOLUME := pg-4kgbbad-volume
APP_CONTAINER := cauth_app
DB_CONTAINER := pg-4kgbbad-1
PORT := 9090

# ─────────────────────────────────────
# COMMANDS
# ─────────────────────────────────────

up:
	docker compose up --build -d

down:
	docker compose down

restart: down up

logs:
	docker compose logs -f

bash:
	docker exec -it $(APP_CONTAINER) sh

psql:
	docker exec -it $(DB_CONTAINER) psql -U postgres -d $(POSTGRES_DB)

env:
	@echo "POSTGRES_DB=$(POSTGRES_DB)"
	@echo "APP_DB_USER=$(APP_DB_USER)"
	@echo "APP_DB_PASSWORD=$(APP_DB_PASSWORD)"

reset-db:
	docker compose down -v
	docker volume rm $(DB_VOLUME) || true
	docker compose up -d --build

rebuild:
	docker compose build --no-cache

status:
	docker compose ps

open:
	xdg-open http://localhost:$(PORT)

# ─────────────────────────────────────
# SHORTCUTS
# ─────────────────────────────────────

r: restart
b: bash
l: logs
s: status
o: open
gg