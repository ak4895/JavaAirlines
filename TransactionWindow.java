import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TransactionWindow extends JFrame {
    private JComboBox<String> paymentMethodComboBox;
    private JTextField usernameField;
    private Connection connection;

    public TransactionWindow(String username, int bookingId, BookingWindow bookingWindow) {
        super("Transaction");
        setSize(400, 300);

        JPanel transactionPanel = new JPanel(new GridLayout(3, 2));

        // Create payment method combo box
        paymentMethodComboBox = new JComboBox<>(new String[]{"Cash", "Card", "Online"});
        transactionPanel.add(new JLabel("Payment Method:"));
        transactionPanel.add(paymentMethodComboBox);

        // Create username input field
        usernameField = new JTextField(10);
        usernameField.setText(username);
        transactionPanel.add(new JLabel("Username:"));
        transactionPanel.add(usernameField);
        usernameField.setEditable(false);

        // Create "Pay" button
        JButton payButton = new JButton("Pay");
        transactionPanel.add(payButton);

        add(transactionPanel, BorderLayout.CENTER);
        setVisible(true);

        // Initialize the database connection using JDBC
        String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/db"; // Modify with your database URL
        String usernameDB = "root"; // Modify with your database username
        String password = "Aayush@1"; // Modify with your database password

        try {
            connection = DriverManager.getConnection(jdbcUrl, usernameDB, password);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.");
        }

        // Create transactions table if not exists
        createTransactionsTable();

        // Pay button action listener
        payButton.addActionListener(e -> {
            String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();

            // Perform payment processing here
            // For demonstration purposes, just show a message
            JOptionPane.showMessageDialog(null, "Payment processed for " + paymentMethod + " by " + username);

            // Insert transaction into transactions table
            int transactionId = insertTransaction(username, paymentMethod, bookingId);
            if (transactionId != -1) {
                // Open the booking details window
                new BookingDetailsWindow1(username, transactionId).setVisible(true);
            }
        });
    }

    private void createTransactionsTable() {
        try {
            Statement statement = connection.createStatement();
            String createTableSQL = "CREATE TABLE IF NOT EXISTS transactions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(255) NOT NULL," +
                    "payment_method VARCHAR(255) NOT NULL," +
                    "booking_id INT," +
                    "FOREIGN KEY (booking_id) REFERENCES flight_bookings(id)" +
                    ")";
            statement.executeUpdate(createTableSQL);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to create transactions table.");
        }
    }

    private int insertTransaction(String username, String paymentMethod, int bookingId) {
        try {
            String insertQuery = "INSERT INTO transactions (username, payment_method, booking_id) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, paymentMethod);
            preparedStatement.setInt(3, bookingId);
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated transaction ID
                }
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to insert transaction: " + e.getMessage());
        }
        return -1; // Return -1 if insertion fails
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TransactionWindow("username", 1, null).setVisible(true));
    }
}
