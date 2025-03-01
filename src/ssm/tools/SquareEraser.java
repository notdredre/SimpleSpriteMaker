package ssm.tools;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class SquareEraser extends SquareTool {
    public void draw(int x, int y, Color c, BufferedImage buffer, int scale) {
        int screenX = x * scale;
        int screenY = y * scale;
        for (int i = screenX; i < screenX + (size * scale); i++) {
            for (int j = screenY; j < screenY + (size * scale); j++) {
                if (i >= 0 && i < buffer.getWidth() && j >= 0 && j < buffer.getHeight())
                    buffer.setRGB(i, j, 0);
            }
        }
    }
}
