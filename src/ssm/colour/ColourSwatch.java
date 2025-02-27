package ssm.colour;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class ColourSwatch extends JPanel {
    private Color colour;
    private int size;

    public ColourSwatch(int size) {
        this.size = size;
        colour = Color.WHITE;
        setSize(size, size);
    }

    public ColourSwatch(Color colour, int size) {
        this.colour = colour;
        this.size = size;
        setSize(size, size);
    }

    public void draw() {
        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.setColor(colour);
        g2.fillRect(0, 0, size, size);
        g2.drawRect(0, 0, size, size);
        g2.dispose();
    }

    public void highlight() {
        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.setColor(Color.BLACK);
        g2.drawRect(0, 0, size, size);
        g2.dispose();
    }

    public void setColour(Color c) {
        colour = c;
    }
}
