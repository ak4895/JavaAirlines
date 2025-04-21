import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class BookingDetailsWindow1 extends JFrame {
    private JTextArea bookingDetailsArea;

    public BookingDetailsWindow1(String username, int transactionId) {
        super("Booking Details");
        setSize(400, 300);
        JPanel bookingDetailsPanel = new JPanel(new BorderLayout());

        // Create booking details area
        bookingDetailsArea = new JTextArea(10, 20);
        bookingDetailsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bookingDetailsArea);
        bookingDetailsPanel.add(scrollPane, BorderLayout.CENTER);

        add(bookingDetailsPanel, BorderLayout.CENTER);
        setVisible(true);

        // Initialize the database connection using JDBC
        String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/db"; // Modify with your database URL
        String usernameDB = "root"; // Modify with your database username
        String password = "Aayush@1"; // Modify with your database password

        try (Connection connection = DriverManager.getConnection(jdbcUrl, usernameDB, password)) {
            // Retrieve booking details from the database
            String selectQuery = "SELECT fb.booking_id, fb.date, fb.departure_city, fb.arrival_city, fb.airline, fb.flight_number, p.age, p.gender, p.name " +
                    "FROM flight_bookings fb " +
                    "JOIN transactions t ON fb.booking_id = t.booking_id " +
                    "JOIN Passenger p ON fb.booking_id = p.bid " +
                    "WHERE t.username = ? AND t.id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, transactionId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Display booking details in the text area
                String bookingDetails = "Transaction ID: " + transactionId + "\n"
                        + "Date: " + resultSet.getString("date") + "\n"
                        + "From: " + resultSet.getString("departure_city") + "\n"
                        + "To: " + resultSet.getString("arrival_city") + "\n"
                        + "Airline: " + resultSet.getString("airline") + "\n"
                        + "Username: " + username + "\n"
                        + "Flight Number: " + resultSet.getString("flight_number") + "\n"
                        + "Passenger Age: " + resultSet.getInt("age") + "\n"
                        + "Passenger Gender: " + resultSet.getString("gender") + "\n"
                        + "Passenger Name: " + resultSet.getString("name");
                bookingDetailsArea.setText(bookingDetails);
            } else {
                bookingDetailsArea.setText("No booking details found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to retrieve booking details.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookingDetailsWindow1("username", 1).setVisible(true));
    }
}
