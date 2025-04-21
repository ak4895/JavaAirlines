import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class ForgotPasswordWindow extends JFrame {

    private Connection connection;

    public ForgotPasswordWindow() {
        
        super("Java Airlines - Forgot Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        
        // Use the DatabaseConnector to get the connection
        connection = DatabaseConnector.getConnection();

        JPanel forgotPasswordPanel = new JPanel();
        forgotPasswordPanel.setLayout(new GridLayout(4, 2, 5, 5));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        forgotPasswordPanel.add(usernameLabel);
        forgotPasswordPanel.add(usernameField);
        forgotPasswordPanel.add(emailLabel);
        forgotPasswordPanel.add(emailField);
        Color tranparentColor = new Color(255,255,255,100);

       forgotPasswordPanel.setBackground(tranparentColor);

        JButton resetPasswordButton = new JButton("Reset Password");
        resetPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String email = emailField.getText();

                // Verify username and email against the database
                try {
                    PreparedStatement verifyStatement = connection.prepareStatement(
                            "SELECT * FROM users WHERE username = ? AND email = ?");
                    verifyStatement.setString(1, username);
                    verifyStatement.setString(2, email);
                    ResultSet resultSet = verifyStatement.executeQuery();

                    if (resultSet.next()) {
                        // Username and email match, allow changing password
                        String newPassword = JOptionPane.showInputDialog(null, "Enter your new password:");
                        if (newPassword != null && !newPassword.isEmpty()) {
                            // Update password in the database
                            PreparedStatement updatePasswordStatement = connection.prepareStatement(
                                    "UPDATE users SET password = ? WHERE username = ?");
                            updatePasswordStatement.setString(1, newPassword);
                            updatePasswordStatement.setString(2, username);
                            updatePasswordStatement.executeUpdate();

                            JOptionPane.showMessageDialog(null, "Password updated successfully", "Password Reset", JOptionPane.INFORMATION_MESSAGE);
                            dispose(); // Close the forgot password window
                        } else {
                            JOptionPane.showMessageDialog(null, "Please enter a valid password", "Password Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Username or email does not match", "Verification Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        resetPasswordButton.setBackground(tranparentColor);

        forgotPasswordPanel.add(resetPasswordButton);

        setBackgroundImage("C:\\Users\\kumat\\Downloads\\ai.jpg");

        add(forgotPasswordPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void setBackgroundImage(String imagePath) {
        ImageIcon background = new ImageIcon(imagePath);
        Image scaledImage = background.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        setContentPane(new JLabel(new ImageIcon(scaledImage)));
        setLayout(new FlowLayout());
    }
}