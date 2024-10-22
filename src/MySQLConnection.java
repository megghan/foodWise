import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/foodwisebd"; // Database URL
        String username = "yetiPequeno"; // MySQL username
        String password = "27megghan!*"; // MySQL password

        try {
            // Establish connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database successfully!");

            // Perform database operations here

            // Close the connection
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }
}