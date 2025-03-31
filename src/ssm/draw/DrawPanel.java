package ssm.draw;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import ssm.ProjectListener;
import ssm.Refreshable;
import ssm.colour.ColourObject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import ssm.tools.Tool;
import ssm.tools.ToolListener;
import ssm.tools.ToolManager;

public class DrawPanel extends JPanel implements ColourObject, Refreshable, ToolListener, ProjectListener {
    private final int WIDTH = 500, HEIGHT = 500, RESIZE_MAX = 5;
    private final Color bgColour = new Color(180, 180, 180);
    private int scale, scaleWidth, scaleHeight, pixelWidth, pixelHeight;
    private BufferedImage drawBuffer, overlayBuffer, renderBuffer, writeBuffer, backgroundBuffer, previewBuffer,
            compositeBuffer;
    private int x, y;
    private float percentX, percentY;
    private Color primary, secondary;
    private Tool currentTool;
    private ToolManager toolManager;
    private int currentPixelX, currentPixelY;
    private int resizeX, resizeY, resizeFactor;
    private int panelWidth, panelHeight;
    private DrawingMouseListener drawingMouseListener;
    private DrawingKeyboardListener drawingKeyboardListener;
    private UndoStack undoStack;

    public DrawPanel() {
        setBackground(bgColour);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panelWidth = panelHeight = 0;
        scale = 20;
        toolManager = ToolManager.getToolManager();
        toolManager.addToolListener(this);
        drawingMouseListener = new DrawingMouseListener(this);
        drawingKeyboardListener = new DrawingKeyboardListener(this);
        addMouseListener(drawingMouseListener);
        addMouseMotionListener(drawingMouseListener);
        addMouseWheelListener(drawingMouseListener);
        addKeyListener(drawingKeyboardListener);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                render();
            }
        });
    }

    public void init(int pixelWidth, int pixelHeight) {
        scaleWidth = pixelWidth * scale;
        scaleHeight = pixelHeight * scale;
        panelWidth = getWidth();
        panelHeight = getHeight();
        resizeFactor = 1;
        resizeX = Math.clamp(resizeFactor * scaleWidth, scaleWidth, scaleWidth * RESIZE_MAX);
        resizeY = Math.clamp(resizeFactor * scaleHeight, scaleHeight, scaleHeight * RESIZE_MAX);
        percentX = percentY = 0.5f;
        currentPixelX = currentPixelY = -1;
        createBuffers(pixelWidth, pixelHeight, scale);
        positionDrawing();
        clear();
    }

    public void createBuffers(int pixelWidth, int pixelHeight, int scale) {
        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;
        drawBuffer = new BufferedImage(scaleWidth, scaleHeight, BufferedImage.TYPE_INT_ARGB);
        writeBuffer = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_ARGB);
        overlayBuffer = new BufferedImage(scaleWidth, scaleHeight, BufferedImage.TYPE_INT_ARGB);
        renderBuffer = new BufferedImage(scaleWidth, scaleHeight, BufferedImage.TYPE_INT_RGB);
        backgroundBuffer = new BufferedImage(scaleWidth, scaleHeight, BufferedImage.TYPE_INT_RGB);
        if (panelWidth > 0 && panelHeight > 0)
            compositeBuffer = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_RGB);
        else
            compositeBuffer = new BufferedImage(scaleWidth, scaleHeight, BufferedImage.TYPE_INT_RGB);
    }

    public void render() {
        requestFocus();
        Graphics2D c2 = (Graphics2D) compositeBuffer.getGraphics();
        c2.setColor(getBackground());
        c2.fillRect(0, 0, getWidth(), getHeight());

        if (renderBuffer != null) {
            Graphics2D r2 = (Graphics2D) renderBuffer.getGraphics();
            r2.drawImage(backgroundBuffer, 0, 0, null);
            if (previewBuffer != null)
                r2.drawImage(previewBuffer, 0, 0, null);
            r2.drawImage(drawBuffer, 0, 0, null);
            r2.drawImage(overlayBuffer, 0, 0, null);
            r2.dispose();
            c2.drawImage(renderBuffer.getScaledInstance(resizeX, resizeY, BufferedImage.SCALE_FAST), x, y, null);
            drawPositionMarkers(0, 0, pixelWidth, pixelHeight);
            c2.setColor(Color.BLACK);
            String currentCellOut = "(" + (currentPixelY + 1) + ", " + (currentPixelX + 1) + ")";
            c2.drawString(currentCellOut, compositeBuffer.getWidth() - 50, compositeBuffer.getHeight() - 50);
        }

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

    public void useTool(int eventX, int eventY, int option) {
        scaleInput(eventX, eventY);
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

    public boolean comparePixel(int eventX, int eventY) {
        int newX = ((eventX - x) / scale) / (resizeX / scaleWidth);
        int newY = ((eventY - y) / scale) / (resizeY / scaleHeight);
        if (newX == currentPixelX && newY == currentPixelY)
            return true;
        return false;
    }

    public void scaleInput(int eventX, int eventY) {
        currentPixelX = ((eventX - x) / scale) / (resizeX / scaleWidth);
        currentPixelY = ((eventY - y) / scale) / (resizeY / scaleHeight);
    }

    private void drawPositionMarkers(int x1, int y1, int x2, int y2) {
        int xWidth = x2 - x1;
        int yHeight = y2 - y1;
        if (xWidth >= 20) {
            drawWidthMarkers(x1, x2 / 2);
            drawWidthMarkers(x2 / 2, x2);
        }
        if (yHeight >= 20) {
            drawHeightMarkers(y1, y2 / 2);
            drawHeightMarkers(y2 / 2, y2);
        }
        Graphics2D c2 = (Graphics2D) compositeBuffer.getGraphics();
        c2.setColor(new Color(0, 0, 0, 127));
        c2.setFont(new Font(Font.DIALOG, Font.BOLD, scale * resizeFactor));
        int yOff = 0;
        if (y >= 0)
            yOff = y;
        if (xWidth >= 20)
            c2.drawString(Integer.toString(x1 + xWidth / 2), (x1 + xWidth / 2) * scale * resizeFactor + x - scale * resizeFactor, scale * resizeFactor + yOff);
        c2.drawString(Integer.toString(x2), x2 * scale * resizeFactor + x - scale * resizeFactor, scale * resizeFactor + yOff);

        int xOff = 0;
        if (x >= 0)
            xOff = x;
        if (yHeight >= 20)
            c2.drawString(Integer.toString(y1 + yHeight / 2), xOff, (y1 + yHeight / 2) * scale * resizeFactor + y);
        c2.drawString(Integer.toString(y2), xOff, (y2) * scale * resizeFactor + y);
    }

    private void drawWidthMarkers(int x1, int x2) {
        int xWidth = x2 - x1;
        if (xWidth < 20)
            return;

        Graphics2D c2 = (Graphics2D) compositeBuffer.getGraphics();
        c2.setColor(new Color(0, 0, 0, 127));
        c2.setFont(new Font(Font.DIALOG, Font.BOLD, scale * resizeFactor));
        int yOff = 0;
        if (y >= 0)
            yOff = y;
        c2.drawString(Integer.toString(x1 + xWidth / 2), (x1 + xWidth / 2) * scale * resizeFactor + x - scale * resizeFactor, scale * resizeFactor + yOff);

    }

    private void drawHeightMarkers(int y1, int y2) {
        int yHeight = y2 - y1;
        if (yHeight < 20)
            return;

        Graphics2D c2 = (Graphics2D) compositeBuffer.getGraphics();
        c2.setColor(new Color(0, 0, 0, 127));
        c2.setFont(new Font(Font.DIALOG, Font.BOLD, scale * resizeFactor));
        int xOff = 0;
        if (x >= 0)
            xOff = x;
        c2.drawString(Integer.toString(y1 + yHeight / 2), xOff, (y1 + yHeight / 2) * scale * resizeFactor + y);
    }

    public void clearOverlay() {
        clearBuffer(overlayBuffer);
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

    public void commit() {
        undoStack.push(drawBuffer, writeBuffer);
    }

    public void undo() {
        if (!undoStack.isLast()) {
            clearBuffer(drawBuffer);
            clearBuffer(writeBuffer);
            undoStack.pop(drawBuffer, writeBuffer);
            render();
        }
    }

    private void resetBackground() {
        if (getHeight() > 0 && getWidth() > 0)
            backgroundBuffer = new BufferedImage(scaleWidth, scaleHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D b2 = (Graphics2D) backgroundBuffer.getGraphics();
        b2.setBackground(getBackground());
        b2.fillRect(0, 0, getWidth(), getHeight());
        try {
            File trasnparencyFile = new File("src/ssm/res/transparentTexture.png");
            Paint transparency = new TexturePaint(ImageIO.read(trasnparencyFile),
                    new Rectangle2D.Double(0, 0, scale * 5, scale * 5));
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

    public Tool getCurrentTool() {
        return currentTool;
    }

    public void onBuffersChanged(BufferedImage drawBuffer, BufferedImage writeBuffer, BufferedImage previewBuffer) {
        this.drawBuffer = drawBuffer;
        this.writeBuffer = writeBuffer;
        this.previewBuffer = previewBuffer;
        render();
    }

    public void onNewProject(int numRows, int numCols, int drawingWidth, int drawingHeight) {
        init(drawingWidth, drawingHeight);
    }


    public void onCellChanged(int currentRow, int currentCol, UndoStack currentUndo) {
        undoStack = currentUndo;
    }
}