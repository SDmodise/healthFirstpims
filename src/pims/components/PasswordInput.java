package pims.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PasswordInput extends JPanel {

    private final JPasswordField passwordField;

    private final JButton eyeButton;

    private final char defaultEchoChar;


    private static final Color EYE_ACTIVE =
            new Color(225, 235, 243);


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public PasswordInput() {

        setLayout(
                new BorderLayout()
        );

        setBackground(
                Color.WHITE
        );

        setPreferredSize(
                new Dimension(280, 38)
        );

        setMinimumSize(
                new Dimension(280, 38)
        );

        setMaximumSize(
                new Dimension(280, 38)
        );

        setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        setBorder(
                BorderFactory.createLineBorder(
                        new Color(210, 218, 225),
                        1
                )
        );


        // =====================================================
        // LOCK ICON
        // =====================================================

        JLabel lockLabel =
                new JLabel(
                        new SvgIcon(
                                "/pims/gui/images/icons/lock.svg",
                                18,
                                18
                        )
                );


        JPanel lockContainer =
                new JPanel(
                        new GridBagLayout()
                );

        lockContainer.setOpaque(false);

        lockContainer.setPreferredSize(
                new Dimension(38, 36)
        );

        lockContainer.add(
                lockLabel
        );


        add(
                lockContainer,
                BorderLayout.WEST
        );


        // =====================================================
        // PASSWORD FIELD
        // =====================================================

        passwordField =
                new JPasswordField();


        passwordField.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );


        passwordField.setForeground(
                new Color(45, 55, 65)
        );


        passwordField.setBackground(
                Color.WHITE
        );


        passwordField.setBorder(null);

        passwordField.setOpaque(false);


        defaultEchoChar =
                passwordField.getEchoChar();


        add(
                passwordField,
                BorderLayout.CENTER
        );


        // =====================================================
        // EYE BUTTON
        // =====================================================

        eyeButton =
                new JButton();


        eyeButton.setIcon(
                new SvgIcon(
                        "/pims/gui/images/icons/eye.svg",
                        18,
                        18
                )
        );


        eyeButton.setPreferredSize(
                new Dimension(38, 36)
        );

        eyeButton.setMinimumSize(
                new Dimension(38, 36)
        );

        eyeButton.setMaximumSize(
                new Dimension(38, 36)
        );


        eyeButton.setBackground(
                Color.WHITE
        );


        eyeButton.setOpaque(true);


        eyeButton.setBorder(
                BorderFactory.createEmptyBorder()
        );


        eyeButton.setFocusPainted(false);

        eyeButton.setBorderPainted(false);

        eyeButton.setContentAreaFilled(true);


        eyeButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        // =====================================================
        // PRESS = SHOW PASSWORD
        // RELEASE = HIDE PASSWORD
        // =====================================================

        eyeButton.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mousePressed(
                            MouseEvent e
                    ) {

                        passwordField.setEchoChar(
                                (char) 0
                        );


                        // Highlight ONLY the eye
                        eyeButton.setBackground(
                                EYE_ACTIVE
                        );


                        eyeButton.repaint();
                    }


                    @Override
                    public void mouseReleased(
                            MouseEvent e
                    ) {

                        passwordField.setEchoChar(
                                defaultEchoChar
                        );


                        // Remove eye highlight
                        eyeButton.setBackground(
                                Color.WHITE
                        );


                        eyeButton.repaint();
                    }


                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        if (!SwingUtilities
                                .isLeftMouseButton(e)) {

                            passwordField.setEchoChar(
                                    defaultEchoChar
                            );

                            eyeButton.setBackground(
                                    Color.WHITE
                            );

                            eyeButton.repaint();
                        }
                    }
                }
        );


        add(
                eyeButton,
                BorderLayout.EAST
        );
    }


    // =========================================================
    // GET PASSWORD
    // =========================================================

    public char[] getPassword() {

        return passwordField.getPassword();
    }


    // =========================================================
    // CLEAR PASSWORD
    // =========================================================

    public void setText(
            String text
    ) {

        passwordField.setText(
                text
        );
    }


    // =========================================================
    // FOCUS PASSWORD FIELD
    // =========================================================

    @Override
    public boolean requestFocusInWindow() {

        return passwordField.requestFocusInWindow();
    }
}