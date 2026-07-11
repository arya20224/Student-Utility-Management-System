# Student Utility Management System

## What it does
A console-based CRUD application for managing student academic records (roll number, name, email, attendance, marks), backed by MySQL via raw JDBC.

## How it works
- Menu-driven console interface: add / view / update / delete / exit
- All database operations use `PreparedStatement` to prevent SQL injection
- Every operation uses try-with-resources for `Connection` and `Statement` objects, so connections close automatically even if an exception occurs

## Database Setup
```sql
CREATE DATABASE student_utility_db;

USE student_utility_db;

CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    roll_number VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    attendance_percentage DOUBLE DEFAULT 0,
    marks_obtained INT DEFAULT 0
);
```

## Running Locally
1. Clone the repo, open in IntelliJ
2. Run the SQL above to set up the database and table
3. Update the `URL` / `USER` / `PASSWORD` constants at the top of `StudentUtilityManagementSystem.java` to match your local MySQL setup — don't commit real credentials
4. Run the `main` method

## Note
This was an early JDBC fundamentals project — a single class with static methods, no service/repository separation. Built before learning Spring Data JPA/Hibernate, which the two later projects in this portfolio use.
