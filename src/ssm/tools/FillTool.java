package ssm.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ssm.tools.ToolManager.ToolType;

public class FillTool extends Tool {
    public void draw(int x, int y, Color c, BufferedImage buffer, int scale) {
        int screenX = x * scale;
        int screenY = y * scale;
        int startColor = buffer.getRGB(screenX, screenY);
        

        fill(x, y, c, buffer, scale, startColor);
    }

    private void fill(int x, int y, Color c, BufferedImage buffer, int scale, int startColor) {
        int screenX = x * scale;
        int screenY = y * scale;

        if (x < 0 || screenX > buffer.getWidth() - 1 || y < 0 || screenY > buffer.getHeight() - 1)
            return;

        if (buffer.getRGB(screenX, screenY) != startColor || buffer.getRGB(screenX, screenY) == c.getRGB())
            return;
        
        Graphics2D b2 = (Graphics2D) buffer.getGraphics();
        b2.scale(scale, scale);
        b2.setColor(c);
        b2.fillRect(x, y, 1, 1);
        b2.dispose();
        fill(x - 1, y, c, buffer, scale, startColor);
        fill(x + 1, y, c, buffer, scale, startColor);
        fill(x, y - 1, c, buffer, scale, startColor);
        fill(x, y + 1, c, buffer, scale, startColor);
    }
    public void preview(int x, int y, BufferedImage drawBuffer, BufferedImage overlayBuffer, int scale) {

    }

    public ToolType getToolType() {
        return ToolType.FILLTOOL;
    }
}
