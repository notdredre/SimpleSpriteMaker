package ssm.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ssm.colour.ColourOp;

public abstract class SquareTool extends DrawTool {
    public void preview(int x, int y, BufferedImage drawBuffer, BufferedImage overlayBuffer, int scale) {
        int screenX = x * scale;
        int screenY = y * scale;
        int screenEast = overlayBuffer.getWidth();
        int screenSouth = overlayBuffer.getHeight();
        int pixelEast = screenX + size * scale;
        int pixelSouth = screenY + size * scale;
        int width = pixelEast - screenX;
        int height = pixelSouth - screenY;

        BufferedImage pixelOverlay = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int drawPic = 0;
        if (screenX >= 0 && screenX < screenEast)
            drawPic = drawBuffer.getRGB(screenX, 0);
        if (screenY >= 0 && screenY < screenSouth)
            drawPic = drawBuffer.getRGB(0, screenY);
        if (screenX >= 0 && screenX < screenEast && screenY >= 0 && screenY < screenSouth)
            drawPic = drawBuffer.getRGB(screenX, screenY);
        int[] resultPix = new int[width * height];
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int bRGB = drawPic;
                int result = ColourOp.minus(Color.WHITE.getRGB(), bRGB);
                if (bRGB == 0)
                    result = Color.BLACK.getRGB();
                if (i == 0 || i == width - 1 || j == 0 || j == height - 1){   
                    resultPix[j * height + i] = result;
                }
            }
        }
        pixelOverlay.setRGB(0, 0, width, height, resultPix, 0, width);
        Graphics2D  o2 = (Graphics2D) overlayBuffer.getGraphics();
        o2.drawImage(pixelOverlay, screenX, screenY, null);
    }
}
