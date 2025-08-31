@echo off
setlocal enabledelayedexpansion

echo ========================================
echo Board JPA - Local Deployment Script
echo ========================================

:: GitHub 저장소 정보 설정
set REPO_OWNER=your-username
set REPO_NAME=Board_JPA-master
set BRANCH=master
set WORKFLOW_NAME=Java CI with Gradle

echo.
echo 1. Checking GitHub Actions status...
echo Repository: %REPO_OWNER%/%REPO_NAME%
echo Branch: %BRANCH%

:: GitHub CLI가 설치되어 있는지 확인
gh --version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: GitHub CLI (gh) is not installed!
    echo Please install from: https://cli.github.com/
    echo.
    pause
    exit /b 1
)

:: GitHub에 로그인 확인
gh auth status >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Please login to GitHub first:
    echo gh auth login
    echo.
    pause
    exit /b 1
)

echo.
echo 2. Getting latest workflow run...
for /f "tokens=*" %%i in ('gh run list --repo %REPO_OWNER%/%REPO_NAME% --branch %BRANCH% --limit 1 --json databaseId,status,conclusion --jq ".[0].databaseId"') do set RUN_ID=%%i

if "%RUN_ID%"=="" (
    echo ERROR: No workflow runs found!
    pause
    exit /b 1
)

echo Latest run ID: %RUN_ID%

:: 워크플로우 상태 확인
for /f "tokens=*" %%i in ('gh run view %RUN_ID% --repo %REPO_OWNER%/%REPO_NAME% --json status,conclusion --jq ".status"') do set STATUS=%%i

if "%STATUS%"=="completed" (
    echo Workflow completed successfully!
) else (
    echo Workflow status: %STATUS%
    echo Please wait for workflow to complete.
    pause
    exit /b 1
)

echo.
echo 3. Downloading JAR artifacts...
if not exist "downloads" mkdir downloads
cd downloads

:: 아티팩트 다운로드
gh run download %RUN_ID% --repo %REPO_OWNER%/%REPO_NAME% --dir .

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to download artifacts!
    pause
    exit /b 1
)

echo.
echo 4. Stopping existing application...
taskkill /f /im java.exe >nul 2>&1
echo Existing application stopped.

echo.
echo 5. Starting application...
for %%f in (*.jar) do (
    echo Starting: %%f
    start "Board JPA App" java -jar "%%f" --spring.profiles.active=ty
    set JAR_FILE=%%f
    goto :found_jar
)

:found_jar
if defined JAR_FILE (
    echo.
    echo ========================================
    echo Application started successfully!
    echo JAR File: %JAR_FILE%
    echo URL: http://localhost:8080
    echo Swagger: http://localhost:8080/swagger-ui.html
    echo ========================================
    echo.
    echo Press any key to open in browser...
    pause >nul
    start http://localhost:8080
) else (
    echo ERROR: No JAR file found in artifacts!
)

cd ..
echo.
echo Deployment completed!
pause
