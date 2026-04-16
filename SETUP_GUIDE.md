# Bank Management System - Complete Setup Guide

## Step-by-Step Installation and Running Instructions

### Prerequisites
1. **Java Development Kit (JDK) 11+** - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or use OpenJDK
2. **MySQL Server 8.0+** - Download from [MySQL](https://dev.mysql.com/downloads/mysql/)
3. **Maven 3.6+** (Optional, for easier dependency management) - Download from [Maven](https://maven.apache.org/download.cgi)

### Step 1: Database Setup

1. **Install MySQL Server**
   - Follow the installation wizard for your operating system
   - Set a root password (remember it for later)
   - Start the MySQL service

2. **Create Database and Import Schema**
   ```sql
   -- Open MySQL Command Line Client or MySQL Workbench
   -- Enter your root password when prompted
   
   -- Run the database schema
   SOURCE path/to/bank-management-system/database_schema.sql;
   ```
   
   Or using command line:
   ```bash
   mysql -u root -p < database_schema.sql
   ```

3. **Verify Database Creation**
   ```sql
   USE bank_management_system;
   SHOW TABLES;
   -- You should see: accounts, customers, transactions, users
   ```

### Step 2: Configure Database Connection

Edit the database connection settings in:
`src/main/java/com/bank/util/DatabaseConnection.java`

```java
private static final String URL = "jdbc:mysql://localhost:3306/bank_management_system";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_mysql_password_here"; // Update this!
```

### Step 3: Build and Run the Application

#### Option A: Using Maven (Recommended)

1. **Navigate to project directory**
   ```bash
   cd bank-management-system
   ```

2. **Build the project**
   ```bash
   mvn clean package
   ```

3. **Run the application**
   ```bash
   java -jar target/bank-management-system-executable.jar
   ```

#### Option B: Manual Compilation (Without Maven)

1. **Download MySQL Connector**
   - Download from: [MySQL Connector JAR](https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar)
   - Save it in the `lib` folder

2. **Compile the application**
   ```bash
   javac -cp lib/mysql-connector-java-8.0.33.jar -d . src/main/java/com/bank/util/*.java src/main/java/com/bank/dao/*.java src/main/java/com/bank/ui/*.java
   ```

3. **Run the application**
   ```bash
   java -cp .;lib/mysql-connector-java-8.0.33.jar com.bank.ui.LoginWindow
   ```

#### Option C: Using IDE (Eclipse/IntelliJ)

1. **Import the project**
   - File > Import > Maven Project
   - Select the bank-management-system folder

2. **Add MySQL Connector**
   - Right-click project > Build Path > Configure Build Path
   - Add External JARs > Select mysql-connector-java-8.0.33.jar

3. **Run the application**
   - Right-click `LoginWindow.java` > Run As > Java Application

### Step 4: Login and Test

1. **Default Login Credentials**
   - Username: `admin`
   - Password: `admin123`

2. **Test the Application**
   - Explore the dashboard
   - Try adding a new customer
   - Create an account for the customer
   - Process a deposit or withdrawal
   - View transaction history

### Step 5: Troubleshooting Common Issues

#### Database Connection Issues
```
Error: Database connection failed
```
**Solutions:**
- Check if MySQL server is running
- Verify username and password in DatabaseConnection.java
- Ensure database `bank_management_system` exists
- Check if MySQL port (3306) is accessible

#### ClassNotFoundException
```
Error: ClassNotFoundException: com.mysql.cj.jdbc.Driver
```
**Solutions:**
- Ensure MySQL connector JAR is in classpath
- Verify JAR file is not corrupted
- Check Maven dependencies in pom.xml

#### Compilation Errors
```
Error: package com.mysql.cj.jdbc does not exist
```
**Solutions:**
- Add MySQL connector to build path
- Use Maven to manage dependencies
- Check import statements in DatabaseConnection.java

#### Login Failed
```
Error: Invalid username or password
```
**Solutions:**
- Check if default user exists in database
- Run: `SELECT * FROM users;` in MySQL
- Reset admin password if needed

### Step 6: Advanced Configuration

#### Changing Default Login
```sql
UPDATE users SET password = 'new_password' WHERE username = 'admin';
```

#### Adding New Users
```sql
INSERT INTO users (username, password, role) VALUES ('employee1', 'emp123', 'EMPLOYEE');
```

#### Database Backup
```bash
mysqldump -u root -p bank_management_system > backup.sql
```

#### Database Restore
```bash
mysql -u root -p bank_management_system < backup.sql
```

## Project Structure Overview

```
bank-management-system/
|
|-- src/main/java/com/bank/
|   |-- model/                 # Data Models
|   |   |-- Customer.java
|   |   |-- Account.java
|   |   |-- Transaction.java
|   |
|   |-- dao/                   # Data Access Objects
|   |   |-- CustomerDAO.java
|   |   |-- AccountDAO.java
|   |   |-- TransactionDAO.java
|   |   |-- UserDAO.java
|   |
|   |-- ui/                    # User Interface
|   |   |-- LoginWindow.java
|   |   |-- DashboardWindow.java
|   |   |-- CustomerManagementWindow.java
|   |   |-- AccountManagementWindow.java
|   |   |-- TransactionManagementWindow.java
|   |
|   |-- util/                  # Utilities
|       |-- DatabaseConnection.java
|
|-- lib/                       # External JAR files
|-- pom.xml                    # Maven configuration
|-- database_schema.sql        # Database structure
|-- README.md                  # Project documentation
|-- SETUP_GUIDE.md             # This setup guide
|-- run.bat                    # Quick run script (Windows)
|-- compile_test.bat           # Compilation test script
```

## Features Demonstrated

1. **Authentication System**
   - Secure login with password verification
   - Role-based access control

2. **Customer Management**
   - CRUD operations for customers
   - Search functionality
   - Data validation

3. **Account Management**
   - Multiple account types (Savings, Current, Fixed)
   - Account status management
   - Balance tracking

4. **Transaction Processing**
   - Deposits and withdrawals
   - Fund transfers between accounts
   - Transaction history

5. **Dashboard Analytics**
   - Real-time statistics
   - Recent transactions overview
   - System health monitoring

## Development Tips

### Adding New Features
1. Follow the existing package structure
2. Use DAO pattern for database operations
3. Implement proper error handling
4. Add input validation for user inputs
5. Follow Java naming conventions

### Database Best Practices
1. Use prepared statements to prevent SQL injection
2. Implement proper transaction management
3. Add foreign key constraints
4. Use appropriate data types
5. Index frequently queried columns

### UI Best Practices
1. Use consistent styling across windows
2. Provide clear error messages
3. Implement proper event handling
4. Add keyboard shortcuts where appropriate
5. Ensure responsive design

## Support

For additional help:
1. Check the troubleshooting section above
2. Review the README.md file
3. Examine the code comments
4. Test with the provided sample data

## Next Steps

After successful setup, you can:
1. Customize the UI with your preferred styling
2. Add additional features like reports and analytics
3. Implement user role management
4. Add data export/import functionality
5. Enhance security with password encryption

Enjoy using your Bank Management System!
