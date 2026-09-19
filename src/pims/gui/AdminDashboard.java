package pims.gui;

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
import java.time.format.DateTimeFormatter;

public class AdminDashboard extends JFrame {
    private JTabbedPane tabbedPane;
    private MedicineDAO medicineDAO;
    private SupplierDAO supplierDAO;
    private UserDAO userDAO;

    public AdminDashboard() {
        setTitle("Admin Dashboard - HealthFirst PIMS");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize DAOs FIRST
        medicineDAO = new MedicineDAO();
        supplierDAO = new SupplierDAO();
        userDAO = new UserDAO();

        // Create tabs AFTER DAOs are initialized
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Manage Medicines", createMedicinesPanel());
        tabbedPane.addTab("Manage Suppliers", createSuppliersPanel());
        tabbedPane.addTab("Manage Users", createUsersPanel());

        // Bottom panel for Logout
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());

        bottomPanel.add(logoutButton);

        add(tabbedPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ===== MEDICINES TAB =====
    private JPanel createMedicinesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add Medicine");
        JButton editBtn = new JButton("Edit Medicine");
        JButton deleteBtn = new JButton("Delete Medicine");
        JButton refreshBtn = new JButton("Refresh");

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);

        // Table
        DefaultTableModel medicineTableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Company", "Type", "Price", "Qty", "Reorder Level", "Expiry Date", "Supplier ID"},
                0
        );
        JTable medicineTable = new JTable(medicineTableModel);
        JScrollPane scrollPane = new JScrollPane(medicineTable);

        // Load medicines into table
        loadMedicinesTable(medicineTableModel);

        // Button actions
        refreshBtn.addActionListener(e -> loadMedicinesTable(medicineTableModel));

        addBtn.addActionListener(e -> showAddMedicineDialog(medicineTableModel));

        deleteBtn.addActionListener(e -> {
            int selectedRow = medicineTable.getSelectedRow();
            if (selectedRow >= 0) {
                int medicineId = (int) medicineTableModel.getValueAt(selectedRow, 0);
                if (medicineDAO.deleteMedicine(medicineId)) {
                    JOptionPane.showMessageDialog(panel, "Medicine deleted successfully!");
                    loadMedicinesTable(medicineTableModel);
                } else {
                    JOptionPane.showMessageDialog(panel, "Failed to delete medicine!");
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a medicine to delete!");
            }
        });

        editBtn.addActionListener(e -> {
            int selectedRow = medicineTable.getSelectedRow();
            if (selectedRow >= 0) {
                int medicineId = (int) medicineTableModel.getValueAt(selectedRow, 0);
                showEditMedicineDialog(medicineId, medicineTableModel);
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a medicine to edit!");
            }
        });

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadMedicinesTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Medicine med : medicineDAO.getAllMedicines()) {
            model.addRow(new Object[]{
                    med.getMedicineId(),
                    med.getName(),
                    med.getCompany(),
                    med.getMedicineType(),
                    med.getPrice(),
                    med.getQuantityInStock(),
                    med.getReorderLevel(),
                    med.getExpiryDate(),
                    med.getSupplierId()
            });
        }
    }

    private void showAddMedicineDialog(DefaultTableModel tableModel) {
        JDialog dialog = new JDialog(this, "Add Medicine", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(15);
        JTextField companyField = new JTextField(15);
        JTextField typeField = new JTextField(15);
        JTextField priceField = new JTextField(15);
        JTextField qtyField = new JTextField(15);
        JTextField reorderField = new JTextField(15);
        JTextField expiryField = new JTextField(15);
        JTextField supplierIdField = new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Company:"), gbc);
        gbc.gridx = 1;
        panel.add(companyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        panel.add(typeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        panel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        panel.add(qtyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Reorder Level:"), gbc);
        gbc.gridx = 1;
        panel.add(reorderField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Expiry Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        panel.add(expiryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JLabel("Supplier ID:"), gbc);
        gbc.gridx = 1;
        panel.add(supplierIdField, gbc);

        JButton saveBtn = new JButton("Save");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        panel.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            try {
                Medicine med = new Medicine(
                        0,
                        nameField.getText(),
                        companyField.getText(),
                        typeField.getText(),
                        Double.parseDouble(priceField.getText()),
                        Integer.parseInt(qtyField.getText()),
                        Integer.parseInt(reorderField.getText()),
                        LocalDate.parse(expiryField.getText()),
                        Integer.parseInt(supplierIdField.getText())
                );
                if (medicineDAO.addMedicine(med)) {
                    JOptionPane.showMessageDialog(dialog, "Medicine added successfully!");
                    loadMedicinesTable(tableModel);
                    dialog.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showEditMedicineDialog(int medicineId, DefaultTableModel tableModel) {
        Medicine med = medicineDAO.getMedicineById(medicineId);
        if (med == null) {
            JOptionPane.showMessageDialog(this, "Medicine not found!");
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Medicine", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(med.getName(), 15);
        JTextField companyField = new JTextField(med.getCompany(), 15);
        JTextField typeField = new JTextField(med.getMedicineType(), 15);
        JTextField priceField = new JTextField(String.valueOf(med.getPrice()), 15);
        JTextField qtyField = new JTextField(String.valueOf(med.getQuantityInStock()), 15);
        JTextField reorderField = new JTextField(String.valueOf(med.getReorderLevel()), 15);
        JTextField expiryField = new JTextField(med.getExpiryDate().toString(), 15);
        JTextField supplierIdField = new JTextField(String.valueOf(med.getSupplierId()), 15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Company:"), gbc);
        gbc.gridx = 1;
        panel.add(companyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        panel.add(typeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        panel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        panel.add(qtyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Reorder Level:"), gbc);
        gbc.gridx = 1;
        panel.add(reorderField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Expiry Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        panel.add(expiryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JLabel("Supplier ID:"), gbc);
        gbc.gridx = 1;
        panel.add(supplierIdField, gbc);

        JButton updateBtn = new JButton("Update");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        panel.add(updateBtn, gbc);

        updateBtn.addActionListener(e -> {
            try {
                med.setName(nameField.getText());
                med.setCompany(companyField.getText());
                med.setMedicineType(typeField.getText());
                med.setPrice(Double.parseDouble(priceField.getText()));
                med.setQuantityInStock(Integer.parseInt(qtyField.getText()));
                med.setReorderLevel(Integer.parseInt(reorderField.getText()));
                med.setExpiryDate(LocalDate.parse(expiryField.getText()));
                med.setSupplierId(Integer.parseInt(supplierIdField.getText()));

                if (medicineDAO.updateMedicine(med)) {
                    JOptionPane.showMessageDialog(dialog, "Medicine updated successfully!");
                    loadMedicinesTable(tableModel);
                    dialog.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // ===== SUPPLIERS TAB =====
    private JPanel createSuppliersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add Supplier");
        JButton deleteBtn = new JButton("Delete Supplier");
        JButton refreshBtn = new JButton("Refresh");

        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);

        DefaultTableModel supplierTableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Contact Person", "Phone", "Email", "Address"},
                0
        );
        JTable supplierTable = new JTable(supplierTableModel);
        JScrollPane scrollPane = new JScrollPane(supplierTable);

        loadSuppliersTable(supplierTableModel);

        refreshBtn.addActionListener(e -> loadSuppliersTable(supplierTableModel));

        addBtn.addActionListener(e -> showAddSupplierDialog(supplierTableModel));

        deleteBtn.addActionListener(e -> {
            int selectedRow = supplierTable.getSelectedRow();
            if (selectedRow >= 0) {
                int supplierId = (int) supplierTableModel.getValueAt(selectedRow, 0);
                if (supplierDAO.deleteSupplier(supplierId)) {
                    JOptionPane.showMessageDialog(panel, "Supplier deleted successfully!");
                    loadSuppliersTable(supplierTableModel);
                } else {
                    JOptionPane.showMessageDialog(panel, "Failed to delete supplier!");
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a supplier to delete!");
            }
        });

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadSuppliersTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Supplier sup : supplierDAO.getAllSuppliers()) {
            model.addRow(new Object[]{
                    sup.getSupplierId(),
                    sup.getName(),
                    sup.getContactPerson(),
                    sup.getPhone(),
                    sup.getEmail(),
                    sup.getAddress()
            });
        }
    }

    private void showAddSupplierDialog(DefaultTableModel tableModel) {
        JDialog dialog = new JDialog(this, "Add Supplier", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(15);
        JTextField contactField = new JTextField(15);
        JTextField phoneField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField addressField = new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Contact Person:"), gbc);
        gbc.gridx = 1;
        panel.add(contactField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        panel.add(addressField, gbc);

        JButton saveBtn = new JButton("Save");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            try {
                Supplier sup = new Supplier(
                        0,
                        nameField.getText(),
                        contactField.getText(),
                        phoneField.getText(),
                        emailField.getText(),
                        addressField.getText()
                );
                if (supplierDAO.addSupplier(sup)) {
                    JOptionPane.showMessageDialog(dialog, "Supplier added successfully!");
                    loadSuppliersTable(tableModel);
                    dialog.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // ===== USERS TAB =====
    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add Cashier");
        JButton deleteBtn = new JButton("Delete User");
        JButton refreshBtn = new JButton("Refresh");

        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);

        DefaultTableModel userTableModel = new DefaultTableModel(
                new String[]{"ID", "Username", "Full Name", "Role"},
                0
        );
        JTable userTable = new JTable(userTableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        loadUsersTable(userTableModel);

        refreshBtn.addActionListener(e -> loadUsersTable(userTableModel));

        addBtn.addActionListener(e -> showAddUserDialog(userTableModel));

        deleteBtn.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow >= 0) {
                int userId = (int) userTableModel.getValueAt(selectedRow, 0);
                if (userDAO.deleteUser(userId)) {
                    JOptionPane.showMessageDialog(panel, "User deleted successfully!");
                    loadUsersTable(userTableModel);
                } else {
                    JOptionPane.showMessageDialog(panel, "Failed to delete user!");
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Please select a user to delete!");
            }
        });

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadUsersTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (User user : userDAO.getAllUsers()) {
            model.addRow(new Object[]{
                    user.getUserId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.getRole()
            });
        }
    }

    private void showAddUserDialog(DefaultTableModel tableModel) {
        JDialog dialog = new JDialog(this, "Add Cashier", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JTextField fullNameField = new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        panel.add(fullNameField, gbc);

        JButton saveBtn = new JButton("Save");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            try {
                User user = new User(
                        0,
                        usernameField.getText(),
                        new String(passwordField.getPassword()),
                        "Cashier",
                        fullNameField.getText()
                );
                if (userDAO.addUser(user)) {
                    JOptionPane.showMessageDialog(dialog, "Cashier added successfully!");
                    loadUsersTable(tableModel);
                    dialog.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }
    private void logout() {

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame();
        }
    }
}