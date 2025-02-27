package ssm.tools;

import java.awt.Color;
import java.awt.image.BufferedImage;

public abstract class DrawTool extends Tool {
    protected int size;

    public DrawTool() {
        size = 1;
    }

    public DrawTool(int size) {
        this.size = size;
    }

    public void increaseSize() {
        size++;
    }

    public void decreaseSize() {
        size--;
    }

    protected abstract void draw(int x, int y, Color c, BufferedImage buffer, int scale);
    public abstract void preview(int x, int y, BufferedImage overlayBuffer, int scale);
}
