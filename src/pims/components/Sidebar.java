package pims.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.util.function.Consumer;

public class Sidebar extends JPanel {

    // ==========================================================
    // COLORS
    // ==========================================================

    private static final Color SIDEBAR_COLOR =
            new Color(27, 61, 88);

    private static final Color ACTIVE_COLOR =
            new Color(45, 155, 105);

    private static final Color HOVER_COLOR =
            new Color(35, 78, 112);

    private static final Color TEXT_COLOR =
            Color.WHITE;

    private static final Color MUTED_COLOR =
            new Color(190, 205, 215);


    // ==========================================================
    // COMPONENTS
    // ==========================================================

    private final JPanel menuPanel;

    private final Consumer<String> navigationListener;

    private JButton activeButton;


    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================

    public Sidebar(Consumer<String> navigationListener) {

        this.navigationListener = navigationListener;

        setPreferredSize(new Dimension(245, 700));
        setBackground(SIDEBAR_COLOR);
        setLayout(new BorderLayout());


        // ======================================================
        // TOP BRANDING
        // ======================================================

        JPanel brandPanel = new JPanel();

        brandPanel.setOpaque(false);

        brandPanel.setLayout(
                new BoxLayout(
                        brandPanel,
                        BoxLayout.Y_AXIS
                )
        );

        brandPanel.setBorder(
                new EmptyBorder(
                        25,
                        20,
                        20,
                        20
                )
        );


        // Logo

        JLabel logoLabel = createLogo();


        // Application title

        JLabel titleLabel =
                new JLabel("HealthFirst PIMS");

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        titleLabel.setForeground(TEXT_COLOR);

        titleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        // Subtitle

        JLabel subtitleLabel =
                new JLabel("Admin Portal");

        subtitleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        subtitleLabel.setForeground(MUTED_COLOR);

        subtitleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        brandPanel.add(logoLabel);

        brandPanel.add(
                Box.createVerticalStrut(10)
        );

        brandPanel.add(titleLabel);

        brandPanel.add(
                Box.createVerticalStrut(3)
        );

        brandPanel.add(subtitleLabel);


        add(
                brandPanel,
                BorderLayout.NORTH
        );


        // ======================================================
        // MENU PANEL
        // ======================================================

        menuPanel = new JPanel();

        menuPanel.setOpaque(false);

        menuPanel.setLayout(
                new BoxLayout(
                        menuPanel,
                        BoxLayout.Y_AXIS
                )
        );

        menuPanel.setBorder(
                new EmptyBorder(
                        10,
                        12,
                        10,
                        12
                )
        );


        // ======================================================
        // MENU ITEMS
        // ======================================================

        addMenuItem(
                "Dashboard",
                "dashboard.svg"
        );

        addMenuItem(
                "Medicines",
                "medicines.svg"
        );

        addMenuItem(
                "Suppliers",
                "suppliers.svg"
        );

        addMenuItem(
                "Users",
                "users.svg"
        );

        addMenuItem(
                "Sales",
                "sales.svg"
        );

        addMenuItem(
                "Sales History",
                "history.svg"
        );

        addMenuItem(
                "Reports",
                "reports.svg"
        );

        addMenuItem(
                "Alerts",
                "alerts.svg"
        );

        addMenuItem(
                "Settings",
                "settings.svg"
        );


        // ======================================================
        // SCROLL PANE
        // ======================================================

        JScrollPane scrollPane =
                new JScrollPane(menuPanel);

        scrollPane.setBorder(null);

        scrollPane.setOpaque(false);

        scrollPane.getViewport()
                .setOpaque(false);

        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants
                        .HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants
                        .VERTICAL_SCROLLBAR_AS_NEEDED
        );


        add(
                scrollPane,
                BorderLayout.CENTER
        );


        // ======================================================
        // LOGOUT
        // ======================================================

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout()
                );

        bottomPanel.setOpaque(false);

        bottomPanel.setBorder(
                new EmptyBorder(
                        10,
                        12,
                        20,
                        12
                )
        );


        JButton logoutButton =
                createMenuButton(
                        "Logout",
                        "logout.svg"
                );


        logoutButton.addActionListener(e -> {

            if (navigationListener != null) {

                navigationListener.accept(
                        "Logout"
                );
            }
        });


        bottomPanel.add(
                logoutButton,
                BorderLayout.CENTER
        );


        add(
                bottomPanel,
                BorderLayout.SOUTH
        );
    }


    // ==========================================================
    // ADD MENU ITEM
    // ==========================================================

    private void addMenuItem(
            String text,
            String iconFile
    ) {

        JButton button =
                createMenuButton(
                        text,
                        iconFile
                );


        button.addActionListener(e -> {

            setActiveButton(button);


            if (navigationListener != null) {

                navigationListener.accept(
                        text
                );
            }
        });


        menuPanel.add(button);

        menuPanel.add(
                Box.createVerticalStrut(5)
        );


        // Dashboard selected by default

        if (text.equals("Dashboard")) {

            setActiveButton(button);
        }
    }


    // ==========================================================
    // ACTIVE MENU ITEM
    // ==========================================================

    private void setActiveButton(
            JButton button
    ) {

        // Reset previous active button

        if (activeButton != null) {

            activeButton.setBackground(
                    SIDEBAR_COLOR
            );
        }


        // Set new active button

        activeButton = button;

        activeButton.setBackground(
                ACTIVE_COLOR
        );
    }


    // ==========================================================
    // CREATE MENU BUTTON
    // ==========================================================

    private JButton createMenuButton(
            String text,
            String iconFile
    ) {

        JButton button =
                new JButton();


        // Text

        button.setText(text);

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        button.setForeground(
                TEXT_COLOR
        );


        // Background

        button.setBackground(
                SIDEBAR_COLOR
        );


        // Alignment

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setHorizontalTextPosition(
                SwingConstants.RIGHT
        );

        button.setIconTextGap(15);


        // Border / appearance

        button.setBorder(
                new EmptyBorder(
                        11,
                        15,
                        11,
                        15
                )
        );

        button.setFocusPainted(false);

        button.setContentAreaFilled(false);
        button.setOpaque(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {

            @Override
            public void paint(
                    Graphics g,
                    JComponent c
            ) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                JButton btn = (JButton) c;

                if (btn == activeButton) {

                    g2.setColor(ACTIVE_COLOR);

                } else {

                    g2.setColor(
                            btn.getBackground()
                    );
                }

                g2.fillRoundRect(
                        0,
                        0,
                        btn.getWidth(),
                        btn.getHeight(),
                        12,
                        12
                );

                g2.dispose();

                super.paint(g, c);
            }
        });


        // Cursor

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        // ======================================================
        // SVG ICON
        // ======================================================

        SvgIcon icon =
                new SvgIcon(
                        "/pims/gui/images/icons/"
                                + iconFile,
                        24,
                        24
                );

        button.setIcon(icon);


        // ======================================================
        // HOVER EFFECT
        // ======================================================

        button.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent e
                    ) {

                        if (button != activeButton) {

                            button.setBackground(
                                    HOVER_COLOR
                            );
                        }
                    }


                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent e
                    ) {

                        if (button != activeButton) {

                            button.setBackground(
                                    SIDEBAR_COLOR
                            );
                        }
                    }
                }
        );


        return button;
    }


    // ==========================================================
    // LOGO
    // ==========================================================

    private JLabel createLogo() {

        JLabel label =
                new JLabel();

        label.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        URL logoURL =
                getClass().getResource(
                        "/pims/gui/HealthFirstLogo.png"
                );


        if (logoURL != null) {

            ImageIcon original =
                    new ImageIcon(
                            logoURL
                    );


            Image image =
                    original
                            .getImage()
                            .getScaledInstance(
                                    80,
                                    80,
                                    Image.SCALE_SMOOTH
                            );


            label.setIcon(
                    new ImageIcon(image)
            );

        } else {

            // Fallback if logo cannot be found

            label.setText("HF");

            label.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            28
                    )
            );

            label.setForeground(
                    TEXT_COLOR
            );
        }


        return label;
    }
}