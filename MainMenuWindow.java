import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MainMenuWindow extends JFrame {

    private String loggedInUsername;
    private Connection connection; // You'll need a database connection

    public MainMenuWindow(String username) {
        loggedInUsername = username;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Initialize your database connection here, if not already done
        connection = DatabaseConnector.getConnection(); // Adjust this line

        // Create a panel with a background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("C:/Users/kumat/Downloads/ai.jpg");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridLayout(4, 1, 5, 5));

        // Create a custom font for the button text
        Font buttonFont = new Font("Arial", Font.BOLD, 24);

        // Create a smaller button size
        Dimension buttonSize = new Dimension(150, 60);

        JButton bookFlightButton = new JButton("Book Flight");
        bookFlightButton.setPreferredSize(buttonSize);
        bookFlightButton.setOpaque(true); // Make the button transparent
        bookFlightButton.setContentAreaFilled(false); // Make the button content area transparent
        bookFlightButton.setBorderPainted(true); // Remove button border
        bookFlightButton.setFont(buttonFont); // Set the custom font
        bookFlightButton.setForeground(Color.BLACK); // Set the text color
        bookFlightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a new window for flight booking
                new FlightBookingWindow();
            }
        });
        backgroundPanel.add(bookFlightButton);

        JButton manageBookingsButton = new JButton("Manage Bookings");
        manageBookingsButton.setPreferredSize(buttonSize);
        manageBookingsButton.setOpaque(true); // Make the button transparent
        manageBookingsButton.setContentAreaFilled(false); // Make the button content area transparent
        manageBookingsButton.setBorderPainted(true); // Remove button border
        manageBookingsButton.setFont(buttonFont); // Set the custom font
        manageBookingsButton.setForeground(Color.BLACK); // Set the text color
        manageBookingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BookingDetailsWindow(loggedInUsername, connection);
            }
        });
        backgroundPanel.add(manageBookingsButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(buttonSize);
        logoutButton.setOpaque(true); // Make the button transparent
        logoutButton.setContentAreaFilled(false); // Make the button content area transparent
        logoutButton.setBorderPainted(true); // Remove button border
        logoutButton.setFont(buttonFont); // Set the custom font
        logoutButton.setForeground(Color.BLACK); // Set the text color
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the main menu window
                new LoginWindow(); // Open the login window
            }
        });
        backgroundPanel.add(logoutButton);

        add(backgroundPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public MainMenuWindow() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenuWindow("YourUsernameHere");
        });
    }
}
