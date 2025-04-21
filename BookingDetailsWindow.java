import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingDetailsWindow extends JFrame {

    public BookingDetailsWindow(String username, Connection connection) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setSize(600, 400);
        setTitle("Your Booked Flights");

        JPanel bookingDetailsPanel = new JPanel();
        bookingDetailsPanel.setLayout(new GridLayout(0, 1, 5, 5));

        try {
            // Fetch and display the booking details for the provided username from the database
            PreparedStatement bookingStatement = connection.prepareStatement(
                    "SELECT * FROM flight_bookings WHERE username = ?");
            bookingStatement.setString(1, username);

            ResultSet bookingResultSet = bookingStatement.executeQuery();

            // Create a table to display the booking details
            String[] columnNames = {"Booking ID", "Flight Number", "Booking Date", "From Location", "To Location", "Airline"};
            Object[][] data = new Object[50][8]; // Assuming a maximum of 50 bookings

            int row = 0;

            while (bookingResultSet.next()) {
                data[row][0] = bookingResultSet.getInt("booking_id");
                data[row][1] = bookingResultSet.getString("flight_number");
                data[row][2] = bookingResultSet.getDate("date");
                data[row][3] = bookingResultSet.getString("departure_city");
                data[row][4] = bookingResultSet.getString("arrival_city");
                data[row][5] = bookingResultSet.getString("airline");

                row++;
            }

            JTable table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            bookingDetailsPanel.add(scrollPane);

            bookingResultSet.close();
            bookingStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Add a "Log Out" button
        JButton logoutButton = new JButton("Log Out");
        logoutButton.setPreferredSize(new Dimension(100, 30)); // Adjust the dimensions as needed
        buttonPanel.add(logoutButton);

        logoutButton.addActionListener(e -> {
            // Perform the logout action, e.g., close the current window and open the login window
            dispose();
            new LoginWindow(); // Open the login window
        });

        // Add a "Back to Main Menu" button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setPreferredSize(new Dimension(120, 30)); // Adjust the dimensions as needed
        buttonPanel.add(backButton);

        backButton.addActionListener(e -> dispose());

        // Add the button panel to the main panel
        bookingDetailsPanel.add(buttonPanel);
        add(bookingDetailsPanel);
        setVisible(true);
    }

    public BookingDetailsWindow(DefaultListModel<BookingDetails> bookingListModel) {
    }
}