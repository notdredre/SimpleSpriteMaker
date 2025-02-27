import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class ColourPreview extends JPanel implements ColourSwitcher, MouseListener {
    private ColourSwatch primary, secondary;
    private SpringLayout layout;
    private ColourManager colourManager;
    private final int WIDTH = 100, HEIGHT = 50, SIZE = 50;
    private int selected;

    public ColourPreview() {
        colourManager = ColourManager.getColourManager();
        layout = new SpringLayout();
        setSize(WIDTH, HEIGHT);
        primary = new ColourSwatch(SIZE);
        secondary = new ColourSwatch(SIZE);
        setLayout(layout);

        layout.putConstraint(SpringLayout.NORTH, primary, 10, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.NORTH, secondary, 10, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, primary, 30, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, secondary, 5, SpringLayout.EAST, primary);
        layout.putConstraint(SpringLayout.EAST, primary, SIZE + 1, SpringLayout.WEST, primary);
        layout.putConstraint(SpringLayout.EAST, secondary, SIZE + 1, SpringLayout.WEST, secondary);
        layout.putConstraint(SpringLayout.SOUTH, primary, SIZE + 1, SpringLayout.NORTH, primary);
        layout.putConstraint(SpringLayout.SOUTH, secondary, SIZE + 1, SpringLayout.NORTH, secondary);
        layout.putConstraint(SpringLayout.EAST, this, 30, SpringLayout.EAST, secondary);
        
        add(primary);
        add(secondary);
        addMouseListener(this);
    }

    private void drawColours() {
        primary.draw();
        secondary.draw();
        if (selected == 0) {
            primary.highlight();
        }
        if (selected == 1) {
            secondary.highlight();
        }
    }

    public void switchColour(int selected) {
        this.selected = selected;
    }

    public void updateColours(Color primary, Color secondary) {
        this.primary.setColour(primary);
        this.secondary.setColour(secondary);
        drawColours();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        drawColours();
        if (primary.getBounds().contains(e.getPoint())) {
            colourManager.setSelected(0);
            primary.highlight();
        }
        if (secondary.getBounds().contains(e.getPoint())) {
            colourManager.setSelected(1);
            secondary.highlight();
        }
        
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    
}
