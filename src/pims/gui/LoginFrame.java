package pims.gui;

import pims.dao.UserDAO;
import pims.gui.login.LoginLeftPanel;
import pims.gui.login.LoginRightPanel;
import pims.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private LoginRightPanel rightPanel;

    private JLabel errorLabel;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public LoginFrame() {

        setTitle(
                "HealthFirst PIMS - Login"
        );


        setSize(
                900,
                550
        );


        setMinimumSize(
                new Dimension(
                        900,
                        550
                )
        );


        setLocationRelativeTo(null);


        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );


        setResizable(false);


        createUI();


        setVisible(true);


        SwingUtilities.invokeLater(() ->
                rightPanel.focusUsername()
        );
    }


    // =========================================================
    // CREATE UI
    // =========================================================

    private void createUI() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout()
                );


        LoginLeftPanel leftPanel =
                new LoginLeftPanel();


        rightPanel =
                new LoginRightPanel(
                        this::performLogin
                );


        mainPanel.add(
                leftPanel,
                BorderLayout.WEST
        );


        mainPanel.add(
                rightPanel,
                BorderLayout.CENTER
        );


        setContentPane(
                mainPanel
        );
    }


    // =========================================================
    // LOGIN
    // =========================================================

    private void performLogin() {

        String username =
                rightPanel.getUsername();


        String password =
                rightPanel.getPassword();


        String selectedRole =
                rightPanel.getSelectedRole();


        // -----------------------------------------------------
        // VALIDATION
        // -----------------------------------------------------

        if (username.isEmpty()
                || password.isEmpty()) {

            showError(
                    "Please enter your username and password."
            );

            return;
        }


        try {

            UserDAO userDAO =
                    new UserDAO();


            User user =
                    userDAO.authenticate(
                            username,
                            password,
                            selectedRole
                    );


            // -------------------------------------------------
            // INVALID LOGIN
            // -------------------------------------------------

            if (user == null) {

                showError(
                        "Invalid username, password or role."
                );


                rightPanel.clearPassword();


                rightPanel.focusPassword();


                return;
            }


            // -------------------------------------------------
            // LOGIN SUCCESSFUL
            // -------------------------------------------------

            dispose();


            // -------------------------------------------------
            // ADMIN
            // -------------------------------------------------

            if (user.getRole()
                    .equalsIgnoreCase("Admin")) {

                new AdminDashboard();
            }


            // -------------------------------------------------
            // CASHIER
            // -------------------------------------------------

            else if (
                    user.getRole()
                            .equalsIgnoreCase("Cashier")
            ) {

                new CashierDashboard(
                        user.getUserId()
                );
            }


        } catch (Exception e) {

            e.printStackTrace();


            showError(
                    "Unable to connect to the database."
            );
        }
    }


    // =========================================================
    // ERROR MESSAGE
    // =========================================================

    private void showError(
            String message
    ) {

        if (errorLabel != null) {

            errorLabel.setText(
                    message
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    message,
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}