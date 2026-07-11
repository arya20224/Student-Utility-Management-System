package org.example;
import java.sql.*;
import java.util.Scanner;

public class StudentUtilityManagementSystem {

    // UPDATE THESE CREDENTIALS BASED ON YOUR LOCAL MYSQL SETUP
    private static final String URL = "jdbc:mysql://localhost:3306/student_utility_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        // Test database connection on startup
        try (Connection conn = getConnection()) {
            System.out.println("====== SYSTEM ONLINE: Connected to MySQL Successfully ======");
        } catch (SQLException e) {
            System.err.println("CRITICAL ERROR: Could not connect to MySQL. Check URL/Credentials.");
            e.printStackTrace();
            return;
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- STUDENT UTILITY MANAGEMENT SYSTEM ---");
            System.out.println("1. Add New Student Record");
            System.out.println("2. View All Student Records");
            System.out.println("3. Update Attendance & Marks");
            System.out.println("4. Delete Student Record");
            System.out.println("5. Exit System");
            System.out.print("Enter your choice (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    addStudent(scanner);
                    break;
                case 2:
                    viewStudents();
                    break;
                case 3:
                    updateStudentPerformance(scanner);
                    break;
                case 4:
                    deleteStudent(scanner);
                    break;
                case 5:
                    System.out.println("Shutting down Student Utility System. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Please select between 1 and 5.");
            }
        }
    }

    // 1. DATABASE CONNECTION PROVIDER
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 2. CREATE OPERATION (Add Student)
    private static void addStudent(Scanner scanner) {
        System.out.print("Enter Roll Number (Unique): ");
        String roll = scanner.nextLine();
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email Address: ");
        String email = scanner.nextLine();

        // Using PreparedStatement to prevent SQL Injection
        String query = "INSERT INTO students (roll_number, name, email) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, roll);
            pstmt.setString(2, name);
            pstmt.setString(3, email);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("SUCCESS: Student profile created successfully!");
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Failed to add student. Roll number might already exist.");
            e.printStackTrace();
        }
    }

    // 3. READ OPERATION (View All Students)
    private static void viewStudents() {
        String query = "SELECT * FROM students";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n------------------------------------------------------------------------------------");
            System.out.printf("%-5s | %-12s | %-20s | %-25s | %-10s | %-5s\n", "ID", "Roll No", "Name", "Email", "Attendance", "Marks");
            System.out.println("------------------------------------------------------------------------------------");

            boolean hasRecords = false;
            while (rs.next()) {
                hasRecords = true;
                System.out.printf("%-5d | %-12s | %-20s | %-25s | %-10.2f | %-5d\n",
                        rs.getInt("id"),
                        rs.getString("roll_number"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getDouble("attendance_percentage"),
                        rs.getInt("marks_obtained")
                );
            }
            if (!hasRecords) {
                System.out.println("No records found in the database.");
            }
            System.out.println("------------------------------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("ERROR: Failed to fetch student data.");
            e.printStackTrace();
        }
    }

    // 4. UPDATE OPERATION (Modify Marks and Attendance)
    private static void updateStudentPerformance(Scanner scanner) {
        System.out.print("Enter the Roll Number of the student to update: ");
        String roll = scanner.nextLine();
        System.out.print("Enter New Attendance Percentage (e.g., 85.5): ");
        double attendance = scanner.nextDouble();
        System.out.print("Enter New Marks Obtained (out of 100): ");
        int marks = scanner.nextInt();

        String query = "UPDATE students SET attendance_percentage = ?, marks_obtained = ? WHERE roll_number = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, attendance);
            pstmt.setInt(2, marks);
            pstmt.setString(3, roll);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("SUCCESS: Student performance parameters updated!");
            } else {
                System.out.println("WARNING: No student found with Roll Number: " + roll);
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Failed to update records.");
            e.printStackTrace();
        }
    }

    // 5. DELETE OPERATION (Remove Student)
    private static void deleteStudent(Scanner scanner) {
        System.out.print("Enter the Roll Number of the student to delete: ");
        String roll = scanner.nextLine();

        String query = "DELETE FROM students WHERE roll_number = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, roll);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("SUCCESS: Student record purged from database.");
            } else {
                System.out.println("WARNING: No student found with Roll Number: " + roll);
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Failed to delete data.");
            e.printStackTrace();
        }
    }
}
