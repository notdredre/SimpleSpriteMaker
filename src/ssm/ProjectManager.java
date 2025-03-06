package ssm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;

import ssm.draw.DrawPanel;
import ssm.draw.Project;
import ssm.draw.SpritesheetPanel;
import ssm.file.ImageFileManager;
import ssm.file.NewProjectDialog;
import ssm.file.SaveChooser;

public class ProjectManager implements ActionListener {
    private static ProjectManager projectManager = null;
    private Project project;
    private String targetExtension;
    private String currentTarget;
    private SaveChooser saveChooser;
    private NewProjectDialog newDialog;
    private MainWindow mainWindow;
    private DrawPanel drawPanel;
    private SpritesheetPanel spritesheetPanel;
    private ImageFileManager imageFileManager;

    private ProjectManager(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        drawPanel = mainWindow.getDrawPanel();
        saveChooser = null;
        newDialog = new NewProjectDialog(this);
        imageFileManager = ImageFileManager.getImageFileManager();
    }

    public static ProjectManager getProjectManager(MainWindow mainWindow) {
        if (projectManager == null)
            projectManager = new ProjectManager(mainWindow);
        return projectManager;
    }

    public static ProjectManager getProjectManager() {
        return projectManager;
    }

    public void createNewProject(int numRow, int numCols, int drawingWidth, int drawingHeight) {
        project = Project.newProject(numRow, numCols, drawingWidth, drawingHeight, 20);
        if (spritesheetPanel != null) {
            if (project.getNumCols() > 1 || project.getNumRows() > 1)
                spritesheetPanel.setVisible(true);
            else
                spritesheetPanel.setVisible(false);
        }
        drawPanel.init(drawingWidth, drawingHeight);
    }

    public void createNewProject(int drawingWidth, int drawingHeight) {
        createNewProject(1, 1, drawingWidth, drawingHeight);
    }

    public void setSpritesheetPanel(SpritesheetPanel spritesheetPanel) {
        this.spritesheetPanel = spritesheetPanel;
        if (project.getNumCols() > 1 || project.getNumRows() > 1)
            spritesheetPanel.setVisible(true);
        else
            spritesheetPanel.setVisible(false);
    }

    public void setPreview(boolean preview) {
        project.setPreview(preview);
    }

    public int getCurrentCol() {
        return project.getCurrentCol();
    }

    public int getCurrentRow() {
        return project.getCurrentRow();
    }

    public int getNumRows() {
        return project.getNumRows();
    }

    public int getNumCols() {
        return project.getNumCols();
    }

    public void setCell(int row, int col) {
        project.setCell(row, col);
    }

    public void moveLeft() {
        project.moveLeft();
        drawPanel.updateBuffers();
    }

    public void moveRight() {
        project.moveRight();
        drawPanel.updateBuffers();
    }

    public void moveUp() {
        project.moveUp();
        drawPanel.updateBuffers();
    }

    public void moveDown() {
        project.moveDown();
        drawPanel.updateBuffers();
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
