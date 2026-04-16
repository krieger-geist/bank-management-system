@echo off
echo Testing Java Bank Management System compilation...

echo Compiling model classes...
javac -d . src\main\java\com\bank\model\Customer.java
if %ERRORLEVEL% NEQ 0 goto error

javac -d . src\main\java\com\bank\model\Account.java
if %ERRORLEVEL% NEQ 0 goto error

javac -d . src\main\java\com\bank\model\Transaction.java
if %ERRORLEVEL% NEQ 0 goto error

echo Model classes compiled successfully!

echo.
echo NOTE: To compile the full application with database connectivity:
echo 1. Download MySQL Connector JAR from: https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar
echo 2. Place it in the lib directory
echo 3. Run: javac -cp lib\mysql-connector-java-8.0.33.jar -d . src\main\java\com\bank\util\*.java src\main\java\com\bank\dao\*.java src\main\java\com\bank\ui\*.java
echo 4. Run: java -cp .;lib\mysql-connector-java-8.0.33.jar com.bank.ui.LoginWindow
echo.
goto end

:error
echo Compilation failed! Please check the error messages above.

:end
pause
