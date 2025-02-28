package ssm.tools;

import java.util.HashMap;

public class ToolManager {
    private static ToolManager toolManager = null;
    private HashMap<String, Tool> tools;
    private Tool current;

    public enum ToolType {
        DRAWTOOL("DrawTool");

        private String value;
        ToolType(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    private ToolManager() {
        tools = new HashMap<>();
        tools.put("SquareBrush", new SquareBrush());
        tools.put("SquareEraser", new SquareEraser());
        current = tools.get("SquareBrush");
    }

    public static ToolManager getToolManager() {
        if (toolManager == null)
            toolManager = new ToolManager();
        return toolManager;
    }

    public Tool getCurrent() {
        return current;
    }

    public Tool getSquareBrush() {
        current = tools.get("SquareBrush");
        return current;
    }

    public Tool getSquareEraser() {
        current = tools.get("SquareEraser");
        return current;
    }

    public void increaseSize() {
        if (current instanceof DrawTool) {
            ((DrawTool) current).increaseSize();
        }
    }

    public void decreaseSize() {
        if (current instanceof DrawTool) {
            ((DrawTool) current).decreaseSize();
        }
    }

    public void setSize(Number size) {
        if (current instanceof DrawTool) {
            ((DrawTool) current).setSize(size.intValue());
        }
    }

    public int getSize() {
        if (current instanceof DrawTool) {
            return ((DrawTool) current).getSize();
        }
        return -1;
    }
}
