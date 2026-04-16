@echo off
echo Downloading MySQL Connector JAR...
echo.

REM Create lib directory if it doesn't exist
if not exist "lib" mkdir lib

REM Download MySQL Connector using PowerShell
powershell -Command "& {Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar' -OutFile 'lib\mysql-connector-java-8.0.33.jar'}"

if exist "lib\mysql-connector-java-8.0.33.jar" (
    echo.
    echo SUCCESS: MySQL Connector downloaded successfully!
    echo.
    echo You can now run the application using: run_without_maven.bat
) else (
    echo.
    echo ERROR: Failed to download MySQL Connector.
    echo.
    echo Please download it manually from:
    echo https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar
    echo.
    echo Save it as: lib\mysql-connector-java-8.0.33.jar
)

echo.
pause
