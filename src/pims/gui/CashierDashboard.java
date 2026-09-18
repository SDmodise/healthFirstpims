package pims.gui;

import javax.swing.*;

public class CashierDashboard extends JFrame {
    public CashierDashboard() {
        setTitle("Cashier POS - HealthFirst PIMS");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Cashier POS - Under Construction");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);

        setVisible(true);
    }
}