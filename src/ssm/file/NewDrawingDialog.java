package ssm.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class NewDrawingDialog extends JDialog implements ActionListener {
    private final int WIDTH = 300, HEIGHT = 200;
    private JPanel dimensionPanel, buttonPanel;
    private SpinnerNumberModel dimensionXModel, dimensionYModel;
    private JSpinner dimensionX, dimensionY;
    private JButton okButton, cancelButton;
    private ImageFileManager imageFileManager;

    public NewDrawingDialog() {
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setAutoRequestFocus(true);
        setAlwaysOnTop(true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        dimensionPanel = new JPanel();
        dimensionPanel.setSize(300, 100);
        dimensionXModel = new SpinnerNumberModel(25, 1, 100, 1);
        dimensionYModel = new SpinnerNumberModel(25, 1, 100, 1);
        dimensionX = new JSpinner(dimensionXModel);
        dimensionY = new JSpinner(dimensionYModel);
        dimensionPanel.add(dimensionX);
        dimensionPanel.add(dimensionY);
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
            imageFileManager.newDrawing(dimensionXModel.getNumber(), dimensionYModel.getNumber());
        }
        if (command.equals("Cancel")) {
            dispose();
        }
    }
}
