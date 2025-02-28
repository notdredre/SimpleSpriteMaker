package ssm.tools;
import java.awt.Color;
import java.awt.image.BufferedImage;

import ssm.tools.ToolManager.ToolType;

public abstract class Tool {
    public abstract ToolType getToolType();
    protected abstract void draw(int x, int y, Color c, BufferedImage buffer, int scale);
    public void use(int x, int y, Color c, BufferedImage drawBuffer, BufferedImage writeBuffer, int scale) {
        draw(x, y, c, drawBuffer, scale);
        draw(x, y, c, writeBuffer, 1);
    }
    public abstract void preview(int x, int y, BufferedImage overlayBuffer, int scale);
    protected void clearPreview(BufferedImage overlayBuffer) {
        int width = overlayBuffer.getWidth();
        int height = overlayBuffer.getHeight();
        int[] pixels = new int[width * height];
        overlayBuffer.getRGB(0, 0, width, height, pixels, 0, width);
        for (int i = 0; i < width * height; i++) {
            pixels[i] = 0;
        }
        overlayBuffer.setRGB(0, 0, width, height, pixels, 0, width);
    }
}
