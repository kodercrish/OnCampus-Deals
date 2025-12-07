#!/bin/bash

# Simple test runner in the same style as your start-all.sh
# Runs ./gradlew clean test in each backend service in background (nohup)
# Logs -> ./test_logs/<Service>_test.log
# No PID tracking, no kill logic (build/test processes are short-lived)

ROOT="$(pwd)"
LOG_DIR="$ROOT/test_logs"

# create logs dir
mkdir -p "$LOG_DIR"

echo "Starting tests..."
# list services (match folder names under backend/)
SERVICES=("Admin" "ApiGateway" "Chat" "identity" "Listing" "Search")

for SERVICE in "${SERVICES[@]}"; do
  echo "Starting $SERVICE..."
  pushd "backend/$SERVICE" > /dev/null 2>&1 || { echo "  (skipping $SERVICE - dir missing)"; continue; }

  # run gradle test in background, redirect logs (use same pattern as your start script)
  nohup ./gradlew clean test > "../../test_logs/${SERVICE}_test.log" 2>&1 &

  popd > /dev/null 2>&1
done

echo "--------------------------------------------------"
echo "All tests started!"
echo "Logs saved to $LOG_DIR"
echo "To follow a log: tail -f $LOG_DIR/<Service>_test.log"
echo "--------------------------------------------------"
