#!/usr/bin/env bash
# Demo scripts for Reactive Programming video

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Base URL
BASE_URL="http://localhost:8080"

# Function to check if server is running
check_server() {
    curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/demo/reactive/status" | grep -q "200"
    if [ $? -ne 0 ]; then
        echo "⚠️  Server not running on port 8080. Start with: gradle :reactive:bootRun"
        exit 1
    fi
}

# Main menu
show_menu() {
    echo ""
    echo "🚀 Reactive Programming Demo Scripts"
    echo "===================================="
    echo "1) Test concurrent Mono requests (3 parallel)"
    echo "2) Test concurrent Mono requests (10 parallel)"
    echo "3) Toggle error simulation"
    echo "4) Toggle slowness simulation"
    echo "5) Reset demo state"
    echo "6) Show backpressure demo"
    echo "7) Stream events (SSE) - Press Ctrl+C to stop"
    echo "8) Performance test (100 concurrent delayed requests)"
    echo "9) Check demo status"
    echo "0) Exit"
    echo ""
    read -p "Select option: " choice
}

case "${1:-menu}" in
    mono3|1)
        check_server
        echo -e "${BLUE}Making 3 concurrent requests to Mono endpoint...${NC}"
        for i in {1..3}; do
            echo "Request $i"
            curl -s "$BASE_URL/api/demo/reactive/mono" | jq -c '{transformed, newSalary, processingTime}' &
        done
        wait
        echo -e "${GREEN}✓ All requests completed${NC}"
        ;;
        
    mono10|2)
        check_server
        echo -e "${BLUE}Making 10 concurrent requests to Mono endpoint...${NC}"
        for i in {1..10}; do
            curl -s "$BASE_URL/api/demo/reactive/mono" &
        done
        wait
        echo -e "${GREEN}✓ All requests completed${NC}"
        ;;
        
    error|3)
        check_server
        echo -e "${BLUE}Toggling error simulation...${NC}"
        curl -X POST "$BASE_URL/api/demo/reactive/toggle-error" | jq
        ;;
        
    slow|4)
        check_server
        echo -e "${BLUE}Toggling slowness simulation...${NC}"
        curl -X POST "$BASE_URL/api/demo/reactive/toggle-slowness" | jq
        ;;
        
    reset|5)
        check_server
        echo -e "${BLUE}Resetting demo state...${NC}"
        curl -X POST "$BASE_URL/api/demo/reactive/reset" | jq
        ;;
        
    backpressure|6)
        check_server
        echo -e "${BLUE}Running backpressure demo...${NC}"
        curl "$BASE_URL/api/demo/reactive/backpressure" | jq
        ;;
        
    events|7)
        check_server
        echo -e "${BLUE}Streaming events (Press Ctrl+C to stop)...${NC}"
        curl -N "$BASE_URL/api/demo/reactive/events"
        ;;
        
    perf|8)
        check_server
        echo -e "${BLUE}Performance test: 100 concurrent requests with 1s delay...${NC}"
        start_time=$(date +%s)
        for i in {1..100}; do
            curl -s "$BASE_URL/api/employees/delayed?seconds=1" > /dev/null &
        done
        wait
        end_time=$(date +%s)
        duration=$((end_time - start_time))
        echo -e "${GREEN}✓ Completed 100 requests in ${duration} seconds${NC}"
        echo "With blocking I/O, this would take 100+ seconds!"
        ;;
        
    status|9)
        check_server
        echo -e "${BLUE}Current demo status:${NC}"
        curl -s "$BASE_URL/api/demo/reactive/status" | jq
        ;;
        
    menu|*)
        check_server
        while true; do
            show_menu
            case $choice in
                1) $0 mono3 ;;
                2) $0 mono10 ;;
                3) $0 error ;;
                4) $0 slow ;;
                5) $0 reset ;;
                6) $0 backpressure ;;
                7) $0 events ;;
                8) $0 perf ;;
                9) $0 status ;;
                0) echo "Goodbye!"; exit 0 ;;
                *) echo "Invalid option" ;;
            esac
        done
        ;;
esac