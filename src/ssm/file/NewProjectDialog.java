package ssm.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import ssm.draw.Project;

public class NewProjectDialog extends JDialog implements ActionListener, ChangeListener {
    private final int WIDTH = 300, HEIGHT = 200;
    private JLabel createLabel, widthLabel, heightLabel, rowLabel, columnLabel;
    private JPanel dimensionPanel, spritePanel, buttonPanel;
    private SpinnerNumberModel widthModel, heightModel, rowModel, columnModel;
    private JSpinner widthSpinner, heightSpinner, rowSpinner, columnSpinner;
    private JButton okButton, cancelButton;
    private JCheckBox lockRatio, spriteSheet;
    private float aspectRatio;
    private Project project;

    public NewProjectDialog() {
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setAutoRequestFocus(true);
        setAlwaysOnTop(true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        project = Project.getProject();
        createLabel = new JLabel("Create New Project");
        createLabel.setAlignmentX(CENTER_ALIGNMENT);
        dimensionPanel = new JPanel();
        dimensionPanel.setSize(300, 100);
        widthLabel = new JLabel("Width: ");
        heightLabel = new JLabel("Height: ");
        lockRatio = new JCheckBox("Lock Aspect Ratio");
        lockRatio.addChangeListener(this);
        widthModel = new SpinnerNumberModel(25, 1, 100, 1);
        heightModel = new SpinnerNumberModel(25, 1, 100, 1);
        widthSpinner = new JSpinner(widthModel);
        heightSpinner = new JSpinner(heightModel);
        widthSpinner.addChangeListener(this);
        heightSpinner.addChangeListener(this);
        dimensionPanel.add(widthLabel);
        dimensionPanel.add(widthSpinner);
        dimensionPanel.add(heightLabel);
        dimensionPanel.add(heightSpinner);
        dimensionPanel.add(lockRatio);
        add(Box.createVerticalStrut(10));
        add(createLabel);
        add(Box.createVerticalStrut(5));
        add(dimensionPanel);

        spriteSheet = new JCheckBox("Spritesheet?");
        spriteSheet.setAlignmentX(CENTER_ALIGNMENT);
        spriteSheet.addChangeListener(this);
        add(spriteSheet);

        rowModel = new SpinnerNumberModel(5, 1, 30, 1);
        columnModel = new SpinnerNumberModel(5, 1, 30, 1);
        rowSpinner = new JSpinner(rowModel);
        columnSpinner = new JSpinner(columnModel);
        rowLabel = new JLabel("Rows: ");
        columnLabel = new JLabel("Columns: ");
        spritePanel = new JPanel();
        spritePanel.add(rowLabel);
        spritePanel.add(rowSpinner);
        spritePanel.add(columnLabel);
        spritePanel.add(columnSpinner);
        spritePanel.setVisible(false);
        add(spritePanel);

        buttonPanel = new JPanel();
        buttonPanel.setSize(300, 100);
        okButton = new JButton("Ok");
        cancelButton = new JButton("Cancel");
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Ok")) {
            int drawingWidth = widthModel.getNumber().intValue();
            int drawingHeight = heightModel.getNumber().intValue();
            if (spriteSheet.isSelected()) {
                int numRows = rowModel.getNumber().intValue();
                int numCols = columnModel.getNumber().intValue();
                project.newProject(numRows, numCols, drawingWidth, drawingHeight, 20);
            }
            else
                project.newProject(drawingWidth, drawingHeight, 20);
            dispose();
        }
        if (command.equals("Cancel")) {
            dispose();
        }
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(lockRatio) && lockRatio.isSelected())
            aspectRatio = widthModel.getNumber().floatValue() / heightModel.getNumber().floatValue();

        if (e.getSource().equals(spriteSheet)) {
            if (spriteSheet.isSelected()) {
                setSize(WIDTH, HEIGHT + 100);
                spriteSheet.setText("Spritesheet!");
                spritePanel.setVisible(true);
            } else {
                setSize(WIDTH, HEIGHT);
                spriteSheet.setText("Spritesheet?");
                spritePanel.setVisible(false);
            } 
        }

        if (!lockRatio.isSelected())
            return;

        if (e.getSource().equals(widthSpinner)) {
            heightModel.setValue(Math.clamp(Math.round(widthModel.getNumber().floatValue() / aspectRatio), 0, 100));
            widthModel.setValue(Math.clamp(Math.round(heightModel.getNumber().floatValue() * aspectRatio), 0, 100));
        }
            

        if (e.getSource().equals(heightSpinner)) {
            widthModel.setValue(Math.clamp(Math.round(heightModel.getNumber().floatValue() * aspectRatio), 0, 100));
            heightModel.setValue(Math.clamp(Math.round(widthModel.getNumber().floatValue() / aspectRatio), 0, 100));
        }
            
    }
}
