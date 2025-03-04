package ssm.draw;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import ssm.file.ImageFileManager;
import ssm.tools.ToolManager;

public class Project {
    private ImageFileManager imageFileManager;
    private static Project project = null;
    private ToolManager toolManager;
    private ArrayList<Drawing> drawings;
    private ArrayList<BufferedImage> writeBuffers;
    private int numRows, numCols, currentRow, currentCol, lastRow, lastCol;
    private int drawingWidth, drawingHeight;
    private int scale;

    private Project(int drawingWidth, int drawingHeight, int scale) {
        this(1, 1, drawingWidth, drawingHeight, scale);
    }

    private Project(int numRows, int numCols, int drawingWidth, int drawingHeight, int scale) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.drawingWidth = drawingWidth;
        this.drawingHeight = drawingHeight;
        this.scale = scale;
        imageFileManager = ImageFileManager.getImageFileManager();
        toolManager = ToolManager.getToolManager();
        toolManager.getSquareBrush();
        lastRow = lastCol = -1;
        currentRow = currentCol = 0;
        drawings = new ArrayList<>(numRows * numCols);
        writeBuffers = new ArrayList<>(numRows * numCols);
        for (int i = 0; i < numRows * numCols; i++) {
            addDrawing();
        }
    }

    public static Project newProject(int numRows, int numCols, int drawingWidth, int drawingHeight, int scale) {
        project = new Project(numRows, numCols, drawingWidth, drawingHeight, scale);
        return project;
    }

    public static Project newProject(int drawingWidth, int drawingHeight, int scale) {
        project = new Project(drawingWidth, drawingHeight, scale);
        return project;
    }

    public static Project getProject() {
        return project;
    }

    public void addDrawing() {
        lastCol = (lastCol + 1) % numCols;
        if (lastCol == 0)
            lastRow = (lastRow + 1) % numRows;

        drawings.add(new Drawing(drawingWidth * scale, drawingHeight * scale, lastRow, lastCol));
        addWriteBuffer();
    }

    private void addWriteBuffer() {
        writeBuffers.add(new BufferedImage(drawingWidth, drawingHeight, BufferedImage.TYPE_INT_ARGB));
    }
 
    public BufferedImage getPreviewBuffer(int row, int col) {
        BufferedImage selected = getDrawBuffer(row, col);
        int[] previewPix = new int[drawingWidth * drawingHeight * scale * scale];
        selected.getRGB(0, 0, drawingWidth * scale, drawingHeight * scale, previewPix, 0, drawingWidth * scale);
        for (int i = 0; i < drawingWidth * drawingHeight * scale * scale; i++) {
            if (previewPix[i] != 0) {
                int alpha = 100;
                int rest = previewPix[i] << 8;
                previewPix[i] = alpha << 24 | (rest >> 8);
            }
        }
        BufferedImage out = new BufferedImage(drawingWidth * scale, drawingHeight * scale, BufferedImage.TYPE_INT_ARGB);
        out.setRGB(0, 0, drawingWidth * scale, drawingHeight * scale, previewPix, 0, drawingWidth * scale);
        return out;
    }

    private BufferedImage getDrawBuffer(int row, int col) {
        return drawings.get(row + col).getBuffer();
    }

    public BufferedImage getDrawBuffer() {
        return getDrawBuffer(currentRow, currentCol);
    }

    public BufferedImage getWriteBuffer() {
        return writeBuffers.get(currentRow + currentCol);
    }

    public BufferedImage getFinalWrite() {
        BufferedImage finalWrite = new BufferedImage(drawingWidth * numCols, drawingHeight * numRows, BufferedImage.TYPE_INT_ARGB);
        Graphics2D f2 = (Graphics2D) finalWrite.getGraphics();
        int current = 0;
        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                f2.drawImage(writeBuffers.get(current), j * drawingWidth, i * drawingHeight, null);
                current++;
            }
        }
        f2.dispose();
        return finalWrite;
    }

    public void saveProject() {
        imageFileManager.setToWrite(getFinalWrite());
        imageFileManager.saveImage();
    }
    
    public void moveRight() {
        if (currentCol < numCols - 1)
            currentCol++;
    }

    public void moveLeft() {
        if (currentCol > 0)
            currentCol--;
    }

    public void moveDown() {
        if (currentRow < numRows - 1)
            currentRow++;
    }

    public void moveUp() {
        if (currentRow > 0)
            currentRow--;
    }
}
