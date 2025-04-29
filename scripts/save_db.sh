#!/bin/bash

# Wait for MySQL to be ready
echo "Waiting for MySQL to be ready..."
until docker exec mysql mysqladmin ping -h localhost -u root -pLibin3137@mysql --silent; do
    sleep 1
done

# Save the database state including table structure and data
echo "Saving database state..."
docker exec mysql mysqldump -u root -pLibin3137@mysql --databases leetcode --add-drop-database --add-drop-table --routines --events --triggers > data/init.sql

echo "Database state saved to data/init.sql" 