package pims.gui.cashier;

import pims.util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CashierCheckout {

    private static final Color WHITE =
            Color.WHITE;

    private static final Color PRIMARY =
            new Color(27, 61, 88);

    private static final Color TEXT =
            new Color(45, 55, 65);

    private static final Color BORDER =
            new Color(225, 230, 235);

    private static final double VAT_RATE = 0.15;

    private final JFrame parent;
    private final CashierPOSPanel posPanel;
    private final int cashierUserId;
    private final ReceiptCallback receiptCallback;

    @FunctionalInterface
    public interface ReceiptCallback {
        void showReceipt(
                int saleId,
                String customerName,
                double total,
                double subtotalExcludingVAT,
                double vatAmount,
                double payment,
                double change
        );
    }

    public CashierCheckout(
            JFrame parent,
            CashierPOSPanel posPanel,
            int cashierUserId,
            ReceiptCallback receiptCallback
    ) {
        this.parent = parent;
        this.posPanel = posPanel;
        this.cashierUserId = cashierUserId;
        this.receiptCallback = receiptCallback;
    }

    // ==========================================
    // CHECKOUT
    // ==========================================

    public void processCheckout() {


        if (
                posPanel.getCartTableModel()
                        .getRowCount()
                        == 0
        ) {

            JOptionPane.showMessageDialog(
                    parent,
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
                        parent,
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
                        parent,
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
                    parent,
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
                    parent,
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
                        parent,
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

            receiptCallback.showReceipt(
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

            posPanel.getCartTableModel()
                    .setRowCount(0);

            posPanel.updateTotal();


            // ==========================================
            // REFRESH MEDICINES
            // ==========================================

            posPanel.searchMedicines();


        } catch (
                SQLException e
        ) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    parent,
                    "Checkout failed.\n\n"
                            + e.getMessage(),
                    "Checkout Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
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
                parent,
                message,
                "Checkout Failed",
                JOptionPane.ERROR_MESSAGE
        );
    }

}
