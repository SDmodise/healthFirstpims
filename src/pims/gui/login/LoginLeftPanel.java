package pims.gui.login;

import pims.components.SvgIcon;

import javax.swing.*;
import java.awt.*;

public class LoginLeftPanel extends JPanel {

    public LoginLeftPanel() {

        setPreferredSize(
                new Dimension(440, 550)
        );

        setBackground(
                LoginTheme.PRIMARY_DARK
        );

        setLayout(
                new BoxLayout(
                        this,
                        BoxLayout.Y_AXIS
                )
        );


        // -----------------------------------------------------
        // TOP SPACING
        // -----------------------------------------------------

        add(
                Box.createVerticalStrut(45)
        );


        // -----------------------------------------------------
        // LOGO
        // -----------------------------------------------------

        JLabel logoLabel =
                new JLabel();


        try {

            ImageIcon originalLogo =
                    new ImageIcon(
                            getClass().getResource(
                                    "/pims/gui/HealthFirstLogo.png"
                            )
                    );


            Image scaledLogo =
                    originalLogo.getImage()
                            .getScaledInstance(
                                    110,
                                    110,
                                    Image.SCALE_SMOOTH
                            );


            logoLabel.setIcon(
                    new ImageIcon(
                            scaledLogo
                    )
            );


        } catch (Exception e) {

            System.err.println(
                    "Could not load HealthFirst logo: "
                            + e.getMessage()
            );
        }


        logoLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        add(logoLabel);


        add(
                Box.createVerticalStrut(18)
        );


        // -----------------------------------------------------
        // PHARMACY NAME
        // -----------------------------------------------------

        JLabel titleLabel =
                new JLabel(
                        "HealthFirst Pharmacy"
                );


        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        26
                )
        );


        titleLabel.setForeground(
                LoginTheme.WHITE
        );


        titleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        add(titleLabel);


        add(
                Box.createVerticalStrut(6)
        );


        // -----------------------------------------------------
        // SUBTITLE
        // -----------------------------------------------------

        JLabel subtitleLabel =
                new JLabel(
                        "Pharmacy Inventory Management System"
                );


        subtitleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );


        subtitleLabel.setForeground(
                new Color(190, 205, 215)
        );


        subtitleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        add(subtitleLabel);


        add(
                Box.createVerticalStrut(8)
        );


        // -----------------------------------------------------
        // TAGLINE
        // -----------------------------------------------------

        JLabel taglineLabel =
                new JLabel(
                        "Efficient. Secure. Professional."
                );


        taglineLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.ITALIC,
                        13
                )
        );


        taglineLabel.setForeground(
                new Color(190, 205, 215)
        );


        taglineLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        add(taglineLabel);


        add(
                Box.createVerticalStrut(42)
        );


        // -----------------------------------------------------
        // FEATURES
        // -----------------------------------------------------

        add(
                createFeatureRow(
                        "/pims/gui/images/icons/medicines.svg",
                        "Inventory Management"
                )
        );


        add(
                Box.createVerticalStrut(18)
        );


        add(
                createFeatureRow(
                        "/pims/gui/images/icons/sales.svg",
                        "Point of Sale Processing"
                )
        );


        add(
                Box.createVerticalStrut(18)
        );


        add(
                createFeatureRow(
                        "/pims/gui/images/icons/reports.svg",
                        "Sales & Reporting"
                )
        );


        add(
                Box.createVerticalStrut(18)
        );


        add(
                createFeatureRow(
                        "/pims/gui/images/icons/security.svg",
                        "Secure User Management"
                )
        );
    }


    // =========================================================
    // FEATURE ROW
    // =========================================================

    private JPanel createFeatureRow(
            String iconPath,
            String text
    ) {

        JPanel row =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                0,
                                0
                        )
                );


        row.setOpaque(false);


        row.setPreferredSize(
                new Dimension(300, 28)
        );


        row.setMaximumSize(
                new Dimension(300, 28)
        );


        row.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel iconLabel =
                new JLabel(
                        new SvgIcon(
                                iconPath,
                                18,
                                18
                        )
                );


        row.add(iconLabel);


        row.add(
                Box.createHorizontalStrut(14)
        );


        JLabel textLabel =
                new JLabel(text);


        textLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );


        textLabel.setForeground(
                LoginTheme.WHITE
        );


        row.add(textLabel);


        return row;
    }
}