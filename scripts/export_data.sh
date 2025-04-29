#!/bin/bash

# Export all data from the leetcode database to a SQL file
docker exec mysql mysqldump -u root -p"Libin3137@mysql" leetcode > sql/leetcode_data.sql

echo "Data exported to sql/leetcode_data.sql" 