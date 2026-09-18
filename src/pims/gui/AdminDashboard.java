package pims.gui;

import javax.swing.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard - HealthFirst PIMS");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Admin Dashboard - Under Construction");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);

        setVisible(true);
    }
}