package ssm.file;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class SaveFilePanel extends JPanel implements ActionListener {
    private String currentTarget;
    private SaveChooser saveChooser;
    private JButton saveButton, saveAsButton, newButton;
    private NewDrawingDialog newDialog;
    private ImageFileManager imageFileManager;
    private String targetExtension;

    public SaveFilePanel() {
        setPreferredSize(new Dimension(200, 100));
        imageFileManager = ImageFileManager.getImageFileManager();
        currentTarget = null;
        newDialog = new NewDrawingDialog();
        newButton = new JButton("New Drawing");
        saveButton = new JButton("Save");
        saveAsButton = new JButton("Save As");
        add(newButton);
        add(saveButton);
        add(saveAsButton);
        newButton.addActionListener(this);
        saveButton.addActionListener(this);
        saveAsButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("New Drawing")) {
            if (!newDialog.isVisible()) {
                newDialog.setLocationRelativeTo(null);
                newDialog.setVisible(true);
            } else {
                newDialog.toFront();
            }

        }
        if (e.getActionCommand().equals("Save")) {
            if (currentTarget == null) {
                if (saveChooser == null)
                    saveChooser = new SaveChooser();
                int result = saveChooser.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    targetExtension = saveChooser.getTargetExtension();
                    try {
                        currentTarget = saveChooser.getTargetPath();
                        imageFileManager.saveImage(currentTarget, targetExtension);
                    } catch (FileNotFoundException f) {
                        f.printStackTrace();
                    }
                }
                return;
            }
        }
        if (e.getActionCommand().equals("Save As")) {
            if (saveChooser == null)
                saveChooser = new SaveChooser();
            int result = saveChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                targetExtension = saveChooser.getTargetExtension();
                try {
                    String target = saveChooser.getTargetPath();
                    imageFileManager.saveImage(target, targetExtension);
                } catch (FileNotFoundException f) {
                    f.printStackTrace();
                }
            }
        }
    }

}
