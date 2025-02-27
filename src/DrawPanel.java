import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class DrawPanel extends JPanel implements MouseMotionListener, ColourObject {
    private final int WIDTH = 600, HEIGHT = 600, SCALE = 20;
    private BufferedImage drawBuffer;
    private Color primary, secondary;
    
    public DrawPanel() {
        addMouseMotionListener(this);
        drawBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    private void render() {
        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.drawImage(drawBuffer, 0, 0, null);
        g2.dispose();
    }

    public void clear() {
        Graphics2D g2 = (Graphics2D) drawBuffer.getGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, WIDTH, HEIGHT);
        g2.dispose();
        render();
    }

    private void drawPixel(int x, int y, Color c) {
        Graphics2D g2 = (Graphics2D) drawBuffer.getGraphics();
        g2.scale(SCALE, SCALE);
        g2.setColor(c);
        g2.fillRect(x, y, 1, 1);
        g2.dispose();
        render();
    }

    public void changeColour(int chosen) {}

    public void updateColours(Color primary, Color secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX() / SCALE;
        int y = e.getY() / SCALE;
        if(SwingUtilities.isLeftMouseButton(e))
            drawPixel(x, y, primary);
        if(SwingUtilities.isRightMouseButton(e))
            drawPixel(x, y, secondary);
    }

    public void mouseMoved(MouseEvent e) {

    }

    public int getDrawWidth() {
        return WIDTH;
    }

    public int getDrawHeight() {
        return HEIGHT;
    }
}
