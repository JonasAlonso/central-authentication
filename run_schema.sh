#!/bin/bash

# Usage: ./run-schema.sh dev | test | local

ENV=${1:-dev}  # Use first argument, or fallback to 'dev'
if [ -z "$ENV" ]; then
  echo "Usage: ./run-schema.sh [dev|test|local]"
  exit 1
fi

UPPER_ENV=$(echo "$ENV" | tr '[:lower:]' '[:upper:]')

# Load the env file with DB credentials (do NOT commit to git)
echo "Sourcing env/$ENV.properties..."
set -a
source env/$ENV.properties
set +a

# Run the schema creation using the correct profile and prefix
echo "Running Maven SQL plugin with -P$ENV and ENV_PREFIX=$UPPER_ENV..."
mvn sql:execute@initdb -P$ENV -DENV_PREFIX=$UPPER_ENV