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

public class NewProjectDialog extends JDialog implements ActionListener, ChangeListener {
    private final int WIDTH = 300, HEIGHT = 200;
    private JLabel createLabel, widthLabel, heightLabel;
    private JPanel dimensionPanel, buttonPanel;
    private SpinnerNumberModel widthModel, heightModel;
    private JSpinner widthSpinner, heightSpinner;
    private JButton okButton, cancelButton;
    private JCheckBox lockRatio;
    private ImageFileManager imageFileManager;
    private float aspectRatio;

    public NewProjectDialog() {
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setAutoRequestFocus(true);
        setAlwaysOnTop(true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
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

        buttonPanel = new JPanel();
        buttonPanel.setSize(300, 100);
        okButton = new JButton("Ok");
        cancelButton = new JButton("Cancel");
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel);
        imageFileManager = ImageFileManager.getImageFileManager();
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Ok")) {
            imageFileManager.newDrawing(widthModel.getNumber(), heightModel.getNumber());
            dispose();
        }
        if (command.equals("Cancel")) {
            dispose();
        }
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(lockRatio) && lockRatio.isSelected())
            aspectRatio = widthModel.getNumber().floatValue() / heightModel.getNumber().floatValue();

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
