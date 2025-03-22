package ssm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import javax.swing.JFileChooser;
import ssm.draw.DrawPanel;
import ssm.draw.Project;
import ssm.file.ImageFileManager;
import ssm.file.NewProjectDialog;
import ssm.file.SaveChooser;

public class MenuHandler implements ActionListener {
    private Project project;
    private String targetExtension;
    private String currentTarget;
    private SaveChooser saveChooser;
    private NewProjectDialog newDialog;
    private MainWindow mainWindow;
    private DrawPanel drawPanel;
    private ImageFileManager imageFileManager;

    public MenuHandler(MainWindow mainWindow, DrawPanel drawPanel) {
        project = Project.getProject();
        targetExtension = currentTarget = null;
        saveChooser = null;
        newDialog = new NewProjectDialog();
        this.mainWindow = mainWindow;
        this.drawPanel = drawPanel;
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
        currentTarget = project.getName();
        if (e.getActionCommand().equals("Save")) {
            if (currentTarget == null) {
                if (saveChooser == null)
                    saveChooser = new SaveChooser(currentTarget);
                int result = saveChooser.showSaveDialog(mainWindow);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        currentTarget = saveChooser.getTargetPath();
                        project.setName(currentTarget);
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
                saveChooser = new SaveChooser(currentTarget);
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
