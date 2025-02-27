package ssm.tools;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ToolPanel extends JPanel implements ActionListener {
    private ToolManager toolManager;
    private ArrayList<JButton> buttons;
    private GridLayout layout;

    public ToolPanel() {
        toolManager = ToolManager.getToolManager();
        buttons = new ArrayList<>();
        layout = new GridLayout(5, 10, 5, 5);
        setLayout(layout);
        initButtons();
    }

    private void initButtons() {
        for (int i = 0; i < 2; i++) {
            JButton newButton = new JButton();
            newButton.addActionListener(this);
            newButton.setSize(50, 50);
                
            buttons.add(newButton);
            add(newButton);

        }

        buttons.get(0).setText("SquareBrush");
        buttons.get(1).setText("SquareEraser");
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command == "SquareBrush") {
            toolManager.getSquareBrush();
        }
        if (command == "SquareEraser") {
            toolManager.getSquareEraser();
        }
    }


}
