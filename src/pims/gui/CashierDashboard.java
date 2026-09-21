
package pims.gui;

import pims.dao.MedicineDAO;
import pims.model.Medicine;
import pims.util.DBConnection;
import pims.gui.cashier.CashierHeaderPanel;
import pims.gui.cashier.CashierPOSPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CashierDashboard extends JFrame {

    // ==========================================
    // COLORS
    // ==========================================

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

    private static final Color ERROR =
            new Color(180, 70, 70);


    // ==========================================
    // VAT
    // ==========================================

    private static final double VAT_RATE = 0.15;


    // ==========================================
    // VARIABLES
    // ==========================================

    private final int cashierUserId;

    private final MedicineDAO medicineDAO;

    private CashierPOSPanel posPanel;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public CashierDashboard(int cashierUserId) {

        this.cashierUserId = cashierUserId;

        this.medicineDAO =
                new MedicineDAO();

        setTitle(
                "Cashier POS - HealthFirst PIMS"
        );

        setSize(1150, 720);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        createUI();

        setVisible(true);
    }


    // ==========================================
    // CREATE UI
    // ==========================================

    private void createUI() {

        getContentPane()
                .setBackground(BACKGROUND);

        setLayout(
                new BorderLayout()
        );


        // ==========================================
        // HEADER
        // ==========================================

        JPanel headerPanel =
                new CashierHeaderPanel();


        // ==========================================
        // POS PANEL
        // ==========================================

        posPanel =
                new CashierPOSPanel(
                        medicineDAO,
                        this,
                        this::showSalesHistory,
                        this::checkout,
                        this::logout
                );


        add(
                headerPanel,
                BorderLayout.NORTH
        );

        add(
                posPanel,
                BorderLayout.CENTER
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
    // CHECKOUT
    // ==========================================

    private void checkout() {

        if (
                posPanel.getCartTableModel()
                        .getRowCount()
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
                posPanel.calculateCartTotal();


        // ==========================================
        // CUSTOMER NAME
        // ==========================================

        JTextField customerField =
                new JTextField();

        customerField.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        styleTextField(
                customerField
        );


        JPanel customerPanel =
                new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        customerPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        5,
                        5,
                        5
                )
        );

        customerPanel.add(
                new JLabel(
                        "Customer name:"
                ),
                BorderLayout.WEST
        );

        customerPanel.add(
                customerField,
                BorderLayout.CENTER
        );


        int customerResult =
                JOptionPane.showConfirmDialog(
                        this,
                        customerPanel,
                        "Customer Information",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );


        if (
                customerResult
                        != JOptionPane.OK_OPTION
        ) {
            return;
        }


        String customerName =
                customerField
                        .getText()
                        .trim();


        if (customerName.isEmpty()) {
            customerName = null;
        }


        // ==========================================
        // PAYMENT
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
                                    .replace(
                                            ",",
                                            "."
                                    )
                    );

        } catch (
                NumberFormatException e
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid payment amount.",
                    "Invalid Payment",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        payment =
                roundMoney(
                        payment
                );


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
                roundMoney(
                        payment - total
                );


        // ==========================================
        // VAT
        // ==========================================

        double subtotalExcludingVAT =
                roundMoney(
                        total / (1 + VAT_RATE)
                );


        double vatAmount =
                roundMoney(
                        total
                                - subtotalExcludingVAT
                );


        // ==========================================
        // CONFIRM
        // ==========================================

        String customerDisplay =
                customerName == null
                        ? "Walk-in Customer"
                        : customerName;


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
                                customerDisplay,
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
            // PRICE MAP
            // ==========================================

            Map<Integer, Double> salePrices =
                    new HashMap<>();


            // ==========================================
            // VERIFY STOCK + EXPIRY + CURRENT PRICE
            // ==========================================

            for (
                    int row = 0;
                    row < posPanel.getCartTableModel().getRowCount();
                    row++
            ) {

                int medicineId =
                        (int)
                                posPanel.getCartTableModel()
                                        .getValueAt(
                                                row,
                                                0
                                        );


                int quantity =
                        (int)
                                posPanel.getCartTableModel()
                                        .getValueAt(
                                                row,
                                                3
                                        );


                String medicineName =
                        (String)
                                posPanel.getCartTableModel()
                                        .getValueAt(
                                                row,
                                                1
                                        );


                String sql =
                        "SELECT quantity_in_stock, "
                                + "expiry_date, "
                                + "price "
                                + "FROM medicines "
                                + "WHERE medicine_id = ? "
                                + "FOR UPDATE";


                try (
                        PreparedStatement stmt =
                                conn.prepareStatement(
                                        sql
                                )
                ) {

                    stmt.setInt(
                            1,
                            medicineId
                    );


                    try (
                            ResultSet rs =
                                    stmt.executeQuery()
                    ) {

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


                        double currentPrice =
                                rs.getDouble(
                                        "price"
                                );


                        if (
                                getSettingBoolean(
                                        "prevent_expired_sales",
                                        true
                                )
                                        && expiry != null
                                        && expiry
                                        .toLocalDate()
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


                        if (
                                quantity > stock
                        ) {

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


                        salePrices.put(
                                medicineId,
                                currentPrice
                        );
                    }
                }
            }


            // ==========================================
            // CREATE SALE
            // ==========================================

            String saleSQL =
                    "INSERT INTO sales "
                            + "(customer_name, "
                            + "total_amount, "
                            + "vat_amount, "
                            + "payment_amount, "
                            + "change_amount, "
                            + "user_id) "
                            + "VALUES (?, ?, ?, ?, ?, ?)";


            int saleId;


            try (
                    PreparedStatement stmt =
                            conn.prepareStatement(
                                    saleSQL,
                                    Statement.RETURN_GENERATED_KEYS
                            )
            ) {

                if (
                        customerName == null
                                || customerName.isEmpty()
                ) {

                    stmt.setNull(
                            1,
                            Types.VARCHAR
                    );

                } else {

                    stmt.setString(
                            1,
                            customerName
                    );
                }


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


                try (
                        ResultSet keys =
                                stmt.getGeneratedKeys()
                ) {

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
            }


            // ==========================================
            // SALE ITEMS
            // ==========================================

            String itemSQL =
                    "INSERT INTO sale_items "
                            + "(sale_id, "
                            + "medicine_id, "
                            + "quantity_sold, "
                            + "price_at_sale) "
                            + "VALUES (?, ?, ?, ?)";


            try (
                    PreparedStatement stmt =
                            conn.prepareStatement(
                                    itemSQL
                            )
            ) {

                for (
                        int row = 0;
                        row < posPanel.getCartTableModel().getRowCount();
                        row++
                ) {

                    int medicineId =
                            (int)
                                    posPanel.getCartTableModel()
                                            .getValueAt(
                                                    row,
                                                    0
                                            );


                    int quantity =
                            (int)
                                    posPanel.getCartTableModel()
                                            .getValueAt(
                                                    row,
                                                    3
                                            );


                    Double price =
                            salePrices.get(
                                    medicineId
                            );


                    if (price == null) {

                        conn.rollback();

                        showCheckoutError(
                                "Medicine price could not be found."
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
                        row < posPanel.getCartTableModel().getRowCount();
                        row++
                ) {

                    int medicineId =
                            (int)
                                    posPanel.getCartTableModel()
                                            .getValueAt(
                                                    row,
                                                    0
                                            );


                    int quantity =
                            (int)
                                    posPanel.getCartTableModel()
                                            .getValueAt(
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
                    customerName,
                    total,
                    subtotalExcludingVAT,
                    vatAmount,
                    payment,
                    change
            );


            // ==========================================
            // CLEAR CART
            // ==========================================

            posPanel.clearCartAfterCheckout();


            // ==========================================
            // REFRESH MEDICINES
            // ==========================================

            posPanel.searchMedicines();


        } catch (
                SQLException e
        ) {

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
    // RECEIPT
    // ==========================================

    private void showReceipt(
            int saleId,
            String customerName,
            double total,
            double subtotalExcludingVAT,
            double vatAmount,
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
                720
        );

        dialog.setLocationRelativeTo(
                this
        );

        dialog.setResizable(
                false
        );


        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(
                                0,
                                0
                        )
                );

        mainPanel.setBackground(
                BACKGROUND
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        18,
                        18,
                        18
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
                                22,
                                24,
                                20,
                                24
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
                    new ImageIcon(
                            logoURL
                    );


            Image image =
                    originalIcon
                            .getImage()
                            .getScaledInstance(
                                    64,
                                    64,
                                    Image.SCALE_SMOOTH
                            );


            logoLabel.setIcon(
                    new ImageIcon(
                            image
                    )
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


        headerPanel.add(
                logoLabel
        );

        headerPanel.add(
                Box.createVerticalStrut(
                        6
                )
        );

        headerPanel.add(
                titleLabel
        );

        headerPanel.add(
                Box.createVerticalStrut(
                        3
                )
        );

        headerPanel.add(
                subtitleLabel
        );

        headerPanel.add(
                Box.createVerticalStrut(
                        14
                )
        );


        // ==========================================
        // PHARMACY INFORMATION
        // ==========================================

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


        headerPanel.add(
                Box.createVerticalStrut(
                        14
                )
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
                                0,
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
                        0,
                        0,
                        12,
                        0
                )
        );


        JLabel saleIdLabel =
                new JLabel(
                        "Sale ID: " + saleId
                );


        boolean showDateTime =
                getSettingBoolean(
                        "receipt_show_datetime",
                        true
                );


        if (showDateTime) {

            infoPanel.add(
                    saleIdLabel
            );


            infoPanel.add(
                    new JLabel(
                            "Date: "
                                    + LocalDateTime
                                    .now()
                                    .format(
                                            DateTimeFormatter.ofPattern(
                                                    "yyyy-MM-dd HH:mm"
                                            )
                                    )
                    )
            );

        } else {

            infoPanel.add(
                    saleIdLabel
            );

            infoPanel.add(
                    new JLabel("")
            );
        }


        String customerDisplay =
                customerName == null
                        || customerName.isEmpty()
                        ? "Walk-in Customer"
                        : customerName;


        infoPanel.add(
                new JLabel(
                        "Customer: "
                                + customerDisplay
                )
        );


        boolean showCashier =
                getSettingBoolean(
                        "receipt_show_cashier",
                        true
                );


        if (showCashier) {

            String cashierName =
                    getCashierName();


            infoPanel.add(
                    new JLabel(
                            "Cashier: "
                                    + cashierName
                    )
            );

        } else {

            infoPanel.add(
                    new JLabel("")
            );
        }


        // ==========================================
        // ITEMS
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


        /*
         * IMPORTANT:
         *
         * The receipt is created BEFORE the cart is cleared.
         * Therefore the current cart items are still available here.
         */
        for (
                int row = 0;
                row < posPanel.getCartTableModel().getRowCount();
                row++
        ) {

            String name =
                    (String)
                            posPanel.getCartTableModel()
                                    .getValueAt(
                                            row,
                                            1
                                    );


            String priceText =
                    (String)
                            posPanel.getCartTableModel()
                                    .getValueAt(
                                            row,
                                            2
                                    );


            int quantity =
                    (int)
                            posPanel.getCartTableModel()
                                    .getValueAt(
                                            row,
                                            3
                                    );


            double price =
                    parseCurrency(
                            priceText
                    );


            double itemSubtotal =
                    roundMoney(
                            price * quantity
                    );


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
                                    itemSubtotal
                            )
                    }
            );
        }


        JTable receiptTable =
                new JTable(
                        receiptModel
                );


        receiptTable.setRowHeight(
                30
        );

        receiptTable.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        receiptTable.setForeground(
                TEXT
        );

        receiptTable.setBackground(
                WHITE
        );

        receiptTable.setGridColor(
                BORDER
        );

        receiptTable.setFocusable(
                false
        );

        receiptTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );


        receiptTable.getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                12
                        )
                );

        receiptTable.getTableHeader()
                .setForeground(
                        WHITE
                );

        receiptTable.getTableHeader()
                .setBackground(
                        PRIMARY
                );

        receiptTable.getTableHeader()
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


        /*
         * Dynamically size the table according
         * to the number of items.
         *
         * This prevents the receipt items from
         * disappearing behind the totals section.
         */
        int itemRows =
                Math.max(
                        1,
                        receiptModel.getRowCount()
                );


        int tableHeight =
                Math.min(
                        230,
                        34 + (itemRows * 30)
                );


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
                        tableHeight
                )
        );


        // ==========================================
        // TOTALS
        // ==========================================

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
                        12,
                        0,
                        0,
                        0
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
                Box.createVerticalStrut(
                        6
                )
        );


        totalsPanel.add(
                createReceiptAmountRow(
                        "VAT (15%)",
                        vatAmount,
                        false
                )
        );


        totalsPanel.add(
                Box.createVerticalStrut(
                        6
                )
        );


        totalsPanel.add(
                createReceiptAmountRow(
                        "Payment",
                        payment,
                        false
                )
        );


        totalsPanel.add(
                Box.createVerticalStrut(
                        6
                )
        );


        totalsPanel.add(
                createReceiptAmountRow(
                        "Change",
                        change,
                        false
                )
        );


        totalsPanel.add(
                Box.createVerticalStrut(
                        10
                )
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
                Box.createVerticalStrut(
                        12
                )
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
                Box.createVerticalStrut(
                        12
                )
        );


        // ==========================================
        // FOOTER
        // ==========================================

        String receiptFooter =
                getSetting(
                        "receipt_footer",
                        "Thank you for choosing HealthFirst Pharmacy."
                );


        JLabel thankYouLabel =
                new JLabel(
                        receiptFooter
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
                new JPanel(
                        new BorderLayout(
                                0,
                                8
                        )
                );

        receiptBody.setBackground(
                WHITE
        );


        receiptBody.add(
                infoPanel,
                BorderLayout.NORTH
        );


        receiptBody.add(
                tableScroll,
                BorderLayout.CENTER
        );


        receiptBody.add(
                totalsPanel,
                BorderLayout.SOUTH
        );


        receiptPanel.add(
                receiptBody,
                BorderLayout.CENTER
        );


        // ==========================================
        // CLOSE BUTTON
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
                        14,
                        0,
                        0,
                        0
                )
        );


        JButton closeButton =
                createReceiptButton(
                        "Close",
                        SECONDARY
                );


        closeButton.addActionListener(
                e -> dialog.dispose()
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

        dialog.setVisible(
                true
        );
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
                new JLabel(
                        label
                );


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


        labelComponent.setFont(
                font
        );

        amountComponent.setFont(
                font
        );


        labelComponent.setForeground(
                TEXT
        );

        amountComponent.setForeground(
                TEXT
        );


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

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        18,
                        10,
                        18
                )
        );


        return button;
    }


    // ==========================================
    // CASHIER NAME
    // ==========================================

    private String getCashierName() {

        String sql =
                "SELECT full_name "
                        + "FROM users "
                        + "WHERE user_id = ?";


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


            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                if (rs.next()) {

                    String name =
                            rs.getString(
                                    "full_name"
                            );


                    if (
                            name != null
                                    && !name.trim().isEmpty()
                    ) {

                        return name;
                    }
                }
            }

        } catch (
                SQLException e
        ) {

            e.printStackTrace();
        }


        return "Cashier";
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
                1050,
                600
        );

        dialog.setLocationRelativeTo(
                this
        );


        DefaultTableModel model =
                new DefaultTableModel(
                        new Object[]{
                                "Sale ID",
                                "Date",
                                "Customer",
                                "Total",
                                "VAT",
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
                new JTable(
                        model
                );


        styleTable(
                table
        );


        table.setRowHeight(
                30
        );


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


            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                while (
                        rs.next()
                ) {

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
                                            rs.getDouble(
                                                    "total_amount"
                                            )
                                    ),
                                    String.format(
                                            Locale.US,
                                            "R%.2f",
                                            rs.getDouble(
                                                    "vat_amount"
                                            )
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
            }

        } catch (
                SQLException e
        ) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load sales history.\n\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        table.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(60);

        table.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(140);

        table.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(180);

        table.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(100);

        table.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(90);

        table.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(100);

        table.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(100);


        JButton viewButton =
                createButton(
                        "View Sale",
                        PRIMARY
                );


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
                            (int)
                                    model
                                            .getValueAt(
                                                    row,
                                                    0
                                            );


                    showSaleDetails(
                            saleId
                    );
                }
        );


        JButton closeButton =
                createButton(
                        "Close",
                        SECONDARY
                );


        closeButton.addActionListener(
                e -> dialog.dispose()
        );


        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                8
                        )
                );


        bottom.setBackground(
                BACKGROUND
        );


        bottom.add(
                viewButton
        );

        bottom.add(
                closeButton
        );


        dialog.add(
                new JScrollPane(
                        table
                ),
                BorderLayout.CENTER
        );


        dialog.add(
                bottom,
                BorderLayout.SOUTH
        );


        dialog.setVisible(
                true
        );
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
                "SELECT "
                        + "s.sale_id, "
                        + "s.sale_date, "
                        + "s.customer_name, "
                        + "s.total_amount, "
                        + "s.vat_amount, "
                        + "s.payment_amount, "
                        + "s.change_amount, "
                        + "u.full_name "
                        + "FROM sales s "
                        + "LEFT JOIN users u "
                        + "ON s.user_id = u.user_id "
                        + "WHERE s.sale_id = ?";


        String itemSQL =
                "SELECT "
                        + "m.name, "
                        + "si.quantity_sold, "
                        + "si.price_at_sale "
                        + "FROM sale_items si "
                        + "JOIN medicines m "
                        + "ON si.medicine_id = m.medicine_id "
                        + "WHERE si.sale_id = ? "
                        + "ORDER BY si.sale_item_id";


        try (
                Connection conn =
                        DBConnection.getConnection()
        ) {


            // ==========================================
            // SALE INFORMATION
            // ==========================================

            try (
                    PreparedStatement stmt =
                            conn.prepareStatement(
                                    saleSQL
                            )
            ) {

                stmt.setInt(
                        1,
                        saleId
                );


                try (
                        ResultSet rs =
                                stmt.executeQuery()
                ) {

                    if (!rs.next()) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Sale could not be found.",
                                "Sale Details",
                                JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }


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


                    String cashier =
                            rs.getString(
                                    "full_name"
                            );


                    if (
                            cashier == null
                                    || cashier.trim().isEmpty()
                    ) {

                        cashier =
                                "Unknown";
                    }


                    details.append(
                            "Sale ID: "
                                    + rs.getInt(
                                    "sale_id"
                            )
                                    + "\n"
                    );


                    details.append(
                            "Date: "
                                    + rs.getTimestamp(
                                    "sale_date"
                            )
                                    + "\n"
                    );


                    details.append(
                            "Customer: "
                                    + customer
                                    + "\n"
                    );


                    details.append(
                            "Cashier: "
                                    + cashier
                                    + "\n\n"
                    );


                    double total =
                            rs.getDouble(
                                    "total_amount"
                            );


                    double vat =
                            rs.getDouble(
                                    "vat_amount"
                            );


                    double subtotalExclVAT =
                            roundMoney(
                                    total - vat
                            );


                    details.append(
                            String.format(
                                    Locale.US,
                                    "Subtotal (excl. VAT): R%.2f\n",
                                    subtotalExclVAT
                            )
                    );


                    details.append(
                            String.format(
                                    Locale.US,
                                    "VAT (15%%): R%.2f\n",
                                    vat
                            )
                    );


                    details.append(
                            String.format(
                                    Locale.US,
                                    "Total: R%.2f\n",
                                    total
                            )
                    );


                    details.append(
                            String.format(
                                    Locale.US,
                                    "Payment: R%.2f\n",
                                    rs.getDouble(
                                            "payment_amount"
                                    )
                            )
                    );


                    details.append(
                            String.format(
                                    Locale.US,
                                    "Change: R%.2f\n",
                                    rs.getDouble(
                                            "change_amount"
                                    )
                            )
                    );


                    details.append(
                            "\n"
                    );


                    details.append(
                            "ITEMS\n"
                    );


                    details.append(
                            "------------------------------------------\n"
                    );
                }
            }


            // ==========================================
            // ITEMS
            // ==========================================

            try (
                    PreparedStatement stmt =
                            conn.prepareStatement(
                                    itemSQL
                            )
            ) {

                stmt.setInt(
                        1,
                        saleId
                );


                try (
                        ResultSet rs =
                                stmt.executeQuery()
                ) {

                    while (
                            rs.next()
                    ) {

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
                                roundMoney(
                                        quantity * price
                                );


                        details.append(
                                String.format(
                                        Locale.US,
                                        "%s x%d @ R%.2f = R%.2f\n",
                                        name,
                                        quantity,
                                        price,
                                        subtotal
                                )
                        );
                    }
                }
            }


        } catch (
                SQLException e
        ) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load sale details.\n\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        JTextArea area =
                new JTextArea(
                        details.toString()
                );


        area.setEditable(
                false
        );


        area.setLineWrap(
                true
        );


        area.setWrapStyleWord(
                true
        );


        area.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        13
                )
        );


        area.setForeground(
                TEXT
        );


        area.setBackground(
                WHITE
        );


        JScrollPane scrollPane =
                new JScrollPane(
                        area
                );


        scrollPane.setPreferredSize(
                new Dimension(
                        620,
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
