@echo off
echo Bank Management System - Manual Setup Runner
echo.

REM Check if MySQL connector exists
if not exist "lib\mysql-connector-java-8.0.33.jar" (
    echo ERROR: MySQL Connector JAR not found!
    echo.
    echo Please download it from:
    echo https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar
    echo.
    echo Save it as: lib\mysql-connector-java-8.0.33.jar
    echo.
    pause
    exit /b 1
)

echo MySQL Connector found. Compiling application...
echo.

REM Compile all Java files
javac -cp "lib\mysql-connector-java-8.0.33.jar" -d . ^
    src\main\java\com\bank\model\*.java ^
    src\main\java\com\bank\util\*.java ^
    src\main\java\com\bank\dao\*.java ^
    src\main\java\com\bank\ui\*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo COMPILATION FAILED!
    echo.
    echo Common issues:
    echo 1. MySQL not running - Start MySQL service
    echo 2. Wrong database password - Update DatabaseConnection.java
    echo 3. Database not created - Run database_schema.sql in MySQL
    echo.
    pause
    exit /b 1
)

echo.
echo Compilation successful! Starting application...
echo.

REM Run the application
java -cp ".;lib\mysql-connector-java-8.0.33.jar" com.bank.ui.LoginWindow

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo APPLICATION FAILED TO START!
    echo.
    echo Check the error message above for details.
    echo.
    pause
)
