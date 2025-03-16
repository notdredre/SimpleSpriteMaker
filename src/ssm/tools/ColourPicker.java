package ssm.tools;

import java.awt.Color;
import java.awt.image.BufferedImage;

import ssm.colour.ColourManager;
import ssm.tools.ToolManager.ToolType;

public class ColourPicker extends Tool {
    private ColourManager colourManager;

    public ColourPicker() {
        colourManager = ColourManager.getColourManager();
    }
    @Override
    public ToolType getToolType() {
        // TODO Chsnge this
        return ToolType.FILLTOOL;
    }

    @Override
    protected void draw(int x, int y, Color c, BufferedImage buffer, int scale) {
        int screenX = x * scale;
        int screenY = y * scale;
        if (screenX >= 0 && screenX < buffer.getWidth() && screenY >= 0 && screenY < buffer.getHeight()) {
            Color newColor = new Color(buffer.getRGB(screenX, screenY));
            colourManager.setColour(newColor);
        }
    }

    @Override
    public void preview(int x, int y, BufferedImage drawBuffer, BufferedImage overlayBuffer, int scale) {
    }
    
}
