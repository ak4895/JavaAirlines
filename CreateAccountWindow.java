import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class CreateAccountWindow extends JFrame {
    

    private Connection connection;

    public CreateAccountWindow() {
        
        super("Java Airlines - Create Account");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        
        // Connect to the database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/db", "root", "Aayush@1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel createAccountPanel = new JPanel();
        createAccountPanel.setLayout(new GridLayout(5, 2, 5, 5));

        // Your existing components
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
          
        Color tranparentColor = new Color(255,255,255,100);

       createAccountPanel.setBackground(tranparentColor);
    
        createAccountPanel.add(usernameLabel);
        createAccountPanel.add(usernameField);
        createAccountPanel.add(passwordLabel);
        createAccountPanel.add(passwordField);
        createAccountPanel.add(nameLabel);
        createAccountPanel.add(nameField);
        createAccountPanel.add(emailLabel);
        createAccountPanel.add(emailField);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUsername = usernameField.getText();
                String newPassword = new String(passwordField.getPassword());
                String newName = nameField.getText();
                String newEmail = emailField.getText();

                // Check for empty fields
                if (newUsername.isEmpty() || newPassword.isEmpty() || newName.isEmpty() || newEmail.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields", "Missing Information", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Your existing code to insert new user data into the database
                try {
                    PreparedStatement insertStatement = connection.prepareStatement(
                            "INSERT INTO users (username, password, name, email) VALUES (?, ?, ?, ?)");
                    insertStatement.setString(1, newUsername);
                    insertStatement.setString(2, newPassword);
                    insertStatement.setString(3, newName);
                    insertStatement.setString(4, newEmail);
                    insertStatement.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                JOptionPane.showMessageDialog(null, "Account created successfully", "Account Creation", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Close the create account window
            }
        });
        createAccountButton.setBackground(tranparentColor);

        createAccountPanel.add(createAccountButton);

        // Set background image
        setBackgroundImage("C:\\Users\\kumat\\Downloads\\ai.jpg");

        add(createAccountPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void setBackgroundImage(String imagePath) {
        ImageIcon background = new ImageIcon(imagePath);
        Image scaledImage = background.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        setContentPane(new JLabel(new ImageIcon(scaledImage)));
        setLayout(new FlowLayout());
    }

    public static void main(String[] args) {
        new CreateAccountWindow();
    }
}