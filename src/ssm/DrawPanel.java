package ssm;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import ssm.colour.ColourObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import ssm.file.ImageFileManager;

public class DrawPanel extends JPanel implements MouseInputListener, ColourObject, Refreshable {
    private final int WIDTH = 600, HEIGHT = 600, SCALE = 20;

    private BufferedImage drawBuffer, overlayBuffer, renderBuffer, writeBuffer;
    private Color primary, secondary;
    private ImageFileManager imageFileManager;
    private int currentPixelX, currentPixelY;
    
    public DrawPanel() {
        currentPixelX = currentPixelY = -1;
        imageFileManager = ImageFileManager.getImageFileManager();
        addMouseListener(this);
        addMouseMotionListener(this);
        drawBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        overlayBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        renderBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        writeBuffer = new BufferedImage(WIDTH / SCALE, HEIGHT / SCALE, BufferedImage.TYPE_INT_ARGB);
        currentPixelX = currentPixelY = -1;
        imageFileManager.setToWrite(writeBuffer);
    }

    private void render() {
        Graphics2D r2 = (Graphics2D) renderBuffer.getGraphics();
        r2.drawImage(drawBuffer, 0, 0, null);
        r2.drawImage(overlayBuffer, 0, 0, null);
        r2.dispose();

        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.drawImage(renderBuffer, 0, 0, null);
        g2.dispose();
    }

    public void clear() {
        Graphics2D d2 = (Graphics2D) drawBuffer.getGraphics();
        d2.setColor(Color.WHITE);
        d2.fillRect(0, 0, WIDTH, HEIGHT);
        d2.dispose();
        render();
    }

    private void clearOverlay() {
        int[] overlayPix = new int[WIDTH * HEIGHT];
        overlayBuffer.getRGB(0, 0, WIDTH, HEIGHT, overlayPix, 0, WIDTH);
        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            overlayPix[i] = 0;
        }
        overlayBuffer.setRGB(0, 0, WIDTH, HEIGHT, overlayPix, 0, WIDTH);
    }

    private void drawOverlay(int x, int y) {
        clearOverlay();
        int pixelXStart = x * SCALE;
        int pixelYStart = y * SCALE;
        Graphics2D o2 = (Graphics2D) overlayBuffer.getGraphics();
        o2.setColor(Color.BLACK);
        o2.drawRect(pixelXStart, pixelYStart, SCALE - 1, SCALE - 1);
    }

    private void drawPixel(int x, int y, Color c) {
        Graphics2D g2 = (Graphics2D) drawBuffer.getGraphics();
        g2.scale(SCALE, SCALE);
        g2.setColor(c);
        g2.fillRect(x, y, 1, 1);
        g2.dispose();

        Graphics2D w2 = (Graphics2D) writeBuffer.getGraphics();
        w2.setColor(c);
        w2.fillRect(x, y, 1, 1);
        w2.dispose();
        render();
    }

    public void refresh() {
        render();
    }

    public void updateColours(Color primary, Color secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    public void mouseDragged(MouseEvent e) {
        currentPixelX = e.getX() / SCALE;
        currentPixelY = e.getY() / SCALE;
        if(SwingUtilities.isLeftMouseButton(e))
            drawPixel(currentPixelX, currentPixelY, primary);
        if(SwingUtilities.isRightMouseButton(e))
            drawPixel(currentPixelX, currentPixelY, secondary);
        drawOverlay(currentPixelX, currentPixelY);
    }

    public void mouseMoved(MouseEvent e) {
        int eventX = e.getX() / SCALE;
        int eventY = e.getY() / SCALE;
        if (eventX != currentPixelX || eventY != currentPixelY) {
            currentPixelX = e.getX() / SCALE;
            currentPixelY = e.getY() / SCALE;
            drawOverlay(currentPixelX, currentPixelY);
        }
    }

    public void mouseClicked(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e))
            drawPixel(currentPixelX, currentPixelY, primary);
        if(SwingUtilities.isRightMouseButton(e))
            drawPixel(currentPixelX, currentPixelY, secondary);
    }

    public void mouseExited(MouseEvent e) {
        clearOverlay();
    }

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public int getDrawWidth() {
        return WIDTH;
    }

    public int getDrawHeight() {
        return HEIGHT;
    }
}
