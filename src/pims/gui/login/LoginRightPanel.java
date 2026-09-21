package pims.gui.login;

import pims.components.LoginInput;
import pims.components.PasswordInput;
import pims.components.RoleSelector;
import pims.components.SvgIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginRightPanel extends JPanel {

    private final JTextField userField;

    private final PasswordInput passwordInput;

    private final RoleSelector roleSelector;


    public LoginRightPanel(
            Runnable loginAction
    ) {

        setBackground(
                LoginTheme.BACKGROUND
        );

        setLayout(
                new GridBagLayout()
        );


        JPanel loginCard =
                new JPanel();


        loginCard.setBackground(
                LoginTheme.WHITE
        );


        loginCard.setPreferredSize(
                new Dimension(360, 430)
        );


        loginCard.setMinimumSize(
                new Dimension(360, 430)
        );


        loginCard.setMaximumSize(
                new Dimension(360, 430)
        );


        loginCard.setBorder(
                BorderFactory.createLineBorder(
                        LoginTheme.BORDER,
                        1
                )
        );


        loginCard.setLayout(
                new BoxLayout(
                        loginCard,
                        BoxLayout.Y_AXIS
                )
        );


        // -----------------------------------------------------
        // TOP SPACING
        // -----------------------------------------------------

        loginCard.add(
                Box.createVerticalStrut(25)
        );


        // -----------------------------------------------------
        // WELCOME BACK
        // -----------------------------------------------------

        JLabel welcomeLabel =
                new JLabel(
                        "Welcome Back"
                );


        welcomeLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        26
                )
        );


        welcomeLabel.setForeground(
                LoginTheme.TEXT
        );


        welcomeLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        loginCard.add(welcomeLabel);


        loginCard.add(
                Box.createVerticalStrut(4)
        );


        // -----------------------------------------------------
        // SUBTITLE
        // -----------------------------------------------------

        JLabel subtitleLabel =
                new JLabel(
                        "Sign in to continue"
                );


        subtitleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );


        subtitleLabel.setForeground(
                LoginTheme.MUTED
        );


        subtitleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        loginCard.add(subtitleLabel);


        loginCard.add(
                Box.createVerticalStrut(22)
        );


        // -----------------------------------------------------
        // USERNAME LABEL
        // -----------------------------------------------------

        loginCard.add(
                createFieldLabel(
                        "Username"
                )
        );


        loginCard.add(
                Box.createVerticalStrut(6)
        );


        // -----------------------------------------------------
        // USERNAME FIELD
        // -----------------------------------------------------

        userField =
                new JTextField();


        LoginInput usernameInput =
                new LoginInput(
                        userField,
                        "/pims/gui/images/icons/user.svg"
                );


        loginCard.add(usernameInput);


        loginCard.add(
                Box.createVerticalStrut(13)
        );


        // -----------------------------------------------------
        // PASSWORD LABEL
        // -----------------------------------------------------

        loginCard.add(
                createFieldLabel(
                        "Password"
                )
        );


        loginCard.add(
                Box.createVerticalStrut(6)
        );


        // -----------------------------------------------------
        // PASSWORD
        // -----------------------------------------------------

        passwordInput =
                new PasswordInput();


        loginCard.add(passwordInput);


        loginCard.add(
                Box.createVerticalStrut(13)
        );


        // -----------------------------------------------------
        // ROLE LABEL
        // -----------------------------------------------------

        loginCard.add(
                createFieldLabel(
                        "Role"
                )
        );


        loginCard.add(
                Box.createVerticalStrut(6)
        );


        // -----------------------------------------------------
        // ROLE
        // -----------------------------------------------------

        roleSelector =
                new RoleSelector();


        loginCard.add(roleSelector);


        loginCard.add(
                Box.createVerticalStrut(20)
        );


        // -----------------------------------------------------
        // LOGIN BUTTON
        // -----------------------------------------------------

        JButton loginButton =
                new JButton(
                        "Login"
                );


        loginButton.setIcon(
                new SvgIcon(
                        "/pims/gui/images/icons/login.svg",
                        18,
                        18
                )
        );


        loginButton.setIconTextGap(8);


        loginButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );


        loginButton.setForeground(
                LoginTheme.WHITE
        );


        loginButton.setBackground(
                LoginTheme.PRIMARY
        );


        loginButton.setPreferredSize(
                new Dimension(140, 40)
        );


        loginButton.setMinimumSize(
                new Dimension(140, 40)
        );


        loginButton.setMaximumSize(
                new Dimension(140, 40)
        );


        loginButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        loginButton.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        loginButton.setFocusPainted(false);


        loginButton.setBorderPainted(false);


        loginButton.setOpaque(true);


        loginButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        loginButton.addActionListener(
                e -> loginAction.run()
        );


        loginCard.add(loginButton);


        // -----------------------------------------------------
        // ENTER KEY SUPPORT
        // -----------------------------------------------------

        passwordInput.addKeyListener(
                new KeyAdapter() {

                    @Override
                    public void keyPressed(
                            KeyEvent e
                    ) {

                        if (e.getKeyCode()
                                == KeyEvent.VK_ENTER) {

                            loginAction.run();
                        }
                    }
                }
        );


        userField.addKeyListener(
                new KeyAdapter() {

                    @Override
                    public void keyPressed(
                            KeyEvent e
                    ) {

                        if (e.getKeyCode()
                                == KeyEvent.VK_ENTER) {

                            passwordInput
                                    .requestFocusInWindow();
                        }
                    }
                }
        );


        // -----------------------------------------------------
        // ADD LOGIN CARD
        // -----------------------------------------------------

        GridBagConstraints gbc =
                new GridBagConstraints();


        gbc.gridx = 0;
        gbc.gridy = 0;


        gbc.anchor =
                GridBagConstraints.CENTER;


        add(
                loginCard,
                gbc
        );
    }


    // =========================================================
    // FIELD LABEL
    // =========================================================

    private JLabel createFieldLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);


        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );


        label.setForeground(
                LoginTheme.TEXT
        );


        label.setPreferredSize(
                new Dimension(280, 18)
        );


        label.setMinimumSize(
                new Dimension(280, 18)
        );


        label.setMaximumSize(
                new Dimension(280, 18)
        );


        label.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        label.setHorizontalAlignment(
                SwingConstants.LEFT
        );


        return label;
    }


    // =========================================================
    // GET USERNAME
    // =========================================================

    public String getUsername() {

        return userField
                .getText()
                .trim();
    }


    // =========================================================
    // GET PASSWORD
    // =========================================================

    public String getPassword() {

        return new String(
                passwordInput.getPassword()
        );
    }


    // =========================================================
    // GET ROLE
    // =========================================================

    public String getSelectedRole() {

        return roleSelector
                .getSelectedRole();
    }


    // =========================================================
    // CLEAR PASSWORD
    // =========================================================

    public void clearPassword() {

        passwordInput.setText("");
    }


    // =========================================================
    // FOCUS PASSWORD
    // =========================================================

    public void focusPassword() {

        passwordInput.requestFocusInWindow();
    }


    // =========================================================
    // FOCUS USERNAME
    // =========================================================

    public void focusUsername() {

        userField.requestFocusInWindow();
    }
}