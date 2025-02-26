import java.awt.Color;

public class ColourManager {
    private static ColourManager colourManager;
    private static Color primary, secondary;
    
    private ColourManager() {
        primary = Color.BLACK;
        secondary = Color.WHITE;
    }

    public static ColourManager getColourManager() {
        if (colourManager == null)
            return new ColourManager();
        return colourManager;
    }

    public void setPrimary(int r, int g, int b, int a) {
        primary = new Color(r, g, b, a);
    }

    public void setSecondary(int r, int g, int b, int a) {
        secondary = new Color(r, g, b, a);
    }

    public Color getPrimary() {
        
        return primary;
    }

    public Color getSecondary() {
        return secondary;
    }
}
