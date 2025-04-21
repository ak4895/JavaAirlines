import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CricketStatsUI {
    private JFrame frame;
    private ArrayList<Team> teams;

    private boolean loggedIn = false;

    public CricketStatsUI() {
        teams = new ArrayList<>();

        frame = new JFrame("Cricket Stats");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        if (!loggedIn) {
            showLoginScreen();
        } else {
            showMainMenu();
        }
    }

    private void showLoginScreen() {
        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);
        
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 160, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 160, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 250, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());

                // Check login credentials
                if (username.equals("rishav") && password.equals("rishav")) {
                    loggedIn = true;
                    frame.getContentPane().removeAll();
                    frame.repaint();
                    showMainMenu();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password. Please try again.");
                }
            }
        });

        frame.setVisible(true);
    }

    private void showMainMenu() {
        JButton addButton = new JButton("Add Team");
        addButton.setBounds(100, 50, 200, 30);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTeam();
            }
        });

        JButton calculateButton = new JButton("Calculate NRR");
        calculateButton.setBounds(100, 100, 200, 30);
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateNRR();
            }
        });

        JButton displayButton = new JButton("Display Teams");
        displayButton.setBounds(100, 150, 200, 30);
        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayTeams();
            }
        });

        frame.add(addButton);
        frame.add(calculateButton);
        frame.add(displayButton);

        frame.setLayout(null);
        frame.setVisible(true);
    }

    private void addTeam() {
        String teamName = JOptionPane.showInputDialog(frame, "Enter Team Name:");
        if (teamName != null && !teamName.isEmpty()) {
            Team team = new Team(teamName);
            teams.add(team);
        }
    }

    private void calculateNRR() {
        for (Team team : teams) {
            double runsScored = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter runs scored for " + team.getName()));
            double runsConceded = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter runs conceded for " + team.getName()));
            team.calculateNRR(runsScored, runsConceded);
        }
    }

    private void displayTeams() {
        Collections.sort(teams, Comparator.comparing(Team::getNetRunRate).reversed());

        StringBuilder output = new StringBuilder("Teams Sorted by Net Run Rate:\n");
        for (Team team : teams) {
            output.append(team.getName()).append(" - NRR: ").append(team.getNetRunRate()).append("\n");
        }

        JOptionPane.showMessageDialog(frame, output.toString());
    }

    public static void main(String[] args) {
        new CricketStatsUI();
    }

    class Team {
        private String name;
        private double netRunRate;

        public Team(String name) {
            this.name = name;
            this.netRunRate = 0.0;
        }

        public String getName() {
            return name;
        }

        public double getNetRunRate() {
            return netRunRate;
        }

        public void calculateNRR(double runsScored, double runsConceded) {
            netRunRate = (runsScored - runsConceded) / runsConceded;
        }
    }
}