package ssm.draw;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import ssm.ProjectListener;

public class SpritesheetPanel extends JPanel implements ActionListener, ChangeListener, ProjectListener {
    private JButton left, right, up, down;
    private JPanel leftRightPanel, currentCellPanel;
    private JLabel currentCell;
    private SpinnerNumberModel rowModel, colModel;
    private JSpinner rowSpinner, colSpinner;
    private Project project;
    private JCheckBox previewLast;

    public SpritesheetPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        project = Project.getProject();
        left = new JButton("Left");
        right = new JButton("Right");
        up = new JButton("Up");
        down = new JButton("Down");
        left.addActionListener(this);
        right.addActionListener(this);
        up.addActionListener(this);
        down.addActionListener(this);

        up.setAlignmentX(CENTER_ALIGNMENT);
        down.setAlignmentX(CENTER_ALIGNMENT);
        leftRightPanel = new JPanel();
        leftRightPanel.setMaximumSize(new Dimension(200, 35));
        add(up);
        leftRightPanel.add(left);
        leftRightPanel.add(right);
        add(leftRightPanel);
        add(down);

        currentCellPanel = new JPanel();
        currentCellPanel.setMaximumSize(new Dimension(200, 30));
        currentCell = new JLabel("Current Cell: ");
        rowSpinner = new JSpinner();
        rowSpinner.addChangeListener(this);
        colSpinner = new JSpinner();
        colSpinner.addChangeListener(this);
        currentCellPanel.add(currentCell);
        currentCellPanel.add(rowSpinner);
        currentCellPanel.add(colSpinner);
        add(currentCellPanel);

        previewLast = new JCheckBox("Preview Last Cell");
        previewLast.addChangeListener(this);
        previewLast.setAlignmentX(CENTER_ALIGNMENT);
        add(previewLast);
    }

    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "Left":
                project.moveLeft();
                break;
            case "Right":
                project.moveRight();
                break;
            case "Down":
                project.moveDown();
                break;
            case "Up":
                project.moveUp();
                break;
        }
    }

    public void onNewProject(int numRows, int numCols, int drawingWidth, int drawingHeight) {
        if (numRows > 1 || numCols > 1) {
            setVisible(true);
        } else {
            setVisible(false);
        }
        rowModel = new SpinnerNumberModel(1, 1, numRows, 1);
        colModel = new SpinnerNumberModel(1, 1, numCols, 1);
        rowSpinner.setModel(rowModel);
        colSpinner.setModel(colModel);
    }

    public void onCellChanged(int currentRow, int currentCol) {
        rowSpinner.setValue(currentRow + 1);
        colSpinner.setValue(currentCol + 1);
    }

    public void onBuffersChanged(BufferedImage drawBuffer, BufferedImage writeBuffer, BufferedImage previewBuffer) {
        
    }

    public void stateChanged(ChangeEvent e) {
        int row = rowModel.getNumber().intValue() - 1;
        int col = colModel.getNumber().intValue() - 1;
        if (rowSpinner.isFocusOwner() || colSpinner.isFocusOwner())
            project.setCell(row, col);

        if (previewLast.isSelected()) {
            project.setPreview(true);
        } else
            project.setPreview(false);
    }
}
