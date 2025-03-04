package ssm.file;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import ssm.draw.Project;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class SaveFilePanel extends JPanel implements ActionListener {
    private String currentTarget;
    private SaveChooser saveChooser;
    private JButton saveButton, saveAsButton, newButton;
    private NewProjectDialog newDialog;
    private ImageFileManager imageFileManager;
    private Project project;
    private String targetExtension;

    public SaveFilePanel() {
        setPreferredSize(new Dimension(200, 100));
        imageFileManager = ImageFileManager.getImageFileManager();
        currentTarget = null;
        newDialog = new NewProjectDialog();
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
        project = Project.getProject();
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
                    try {
                        currentTarget = saveChooser.getTargetPath();
                        targetExtension = saveChooser.getTargetExtension();
                        imageFileManager.setTarget(currentTarget, targetExtension);
                        project.saveProject();
                    } catch (FileNotFoundException f) {
                        f.printStackTrace();
                    }   
                }
                return;
            }
            project.saveProject();
        }
        if (e.getActionCommand().equals("Save As")) {
            if (saveChooser == null)
                saveChooser = new SaveChooser();
            int result = saveChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String target = saveChooser.getTargetPath();
                    targetExtension = saveChooser.getTargetExtension();
                    imageFileManager.setTarget(target, targetExtension);
                    project.saveProject();
                } catch (FileNotFoundException f) {
                    f.printStackTrace();
                }
            }
        }
    }
}
