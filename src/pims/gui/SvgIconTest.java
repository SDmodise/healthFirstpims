package pims.gui;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.parser.SVGLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class SvgIconTest extends JFrame {

    public SvgIconTest() {
        setTitle("SVG Icon Test");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            URL svgURL = getClass().getResource(
                    "/pims/gui/images/icons/dashboard.svg"
            );

            if (svgURL == null) {
                throw new RuntimeException("dashboard.svg was not found.");
            }

            SVGLoader loader = new SVGLoader();
            SVGDocument document = loader.load(svgURL);

            int size = 64;

            BufferedImage image = new BufferedImage(
                    size,
                    size,
                    BufferedImage.TYPE_INT_ARGB
            );

            Graphics2D graphics = image.createGraphics();

            graphics.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            graphics.setRenderingHint(
                    RenderingHints.KEY_STROKE_CONTROL,
                    RenderingHints.VALUE_STROKE_PURE
            );

            document.render(null, graphics);

            graphics.dispose();

            iconLabel.setIcon(new ImageIcon(image));

        } catch (Exception e) {
            e.printStackTrace();
            iconLabel.setText("SVG failed to load");
        }

        add(iconLabel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SvgIconTest window = new SvgIconTest();
            window.setVisible(true);
        });
    }
}