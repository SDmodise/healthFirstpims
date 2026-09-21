
package pims.gui;

import pims.gui.cashier.CashierReceipt;
import pims.dao.MedicineDAO;
import pims.util.DBConnection;
import pims.gui.cashier.CashierHeaderPanel;
import pims.gui.cashier.CashierCheckout;
import pims.gui.cashier.CashierPOSPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Locale;

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

    // ==========================================
    // VARIABLES
    // ==========================================

    private final int cashierUserId;

    private final MedicineDAO medicineDAO;

    private CashierPOSPanel posPanel;

    private final CashierCheckout checkoutHandler;

    private final CashierReceipt receiptHandler;


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

        this.receiptHandler =
                new CashierReceipt(
                        this,
                        posPanel,
                        cashierUserId
                );

        this.checkoutHandler =
                new CashierCheckout(
                        this,
                        posPanel,
                        cashierUserId,
                        receiptHandler::showReceipt
                );

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

        checkoutHandler.processCheckout();
    }


    // ==========================================
    // SETTINGS
    // ==========================================



    // ==========================================
    // RECEIPT
    // ==========================================


    // ==========================================
    // RECEIPT AMOUNT ROW
    // ==========================================


    // ==========================================
    // RECEIPT BUTTON
    // ==========================================


    // ==========================================
    // CASHIER NAME
    // ==========================================


    // ==========================================
    // CURRENCY PARSER
    // ==========================================


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
