import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class PassengerStaffInformationWindow extends JFrame {
    private Connection dbConnection;

    public PassengerStaffInformationWindow() {
        super("Passenger & Staff Information");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);

        dbConnection = DatabaseConnector.getConnection();

        JPanel panel = new JPanel(new GridLayout(4, 1));

        // Create components for staff information
        JPanel staffPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        staffPanel.setBorder(BorderFactory.createTitledBorder("Staff Information"));

        JTextField staffIdField = new JTextField();
        JTextField staffNameField = new JTextField();
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        JTextField designationField = new JTextField();
        JTextField deptField = new JTextField();
        JTextField deptIdField = new JTextField();
        JTextField salaryField = new JTextField();
        JTextField ageField = new JTextField();

        staffPanel.add(new JLabel("Staff ID:"));
        staffPanel.add(staffIdField);
        staffPanel.add(new JLabel("Name:"));
        staffPanel.add(staffNameField);
        staffPanel.add(new JLabel("Gender:"));
        staffPanel.add(genderComboBox);
        staffPanel.add(new JLabel("Designation:"));
        staffPanel.add(designationField);
        staffPanel.add(new JLabel("Department:"));
        staffPanel.add(deptField);
        staffPanel.add(new JLabel("Department ID:"));
        staffPanel.add(deptIdField);
        staffPanel.add(new JLabel("Salary:"));
        staffPanel.add(salaryField);
        staffPanel.add(new JLabel("Age:"));
        staffPanel.add(ageField);

        panel.add(staffPanel);

        // Create a button to save staff information to the database
        JButton saveStaffButton = new JButton("Save Staff Information");
        saveStaffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveStaffInformation(staffIdField.getText(), staffNameField.getText(), (String) genderComboBox.getSelectedItem(),
                        designationField.getText(), deptField.getText(), deptIdField.getText(), salaryField.getText(), ageField.getText());
            }
        });
        panel.add(saveStaffButton);

        // Create a button to view all passengers from the passenger table
        JButton viewPassengersButton = new JButton("View All Passengers");
        viewPassengersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAllPassengers();
            }
        });
        panel.add(viewPassengersButton);

        // Create a button to view all staff from the staff table
        JButton viewStaffButton = new JButton("View All Staff");
        viewStaffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAllStaff();
            }
        });
        panel.add(viewStaffButton);

        add(panel);
    }

    private void saveStaffInformation(String staffId, String name, String gender, String designation, String dept, String deptId, String salary, String age) {
        // Implement the save operation to the database
        String query = "INSERT INTO staff (staff_id, name, gender, designation, department, dept_id, salary, age) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, staffId);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, gender);
            preparedStatement.setString(4, designation);
            preparedStatement.setString(5, dept);
            preparedStatement.setString(6, deptId);
            preparedStatement.setString(7, salary);
            preparedStatement.setString(8, age);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Staff information saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error saving staff information", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            System.err.println("Error saving staff information to the database: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving staff information", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewAllPassengers() {
        // Create a new JFrame to display the passenger table
        JFrame passengerFrame = new JFrame("All Passengers");
        passengerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        passengerFrame.setSize(800, 600);

        // Create a table model to hold the passenger data
        DefaultTableModel tableModel = new DefaultTableModel();

        // Create a JTable using the table model
        JTable table = new JTable(tableModel);

        // Add columns to the table model
        tableModel.addColumn("ID");
        tableModel.addColumn("Booking ID");
        tableModel.addColumn("Age");
        tableModel.addColumn("Name");
        tableModel.addColumn("Gender");

        // Retrieve data from the database and add it to the table model
        String query = "SELECT id, bid, age, name, gender FROM Passenger";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Vector<String> rowData = new Vector<>();
                rowData.add(resultSet.getString("id"));
                rowData.add(resultSet.getString("bid"));
                rowData.add(resultSet.getString("age"));
                rowData.add(resultSet.getString("name"));
                rowData.add(resultSet.getString("gender"));
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            System.err.println("Error retrieving passengers from the database: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving passengers", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Add the table to a scroll pane and add the scroll pane to the frame
        JScrollPane scrollPane = new JScrollPane(table);
        passengerFrame.add(scrollPane);

        // Display the frame
        passengerFrame.setVisible(true);
    }

    private void viewAllStaff() {
        // Create a new JFrame to display the staff table
        JFrame staffFrame = new JFrame("All Staff");
        staffFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        staffFrame.setSize(800, 600);

        // Create a table model to hold the staff data
        DefaultTableModel tableModel = new DefaultTableModel();

        // Create a JTable using the table model
        JTable table = new JTable(tableModel);

        // Add columns to the table model
        tableModel.addColumn("Staff ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Gender");
        tableModel.addColumn("Designation");
        tableModel.addColumn("Department");
        tableModel.addColumn("Department ID");
        tableModel.addColumn("Salary");
        tableModel.addColumn("Age");

        // Retrieve data from the database and add it to the table model
        String query = "SELECT * FROM staff";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Vector<String> rowData = new Vector<>();
                rowData.add(resultSet.getString("staff_id"));
                rowData.add(resultSet.getString("name"));
                rowData.add(resultSet.getString("gender"));
                rowData.add(resultSet.getString("designation"));
                rowData.add(resultSet.getString("department"));
                rowData.add(resultSet.getString("dept_id"));
                rowData.add(resultSet.getString("salary"));
                rowData.add(resultSet.getString("age"));
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            System.err.println("Error retrieving staff from the database: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving staff", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Add the table to a scroll pane and add the scroll pane to the frame
        JScrollPane scrollPane = new JScrollPane(table);
        staffFrame.add(scrollPane);

        // Display the frame
        staffFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PassengerStaffInformationWindow().setVisible(true));
    }
}
