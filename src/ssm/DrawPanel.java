package ssm;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import ssm.colour.ColourObject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import ssm.file.ImageFileManager;
import ssm.tools.Tool;
import ssm.tools.ToolManager;

public class DrawPanel extends JPanel implements MouseInputListener, ColourObject, Refreshable {
    private final int WIDTH = 500, HEIGHT = 500;
    private int drawWidth, drawHeight, scale;
    private int width, height;
    private BufferedImage drawBuffer, overlayBuffer, renderBuffer, writeBuffer, backgroundBuffer;
    private int x, y;
    private Color primary, secondary;
    private Tool currentTool;
    private ToolManager toolManager;
    private ImageFileManager imageFileManager;
    private int currentPixelX, currentPixelY;
    
    public DrawPanel() {
        setBackground(new Color(180, 180, 180));
        drawWidth = 25;
        drawHeight = 25;
        x = 20;
        y = 20;
        if (drawWidth > drawHeight)
            scale = WIDTH / drawWidth;
        else
            scale = WIDTH / drawHeight;
        width = drawWidth * scale;
        height = drawHeight * scale;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        currentPixelX = currentPixelY = -1;
        toolManager = ToolManager.getToolManager();
        currentTool = toolManager.getSquareBrush();
        imageFileManager = ImageFileManager.getImageFileManager();
        addMouseListener(this);
        addMouseMotionListener(this);
        drawBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        overlayBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        renderBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        writeBuffer = new BufferedImage(width / scale, height / scale, BufferedImage.TYPE_INT_ARGB);
        backgroundBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
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
        g2.drawImage(renderBuffer, x, y, null);
        g2.dispose();
    }

    public void clear() {
        Graphics2D d2 = (Graphics2D) backgroundBuffer.getGraphics();
        try {
            Paint transparency = new TexturePaint(imageFileManager.openImage(getClass().getResourceAsStream("res/transparentTexture.png")), new Rectangle2D.Double(0, 0, 100, 100));
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
        int[] overlayPix = new int[width * height];
        overlayBuffer.getRGB(0, 0, width, height, overlayPix, 0, width);
        for (int i = 0; i < width * height; i++) {
            overlayPix[i] = 0;
        }
        overlayBuffer.setRGB(0, 0, width, height, overlayPix, 0, width);
    }

    public void refresh() {
        render();
    }

    public void updateColours(Color primary, Color secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    public void mouseDragged(MouseEvent e) {
        currentPixelX = (e.getX() - x) / scale;
        currentPixelY = (e.getY() - y) / scale;
        currentTool = toolManager.getCurrent();
        if(SwingUtilities.isLeftMouseButton(e))
            currentTool.use(currentPixelX, currentPixelY, primary, drawBuffer, writeBuffer, scale);
        if(SwingUtilities.isRightMouseButton(e))
        currentTool.use(currentPixelX, currentPixelY, secondary, drawBuffer, writeBuffer, scale);
        currentTool.preview(currentPixelX, currentPixelY, overlayBuffer, scale);
    }

    public void mouseMoved(MouseEvent e) {
        int eventX = Math.round((e.getX() - x) / scale);
        int eventY = Math.round((e.getY() - y) / scale);
        System.out.println(e.getX() + " " + e.getY() + " " + eventX + " " + eventY);
        currentTool = toolManager.getCurrent();
        if (eventX != currentPixelX || eventY != currentPixelY) {
            currentPixelX = eventX;
            currentPixelY = eventY;
            currentTool.preview(currentPixelX, currentPixelY, overlayBuffer, scale);
        }
    }

    public void mouseClicked(MouseEvent e) {
        currentTool = toolManager.getCurrent();
        if(SwingUtilities.isLeftMouseButton(e))
            currentTool.use(currentPixelX, currentPixelY, primary, drawBuffer, writeBuffer, scale);
        if(SwingUtilities.isRightMouseButton(e))
            currentTool.use(currentPixelX, currentPixelY, secondary, drawBuffer, writeBuffer, scale);
    }

    public void mouseExited(MouseEvent e) {
        clearOverlay();
    }

    public void mouseEntered(MouseEvent e) {
        currentTool = toolManager.getCurrent();
        int onMask = 0;
        int offMask = MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON2_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK;
        if ((e.getModifiersEx() & (onMask | offMask)) == onMask)
            currentTool.preview(currentPixelX, currentPixelY, overlayBuffer, scale);
    }

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}


    public int getDrawWidth() {
        return WIDTH;
    }

    public int getDrawHeight() {
        return HEIGHT;
    }
}
