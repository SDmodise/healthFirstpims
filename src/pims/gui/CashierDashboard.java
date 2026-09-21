package pims.gui;

import pims.dao.MedicineDAO;
import pims.model.Medicine;
import pims.util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CashierDashboard extends JFrame {

    private static final Color BACKGROUND = new Color(243, 246, 249);
    private static final Color WHITE = Color.WHITE;
    private static final Color PRIMARY = new Color(27, 61, 88);
    private static final Color SECONDARY = new Color(45, 155, 105);
    private static final Color TEXT = new Color(45, 55, 65);
    private static final Color MUTED = new Color(110, 120, 130);
    private static final Color BORDER = new Color(225, 230, 235);

    // ==========================================
    // VARIABLES
    // ==========================================

    private final int cashierUserId;
    private final MedicineDAO medicineDAO;

    private JTextField searchField;
    private JTable medicineTable;
    private JTable cartTable;

    private DefaultTableModel medicineTableModel;
    private DefaultTableModel cartTableModel;

    private JLabel totalLabel;

    private List<Medicine> searchResults = new ArrayList<>();


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public CashierDashboard(int cashierUserId) {

        this.cashierUserId = cashierUserId;
        this.medicineDAO = new MedicineDAO();

        setTitle("Cashier POS - HealthFirst PIMS");
        setSize(1150, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();

        setVisible(true);
    }


    // ==========================================
    // CREATE UI
    // ==========================================

    private void createUI() {

        getContentPane().setBackground(BACKGROUND);
        setLayout(new BorderLayout());

        // ==========================================
        // HEADER
        // ==========================================

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WHITE);

        headerPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 0, 1, 0, BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                18, 24, 18, 24
                        )
                )
        );

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel =
                new JLabel("Cashier Point of Sale");

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        titleLabel.setForeground(PRIMARY);

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

        subtitleLabel.setForeground(MUTED);

        subtitleLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        4, 0, 0, 0
                )
        );

        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        JLabel statusLabel =
                new JLabel("  READY  ");

        statusLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        statusLabel.setForeground(SECONDARY);

        statusLabel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                SECONDARY,
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                6, 8, 6, 8
                        )
                )
        );

        headerPanel.add(
                titlePanel,
                BorderLayout.WEST
        );

        headerPanel.add(
                statusLabel,
                BorderLayout.EAST
        );


        // ==========================================
        // SEARCH + MEDICINES
        // ==========================================

        JPanel leftPanel =
                new JPanel(
                        new BorderLayout(12, 12)
                );

        leftPanel.setBackground(BACKGROUND);

        leftPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 18, 18, 9
                )
        );

        JPanel searchPanel =
                createSectionPanel();

        searchPanel.setLayout(
                new BorderLayout(10, 0)
        );

        JLabel searchLabel =
                new JLabel("Search medicine");

        searchLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        searchLabel.setForeground(TEXT);

        JPanel searchInputPanel =
                new JPanel(
                        new BorderLayout(8, 0)
                );

        searchInputPanel.setOpaque(false);

        searchField = new JTextField();

        searchField.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        styleTextField(searchField);

        JButton searchButton =
                createButton(
                        "Search",
                        PRIMARY
                );

        searchButton.addActionListener(
                _ -> searchMedicines()
        );

        searchField.addActionListener(
                _ -> searchMedicines()
        );

        searchInputPanel.add(
                searchField,
                BorderLayout.CENTER
        );

        searchInputPanel.add(
                searchButton,
                BorderLayout.EAST
        );

        searchPanel.add(
                searchLabel,
                BorderLayout.NORTH
        );

        searchPanel.add(
                searchInputPanel,
                BorderLayout.CENTER
        );


        // ==========================================
        // MEDICINE TABLE
        // ==========================================

        medicineTableModel =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Medicine",
                                "Company",
                                "Type",
                                "Price",
                                "Stock",
                                "Expiry"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        medicineTable =
                new JTable(
                        medicineTableModel
                );

        styleTable(medicineTable);

        medicineTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        medicineTable.setRowHeight(30);

        medicineTable
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(45);

        medicineTable
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(150);

        medicineTable
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(125);

        medicineTable
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(85);

        medicineTable
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(80);

        medicineTable
                .getColumnModel()
                .getColumn(5)
                .setPreferredWidth(60);

        medicineTable
                .getColumnModel()
                .getColumn(6)
                .setPreferredWidth(95);

        JScrollPane medicineScroll =
                new JScrollPane(
                        medicineTable
                );

        medicineScroll.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        medicineScroll
                .getViewport()
                .setBackground(WHITE);


        JPanel medicineSection =
                createSectionPanel();

        medicineSection.setLayout(
                new BorderLayout(0, 12)
        );

        JLabel medicineTitle =
                new JLabel(
                        "Available Medicines"
                );

        medicineTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        medicineTitle.setForeground(TEXT);

        JButton addButton =
                createButton(
                        "Add to Cart",
                        SECONDARY
                );

        addButton.addActionListener(
                _ -> addSelectedMedicineToCart()
        );

        JPanel medicineBottom =
                new JPanel(
                        new BorderLayout()
                );

        medicineBottom.setOpaque(false);

        medicineBottom.add(
                addButton,
                BorderLayout.EAST
        );

        medicineSection.add(
                medicineTitle,
                BorderLayout.NORTH
        );

        medicineSection.add(
                medicineScroll,
                BorderLayout.CENTER
        );

        medicineSection.add(
                medicineBottom,
                BorderLayout.SOUTH
        );

        leftPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );

        leftPanel.add(
                medicineSection,
                BorderLayout.CENTER
        );


        // ==========================================
        // CART
        // ==========================================

        JPanel rightPanel =
                new JPanel(
                        new BorderLayout(12, 12)
                );

        rightPanel.setBackground(BACKGROUND);

        rightPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 9, 18, 18
                )
        );

        cartTableModel =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Medicine",
                                "Price",
                                "Quantity",
                                "Subtotal"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        cartTable =
                new JTable(
                        cartTableModel
                );

        styleTable(cartTable);

        cartTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        cartTable.setRowHeight(30);

        cartTable
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(45);

        cartTable
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(165);

        cartTable
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(80);

        cartTable
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(70);

        cartTable
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(90);

        JScrollPane cartScroll =
                new JScrollPane(
                        cartTable
                );

        cartScroll.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        cartScroll
                .getViewport()
                .setBackground(WHITE);

        JPanel cartSection =
                createSectionPanel();

        cartSection.setLayout(
                new BorderLayout(0, 12)
        );

        JLabel cartTitle =
                new JLabel("Shopping Cart");

        cartTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        cartTitle.setForeground(TEXT);

        JPanel cartButtons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                7,
                                0
                        )
                );

        cartButtons.setOpaque(false);

        JButton minusButton =
                createButton(
                        "-",
                        PRIMARY
                );

        JButton plusButton =
                createButton(
                        "+",
                        PRIMARY
                );

        JButton removeButton =
                createButton(
                        "Remove",
                        new Color(
                                180,
                                70,
                                70
                        )
                );

        JButton clearButton =
                createButton(
                        "Clear Cart",
                        new Color(
                                120,
                                130,
                                140
                        )
                );

        minusButton.setPreferredSize(
                new Dimension(42, 34)
        );

        plusButton.setPreferredSize(
                new Dimension(42, 34)
        );

        minusButton.addActionListener(
                _ -> decreaseQuantity()
        );

        plusButton.addActionListener(
                _ -> increaseQuantity()
        );

        removeButton.addActionListener(
                _ -> removeSelectedItem()
        );

        clearButton.addActionListener(
                _ -> clearCart()
        );

        cartButtons.add(minusButton);
        cartButtons.add(plusButton);
        cartButtons.add(removeButton);
        cartButtons.add(clearButton);

        cartSection.add(
                cartTitle,
                BorderLayout.NORTH
        );

        cartSection.add(
                cartScroll,
                BorderLayout.CENTER
        );

        cartSection.add(
                cartButtons,
                BorderLayout.SOUTH
        );

        rightPanel.add(
                cartSection,
                BorderLayout.CENTER
        );


        // ==========================================
        // TOTAL + ACTIONS
        // ==========================================

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout(15, 0)
                );

        bottomPanel.setBackground(WHITE);

        bottomPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                1, 0, 0, 0, BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                14, 24, 14, 24
                        )
                )
        );

        JPanel totalPanel =
                new JPanel();

        totalPanel.setOpaque(false);

        totalPanel.setLayout(
                new BoxLayout(
                        totalPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel totalCaption =
                new JLabel("ORDER TOTAL");

        totalCaption.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        totalCaption.setForeground(MUTED);

        totalLabel =
                new JLabel("R0.00");

        totalLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        26
                )
        );

        totalLabel.setForeground(PRIMARY);

        totalPanel.add(totalCaption);
        totalPanel.add(totalLabel);

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        buttons.setOpaque(false);

        JButton salesHistoryButton =
                createButton(
                        "Sales History",
                        PRIMARY
                );

        JButton checkoutButton =
                createButton(
                        "Checkout",
                        SECONDARY
                );

        JButton logoutButton =
                createButton(
                        "Logout",
                        new Color(
                                120,
                                70,
                                70
                        )
                );

        salesHistoryButton.addActionListener(
                _ -> showSalesHistory()
        );

        checkoutButton.addActionListener(
                _ -> checkout()
        );

        logoutButton.addActionListener(
                _ -> logout()
        );

        buttons.add(salesHistoryButton);
        buttons.add(checkoutButton);
        buttons.add(logoutButton);

        bottomPanel.add(
                totalPanel,
                BorderLayout.WEST
        );

        bottomPanel.add(
                buttons,
                BorderLayout.EAST
        );


        // ==========================================
        // MAIN SPLIT
        // ==========================================

        JSplitPane splitPane =
                new JSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT,
                        leftPanel,
                        rightPanel
                );

        splitPane.setDividerLocation(570);
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);
        splitPane.setBackground(BACKGROUND);

        add(
                headerPanel,
                BorderLayout.NORTH
        );

        add(
                splitPane,
                BorderLayout.CENTER
        );

        add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        SwingUtilities.invokeLater(
                () -> {
                    searchField.requestFocusInWindow();
                    searchMedicines();
                }
        );
    }


    // ==========================================
    // SECTION PANEL
    // ==========================================

    private JPanel createSectionPanel() {

        JPanel panel = new JPanel();

        panel.setBackground(WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                14, 14, 14, 14
                        )
                )
        );

        return panel;
    }


    // ==========================================
    // BUTTON
    // ==========================================

    private JButton createButton(
            String text,
            Color background
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

        button.setForeground(Color.WHITE);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        9, 16, 9, 16
                )
        );

        return button;
    }


    // ==========================================
    // TEXT FIELD
    // ==========================================

    private void styleTextField(
            JTextField field
    ) {

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                7, 10, 7, 10
                        )
                )
        );

        field.setBackground(WHITE);
        field.setForeground(TEXT);
        field.setCaretColor(PRIMARY);
    }


    // ==========================================
    // TABLE STYLE
    // ==========================================

    private void styleTable(
            JTable table
    ) {

        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        table.setForeground(TEXT);
        table.setBackground(WHITE);

        table.setGridColor(
                new Color(
                        235,
                        239,
                        242
                )
        );

        table.setSelectionBackground(
                new Color(
                        220,
                        239,
                        230
                )
        );

        table.setSelectionForeground(TEXT);
        table.setShowVerticalLines(false);

        table.setIntercellSpacing(
                new Dimension(0, 1)
        );

        table.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        table.getTableHeader().setForeground(
                Color.WHITE
        );

        table.getTableHeader().setBackground(
                PRIMARY
        );

        table.getTableHeader().setPreferredSize(
                new Dimension(0, 34)
        );

        table.getTableHeader()
                .setReorderingAllowed(false);
    }


    // ==========================================
    // SEARCH
    // ==========================================

    private void searchMedicines() {

        String searchTerm =
                searchField.getText().trim();

        if (searchTerm.isEmpty()) {

            searchResults =
                    medicineDAO.getAllMedicines();

        } else {

            searchResults =
                    medicineDAO.searchMedicines(
                            searchTerm
                    );
        }

        medicineTableModel.setRowCount(0);

        for (
                Medicine medicine :
                searchResults
        ) {

            medicineTableModel.addRow(
                    new Object[]{
                            medicine.getMedicineId(),
                            medicine.getName(),
                            medicine.getCompany(),
                            medicine.getMedicineType(),
                            String.format(
                                    Locale.US,
                                    "R%.2f",
                                    medicine.getPrice()
                            ),
                            medicine.getQuantityInStock(),
                            medicine.getExpiryDate()
                    }
            );
        }

        if (searchResults.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No medicines found.",
                    "Search",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }


    // ==========================================
    // ADD TO CART
    // ==========================================

    private void addSelectedMedicineToCart() {

        int selectedRow =
                medicineTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a medicine first."
            );

            return;
        }

        Medicine medicine =
                searchResults.get(selectedRow);


        // ==========================================
        // EXPIRY CHECK
        // ==========================================

        if (medicine.getExpiryDate() != null) {

            if (
                    medicine.getExpiryDate()
                            .isBefore(
                                    LocalDate.now()
                            )
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "This medicine has expired and cannot be sold.\n\n"
                                + medicine.getName()
                                + "\nExpiry: "
                                + medicine.getExpiryDate(),
                        "Expired Medicine",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }
        }


        // ==========================================
        // STOCK CHECK
        // ==========================================

        if (
                medicine.getQuantityInStock()
                        <= 0
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "This medicine is out of stock.",
                    "Out of Stock",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        int existingRow =
                findMedicineInCart(
                        medicine.getMedicineId()
                );

        if (existingRow >= 0) {

            int currentQuantity =
                    (int) cartTableModel.getValueAt(
                            existingRow,
                            3
                    );

            if (
                    currentQuantity + 1
                            > medicine.getQuantityInStock()
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "You cannot sell more than "
                                + medicine.getQuantityInStock()
                                + " units.",
                        "Insufficient Stock",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            cartTableModel.setValueAt(
                    currentQuantity + 1,
                    existingRow,
                    3
            );

            updateRowSubtotal(existingRow);

        } else {

            cartTableModel.addRow(
                    new Object[]{
                            medicine.getMedicineId(),
                            medicine.getName(),
                            String.format(
                                    Locale.US,
                                    "R%.2f",
                                    medicine.getPrice()
                            ),
                            1,
                            String.format(
                                    Locale.US,
                                    "R%.2f",
                                    medicine.getPrice()
                            )
                    }
            );
        }

        updateTotal();

        checkLowStock(medicine);
    }


    // ==========================================
    // INCREASE QUANTITY
    // ==========================================

    private void increaseQuantity() {

        int row =
                cartTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select an item in the cart first."
            );

            return;
        }

        int medicineId =
                (int) cartTableModel.getValueAt(
                        row,
                        0
                );

        Medicine medicine =
                medicineDAO.getMedicineById(
                        medicineId
                );

        if (medicine == null) {
            return;
        }

        int quantity =
                (int) cartTableModel.getValueAt(
                        row,
                        3
                );

        if (
                quantity + 1
                        > medicine.getQuantityInStock()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Only "
                            + medicine.getQuantityInStock()
                            + " units are available.",
                    "Insufficient Stock",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        cartTableModel.setValueAt(
                quantity + 1,
                row,
                3
        );

        updateRowSubtotal(row);

        updateTotal();
    }


    // ==========================================
    // DECREASE QUANTITY
    // ==========================================

    private void decreaseQuantity() {

        int row =
                cartTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select an item in the cart first."
            );

            return;
        }

        int quantity =
                (int) cartTableModel.getValueAt(
                        row,
                        3
                );

        if (quantity <= 1) {

            cartTableModel.removeRow(row);

        } else {

            cartTableModel.setValueAt(
                    quantity - 1,
                    row,
                    3
            );

            updateRowSubtotal(row);
        }

        updateTotal();
    }


    // ==========================================
    // UPDATE SUBTOTAL
    // ==========================================

    private void updateRowSubtotal(
            int row
    ) {

        if (row < 0) {
            return;
        }

        int medicineId =
                (int) cartTableModel.getValueAt(
                        row,
                        0
                );

        int quantity =
                (int) cartTableModel.getValueAt(
                        row,
                        3
                );

        Medicine medicine =
                medicineDAO.getMedicineById(
                        medicineId
                );

        if (medicine != null) {

            double subtotal =
                    medicine.getPrice()
                            * quantity;

            cartTableModel.setValueAt(
                    String.format(
                            Locale.US,
                            "R%.2f",
                            subtotal
                    ),
                    row,
                    4
            );
        }
    }


    // ==========================================
    // FIND CART ITEM
    // ==========================================

    private int findMedicineInCart(
            int medicineId
    ) {

        for (
                int row = 0;
                row < cartTableModel.getRowCount();
                row++
        ) {

            int id =
                    (int) cartTableModel.getValueAt(
                            row,
                            0
                    );

            if (id == medicineId) {
                return row;
            }
        }

        return -1;
    }


    // ==========================================
    // REMOVE
    // ==========================================

    private void removeSelectedItem() {

        int row =
                cartTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select an item in the cart first."
            );

            return;
        }

        cartTableModel.removeRow(row);

        updateTotal();
    }


    // ==========================================
    // CLEAR CART
    // ==========================================

    private void clearCart() {

        if (
                cartTableModel.getRowCount()
                        == 0
        ) {
            return;
        }

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to clear the cart?",
                        "Clear Cart",
                        JOptionPane.YES_NO_OPTION
                );

        if (
                choice
                        == JOptionPane.YES_OPTION
        ) {

            cartTableModel.setRowCount(0);

            updateTotal();
        }
    }


    // ==========================================
    // CALCULATE CART TOTAL
    // ==========================================

    private double calculateCartTotal() {

        double total = 0;

        for (
                int row = 0;
                row < cartTableModel.getRowCount();
                row++
        ) {

            String priceText =
                    (String) cartTableModel.getValueAt(
                            row,
                            2
                    );

            int quantity =
                    (int) cartTableModel.getValueAt(
                            row,
                            3
                    );

            double price =
                    parseCurrency(priceText);

            total +=
                    price * quantity;
        }

        return total;
    }


    // ==========================================
    // UPDATE TOTAL
    // ==========================================

    private void updateTotal() {

        double total =
                calculateCartTotal();

        totalLabel.setText(
                String.format(
                        Locale.US,
                        "Total: R%.2f",
                        total
                )
        );
    }


    // ==========================================
    // LOW STOCK WARNING
    // ==========================================

    private void checkLowStock(
            Medicine medicine
    ) {

        boolean warningsEnabled =
                getSettingBoolean(
                        "low_stock_warnings",
                        true
                );

        if (!warningsEnabled) {
            return;
        }

        if (
                medicine.getQuantityInStock()
                        <= medicine.getReorderLevel()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Low stock warning:\n\n"
                            + medicine.getName()
                            + "\nCurrent stock: "
                            + medicine.getQuantityInStock()
                            + "\nReorder level: "
                            + medicine.getReorderLevel(),
                    "Low Stock",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }


    // ==========================================
    // CHECKOUT
    // ==========================================

    private void checkout() {

        if (
                cartTableModel.getRowCount()
                        == 0
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "The cart is empty.",
                    "Checkout",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        double total =
                calculateCartTotal();


        // ==========================================
        // CUSTOMER NAME
        // ==========================================

        String customerName =
                JOptionPane.showInputDialog(
                        this,
                        "Enter customer name:",
                        "Customer Information",
                        JOptionPane.PLAIN_MESSAGE
                );

        if (customerName == null) {
            return;
        }

        customerName =
                customerName.trim();

        if (customerName.isEmpty()) {
            customerName = "Walk-in Customer";
        }


        // ==========================================
        // PAYMENT INPUT
        // ==========================================

        String paymentInput =
                JOptionPane.showInputDialog(
                        this,
                        String.format(
                                Locale.US,
                                "Customer: %s\n\n"
                                        + "Total: R%.2f\n\n"
                                        + "Enter amount received:",
                                customerName,
                                total
                        ),
                        "Payment",
                        JOptionPane.PLAIN_MESSAGE
                );

        if (paymentInput == null) {
            return;
        }

        double payment;

        try {

            payment =
                    Double.parseDouble(
                            paymentInput
                                    .trim()
                                    .replace(",", ".")
                    );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid payment amount.",
                    "Invalid Payment",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        if (payment < total) {

            JOptionPane.showMessageDialog(
                    this,
                    String.format(
                            Locale.US,
                            "Insufficient payment.\n\n"
                                    + "Total: R%.2f\n"
                                    + "Received: R%.2f",
                            total,
                            payment
                    ),
                    "Insufficient Payment",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        double change =
                payment - total;


        // ==========================================
        // VAT
        // ==========================================

        double vatRate = 0.15;

        /*
         * Medicine prices are already VAT-inclusive.
         *
         * Example:
         *
         * Total = R460.74
         *
         * VAT-exclusive:
         *
         * R460.74 / 1.15 = R400.64
         *
         * VAT:
         *
         * R460.74 - R400.64 = R60.10
         */

        double subtotalExcludingVAT =
                total / (1 + vatRate);

        double vatAmount =
                total - subtotalExcludingVAT;


        // ==========================================
        // CONFIRM CHECKOUT
        // ==========================================

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        String.format(
                                Locale.US,
                                "Complete sale?\n\n"
                                        + "Customer: %s\n"
                                        + "Subtotal (excl. VAT): R%.2f\n"
                                        + "VAT (15%%): R%.2f\n"
                                        + "Total: R%.2f\n"
                                        + "Payment: R%.2f\n"
                                        + "Change: R%.2f",
                                customerName,
                                subtotalExcludingVAT,
                                vatAmount,
                                total,
                                payment,
                                change
                        ),
                        "Confirm Checkout",
                        JOptionPane.YES_NO_OPTION
                );

        if (
                confirmation
                        != JOptionPane.YES_OPTION
        ) {
            return;
        }


        // ==========================================
        // DATABASE TRANSACTION
        // ==========================================

        try (
                Connection conn =
                        DBConnection.getConnection()
        ) {

            conn.setAutoCommit(false);


            // ==========================================
            // VERIFY STOCK + EXPIRY
            // ==========================================

            for (
                    int row = 0;
                    row < cartTableModel.getRowCount();
                    row++
            ) {

                int medicineId =
                        (int) cartTableModel.getValueAt(
                                row,
                                0
                        );

                int quantity =
                        (int) cartTableModel.getValueAt(
                                row,
                                3
                        );

                String medicineName =
                        (String) cartTableModel.getValueAt(
                                row,
                                1
                        );


                String sql =
                        "SELECT quantity_in_stock, expiry_date "
                                + "FROM medicines "
                                + "WHERE medicine_id = ? "
                                + "FOR UPDATE";

                try (
                        PreparedStatement stmt =
                                conn.prepareStatement(sql)
                ) {

                    stmt.setInt(
                            1,
                            medicineId
                    );

                    ResultSet rs =
                            stmt.executeQuery();

                    if (!rs.next()) {

                        conn.rollback();

                        showCheckoutError(
                                "Medicine no longer exists:\n"
                                        + medicineName
                        );

                        return;
                    }


                    int stock =
                            rs.getInt(
                                    "quantity_in_stock"
                            );

                    Date expiry =
                            rs.getDate(
                                    "expiry_date"
                            );


                    boolean preventExpiredSales =
                            getSettingBoolean(
                                    "prevent_expired_sales",
                                    true
                            );

                    if (
                            preventExpiredSales
                                    && expiry != null
                                    && expiry.toLocalDate()
                                    .isBefore(
                                            LocalDate.now()
                                    )
                    ) {

                        conn.rollback();

                        showCheckoutError(
                                "This medicine has expired:\n"
                                        + medicineName
                        );

                        return;
                    }


                    if (quantity > stock) {

                        conn.rollback();

                        showCheckoutError(
                                "Insufficient stock for:\n"
                                        + medicineName
                                        + "\n\nAvailable: "
                                        + stock
                                        + "\nRequested: "
                                        + quantity
                        );

                        return;
                    }
                }
            }


            // ==========================================
            // CREATE SALE
            // ==========================================

            String saleSQL =
                    "INSERT INTO sales "
                            + "(customer_name, total_amount, "
                            + "vat_amount, payment_amount, "
                            + "change_amount, user_id) "
                            + "VALUES (?, ?, ?, ?, ?, ?)";

            int saleId;

            try (
                    PreparedStatement stmt =
                            conn.prepareStatement(
                                    saleSQL,
                                    Statement.RETURN_GENERATED_KEYS
                            )
            ) {

                stmt.setString(
                        1,
                        customerName
                );

                stmt.setDouble(
                        2,
                        total
                );

                stmt.setDouble(
                        3,
                        vatAmount
                );

                stmt.setDouble(
                        4,
                        payment
                );

                stmt.setDouble(
                        5,
                        change
                );

                stmt.setInt(
                        6,
                        cashierUserId
                );

                stmt.executeUpdate();

                ResultSet keys =
                        stmt.getGeneratedKeys();

                if (!keys.next()) {

                    conn.rollback();

                    showCheckoutError(
                            "Could not create the sale."
                    );

                    return;
                }

                saleId =
                        keys.getInt(1);
            }


            // ==========================================
            // SALE ITEMS
            // ==========================================

            String itemSQL =
                    "INSERT INTO sale_items "
                            + "(sale_id, medicine_id, "
                            + "quantity_sold, price_at_sale) "
                            + "VALUES (?, ?, ?, ?)";

            try (
                    PreparedStatement stmt =
                            conn.prepareStatement(itemSQL)
            ) {

                for (
                        int row = 0;
                        row < cartTableModel.getRowCount();
                        row++
                ) {

                    int medicineId =
                            (int) cartTableModel.getValueAt(
                                    row,
                                    0
                            );

                    int quantity =
                            (int) cartTableModel.getValueAt(
                                    row,
                                    3
                            );

                    String priceText =
                            (String) cartTableModel.getValueAt(
                                    row,
                                    2
                            );

                    double price =
                            parseCurrency(priceText);

                    stmt.setInt(
                            1,
                            saleId
                    );

                    stmt.setInt(
                            2,
                            medicineId
                    );

                    stmt.setInt(
                            3,
                            quantity
                    );

                    stmt.setDouble(
                            4,
                            price
                    );

                    stmt.addBatch();
                }

                stmt.executeBatch();
            }


            // ==========================================
            // REDUCE STOCK
            // ==========================================

            String stockSQL =
                    "UPDATE medicines "
                            + "SET quantity_in_stock = "
                            + "quantity_in_stock - ? "
                            + "WHERE medicine_id = ? "
                            + "AND quantity_in_stock >= ?";

            try (
                    PreparedStatement stmt =
                            conn.prepareStatement(
                                    stockSQL
                            )
            ) {

                for (
                        int row = 0;
                        row < cartTableModel.getRowCount();
                        row++
                ) {

                    int medicineId =
                            (int) cartTableModel.getValueAt(
                                    row,
                                    0
                            );

                    int quantity =
                            (int) cartTableModel.getValueAt(
                                    row,
                                    3
                            );

                    stmt.setInt(
                            1,
                            quantity
                    );

                    stmt.setInt(
                            2,
                            medicineId
                    );

                    stmt.setInt(
                            3,
                            quantity
                    );

                    if (
                            stmt.executeUpdate()
                                    == 0
                    ) {

                        conn.rollback();

                        showCheckoutError(
                                "Stock could not be updated."
                        );

                        return;
                    }
                }
            }


            // ==========================================
            // COMMIT
            // ==========================================

            conn.commit();


            // ==========================================
            // RECEIPT
            // ==========================================

            showReceipt(
                    saleId,
                    total,
                    payment,
                    change
            );


            // ==========================================
            // CLEAR CART
            // ==========================================

            cartTableModel.setRowCount(0);

            updateTotal();


            // ==========================================
            // REFRESH MEDICINES
            // ==========================================

            searchMedicines();

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Checkout failed.\n\n"
                            + e.getMessage(),
                    "Checkout Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // ==========================================
    // GET SYSTEM SETTING
    // ==========================================

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

            ResultSet rs =
                    stmt.executeQuery();

            if (rs.next()) {

                String value =
                        rs.getString(
                                "setting_value"
                        );

                if (
                        value != null
                                && !value.trim().isEmpty()
                ) {

                    return value;
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return defaultValue;
    }


    // ==========================================
    // GET BOOLEAN SETTING
    // ==========================================

    private boolean getSettingBoolean(
            String key,
            boolean defaultValue
    ) {

        String value =
                getSetting(
                        key,
                        String.valueOf(
                                defaultValue
                        )
                );

        return Boolean.parseBoolean(
                value
        );
    }


    // ==========================================
    // RECEIPT
    // ==========================================

    private void showReceipt(
            int saleId,
            double total,
            double payment,
            double change
    ) {

        JDialog dialog =
                new JDialog(
                        this,
                        "Sale Receipt",
                        true
                );

        dialog.setSize(
                580,
                760
        );

        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);


        JPanel mainPanel =
                new JPanel(
                        new BorderLayout()
                );

        mainPanel.setBackground(
                BACKGROUND
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 18, 18, 18
                )
        );


        // ==========================================
        // RECEIPT CARD
        // ==========================================

        JPanel receiptPanel =
                new JPanel(
                        new BorderLayout()
                );

        receiptPanel.setBackground(
                WHITE
        );

        receiptPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                22, 24, 20, 24
                        )
                )
        );


        // ==========================================
        // HEADER
        // ==========================================

        JPanel headerPanel =
                new JPanel();

        headerPanel.setLayout(
                new BoxLayout(
                        headerPanel,
                        BoxLayout.Y_AXIS
                )
        );

        headerPanel.setBackground(
                WHITE
        );


        JLabel logoLabel =
                new JLabel();

        java.net.URL logoURL =
                getClass().getResource(
                        "/pims/gui/HealthFirstLogo.png"
                );

        if (logoURL != null) {

            ImageIcon originalIcon =
                    new ImageIcon(logoURL);

            Image image =
                    originalIcon
                            .getImage()
                            .getScaledInstance(
                                    64,
                                    64,
                                    Image.SCALE_SMOOTH
                            );

            logoLabel.setIcon(
                    new ImageIcon(image)
            );
        }

        logoLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        String pharmacyName =
                getSetting(
                        "pharmacy_name",
                        "HealthFirst Pharmacy"
                );

        JLabel titleLabel =
                new JLabel(
                        pharmacyName.toUpperCase()
                );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        titleLabel.setForeground(
                PRIMARY
        );

        titleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel subtitleLabel =
                new JLabel(
                        "PHARMACY SALES RECEIPT"
                );

        subtitleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        subtitleLabel.setForeground(
                MUTED
        );

        subtitleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        headerPanel.add(logoLabel);

        headerPanel.add(
                Box.createVerticalStrut(6)
        );

        headerPanel.add(titleLabel);

        headerPanel.add(
                Box.createVerticalStrut(3)
        );

        headerPanel.add(subtitleLabel);

        headerPanel.add(
                Box.createVerticalStrut(16)
        );


        String pharmacyAddress =
                getSetting(
                        "pharmacy_address",
                        ""
                );

        String pharmacyPhone =
                getSetting(
                        "pharmacy_phone",
                        ""
                );

        String pharmacyEmail =
                getSetting(
                        "pharmacy_email",
                        ""
                );

        String pharmacyRegistration =
                getSetting(
                        "pharmacy_registration",
                        ""
                );


        if (
                !pharmacyAddress
                        .trim()
                        .isEmpty()
        ) {

            JLabel addressLabel =
                    new JLabel(
                            pharmacyAddress
                    );

            addressLabel.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            11
                    )
            );

            addressLabel.setForeground(
                    MUTED
            );

            addressLabel.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            headerPanel.add(
                    addressLabel
            );
        }


        if (
                !pharmacyPhone
                        .trim()
                        .isEmpty()
        ) {

            JLabel phoneLabel =
                    new JLabel(
                            pharmacyPhone
                    );

            phoneLabel.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            11
                    )
            );

            phoneLabel.setForeground(
                    MUTED
            );

            phoneLabel.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            headerPanel.add(
                    phoneLabel
            );
        }


        if (
                !pharmacyEmail
                        .trim()
                        .isEmpty()
        ) {

            JLabel emailLabel =
                    new JLabel(
                            pharmacyEmail
                    );

            emailLabel.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            11
                    )
            );

            emailLabel.setForeground(
                    MUTED
            );

            emailLabel.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            headerPanel.add(
                    emailLabel
            );
        }


        if (
                !pharmacyRegistration
                        .trim()
                        .isEmpty()
        ) {

            JLabel registrationLabel =
                    new JLabel(
                            pharmacyRegistration
                    );

            registrationLabel.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            11
                    )
            );

            registrationLabel.setForeground(
                    MUTED
            );

            registrationLabel.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            headerPanel.add(
                    registrationLabel
            );
        }


        headerPanel.add(
                Box.createVerticalStrut(16)
        );


        receiptPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );


        // ==========================================
        // SALE INFORMATION
        // ==========================================

        JPanel infoPanel =
                new JPanel(
                        new GridLayout(
                                3,
                                2,
                                10,
                                5
                        )
                );

        infoPanel.setBackground(
                WHITE
        );

        infoPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 0, 15, 0
                )
        );


        String customerName =
                getCustomerName(saleId);


        String saleDate =
                getSaleDate(saleId);


        String cashierName =
                getCashierName(saleId);


        JLabel saleIdLabel =
                new JLabel(
                        "Sale ID: " + saleId
                );

        JLabel dateLabel =
                new JLabel(
                        "Date: " + saleDate
                );

        JLabel customerLabel =
                new JLabel(
                        "Customer: " + customerName
                );

        JLabel cashierLabel =
                new JLabel(
                        "Cashier: " + cashierName
                );

        JLabel statusLabel =
                new JLabel(
                        "Status: Completed"
                );

        JLabel emptyLabel =
                new JLabel("");


        Font infoFont =
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                );

        saleIdLabel.setFont(infoFont);
        dateLabel.setFont(infoFont);
        customerLabel.setFont(infoFont);
        cashierLabel.setFont(infoFont);
        statusLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        saleIdLabel.setForeground(TEXT);
        dateLabel.setForeground(TEXT);
        customerLabel.setForeground(TEXT);
        cashierLabel.setForeground(TEXT);
        statusLabel.setForeground(SECONDARY);


        infoPanel.add(
                saleIdLabel
        );

        infoPanel.add(
                dateLabel
        );

        infoPanel.add(
                customerLabel
        );

        infoPanel.add(
                cashierLabel
        );

        infoPanel.add(
                statusLabel
        );

        infoPanel.add(
                emptyLabel
        );


        // ==========================================
        // RECEIPT ITEMS
        // ==========================================

        DefaultTableModel receiptModel =
                new DefaultTableModel(
                        new Object[]{
                                "Medicine",
                                "Qty",
                                "Price",
                                "Subtotal"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };


        String itemSQL =
                "SELECT m.name, "
                        + "si.quantity_sold, "
                        + "si.price_at_sale "
                        + "FROM sale_items si "
                        + "JOIN medicines m "
                        + "ON si.medicine_id = m.medicine_id "
                        + "WHERE si.sale_id = ? "
                        + "ORDER BY si.sale_item_id";


        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(
                                itemSQL
                        )
        ) {

            stmt.setInt(
                    1,
                    saleId
            );

            ResultSet rs =
                    stmt.executeQuery();

            while (rs.next()) {

                String name =
                        rs.getString(
                                "name"
                        );

                int quantity =
                        rs.getInt(
                                "quantity_sold"
                        );

                double price =
                        rs.getDouble(
                                "price_at_sale"
                        );

                double subtotal =
                        price * quantity;

                receiptModel.addRow(
                        new Object[]{
                                name,
                                quantity,
                                String.format(
                                        Locale.US,
                                        "R%.2f",
                                        price
                                ),
                                String.format(
                                        Locale.US,
                                        "R%.2f",
                                        subtotal
                                )
                        }
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    dialog,
                    "Could not load receipt items.",
                    "Receipt Error",
                    JOptionPane.ERROR_MESSAGE
            );

            dialog.dispose();

            return;
        }


        JTable receiptTable =
                new JTable(
                        receiptModel
                );

        receiptTable.setRowHeight(30);

        receiptTable.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        receiptTable.setForeground(TEXT);
        receiptTable.setBackground(WHITE);

        receiptTable.setGridColor(
                BORDER
        );

        receiptTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        receiptTable.setFocusable(false);


        receiptTable
                .getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                12
                        )
                );

        receiptTable
                .getTableHeader()
                .setForeground(
                        WHITE
                );

        receiptTable
                .getTableHeader()
                .setBackground(
                        PRIMARY
                );

        receiptTable
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                32
                        )
                );

        receiptTable
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(230);

        receiptTable
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(45);

        receiptTable
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(80);

        receiptTable
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(90);


        JScrollPane tableScroll =
                new JScrollPane(
                        receiptTable
                );

        tableScroll.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        tableScroll.setPreferredSize(
                new Dimension(
                        480,
                        160
                )
        );


        // ==========================================
        // TOTALS
        // ==========================================

        double vatRate = 0.15;

        double subtotalExcludingVAT =
                total / (1 + vatRate);

        double vatAmount =
                total - subtotalExcludingVAT;


        JPanel totalsPanel =
                new JPanel();

        totalsPanel.setLayout(
                new BoxLayout(
                        totalsPanel,
                        BoxLayout.Y_AXIS
                )
        );

        totalsPanel.setBackground(
                WHITE
        );

        totalsPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 0, 0, 0
                )
        );


        totalsPanel.add(
                createReceiptAmountRow(
                        "Subtotal (excl. VAT)",
                        subtotalExcludingVAT,
                        false
                )
        );

        totalsPanel.add(
                Box.createVerticalStrut(6)
        );


        totalsPanel.add(
                createReceiptAmountRow(
                        "VAT (15%)",
                        vatAmount,
                        false
                )
        );

        totalsPanel.add(
                Box.createVerticalStrut(6)
        );


        totalsPanel.add(
                createReceiptAmountRow(
                        "Payment",
                        payment,
                        false
                )
        );

        totalsPanel.add(
                Box.createVerticalStrut(6)
        );


        totalsPanel.add(
                createReceiptAmountRow(
                        "Change",
                        change,
                        false
                )
        );

        totalsPanel.add(
                Box.createVerticalStrut(10)
        );


        JSeparator separator =
                new JSeparator();

        separator.setForeground(
                BORDER
        );

        separator.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        1
                )
        );

        totalsPanel.add(
                separator
        );

        totalsPanel.add(
                Box.createVerticalStrut(12)
        );


        // ==========================================
        // CENTERED TOTAL
        // ==========================================

        JLabel receiptTotalLabel =
                new JLabel(
                        String.format(
                                Locale.US,
                                "TOTAL  R%.2f",
                                total
                        )
                );

        receiptTotalLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        receiptTotalLabel.setForeground(
                SECONDARY
        );

        receiptTotalLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        totalsPanel.add(
                receiptTotalLabel
        );


        totalsPanel.add(
                Box.createVerticalStrut(12)
        );


        // ==========================================
        // RECEIPT FOOTER
        // ==========================================

        String receiptFooter =
                getSetting(
                        "receipt_footer",
                        "Thank you for choosing HealthFirst Pharmacy."
                );


        JLabel thankYouLabel =
                new JLabel(
                        "<html><div style='text-align:center;'>"
                                + receiptFooter
                                + "</div></html>"
                );

        thankYouLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        thankYouLabel.setForeground(
                MUTED
        );

        thankYouLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        totalsPanel.add(
                thankYouLabel
        );


        // ==========================================
        // RECEIPT BODY
        // ==========================================

        JPanel receiptBody =
                new JPanel();

        receiptBody.setLayout(
                new BoxLayout(
                        receiptBody,
                        BoxLayout.Y_AXIS
                )
        );

        receiptBody.setBackground(
                WHITE
        );


        infoPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        tableScroll.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        totalsPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        receiptBody.add(
                infoPanel
        );

        receiptBody.add(
                Box.createVerticalStrut(8)
        );

        receiptBody.add(
                tableScroll
        );

        receiptBody.add(
                Box.createVerticalStrut(8)
        );

        receiptBody.add(
                totalsPanel
        );


        receiptPanel.add(
                receiptBody,
                BorderLayout.CENTER
        );


        // ==========================================
        // BUTTONS
        // ==========================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        buttonPanel.setBackground(
                BACKGROUND
        );

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        14, 0, 0, 0
                )
        );


        JButton closeButton =
                createReceiptButton(
                        "Close",
                        SECONDARY
                );

        closeButton.addActionListener(
                _ -> dialog.dispose()
        );

        buttonPanel.add(
                closeButton
        );


        mainPanel.add(
                receiptPanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        dialog.setContentPane(
                mainPanel
        );

        dialog.setVisible(true);
    }


    // ==========================================
    // RECEIPT AMOUNT ROW
    // ==========================================

    private JPanel createReceiptAmountRow(
            String label,
            double amount,
            boolean emphasized
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                WHITE
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        24
                )
        );


        JLabel labelComponent =
                new JLabel(label);

        JLabel amountComponent =
                new JLabel(
                        String.format(
                                Locale.US,
                                "R%.2f",
                                amount
                        )
                );


        Font font =
                new Font(
                        "Segoe UI",
                        emphasized
                                ? Font.BOLD
                                : Font.PLAIN,
                        13
                );


        labelComponent.setFont(font);
        amountComponent.setFont(font);

        labelComponent.setForeground(TEXT);
        amountComponent.setForeground(TEXT);


        panel.add(
                labelComponent,
                BorderLayout.WEST
        );

        panel.add(
                amountComponent,
                BorderLayout.EAST
        );


        return panel;
    }


    // ==========================================
    // RECEIPT BUTTON
    // ==========================================

    private JButton createReceiptButton(
            String text,
            Color background
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

        button.setForeground(WHITE);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 18, 10, 18
                )
        );

        return button;
    }


    // ==========================================
    // GET CUSTOMER NAME
    // ==========================================

    private String getCustomerName(
            int saleId
    ) {

        String sql =
                "SELECT customer_name "
                        + "FROM sales "
                        + "WHERE sale_id = ?";


        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(
                                sql
                        )
        ) {

            stmt.setInt(
                    1,
                    saleId
            );

            ResultSet rs =
                    stmt.executeQuery();

            if (rs.next()) {

                String name =
                        rs.getString(
                                "customer_name"
                        );

                if (
                        name != null
                                && !name.trim().isEmpty()
                ) {

                    return name;
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return "Walk-in Customer";
    }


    // ==========================================
    // GET SALE DATE
    // ==========================================

    private String getSaleDate(
            int saleId
    ) {

        String sql =
                "SELECT sale_date "
                        + "FROM sales "
                        + "WHERE sale_id = ?";


        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(
                                sql
                        )
        ) {

            stmt.setInt(
                    1,
                    saleId
            );

            ResultSet rs =
                    stmt.executeQuery();

            if (rs.next()) {

                Timestamp timestamp =
                        rs.getTimestamp(
                                "sale_date"
                        );

                if (timestamp != null) {

                    return timestamp
                            .toLocalDateTime()
                            .format(
                                    DateTimeFormatter.ofPattern(
                                            "yyyy-MM-dd HH:mm"
                                    )
                            );
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return LocalDateTime.now()
                .format(
                        DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd HH:mm"
                        )
                );
    }


    // ==========================================
    // GET CASHIER NAME
    // ==========================================

    private String getCashierName(
            int saleId
    ) {

        String sql =
                "SELECT COALESCE("
                        + "u.full_name, "
                        + "'Unknown'"
                        + ") AS cashier "
                        + "FROM sales s "
                        + "LEFT JOIN users u "
                        + "ON s.user_id = u.user_id "
                        + "WHERE s.sale_id = ?";


        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(
                                sql
                        )
        ) {

            stmt.setInt(
                    1,
                    saleId
            );

            ResultSet rs =
                    stmt.executeQuery();

            if (rs.next()) {

                return rs.getString(
                        "cashier"
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return "Unknown";
    }


    // ==========================================
    // PARSE CURRENCY
    // ==========================================

    private double parseCurrency(
            String value
    ) {

        if (
                value == null
                        || value.trim().isEmpty()
        ) {

            return 0;
        }

        return Double.parseDouble(
                value
                        .replace(
                                "R",
                                ""
                        )
                        .replace(
                                ",",
                                ""
                        )
                        .trim()
        );
    }


    // ==========================================
    // CHECKOUT ERROR
    // ==========================================

    private void showCheckoutError(
            String message
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Checkout Failed",
                JOptionPane.ERROR_MESSAGE
        );
    }


    // ==========================================
    // SALES HISTORY
    // ==========================================

    private void showSalesHistory() {

        JDialog dialog =
                new JDialog(
                        this,
                        "Sales History",
                        true
                );

        dialog.setSize(
                1100,
                550
        );

        dialog.setLocationRelativeTo(this);


        DefaultTableModel model =
                new DefaultTableModel(
                        new Object[]{
                                "Sale ID",
                                "Date",
                                "Customer",
                                "Subtotal",
                                "VAT",
                                "Total",
                                "Payment",
                                "Change"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };


        JTable table =
                new JTable(model);

        styleTable(table);

        table.setRowHeight(30);


        String sql =
                "SELECT sale_id, "
                        + "sale_date, "
                        + "customer_name, "
                        + "total_amount, "
                        + "vat_amount, "
                        + "payment_amount, "
                        + "change_amount "
                        + "FROM sales "
                        + "WHERE user_id = ? "
                        + "ORDER BY sale_date DESC";


        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(
                                sql
                        )
        ) {

            stmt.setInt(
                    1,
                    cashierUserId
            );

            ResultSet rs =
                    stmt.executeQuery();


            while (rs.next()) {

                double totalAmount =
                        rs.getDouble(
                                "total_amount"
                        );

                double vatAmount =
                        rs.getDouble(
                                "vat_amount"
                        );

                double subtotalExcludingVAT =
                        totalAmount - vatAmount;


                String customer =
                        rs.getString(
                                "customer_name"
                        );

                if (
                        customer == null
                                || customer.trim().isEmpty()
                ) {

                    customer =
                            "Walk-in Customer";
                }


                model.addRow(
                        new Object[]{
                                rs.getInt(
                                        "sale_id"
                                ),

                                rs.getTimestamp(
                                        "sale_date"
                                ),

                                customer,

                                String.format(
                                        Locale.US,
                                        "R%.2f",
                                        subtotalExcludingVAT
                                ),

                                String.format(
                                        Locale.US,
                                        "R%.2f",
                                        vatAmount
                                ),

                                String.format(
                                        Locale.US,
                                        "R%.2f",
                                        totalAmount
                                ),

                                String.format(
                                        Locale.US,
                                        "R%.2f",
                                        rs.getDouble(
                                                "payment_amount"
                                        )
                                ),

                                String.format(
                                        Locale.US,
                                        "R%.2f",
                                        rs.getDouble(
                                                "change_amount"
                                        )
                                )
                        }
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load sales history.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        JButton viewButton =
                createButton(
                        "View Sale",
                        PRIMARY
                );


        viewButton.addActionListener(
                _ -> {

                    int row =
                            table.getSelectedRow();

                    if (row == -1) {

                        JOptionPane.showMessageDialog(
                                dialog,
                                "Please select a sale."
                        );

                        return;
                    }


                    int saleId =
                            (int) model.getValueAt(
                                    row,
                                    0
                            );


                    showSaleDetails(
                            saleId
                    );
                }
        );


        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        bottom.setBackground(
                BACKGROUND
        );

        bottom.add(
                viewButton
        );


        JScrollPane scrollPane =
                new JScrollPane(
                        table
                );

        dialog.add(
                scrollPane,
                BorderLayout.CENTER
        );

        dialog.add(
                bottom,
                BorderLayout.SOUTH
        );


        dialog.setVisible(true);
    }


    // ==========================================
    // SALE DETAILS
    // ==========================================

    private void showSaleDetails(
            int saleId
    ) {

        StringBuilder details =
                new StringBuilder();


        String saleSQL =
                "SELECT s.sale_date, "
                        + "s.customer_name, "
                        + "s.total_amount, "
                        + "s.vat_amount, "
                        + "s.payment_amount, "
                        + "s.change_amount, "
                        + "COALESCE("
                        + "u.full_name, "
                        + "'Unknown'"
                        + ") AS cashier "
                        + "FROM sales s "
                        + "LEFT JOIN users u "
                        + "ON s.user_id = u.user_id "
                        + "WHERE s.sale_id = ?";


        double total = 0;
        double vat = 0;
        double payment = 0;
        double change = 0;

        String customer =
                "Walk-in Customer";

        String cashier =
                "Unknown";

        String saleDate =
                "";


        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(
                                saleSQL
                        )
        ) {

            stmt.setInt(
                    1,
                    saleId
            );

            ResultSet rs =
                    stmt.executeQuery();

            if (rs.next()) {

                Timestamp timestamp =
                        rs.getTimestamp(
                                "sale_date"
                        );

                if (timestamp != null) {

                    saleDate =
                            timestamp
                                    .toLocalDateTime()
                                    .format(
                                            DateTimeFormatter.ofPattern(
                                                    "yyyy-MM-dd HH:mm"
                                            )
                                    );
                }


                String dbCustomer =
                        rs.getString(
                                "customer_name"
                        );

                if (
                        dbCustomer != null
                                && !dbCustomer
                                .trim()
                                .isEmpty()
                ) {

                    customer =
                            dbCustomer;
                }


                cashier =
                        rs.getString(
                                "cashier"
                        );

                total =
                        rs.getDouble(
                                "total_amount"
                        );

                vat =
                        rs.getDouble(
                                "vat_amount"
                        );

                payment =
                        rs.getDouble(
                                "payment_amount"
                        );

                change =
                        rs.getDouble(
                                "change_amount"
                        );
            }

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load sale information.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        double subtotal =
                total - vat;


        details.append(
                "SALE DETAILS\n"
        );

        details.append(
                "========================================\n\n"
        );

        details.append(
                "Sale ID:        "
                        + saleId
                        + "\n"
        );

        details.append(
                "Date:           "
                        + saleDate
                        + "\n"
        );

        details.append(
                "Customer:       "
                        + customer
                        + "\n"
        );

        details.append(
                "Cashier:        "
                        + cashier
                        + "\n\n"
        );

        details.append(
                "ITEMS\n"
        );

        details.append(
                "----------------------------------------\n"
        );


        String itemSQL =
                "SELECT m.name, "
                        + "si.quantity_sold, "
                        + "si.price_at_sale "
                        + "FROM sale_items si "
                        + "JOIN medicines m "
                        + "ON si.medicine_id = m.medicine_id "
                        + "WHERE si.sale_id = ? "
                        + "ORDER BY si.sale_item_id";


        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(
                                itemSQL
                        )
        ) {

            stmt.setInt(
                    1,
                    saleId
            );

            ResultSet rs =
                    stmt.executeQuery();


            while (rs.next()) {

                String name =
                        rs.getString(
                                "name"
                        );

                int quantity =
                        rs.getInt(
                                "quantity_sold"
                        );

                double price =
                        rs.getDouble(
                                "price_at_sale"
                        );

                double itemSubtotal =
                        quantity * price;


                details.append(
                        String.format(
                                Locale.US,
                                "%s x%d = R%.2f\n",
                                name,
                                quantity,
                                itemSubtotal
                        )
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load sale items.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        details.append(
                "\n"
        );

        details.append(
                "----------------------------------------\n"
        );


        details.append(
                String.format(
                        Locale.US,
                        "Subtotal (excl. VAT): R%.2f\n",
                        subtotal
                )
        );

        details.append(
                String.format(
                        Locale.US,
                        "VAT (15%%):            R%.2f\n",
                        vat
                )
        );

        details.append(
                String.format(
                        Locale.US,
                        "Total:                 R%.2f\n",
                        total
                )
        );

        details.append(
                String.format(
                        Locale.US,
                        "Payment:               R%.2f\n",
                        payment
                )
        );

        details.append(
                String.format(
                        Locale.US,
                        "Change:                R%.2f\n",
                        change
                )
        );


        JTextArea area =
                new JTextArea(
                        details.toString()
                );

        area.setEditable(false);

        area.setBackground(
                WHITE
        );

        area.setForeground(
                TEXT
        );

        area.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        13
                )
        );

        area.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 12, 12, 12
                )
        );


        JScrollPane scrollPane =
                new JScrollPane(
                        area
                );

        scrollPane.setPreferredSize(
                new Dimension(
                        600,
                        450
                )
        );


        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Sale Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    // ==========================================
    // LOGOUT
    // ==========================================

    private void logout() {

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "Confirm Logout",
                        JOptionPane.YES_NO_OPTION
                );

        if (
                choice
                        == JOptionPane.YES_OPTION
        ) {

            dispose();

            new LoginFrame();
        }
    }
}