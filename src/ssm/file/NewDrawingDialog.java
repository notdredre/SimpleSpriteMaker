package ssm.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class NewDrawingDialog extends JDialog implements ActionListener {
    private final int WIDTH = 300, HEIGHT = 200;
    private JLabel createLabel, widthLabel, heightLabel;
    private JPanel dimensionPanel, buttonPanel;
    private SpinnerNumberModel heightModel, dimensionYModel;
    private JSpinner widthSpinner, heightSpinner;
    private JButton okButton, cancelButton;
    private ImageFileManager imageFileManager;

    public NewDrawingDialog() {
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setAutoRequestFocus(true);
        setAlwaysOnTop(true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        createLabel = new JLabel("Create New Drawing");
        createLabel.setAlignmentX(CENTER_ALIGNMENT);
        
        dimensionPanel = new JPanel();
        dimensionPanel.setSize(300, 100);
        widthLabel = new JLabel("Width: ");
        heightLabel = new JLabel("Height: ");
        heightModel = new SpinnerNumberModel(25, 1, 100, 1);
        dimensionYModel = new SpinnerNumberModel(25, 1, 100, 1);
        widthSpinner = new JSpinner(heightModel);
        heightSpinner = new JSpinner(dimensionYModel);
        dimensionPanel.add(widthLabel);
        dimensionPanel.add(widthSpinner);
        dimensionPanel.add(heightLabel);
        dimensionPanel.add(heightSpinner);
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
            imageFileManager.newDrawing(heightModel.getNumber(), dimensionYModel.getNumber());
            dispose();
        }
        if (command.equals("Cancel")) {
            dispose();
        }
    }
}
