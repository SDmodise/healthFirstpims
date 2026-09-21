package pims.gui.cashier;

import pims.dao.MedicineDAO;
import pims.model.Medicine;
import pims.util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CashierPOSPanel extends JPanel {

    private static final long serialVersionUID = 1L;

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

    private final MedicineDAO medicineDAO;
    private final Component dialogParent;
    private final Runnable salesHistoryAction;
    private final Runnable checkoutAction;
    private final Runnable logoutAction;

    private JTextField searchField;
    private JTable medicineTable;
    private JTable cartTable;
    private DefaultTableModel medicineTableModel;
    private DefaultTableModel cartTableModel;
    private JLabel totalLabel;
    private List<Medicine> searchResults =
            new ArrayList<>();

    public CashierPOSPanel(
            MedicineDAO medicineDAO,
            Component dialogParent,
            Runnable salesHistoryAction,
            Runnable checkoutAction,
            Runnable logoutAction
    ) {

        this.medicineDAO = medicineDAO;
        this.dialogParent = dialogParent;
        this.salesHistoryAction = salesHistoryAction;
        this.checkoutAction = checkoutAction;
        this.logoutAction = logoutAction;

        createUI();
    }

    // ==========================================
    // CREATE UI
    // ==========================================

    private void createUI() {

        setBackground(
                BACKGROUND
        );

        setLayout(
                new BorderLayout()
        );


        // ==========================================
        // LEFT PANEL
        // ==========================================

        JPanel leftPanel =
                new JPanel(
                        new BorderLayout(
                                12,
                                12
                        )
                );

        leftPanel.setBackground(
                BACKGROUND
        );

        leftPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        18,
                        18,
                        9
                )
        );


        // ==========================================
        // SEARCH
        // ==========================================

        JPanel searchPanel =
                createSectionPanel();

        searchPanel.setLayout(
                new BorderLayout(
                        10,
                        0
                )
        );


        JLabel searchLabel =
                new JLabel(
                        "Search medicine"
                );

        searchLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        searchLabel.setForeground(
                TEXT
        );


        JPanel searchInputPanel =
                new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        searchInputPanel.setOpaque(false);


        searchField =
                new JTextField();

        searchField.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        styleTextField(
                searchField
        );


        JButton searchButton =
                createButton(
                        "Search",
                        PRIMARY
                );

        searchButton.addActionListener(
                e -> searchMedicines()
        );

        searchField.addActionListener(
                e -> searchMedicines()
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

        styleTable(
                medicineTable
        );

        medicineTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        medicineTable.setRowHeight(
                30
        );


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
                new BorderLayout(
                        0,
                        12
                )
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

        medicineTitle.setForeground(
                TEXT
        );


        JButton addButton =
                createButton(
                        "Add to Cart",
                        SECONDARY
                );

        addButton.addActionListener(
                e -> addSelectedMedicineToCart()
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
        // RIGHT PANEL
        // ==========================================

        JPanel rightPanel =
                new JPanel(
                        new BorderLayout(
                                12,
                                12
                        )
                );

        rightPanel.setBackground(
                BACKGROUND
        );

        rightPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        9,
                        18,
                        18
                )
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
                new JTable(
                        cartTableModel
                );

        styleTable(
                cartTable
        );

        cartTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        cartTable.setRowHeight(
                30
        );


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
                new BorderLayout(
                        0,
                        12
                )
        );


        JLabel cartTitle =
                new JLabel(
                        "Shopping Cart"
                );

        cartTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        cartTitle.setForeground(
                TEXT
        );


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
                new Dimension(
                        42,
                        34
                )
        );

        plusButton.setPreferredSize(
                new Dimension(
                        42,
                        34
                )
        );


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


        cartButtons.add(
                minusButton
        );

        cartButtons.add(
                plusButton
        );

        cartButtons.add(
                removeButton
        );

        cartButtons.add(
                clearButton
        );


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
        // BOTTOM PANEL
        // ==========================================

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout(
                                15,
                                0
                        )
                );

        bottomPanel.setBackground(
                WHITE
        );

        bottomPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                1,
                                0,
                                0,
                                0,
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                14,
                                24,
                                14,
                                24
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
                new JLabel(
                        "ORDER TOTAL"
                );

        totalCaption.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        totalCaption.setForeground(
                MUTED
        );


        totalLabel =
                new JLabel(
                        "R0.00"
                );

        totalLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        26
                )
        );

        totalLabel.setForeground(
                PRIMARY
        );


        totalPanel.add(
                totalCaption
        );

        totalPanel.add(
                totalLabel
        );


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
                e -> salesHistoryAction.run()
        );

        checkoutButton.addActionListener(
                e -> checkoutAction.run()
        );

        logoutButton.addActionListener(
                e -> logoutAction.run()
        );


        buttons.add(
                salesHistoryButton
        );

        buttons.add(
                checkoutButton
        );

        buttons.add(
                logoutButton
        );


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

        splitPane.setDividerLocation(
                570
        );

        splitPane.setDividerSize(
                5
        );

        splitPane.setBorder(null);

        splitPane.setBackground(
                BACKGROUND
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
                    searchField
                            .requestFocusInWindow();

                    searchMedicines();
                }
        );
    }


    // ==========================================
    // SECTION PANEL
    // ==========================================

    private JPanel createSectionPanel() {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                WHITE
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                14,
                                14,
                                14,
                                14
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
                new JButton(
                        text
                );

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
                background
        );

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
                        9,
                        16,
                        9,
                        16
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
                                7,
                                10,
                                7,
                                10
                        )
                )
        );

        field.setBackground(
                WHITE
        );

        field.setForeground(
                TEXT
        );

        field.setCaretColor(
                PRIMARY
        );
    }


    // ==========================================
    // TABLE
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

        table.setForeground(
                TEXT
        );

        table.setBackground(
                WHITE
        );

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

        table.setSelectionForeground(
                TEXT
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


        table.getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                12
                        )
                );

        table.getTableHeader()
                .setForeground(
                        WHITE
                );

        table.getTableHeader()
                .setBackground(
                        PRIMARY
                );

        table.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                34
                        )
                );

        table.getTableHeader()
                .setReorderingAllowed(
                        false
                );
    }




    // ==========================================
    // SEARCH
    // ==========================================

    public void searchMedicines() {

        String searchTerm =
                searchField
                        .getText()
                        .trim();


        if (searchTerm.isEmpty()) {

            searchResults =
                    medicineDAO
                            .getAllMedicines();

        } else {

            searchResults =
                    medicineDAO
                            .searchMedicines(
                                    searchTerm
                            );
        }


        medicineTableModel
                .setRowCount(0);


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


        if (
                searchResults.isEmpty()
                        && !searchTerm.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    dialogParent,
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
                medicineTable
                        .getSelectedRow();


        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    dialogParent,
                    "Please select a medicine first."
            );

            return;
        }


        Medicine medicine =
                searchResults.get(
                        selectedRow
                );


        // ==========================================
        // EXPIRY
        // ==========================================

        if (
                medicine.getExpiryDate()
                        != null
        ) {

            if (
                    medicine
                            .getExpiryDate()
                            .isBefore(
                                    LocalDate.now()
                            )
            ) {

                JOptionPane.showMessageDialog(
                        dialogParent,
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
        // STOCK
        // ==========================================

        if (
                medicine
                        .getQuantityInStock()
                        <= 0
        ) {

            JOptionPane.showMessageDialog(
                    dialogParent,
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
                    (int)
                            cartTableModel
                                    .getValueAt(
                                            existingRow,
                                            3
                                    );


            if (
                    currentQuantity + 1
                            > medicine
                            .getQuantityInStock()
            ) {

                JOptionPane.showMessageDialog(
                        dialogParent,
                        "You cannot sell more than "
                                + medicine
                                .getQuantityInStock()
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


            updateRowSubtotal(
                    existingRow
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


        updateTotal();

        checkLowStock(
                medicine
        );
    }


    // ==========================================
    // INCREASE
    // ==========================================

    private void increaseQuantity() {

        int row =
                cartTable
                        .getSelectedRow();


        if (row == -1) {

            JOptionPane.showMessageDialog(
                    dialogParent,
                    "Select an item in the cart first."
            );

            return;
        }


        int medicineId =
                (int)
                        cartTableModel
                                .getValueAt(
                                        row,
                                        0
                                );


        Medicine medicine =
                medicineDAO
                        .getMedicineById(
                                medicineId
                        );


        if (medicine == null) {
            return;
        }


        int quantity =
                (int)
                        cartTableModel
                                .getValueAt(
                                        row,
                                        3
                                );


        if (
                quantity + 1
                        > medicine
                        .getQuantityInStock()
        ) {

            JOptionPane.showMessageDialog(
                    dialogParent,
                    "Only "
                            + medicine
                            .getQuantityInStock()
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


        updateRowSubtotal(
                row
        );

        updateTotal();
    }


    // ==========================================
    // DECREASE
    // ==========================================

    private void decreaseQuantity() {

        int row =
                cartTable
                        .getSelectedRow();


        if (row == -1) {

            JOptionPane.showMessageDialog(
                    dialogParent,
                    "Select an item in the cart first."
            );

            return;
        }


        int quantity =
                (int)
                        cartTableModel
                                .getValueAt(
                                        row,
                                        3
                                );


        if (quantity <= 1) {

            cartTableModel.removeRow(
                    row
            );

        } else {

            cartTableModel.setValueAt(
                    quantity - 1,
                    row,
                    3
            );

            updateRowSubtotal(
                    row
            );
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


        String priceText =
                (String)
                        cartTableModel
                                .getValueAt(
                                        row,
                                        2
                                );


        int quantity =
                (int)
                        cartTableModel
                                .getValueAt(
                                        row,
                                        3
                                );


        double price =
                parseCurrency(
                        priceText
                );


        double subtotal =
                price * quantity;


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
                    (int)
                            cartTableModel
                                    .getValueAt(
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
                cartTable
                        .getSelectedRow();


        if (row == -1) {

            JOptionPane.showMessageDialog(
                    dialogParent,
                    "Select an item in the cart first."
            );

            return;
        }


        cartTableModel.removeRow(
                row
        );

        updateTotal();
    }


    // ==========================================
    // CLEAR
    // ==========================================

    private void clearCart() {

        if (
                cartTableModel
                        .getRowCount()
                        == 0
        ) {
            return;
        }


        int choice =
                JOptionPane.showConfirmDialog(
                        dialogParent,
                        "Are you sure you want to clear the cart?",
                        "Clear Cart",
                        JOptionPane.YES_NO_OPTION
                );


        if (
                choice
                        == JOptionPane.YES_OPTION
        ) {

            cartTableModel
                    .setRowCount(0);

            updateTotal();
        }
    }


    // ==========================================
    // CALCULATE TOTAL
    // ==========================================

    public double calculateCartTotal() {

        double total = 0;


        for (
                int row = 0;
                row < cartTableModel.getRowCount();
                row++
        ) {

            String priceText =
                    (String)
                            cartTableModel
                                    .getValueAt(
                                            row,
                                            2
                                    );


            int quantity =
                    (int)
                            cartTableModel
                                    .getValueAt(
                                            row,
                                            3
                                    );


            double price =
                    parseCurrency(
                            priceText
                    );


            total +=
                    price * quantity;
        }


        return roundMoney(
                total
        );
    }


    // ==========================================
    // UPDATE TOTAL
    // ==========================================

    public void updateTotal() {

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
    // LOW STOCK
    // ==========================================

    private void checkLowStock(
            Medicine medicine
    ) {

        boolean enabled =
                getSettingBoolean(
                        "low_stock_warnings",
                        true
                );


        if (!enabled) {
            return;
        }


        if (
                medicine
                        .getQuantityInStock()
                        <= medicine
                        .getReorderLevel()
        ) {

            JOptionPane.showMessageDialog(
                    dialogParent,
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
    // SETTINGS
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
                        conn.prepareStatement(
                                sql
                        )
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
            }

        } catch (
                SQLException e
        ) {

            e.printStackTrace();
        }


        return defaultValue;
    }


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
    // CURRENCY PARSER
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
    // ROUND MONEY
    // ==========================================

    private double roundMoney(
            double value
    ) {

        return Math.round(
                value * 100.0
        ) / 100.0;
    }



    // ==========================================
    // PUBLIC ACCESS
    // ==========================================

    public DefaultTableModel getCartTableModel() {
        return cartTableModel;
    }

    public JTable getCartTable() {
        return cartTable;
    }

    public void clearCartAfterCheckout() {
        cartTableModel.setRowCount(0);
        updateTotal();
    }

    public void focusSearchField() {
        searchField.requestFocusInWindow();
    }
}
