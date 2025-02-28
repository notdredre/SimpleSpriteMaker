package ssm.tools;

import ssm.tools.ToolManager.ToolType;

public abstract class DrawTool extends Tool {
    protected int size;

    public DrawTool() {
        size = 1;
    }

    public DrawTool(int size) {
        this.size = size;
    }

    public void increaseSize() {
        size++;
    }

    public void decreaseSize() {
        size--;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    public int getSize() {
        return size;
    }

    public ToolType getToolType() {
        return ToolType.DRAWTOOL;
    }
}
