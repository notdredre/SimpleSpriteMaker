package ssm.draw;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

public class DrawingMouseListener implements MouseInputListener, MouseWheelListener {
    private DrawPanel drawPanel;
    private int mouseX, mouseY;

    public DrawingMouseListener(DrawPanel drawPanel) {
        this.drawPanel = drawPanel;
        mouseX = mouseY = -1;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e))
            drawPanel.useTool(0);
        if(SwingUtilities.isRightMouseButton(e))
            drawPanel.useTool(1);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {
        int onMask = 0;
        int offMask = MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON2_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK;
        if ((e.getModifiersEx() & (onMask | offMask)) == onMask)
            drawPanel.previewTool();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        drawPanel.clearOverlay();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        drawPanel.scaleInput(e.getX(), e.getY());
        if(SwingUtilities.isLeftMouseButton(e))
            drawPanel.useTool(0);
        if(SwingUtilities.isRightMouseButton(e))
            drawPanel.useTool(1);
        drawPanel.previewTool();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        drawPanel.scaleInput(mouseX, mouseY);
        drawPanel.previewTool();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int resizeAmount = e.getWheelRotation();
        drawPanel.resize(resizeAmount);
    }
    
}
