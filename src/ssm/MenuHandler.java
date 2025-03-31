package ssm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import javax.swing.JFileChooser;
import ssm.draw.DrawPanel;
import ssm.draw.Project;
import ssm.file.ImageFileManager;
import ssm.file.NewProjectDialog;
import ssm.file.OpenChooser;
import ssm.file.SaveChooser;

public class MenuHandler implements ActionListener {
    private Project project;
    private String targetExtension;
    private String currentTarget;
    private SaveChooser saveChooser;
    private OpenChooser openChooser;
    private NewProjectDialog newDialog;
    private MainWindow mainWindow;
    private DrawPanel drawPanel;
    private ImageFileManager imageFileManager;

    public MenuHandler(MainWindow mainWindow, DrawPanel drawPanel) {
        project = Project.getProject();
        targetExtension = currentTarget = null;
        saveChooser = null;
        this.mainWindow = mainWindow;
        newDialog = new NewProjectDialog(mainWindow);
        this.drawPanel = drawPanel;
        imageFileManager = ImageFileManager.getImageFileManager();
    }
    
    public void actionPerformed(ActionEvent e) {
        project = Project.getProject();
        currentTarget = project.getName();
        switch (e.getActionCommand()) {
            case "New Project":
                if (!newDialog.isVisible()) {
                    newDialog.setLocationRelativeTo(null);
                    newDialog.setVisible(true);
                } else {
                    newDialog.toFront();
                }
                break;
            case "Save":
                if (currentTarget.equals("Untitled")) {
                    if (saveChooser == null)
                        saveChooser = new SaveChooser(currentTarget);
                    saveChooser.setTarget(currentTarget);
                    int result = saveChooser.showSaveDialog(mainWindow);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        try {
                            currentTarget = saveChooser.getTargetPath();
                            project.setName(currentTarget);
                            targetExtension = saveChooser.getTargetExtension();
                            imageFileManager.setTarget(currentTarget, targetExtension);
                            project.saveProject();
                            mainWindow.setTitle(project.getName() + "." + targetExtension + " - SimpleSpriteMaker v0.6");
                        } catch (FileNotFoundException f) {
                            f.printStackTrace();
                        }
                    }
                    return;
                }
                project.saveProject();
                break;
        case "Save As":
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
            break;
        case "Undo":
            drawPanel.undo();
            break;
        case "Open":
            openChooser = new OpenChooser(mainWindow);
        }
    }
}
