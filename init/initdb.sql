DROP SCHEMA IF EXISTS ${ENV_PREFIX}_${database.schema} CASCADE;

DROP ROLE IF EXISTS ${ENV_PREFIX}_${database.user};

CREATE ROLE ${ENV_PREFIX}_${database.user} LOGIN PASSWORD '${database.pswd}';

CREATE SCHEMA ${ENV_PREFIX}_${database.schema} AUTHORIZATION ${ENV_PREFIX}_${database.user};

REVOKE ALL ON SCHEMA ${ENV_PREFIX}_${database.schema} FROM PUBLIC;

GRANT USAGE ON SCHEMA ${ENV_PREFIX}_${database.schema} TO ${ENV_PREFIX}_${database.user};
GRANT CREATE ON SCHEMA ${ENV_PREFIX}_${database.schema} TO ${ENV_PREFIX}_${database.user};

ALTER DEFAULT PRIVILEGES IN SCHEMA ${ENV_PREFIX}_${database.schema}
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO ${ENV_PREFIX}_${database.user};
