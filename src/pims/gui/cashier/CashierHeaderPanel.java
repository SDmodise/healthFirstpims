package pims.gui.cashier;

import javax.swing.*;
import java.awt.*;

public class CashierHeaderPanel extends JPanel {

    // ==========================================================
    // COLORS
    // ==========================================================

    private static final Color WHITE =
            Color.WHITE;

    private static final Color PRIMARY =
            new Color(27, 61, 88);

    private static final Color SECONDARY =
            new Color(45, 155, 105);

    private static final Color MUTED =
            new Color(110, 120, 130);

    private static final Color BORDER =
            new Color(225, 230, 235);


    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================

    public CashierHeaderPanel() {

        setBackground(
                WHITE
        );

        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                18,
                                24,
                                18,
                                24
                        )
                )
        );

        setLayout(
                new BorderLayout()
        );


        // ======================================================
        // TITLE PANEL
        // ======================================================

        JPanel titlePanel =
                new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel titleLabel =
                new JLabel(
                        "Cashier Point of Sale"
                );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        titleLabel.setForeground(
                PRIMARY
        );


        JLabel subtitleLabel =
                new JLabel(
                        "Process sales and manage customer purchases"
                );

        subtitleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        subtitleLabel.setForeground(
                MUTED
        );

        subtitleLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        4,
                        0,
                        0,
                        0
                )
        );


        titlePanel.add(
                titleLabel
        );

        titlePanel.add(
                subtitleLabel
        );


        // ======================================================
        // STATUS
        // ======================================================

        JLabel statusLabel =
                new JLabel(
                        "  READY  "
                );

        statusLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        statusLabel.setForeground(
                SECONDARY
        );

        statusLabel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                SECONDARY,
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                6,
                                8,
                                6,
                                8
                        )
                )
        );


        // ======================================================
        // ADD COMPONENTS
        // ======================================================

        add(
                titlePanel,
                BorderLayout.WEST
        );

        add(
                statusLabel,
                BorderLayout.EAST
        );
    }
}