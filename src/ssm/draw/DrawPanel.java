package ssm.draw;
import javax.swing.JPanel;
import ssm.Refreshable;
import ssm.colour.ColourObject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import ssm.file.ImageFileManager;
import ssm.tools.Tool;
import ssm.tools.ToolListener;
import ssm.tools.ToolManager;

public class DrawPanel extends JPanel implements ColourObject, Refreshable, ToolListener {
    private final int WIDTH = 500, HEIGHT = 500, RESIZE_MAX = 5;
    private final Color bgColour = new Color(180, 180, 180);
    private int scale;
    private int scaleWidth, scaleHeight, pixelWidth, pixelHeight;
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
    private DrawingMouseListener drawingMouseListener;
    
    public DrawPanel() {
        setBackground(bgColour);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        toolManager = ToolManager.getToolManager();
        toolManager.addToolListener(this);
        imageFileManager = ImageFileManager.getImageFileManager();
        drawingMouseListener = new DrawingMouseListener(this);
        addMouseListener(drawingMouseListener);
        addMouseMotionListener(drawingMouseListener);
        addMouseWheelListener(drawingMouseListener);
        panelWidth = panelHeight = 0;
        scaleWidth = scaleHeight = 0;
        createNewDrawing(30, 30);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                render();
            }
        });
    }

    public void createNewDrawing(int pixelWidth, int pixelHeight) {
        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;
        if (pixelWidth > pixelHeight)
            scale = WIDTH / pixelWidth;
        else
            scale = HEIGHT / pixelHeight;
        scaleWidth = pixelWidth * scale;
        scaleHeight = pixelHeight * scale;
        panelWidth = getWidth();
        panelHeight = getHeight();
        resizeFactor = 1;
        resizeX = Math.clamp(resizeFactor * scaleWidth, scaleWidth, scaleWidth * RESIZE_MAX);
        resizeY = Math.clamp(resizeFactor * scaleHeight, scaleHeight, scaleHeight * RESIZE_MAX);
        percentX = percentY = 0.5f;
        currentPixelX = currentPixelY = -1;
        drawBuffer = new BufferedImage(scaleWidth, scaleHeight, BufferedImage.TYPE_INT_ARGB);
        overlayBuffer = new BufferedImage(scaleWidth, scaleHeight, BufferedImage.TYPE_INT_ARGB);
        renderBuffer = new BufferedImage(scaleWidth, scaleHeight, BufferedImage.TYPE_INT_RGB);
        writeBuffer = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_ARGB);
        backgroundBuffer = new BufferedImage(scaleWidth, scaleHeight, BufferedImage.TYPE_INT_RGB);
        if (panelWidth > 0 && panelHeight > 0)
            compositeBuffer = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_RGB);
        else
        compositeBuffer = new BufferedImage(scaleWidth, scaleHeight, BufferedImage.TYPE_INT_RGB);
        imageFileManager.setToWrite(writeBuffer);
        toolManager.getSquareBrush();
        positionDrawing();
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
        if (g2 == null)
            return;        
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
        if (panelHeight == getHeight() && panelWidth == getWidth()) {
            return;
        }
        panelWidth = getWidth();
        panelHeight = getHeight();
        compositeBuffer = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_RGB);
        positionDrawing();
        render();
    }

    public void updateColours(Color primary, Color secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    public void postGraphicsInit() {
        positionDrawing();
        resetBackground();
    }

    public void useTool(int option) {
        if (option == 0) {
            currentTool.use(currentPixelX, currentPixelY, primary, drawBuffer, writeBuffer, scale);
        }
        if (option == 1) {
            currentTool.use(currentPixelX, currentPixelY, secondary, drawBuffer, writeBuffer, scale);
        }
    }

    public void previewTool() {
        currentTool.preview(currentPixelX, currentPixelY, drawBuffer, overlayBuffer, scale);
    }

    public void scaleInput(int eventX, int eventY) {
        currentPixelX = ((eventX - x) / scale) / (resizeX / scaleWidth);
        currentPixelY = ((eventY - y) / scale) / (resizeY / scaleHeight);
    }

    public void clearOverlay() {
        clearBuffer(overlayBuffer);
    }

    public void resize(int resizeAmount) {
        resizeFactor = Math.clamp(resizeFactor - resizeAmount, 1, RESIZE_MAX);
        resizeX = Math.clamp(resizeFactor * scaleWidth, scaleWidth, scaleWidth * RESIZE_MAX);
        resizeY = Math.clamp(resizeFactor * scaleHeight, scaleHeight, scaleHeight * RESIZE_MAX);
        positionDrawing();
    }

    private void positionDrawing() {
        x = Math.round(percentX * getWidth()) - resizeX / 2;
        y = Math.round(percentY * getHeight()) - resizeY / 2;
    }

    private void reposition(float percentX, float percentY) {
        this.percentX = percentX;
        this.percentY = percentY;
        positionDrawing();
    }

    public void reposition(int newX, int newY, int oldX, int oldY) {  
        float diffX = newX - oldX;
        float diffY = newY - oldY;
        percentX += diffX / panelWidth;
        percentY += diffY / panelHeight;
        reposition(percentX, percentY);
    }

    public void scaleToWidth() {
        scale = getWidth() / pixelWidth;
    }

    public void scaleToHeight() {
        System.out.println(getHeight());
        scale = getHeight() / pixelHeight;
    }

    private void resetBackground() {
        if (getHeight() > 0 && getWidth() > 0)
            backgroundBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        clearBuffer(backgroundBuffer);
        Graphics2D b2 = (Graphics2D) backgroundBuffer.getGraphics();
        b2.setBackground(getBackground());
        b2.fillRect(0, 0, getWidth(), getHeight());
        try {
            Paint transparency = new TexturePaint(imageFileManager.openImage(getClass().getResourceAsStream("/ssm/res/transparentTexture.png")), new Rectangle2D.Double(0, 0, scale * 5, scale * 5));
            b2.setPaint(transparency);
        } catch (IOException e) {
            e.printStackTrace();
            b2.setColor(Color.WHITE);
        }
        b2.fillRect(0, 0, scaleWidth, scaleHeight);
        b2.dispose();
        render();
    }

    public void setCurrentTool(Tool tool) {
        currentTool = tool;
    }

    public int getPixelWidth() {
        return WIDTH;
    }

    public int getPixelHeight() {
        return HEIGHT;
    }
}
