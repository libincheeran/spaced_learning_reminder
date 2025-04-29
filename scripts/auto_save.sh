#!/bin/bash

# Function to handle container stop
handle_stop() {
    echo "Saving database state..."
    # Ensure MySQL is still running and accessible
    if docker ps | grep -q mysql; then
        docker exec mysql mysqldump -u root -pLibin3137@mysql leetcode > data/init.sql
        echo "Database state saved to data/init.sql"
    else
        echo "MySQL container is not running, cannot save state"
    fi
    exit 0
}

# Set up trap to catch container stop
trap handle_stop SIGTERM SIGINT

# Start the containers
docker-compose up

# The script will wait here until containers are stopped
# When stopped, the trap will trigger handle_stop 