package ssm;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import ssm.colour.ColourObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import ssm.file.ImageFileManager;
import ssm.tools.SquareBrush;
import ssm.tools.SquareEraser;
import ssm.tools.Tool;

public class DrawPanel extends JPanel implements MouseInputListener, KeyListener, ColourObject, Refreshable {
    private final int WIDTH = 500, HEIGHT = 500, SCALE = 20;

    private BufferedImage drawBuffer, overlayBuffer, renderBuffer, writeBuffer, backgroundBuffer;
    private Color primary, secondary;
    private Tool currentTool;
    private ImageFileManager imageFileManager;
    private int currentPixelX, currentPixelY;
    
    public DrawPanel() {
        currentPixelX = currentPixelY = -1;
        currentTool = new SquareBrush();
        imageFileManager = ImageFileManager.getImageFileManager();
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        drawBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        overlayBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        renderBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        writeBuffer = new BufferedImage(WIDTH / SCALE, HEIGHT / SCALE, BufferedImage.TYPE_INT_ARGB);
        backgroundBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        currentPixelX = currentPixelY = -1;
        imageFileManager.setToWrite(writeBuffer);
    }

    private void render() {
        Graphics2D r2 = (Graphics2D) renderBuffer.getGraphics();
        r2.drawImage(backgroundBuffer, 0, 0, null);
        r2.drawImage(drawBuffer, 0, 0, null);
        r2.drawImage(overlayBuffer, 0, 0, null);
        r2.dispose();

        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.drawImage(renderBuffer, 0, 0, null);
        g2.dispose();
    }

    public void clear() {
        Graphics2D d2 = (Graphics2D) backgroundBuffer.getGraphics();
        try {
            Paint transparency = new TexturePaint(imageFileManager.openImage(getClass().getResource("res/transparentTexture.png").getPath()), new Rectangle2D.Double(0, 0, 100, 100));
            d2.setPaint(transparency);
        } catch (IOException e) {
            e.printStackTrace();
            d2.setColor(Color.WHITE);
        }
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
            currentTool.use(currentPixelX, currentPixelY, primary, drawBuffer, writeBuffer, SCALE);
        if(SwingUtilities.isRightMouseButton(e))
        currentTool.use(currentPixelX, currentPixelY, secondary, drawBuffer, writeBuffer, SCALE);
        currentTool.preview(currentPixelX, currentPixelY, overlayBuffer, SCALE);
    }

    public void mouseMoved(MouseEvent e) {
        int eventX = e.getX() / SCALE;
        int eventY = e.getY() / SCALE;
        if (eventX != currentPixelX || eventY != currentPixelY) {
            currentPixelX = e.getX() / SCALE;
            currentPixelY = e.getY() / SCALE;
            currentTool.preview(currentPixelX, currentPixelY, overlayBuffer, SCALE);
        }
    }

    public void mouseClicked(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e))
            currentTool.use(currentPixelX, currentPixelY, primary, drawBuffer, writeBuffer, SCALE);
        if(SwingUtilities.isRightMouseButton(e))
            currentTool.use(currentPixelX, currentPixelY, secondary, drawBuffer, writeBuffer, SCALE);
    }

    public void mouseExited(MouseEvent e) {
        clearOverlay();
    }

    public void mouseEntered(MouseEvent e) {
        currentTool.preview(currentPixelX, currentPixelY, overlayBuffer, SCALE);
    }

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}


    public int getDrawWidth() {
        return WIDTH;
    }

    public int getDrawHeight() {
        return HEIGHT;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            currentTool = new SquareEraser();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            currentTool = new SquareBrush();
        }
    }
}
