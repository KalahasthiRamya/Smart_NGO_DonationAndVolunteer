@REM ----------------------------------------------------------------------------
@REM Smart NGO Platform - Maven Start Up Batch Script
@REM Auto-frees Port 8080 if occupied by a previous Java process
@REM ----------------------------------------------------------------------------
@echo off
setlocal

set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-8.0.502.7-hotspot"
set "MAVEN_CMD=C:\Users\ramya\AppData\Roaming\Code\User\globalStorage\pleiades.java-extension-pack-jdk\maven\latest\bin\mvn.cmd"

echo Stopping any existing Java processes on port 8080...
powershell -Command "Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force -ErrorAction SilentlyContinue }"

echo Starting Smart NGO Platform on http://localhost:8080 ...
if exist "%MAVEN_CMD%" (
    "%MAVEN_CMD%" %*
) else (
    mvn %*
)
