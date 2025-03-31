package ssm.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ssm.MainWindow;
import ssm.draw.Project;

public class OpenChooser extends JFrame implements ActionListener, ChangeListener {
    private final int WIDTH = 300, HEIGHT = 200;
    private JLabel fileLabel, rowLabel, colLabel;
    private JCheckBox spriteSheet;
    private JSpinner rowSpinner, colSpinner;
    private SpinnerNumberModel row, col;
    private JButton openDialog, confirm;
    private JPanel spinnerPanel;
    private JFileChooser openChooser;
    private Project project;
    private MainWindow mainWindow;
    private String selected;

    public OpenChooser(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        selected = null;
        setSize(WIDTH, HEIGHT);
        setTitle("Open file");
        setResizable(false);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setVisible(true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        row = new SpinnerNumberModel(5, 1, 100, 1);
        col = new SpinnerNumberModel(5, 1, 100, 1);
        rowSpinner = new JSpinner(row);
        colSpinner = new JSpinner(col);
        openDialog = new JButton("Choose File");
        openDialog.addActionListener(this);
        openChooser = new JFileChooser();
        spinnerPanel = new JPanel();
        rowLabel = new JLabel("Rows:");
        colLabel = new JLabel("Columns:");
        spinnerPanel.add(rowLabel);
        spinnerPanel.add(rowSpinner);
        spinnerPanel.add(colLabel);
        spinnerPanel.add(colSpinner);
        spinnerPanel.setVisible(false);
        spinnerPanel.setAlignmentX(CENTER_ALIGNMENT);
        fileLabel = new JLabel("No file selected");
        fileLabel.setAlignmentX(CENTER_ALIGNMENT);
        openDialog.setAlignmentX(CENTER_ALIGNMENT);
        spriteSheet = new JCheckBox("Spritesheet?");
        spriteSheet.setAlignmentX(CENTER_ALIGNMENT);
        spriteSheet.addChangeListener(this);
        add(fileLabel);
        add(openDialog);
        add(spriteSheet);
        add(spinnerPanel);
        confirm = new JButton("Ok");
        confirm.setAlignmentX(CENTER_ALIGNMENT);
        confirm.addActionListener(this);
        add(confirm);
        project = Project.getProject();
    }

    public void stateChanged(ChangeEvent e) {
        if (spriteSheet.isSelected()) {
            spinnerPanel.setVisible(true);
        } else {
            spinnerPanel.setVisible(false);
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Choose File":
                int result = openChooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selected = openChooser.getSelectedFile().getAbsolutePath();
                    fileLabel.setText(selected);
                }
                break;
            case "Ok":
                if (selected == null)
                    return;
                int numRows = row.getNumber().intValue();
                int numCols = col.getNumber().intValue();
                if (spriteSheet.isSelected() && (numCols > 1 || numRows > 1))
                    project.openProject(selected, numRows, numCols);
                else
                    project.openProject(selected);
                mainWindow.setTitle(project.getName() + " - SimpleSpriteMaker v0.6");
                dispose();
                break;
            default:
                break;
        }
    }
}
