package ssm.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import ssm.draw.Project;

public class OpenChooser extends JFrame implements ActionListener {
    private final int WIDTH = 300, HEIGHT = 200;
    private JSpinner rowSpinner, colSpinner;
    private SpinnerNumberModel row, col;
    private JButton openDialog;
    private JPanel spinnerPanel;
    private JFileChooser openChooser;
    private Project project;

    public OpenChooser() {
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setVisible(true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        row = new SpinnerNumberModel(5, 1, 100, 1);
        col = new SpinnerNumberModel(5, 1, 100, 1);
        rowSpinner = new JSpinner(row);
        colSpinner = new JSpinner(col);
        openDialog = new JButton("Choose File");
        openDialog.addActionListener(this);
        openChooser = new JFileChooser();
        spinnerPanel = new JPanel();
        spinnerPanel.add(rowSpinner);
        spinnerPanel.add(colSpinner);
        add(spinnerPanel);
        add(openDialog);
        project = Project.getProject();
    }

    public void actionPerformed(ActionEvent e) {
        int result = openChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            project.openProject(openChooser.getSelectedFile().getAbsolutePath(), row.getNumber().intValue(), col.getNumber().intValue());
            dispose();
        }
    }
}
