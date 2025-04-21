import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FlightDetailsEntryWindow extends JFrame {
    private JTextField flightNumberField;
    private JTextField airlineField;
    private JTextField fromField;
    private JTextField toField;
    private JSpinner departureTimeSpinner;
    private JSpinner arrivalTimeSpinner;
    private DefaultListModel<FlightDetails> flightListModel;
    private JList<FlightDetails> flightList;
    private DefaultListModel<UserDetails> userListModel;
private JList<UserDetails> userList;
private JScrollPane userScrollPane;

private DefaultListModel<BookingDetails> bookingListModel;
private JList<BookingDetails> bookingList;
private JScrollPane bookingScrollPane;
    private Connection dbConnection;

    public FlightDetailsEntryWindow() {

        super("Flight Details Management");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);

        dbConnection = DatabaseConnector.getConnection();

        JPanel flightDetailsPanel = new JPanel();
        flightDetailsPanel.setLayout(new GridLayout(6, 2, 5, 5));

        JLabel flightNumberLabel = new JLabel("Flight Number:");
        flightNumberField = new JTextField();
        JLabel airlineLabel = new JLabel("Airline:");
        airlineField = new JTextField();
        JLabel fromLabel = new JLabel("From:");
        fromField = new JTextField();
        JLabel toLabel = new JLabel("To:");
        toField = new JTextField();
        JLabel departureTimeLabel = new JLabel("Departure Time:");
        departureTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor departureEditor = new JSpinner.DateEditor(departureTimeSpinner, "HH:mm");
        departureTimeSpinner.setEditor(departureEditor);
        JLabel arrivalTimeLabel = new JLabel("Arrival Time:");
        arrivalTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor arrivalEditor = new JSpinner.DateEditor(arrivalTimeSpinner, "HH:mm");
        arrivalTimeSpinner.setEditor(arrivalEditor);

        flightDetailsPanel.add(flightNumberLabel);
        flightDetailsPanel.add(flightNumberField);
        flightDetailsPanel.add(airlineLabel);
        flightDetailsPanel.add(airlineField);
        flightDetailsPanel.add(fromLabel);
        flightDetailsPanel.add(fromField);
        flightDetailsPanel.add(toLabel);
        flightDetailsPanel.add(toField);
        flightDetailsPanel.add(departureTimeLabel);
        flightDetailsPanel.add(departureTimeSpinner);
        flightDetailsPanel.add(arrivalTimeLabel);
        flightDetailsPanel.add(arrivalTimeSpinner);
       
        
        // Create a panel for the new buttons and the text area
        // Define new components for user and booking information


// ...

// In your constructor:



        JButton saveButton = new JButton("Save Flight Details");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFlightDetails();
            }
        });

        JButton viewAllFlightsButton = new JButton("View All Flights");
        viewAllFlightsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAllFlightDetails();
            }
        });
        JButton viewAllUsersButton = new JButton("View All Users");
viewAllUsersButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        viewAllUsers();
    }
});
flightDetailsPanel.add(viewAllUsersButton);

// Button to view all bookings
JButton viewAllBookingsButton = new JButton("View All Bookings");
viewAllBookingsButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        viewAllBookings();
    }
});
flightDetailsPanel.add(viewAllBookingsButton);
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the main menu window
                new LoginWindow(); // Open the login window
            }
        });
        JButton viewAllTransactionsButton = new JButton("View All Transactions");
viewAllTransactionsButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        viewAllTransactions();
    }
});
// Add a button for passenger and staff information
JButton passengerStaffButton = new JButton("Passenger & Staff Information");
passengerStaffButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Open the new UI for passenger and staff information
        PassengerStaffInformationWindow passengerStaffInformationWindow = new PassengerStaffInformationWindow();
        passengerStaffInformationWindow.setVisible(true);
    }
});
flightDetailsPanel.add(passengerStaffButton);

flightDetailsPanel.add(viewAllTransactionsButton);
        flightDetailsPanel.add(logoutButton);

        flightDetailsPanel.add(saveButton);
        flightDetailsPanel.add(viewAllFlightsButton);
userListModel = new DefaultListModel<>();
userList = new JList<>(userListModel);
userScrollPane = new JScrollPane(userList);
userScrollPane.setPreferredSize(new Dimension(400, 200));

bookingListModel = new DefaultListModel<>();
bookingList = new JList<>(bookingListModel);
bookingScrollPane = new JScrollPane(bookingList);
bookingScrollPane.setPreferredSize(new Dimension(400, 200));
        flightListModel = new DefaultListModel<>();
        flightList = new JList<>(flightListModel);
        JScrollPane flightListScrollPane = new JScrollPane(flightList);
        flightList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        flightList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                FlightDetails selectedFlight = flightList.getSelectedValue();
                if (selectedFlight != null) {
                    showUpdateAndDeleteOptions(selectedFlight);
                }
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(flightDetailsPanel, BorderLayout.WEST);
        mainPanel.add(flightListScrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private void saveFlightDetails() {
        String flightNumber = flightNumberField.getText();
        String airline = airlineField.getText();
        String from = fromField.getText();
        String to = toField.getText();
        Date departureTime = (Date) departureTimeSpinner.getValue();
        Date arrivalTime = (Date) arrivalTimeSpinner.getValue();

        FlightDetails newFlight = new FlightDetails(flightNumber, airline, from, to, departureTime, arrivalTime);
        flightListModel.addElement(newFlight);

        saveToDatabase(newFlight);

        flightNumberField.setText("");
        airlineField.setText("");
        fromField.setText("");
        toField.setText("");
        departureTimeSpinner.setValue(new Date());
        arrivalTimeSpinner.setValue(new Date());
    }
    private void viewAllUsers() {
        ArrayList<UserDetails> users = retrieveUsersFromDatabase();
        userListModel.clear();
        users.forEach(userListModel::addElement);

        // Create and display the UserDetailsWindow
        UserDetailsWindow userDetailsWindow = new UserDetailsWindow(userListModel);
        userDetailsWindow.setVisible(true);
    }
    private void viewAllBookings() {
        // Create and display the BookingDetailsRetrievalWindow
        BookingDetailsRetrievalWindow retrievalWindow = new BookingDetailsRetrievalWindow();
        retrievalWindow.setVisible(true);
    }
    
    private void viewAllTransactions() {
        ArrayList<TransactionDetails> transactions = retrieveTransactionsFromDatabase();
        // Display the transactions in a new window or in a dialog
        // For example, you can create a new window or use JOptionPane
        // Here's a basic example using JOptionPane
        StringBuilder sb = new StringBuilder();
        transactions.forEach(t -> sb.append(t.toString()).append("\n"));
        JOptionPane.showMessageDialog(this, sb.toString(), "All Transactions", JOptionPane.INFORMATION_MESSAGE);
    }
    

    private void showUpdateAndDeleteOptions(FlightDetails selectedFlight) {
        String flightNumberInput = JOptionPane.showInputDialog(this, "Enter the Flight Number to update or delete:");

        if (flightNumberInput != null && !flightNumberInput.isEmpty()) {
            // Store the flight number entered by the user
            selectedFlight.setFlightNumber(flightNumberInput);

            int option = JOptionPane.showOptionDialog(this,
                    "Select an action for the selected flight:",
                    "Flight Options",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"Update", "Delete", "Cancel"},
                    "Update");

            if (option == 0) {
                updateFlightDetails(selectedFlight);
            } else if (option == 1) {
                deleteFlight(selectedFlight);
            }
        }
    }

    private void saveToDatabase(FlightDetails flight) {
        String query = "INSERT INTO flights (flight_number, airline, departure_city, arrival_city, departure_time, arrival_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, flight.getFlightNumber());
            preparedStatement.setString(2, flight.getAirline());
            preparedStatement.setString(3, flight.getFrom());
            preparedStatement.setString(4, flight.getTo());
            preparedStatement.setTimestamp(5, new java.sql.Timestamp(flight.getDepartureTime().getTime()));
            preparedStatement.setTimestamp(6, new java.sql.Timestamp(flight.getArrivalTime().getTime()));

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Flight details saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error saving flight details", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            System.err.println("Error saving flight details to the database: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving flight details", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateFlightDetails(FlightDetails selectedFlight) {
        SwingUtilities.invokeLater(() -> new UpdateFlightDetailsUI(selectedFlight, this));
    }

    private void deleteFlight(FlightDetails flight) {
        String query = "DELETE FROM flights WHERE flight_number = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, flight.getFlightNumber());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Flight deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                flightListModel.removeElement(flight);
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting flight", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting flight from the database: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting flight", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewAllFlightDetails() {
        ArrayList<FlightDetails> flights = retrieveFlightsFromDatabase();
        flightListModel.clear();
        flights.forEach(flightListModel::addElement);
    }
    

    public boolean updateFlightInDatabase(FlightDetails flight) {
        String query = "UPDATE flights SET flight_number=?, airline=?, departure_city=?, arrival_city=?, departure_time=?, arrival_time=? WHERE flight_number = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, flight.getFlightNumber());
            preparedStatement.setString(2, flight.getAirline());
            preparedStatement.setString(3, flight.getFrom());
            preparedStatement.setString(4, flight.getTo());
            preparedStatement.setTimestamp(5, new java.sql.Timestamp(flight.getDepartureTime().getTime()));
            preparedStatement.setTimestamp(6, new java.sql.Timestamp(flight.getArrivalTime().getTime()));
            preparedStatement.setString(7, flight.getFlightNumber());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating flight details in the database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void updateFlightInListModel(FlightDetails flight) {
        int index = flightListModel.indexOf(flight);
        if (index != -1) {
            flightListModel.set(index, flight);
        }
    }
    private ArrayList<UserDetails> retrieveUsersFromDatabase() {
        ArrayList<UserDetails> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (ResultSet resultSet = dbConnection.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                // Retrieve user details from the result set and create UserDetails objects.
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");

                // Create a UserDetails object and add it to the 'users' list.
                UserDetails user = new UserDetails(username, password, name, email);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    private ArrayList<TransactionDetails> retrieveTransactionsFromDatabase() {
        ArrayList<TransactionDetails> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions";
        try (ResultSet resultSet = dbConnection.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                int transactionId = resultSet.getInt("id");
                int bookingId = resultSet.getInt("booking_id");
                String username = resultSet.getString("username");
                String paymentMethod = resultSet.getString("payment_method");
                transactions.add(new TransactionDetails(transactionId, bookingId, username, paymentMethod));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transactions from the database: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }
    
    
    private ArrayList<FlightDetails> retrieveFlightsFromDatabase() {
        ArrayList<FlightDetails> flights = new ArrayList<>();
        String query = "SELECT * FROM flights";
        try (ResultSet resultSet = dbConnection.createStatement().executeQuery(query)) {
            while (resultSet.next()) {
                String flightNumber = resultSet.getString("flight_number");
                String airline = resultSet.getString("airline");
                String from = resultSet.getString("departure_city");
                String to = resultSet.getString("arrival_city");
                Date departureTime = resultSet.getTimestamp("departure_time");
                Date arrivalTime = resultSet.getTimestamp("arrival_time");
                flights.add(new FlightDetails(flightNumber, airline, from, to, departureTime, arrivalTime));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving flights from the database: " + e.getMessage());
            e.printStackTrace();
        }
        return flights;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlightDetailsEntryWindow());
    }
}
class UserDetails {
    private String username;
    private String name;
    private String email;

    public UserDetails(String username, String password, String name, String email) {
        this.username = username;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Username: " + username + ", Name: " + name + ", Email: " + email;
    }
}

class BookingDetails {
    private int bookingId;
    private String username;
    private String flightNumber;
    private String bookingDate;
    private String fromLocation;
    private String toLocation;
    private String airline;
    private String departureTime;
    private String arrivalTime;

    public BookingDetails(int bookingId, String username, String flightNumber, String bookingDate, String fromLocation, String toLocation, String airline, String departureTime, String arrivalTime) {
        this.bookingId = bookingId;
        this.username = username;
        this.flightNumber = flightNumber;
        this.bookingDate = bookingDate;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.airline = airline;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public BookingDetails(int bookingId2, String username2, String flightNumber2, String bookingDate2,
            String fromLocation2, String toLocation2, String airline2) {
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId + ", Username: " + username + ", Flight Number: " + flightNumber +
                ", Booking Date: " + bookingDate + ", From: " + fromLocation + ", To: " + toLocation +
                ", Airline: " + airline + ", Departure Time: " + departureTime + ", Arrival Time: " + arrivalTime;
    }
}
class TransactionDetails {
    private int transactionId;
    private int bookingId;
    private String username;
    private String paymentMethod;

    public TransactionDetails(int transactionId, int bookingId, String username, String paymentMethod) {
        this.transactionId = transactionId;
        this.bookingId = bookingId;
        this.username = username;
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "Transaction ID: " + transactionId + ", Booking ID: " + bookingId + ", Username: " + username + ", Payment Method: " + paymentMethod;
    }
}


class FlightDetails {
    private String flightNumber;
    private String airline;
    private String from;
    private String to;
    private Date departureTime;
    private Date arrivalTime;

    public FlightDetails(String flightNumber, String airline,String from,String to,Date departureTime,Date arrivalTime) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.from = from;
        this.to = to;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return flightNumber + " - " + airline + " - " + from + " to " + to +
                " - Departure: " + dateFormat.format(departureTime) + " - Arrival: " + dateFormat.format(arrivalTime);
    }
}
