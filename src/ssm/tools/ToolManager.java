package ssm.tools;

import java.util.HashMap;

public class ToolManager {
    private static ToolManager toolManager = null;
    private HashMap<String, Tool> tools;
    private Tool current;

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
}
