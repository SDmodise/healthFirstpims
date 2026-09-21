package pims.gui.cashier;

import pims.util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CashierReceipt {

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

    private final JFrame parent;
    private final CashierPOSPanel posPanel;
    private final int cashierUserId;

    public CashierReceipt(
            JFrame parent,
            CashierPOSPanel posPanel,
            int cashierUserId
    ) {
        this.parent = parent;
        this.posPanel = posPanel;
        this.cashierUserId = cashierUserId;
    }

    public void showReceipt(
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
                        parent,
                        "Sale Receipt",
                        true
                );


        dialog.setSize(
                580,
                720
        );

        dialog.setLocationRelativeTo(
                parent
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

    private double roundMoney(
            double value
    ) {

        return Math.round(
                value * 100.0
        ) / 100.0;
    }
}
