package ssm.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SquareBrush extends SquareTool {
    protected void draw(int x, int y, Color c, BufferedImage buffer, int scale) {
        Graphics2D b2 = (Graphics2D) buffer.getGraphics();
        b2.setColor(c);
        b2.scale(scale, scale);
        b2.fillRect(x, y, size, size);
        b2.dispose();
    }
}
