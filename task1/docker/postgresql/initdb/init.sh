#!/bin/sh
set -e
psql -v ON_ERROR_STOP=1 -v "user=$PG_TASK1_USER" -v "pass=$PG_TASK1_PASS" \
  --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" \
  < /docker-entrypoint-initdb.d/sql/users.sql
cp /etc/postgresql/config/postgresql.conf /var/lib/postgresql/data/postgresql.conf
cp /etc/postgresql/config/pg_hba.conf /var/lib/postgresql/data/pg_hba.conf