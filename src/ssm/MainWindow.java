package ssm;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import ssm.colour.ColourManager;
import ssm.colour.ColourPreview;
import ssm.colour.ColourSliders;
import ssm.file.SaveFilePanel;

public class MainWindow extends JFrame implements Runnable {
    private final int WIDTH = 1000, HEIGHT = 800;
    private JPanel mainPanel, colourPanel;
    private SpringLayout mainLayout;
    private DrawPanel drawPanel;
    private ColourSliders colourSliders;
    private ColourPreview colourPreview;
    private ColourManager colourManager;
    private SaveFilePanel saveFilePanel;
    private ArrayList<Refreshable> toRefresh;
    private Thread refreshThread;

    public MainWindow() {
        toRefresh = new ArrayList<>();
        setTitle("SimpleSpriteMaker");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setVisible(true);
        colourManager.updateColourObjects();
        mainPanel.setBackground(new Color(220, 220, 220));
        drawPanel.clear();
        drawPanel.requestFocus();
        refreshThread = new Thread(this);
        refreshThread.start();
    }

    private void initComponents() {
        colourManager = ColourManager.getColourManager();

        mainLayout = new SpringLayout();
        mainPanel = new JPanel();
        mainPanel.setLayout(mainLayout);

        drawPanel = new DrawPanel();
        toRefresh.add(drawPanel);
        colourManager.addColourObject(drawPanel);
        mainPanel.add(drawPanel);

        colourPanel = new JPanel();
        colourPanel.setMaximumSize(new Dimension(200, 300));
        colourPanel.setLayout(new BoxLayout(colourPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(colourPanel);

        colourSliders = new ColourSliders();
        colourManager.addColourReading(colourSliders);
        colourPanel.add(colourSliders);

        colourPreview = new ColourPreview();
        toRefresh.add(colourPreview);
        colourManager.addColourSwitcher(colourPreview);
        colourPanel.add(colourPreview);

        saveFilePanel = new SaveFilePanel();
        mainPanel.add(saveFilePanel);

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

        // Constraints for saveFilePanel
        mainLayout.putConstraint(SpringLayout.WEST, saveFilePanel, 30, SpringLayout.EAST, drawPanel);
        mainLayout.putConstraint(SpringLayout.NORTH, saveFilePanel, 30, SpringLayout.SOUTH, colourPanel);
        
        add(mainPanel);
    }

    public void run() {
        try {
            while(true) {
                refreshAll();
                Thread.sleep(15);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshAll() {
        for (Refreshable r : toRefresh) {
            r.refresh();
        }
    }
}
