import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainWindow extends JFrame {
    private final int WIDTH = 1000, HEIGHT = 800;
    private JPanel mainPanel, colourPanel;
    private SpringLayout mainLayout;
    private DrawPanel drawPanel;
    private ColourSliders colourSliders;
    private ColourPreview colourPreview;
    private ColourManager colourManager;

    public MainWindow() {
        setTitle("SimpleSpriteMaker");
        setSize(WIDTH, HEIGHT);
        //setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setVisible(true);
        colourManager.updateColourObjects();
        mainPanel.setBackground(new Color(220, 220, 220));
        drawPanel.clear();
    }

    private void initComponents() {
        colourManager = ColourManager.getColourManager();

        mainLayout = new SpringLayout();
        mainPanel = new JPanel();
        mainPanel.setLayout(mainLayout);

        drawPanel = new DrawPanel();
        colourManager.addColourObject(drawPanel);
        mainPanel.add(drawPanel);

        colourPanel = new JPanel();
        colourPanel.setMaximumSize(new Dimension(200, 300));
        colourPanel.setLayout(new BoxLayout(colourPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(colourPanel);

        colourSliders = new ColourSliders();
        colourManager.addColourObject(colourSliders);
        colourPanel.add(colourSliders);

        colourPreview = new ColourPreview();
        colourManager.addColourObject(colourPreview);
        colourPanel.add(colourPreview);

        // Constraints for drawPanel
        mainLayout.putConstraint(SpringLayout.WEST, drawPanel, 20, SpringLayout.WEST, mainPanel);
        mainLayout.putConstraint(SpringLayout.NORTH, drawPanel, 20, SpringLayout.NORTH, mainPanel);       
        mainLayout.putConstraint(SpringLayout.SOUTH, drawPanel, drawPanel.getDrawHeight(), SpringLayout.NORTH, drawPanel);
        mainLayout.putConstraint(SpringLayout.EAST, drawPanel, drawPanel.getDrawWidth(), SpringLayout.WEST, drawPanel);

        // Constraints for colourPanel
        mainLayout.putConstraint(SpringLayout.WEST, colourPanel, 30, SpringLayout.EAST, drawPanel);
        mainLayout.putConstraint(SpringLayout.EAST, mainPanel, 20, SpringLayout.EAST, colourPanel);
        mainLayout.putConstraint(SpringLayout.NORTH, colourPanel, 20, SpringLayout.NORTH, mainPanel);
        mainLayout.putConstraint(SpringLayout.SOUTH, colourPanel, 200, SpringLayout.NORTH, colourPanel);

        
        add(mainPanel);
    }
}
