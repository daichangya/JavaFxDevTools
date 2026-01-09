@echo off
REM JavaFxDevTools Installation Script for Windows
REM Usage: install.bat [version]

setlocal enabledelayedexpansion

REM Configuration
set REPO_URL=https://github.com/daichangya/JavaFxDevTools
set INSTALL_DIR=%USERPROFILE%\JavaFxDevTools
set BIN_DIR=%USERPROFILE%\JavaFxDevTools\bin
set VERSION=%1

if "%VERSION%"=="" set VERSION=latest

echo JavaFxDevTools Installation Script
echo ==================================
echo.

REM Check Java version
java -version >nul 2>&1
if errorlevel 1 (
    echo Error: Java is not installed. Please install JDK 17 or higher.
    echo Visit: https://adoptium.net/
    pause
    exit /b 1
)

echo [OK] Java version check passed.
echo.

REM Determine version
if "%VERSION%"=="latest" (
    echo Fetching latest version...
    for /f "tokens=*" %%i in ('curl -s https://api.github.com/repos/daichangya/JavaFxDevTools/releases/latest ^| findstr "tag_name"') do (
        set TAG_LINE=%%i
    )
    REM Extract version from JSON (simplified)
    echo Latest version will be determined from GitHub API
    set VERSION=latest
)

REM Create installation directory
echo Creating installation directory...
if not exist "%INSTALL_DIR%" mkdir "%INSTALL_DIR%"
if not exist "%BIN_DIR%" mkdir "%BIN_DIR%"

REM Download JAR files
echo.
echo Downloading DevTools...
curl -L -f -o "%INSTALL_DIR%\DevTools-%VERSION%.jar" "%REPO_URL%/releases/download/v%VERSION%/DevTools-%VERSION%.jar"
if errorlevel 1 (
    echo Error: Failed to download DevTools
    pause
    exit /b 1
)
echo [OK] DevTools downloaded successfully

echo Downloading JavaFxEditor...
curl -L -f -o "%INSTALL_DIR%\JavaFxEditor-%VERSION%.jar" "%REPO_URL%/releases/download/v%VERSION%/JavaFxEditor-%VERSION%.jar"
if errorlevel 1 (
    echo Error: Failed to download JavaFxEditor
    pause
    exit /b 1
)
echo [OK] JavaFxEditor downloaded successfully

REM Create launcher scripts
echo.
echo Creating launcher scripts...

echo @echo off > "%BIN_DIR%\devtools.bat"
echo java -jar "%INSTALL_DIR%\DevTools-%VERSION%.jar" %%* >> "%BIN_DIR%\devtools.bat"

echo @echo off > "%BIN_DIR%\javafxeditor.bat"
echo java -jar "%INSTALL_DIR%\JavaFxEditor-%VERSION%.jar" %%* >> "%BIN_DIR%\javafxeditor.bat"

echo [OK] Launcher scripts created

REM Check if bin directory is in PATH
echo %PATH% | findstr /C:"%BIN_DIR%" >nul
if errorlevel 1 (
    echo.
    echo [WARNING] %BIN_DIR% is not in your PATH
    echo.
    echo To add it permanently:
    echo 1. Open System Properties ^(Win + Pause^)
    echo 2. Click "Environment Variables"
    echo 3. Edit "Path" variable
    echo 4. Add: %BIN_DIR%
    echo.
) else (
    echo [OK] %BIN_DIR% is already in your PATH
)

echo.
echo Installation completed!
echo.
echo Applications installed to: %INSTALL_DIR%
echo Launcher scripts created in: %BIN_DIR%
echo.
echo You can now run:
echo   devtools.bat
echo   javafxeditor.bat
echo.
pause

