package ssm.draw;

import java.awt.image.BufferedImage;

public class Drawing {
    private BufferedImage drawBuffer;
    private int width, height, row, col;

    public Drawing(int width, int height) {
        this.width = width;
        this.height = height;
        row = col = 0;
    }

    public Drawing(int width, int height, int row, int col) {
        this.width = width;
        this.height = height;
        this.row = row;
        this.col = col;
        drawBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public BufferedImage getBuffer() {
        return drawBuffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return col;
    }
}
