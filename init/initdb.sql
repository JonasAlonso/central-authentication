-- Drop existing schema and user
DROP SCHEMA IF EXISTS ${ENV_PREFIX}_${database.schema} CASCADE;

DROP ROLE IF EXISTS ${ENV_PREFIX}_${database.user};

-- Create new user
CREATE ROLE ${ENV_PREFIX}_${database.user} LOGIN PASSWORD '${database.pswd}';

-- Create schema owned by the new user
CREATE SCHEMA ${ENV_PREFIX}_${database.schema} AUTHORIZATION ${ENV_PREFIX}_${database.user};

-- Tighten public access
REVOKE ALL ON SCHEMA ${ENV_PREFIX}_${database.schema} FROM PUBLIC;

-- Grant access to the new user (redundant since they are the owner, but explicit is good)
GRANT USAGE ON SCHEMA ${ENV_PREFIX}_${database.schema} TO ${ENV_PREFIX}_${database.user};
GRANT CREATE ON SCHEMA ${ENV_PREFIX}_${database.schema} TO ${ENV_PREFIX}_${database.user};

-- 🔥 Now grant access to postgres as well
GRANT USAGE ON SCHEMA ${ENV_PREFIX}_${database.schema} TO postgres;
GRANT CREATE ON SCHEMA ${ENV_PREFIX}_${database.schema} TO postgres;

-- Set default privileges inside this schema for both the user and postgres
ALTER DEFAULT PRIVILEGES IN SCHEMA ${ENV_PREFIX}_${database.schema}
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO ${ENV_PREFIX}_${database.user};

ALTER DEFAULT PRIVILEGES IN SCHEMA ${ENV_PREFIX}_${database.schema}
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO postgres;
