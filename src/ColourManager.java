import java.awt.Color;
import java.util.ArrayList;

public class ColourManager {
    private static ColourManager colourManager;
    private static Color[] colours;
    private static ArrayList<ColourObject> colourObjects;
    private static int selected;

    private ColourManager() {
        selected = 0;
        colours = new Color[2];
        colours[0] = Color.BLACK;
        colours[1] = Color.WHITE;
        colourObjects = new ArrayList<>();
    }

    public static ColourManager getColourManager() {
        if (colourManager == null) {
            colourManager = new ColourManager();
        }
            
        return colourManager;
    }

    public void setSelected(int choice) {
        selected = choice;
        for (ColourObject c : colourObjects) {
            c.changeColour(choice);
        }
    }

    public int getSelected() {
        return selected;
    }

    public void setColour(Color c) {
        colours[selected] = c;
        updateColourObjects();
    }

    public void setColour(int r, int g, int b) {
        colours[selected] = new Color(r, g, b);
        updateColourObjects();
    }

    public void setColour(int r, int g, int b, int a) {
        colours[selected] = new Color(r, g, b, a);
        updateColourObjects();
    }


    public void addColourObject(ColourObject c) {
        if (c instanceof ColourObject) {
            colourObjects.add(c);
            return;
        }
        throw new IllegalArgumentException();
    }

    public void updateColourObjects() {
        for (ColourObject c : colourObjects) {
            c.updateColours(colours[0], colours[1]);
        }
    }
}
