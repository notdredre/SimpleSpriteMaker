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
    private int numRows, numCols, currentRow, currentCol, lastRow, lastCol, previewRow, previewCol;
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
        lastRow = lastCol = previewRow = previewCol = -1;
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

    public void setPreview(int row, int col) {
        if (row >= 0 && row < numRows && col >= 0 && col < numCols) {
            previewRow = row;
            previewCol = col;
        }
    }
 
    private BufferedImage getPreviewBuffer(int row, int col) {
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

    public BufferedImage getPreviewBuffer() {
        if (previewRow != -1 && previewCol != -1)
            return getPreviewBuffer(previewRow, previewCol);
        else
            return null;
    }

    private BufferedImage getDrawBuffer(int row, int col) {
        return drawings.get(row * numCols + col).getBuffer();
    }

    public BufferedImage getDrawBuffer() {
        return getDrawBuffer(currentRow, currentCol);
    }

    public BufferedImage getWriteBuffer() {
        return writeBuffers.get(currentRow * numCols + currentCol);
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
        currentCol = (currentCol + 1) % numCols;
        if (currentCol == 0)
            currentRow = (currentRow + 1) % numRows;
    }

    public void moveLeft() {
        currentCol = Math.abs((currentCol - 1) % numCols);
        if (currentCol == numCols - 1)
            currentRow = Math.abs((currentRow - 1) % numRows);
    }

    public void moveDown() {
        currentRow = (currentRow + 1) % numRows;
        if (currentRow == 0)
            currentCol = (currentCol + 1) % numCols;
    }

    public void moveUp() {
        currentRow = Math.abs((currentRow - 1) % numRows);
        if (currentRow == numRows - 1)
            currentCol = Math.abs((currentCol - 1) % numCols);
    }
}
