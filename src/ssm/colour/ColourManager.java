package ssm.colour;
import java.awt.Color;
import java.util.ArrayList;

public class ColourManager {
    private static ColourManager colourManager;
    private static Color[] colours;
    private static ArrayList<ColourObject> colourObjects;
    private static ArrayList<ColourSwitcher> colourSwitchers;
    private static ArrayList<ColourReading> colourReadings;
    private static int selected;

    private ColourManager() {
        colours = new Color[2];
        colours[0] = Color.BLACK;
        colours[1] = Color.WHITE;
        colourObjects = new ArrayList<>();
        colourSwitchers = new ArrayList<>();
        colourReadings = new ArrayList<>();
        setSelected(0);
    }

    public static ColourManager getColourManager() {
        if (colourManager == null) {
            colourManager = new ColourManager();
        }
            
        return colourManager;
    }

    public void setSelected(int choice) {
        selected = choice;
        updateColourSwitchers();
        updateColourReadings();
    }

    public int getSelected() {
        return selected;
    }

    public void setColour(Color c) {
        colours[selected] = c;
        updateColourObjects();
    }

    public void setColour(int r, int g, int b) {
        setColour(new Color(r, g, b));
    }

    public void setColour(int r, int g, int b, int a) {
        setColour(new Color(r, g, b, a));
    }

    public void setRed(int r) {
        Color c = colours[selected];
        setColour(new Color(r, c.getGreen(), c.getBlue(), c.getAlpha()));
    }

    public void setGreen(int g) {
        Color c = colours[selected];
        setColour(new Color(c.getRed(), g, c.getBlue(), c.getAlpha()));
    }

    public void setBlue(int b) {
        Color c = colours[selected];
        setColour(new Color(c.getRed(), c.getGreen(), b, c.getAlpha()));
    }

    public void setAlpha(int a) {
        Color c = colours[selected];
        setColour(new Color(c.getRed(), c.getGreen(), c.getBlue(), a));
    }

    public void addColourObject(ColourObject c) {
        if (c instanceof ColourObject) {
            colourObjects.add(c);
            return;
        }
        throw new IllegalArgumentException();
    }

    public void addColourSwitcher(ColourSwitcher c) {
        if (c instanceof ColourSwitcher) {
            colourSwitchers.add(c);
            colourObjects.add(c);
            updateColourSwitchers();
            return;
        }
        throw new IllegalArgumentException();
    }

    public void addColourReading(ColourReading c) {
        if (c instanceof ColourReading) {
            colourReadings.add(c);
            return;
        }
        throw new IllegalArgumentException();
    }

    public void updateColourObjects() {
        for (ColourObject c : colourObjects) {
            c.updateColours(colours[0], colours[1]);
        }
    }

    public void updateColourSwitchers() {
        for (ColourSwitcher c : colourSwitchers) {
            c.switchColour(selected);
        }
    }

    public void updateColourReadings() {
        for (ColourReading c : colourReadings) {
            c.detailColour(colours[selected]);
        }
    }
}
