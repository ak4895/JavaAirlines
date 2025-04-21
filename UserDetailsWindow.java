import javax.swing.*;
import java.awt.*;

public class UserDetailsWindow extends JFrame {
    private JList<UserDetails> userList;
    private JScrollPane userScrollPane;

    public UserDetailsWindow(DefaultListModel<UserDetails> userListModel) {
        super("User Details");
        // Create the user list and add it to a scroll pane
        userList = new JList<>(userListModel);
        userScrollPane = new JScrollPane(userList);
setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Set up the layout
        setLayout(new BorderLayout());
        add(userScrollPane, BorderLayout.CENTER);

        // Set window properties
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window, not the main window
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DefaultListModel<UserDetails> userListModel = new DefaultListModel<>();
            UserDetailsWindow userDetailsWindow = new UserDetailsWindow(userListModel);
            userDetailsWindow.setVisible(true);
        });
    }
}
