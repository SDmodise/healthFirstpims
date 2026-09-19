package pims.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import pims.dao.UserDAO;
import pims.model.User;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JLabel errorLabel;

    // Application colours
    private static final Color PRIMARY = new Color(35, 78, 112);
    private static final Color PRIMARY_DARK = new Color(27, 61, 88);
    private static final Color BACKGROUND = new Color(243, 246, 249);
    private static final Color TEXT = new Color(45, 55, 65);
    private static final Color MUTED = new Color(110, 120, 130);
    private static final Color BORDER = new Color(210, 218, 225);
    private static final Color ERROR = new Color(190, 55, 55);

    public LoginFrame() {
        setTitle("HealthFirst PIMS - Login");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND);

        // =========================
        // HEADER
        // =========================

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(PRIMARY);
        headerPanel.setBorder(new EmptyBorder(30, 30, 25, 30));

        JLabel titleLabel = new JLabel("HealthFirst PIMS");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel subtitleLabel = new JLabel(
                "Pharmacy Information Management System"
        );
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setForeground(new Color(220, 230, 238));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(7));
        headerPanel.add(subtitleLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // =========================
        // LOGIN CARD
        // =========================

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(25, 30, 25, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(7, 0, 7, 0);

        // Login title
        JLabel loginTitle = new JLabel("Sign in");
        loginTitle.setForeground(TEXT);
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

        gbc.gridy = 0;
        gbc.insets = new Insets(5, 0, 18, 0);
        card.add(loginTitle, gbc);

        // Username label
        JLabel usernameLabel = createLabel("Username");

        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 5, 0);
        card.add(usernameLabel, gbc);

        // Username field
        usernameField = createTextField();

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 10, 0);
        card.add(usernameField, gbc);

        // Password label
        JLabel passwordLabel = createLabel("Password");

        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 5, 0);
        card.add(passwordLabel, gbc);

        // Password field
        passwordField = new JPasswordField();
        styleTextField(passwordField);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 10, 0);
        card.add(passwordField, gbc);

        // Role label
        JLabel roleLabel = createLabel("Role");

        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 5, 0);
        card.add(roleLabel, gbc);

        // Role dropdown
        roleCombo = new JComboBox<>(
                new String[]{"Admin", "Cashier"}
        );

        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleCombo.setPreferredSize(new Dimension(0, 38));
        roleCombo.setBackground(Color.WHITE);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);
        card.add(roleCombo, gbc);

        // Error message
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(ERROR);
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        card.add(errorLabel, gbc);

        // Login button
        JButton loginButton = new JButton("Login");
        styleButton(loginButton);

        loginButton.addActionListener(
                (ActionEvent e) -> handleLogin()
        );

        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 5, 0);
        card.add(loginButton, gbc);

        // Press Enter to login
        getRootPane().setDefaultButton(loginButton);

        // Center card inside content area
        JPanel cardContainer = new JPanel(new GridBagLayout());
        cardContainer.setBackground(BACKGROUND);
        cardContainer.setBorder(
                new EmptyBorder(20, 30, 20, 30)
        );

        cardContainer.add(card);

        mainPanel.add(
                cardContainer,
                BorderLayout.CENTER
        );

        add(mainPanel);

        // Put cursor in username field
        SwingUtilities.invokeLater(
                () -> usernameField.requestFocusInWindow()
        );

        setVisible(true);
    }

    // =========================
    // UI HELPERS
    // =========================

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        return label;
    }

    private JTextField createTextField() {

        JTextField field = new JTextField();

        styleTextField(field);

        return field;
    }

    private void styleTextField(JTextField field) {

        field.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        field.setPreferredSize(
                new Dimension(0, 38)
        );

        field.setBackground(Color.WHITE);
        field.setForeground(TEXT);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(5, 10, 5, 10)
                )
        );
    }

    private void styleButton(JButton button) {

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY);

        button.setPreferredSize(
                new Dimension(0, 40)
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        8, 15, 8, 15
                )
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );
    }

    // =========================
    // LOGIN
    // =========================

    private void handleLogin() {

        String username =
                usernameField.getText().trim();

        String password =
                new String(passwordField.getPassword());

        String role =
                (String) roleCombo.getSelectedItem();

        // Check for empty fields
        if (username.isEmpty() || password.isEmpty()) {

            errorLabel.setText(
                    "Please enter your username and password."
            );

            return;
        }

        // Authenticate using the database
        UserDAO userDAO = new UserDAO();

        User user =
                userDAO.authenticate(
                        username,
                        password,
                        role
                );

        if (user != null) {

            errorLabel.setText("");

            if ("Admin".equalsIgnoreCase(user.getRole())) {

                new AdminDashboard();

            } else if ("Cashier".equalsIgnoreCase(user.getRole())) {

                // Pass the logged-in cashier's user ID
                new CashierDashboard(
                        user.getUserId()
                );
            }

            dispose();

        } else {

            errorLabel.setText(
                    "Invalid username, password, or role."
            );

            passwordField.setText("");

            passwordField.requestFocusInWindow();
        }
    }
}