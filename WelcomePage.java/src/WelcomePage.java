import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePage {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Welcome Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        frame.add(panel);

        // Create a welcome label
        JLabel welcomeLabel = new JLabel("Welcome to our application!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(welcomeLabel);

        // Create a "Let's Start" button
        JButton startButton = new JButton("Let's Start");
        panel.add(startButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the welcome page
                openLoginPage();
            }
        });

        frame.setVisible(true);
    }

    // Method to open the login page
    public static void openLoginPage() {
        JFrame loginFrame = new JFrame("Login Page");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 200);

        JPanel loginPanel = new JPanel();
        loginFrame.add(loginPanel);

        // Add login components: username, password fields, and login button
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.equals("admin") && password.equals("admin")) {
                    loginFrame.dispose(); // Close the login page
                    openMainMenu();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid credentials. Try again.");
                }
            }
        });

        loginFrame.setVisible(true);
    }

    // Method to open the main menu page
    public static void openMainMenu() {
        JFrame mainMenuFrame = new JFrame("Main Menu");
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setSize(400, 300);

        JPanel mainMenuPanel = new JPanel();
        mainMenuFrame.add(mainMenuPanel);

        // Add UI components for owner details and vehicle information

        // Owner Details UI
        JLabel ownerNameLabel = new JLabel("Owner Name:");
        JTextField ownerNameField = new JTextField(20);

        // Vehicle Type UI
        JLabel vehicleTypeLabel = new JLabel("Vehicle Type:");
        String[] vehicleTypes = {"Two Wheeler", "Four Wheeler"};
        JComboBox<String> vehicleTypeComboBox = new JComboBox<>(vehicleTypes);

        // Vehicle Number UI
        JLabel vehicleNumberLabel = new JLabel("Vehicle Number:");
        JTextField vehicleNumberField = new JTextField(20);

        // Display Message UI
        JButton displayButton = new JButton("Display");

        mainMenuPanel.add(ownerNameLabel);
        mainMenuPanel.add(ownerNameField);
        mainMenuPanel.add(vehicleTypeLabel);
        mainMenuPanel.add(vehicleTypeComboBox);
        mainMenuPanel.add(vehicleNumberLabel);
        mainMenuPanel.add(vehicleNumberField);
        mainMenuPanel.add(displayButton);

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ownerName = ownerNameField.getText();
                String vehicleType = (String) vehicleTypeComboBox.getSelectedItem();
                String vehicleNumber = vehicleNumberField.getText();

                String message = "Owner Name: " + ownerName + "\nVehicle Type: " + vehicleType + "\nVehicle Number: " + vehicleNumber + "\nis Insured";

                JOptionPane.showMessageDialog(mainMenuFrame, message);
            }
        });

        mainMenuFrame.setVisible(true);
    }
}