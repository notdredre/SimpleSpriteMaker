package ssm.draw;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Project {
    private static Project project = null;
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
        lastRow = lastCol = -1;
        currentRow = currentCol = 0;
        drawings = new ArrayList<>(numRows * numCols);
        writeBuffers = new ArrayList<>(numRows * numCols);
        for (int i = 0; i < numRows * numCols; i++) {
            addDrawing();
        }
    }

    public static Project getProject(int numRows, int numCols, int drawingWidth, int drawingHeight, int scale) {
        if (project == null) {
            project = new Project(numRows, numCols, drawingWidth, drawingHeight, scale);
        }
        return project;
    }

    public static Project getProject(int drawingWidth, int drawingHeight, int scale) {
        if (project == null) {
            project = new Project(drawingWidth, drawingHeight, scale);
        }
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
 
    public BufferedImage getDrawBuffer() {
        return drawings.get(currentRow + currentCol).getBuffer();
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

    public void moveRight() {
        if (currentCol < numCols)
            currentCol++;
    }

    public void moveLeft() {
        if (currentCol > 0)
            currentCol--;
    }

    public void moveDown() {
        if (currentRow < numRows)
            currentRow++;
    }

    public void moveUp() {
        if (currentRow > 0)
            currentRow--;
    }
}
