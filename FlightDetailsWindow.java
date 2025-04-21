import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FlightDetailsWindow extends JFrame {

    public FlightDetailsWindow(ResultSet resultSet) {

        super("Flight Details");
        setSize(600, 300);
setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(0, 1, 10, 10));

        try {
            while (resultSet.next()) {
                JLabel flightLabel = new JLabel("Flight number: " + resultSet.getString("flight_number"));
                JLabel departureLabel = new JLabel("Departure city: " + resultSet.getString("departure_city"));
                JLabel arrivalLabel = new JLabel("Arrival city: " + resultSet.getString("arrival_city"));
                JLabel airlineLabel = new JLabel("Airline: " + resultSet.getString("airline"));
                JLabel departureTimeLabel = new JLabel("Departure time: " + resultSet.getString("departure_time"));
                JLabel arrivalTimeLabel = new JLabel("Arrival time: " + resultSet.getString("arrival_time"));

                detailsPanel.add(flightLabel);
                detailsPanel.add(departureLabel);
                detailsPanel.add(arrivalLabel);
                detailsPanel.add(airlineLabel);
                detailsPanel.add(departureTimeLabel);
                detailsPanel.add(arrivalTimeLabel);
                detailsPanel.add(new JLabel("")); // Add an empty label for spacing
            }
        } catch (SQLException e) {
            System.err.println("Error while processing flight details: " + e.getMessage());
            e.printStackTrace();
        }

        // Create a "Book Flight" button
        JButton bookFlightButton = new JButton("Book Flight");
        bookFlightButton.addActionListener(e -> openBookingWindow());
        JButton logoutButton = new JButton("Log Out");
        detailsPanel.add(logoutButton);

        // Add a "Main Menu" button
        JButton mainMenuButton = new JButton("Main Menu");
        detailsPanel.add(mainMenuButton);

        logoutButton.addActionListener(e -> {
            // Perform the logout action, e.g., close the current window and open the login window
            dispose(); // Close the flight details window
            new LoginWindow(); // Open the login window
        });

        mainMenuButton.addActionListener(e -> {
            // Perform action to navigate back to the main menu window
            dispose(); // Close the flight details window
            new MainMenuWindow(); // Open the main menu window
        });

        add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
        add(bookFlightButton, BorderLayout.SOUTH); // Add the "Book Flight" button to the bottom of the frame
        setVisible(true);
    }

    private void openBookingWindow() {
        // You need to create a BookingWindow without passing the selected date.
        BookingWindow bookingWindow = new BookingWindow();
        bookingWindow.setVisible(true);
    }
}
