package ssm;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionListener;

public class MainMenu extends MenuBar{
    private Menu file, edit, help;
    private MenuItem newProject, saveDrawing, saveAs, undo;

    public MainMenu() {
        file = new Menu("File");
        edit = new Menu("Edit");
        help = new Menu("Help");
        newProject = new MenuItem("New Project");
        saveDrawing = new MenuItem("Save");
        saveAs = new MenuItem("Save As");
        undo = new MenuItem("Undo");
        file.add(newProject);
        file.add(saveDrawing);
        file.add(saveAs);
        edit.add(undo);
        add(file);
        add(edit);
        add(help);
    }

    public void addActionListener(ActionListener listener) {
        file.addActionListener(listener);
        edit.addActionListener(listener);
        help.addActionListener(listener);
    }
}
