package ssm.draw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class SpritesheetPanel extends JPanel implements ActionListener{
    private JButton left, right, up, down;
    private DrawPanel drawPanel;

    public SpritesheetPanel(DrawPanel drawPanel) {
        this.drawPanel = drawPanel;
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
                drawPanel.moveLeft();
                break;
            case "Right":
                drawPanel.moveRight();
                break;
            case "Down":
                drawPanel.moveDown();
                break;
            case "Up":
                drawPanel.moveUp();
                break;
        }
    }
}
