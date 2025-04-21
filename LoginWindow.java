import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginWindow extends JFrame {

    private Connection connection;
    private String loggedInUsername; 

    public LoginWindow() {
        
        super("Java Airlines - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        // Use the DatabaseConnector to get the connection
        connection = DatabaseConnector.getConnection();

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(4, 2, 5, 5));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        Color transparentColor = new Color(255, 255, 255, 100);

        loginPanel.setBackground(transparentColor);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Verify login credentials against the database
                try {
                    PreparedStatement loginStatement = connection.prepareStatement(
                            "SELECT * FROM users WHERE username = ? AND password = ?");
                    loginStatement.setString(1, username);
                    loginStatement.setString(2, password);

                    if (username.equals("admin") && password.equals("admin")) {
                        // Admin login successful
                        dispose(); // Close the login window
                        // Open a new UI for entering flight details
                        new FlightDetailsEntryWindow();
                    } else {
                        ResultSet resultSet = loginStatement.executeQuery();
                        if (resultSet.next()) {
                            loggedInUsername = username;
                            // Login successful
                            dispose(); // Close the login window
                          new MainMenuWindow(loggedInUsername); // Open the main menu window
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateAccountWindow();
            }
        });
        createAccountButton.setBackground(transparentColor);

        JButton forgotPasswordButton = new JButton("Forgot Password");
        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ForgotPasswordWindow();
            }
        });
        forgotPasswordButton.setBackground(transparentColor);
        loginButton.setBackground(transparentColor);
        loginPanel.add(loginButton);
        loginPanel.add(createAccountButton);
        loginPanel.add(forgotPasswordButton);

        // Set background image
        setBackgroundImage("C:\\Users\\kumat\\Downloads\\ai.jpg");

        add(loginPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void setBackgroundImage(String imagePath) {
        ImageIcon background = new ImageIcon(imagePath);
        Image scaledImage = background.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        setContentPane(new JLabel(new ImageIcon(scaledImage)));
        setLayout(new FlowLayout());
    }
    public String getLoggedInUsername() {
        return loggedInUsername;
    }
}
