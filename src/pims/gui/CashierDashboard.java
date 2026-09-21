package pims.gui;

import pims.dao.MedicineDAO;
import pims.gui.cashier.CashierHeaderPanel;
import pims.gui.cashier.CashierCheckout;
import pims.gui.cashier.CashierPOSPanel;
import pims.gui.cashier.CashierReceipt;
import pims.gui.cashier.CashierSalesHistory;

import javax.swing.*;
import java.awt.*;

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

    private final CashierSalesHistory salesHistoryHandler;


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

        this.salesHistoryHandler =
                new CashierSalesHistory(
                        this,
                        cashierUserId
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

    // ==========================================
    // TABLE
    // ==========================================

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

    // ==========================================
    // SALES HISTORY
    // ==========================================

    // ==========================================
    // SALE DETAILS
    // ==========================================

    // ==========================================
    // SALES HISTORY
    // ==========================================

    private void showSalesHistory() {

        salesHistoryHandler.showSalesHistory();
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