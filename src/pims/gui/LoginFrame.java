package pims.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JLabel errorLabel;

    public LoginFrame() {
        setTitle("HealthFirst Pharmacy - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Login to PIMS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Role
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Role:"), gbc);

        roleCombo = new JComboBox<>(new String[]{"Admin", "Cashier"});
        gbc.gridx = 1;
        panel.add(roleCombo, gbc);

        // Error label
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(errorLabel, gbc);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener((ActionEvent e) -> handleLogin());
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        add(panel);
        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();

        // TODO: Replace with actual database authentication
        if (validateCredentials(username, password, role)) {
            errorLabel.setText("");
            if ("Admin".equals(role)) {
                new AdminDashboard();
            } else {
                new CashierDashboard();
            }
            dispose();
        } else {
            errorLabel.setText("Invalid credentials!");
            passwordField.setText("");
        }
    }

    private boolean validateCredentials(String username, String password, String role) {
        // TODO: Query database to verify credentials
        // For now, use hardcoded test credentials
        if ("admin".equals(username) && "admin123".equals(password) && "Admin".equals(role)) {
            return true;
        }
        if ("cashier".equals(username) && "cash123".equals(password) && "Cashier".equals(role)) {
            return true;
        }
        return false;
    }
}