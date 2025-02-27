package ssm.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SquareEraser extends DrawTool {
    public void draw(int x, int y, Color c, BufferedImage buffer, int scale) {
        int screenX = x * scale;
        int screenY = y * scale;
        for (int i = screenX; i < screenX + (size * scale); i++) {
            for (int j = screenY; j < screenY + (size * scale); j++) {
                buffer.setRGB(i, j, 0);
            }
        }
    }

    public void preview(int x, int y, BufferedImage overlayBuffer, int scale) {
        clearPreview(overlayBuffer);
        int screenX = x * scale;
        int screenY = y * scale;
        Graphics2D o2 = (Graphics2D) overlayBuffer.getGraphics();
        o2.setColor(Color.BLACK);
        o2.drawRect(screenX, screenY, size * scale - 1, size * scale - 1);
        o2.dispose();
    }
}
