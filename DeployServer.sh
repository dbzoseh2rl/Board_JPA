#!/bin/bash

# Board JPA - Auto Deploy Script (Ubuntu/Linux)
# This script automatically deploys and runs the application

set -e  # 에러 발생 시 스크립트 중단

echo "========================================"
echo "Board JPA - Auto Deploy Script"
echo "========================================"
echo ""
echo "Server: $(hostname)"
echo "Date: $(date)"
echo "User: $(whoami)"

# 배포 디렉토리 설정
DEPLOY_DIR="$HOME/board-app"
JAR_FILE="$DEPLOY_DIR/app.jar"
PID_FILE="$DEPLOY_DIR/app.pid"
LOG_FILE="$DEPLOY_DIR/app.log"

echo ""
echo "1. Setting up deployment directory..."
mkdir -p "$DEPLOY_DIR"
cd "$DEPLOY_DIR"

# 기존 애플리케이션 종료
echo ""
echo "2. Stopping existing application..."
if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if ps -p $PID > /dev/null 2>&1; then
        echo "Stopping application (PID: $PID)..."
        kill $PID
        sleep 3
        
        # 강제 종료 확인
        if ps -p $PID > /dev/null 2>&1; then
            echo "Force killing application..."
            kill -9 $PID
        fi
        echo "Application stopped"
    else
        echo "No running application found"
    fi
    rm -f "$PID_FILE"
else
    echo "No PID file found"
fi

# JAR 파일 확인
echo ""
echo "3. Checking JAR file..."
if [ ! -f "$JAR_FILE" ]; then
    echo "ERROR: JAR file not found at $JAR_FILE"
    echo "Please ensure the file exists or run the GitHub Actions workflow first."
    exit 1
fi

echo "JAR file found: $JAR_FILE"
echo "File size: $(du -h "$JAR_FILE" | cut -f1)"
echo "Last modified: $(stat -c %y "$JAR_FILE")"

# 애플리케이션 시작
echo ""
echo "4. Starting application..."
echo "Command: java -jar $JAR_FILE --spring.profiles.active=ty"

nohup java -jar "$JAR_FILE" --spring.profiles.active=ty > "$LOG_FILE" 2>&1 &
APP_PID=$!
echo $APP_PID > "$PID_FILE"

echo "Application started with PID: $APP_PID"
echo "PID saved to: $PID_FILE"
echo "Log file: $LOG_FILE"

# 시작 대기
echo ""
echo "5. Waiting for application to start..."
for i in {1..30}; do
    if ps -p $APP_PID > /dev/null 2>&1; then
        echo -n "."
        sleep 1
    else
        echo ""
        echo "❌ Application failed to start!"
        echo "Check logs: $LOG_FILE"
        exit 1
    fi
done
echo ""

# 상태 확인
echo ""
echo "6. Checking application status..."
if ps -p $APP_PID > /dev/null 2>&1; then
    echo "✅ Application is running (PID: $APP_PID)"
    
    # 포트 확인
    if command -v netstat > /dev/null; then
        PORT_STATUS=$(netstat -tlnp 2>/dev/null | grep :8080 || echo "Port 8080 not listening")
        echo "Port status: $PORT_STATUS"
    fi
    
    # 헬스 체크
    echo ""
    echo "7. Performing health check..."
    if command -v curl > /dev/null; then
        echo "Waiting for application to be ready..."
        for i in {1..10}; do
            if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
                echo "✅ Health check passed!"
                break
            else
                echo "Attempt $i/10: Application not ready yet..."
                sleep 3
            fi
        done
        
        if [ $i -eq 10 ]; then
            echo "⚠️  Health check failed - application may still be starting"
        fi
    else
        echo "curl not available - skipping health check"
    fi
    
    echo ""
    echo "========================================"
    echo "🎉 Deployment successful!"
    echo "========================================"
    echo "PID: $APP_PID"
    echo "JAR File: $JAR_FILE"
    echo "Log file: $LOG_FILE"
    echo "URL: http://localhost:8080"
    echo "Swagger: http://localhost:8080/swagger-ui.html"
    echo "========================================"
    
else
    echo "❌ Application failed to start!"
    echo "Check logs: $LOG_FILE"
    exit 1
fi

echo ""
echo "Application Management Commands:"
echo "  Stop app:     kill \$(cat $PID_FILE)"
echo "  View logs:    tail -f $LOG_FILE"
echo "  Check status: ps aux | grep java"
echo "  Restart:      $0"
echo ""
echo "Deployment completed successfully!"
