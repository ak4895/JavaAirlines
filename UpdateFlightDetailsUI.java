import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Calendar;

public class UpdateFlightDetailsUI {
    private FlightDetails flightDetails;
    private FlightDetailsEntryWindow parentWindow;

    private JTextField flightNumberField;
    private JTextField airlineField;
    private JTextField fromField;
    private JTextField toField;
    private JSpinner departureTimeSpinner;
    private JSpinner arrivalTimeSpinner;

    public UpdateFlightDetailsUI(FlightDetails flightDetails, FlightDetailsEntryWindow parentWindow) {
        this.flightDetails = flightDetails;
        this.parentWindow = parentWindow;

        JFrame frame = new JFrame("Update Flight Details");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel updatePanel = new JPanel();
        updatePanel.setLayout(new GridLayout(6, 2, 5, 5));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        flightNumberField = new JTextField(flightDetails.getFlightNumber());
        airlineField = new JTextField(flightDetails.getAirline());
        fromField = new JTextField(flightDetails.getFrom());
        toField = new JTextField(flightDetails.getTo());
        departureTimeSpinner = new JSpinner(new SpinnerDateModel(flightDetails.getDepartureTime(), null, null, Calendar.MINUTE));
        JSpinner.DateEditor departureEditor = new JSpinner.DateEditor(departureTimeSpinner, "HH:mm");
        departureTimeSpinner.setEditor(departureEditor);
        arrivalTimeSpinner = new JSpinner(new SpinnerDateModel(flightDetails.getArrivalTime(), null, null, Calendar.MINUTE));
        JSpinner.DateEditor arrivalEditor = new JSpinner.DateEditor(arrivalTimeSpinner, "HH:mm");
        arrivalTimeSpinner.setEditor(arrivalEditor);

        updatePanel.add(new JLabel("Flight Number:"));
        updatePanel.add(flightNumberField);
        updatePanel.add(new JLabel("Airline:"));
        updatePanel.add(airlineField);
        updatePanel.add(new JLabel("From:"));
        updatePanel.add(fromField);
        updatePanel.add(new JLabel("To:"));
        updatePanel.add(toField);
        updatePanel.add(new JLabel("Departure Time:"));
        updatePanel.add(departureTimeSpinner);
        updatePanel.add(new JLabel("Arrival Time:"));
        updatePanel.add(arrivalTimeSpinner);

        JButton updateButton = new JButton("Update Flight");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFlightDetails();
                frame.dispose();
            }
        });

        frame.add(updatePanel, BorderLayout.CENTER);
        frame.add(updateButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void setExtendedState(int maximizedBoth) {
    }

    private void updateFlightDetails() {
        flightDetails.setFlightNumber(flightNumberField.getText());
        flightDetails.setAirline(airlineField.getText());
        flightDetails.setFrom(fromField.getText());
        flightDetails.setTo(toField.getText());
        flightDetails.setDepartureTime((Date) departureTimeSpinner.getValue());
        flightDetails.setArrivalTime((Date) arrivalTimeSpinner.getValue());

        if (parentWindow.updateFlightInDatabase(flightDetails)) {
            parentWindow.updateFlightInListModel(flightDetails);
            JOptionPane.showMessageDialog(null, "Flight details updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error updating flight details", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
