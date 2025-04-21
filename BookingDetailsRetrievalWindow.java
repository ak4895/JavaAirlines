import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class BookingDetailsRetrievalWindow extends JFrame {
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private Connection dbConnection;

    public BookingDetailsRetrievalWindow() {
        super("Booking Details");
        setSize(800, 400);

        // Replace the connection details with your MySQL database information
        String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/db";
        String username = "root";
        String password = "Aayush@1";

        try {
            dbConnection = DriverManager.getConnection(jdbcUrl, username, password);

            tableModel = new DefaultTableModel();
            tableModel.addColumn("Booking ID");
            tableModel.addColumn("Username");
            tableModel.addColumn("Flight Number");
            tableModel.addColumn("Date");
            tableModel.addColumn("Departure City");
            tableModel.addColumn("Arrival City");

            bookingTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(bookingTable);
            scrollPane.setPreferredSize(new Dimension(800, 300));
            add(scrollPane, BorderLayout.CENTER);

            retrieveBookingDetailsFromDatabase(tableModel);

            JButton updateButton = new JButton("Update");
            updateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = bookingTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        String bookingId = tableModel.getValueAt(selectedRow, 0).toString();
                        showUpdateBookingDialog(bookingId);
                    } else {
                        JOptionPane.showMessageDialog(BookingDetailsRetrievalWindow.this,
                                "Please select a booking to update.");
                    }
                }
            });

            JButton logOutButton = new JButton("Log Out");
            logOutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose(); // Close the current window
                    new LoginWindow(); // Open the login window
                }
            });

            JButton backButton = new JButton("Back");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose(); // Close the current window
                    new FlightDetailsEntryWindow(); // Open the previous window
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(updateButton);
            buttonPanel.add(logOutButton);
            buttonPanel.add(backButton);
            add(buttonPanel, BorderLayout.SOUTH);
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void retrieveBookingDetailsFromDatabase(DefaultTableModel tableModel) {
        // Clear all rows in the table before adding updated data
        int rowCount = tableModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }

        String query = "SELECT * FROM flight_bookings";

        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int bookingId = resultSet.getInt("booking_id");
                String username = resultSet.getString("username");
                String flightNumber = resultSet.getString("flight_number");
                Date bookingDate = resultSet.getDate("date");
                String departureCity = resultSet.getString("departure_city");
                String arrivalCity = resultSet.getString("arrival_city");

                Vector<String> row = new Vector<>();
                row.add(Integer.toString(bookingId));
                row.add(username);
                row.add(flightNumber);
                row.add(new SimpleDateFormat("yyyy-MM-dd").format(bookingDate));
                row.add(departureCity);
                row.add(arrivalCity);

                tableModel.addRow(row);
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving booking details from the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showUpdateBookingDialog(String bookingId) {
        JTextField newDepartureCity = new JTextField(10);
        JTextField newArrivalCity = new JTextField(10);
        JDateChooser newBookingDate = new JDateChooser();

        JPanel updatePanel = new JPanel();
        updatePanel.add(new JLabel("New Departure City:"));
        updatePanel.add(newDepartureCity);
        updatePanel.add(new JLabel("New Arrival City:"));
        updatePanel.add(newArrivalCity);
        updatePanel.add(new JLabel("New Date:"));
        updatePanel.add(newBookingDate);

        int result = JOptionPane.showConfirmDialog(null, updatePanel,
                "Update Booking Information", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String newCityDeparture = newDepartureCity.getText();
            String newCityArrival = newArrivalCity.getText();
            Date newDate = new Date(newBookingDate.getDate().getTime());

            if (updateBookingInDatabase(bookingId, newCityDeparture, newCityArrival, newDate)) {
                JOptionPane.showMessageDialog(BookingDetailsRetrievalWindow.this, "Booking updated successfully.");
                clearTable();
                retrieveBookingDetailsFromDatabase(tableModel);
            } else {
                JOptionPane.showMessageDialog(BookingDetailsRetrievalWindow.this, "Error updating booking.");
            }
        }
    }

    private boolean updateBookingInDatabase(String bookingId, String newCityDeparture, String newCityArrival,
            Date newDate) {
        // Check if the updated data matches the data in the 'flights' table
        String flightQuery = "SELECT * FROM flights WHERE departure_city = ? AND arrival_city = ?";
        try {
            PreparedStatement flightStatement = dbConnection.prepareStatement(flightQuery);
            flightStatement.setString(1, newCityDeparture);
            flightStatement.setString(2, newCityArrival);
            ResultSet flightResult = flightStatement.executeQuery();

            if (!flightResult.next()) {
                // The combination of departure and arrival cities does not exist in the
                // 'flights' table
                JOptionPane.showMessageDialog(BookingDetailsRetrievalWindow.this, "Invalid departure/arrival cities.");
                return false;
            }

            // Proceed with the update
            String updateQuery = "UPDATE flight_bookings SET departure_city = ?, arrival_city = ?, date = ? WHERE booking_id = ?";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(updateQuery);
            preparedStatement.setString(1, newCityDeparture);
            preparedStatement.setString(2, newCityArrival);
            preparedStatement.setDate(3, newDate);
            preparedStatement.setString(4, bookingId);

            int rowsUpdated = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating booking in the database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void clearTable() {
        int rowCount = tableModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookingDetailsRetrievalWindow().setVisible(true));
    }
}
