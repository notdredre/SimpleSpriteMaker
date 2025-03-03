package ssm.tools;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ssm.tools.ToolManager.ToolType;

public class ToolControls extends JPanel implements ChangeListener, ToolListener {
    private CardLayout layout;
    private ToolManager toolManager;
    private JPanel drawToolPanel;
    private JPanel fillToolPanel;
    private JPanel noControlPanel;
    private JLabel sizeLabel;
    private JSpinner sizeField;
    private SpinnerNumberModel sizeModel;


    public ToolControls() {
        setPreferredSize(new Dimension(200, 100));
        toolManager = ToolManager.getToolManager();
        layout = new CardLayout();
        fillToolPanel = noControlPanel = new JPanel();
        createDrawToolPanel();
        setLayout(layout);
        add(drawToolPanel, ToolType.DRAWTOOL.value());
        add(fillToolPanel, ToolType.FILLTOOL.value());
        toolManager.addToolListener(this);
    }

    private void createDrawToolPanel() {
        drawToolPanel = new JPanel();
        sizeLabel = new JLabel("Size: ");
        sizeModel = new SpinnerNumberModel(toolManager.getSize(), 1, 100, 1);
        sizeField = new JSpinner(sizeModel);
        drawToolPanel.add(sizeLabel);
        drawToolPanel.add(sizeField);
        sizeField.addChangeListener(this);
        add(drawToolPanel);
    }

    public void stateChanged(ChangeEvent e) {
        toolManager.setSize(sizeModel.getNumber());
    }
    private void updateToolControls(Tool tool) {
        sizeModel.setValue(toolManager.getSize());
        layout.show(this, tool.getToolType().value());
    }

    public void setCurrentTool(Tool tool) {
        updateToolControls(tool);
    }
}
