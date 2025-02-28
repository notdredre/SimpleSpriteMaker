package ssm.tools;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ToolPanel extends JPanel implements ActionListener {
    private ToolManager toolManager;
    private ArrayList<JButton> buttons;
    private JPanel buttonPanel;
    private ToolControls toolControls;
    private GridLayout layout;

    public ToolPanel() {
        toolManager = ToolManager.getToolManager();
        buttons = new ArrayList<>();
        toolControls = new ToolControls();
        layout = new GridLayout(5, 10, 5, 5);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        initButtons();
        add(buttonPanel);
        add(toolControls);
    }

    private void initButtons() {
        buttonPanel = new JPanel(layout);
        for (int i = 0; i < 3; i++) {
            JButton newButton = new JButton();
            newButton.addActionListener(this);
            newButton.setSize(50, 50);
                
            buttons.add(newButton);
            buttonPanel.add(newButton);

        }

        buttons.get(0).setText("SquareBrush");
        buttons.get(1).setText("SquareEraser");
        buttons.get(2).setText("Fill");
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command == "SquareBrush") {
            toolManager.getSquareBrush();
        }
        if (command == "SquareEraser") {
            toolManager.getSquareEraser();
        }
        if (command == "Fill") {
            toolManager.getFill();
        }
        toolControls.updateToolControls();
    }


}
