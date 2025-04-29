#!/bin/bash

# Create backup directory if it doesn't exist
BACKUP_DIR="./backups"
mkdir -p $BACKUP_DIR

# Generate backup filename with timestamp
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/mysql_backup_$TIMESTAMP.sql"

# Execute backup
docker exec mysql mysqldump -u root -pLibin3137@mysql leetcode > $BACKUP_FILE

# Compress the backup
gzip $BACKUP_FILE

echo "Backup created: $BACKUP_FILE.gz" 