#!/bin/bash

# 1. Setup
mkdir -p logs
PID_FILE="$(pwd)/service_pids.txt"
echo "Starting services..."
# Create/Clear the PID file
> "$PID_FILE"

# List of backend services (Exact case matching your screenshot)
# SERVICES=("ApiGateway" "identity" "Listing")
SERVICES=("Admin" "ApiGateway" "Chat" "identity" "Listing" "Search")

# 2. Start Backend Services
for SERVICE in "${SERVICES[@]}"; do
    echo "Starting $SERVICE..."
    
    # Go into the directory
    pushd "backend/$SERVICE" > /dev/null
    
    # Run gradle in background with nohup (prevents hanging)
    nohup ./gradlew bootRun > "../../logs/$SERVICE.log" 2>&1 &
    
    # Capture the PID of the background process immediately
    PID=$!
    echo $PID >> "$PID_FILE"
    
    # Go back to root
    popd > /dev/null
done

# # 3. Start Frontend
# echo "Starting Frontend..."
# pushd frontend > /dev/null
# nohup npx expo start --port 8086 > "../logs/frontend.log" 2>&1 &
# PID=$!
# echo $PID >> "$PID_FILE"
# popd > /dev/null

echo "--------------------------------------------------"
echo "All services started!"
echo "PIDs saved to $PID_FILE"
echo "To stop them, run: ./stop-all.sh"
echo "--------------------------------------------------"