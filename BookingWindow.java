import javax.swing.*;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class BookingWindow extends JFrame {
    private JDateChooser dateChooser;
    private JTextField fromField;
    private JTextField toField;
    private JTextField airlineField;
    private JTextField usernameField;
    private JTextField flightNumberField;
    private JTextField ageField;
    private JComboBox<String> genderComboBox;
    private JTextField nameField;
    private Connection connection;

    public BookingWindow() {
        super("Book a Flight");
        setSize(400, 400);
        JPanel bookingPanel = new JPanel(new GridLayout(10, 2));

        // Create input fields
        dateChooser = new JDateChooser();
        fromField = new JTextField(10);
        toField = new JTextField(10);
        airlineField = new JTextField(10);
        usernameField = new JTextField(10);
        flightNumberField = new JTextField(10);
        ageField = new JTextField(10);
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        nameField = new JTextField(10);

        // Add labels and input fields to the booking panel
        bookingPanel.add(new JLabel("Date:"));
        bookingPanel.add(dateChooser);
        bookingPanel.add(new JLabel("From:"));
        bookingPanel.add(fromField);
        bookingPanel.add(new JLabel("To:"));
        bookingPanel.add(toField);
        bookingPanel.add(new JLabel("Airline:"));
        bookingPanel.add(airlineField);
        bookingPanel.add(new JLabel("Username:"));
        bookingPanel.add(usernameField);
        bookingPanel.add(new JLabel("Flight Number:"));
        bookingPanel.add(flightNumberField);
        bookingPanel.add(new JLabel("Age:"));
        bookingPanel.add(ageField);
        bookingPanel.add(new JLabel("Gender:"));
        bookingPanel.add(genderComboBox);
        bookingPanel.add(new JLabel("Name:"));
        bookingPanel.add(nameField);

        // Create and add a "Book" button with an action listener
        JButton bookButton = new JButton("Book");
        bookingPanel.add(bookButton);

        // Create and add a "Main Menu" button
        JButton mainMenuButton = new JButton("Back");
        bookingPanel.add(mainMenuButton);

        // Main Menu button action listener
        mainMenuButton.addActionListener(e -> {
            // Perform action to navigate back to the main menu window
            dispose(); // Close the booking window
            new MainMenuWindow(); // Open the main menu window
        });

        bookButton.addActionListener(e -> {
            // Get the input values from the text fields and date chooser
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(dateChooser.getDate());
            String from = fromField.getText();
            String to = toField.getText();
            String airline = airlineField.getText();
            String username = usernameField.getText();
            String flightNumber = flightNumberField.getText();
            int age = Integer.parseInt(ageField.getText());
            String gender = (String) genderComboBox.getSelectedItem();
            String name = nameField.getText();

            // Perform database insertion here
            if (saveBookingToDatabase(date, from, to, airline, username, flightNumber, age, gender, name)) {
                // Retrieve the generated booking ID
                int bookingId = getGeneratedBookingId();
                JOptionPane.showMessageDialog(null, "Flight booked successfully for " + date);

                // Open the transaction window with the booking ID
                dispose(); // Close the booking window
                new TransactionWindow(username, bookingId, this).setVisible(true); // Pass bookingId to TransactionWindow
            }
        });

        add(bookingPanel, BorderLayout.CENTER);
        setVisible(true);

        // Initialize the database connection using JDBC
        String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/db"; // Modify with your database URL
        String username = "root"; // Modify with your database username
        String password = "Aayush@1"; // Modify with your database password

        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.");
        }
    }

    private boolean saveBookingToDatabase(String date, String from, String to, String airline, String username, String flightNumber, int age, String gender, String name) {
        try {
            // Prepare the SQL statement and execute the insertion for flight_booking table
            String insertQueryFlightBooking = "INSERT INTO flight_bookings (date, departure_city, arrival_city, airline, username, flight_number) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatementFlightBooking = connection.prepareStatement(insertQueryFlightBooking, Statement.RETURN_GENERATED_KEYS);
            preparedStatementFlightBooking.setString(1, date);
            preparedStatementFlightBooking.setString(2, from);
            preparedStatementFlightBooking.setString(3, to);
            preparedStatementFlightBooking.setString(4, airline);
            preparedStatementFlightBooking.setString(5, username);
            preparedStatementFlightBooking.setString(6, flightNumber);
            int rowsInsertedFlightBooking = preparedStatementFlightBooking.executeUpdate();
            preparedStatementFlightBooking.close();

            // Check if the insertion to flight_booking was successful
            if (rowsInsertedFlightBooking <= 0) {
                return false;
            }

            // Retrieve the generated booking ID
            int bookingId = getGeneratedBookingId();

            // Prepare the SQL statement and execute the insertion for Passenger table
            String insertQueryPassenger = "INSERT INTO Passenger (bid, age, gender, name) " +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatementPassenger = connection.prepareStatement(insertQueryPassenger);
            preparedStatementPassenger.setInt(1, bookingId);
            preparedStatementPassenger.setInt(2, age);
            preparedStatementPassenger.setString(3, gender);
            preparedStatementPassenger.setString(4, name);
            int rowsInsertedPassenger = preparedStatementPassenger.executeUpdate();
            preparedStatementPassenger.close();

            // Check if the insertion to Passenger was successful
            return rowsInsertedPassenger > 0;
        } catch (SQLException ex) {
            System.err.println("Error while saving booking: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    private int getGeneratedBookingId() {
        try {
            String selectQuery = "SELECT LAST_INSERT_ID() AS last_id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            if (resultSet.next()) {
                return resultSet.getInt("last_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if no booking ID is retrieved
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookingWindow().setVisible(true));
    }
}
