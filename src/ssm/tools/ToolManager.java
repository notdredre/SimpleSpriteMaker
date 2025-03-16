package ssm.tools;

import java.util.ArrayList;
import java.util.HashMap;

public class ToolManager {
    private static ToolManager toolManager = null;
    private HashMap<String, Tool> tools;
    private Tool current;
    private static ArrayList<ToolListener> toolListeners;

    public enum ToolType {
        DRAWTOOL("DrawTool"),
        FILLTOOL("FillTool");

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
        tools.put("Fill", new FillTool());
        tools.put("ColourPicker", new ColourPicker());
        toolListeners = new ArrayList<>();
        current = tools.get("SquareBrush");
    }

    public static ToolManager getToolManager() {
        if (toolManager == null)
            toolManager = new ToolManager();
        return toolManager;
    }

    public void addToolListener(ToolListener t) {
        toolListeners.add(t);
        t.setCurrentTool(current);
    }

    private void updateToolListeners() {
        for (ToolListener t : toolListeners) {
            t.setCurrentTool(current);
        }
    }

    public Tool getCurrent() {
        return current;
    }

    public void getSquareBrush() {
        current = tools.get("SquareBrush");
        updateToolListeners();
    }

    public void getSquareEraser() {
        current = tools.get("SquareEraser");
        updateToolListeners();
    }

    public void getFill() {
        current = tools.get("Fill");
        updateToolListeners();
    }

    public void getColourPicker() {
        current = tools.get("ColourPicker");
        updateToolListeners();
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
