package pims.gui;

import pims.components.Sidebar;

import javax.swing.*;
import java.awt.*;

public class SidebarPreview extends JFrame {

    public SidebarPreview() {

        setTitle("HealthFirst PIMS - Sidebar Preview");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Sidebar sidebar = new Sidebar(
                item -> System.out.println("Clicked: " + item)
        );

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(243, 246, 249));

        setLayout(new BorderLayout());

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            SidebarPreview preview = new SidebarPreview();

            preview.setVisible(true);
        });
    }
}