#!/bin/bash

# Wait for MySQL to be ready
while ! mysqladmin ping -h"localhost" --silent; do
    sleep 1
done

# Dump the database with table structure and data
mysqldump -u root -p"${MYSQL_ROOT_PASSWORD}" --databases leetcode > /docker-entrypoint-initdb.d/init.sql

# Keep the container running until it receives a signal
trap "exit" SIGTERM SIGINT
while true; do sleep 1; done 