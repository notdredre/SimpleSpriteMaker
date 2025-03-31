package ssm;

import java.awt.image.BufferedImage;

import ssm.draw.UndoStack;

public interface ProjectListener {
    public abstract void onNewProject(int numRows, int numCols, int drawingWidth, int drawingHeight);
    public abstract void onCellChanged(int currentRow, int currentColumn, UndoStack currentUndo);
    public abstract void onBuffersChanged(BufferedImage drawBuffer, BufferedImage writeBuffer, BufferedImage previewBuffer);
}
