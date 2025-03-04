package ssm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;

import ssm.draw.DrawPanel;
import ssm.draw.Project;
import ssm.file.ImageFileManager;
import ssm.file.NewDrawingDialog;
import ssm.file.SaveChooser;

public class ProjectManager implements ActionListener {
    private Project project;
    private String targetExtension;
    private String currentTarget;
    private SaveChooser saveChooser;
    private NewDrawingDialog newDialog;
    private MainWindow mainWindow;
    private DrawPanel drawPanel;
    private ImageFileManager imageFileManager;

    public ProjectManager(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        drawPanel = mainWindow.getDrawPanel();
        saveChooser = null;
        newDialog = new NewDrawingDialog();
        imageFileManager = ImageFileManager.getImageFileManager();
    }

    public void actionPerformed(ActionEvent e) {
        project = Project.getProject();
        if (e.getActionCommand().equals("New Project")) {
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
                int result = saveChooser.showSaveDialog(mainWindow);
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
            int result = saveChooser.showSaveDialog(mainWindow);
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
        if (e.getActionCommand().equals("Undo")) {
            drawPanel.undo();
        }
    }
}
