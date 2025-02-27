package ssm.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SquareBrush extends Tool {
    private int size;

    public SquareBrush() {
        size = 1;
    }

    public SquareBrush(int size) {
        this.size = size;
    }

    protected void draw(int x, int y, Color c, BufferedImage buffer, int scale) {
        Graphics2D b2 = (Graphics2D) buffer.getGraphics();
        b2.setColor(c);
        b2.scale(scale, scale);
        b2.fillRect(x, y, size, size);
        b2.dispose();
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
