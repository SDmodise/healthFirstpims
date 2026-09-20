package pims.gui;

import pims.dao.UserDAO;
import pims.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

public class LoginFrame extends JFrame {

    // =========================
    // Application colours
    // =========================

    private static final Color PRIMARY = new Color(35, 78, 112);
    private static final Color PRIMARY_DARK = new Color(27, 61, 88);
    private static final Color SECONDARY = new Color(45, 155, 105);
    private static final Color BACKGROUND = new Color(243, 246, 249);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT = new Color(45, 55, 65);
    private static final Color MUTED = new Color(110, 120, 130);
    private static final Color BORDER = new Color(210, 218, 225);
    private static final Color ERROR = new Color(190, 55, 55);

    // =========================
    // Components
    // =========================

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JLabel errorLabel;

    public LoginFrame() {

        setTitle("HealthFirst PIMS - Login");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        createUI();

        setVisible(true);

        SwingUtilities.invokeLater(() ->
                usernameField.requestFocusInWindow()
        );
    }

    private void createUI() {

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(BACKGROUND);

        // ==========================================
        // LEFT BRANDING PANEL
        // ==========================================

        JPanel brandingPanel = new JPanel();
        brandingPanel.setBackground(PRIMARY_DARK);
        brandingPanel.setLayout(new BorderLayout());
        brandingPanel.setBorder(
                new EmptyBorder(35, 35, 35, 35)
        );

        JPanel brandingContent = new JPanel();
        brandingContent.setOpaque(false);
        brandingContent.setLayout(
                new BoxLayout(
                        brandingContent,
                        BoxLayout.Y_AXIS
                )
        );

        // Logo
        JLabel logoLabel = createLogoLabel();

        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandingContent.add(logoLabel);
        brandingContent.add(Box.createVerticalStrut(20));

        // Application name
        JLabel appName = new JLabel(
                "<html><div style='text-align:center;'>"
                        + "HealthFirst"
                        + "</div></html>"
        );

        appName.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        appName.setForeground(WHITE);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandingContent.add(appName);

        brandingContent.add(
                Box.createVerticalStrut(8)
        );

        // Subtitle
        JLabel subtitle = new JLabel(
                "<html><div style='text-align:center;'>"
                        + "Pharmacy Inventory Management System<br>"
                        + "</div></html>"
        );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        subtitle.setForeground(
                new Color(220, 230, 238)
        );

        subtitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        brandingContent.add(subtitle);

        brandingContent.add(
                Box.createVerticalStrut(30)
        );

        // Tagline
        JLabel tagline = new JLabel(
                "Better Health. Brighter Tomorrow."
        );

        tagline.setFont(
                new Font(
                        "Segoe UI",
                        Font.ITALIC,
                        14
                )
        );

        tagline.setForeground(
                new Color(190, 215, 205)
        );

        tagline.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        brandingContent.add(tagline);

        brandingPanel.add(
                brandingContent,
                BorderLayout.CENTER
        );

        // Feature section
        JPanel featurePanel = new JPanel();

        featurePanel.setOpaque(false);

        featurePanel.setLayout(
                new BoxLayout(
                        featurePanel,
                        BoxLayout.Y_AXIS
                )
        );

        featurePanel.setBorder(
                new EmptyBorder(15, 10, 5, 10)
        );

        featurePanel.add(
                createFeatureLabel(
                        "",
                        "Safe Medication Management"
                )
        );

        featurePanel.add(
                Box.createVerticalStrut(8)
        );

        featurePanel.add(
                createFeatureLabel(
                        "",
                        "Efficient Pharmacy Operations"
                )
        );

        featurePanel.add(
                Box.createVerticalStrut(8)
        );

        featurePanel.add(
                createFeatureLabel(
                        "",
                        "Reliable Inventory Control"
                )
        );

        brandingPanel.add(
                featurePanel,
                BorderLayout.SOUTH
        );

        // ==========================================
        // RIGHT LOGIN PANEL
        // ==========================================

        JPanel loginPanel = new JPanel(
                new GridBagLayout()
        );

        loginPanel.setBackground(BACKGROUND);

        JPanel loginCard = new JPanel(
                new GridBagLayout()
        );

        loginCard.setBackground(WHITE);

        loginCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                35,
                                45,
                                35,
                                45
                        )
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets =
                new Insets(
                        5,
                        0,
                        5,
                        0
                );

        // ==========================================
        // Welcome title
        // ==========================================

        JLabel welcomeLabel = new JLabel(
                "Welcome Back"
        );

        welcomeLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        welcomeLabel.setForeground(TEXT);

        gbc.gridx = 0;
        gbc.gridy = 0;

        loginCard.add(
                welcomeLabel,
                gbc
        );

        // Subtitle
        JLabel loginSubtitle = new JLabel(
                "Sign in to access your pharmacy system"
        );

        loginSubtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        loginSubtitle.setForeground(MUTED);

        gbc.gridy++;

        gbc.insets = new Insets(
                0,
                0,
                25,
                0
        );

        loginCard.add(
                loginSubtitle,
                gbc
        );

        // ==========================================
        // Username
        // ==========================================

        JLabel usernameLabel =
                createFieldLabel("Username");

        gbc.gridy++;

        gbc.insets = new Insets(
                5,
                0,
                5,
                0
        );

        loginCard.add(
                usernameLabel,
                gbc
        );

        usernameField =
                createTextField();

        usernameField.setToolTipText(
                "Enter your username"
        );

        gbc.gridy++;

        loginCard.add(
                usernameField,
                gbc
        );

        // ==========================================
        // Password
        // ==========================================

        JLabel passwordLabel =
                createFieldLabel("Password");

        gbc.gridy++;

        gbc.insets = new Insets(
                12,
                0,
                5,
                0
        );

        loginCard.add(
                passwordLabel,
                gbc
        );

        passwordField =
                createPasswordField();

        passwordField.setToolTipText(
                "Enter your password"
        );

        gbc.gridy++;

        gbc.insets = new Insets(
                5,
                0,
                5,
                0
        );

        loginCard.add(
                passwordField,
                gbc
        );

        // ==========================================
        // Role
        // ==========================================

        JLabel roleLabel =
                createFieldLabel("Role");

        gbc.gridy++;

        gbc.insets = new Insets(
                12,
                0,
                5,
                0
        );

        loginCard.add(
                roleLabel,
                gbc
        );

        roleComboBox =
                new JComboBox<>(
                        new String[]{
                                "Admin",
                                "Cashier"
                        }
                );

        roleComboBox.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        roleComboBox.setPreferredSize(
                new Dimension(
                        300,
                        42
                )
        );

        roleComboBox.setBackground(WHITE);

        roleComboBox.setForeground(TEXT);

        gbc.gridy++;

        gbc.insets = new Insets(
                5,
                0,
                5,
                0
        );

        loginCard.add(
                roleComboBox,
                gbc
        );

        // ==========================================
        // Error label
        // ==========================================

        errorLabel = new JLabel(" ");

        errorLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        errorLabel.setForeground(ERROR);

        gbc.gridy++;

        gbc.insets = new Insets(
                8,
                0,
                2,
                0
        );

        loginCard.add(
                errorLabel,
                gbc
        );

        // ==========================================
        // Login button
        // ==========================================

        JButton loginButton =
                createLoginButton();

        loginButton.addActionListener(
                e -> login()
        );

        gbc.gridy++;

        gbc.insets = new Insets(
                10,
                0,
                5,
                0
        );

        loginCard.add(
                loginButton,
                gbc
        );

        // ==========================================
        // Footer
        // ==========================================

        JLabel footerLabel =
                new JLabel(
                        "HealthFirst Pharmacy Management System"
                );

        footerLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        footerLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        footerLabel.setForeground(MUTED);

        gbc.gridy++;

        gbc.insets = new Insets(
                18,
                0,
                0,
                0
        );

        loginCard.add(
                footerLabel,
                gbc
        );

        loginPanel.add(loginCard);

        // ==========================================
        // Add both sides
        // ==========================================

        mainPanel.add(brandingPanel);
        mainPanel.add(loginPanel);

        add(mainPanel);

        // Enter key = Login
        getRootPane().setDefaultButton(
                loginButton
        );

        // Escape key = clear password
        passwordField.getInputMap(
                JComponent.WHEN_FOCUSED
        ).put(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_ESCAPE,
                        0
                ),
                "clearPassword"
        );

        passwordField.getActionMap()
                .put(
                        "clearPassword",
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(
                                    java.awt.event.ActionEvent e
                            ) {
                                passwordField.setText("");
                            }
                        }
                );
    }

    // =====================================================
    // Logo
    // =====================================================

    private JLabel createLogoLabel() {

        JLabel label = new JLabel();

        java.net.URL logoURL =
                getClass().getResource(
                        "/pims/gui/HealthFirstLogo.png"
                );

        if (logoURL != null) {

            ImageIcon originalIcon =
                    new ImageIcon(logoURL);

            Image image =
                    originalIcon.getImage()
                            .getScaledInstance(
                                    190,
                                    190,
                                    Image.SCALE_SMOOTH
                            );

            label.setIcon(
                    new ImageIcon(image)
            );

        } else {

            // Fallback if logo cannot be found
            label.setText("PHARMACY");
            label.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            24
                    )
            );
            label.setForeground(WHITE);

            System.out.println(
                    "Logo not found: "
                            + "/pims/gui/images/HealthFirstLogo.png"
            );
        }

        return label;
    }

    // =====================================================
    // Feature label
    // =====================================================

    private JLabel createFeatureLabel(
            String icon,
            String text
    ) {

        JLabel label =
                new JLabel(
                        icon + "  " + text
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        label.setForeground(
                new Color(220, 230, 238)
        );

        return label;
    }

    // =====================================================
    // Field label
    // =====================================================

    private JLabel createFieldLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        label.setForeground(TEXT);

        return label;
    }

    // =====================================================
    // Text field
    // =====================================================

    private JTextField createTextField() {

        JTextField field =
                new JTextField();

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        field.setForeground(TEXT);
        field.setBackground(WHITE);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                8,
                                12,
                                8,
                                12
                        )
                )
        );

        field.setPreferredSize(
                new Dimension(
                        300,
                        42
                )
        );

        return field;
    }

    // =====================================================
    // Password field
    // =====================================================

    private JPasswordField createPasswordField() {

        JPasswordField field =
                new JPasswordField();

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        field.setForeground(TEXT);
        field.setBackground(WHITE);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                8,
                                12,
                                8,
                                12
                        )
                )
        );

        field.setPreferredSize(
                new Dimension(
                        300,
                        42
                )
        );

        return field;
    }

    // =====================================================
    // Login button
    // =====================================================

    private JButton createLoginButton() {

        JButton button =
                new JButton("Login");

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        button.setForeground(WHITE);
        button.setBackground(PRIMARY);

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        20,
                        10,
                        20
                )
        );

        button.setPreferredSize(
                new Dimension(
                        300,
                        44
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    // =====================================================
    // Authentication
    // =====================================================

    private void login() {

        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        String role =
                (String) roleComboBox
                        .getSelectedItem();

        // Clear previous error
        errorLabel.setText(" ");

        if (username.isEmpty()) {

            errorLabel.setText(
                    "Please enter your username."
            );

            usernameField.requestFocusInWindow();

            return;
        }

        if (password.isEmpty()) {

            errorLabel.setText(
                    "Please enter your password."
            );

            passwordField.requestFocusInWindow();

            return;
        }

        UserDAO userDAO =
                new UserDAO();

        User user =
                userDAO.authenticate(
                        username,
                        password,
                        role
                );

        if (user != null) {

            errorLabel.setText(" ");

            if ("Admin".equalsIgnoreCase(
                    user.getRole()
            )) {

                new AdminDashboard();

            } else if ("Cashier".equalsIgnoreCase(
                    user.getRole()
            )) {

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