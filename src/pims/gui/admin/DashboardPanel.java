package pims.gui.admin;

import pims.dao.MedicineDAO;
import pims.dao.SupplierDAO;
import pims.dao.UserDAO;
import pims.model.Medicine;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    // ==========================================================
    // COLORS
    // ==========================================================

    private static final Color BACKGROUND =
            new Color(243, 246, 249);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color PRIMARY =
            new Color(27, 61, 88);

    private static final Color TEXT =
            new Color(45, 55, 65);

    private static final Color MUTED =
            new Color(110, 120, 130);

    private static final Color BORDER =
            new Color(225, 230, 235);


    // ==========================================================
    // DAOs
    // ==========================================================

    private final MedicineDAO medicineDAO;
    private final SupplierDAO supplierDAO;
    private final UserDAO userDAO;


    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================

    public DashboardPanel(
            MedicineDAO medicineDAO,
            SupplierDAO supplierDAO,
            UserDAO userDAO
    ) {

        this.medicineDAO = medicineDAO;
        this.supplierDAO = supplierDAO;
        this.userDAO = userDAO;

        setLayout(
                new BorderLayout()
        );

        setBackground(
                BACKGROUND
        );

        setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        30,
                        30,
                        30
                )
        );


        // ======================================================
        // HEADER
        // ======================================================

        JPanel headerPanel =
                new JPanel();

        headerPanel.setOpaque(false);

        headerPanel.setLayout(
                new BoxLayout(
                        headerPanel,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel title =
                new JLabel(
                        "Dashboard"
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        title.setForeground(
                TEXT
        );


        JLabel subtitle =
                new JLabel(
                        "Overview of your pharmacy management system"
                );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(
                MUTED
        );


        headerPanel.add(
                title
        );

        headerPanel.add(
                Box.createVerticalStrut(5)
        );

        headerPanel.add(
                subtitle
        );


        add(
                headerPanel,
                BorderLayout.NORTH
        );


        // ======================================================
        // SUMMARY CARDS
        // ======================================================

        int medicineCount =
                medicineDAO
                        .getAllMedicines()
                        .size();


        int supplierCount =
                supplierDAO
                        .getAllSuppliers()
                        .size();


        int userCount =
                userDAO
                        .getAllUsers()
                        .size();


        int lowStockCount =
                getLowStockCount();


        JPanel cardsPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                18,
                                0
                        )
                );

        cardsPanel.setOpaque(false);

        cardsPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        0,
                        30,
                        0
                )
        );


        cardsPanel.add(
                createDashboardCard(
                        "Medicines",
                        String.valueOf(
                                medicineCount
                        ),
                        "Total medicines"
                )
        );


        cardsPanel.add(
                createDashboardCard(
                        "Suppliers",
                        String.valueOf(
                                supplierCount
                        ),
                        "Registered suppliers"
                )
        );


        cardsPanel.add(
                createDashboardCard(
                        "Users",
                        String.valueOf(
                                userCount
                        ),
                        "System users"
                )
        );


        cardsPanel.add(
                createDashboardCard(
                        "Low Stock",
                        String.valueOf(
                                lowStockCount
                        ),
                        "Items needing attention"
                )
        );


        add(
                cardsPanel,
                BorderLayout.CENTER
        );


        // ======================================================
        // QUICK OVERVIEW
        // ======================================================

        JPanel quickPanel =
                new JPanel(
                        new BorderLayout()
                );

        quickPanel.setBackground(
                WHITE
        );

        quickPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );


        JLabel quickTitle =
                new JLabel(
                        "Quick Overview"
                );

        quickTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        quickTitle.setForeground(
                TEXT
        );


        JLabel quickText =
                new JLabel(
                        "Use the navigation menu to manage medicines, suppliers and users."
                );

        quickText.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        quickText.setForeground(
                MUTED
        );


        JPanel quickContent =
                new JPanel();

        quickContent.setOpaque(false);

        quickContent.setLayout(
                new BoxLayout(
                        quickContent,
                        BoxLayout.Y_AXIS
                )
        );


        quickContent.add(
                quickTitle
        );

        quickContent.add(
                Box.createVerticalStrut(8)
        );

        quickContent.add(
                quickText
        );


        quickPanel.add(
                quickContent,
                BorderLayout.CENTER
        );


        add(
                quickPanel,
                BorderLayout.SOUTH
        );
    }


    // ==========================================================
    // DASHBOARD CARD
    // ==========================================================

    private JPanel createDashboardCard(
            String title,
            String value,
            String description
    ) {

        JPanel card =
                new JPanel();

        card.setBackground(
                WHITE
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                18,
                                18,
                                18,
                                18
                        )
                )
        );

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        titleLabel.setForeground(
                MUTED
        );


        JLabel valueLabel =
                new JLabel(
                        value
                );

        valueLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        valueLabel.setForeground(
                PRIMARY
        );


        JLabel descriptionLabel =
                new JLabel(
                        description
                );

        descriptionLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        descriptionLabel.setForeground(
                MUTED
        );


        card.add(
                titleLabel
        );

        card.add(
                Box.createVerticalStrut(8)
        );

        card.add(
                valueLabel
        );

        card.add(
                Box.createVerticalStrut(5)
        );

        card.add(
                descriptionLabel
        );


        return card;
    }


    // ==========================================================
    // LOW STOCK COUNT
    // ==========================================================

    private int getLowStockCount() {

        int count = 0;


        for (Medicine medicine :
                medicineDAO.getAllMedicines()) {

            if (medicine.getQuantityInStock()
                    <= medicine.getReorderLevel()) {

                count++;
            }
        }


        return count;
    }
}