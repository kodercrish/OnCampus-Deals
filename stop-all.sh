#!/bin/bash

PID_FILE="service_pids.txt"

if [ ! -s "$PID_FILE" ]; then
    echo "Error: $PID_FILE is missing or empty."
    echo "If processes are still running, use: pkill -f 'java|expo'"
    exit 1
fi

echo "Stopping services listed in $PID_FILE..."

# Read the file line by line
while read -r PID; do
    # Check if PID is a number and exists
    if [[ "$PID" =~ ^[0-9]+$ ]] && ps -p $PID > /dev/null; then
        kill $PID
        echo "Killed process $PID"
    else
        echo "Process $PID not found or already stopped."
    fi
done < "$PID_FILE"

# Clean up
rm "$PID_FILE"
echo "Cleanup complete."