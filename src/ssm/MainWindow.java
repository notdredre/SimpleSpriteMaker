package ssm;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import ssm.colour.ColourManager;
import ssm.colour.ColourPreview;
import ssm.colour.ColourSliders;
import ssm.draw.DrawPanel;
import ssm.file.ImageFileManager;
import ssm.tools.ToolPanel;

public class MainWindow extends JFrame implements Runnable {
    private final int WIDTH = 1000, HEIGHT = 800;
    private JPanel mainPanel, colourPanel;
    private JButton clearButton;
    private SpringLayout mainLayout;
    private DrawPanel drawPanel;
    private ColourSliders colourSliders;
    private ColourPreview colourPreview;
    private ColourManager colourManager;
    private ImageFileManager imageFileManager;
    private ToolPanel toolPanel;
    private MainMenu mainMenu;
    private ProjectManager projectManager;
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
        setBackground(new Color(240, 240, 240));
        colourManager.updateColourObjects();
        refreshThread = new Thread(this);
        refreshThread.start();
        drawPanel.postGraphicsInit();
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                drawPanel.render();
            }
        });
        addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                drawPanel.render();
            }
        });
    }

    private void initComponents() {
        colourManager = ColourManager.getColourManager();
        imageFileManager = ImageFileManager.getImageFileManager();
        mainLayout = new SpringLayout();
        mainPanel = new JPanel();
        mainPanel.setLayout(mainLayout);

        drawPanel = new DrawPanel();
        toRefresh.add(drawPanel);
        colourManager.addColourObject(drawPanel);
        mainPanel.add(drawPanel);
        imageFileManager.setDrawPanel(drawPanel);

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

        toolPanel = new ToolPanel();
        mainPanel.add(toolPanel);
        
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawPanel.clear();
            }
        });
        
        mainPanel.add(clearButton);


        // Constraints for drawPanel
        mainLayout.putConstraint(SpringLayout.WEST, drawPanel, 20, SpringLayout.WEST, mainPanel);
        mainLayout.putConstraint(SpringLayout.NORTH, drawPanel, 20, SpringLayout.NORTH, mainPanel);

        // Constraints for colourPanel
        mainLayout.putConstraint(SpringLayout.WEST, colourPanel, 30, SpringLayout.EAST, drawPanel);
        mainLayout.putConstraint(SpringLayout.EAST, mainPanel, 20, SpringLayout.EAST, colourPanel);
        mainLayout.putConstraint(SpringLayout.NORTH, colourPanel, 20, SpringLayout.NORTH, mainPanel);
        mainLayout.putConstraint(SpringLayout.SOUTH, colourPanel, 300, SpringLayout.NORTH, colourPanel);

        // Constraints for toolPanel
        mainLayout.putConstraint(SpringLayout.WEST, toolPanel, 30, SpringLayout.EAST, drawPanel);
        mainLayout.putConstraint(SpringLayout.NORTH, toolPanel, 30, SpringLayout.SOUTH, colourPanel);
        mainLayout.putConstraint(SpringLayout.EAST, toolPanel, 200, SpringLayout.WEST, toolPanel);
        mainLayout.putConstraint(SpringLayout.SOUTH, toolPanel, 200, SpringLayout.NORTH, toolPanel);

        // Constraints for clearButton
        mainLayout.putConstraint(SpringLayout.NORTH, clearButton, 10, SpringLayout.SOUTH, drawPanel);
        mainLayout.putConstraint(SpringLayout.EAST, clearButton, 0, SpringLayout.EAST, drawPanel);
        mainLayout.putConstraint(SpringLayout.SOUTH, mainPanel, 20, SpringLayout.SOUTH, clearButton);
        
        add(mainPanel);

        mainMenu = new MainMenu();
        projectManager = new ProjectManager(this);
        mainMenu.addActionListener(projectManager);
        setMenuBar(mainMenu);
    }

    public void run() {
        try {
            while(true) {
                //long pre = System.currentTimeMillis();
                refreshAll();
                Thread.sleep(10);
                /* long post = System.currentTimeMillis();
                long diff = post - pre;
                float fps = 1000 / Math.clamp(diff, 1, 1000000);
                System.out.println("FRAMETIME: " + diff + " FPS: " + fps); */
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

    public DrawPanel getDrawPanel() {
        return drawPanel;
    }
}
