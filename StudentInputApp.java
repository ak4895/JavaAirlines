import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentInputApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Set up the main frame
            JFrame frame = new JFrame("Student Input Form");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLayout(new GridLayout(4, 2)); // Use GridLayout for simplicity

            // Add components
            frame.add(new JLabel("Name:"));
            JTextField nameField = new JTextField();
            frame.add(nameField);

            frame.add(new JLabel("Age:"));
            JTextField ageField = new JTextField();
            frame.add(ageField);

            frame.add(new JLabel("Grade:"));
            JTextField gradeField = new JTextField();
            frame.add(gradeField);

            JButton submitButton = new JButton("Submit");
            frame.add(submitButton);

            // Add an empty label to fill the last cell of the grid
            frame.add(new JLabel());

            // Add action listener to the button
            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Display the message dialog when the button is clicked
                    JOptionPane.showMessageDialog(frame, "bhak bhsdk");
                }
            });

            // Make the frame visible
            frame.setVisible(true);
        });
    }
}
