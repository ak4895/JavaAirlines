import javax.swing.*;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class FlightBookingWindow extends JFrame {
    public FlightBookingWindow() {
        super("Book a Flight");
        setSize(400, 200);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel bookingPanel = new JPanel();
        bookingPanel.setLayout(new GridLayout(5, 2, 5, 5));

        JLabel fromLabel = new JLabel("From:");
        JTextField fromField = new JTextField();
        JLabel toLabel = new JLabel("To:");
        JTextField toField = new JTextField();
        JLabel dateLabel = new JLabel("Date:");
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");

        bookingPanel.add(fromLabel);
        bookingPanel.add(fromField);
        bookingPanel.add(toLabel);
        bookingPanel.add(toField);
        bookingPanel.add(dateLabel);
        bookingPanel.add(dateChooser);

        JButton searchFlightButton = new JButton("Search Flight");
        searchFlightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Connection connection = DatabaseConnector.getConnection();

                try {
                    Statement statement = connection.createStatement();

                    String sql = "SELECT flight_number, departure_city, arrival_city, airline, departure_time, arrival_time " +
                            "FROM flights " +
                            "WHERE departure_city = '" + fromField.getText() + "' " +
                            "AND arrival_city = '" + toField.getText() + "' ";

                    ResultSet resultSet = statement.executeQuery(sql);

                    new FlightDetailsWindow(resultSet);

                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error while searching for flights: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        bookingPanel.add(searchFlightButton);
        JButton mainMenuButton = new JButton("Main Menu");
        bookingPanel.add(mainMenuButton);
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the flight booking window
                new LoginWindow(); // Open the login window
            }
        });
        bookingPanel.add(logoutButton);
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the flight booking window
                new MainMenuWindow(); // Open the main menu window
            }
        });

        add(bookingPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}
