package ssm.draw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import ssm.ProjectManager;

public class SpritesheetPanel extends JPanel implements ActionListener{
    private JButton left, right, up, down;
    ProjectManager projectManager;

    public SpritesheetPanel() {
        projectManager = ProjectManager.getProjectManager(null);
        left = new JButton("Left");
        right = new JButton("Right");
        up = new JButton("Up");
        down = new JButton("Down");
        left.addActionListener(this);
        right.addActionListener(this);
        up.addActionListener(this);
        down.addActionListener(this);
        add(left);
        add(right);
        add(up);
        add(down);
    }

    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "Left":
                projectManager.moveLeft();
                break;
            case "Right":
                projectManager.moveRight();
                break;
            case "Down":
                projectManager.moveDown();
                break;
            case "Up":
                projectManager.moveUp();
                break;
        }
    }
}
