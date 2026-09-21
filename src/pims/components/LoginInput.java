package pims.components;

import javax.swing.*;
import java.awt.*;

public class LoginInput extends JPanel {

    private final JComponent field;

    public LoginInput(
            JComponent field,
            String iconPath
    ) {

        this.field = field;

        setLayout(
                new BorderLayout(
                        8,
                        0
                )
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


        // -----------------------------------------------------
        // ICON
        // -----------------------------------------------------

        JLabel iconLabel =
                new JLabel(
                        new SvgIcon(
                                iconPath,
                                18,
                                18
                        )
                );


        JPanel iconContainer =
                new JPanel(
                        new GridBagLayout()
                );

        iconContainer.setOpaque(false);

        iconContainer.setPreferredSize(
                new Dimension(38, 36)
        );

        iconContainer.add(iconLabel);


        add(
                iconContainer,
                BorderLayout.WEST
        );


        // -----------------------------------------------------
        // FIELD
        // -----------------------------------------------------

        field.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        field.setForeground(
                new Color(45, 55, 65)
        );

        field.setBackground(
                Color.WHITE
        );

        field.setBorder(null);

        field.setOpaque(false);


        add(
                field,
                BorderLayout.CENTER
        );
    }
}