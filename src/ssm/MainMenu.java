package ssm;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionListener;

public class MainMenu extends MenuBar{
    private Menu file, edit, view, help;
    private MenuItem newProject, saveDrawing, saveAs, undo, open, fullPreview;

    public MainMenu() {
        file = new Menu("File");
        edit = new Menu("Edit");
        help = new Menu("Help");
        view = new Menu("View");
        newProject = new MenuItem("New Project");
        saveDrawing = new MenuItem("Save");
        saveAs = new MenuItem("Save As");
        undo = new MenuItem("Undo");
        open = new MenuItem("Open");
        fullPreview = new MenuItem("View full preview");
        file.add(newProject);
        file.add(saveDrawing);
        file.add(saveAs);
        file.add(open);
        edit.add(undo);
        view.add(fullPreview);
        add(file);
        add(edit);
        add(view);
        add(help);
    }

    public void addActionListener(ActionListener listener) {
        file.addActionListener(listener);
        edit.addActionListener(listener);
        view.addActionListener(listener);
        help.addActionListener(listener);
    }
}
