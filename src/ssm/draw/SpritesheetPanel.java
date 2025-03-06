package ssm.draw;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ssm.ProjectManager;

public class SpritesheetPanel extends JPanel implements ActionListener, ChangeListener {
    private JButton left, right, up, down;
    private JPanel leftRightPanel, currentCellPanel;
    private JLabel currentCell;
    private SpinnerNumberModel rowModel, colModel;
    private JSpinner rowSpinner, colSpinner;
    private ProjectManager projectManager;
    private JCheckBox previewLast;

    public SpritesheetPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        projectManager = ProjectManager.getProjectManager(null);
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
        rowModel = new SpinnerNumberModel(projectManager.getCurrentRow() + 1, 1, projectManager.getNumRows(), 1);
        colModel = new SpinnerNumberModel(projectManager.getCurrentCol() + 1, 1, projectManager.getNumCols(), 1);
        rowSpinner = new JSpinner(rowModel);
        rowSpinner.addChangeListener(this);
        colSpinner = new JSpinner(colModel);
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
                projectManager.moveLeft();
                break;
            case "Right":
                projectManager.moveRight();
                break;
            case "Down":
                projectManager.moveDown();
                break;
            case "Up":
                projectManager.moveUp();
                break;
        }
        rowSpinner.setValue(projectManager.getCurrentRow() + 1);
        colSpinner.setValue(projectManager.getCurrentCol() + 1);
    }

    public void stateChanged(ChangeEvent e) {
        int row = rowModel.getNumber().intValue() - 1;
        int col = colModel.getNumber().intValue() - 1;
        if (rowSpinner.isFocusOwner() || colSpinner.isFocusOwner())
            projectManager.setCell(row, col);

        if (previewLast.isSelected()) {
            projectManager.setPreview(true);
        } else
            projectManager.setPreview(false);
    }
}
