package ssm.tools;

import java.awt.Color;
import java.awt.image.BufferedImage;

import ssm.colour.ColourManager;
import ssm.tools.ToolManager.ToolType;

public class ColorPicker extends Tool {
    private ColourManager colourManager;

    public ColorPicker() {
        colourManager = ColourManager.getColourManager();
    }
    @Override
    public ToolType getToolType() {
        // TODO Chsnge this
        return ToolType.FILLTOOL;
    }

    @Override
    protected void draw(int x, int y, Color c, BufferedImage buffer, int scale) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }

    @Override
    public void preview(int x, int y, BufferedImage drawBuffer, BufferedImage overlayBuffer, int scale) {
    }
    
}
