#!/bin/bash

# Import data from SQL file to the leetcode database
docker exec -i mysql mysql -u root -p"$MYSQL_ROOT_PASSWORD" leetcode < sql/leetcode_data.sql

echo "Data imported from sql/leetcode_data.sql" 