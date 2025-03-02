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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import ssm.file.ImageFileManager;
import ssm.tools.Tool;
import ssm.tools.ToolManager;

public class DrawPanel extends JPanel implements MouseInputListener, MouseWheelListener, ColourObject, Refreshable {
    private final int WIDTH = 500, HEIGHT = 500, RESIZE_MAX = 5;
    private int scale;
    private int width, height, drawWidth, drawHeight;
    private BufferedImage drawBuffer, overlayBuffer, renderBuffer, writeBuffer, backgroundBuffer, compositeBuffer;
    private int x, y;
    private float percentX, percentY;
    private Color primary, secondary;
    private Tool currentTool;
    private ToolManager toolManager;
    private ImageFileManager imageFileManager;
    private int currentPixelX, currentPixelY;
    private int resizeX, resizeY, resizeFactor;
    private int panelWidth, panelHeight;
    private int mouseX, mouseY;
    
    public DrawPanel() {
        setBackground(new Color(180, 180, 180));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        toolManager = ToolManager.getToolManager();
        currentTool = toolManager.getSquareBrush();
        imageFileManager = ImageFileManager.getImageFileManager();
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        panelWidth = panelHeight = 0;
        createNewDrawing(30, 30);
    }

    public void createNewDrawing(int drawWidth, int drawHeight) {
        this.drawWidth = drawWidth;
        this.drawHeight = drawHeight;
        if (drawWidth > drawHeight)
            scale = WIDTH / drawWidth;
        else
            scale = HEIGHT / drawHeight;
        width = drawWidth * scale;
        height = drawHeight * scale;
        resizeFactor = 1;
        resizeX = Math.clamp(resizeFactor * width, width, width * RESIZE_MAX);
        resizeY = Math.clamp(resizeFactor * height, height, height * RESIZE_MAX);
        percentX = percentY = 0.5f;
        currentPixelX = currentPixelY = -1;
        currentTool = toolManager.getSquareBrush();
        drawBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        overlayBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        renderBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        writeBuffer = new BufferedImage(width / scale, height / scale, BufferedImage.TYPE_INT_ARGB);
        backgroundBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        compositeBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        imageFileManager.setToWrite(writeBuffer);
        clear();
    }

    public void render() {
        Graphics2D c2 = (Graphics2D) compositeBuffer.getGraphics();
        c2.setColor(getBackground());
        c2.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D r2 = (Graphics2D) renderBuffer.getGraphics();
        r2.drawImage(backgroundBuffer, 0, 0, null);
        r2.drawImage(drawBuffer, 0, 0, null);
        r2.drawImage(overlayBuffer, 0, 0, null);
        r2.dispose();      
        c2.drawImage(renderBuffer.getScaledInstance(resizeX, resizeY, BufferedImage.SCALE_FAST), x, y, null);
        c2.dispose();
        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.drawImage(compositeBuffer, 0, 0, null);
        g2.dispose();
    }

    public void clear() {
        clearBuffer(backgroundBuffer);
        clearBuffer(overlayBuffer);
        clearBuffer(drawBuffer);
        clearBuffer(writeBuffer);
        clearBuffer(compositeBuffer);
        resetBackground();
    }

    private void clearBuffer(BufferedImage buffer) {
        int bWidth = buffer.getWidth();
        int bHeight = buffer.getHeight();
        int[] pix = new int[bWidth * bHeight];
        buffer.getRGB(0, 0, bWidth, bHeight, pix, 0, bWidth);
        for (int i = 0; i < bWidth * bHeight; i++) {
            pix[i] = 0;
        }
        buffer.setRGB(0, 0, bWidth, bHeight, pix, 0, bWidth);
    }

    public void refresh() {
        resizeX = Math.clamp(resizeFactor * width, width, width * RESIZE_MAX);
        resizeY = Math.clamp(resizeFactor * height, height, height * RESIZE_MAX);
        positionDrawing();
        render();
        if (panelHeight == getHeight() && panelWidth == getWidth()) {
            return;
        }
        panelWidth = getWidth();
        panelHeight = getHeight();
        compositeBuffer = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_RGB);
        render();
        
    }

    public void updateColours(Color primary, Color secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    public void mouseDragged(MouseEvent e) {
        currentPixelX = ((e.getX() - x) / scale) / (resizeX / width);
        currentPixelY = ((e.getY() - y) / scale) / (resizeY / height);
        currentTool = toolManager.getCurrent();
        if(SwingUtilities.isLeftMouseButton(e))
            currentTool.use(currentPixelX, currentPixelY, primary, drawBuffer, writeBuffer, scale);
        if(SwingUtilities.isRightMouseButton(e)) 
            currentTool.use(currentPixelX, currentPixelY, secondary, drawBuffer, writeBuffer, scale);
        currentTool.preview(currentPixelX, currentPixelY, drawBuffer, overlayBuffer, scale);
    }

    public void mouseMoved(MouseEvent e) {
        int eventX = ((e.getX() - x) / scale) / (resizeX / width);
        int eventY = ((e.getY() - y) / scale) / (resizeY / height);
        currentTool = toolManager.getCurrent();
        if (eventX != currentPixelX || eventY != currentPixelY) {
            currentPixelX = eventX;
            currentPixelY = eventY;
            currentTool.preview(currentPixelX, currentPixelY, drawBuffer, overlayBuffer, scale);
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
        clearBuffer(overlayBuffer);
    }

    public void mouseEntered(MouseEvent e) {
        currentTool = toolManager.getCurrent();
        int onMask = 0;
        int offMask = MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON2_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK;
        if ((e.getModifiersEx() & (onMask | offMask)) == onMask)
            currentTool.preview(currentPixelX, currentPixelY, drawBuffer, overlayBuffer, scale);
    }

    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void mouseReleased(MouseEvent e) {}

    private void positionDrawing() {
        x = Math.round(percentX * getWidth()) - resizeX / 2;
        y = Math.round(percentY * getHeight()) - resizeY / 2;
    }

    private void reposition(float percentX, float percentY) {
        this.percentX = percentX;
        this.percentY = percentY;
    }

    private void reposition(int mouseX, int mouseY) {
        
        float diffX = mouseX - this.mouseX;
        float diffY = mouseY - this.mouseY;
        
        percentX += diffX / panelWidth;
        percentY += diffY / panelHeight;
        reposition(percentX, percentY);
    }

    public void scaleToWidth() {
        scale = getWidth() / drawWidth;
    }

    public void scaleToHeight() {
        System.out.println(getHeight());
        scale = getHeight() / drawHeight;
    }

    private void resetBackground() {
        if (getHeight() > 0 && getWidth() > 0)
            backgroundBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        clearBuffer(backgroundBuffer);
        Graphics2D b2 = (Graphics2D) backgroundBuffer.getGraphics();
        b2.setBackground(getBackground());
        b2.fillRect(0, 0, getWidth(), getHeight());
        try {
            Paint transparency = new TexturePaint(imageFileManager.openImage(getClass().getResourceAsStream("res/transparentTexture.png")), new Rectangle2D.Double(0, 0, scale * 5, scale * 5));
            b2.setPaint(transparency);
        } catch (IOException e) {
            e.printStackTrace();
            b2.setColor(Color.WHITE);
        }
        b2.fillRect(0, 0, width, height);
        b2.dispose();
    }

    public int getDrawWidth() {
        return WIDTH;
    }

    public int getDrawHeight() {
        return HEIGHT;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        resizeFactor = Math.clamp(resizeFactor - e.getWheelRotation(), 1, RESIZE_MAX);
    }
}
