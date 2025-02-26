import java.awt.Color;
import java.util.ArrayList;

public class ColourManager {
    private static ColourManager colourManager;
    private static Color primary, secondary;
    private static ArrayList<ColourObject> colourObjects;

    private ColourManager() {
        primary = Color.BLACK;
        secondary = Color.WHITE;
        colourObjects = new ArrayList<>();
    }

    public static ColourManager getColourManager() {
        if (colourManager == null)
            return new ColourManager();
        return colourManager;
    }

    public void setPrimary(Color c) {
        primary = c;
        updateColourObjects();
    }

    public void setPrimary(int r, int g, int b) {
        primary = new Color(r, g, b);
        updateColourObjects();
    }

    public void setPrimary(int r, int g, int b, int a) {
        primary = new Color(r, g, b, a);
        updateColourObjects();
    }

    public void setSecondary(Color c) {
        secondary = c;
        updateColourObjects();
    }

    public void setSecondary(int r, int g, int b) {
        secondary = new Color(r, g, b);
        updateColourObjects();
    }

    public void setSecondary(int r, int g, int b, int a) {
        secondary = new Color(r, g, b, a);
        updateColourObjects();
    }

    public void addColourObject(ColourObject c) {
        if (c instanceof ColourObject) {
            colourObjects.add(c);
            c.updateColours(primary, secondary);
            return;
        }
        throw new IllegalArgumentException();
    }

    private void updateColourObjects() {
        for (ColourObject c : colourObjects) {
            c.updateColours(primary, secondary);
        }
    }
}
