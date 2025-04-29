#!/bin/bash

if [ -z "$1" ]; then
    echo "Usage: $0 <backup_file.sql.gz>"
    exit 1
fi

BACKUP_FILE=$1

# Check if the backup file exists
if [ ! -f "$BACKUP_FILE" ]; then
    echo "Backup file not found: $BACKUP_FILE"
    exit 1
fi

# Decompress the backup file
gunzip -c $BACKUP_FILE | docker exec -i mysql mysql -u root -pLibin3137@mysql leetcode

echo "Database restored from: $BACKUP_FILE" 