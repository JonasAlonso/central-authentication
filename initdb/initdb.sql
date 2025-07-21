DROP ROLE IF EXISTS ${db.username};

CREATE ROLE ${db.username} LOGIN PASSWORD '${db.userpassword}';

DROP SCHEMA IF EXISTS ${db.schema} CASCADE;

CREATE SCHEMA ${db.schema} AUTHORIZATION ${db.username};

REVOKE ALL ON SCHEMA ${db.schema} FROM PUBLIC;
GRANT USAGE ON SCHEMA ${db.schema} TO ${db.username};
GRANT CREATE ON SCHEMA ${db.schema} TO ${db.username};

ALTER DEFAULT PRIVILEGES IN SCHEMA ${db.schema}
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO ${db.username};
