package pims.components;

import javax.swing.*;
import java.awt.*;

public class RoleSelector extends JPanel {

    private final JComboBox<String> roleCombo;


    public RoleSelector() {

        setLayout(
                new BorderLayout(
                        8,
                        0
                )
        );

        setBackground(
                Color.WHITE
        );

        setPreferredSize(
                new Dimension(280, 38)
        );

        setMinimumSize(
                new Dimension(280, 38)
        );

        setMaximumSize(
                new Dimension(280, 38)
        );

        setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        setBorder(
                BorderFactory.createLineBorder(
                        new Color(210, 218, 225),
                        1
                )
        );


        // -----------------------------------------------------
        // USERS ICON
        // -----------------------------------------------------

        JLabel iconLabel =
                new JLabel(
                        new SvgIcon(
                                "/pims/gui/images/icons/users.svg",
                                18,
                                18
                        )
                );


        JPanel iconContainer =
                new JPanel(
                        new GridBagLayout()
                );

        iconContainer.setOpaque(false);

        iconContainer.setPreferredSize(
                new Dimension(38, 36)
        );

        iconContainer.add(iconLabel);


        add(
                iconContainer,
                BorderLayout.WEST
        );


        // -----------------------------------------------------
        // ROLE COMBO BOX
        // -----------------------------------------------------

        roleCombo =
                new JComboBox<>(
                        new String[]{
                                "Admin",
                                "Cashier"
                        }
                );


        roleCombo.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        roleCombo.setForeground(
                new Color(45, 55, 65)
        );

        roleCombo.setBackground(
                Color.WHITE
        );

        roleCombo.setBorder(null);

        roleCombo.setFocusable(false);


        add(
                roleCombo,
                BorderLayout.CENTER
        );
    }


    // =========================================================
    // GET SELECTED ROLE
    // =========================================================

    public String getSelectedRole() {

        return (String)
                roleCombo.getSelectedItem();
    }
}