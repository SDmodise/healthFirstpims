package pims.components;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.parser.SVGLoader;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class SvgIcon implements Icon {

    private final SVGDocument document;
    private final int width;
    private final int height;

    public SvgIcon(String resourcePath, int width, int height) {

        this.width = width;
        this.height = height;

        try {

            URL svgURL = getClass().getResource(resourcePath);

            if (svgURL == null) {
                throw new IllegalArgumentException(
                        "SVG not found: " + resourcePath
                );
            }

            SVGLoader loader = new SVGLoader();
            document = loader.load(svgURL);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Could not load SVG: " + resourcePath,
                    e
            );
        }
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }

    @Override
    public void paintIcon(
            Component component,
            Graphics graphics,
            int x,
            int y
    ) {

        Graphics2D g2 =
                (Graphics2D) graphics.create();

        try {

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY
            );

            g2.setRenderingHint(
                    RenderingHints.KEY_STROKE_CONTROL,
                    RenderingHints.VALUE_STROKE_PURE
            );

            g2.translate(x, y);

            document.render(null, g2);

        } finally {

            g2.dispose();
        }
    }
}