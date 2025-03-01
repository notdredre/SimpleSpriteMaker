package ssm.tools;

import java.awt.Color;
import java.awt.image.BufferedImage;

import ssm.colour.ColourOp;

public abstract class SquareTool extends DrawTool {
    public void preview(int x, int y, BufferedImage drawBuffer, BufferedImage overlayBuffer, int scale) {
        clearPreview(overlayBuffer);
        int screenX = x * scale;
        int screenY = y * scale;
        int screenEast = overlayBuffer.getWidth() - 1;
        int screenSouth = overlayBuffer.getHeight() - 1;

        int pixelEast = screenX + size * scale - 1;
        int pixelSouth = screenY + size * scale - 1;

        for (int i = screenX; i <= pixelEast; i++) {
            for (int j = screenY; j <= pixelSouth; j++) {
                if (i > screenEast || j > screenSouth || i < 0 || j < 0)
                    continue;
                int bRGB = drawBuffer.getRGB(i, j);
                int result = ColourOp.minus(Color.BLACK.getRGB(), bRGB);
                if (bRGB == Color.BLACK.getRGB())
                    result = Color.WHITE.getRGB();
                if (i == screenX || i == pixelEast){   
                    overlayBuffer.setRGB(i, j, result);
                }
                if (j == screenY || j == pixelSouth) {
                    overlayBuffer.setRGB(i, j, result);
                }
            }
        }
    }
}
