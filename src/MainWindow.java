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

public class MainWindow extends JFrame implements ChangeListener {
    private final int WIDTH = 1000, HEIGHT = 800;
    private JPanel mainPanel, colourPanel;
    private SpringLayout mainLayout;
    private DrawPanel drawPanel;
    private JSlider[] colourSliders;
    private ColourManager colourManager;

    public MainWindow() {
        setTitle("SimpleSpriteMaker");
        setSize(WIDTH, HEIGHT);
        //setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setVisible(true);
        mainPanel.setBackground(new Color(220, 220, 220));
        drawPanel.clear();
    }

    private void initComponents() {
        colourManager = ColourManager.getColourManager();
        mainLayout = new SpringLayout();
        mainPanel = new JPanel();
        mainPanel.setLayout(mainLayout);
        drawPanel = new DrawPanel();
        mainPanel.add(drawPanel);

        colourPanel = new JPanel();
        colourPanel.setMaximumSize(new Dimension(200, 400));
        colourPanel.setLayout(new BoxLayout(colourPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(colourPanel);
        colourSliders = new JSlider[3];
        for (int i = 0; i < 3; i++) {
            colourSliders[i] = new JSlider(0, 255, 0);
            colourSliders[i].addChangeListener(this);
            colourPanel.add(colourSliders[i]);
            colourPanel.add(Box.createVerticalStrut(20));
        }

        // Constraints for drawPanel
        mainLayout.putConstraint(SpringLayout.WEST, drawPanel, 20, SpringLayout.WEST, mainPanel);
        mainLayout.putConstraint(SpringLayout.NORTH, drawPanel, 20, SpringLayout.NORTH, mainPanel);       
        mainLayout.putConstraint(SpringLayout.SOUTH, drawPanel, drawPanel.getDrawHeight(), SpringLayout.NORTH, drawPanel);
        mainLayout.putConstraint(SpringLayout.EAST, drawPanel, drawPanel.getDrawWidth(), SpringLayout.WEST, drawPanel);

        // Constraints for colourPanel
        mainLayout.putConstraint(SpringLayout.WEST, colourPanel, 30, SpringLayout.EAST, drawPanel);
        mainLayout.putConstraint(SpringLayout.EAST, mainPanel, 20, SpringLayout.EAST, colourPanel);
        mainLayout.putConstraint(SpringLayout.NORTH, colourPanel, 20, SpringLayout.NORTH, mainPanel);

        
        add(mainPanel);
    }

    public void stateChanged(ChangeEvent e) {
        int r = colourSliders[0].getValue();
        int g = colourSliders[1].getValue();
        int b = colourSliders[2].getValue();
        colourManager.setPrimary(r, g, b, 255);
    }
}
