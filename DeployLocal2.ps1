# Board JPA - Local Deployment Script (PowerShell)
param(
    [string]$RepoOwner = "your-username",
    [string]$RepoName = "Board_JPA-master",
    [string]$Branch = "master"
)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Board JPA - Local Deployment Script" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

Write-Host ""
Write-Host "Repository: $RepoOwner/$RepoName" -ForegroundColor Yellow
Write-Host "Branch: $Branch" -ForegroundColor Yellow

# GitHub CLI 설치 확인
try {
    $ghVersion = gh --version 2>$null
    if ($LASTEXITCODE -ne 0) {
        throw "GitHub CLI not found"
    }
    Write-Host "GitHub CLI found" -ForegroundColor Green
} catch {
    Write-Host ""
    Write-Host "ERROR: GitHub CLI (gh) is not installed!" -ForegroundColor Red
    Write-Host "Please install from: https://cli.github.com/" -ForegroundColor Red
    Write-Host ""
    Read-Host "Press Enter to continue"
    exit 1
}

# GitHub 로그인 상태 확인
try {
    gh auth status 2>$null
    if ($LASTEXITCODE -ne 0) {
        throw "Not authenticated"
    }
    Write-Host "GitHub authenticated" -ForegroundColor Green
} catch {
    Write-Host ""
    Write-Host "Please login to GitHub first:" -ForegroundColor Yellow
    Write-Host "gh auth login" -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Press Enter to continue"
    exit 1
}

Write-Host ""
Write-Host "1. Getting latest workflow run..." -ForegroundColor Yellow

# 최신 워크플로우 실행 ID 가져오기
try {
    $runList = gh run list --repo "$RepoOwner/$RepoName" --branch $Branch --limit 1 --json databaseId,status,conclusion
    $runData = $runList | ConvertFrom-Json
    
    if ($runData.Count -eq 0) {
        Write-Host "ERROR: No workflow runs found!" -ForegroundColor Red
        Read-Host "Press Enter to continue"
        exit 1
    }
    
    $runId = $runData[0].databaseId
    $status = $runData[0].status
    
    Write-Host "Latest run ID: $runId" -ForegroundColor Green
    Write-Host "Status: $status" -ForegroundColor Green
    
    if ($status -ne "completed") {
        Write-Host "Please wait for workflow to complete." -ForegroundColor Yellow
        Read-Host "Press Enter to continue"
        exit 1
    }
    
} catch {
    Write-Host "ERROR: Failed to get workflow run info!" -ForegroundColor Red
    Read-Host "Press Enter to continue"
    exit 1
}

Write-Host ""
Write-Host "2. Downloading JAR artifacts..." -ForegroundColor Yellow

# 다운로드 디렉토리 생성
$downloadDir = "downloads"
if (!(Test-Path $downloadDir)) {
    New-Item -ItemType Directory -Path $downloadDir | Out-Null
}

Set-Location $downloadDir

# 아티팩트 다운로드
try {
    gh run download $runId --repo "$RepoOwner/$RepoName" --dir .
    if ($LASTEXITCODE -ne 0) {
        throw "Download failed"
    }
    Write-Host "Artifacts downloaded successfully" -ForegroundColor Green
} catch {
    Write-Host "ERROR: Failed to download artifacts!" -ForegroundColor Red
    Set-Location ..
    Read-Host "Press Enter to continue"
    exit 1
}

Write-Host ""
Write-Host "3. Stopping existing application..." -ForegroundColor Yellow

# 기존 Java 프로세스 종료
try {
    Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force
    Write-Host "Existing application stopped" -ForegroundColor Green
} catch {
    Write-Host "No existing application found" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "4. Starting application..." -ForegroundColor Yellow

# JAR 파일 찾기 및 실행
$jarFiles = Get-ChildItem -Filter "*.jar"
if ($jarFiles.Count -eq 0) {
    Write-Host "ERROR: No JAR file found in artifacts!" -ForegroundColor Red
    Set-Location ..
    Read-Host "Press Enter to continue"
    exit 1
}

$jarFile = $jarFiles[0].Name
Write-Host "Starting: $jarFile" -ForegroundColor Green

# 애플리케이션 백그라운드에서 실행
Start-Process -FilePath "java" -ArgumentList "-jar", $jarFile, "--spring.profiles.active=ty" -WindowStyle Hidden

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "Application started successfully!" -ForegroundColor Green
Write-Host "JAR File: $jarFile" -ForegroundColor Green
Write-Host "URL: http://localhost:8080" -ForegroundColor Green
Write-Host "Swagger: http://localhost:8080/swagger-ui.html" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green

Write-Host ""
Write-Host "Press any key to open in browser..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

# 브라우저에서 열기
Start-Process "http://localhost:8080"

Set-Location ..
Write-Host ""
Write-Host "Deployment completed!" -ForegroundColor Green
Read-Host "Press Enter to continue"
