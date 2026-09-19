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

        JPanel headerPanel =
                new JPanel(new BorderLayout());

        headerPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 15, 10, 15
                )
        );

        JLabel titleLabel =
                new JLabel("HealthFirst PIMS - Cashier POS");

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        headerPanel.add(
                titleLabel,
                BorderLayout.WEST
        );


        // ==========================================
        // SEARCH
        // ==========================================

        JPanel searchPanel =
                new JPanel(new BorderLayout(10, 0));

        searchPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Search Medicine"
                )
        );

        searchField = new JTextField();

        JButton searchButton =
                new JButton("Search");

        searchButton.addActionListener(
                e -> searchMedicines()
        );

        searchField.addActionListener(
                e -> searchMedicines()
        );

        searchPanel.add(
                searchField,
                BorderLayout.CENTER
        );

        searchPanel.add(
                searchButton,
                BorderLayout.EAST
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
                new JTable(medicineTableModel);

        medicineTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane medicineScroll =
                new JScrollPane(medicineTable);

        medicineScroll.setBorder(
                BorderFactory.createTitledBorder(
                        "Available Medicines"
                )
        );


        JButton addButton =
                new JButton("Add to Cart");

        addButton.addActionListener(
                e -> addSelectedMedicineToCart()
        );


        JPanel leftPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        leftPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );

        leftPanel.add(
                medicineScroll,
                BorderLayout.CENTER
        );

        leftPanel.add(
                addButton,
                BorderLayout.SOUTH
        );


        // ==========================================
        // CART TABLE
        // ==========================================

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
                new JTable(cartTableModel);

        cartTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane cartScroll =
                new JScrollPane(cartTable);

        cartScroll.setBorder(
                BorderFactory.createTitledBorder(
                        "Shopping Cart"
                )
        );


        // ==========================================
        // QUANTITY BUTTONS
        // ==========================================

        JPanel cartButtons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        JButton minusButton =
                new JButton("-");

        JButton plusButton =
                new JButton("+");

        JButton removeButton =
                new JButton("Remove");

        JButton clearButton =
                new JButton("Clear Cart");


        minusButton.addActionListener(
                e -> decreaseQuantity()
        );

        plusButton.addActionListener(
                e -> increaseQuantity()
        );

        removeButton.addActionListener(
                e -> removeSelectedItem()
        );

        clearButton.addActionListener(
                e -> clearCart()
        );


        cartButtons.add(minusButton);
        cartButtons.add(plusButton);
        cartButtons.add(removeButton);
        cartButtons.add(clearButton);


        JPanel cartPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        cartPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Shopping Cart"
                )
        );

        cartPanel.add(
                cartScroll,
                BorderLayout.CENTER
        );

        cartPanel.add(
                cartButtons,
                BorderLayout.SOUTH
        );


        // ==========================================
        // TOTAL
        // ==========================================

        JPanel totalPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        totalLabel =
                new JLabel("Total: R0.00");

        totalLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        totalPanel.add(totalLabel);


        // ==========================================
        // SALES HISTORY
        // ==========================================

        JButton salesHistoryButton =
                new JButton("Sales History");

        salesHistoryButton.addActionListener(
                e -> showSalesHistory()
        );


        // ==========================================
        // CHECKOUT
        // ==========================================

        JButton checkoutButton =
                new JButton("Checkout");

        checkoutButton.addActionListener(
                e -> checkout()
        );


        // ==========================================
        // LOGOUT
        // ==========================================

        JButton logoutButton =
                new JButton("Logout");

        logoutButton.addActionListener(
                e -> logout()
        );


        // ==========================================
        // BOTTOM PANEL
        // ==========================================

        JPanel bottomPanel =
                new JPanel(new BorderLayout());

        bottomPanel.add(
                totalPanel,
                BorderLayout.CENTER
        );

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        buttons.add(salesHistoryButton);
        buttons.add(checkoutButton);
        buttons.add(logoutButton);

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
                        cartPanel
                );

        splitPane.setDividerLocation(570);

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
                () -> searchField.requestFocusInWindow()
        );
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

        for (Medicine medicine : searchResults) {

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

            if (medicine.getExpiryDate()
                    .isBefore(LocalDate.now())) {

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

        if (medicine.getQuantityInStock() <= 0) {

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

        updateRowSubtotal(existingRow);

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

    private void updateRowSubtotal(int row) {

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

        if (cartTableModel.getRowCount() == 0) {
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
                choice == JOptionPane.YES_OPTION
        ) {

            cartTableModel.setRowCount(0);

            updateTotal();
        }
    }


    // ==========================================
    // CALCULATE TOTAL
    // ==========================================

    private double calculateCartTotal() {

        double total = 0;

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

            Medicine medicine =
                    medicineDAO.getMedicineById(
                            medicineId
                    );

            if (medicine != null) {

                total +=
                        medicine.getPrice()
                                * quantity;
            }
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

        if (cartTableModel.getRowCount() == 0) {

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
        // PAYMENT INPUT
        // ==========================================

        String paymentInput =
                JOptionPane.showInputDialog(
                        this,
                        String.format(
                                Locale.US,
                                "Total: R%.2f\n\nEnter amount received:",
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
        // CONFIRM
        // ==========================================

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        String.format(
                                Locale.US,
                                "Complete sale?\n\n"
                                        + "Total: R%.2f\n"
                                        + "Payment: R%.2f\n"
                                        + "Change: R%.2f",
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

        try (Connection conn =
                     DBConnection.getConnection()) {

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


                    if (
                            expiry != null
                                    && expiry.toLocalDate()
                                    .isBefore(LocalDate.now())
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
                            + "(total_amount, user_id) "
                            + "VALUES (?, ?)";

            int saleId;

            try (
                    PreparedStatement stmt =
                            conn.prepareStatement(
                                    saleSQL,
                                    Statement.RETURN_GENERATED_KEYS
                            )
            ) {

                stmt.setDouble(
                        1,
                        total
                );

                stmt.setInt(
                        2,
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

                    Medicine medicine =
                            medicineDAO.getMedicineById(
                                    medicineId
                            );

                    if (medicine == null) {

                        conn.rollback();

                        showCheckoutError(
                                "Medicine could not be found."
                        );

                        return;
                    }

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
                            medicine.getPrice()
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
                            conn.prepareStatement(stockSQL)
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


            // Clear cart
            cartTableModel.setRowCount(0);

            updateTotal();

            // Refresh medicine list
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
    // RECEIPT
    // ==========================================

    private void showReceipt(
            int saleId,
            double total,
            double payment,
            double change
    ) {

        JTextArea receipt =
                new JTextArea();

        receipt.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        13
                )
        );

        receipt.setEditable(false);

        StringBuilder text =
                new StringBuilder();

        text.append(
                "================================\n"
        );

        text.append(
                "        HEALTHFIRST PIMS\n"
        );

        text.append(
                "       PHARMACY RECEIPT\n"
        );

        text.append(
                "================================\n"
        );

        text.append(
                "Sale ID: "
                        + saleId
                        + "\n"
        );

        text.append(
                "Date: "
                        + LocalDateTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "yyyy-MM-dd HH:mm"
                                )
                        )
                        + "\n"
        );

        text.append(
                "================================\n"
        );


        for (
                int row = 0;
                row < cartTableModel.getRowCount();
                row++
        ) {

            String name =
                    (String) cartTableModel.getValueAt(
                            row,
                            1
                    );

            double price =
                    getMedicinePrice(
                            (int) cartTableModel.getValueAt(
                                    row,
                                    0
                            )
                    );

            int quantity =
                    (int) cartTableModel.getValueAt(
                            row,
                            3
                    );

            double subtotal =
                    price * quantity;

            text.append(
                    String.format(
                            Locale.US,
                            "%-20s x%-3d R%8.2f\n",
                            name.length() > 20
                                    ? name.substring(0, 20)
                                    : name,
                            quantity,
                            subtotal
                    )
            );
        }


        text.append(
                "================================\n"
        );

        text.append(
                String.format(
                        Locale.US,
                        "TOTAL:       R%10.2f\n",
                        total
                )
        );

        text.append(
                String.format(
                        Locale.US,
                        "PAYMENT:     R%10.2f\n",
                        payment
                )
        );

        text.append(
                String.format(
                        Locale.US,
                        "CHANGE:      R%10.2f\n",
                        change
                )
        );

        text.append(
                "================================\n"
        );

        text.append(
                "        Thank you!\n"
        );

        text.append(
                "================================\n"
        );


        JScrollPane scrollPane =
                new JScrollPane(receipt);

        scrollPane.setPreferredSize(
                new Dimension(
                        450,
                        400
                )
        );


        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Receipt",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    // ==========================================
    // GET MEDICINE PRICE
    // ==========================================

    private double getMedicinePrice(
            int medicineId
    ) {

        Medicine medicine =
                medicineDAO.getMedicineById(
                        medicineId
                );

        if (medicine != null) {
            return medicine.getPrice();
        }

        return 0;
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

        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(this);


        DefaultTableModel model =
                new DefaultTableModel(
                        new Object[]{
                                "Sale ID",
                                "Date",
                                "Total"
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


        String sql =
                "SELECT sale_id, sale_date, total_amount "
                        + "FROM sales "
                        + "WHERE user_id = ? "
                        + "ORDER BY sale_date DESC";


        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setInt(
                    1,
                    cashierUserId
            );

            ResultSet rs =
                    stmt.executeQuery();

            while (rs.next()) {

                model.addRow(
                        new Object[]{
                                rs.getInt("sale_id"),
                                rs.getTimestamp(
                                        "sale_date"
                                ),
                                String.format(
                                        Locale.US,
                                        "R%.2f",
                                        rs.getDouble(
                                                "total_amount"
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
                new JButton("View Sale");


        viewButton.addActionListener(
                e -> {

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

        bottom.add(viewButton);


        dialog.add(
                new JScrollPane(table),
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

        details.append(
                "Sale ID: "
                        + saleId
                        + "\n\n"
        );


        String sql =
                "SELECT m.name, si.quantity_sold, "
                        + "si.price_at_sale "
                        + "FROM sale_items si "
                        + "JOIN medicines m "
                        + "ON si.medicine_id = m.medicine_id "
                        + "WHERE si.sale_id = ?";


        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setInt(
                    1,
                    saleId
            );

            ResultSet rs =
                    stmt.executeQuery();

            while (rs.next()) {

                String name =
                        rs.getString("name");

                int quantity =
                        rs.getInt(
                                "quantity_sold"
                        );

                double price =
                        rs.getDouble(
                                "price_at_sale"
                        );

                double subtotal =
                        quantity * price;

                details.append(
                        String.format(
                                Locale.US,
                                "%s x%d = R%.2f\n",
                                name,
                                quantity,
                                subtotal
                        )
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load sale details.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        JTextArea area =
                new JTextArea(
                        details.toString()
                );

        area.setEditable(false);

        area.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        13
                )
        );

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(area),
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
                choice == JOptionPane.YES_OPTION
        ) {

            dispose();

            new LoginFrame();
        }
    }
}