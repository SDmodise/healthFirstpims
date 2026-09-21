package pims.gui;

import pims.gui.admin.DashboardPanel;
import pims.components.Sidebar;
import pims.util.DBConnection;
import pims.dao.MedicineDAO;
import pims.dao.SupplierDAO;
import pims.dao.UserDAO;
import pims.model.Medicine;
import pims.model.Supplier;
import pims.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDashboard extends JFrame {

    // ==========================================================
    // COLORS
    // ==========================================================

    private static final Color BACKGROUND =
            new Color(243, 246, 249);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color PRIMARY =
            new Color(27, 61, 88);

    private static final Color SECONDARY =
            new Color(45, 155, 105);

    private static final Color TEXT =
            new Color(45, 55, 65);

    private static final Color MUTED =
            new Color(110, 120, 130);

    private static final Color BORDER =
            new Color(225, 230, 235);


    // ==========================================================
    // FIELDS
    // ==========================================================

    private JPanel contentPanel;

    private MedicineDAO medicineDAO;
    private SupplierDAO supplierDAO;
    private UserDAO userDAO;


    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================

    public AdminDashboard() {

        setTitle("HealthFirst PIMS - Admin Dashboard");

        setSize(1200, 750);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());


        // ======================================================
        // INITIALIZE DAOs
        // ======================================================

        medicineDAO = new MedicineDAO();
        supplierDAO = new SupplierDAO();
        userDAO = new UserDAO();


        // ======================================================
        // SIDEBAR
        // ======================================================

        Sidebar sidebar = new Sidebar(item -> {

            switch (item) {

                case "Dashboard":
                    showDashboard();
                    break;

                case "Medicines":
                    showContent(createMedicinesPanel());
                    break;

                case "Suppliers":
                    showContent(createSuppliersPanel());
                    break;

                case "Users":
                    showContent(createUsersPanel());
                    break;

                case "Sales":
                    showPlaceholder(
                            "Sales",
                            "Sales management will be available here."
                    );
                    break;

                case "Sales History":
                    showSalesHistoryPanel();
                    break;

                case "Reports":
                    showPlaceholder(
                            "Reports",
                            "Reports will be available here."
                    );
                    break;

                case "Alerts":
                    showAlertsPanel();
                    break;

                case "Settings":
                    showSettingsPanel();
                    break;

                case "Logout":
                    logout();
                    break;
            }
        });


        add(
                sidebar,
                BorderLayout.WEST
        );


        // ======================================================
        // CONTENT AREA
        // ======================================================

        contentPanel = new JPanel(
                new BorderLayout()
        );

        contentPanel.setBackground(
                BACKGROUND
        );


        add(
                contentPanel,
                BorderLayout.CENTER
        );


        // ======================================================
        // INITIAL SCREEN
        // ======================================================

        showDashboard();


        setVisible(true);
    }


    // ==========================================================
    // CONTENT SWITCHING
    // ==========================================================

    private void showContent(JPanel panel) {

        contentPanel.removeAll();

        contentPanel.add(
                panel,
                BorderLayout.CENTER
        );

        contentPanel.revalidate();

        contentPanel.repaint();
    }


    // ==========================================================
    // DASHBOARD
    // ==========================================================


    private void showDashboard() {

        showContent(
                new DashboardPanel(
                        medicineDAO,
                        supplierDAO,
                        userDAO
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
                new JLabel("Dashboard");

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


        headerPanel.add(title);

        headerPanel.add(
                Box.createVerticalStrut(5)
        );

        headerPanel.add(subtitle);


        // ======================================================
        // SUMMARY CARDS
        // ======================================================

        int medicineCount =
                medicineDAO.getAllMedicines().size();

        int supplierCount =
                supplierDAO.getAllSuppliers().size();

        int userCount =
                userDAO.getAllUsers().size();

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
                        String.valueOf(medicineCount),
                        "Total medicines"
                )
        );


        cardsPanel.add(
                createDashboardCard(
                        "Suppliers",
                        String.valueOf(supplierCount),
                        "Registered suppliers"
                )
        );


        cardsPanel.add(
                createDashboardCard(
                        "Users",
                        String.valueOf(userCount),
                        "System users"
                )
        );


        cardsPanel.add(
                createDashboardCard(
                        "Low Stock",
                        String.valueOf(lowStockCount),
                        "Items needing attention"
                )
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
                new JLabel(title);

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
                new JLabel(value);

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
                new JLabel(description);

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


    // ==========================================================
    // MEDICINES
    // ==========================================================

    private JPanel createMedicinesPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panel.setBackground(
                BACKGROUND
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        25,
                        25,
                        25
                )
        );


        // ======================================================
        // HEADER
        // ======================================================

        JPanel header =
                createSectionHeader(
                        "Medicines",
                        "Manage pharmacy medicine inventory"
                );


        panel.add(
                header,
                BorderLayout.NORTH
        );


        // ======================================================
        // BUTTON PANEL
        // ======================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                8,
                                10
                        )
                );

        buttonPanel.setOpaque(false);


        JButton addBtn =
                createPrimaryButton(
                        "Add Medicine"
                );

        JButton editBtn =
                createSecondaryButton(
                        "Edit Medicine"
                );

        JButton deleteBtn =
                createDangerButton(
                        "Delete Medicine"
                );

        JButton refreshBtn =
                createSecondaryButton(
                        "Refresh"
                );


        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);


        // ======================================================
        // TABLE
        // ======================================================

        DefaultTableModel medicineTableModel =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Name",
                                "Company",
                                "Type",
                                "Price",
                                "Qty",
                                "Reorder Level",
                                "Expiry Date",
                                "Supplier ID"
                        },
                        0
                );


        JTable medicineTable =
                new JTable(
                        medicineTableModel
                );


        styleTable(
                medicineTable
        );


        JScrollPane scrollPane =
                new JScrollPane(
                        medicineTable
                );


        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );


        loadMedicinesTable(
                medicineTableModel
        );


        // ======================================================
        // BUTTON ACTIONS
        // ======================================================

        refreshBtn.addActionListener(
                e ->
                        loadMedicinesTable(
                                medicineTableModel
                        )
        );


        addBtn.addActionListener(
                e ->
                        showAddMedicineDialog(
                                medicineTableModel
                        )
        );


        deleteBtn.addActionListener(e -> {

            int selectedRow =
                    medicineTable.getSelectedRow();


            if (selectedRow >= 0) {

                int medicineId =
                        (int) medicineTableModel
                                .getValueAt(
                                        selectedRow,
                                        0
                                );


                int choice =
                        JOptionPane.showConfirmDialog(
                                this,
                                "Are you sure you want to delete this medicine?",
                                "Confirm Delete",
                                JOptionPane.YES_NO_OPTION
                        );


                if (choice ==
                        JOptionPane.YES_OPTION) {

                    if (medicineDAO
                            .deleteMedicine(
                                    medicineId
                            )) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Medicine deleted successfully!"
                        );

                        loadMedicinesTable(
                                medicineTableModel
                        );

                    } else {

                        JOptionPane.showMessageDialog(
                                this,
                                "Failed to delete medicine!"
                        );
                    }
                }

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a medicine to delete!"
                );
            }
        });


        editBtn.addActionListener(e -> {

            int selectedRow =
                    medicineTable.getSelectedRow();


            if (selectedRow >= 0) {

                int medicineId =
                        (int) medicineTableModel
                                .getValueAt(
                                        selectedRow,
                                        0
                                );


                showEditMedicineDialog(
                        medicineId,
                        medicineTableModel
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a medicine to edit!"
                );
            }
        });


        // ======================================================
        // CENTER CONTENT
        // ======================================================

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout()
                );

        centerPanel.setOpaque(false);


        centerPanel.add(
                buttonPanel,
                BorderLayout.NORTH
        );

        centerPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );


        panel.add(
                centerPanel,
                BorderLayout.CENTER
        );


        return panel;
    }


    private void loadMedicinesTable(
            DefaultTableModel model
    ) {

        model.setRowCount(0);


        for (Medicine med :
                medicineDAO.getAllMedicines()) {

            model.addRow(
                    new Object[]{
                            med.getMedicineId(),
                            med.getName(),
                            med.getCompany(),
                            med.getMedicineType(),
                            med.getPrice(),
                            med.getQuantityInStock(),
                            med.getReorderLevel(),
                            med.getExpiryDate(),
                            med.getSupplierId()
                    }
            );
        }
    }


    // ==========================================================
    // ADD MEDICINE
    // ==========================================================

    private void showAddMedicineDialog(
            DefaultTableModel tableModel
    ) {

        JDialog dialog =
                new JDialog(
                        this,
                        "Add Medicine",
                        true
                );


        dialog.setSize(
                430,
                430
        );

        dialog.setLocationRelativeTo(
                this
        );


        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );


        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        6,
                        6,
                        6,
                        6
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;


        JTextField nameField =
                new JTextField(15);

        JTextField companyField =
                new JTextField(15);

        JTextField typeField =
                new JTextField(15);

        JTextField priceField =
                new JTextField(15);

        JTextField qtyField =
                new JTextField(15);

        JTextField reorderField =
                new JTextField(15);

        JTextField expiryField =
                new JTextField(15);

        JTextField supplierIdField =
                new JTextField(15);


        addFormRow(
                panel,
                gbc,
                0,
                "Name:",
                nameField
        );

        addFormRow(
                panel,
                gbc,
                1,
                "Company:",
                companyField
        );

        addFormRow(
                panel,
                gbc,
                2,
                "Type:",
                typeField
        );

        addFormRow(
                panel,
                gbc,
                3,
                "Price:",
                priceField
        );

        addFormRow(
                panel,
                gbc,
                4,
                "Quantity:",
                qtyField
        );

        addFormRow(
                panel,
                gbc,
                5,
                "Reorder Level:",
                reorderField
        );

        addFormRow(
                panel,
                gbc,
                6,
                "Expiry Date:",
                expiryField
        );

        addFormRow(
                panel,
                gbc,
                7,
                "Supplier ID:",
                supplierIdField
        );


        JLabel dateHint =
                new JLabel(
                        "Format: YYYY-MM-DD"
                );

        dateHint.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        dateHint.setForeground(
                MUTED
        );


        gbc.gridx = 1;
        gbc.gridy = 8;

        panel.add(
                dateHint,
                gbc
        );


        JButton saveBtn =
                createPrimaryButton(
                        "Save Medicine"
                );


        gbc.gridx = 0;
        gbc.gridy = 9;

        gbc.gridwidth = 2;

        gbc.insets =
                new Insets(
                        15,
                        6,
                        6,
                        6
                );


        panel.add(
                saveBtn,
                gbc
        );


        saveBtn.addActionListener(e -> {

            try {

                Medicine med =
                        new Medicine(
                                0,
                                nameField.getText(),
                                companyField.getText(),
                                typeField.getText(),
                                Double.parseDouble(
                                        priceField.getText()
                                ),
                                Integer.parseInt(
                                        qtyField.getText()
                                ),
                                Integer.parseInt(
                                        reorderField.getText()
                                ),
                                LocalDate.parse(
                                        expiryField.getText()
                                ),
                                Integer.parseInt(
                                        supplierIdField.getText()
                                )
                        );


                if (medicineDAO.addMedicine(
                        med
                )) {

                    JOptionPane.showMessageDialog(
                            dialog,
                            "Medicine added successfully!"
                    );

                    loadMedicinesTable(
                            tableModel
                    );

                    dialog.dispose();
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Error: " + ex.getMessage()
                );
            }
        });


        dialog.add(
                panel
        );

        dialog.setVisible(
                true
        );
    }


    // ==========================================================
    // EDIT MEDICINE
    // ==========================================================

    private void showEditMedicineDialog(
            int medicineId,
            DefaultTableModel tableModel
    ) {

        Medicine med =
                medicineDAO.getMedicineById(
                        medicineId
                );


        if (med == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Medicine not found!"
            );

            return;
        }


        JDialog dialog =
                new JDialog(
                        this,
                        "Edit Medicine",
                        true
                );


        dialog.setSize(
                430,
                430
        );

        dialog.setLocationRelativeTo(
                this
        );


        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );


        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        6,
                        6,
                        6,
                        6
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;


        JTextField nameField =
                new JTextField(
                        med.getName(),
                        15
                );

        JTextField companyField =
                new JTextField(
                        med.getCompany(),
                        15
                );

        JTextField typeField =
                new JTextField(
                        med.getMedicineType(),
                        15
                );

        JTextField priceField =
                new JTextField(
                        String.valueOf(
                                med.getPrice()
                        ),
                        15
                );

        JTextField qtyField =
                new JTextField(
                        String.valueOf(
                                med.getQuantityInStock()
                        ),
                        15
                );

        JTextField reorderField =
                new JTextField(
                        String.valueOf(
                                med.getReorderLevel()
                        ),
                        15
                );


        String expiryText =
                med.getExpiryDate() == null
                        ? ""
                        : med.getExpiryDate().toString();


        JTextField expiryField =
                new JTextField(
                        expiryText,
                        15
                );


        JTextField supplierIdField =
                new JTextField(
                        String.valueOf(
                                med.getSupplierId()
                        ),
                        15
                );


        addFormRow(
                panel,
                gbc,
                0,
                "Name:",
                nameField
        );

        addFormRow(
                panel,
                gbc,
                1,
                "Company:",
                companyField
        );

        addFormRow(
                panel,
                gbc,
                2,
                "Type:",
                typeField
        );

        addFormRow(
                panel,
                gbc,
                3,
                "Price:",
                priceField
        );

        addFormRow(
                panel,
                gbc,
                4,
                "Quantity:",
                qtyField
        );

        addFormRow(
                panel,
                gbc,
                5,
                "Reorder Level:",
                reorderField
        );

        addFormRow(
                panel,
                gbc,
                6,
                "Expiry Date:",
                expiryField
        );

        addFormRow(
                panel,
                gbc,
                7,
                "Supplier ID:",
                supplierIdField
        );


        JLabel dateHint =
                new JLabel(
                        "Format: YYYY-MM-DD"
                );

        dateHint.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        dateHint.setForeground(
                MUTED
        );


        gbc.gridx = 1;
        gbc.gridy = 8;

        panel.add(
                dateHint,
                gbc
        );


        JButton updateBtn =
                createPrimaryButton(
                        "Update Medicine"
                );


        gbc.gridx = 0;
        gbc.gridy = 9;

        gbc.gridwidth = 2;

        gbc.insets =
                new Insets(
                        15,
                        6,
                        6,
                        6
                );


        panel.add(
                updateBtn,
                gbc
        );


        updateBtn.addActionListener(e -> {

            try {

                med.setName(
                        nameField.getText()
                );

                med.setCompany(
                        companyField.getText()
                );

                med.setMedicineType(
                        typeField.getText()
                );

                med.setPrice(
                        Double.parseDouble(
                                priceField.getText()
                        )
                );

                med.setQuantityInStock(
                        Integer.parseInt(
                                qtyField.getText()
                        )
                );

                med.setReorderLevel(
                        Integer.parseInt(
                                reorderField.getText()
                        )
                );


                String expiry =
                        expiryField.getText().trim();


                if (expiry.isEmpty()) {

                    med.setExpiryDate(
                            null
                    );

                } else {

                    med.setExpiryDate(
                            LocalDate.parse(
                                    expiry
                            )
                    );
                }


                med.setSupplierId(
                        Integer.parseInt(
                                supplierIdField.getText()
                        )
                );


                if (medicineDAO.updateMedicine(
                        med
                )) {

                    JOptionPane.showMessageDialog(
                            dialog,
                            "Medicine updated successfully!"
                    );

                    loadMedicinesTable(
                            tableModel
                    );

                    dialog.dispose();
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Error: " + ex.getMessage()
                );
            }
        });


        dialog.add(
                panel
        );

        dialog.setVisible(
                true
        );
    }


    // ==========================================================
    // SUPPLIERS
    // ==========================================================

    private JPanel createSuppliersPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panel.setBackground(
                BACKGROUND
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        25,
                        25,
                        25
                )
        );


        JPanel header =
                createSectionHeader(
                        "Suppliers",
                        "Manage pharmacy suppliers"
                );


        panel.add(
                header,
                BorderLayout.NORTH
        );


        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                8,
                                10
                        )
                );

        buttonPanel.setOpaque(false);


        JButton addBtn =
                createPrimaryButton(
                        "Add Supplier"
                );

        JButton deleteBtn =
                createDangerButton(
                        "Delete Supplier"
                );

        JButton refreshBtn =
                createSecondaryButton(
                        "Refresh"
                );


        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);


        DefaultTableModel supplierTableModel =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Name",
                                "Contact Person",
                                "Phone",
                                "Email",
                                "Address"
                        },
                        0
                );


        JTable supplierTable =
                new JTable(
                        supplierTableModel
                );


        styleTable(
                supplierTable
        );


        JScrollPane scrollPane =
                new JScrollPane(
                        supplierTable
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );


        loadSuppliersTable(
                supplierTableModel
        );


        refreshBtn.addActionListener(
                e ->
                        loadSuppliersTable(
                                supplierTableModel
                        )
        );


        addBtn.addActionListener(
                e ->
                        showAddSupplierDialog(
                                supplierTableModel
                        )
        );


        deleteBtn.addActionListener(e -> {

            int selectedRow =
                    supplierTable.getSelectedRow();


            if (selectedRow >= 0) {

                int supplierId =
                        (int) supplierTableModel
                                .getValueAt(
                                        selectedRow,
                                        0
                                );


                int choice =
                        JOptionPane.showConfirmDialog(
                                this,
                                "Are you sure you want to delete this supplier?",
                                "Confirm Delete",
                                JOptionPane.YES_NO_OPTION
                        );


                if (choice ==
                        JOptionPane.YES_OPTION) {

                    if (supplierDAO.deleteSupplier(
                            supplierId
                    )) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Supplier deleted successfully!"
                        );

                        loadSuppliersTable(
                                supplierTableModel
                        );

                    } else {

                        JOptionPane.showMessageDialog(
                                this,
                                "Failed to delete supplier!"
                        );
                    }
                }

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a supplier to delete!"
                );
            }
        });


        JPanel centerPanel =
                new JPanel(
                        new BorderLayout()
                );

        centerPanel.setOpaque(false);


        centerPanel.add(
                buttonPanel,
                BorderLayout.NORTH
        );

        centerPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );


        panel.add(
                centerPanel,
                BorderLayout.CENTER
        );


        return panel;
    }


    private void loadSuppliersTable(
            DefaultTableModel model
    ) {

        model.setRowCount(0);


        for (Supplier sup :
                supplierDAO.getAllSuppliers()) {

            model.addRow(
                    new Object[]{
                            sup.getSupplierId(),
                            sup.getName(),
                            sup.getContactPerson(),
                            sup.getPhone(),
                            sup.getEmail(),
                            sup.getAddress()
                    }
            );
        }
    }


    // ==========================================================
    // ADD SUPPLIER
    // ==========================================================

    private void showAddSupplierDialog(
            DefaultTableModel tableModel
    ) {

        JDialog dialog =
                new JDialog(
                        this,
                        "Add Supplier",
                        true
                );


        dialog.setSize(
                430,
                330
        );

        dialog.setLocationRelativeTo(
                this
        );


        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );


        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        6,
                        6,
                        6,
                        6
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;


        JTextField nameField =
                new JTextField(15);

        JTextField contactField =
                new JTextField(15);

        JTextField phoneField =
                new JTextField(15);

        JTextField emailField =
                new JTextField(15);

        JTextField addressField =
                new JTextField(15);


        addFormRow(
                panel,
                gbc,
                0,
                "Name:",
                nameField
        );

        addFormRow(
                panel,
                gbc,
                1,
                "Contact Person:",
                contactField
        );

        addFormRow(
                panel,
                gbc,
                2,
                "Phone:",
                phoneField
        );

        addFormRow(
                panel,
                gbc,
                3,
                "Email:",
                emailField
        );

        addFormRow(
                panel,
                gbc,
                4,
                "Address:",
                addressField
        );


        JButton saveBtn =
                createPrimaryButton(
                        "Save Supplier"
                );


        gbc.gridx = 0;
        gbc.gridy = 5;

        gbc.gridwidth = 2;

        gbc.insets =
                new Insets(
                        15,
                        6,
                        6,
                        6
                );


        panel.add(
                saveBtn,
                gbc
        );


        saveBtn.addActionListener(e -> {

            try {

                Supplier sup =
                        new Supplier(
                                0,
                                nameField.getText(),
                                contactField.getText(),
                                phoneField.getText(),
                                emailField.getText(),
                                addressField.getText()
                        );


                if (supplierDAO.addSupplier(
                        sup
                )) {

                    JOptionPane.showMessageDialog(
                            dialog,
                            "Supplier added successfully!"
                    );

                    loadSuppliersTable(
                            tableModel
                    );

                    dialog.dispose();
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Error: " + ex.getMessage()
                );
            }
        });


        dialog.add(
                panel
        );

        dialog.setVisible(
                true
        );
    }


    // ==========================================================
    // USERS
    // ==========================================================

    private JPanel createUsersPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panel.setBackground(
                BACKGROUND
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        25,
                        25,
                        25
                )
        );


        JPanel header =
                createSectionHeader(
                        "Users",
                        "Manage HealthFirst PIMS system users"
                );


        panel.add(
                header,
                BorderLayout.NORTH
        );


        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                8,
                                10
                        )
                );

        buttonPanel.setOpaque(false);


        JButton addBtn =
                createPrimaryButton(
                        "Add Cashier"
                );

        JButton deleteBtn =
                createDangerButton(
                        "Delete User"
                );

        JButton refreshBtn =
                createSecondaryButton(
                        "Refresh"
                );


        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);


        DefaultTableModel userTableModel =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Username",
                                "Full Name",
                                "Role"
                        },
                        0
                );


        JTable userTable =
                new JTable(
                        userTableModel
                );


        styleTable(
                userTable
        );


        JScrollPane scrollPane =
                new JScrollPane(
                        userTable
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );


        loadUsersTable(
                userTableModel
        );


        refreshBtn.addActionListener(
                e ->
                        loadUsersTable(
                                userTableModel
                        )
        );


        addBtn.addActionListener(
                e ->
                        showAddUserDialog(
                                userTableModel
                        )
        );


        deleteBtn.addActionListener(e -> {

            int selectedRow =
                    userTable.getSelectedRow();


            if (selectedRow >= 0) {

                int userId =
                        (int) userTableModel
                                .getValueAt(
                                        selectedRow,
                                        0
                                );


                int choice =
                        JOptionPane.showConfirmDialog(
                                this,
                                "Are you sure you want to delete this user?",
                                "Confirm Delete",
                                JOptionPane.YES_NO_OPTION
                        );


                if (choice ==
                        JOptionPane.YES_OPTION) {

                    if (userDAO.deleteUser(
                            userId
                    )) {

                        JOptionPane.showMessageDialog(
                                this,
                                "User deleted successfully!"
                        );

                        loadUsersTable(
                                userTableModel
                        );

                    } else {

                        JOptionPane.showMessageDialog(
                                this,
                                "Failed to delete user!"
                        );
                    }
                }

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a user to delete!"
                );
            }
        });


        JPanel centerPanel =
                new JPanel(
                        new BorderLayout()
                );

        centerPanel.setOpaque(false);


        centerPanel.add(
                buttonPanel,
                BorderLayout.NORTH
        );

        centerPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );


        panel.add(
                centerPanel,
                BorderLayout.CENTER
        );


        return panel;
    }


    private void loadUsersTable(
            DefaultTableModel model
    ) {

        model.setRowCount(0);


        for (User user :
                userDAO.getAllUsers()) {

            model.addRow(
                    new Object[]{
                            user.getUserId(),
                            user.getUsername(),
                            user.getFullName(),
                            user.getRole()
                    }
            );
        }
    }


    // ==========================================================
    // ADD USER / CASHIER
    // ==========================================================

    private void showAddUserDialog(
            DefaultTableModel tableModel
    ) {

        JDialog dialog =
                new JDialog(
                        this,
                        "Add Cashier",
                        true
                );


        dialog.setSize(
                400,
                280
        );

        dialog.setLocationRelativeTo(
                this
        );


        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );


        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        6,
                        6,
                        6,
                        6
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;


        JTextField usernameField =
                new JTextField(15);

        JPasswordField passwordField =
                new JPasswordField(15);

        JTextField fullNameField =
                new JTextField(15);


        addFormRow(
                panel,
                gbc,
                0,
                "Username:",
                usernameField
        );

        addFormRow(
                panel,
                gbc,
                1,
                "Password:",
                passwordField
        );

        addFormRow(
                panel,
                gbc,
                2,
                "Full Name:",
                fullNameField
        );


        JButton saveBtn =
                createPrimaryButton(
                        "Save Cashier"
                );


        gbc.gridx = 0;
        gbc.gridy = 3;

        gbc.gridwidth = 2;

        gbc.insets =
                new Insets(
                        15,
                        6,
                        6,
                        6
                );


        panel.add(
                saveBtn,
                gbc
        );


        saveBtn.addActionListener(e -> {

            try {

                User user =
                        new User(
                                0,
                                usernameField.getText(),
                                new String(
                                        passwordField
                                                .getPassword()
                                ),
                                "Cashier",
                                fullNameField.getText()
                        );


                if (userDAO.addUser(
                        user
                )) {

                    JOptionPane.showMessageDialog(
                            dialog,
                            "Cashier added successfully!"
                    );

                    loadUsersTable(
                            tableModel
                    );

                    dialog.dispose();
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Error: " + ex.getMessage()
                );
            }
        });


        dialog.add(
                panel
        );

        dialog.setVisible(
                true
        );
    }


    // ==========================================================
    // SALES HISTORY
    // ==========================================================

    private void showSalesHistoryPanel() {

        JPanel panel =
                new JPanel(new BorderLayout(10, 10));

        panel.setBackground(BACKGROUND);
        panel.setBorder(
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        JPanel header =
                createSectionHeader(
                        "Sales History",
                        "View all completed pharmacy sales"
                );

        panel.add(header, BorderLayout.NORTH);

        JPanel buttonPanel =
                new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        buttonPanel.setOpaque(false);

        JButton viewButton =
                createPrimaryButton("View Sale");

        JButton refreshButton =
                createSecondaryButton("Refresh");

        buttonPanel.add(viewButton);
        buttonPanel.add(refreshButton);

        DefaultTableModel model =
                new DefaultTableModel(
                        new String[]{
                                "Sale ID",
                                "Date",
                                "Cashier",
                                "Total"
                        },
                        0
                ) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

        JTable table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        loadSalesHistoryTable(model);

        refreshButton.addActionListener(
                e -> loadSalesHistoryTable(model)
        );

        viewButton.addActionListener(e -> {

            int selectedRow = table.getSelectedRow();

            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a sale to view.",
                        "No Sale Selected",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            int saleId =
                    (int) model.getValueAt(selectedRow, 0);

            showSaleDetailsDialog(saleId);
        });

        JPanel centerPanel =
                new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        centerPanel.add(
                buttonPanel,
                BorderLayout.NORTH
        );

        centerPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        panel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        showContent(panel);
    }


    private void loadSalesHistoryTable(
            DefaultTableModel model
    ) {

        model.setRowCount(0);

        String sql =
                "SELECT s.sale_id, s.sale_date, "
                        + "s.total_amount, "
                        + "COALESCE(u.full_name, 'Unknown') AS cashier "
                        + "FROM sales s "
                        + "LEFT JOIN users u ON s.user_id = u.user_id "
                        + "ORDER BY s.sale_date DESC";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("sale_id"),
                        rs.getTimestamp("sale_date"),
                        rs.getString("cashier"),
                        String.format(
                                "R%.2f",
                                rs.getDouble("total_amount")
                        )
                });
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load sales history: "
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    private void showSaleDetailsDialog(int saleId) {

        JDialog dialog =
                new JDialog(
                        this,
                        "Sale #" + saleId,
                        true
                );

        dialog.setSize(750, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        mainPanel.setBackground(BACKGROUND);

        JLabel title =
                new JLabel("Sale #" + saleId + " Details");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        title.setForeground(TEXT);

        mainPanel.add(title, BorderLayout.NORTH);

        DefaultTableModel model =
                new DefaultTableModel(
                        new String[]{
                                "Medicine",
                                "Quantity",
                                "Price",
                                "Subtotal"
                        },
                        0
                ) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

        JTable table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        double total = loadSaleDetails(saleId, model);

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        JPanel bottomPanel =
                new JPanel(new BorderLayout());

        bottomPanel.setOpaque(false);

        JLabel totalLabel =
                new JLabel(
                        String.format(
                                "Total: R%.2f",
                                total
                        )
                );

        totalLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        totalLabel.setForeground(PRIMARY);

        JButton closeButton =
                createSecondaryButton("Close");

        closeButton.addActionListener(
                e -> dialog.dispose()
        );

        bottomPanel.add(
                totalLabel,
                BorderLayout.WEST
        );

        bottomPanel.add(
                closeButton,
                BorderLayout.EAST
        );

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }


    private double loadSaleDetails(
            int saleId,
            DefaultTableModel model
    ) {

        double total = 0.0;

        String sql =
                "SELECT m.name, si.quantity_sold, "
                        + "si.price_at_sale "
                        + "FROM sale_items si "
                        + "JOIN medicines m "
                        + "ON si.medicine_id = m.medicine_id "
                        + "WHERE si.sale_id = ? "
                        + "ORDER BY si.sale_item_id";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, saleId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    int quantity =
                            rs.getInt("quantity_sold");

                    double price =
                            rs.getDouble("price_at_sale");

                    double subtotal =
                            quantity * price;

                    total += subtotal;

                    model.addRow(new Object[]{
                            rs.getString("name"),
                            quantity,
                            String.format("R%.2f", price),
                            String.format("R%.2f", subtotal)
                    });
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load sale details: "
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        return total;
    }

    // ==========================================================
// SETTINGS
// ==========================================================

    private void showSettingsPanel() {

        JPanel panel =
                new JPanel(new BorderLayout(15, 15));

        panel.setBackground(BACKGROUND);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        25,
                        25,
                        25
                )
        );


        // ======================================================
        // HEADER
        // ======================================================

        JPanel header =
                createSectionHeader(
                        "Settings",
                        "Manage HealthFirst PIMS system configuration"
                );

        panel.add(
                header,
                BorderLayout.NORTH
        );


        // ======================================================
        // SETTINGS CONTENT
        // ======================================================

        JPanel content =
                new JPanel();

        content.setBackground(BACKGROUND);

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );


        // ======================================================
        // PHARMACY INFORMATION
        // ======================================================

        JPanel pharmacyPanel =
                createSettingsCard(
                        "Pharmacy Information",
                        "Information used throughout the pharmacy system"
                );

        JTextField pharmacyNameField =
                new JTextField(
                        getSetting("pharmacy_name", "HealthFirst Pharmacy")
                );

        JTextField addressField =
                new JTextField(
                        getSetting("pharmacy_address", "")
                );

        JTextField phoneField =
                new JTextField(
                        getSetting("pharmacy_phone", "")
                );

        JTextField emailField =
                new JTextField(
                        getSetting("pharmacy_email", "")
                );

        JTextField registrationField =
                new JTextField(
                        getSetting("pharmacy_registration", "")
                );


        addSettingsRow(
                pharmacyPanel,
                "Pharmacy Name:",
                pharmacyNameField
        );

        addSettingsRow(
                pharmacyPanel,
                "Address:",
                addressField
        );

        addSettingsRow(
                pharmacyPanel,
                "Phone:",
                phoneField
        );

        addSettingsRow(
                pharmacyPanel,
                "Email:",
                emailField
        );

        addSettingsRow(
                pharmacyPanel,
                "Registration No.:",
                registrationField
        );


        JButton savePharmacyButton =
                createPrimaryButton(
                        "Save Pharmacy Information"
                );


        savePharmacyButton.addActionListener(e -> {

            saveSetting(
                    "pharmacy_name",
                    pharmacyNameField.getText().trim()
            );

            saveSetting(
                    "pharmacy_address",
                    addressField.getText().trim()
            );

            saveSetting(
                    "pharmacy_phone",
                    phoneField.getText().trim()
            );

            saveSetting(
                    "pharmacy_email",
                    emailField.getText().trim()
            );

            saveSetting(
                    "pharmacy_registration",
                    registrationField.getText().trim()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Pharmacy information saved successfully.",
                    "Settings Saved",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });


        addSettingsButton(
                pharmacyPanel,
                savePharmacyButton
        );


        content.add(pharmacyPanel);

        content.add(
                Box.createVerticalStrut(15)
        );


        // ======================================================
        // RECEIPT SETTINGS
        // ======================================================

        JPanel receiptPanel =
                createSettingsCard(
                        "Receipt Settings",
                        "Control the information displayed on sales receipts"
                );


        JTextField receiptFooterField =
                new JTextField(
                        getSetting(
                                "receipt_footer",
                                "Thank you for choosing HealthFirst Pharmacy."
                        )
                );


        JCheckBox showCashierCheck =
                createSettingsCheckBox(
                        "Show cashier name on receipt",
                        getSettingBoolean(
                                "receipt_show_cashier",
                                true
                        )
                );


        JCheckBox showDateTimeCheck =
                createSettingsCheckBox(
                        "Show date and time on receipt",
                        getSettingBoolean(
                                "receipt_show_datetime",
                                true
                        )
                );


        addSettingsRow(
                receiptPanel,
                "Receipt Footer:",
                receiptFooterField
        );


        addSettingsComponent(
                receiptPanel,
                showCashierCheck
        );

        addSettingsComponent(
                receiptPanel,
                showDateTimeCheck
        );


        JButton saveReceiptButton =
                createPrimaryButton(
                        "Save Receipt Settings"
                );


        saveReceiptButton.addActionListener(e -> {

            saveSetting(
                    "receipt_footer",
                    receiptFooterField.getText().trim()
            );

            saveSetting(
                    "receipt_show_cashier",
                    String.valueOf(
                            showCashierCheck.isSelected()
                    )
            );

            saveSetting(
                    "receipt_show_datetime",
                    String.valueOf(
                            showDateTimeCheck.isSelected()
                    )
            );


            JOptionPane.showMessageDialog(
                    this,
                    "Receipt settings saved successfully.",
                    "Settings Saved",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });


        addSettingsButton(
                receiptPanel,
                saveReceiptButton
        );


        content.add(receiptPanel);

        content.add(
                Box.createVerticalStrut(15)
        );


        // ======================================================
        // INVENTORY SETTINGS
        // ======================================================

        JPanel inventoryPanel =
                createSettingsCard(
                        "Inventory Settings",
                        "Configure stock and medicine expiry warnings"
                );


        JTextField reorderField =
                new JTextField(
                        getSetting(
                                "default_reorder_level",
                                "10"
                        )
                );


        JTextField expiryWarningField =
                new JTextField(
                        getSetting(
                                "expiry_warning_days",
                                "30"
                        )
                );


        JCheckBox preventExpiredCheck =
                createSettingsCheckBox(
                        "Prevent sales of expired medicines",
                        getSettingBoolean(
                                "prevent_expired_sales",
                                true
                        )
                );


        JCheckBox lowStockCheck =
                createSettingsCheckBox(
                        "Enable low-stock warnings",
                        getSettingBoolean(
                                "low_stock_warnings",
                                true
                        )
                );


        addSettingsRow(
                inventoryPanel,
                "Default Reorder Level:",
                reorderField
        );


        addSettingsRow(
                inventoryPanel,
                "Expiry Warning (days):",
                expiryWarningField
        );


        addSettingsComponent(
                inventoryPanel,
                preventExpiredCheck
        );


        addSettingsComponent(
                inventoryPanel,
                lowStockCheck
        );


        JButton saveInventoryButton =
                createPrimaryButton(
                        "Save Inventory Settings"
                );


        saveInventoryButton.addActionListener(e -> {

            try {

                int reorderLevel =
                        Integer.parseInt(
                                reorderField.getText().trim()
                        );


                int expiryDays =
                        Integer.parseInt(
                                expiryWarningField.getText().trim()
                        );


                if (reorderLevel < 0 ||
                        expiryDays < 0) {

                    throw new NumberFormatException();
                }


                saveSetting(
                        "default_reorder_level",
                        String.valueOf(reorderLevel)
                );


                saveSetting(
                        "expiry_warning_days",
                        String.valueOf(expiryDays)
                );


                saveSetting(
                        "prevent_expired_sales",
                        String.valueOf(
                                preventExpiredCheck.isSelected()
                        )
                );


                saveSetting(
                        "low_stock_warnings",
                        String.valueOf(
                                lowStockCheck.isSelected()
                        )
                );


                JOptionPane.showMessageDialog(
                        this,
                        "Inventory settings saved successfully.",
                        "Settings Saved",
                        JOptionPane.INFORMATION_MESSAGE
                );


            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Reorder level and expiry warning must be valid positive numbers.",
                        "Invalid Settings",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });


        addSettingsButton(
                inventoryPanel,
                saveInventoryButton
        );


        content.add(inventoryPanel);

        content.add(
                Box.createVerticalStrut(15)
        );


        // ======================================================
        // SYSTEM INFORMATION
        // ======================================================

        JPanel systemPanel =
                createSettingsCard(
                        "System Information",
                        "HealthFirst PIMS application information"
                );


        addInformationRow(
                systemPanel,
                "Application:",
                "HealthFirst PIMS"
        );


        addInformationRow(
                systemPanel,
                "Version:",
                "1.0.0"
        );


        addInformationRow(
                systemPanel,
                "Database:",
                "pims_db"
        );


        addInformationRow(
                systemPanel,
                "System Status:",
                "Connected"
        );


        content.add(systemPanel);


        // ======================================================
        // SCROLLABLE CONTENT
        // ======================================================

        JScrollPane scrollPane =
                new JScrollPane(content);

        scrollPane.setBorder(null);

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        scrollPane.setBackground(BACKGROUND);

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );


        showContent(panel);
    }


// ==========================================================
// SETTINGS CARD
// ==========================================================

    private JPanel createSettingsCard(
            String title,
            String subtitle
    ) {

        JPanel card =
                new JPanel();

        card.setBackground(WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                18,
                                20,
                                20,
                                20
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
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        titleLabel.setForeground(TEXT);


        JLabel subtitleLabel =
                new JLabel(subtitle);

        subtitleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        subtitleLabel.setForeground(MUTED);


        card.add(titleLabel);

        card.add(
                Box.createVerticalStrut(4)
        );

        card.add(subtitleLabel);

        card.add(
                Box.createVerticalStrut(15)
        );


        return card;
    }


// ==========================================================
// SETTINGS ROW
// ==========================================================

    private void addSettingsRow(
            JPanel panel,
            String label,
            JComponent field
    ) {

        JPanel row =
                new JPanel(
                        new BorderLayout(
                                15,
                                0
                        )
                );

        row.setOpaque(false);

        row.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        38
                )
        );


        JLabel labelComponent =
                new JLabel(label);

        labelComponent.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        labelComponent.setForeground(TEXT);

        labelComponent.setPreferredSize(
                new Dimension(
                        170,
                        30
                )
        );


        row.add(
                labelComponent,
                BorderLayout.WEST
        );


        row.add(
                field,
                BorderLayout.CENTER
        );


        panel.add(row);

        panel.add(
                Box.createVerticalStrut(8)
        );
    }


// ==========================================================
// SETTINGS CHECKBOX
// ==========================================================

    private JCheckBox createSettingsCheckBox(
            String text,
            boolean selected
    ) {

        JCheckBox checkBox =
                new JCheckBox(
                        text,
                        selected
                );

        checkBox.setOpaque(false);

        checkBox.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        checkBox.setForeground(TEXT);

        checkBox.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        return checkBox;
    }


// ==========================================================
// SETTINGS COMPONENT
// ==========================================================

    private void addSettingsComponent(
            JPanel panel,
            JComponent component
    ) {

        component.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(component);

        panel.add(
                Box.createVerticalStrut(5)
        );
    }


// ==========================================================
// SETTINGS BUTTON
// ==========================================================

    private void addSettingsButton(
            JPanel panel,
            JButton button
    ) {

        button.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(
                Box.createVerticalStrut(5)
        );

        panel.add(button);
    }


// ==========================================================
// INFORMATION ROW
// ==========================================================

    private void addInformationRow(
            JPanel panel,
            String label,
            String value
    ) {

        JPanel row =
                new JPanel(
                        new BorderLayout()
                );

        row.setOpaque(false);

        row.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        30
                )
        );


        JLabel labelLabel =
                new JLabel(label);

        labelLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        labelLabel.setForeground(TEXT);

        labelLabel.setPreferredSize(
                new Dimension(
                        170,
                        30
                )
        );


        JLabel valueLabel =
                new JLabel(value);

        valueLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        valueLabel.setForeground(MUTED);


        row.add(
                labelLabel,
                BorderLayout.WEST
        );

        row.add(
                valueLabel,
                BorderLayout.CENTER
        );


        panel.add(row);

        panel.add(
                Box.createVerticalStrut(5)
        );
    }


// ==========================================================
// GET SETTING
// ==========================================================

    private String getSetting(
            String key,
            String defaultValue
    ) {

        String sql =
                "SELECT setting_value "
                        + "FROM settings "
                        + "WHERE setting_key = ?";


        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setString(
                    1,
                    key
            );


            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                if (rs.next()) {

                    return rs.getString(
                            "setting_value"
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Could not load setting '"
                            + key
                            + "': "
                            + e.getMessage()
            );
        }


        return defaultValue;
    }


// ==========================================================
// GET BOOLEAN SETTING
// ==========================================================

    private boolean getSettingBoolean(
            String key,
            boolean defaultValue
    ) {

        return Boolean.parseBoolean(
                getSetting(
                        key,
                        String.valueOf(
                                defaultValue
                        )
                )
        );
    }


// ==========================================================
// SAVE SETTING
// ==========================================================

    private void saveSetting(
            String key,
            String value
    ) {

        String sql =
                "INSERT INTO settings "
                        + "(setting_key, setting_value) "
                        + "VALUES (?, ?) "
                        + "ON DUPLICATE KEY UPDATE "
                        + "setting_value = VALUES(setting_value)";


        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setString(
                    1,
                    key
            );

            stmt.setString(
                    2,
                    value
            );

            stmt.executeUpdate();


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not save setting: "
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==========================================================
    // ALERTS
    // ==========================================================

    private void showAlertsPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panel.setBackground(
                BACKGROUND
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        25,
                        25,
                        25
                )
        );


        JPanel header =
                createSectionHeader(
                        "Alerts",
                        "Medicines requiring attention"
                );


        panel.add(
                header,
                BorderLayout.NORTH
        );


        DefaultTableModel model =
                new DefaultTableModel(
                        new String[]{
                                "Medicine",
                                "Current Stock",
                                "Reorder Level",
                                "Expiry Date",
                                "Alert"
                        },
                        0
                );


        JTable table =
                new JTable(model);


        styleTable(table);


        for (Medicine medicine :
                medicineDAO.getAllMedicines()) {

            boolean lowStock =
                    medicine.getQuantityInStock()
                            <= medicine.getReorderLevel();


            boolean expired =
                    medicine.getExpiryDate() != null
                            &&
                            medicine.getExpiryDate()
                                    .isBefore(
                                            LocalDate.now()
                                    );


            boolean expiringSoon =
                    medicine.getExpiryDate() != null
                            &&
                            !expired
                            &&
                            !medicine.getExpiryDate()
                                    .isAfter(
                                            LocalDate.now()
                                                    .plusDays(30)
                                    );


            if (lowStock || expired || expiringSoon) {

                String alert;


                if (expired) {

                    alert = "Expired";

                } else if (lowStock && expiringSoon) {

                    alert =
                            "Low stock / Expiring soon";

                } else if (lowStock) {

                    alert = "Low stock";

                } else {

                    alert = "Expiring soon";
                }


                model.addRow(
                        new Object[]{
                                medicine.getName(),
                                medicine.getQuantityInStock(),
                                medicine.getReorderLevel(),
                                medicine.getExpiryDate(),
                                alert
                        }
                );
            }
        }


        JScrollPane scrollPane =
                new JScrollPane(
                        table
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );


        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );


        if (model.getRowCount() == 0) {

            JLabel noAlerts =
                    new JLabel(
                            "No stock or expiry alerts at this time.",
                            SwingConstants.CENTER
                    );

            noAlerts.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            14
                    )
            );

            noAlerts.setForeground(
                    MUTED
            );

            panel.add(
                    noAlerts,
                    BorderLayout.SOUTH
            );
        }


        showContent(
                panel
        );
    }


    // ==========================================================
    // PLACEHOLDER
    // ==========================================================

    private void showPlaceholder(
            String title,
            String message
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                BACKGROUND
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        30,
                        30,
                        30
                )
        );


        JPanel content =
                new JPanel();

        content.setOpaque(false);

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        titleLabel.setForeground(
                TEXT
        );


        JLabel messageLabel =
                new JLabel(message);

        messageLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        messageLabel.setForeground(
                MUTED
        );


        content.add(
                titleLabel
        );

        content.add(
                Box.createVerticalStrut(8)
        );

        content.add(
                messageLabel
        );


        panel.add(
                content,
                BorderLayout.NORTH
        );


        showContent(
                panel
        );
    }


    // ==========================================================
    // SECTION HEADER
    // ==========================================================

    private JPanel createSectionHeader(
            String title,
            String subtitle
    ) {

        JPanel header =
                new JPanel();

        header.setOpaque(false);

        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        26
                )
        );

        titleLabel.setForeground(
                TEXT
        );


        JLabel subtitleLabel =
                new JLabel(subtitle);

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


        header.add(
                titleLabel
        );

        header.add(
                Box.createVerticalStrut(5)
        );

        header.add(
                subtitleLabel
        );


        return header;
    }


    // ==========================================================
    // FORM ROW HELPER
    // ==========================================================

    private void addFormRow(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String label,
            JComponent field
    ) {

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;


        JLabel labelComponent =
                new JLabel(label);

        labelComponent.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        labelComponent.setForeground(
                TEXT
        );


        panel.add(
                labelComponent,
                gbc
        );


        gbc.gridx = 1;
        gbc.weightx = 1;


        panel.add(
                field,
                gbc
        );
    }


    // ==========================================================
    // TABLE STYLING
    // ==========================================================

    private void styleTable(
            JTable table
    ) {

        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        table.setForeground(
                TEXT
        );

        table.setBackground(
                WHITE
        );

        table.setRowHeight(
                30
        );

        table.setSelectionBackground(
                new Color(
                        220,
                        240,
                        231
                )
        );

        table.setSelectionForeground(
                TEXT
        );

        table.setGridColor(
                new Color(
                        235,
                        238,
                        241
                )
        );

        table.setShowVerticalLines(
                false
        );

        table.setIntercellSpacing(
                new Dimension(
                        0,
                        1
                )
        );


        table.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        table.getTableHeader().setForeground(
                TEXT
        );

        table.getTableHeader().setBackground(
                new Color(
                        238,
                        242,
                        245
                )
        );

        table.getTableHeader().setPreferredSize(
                new Dimension(
                        0,
                        35
                )
        );
    }


    // ==========================================================
    // BUTTON STYLING
    // ==========================================================

    private JButton createPrimaryButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                WHITE
        );

        button.setBackground(
                SECONDARY
        );

        button.setFocusPainted(
                false
        );

        button.setBorderPainted(
                false
        );

        button.setOpaque(
                true
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        return button;
    }


    private JButton createSecondaryButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                PRIMARY
        );

        button.setBackground(
                WHITE
        );

        button.setFocusPainted(
                false
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );


        return button;
    }


    private JButton createDangerButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                new Color(
                        170,
                        55,
                        55
                )
        );

        button.setBackground(
                WHITE
        );

        button.setFocusPainted(
                false
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                220,
                                190,
                                190
                        )
                )
        );


        return button;
    }


    // ==========================================================
    // LOGOUT
    // ==========================================================

    private void logout() {

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "Confirm Logout",
                        JOptionPane.YES_NO_OPTION
                );


        if (choice ==
                JOptionPane.YES_OPTION) {

            dispose();

            new LoginFrame();
        }
    }
}
