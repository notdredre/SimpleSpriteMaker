package ssm.colour;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ColourSwatch extends JPanel {
    private Color colour;
    private int size;
    private BufferedImage buffer;
    private boolean isHighlighted;

    public ColourSwatch(int size) {
        this.size = size;
        colour = Color.WHITE;
        isHighlighted = false;
        buffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        setSize(size, size);
    }

    public ColourSwatch(Color colour, int size) {
        this.colour = colour;
        this.size = size;
        setSize(size, size);
    }

    private void draw() {
        Graphics2D b2 = (Graphics2D) buffer.getGraphics();
        b2.setColor(colour);
        b2.fillRect(0, 0, size, size);
        b2.drawRect(0, 0, size, size);
        b2.dispose();
    }

    private void highlight() {
        Graphics2D b2 = (Graphics2D) buffer.getGraphics();
        b2.setColor(Color.BLACK);
        b2.drawRect(0, 0, size - 1, size - 1);
        b2.dispose();
    }

    public void render() {
        draw();
        if (isHighlighted) {
            highlight();
        }

        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.drawImage(buffer, 0, 0, null);
        g2.dispose();
    }

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    public void setColour(Color c) {
        colour = c;
    }
}
