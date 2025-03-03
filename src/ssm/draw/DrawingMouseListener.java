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
        int eventX = e.getX();
        int eventY = e.getY();
        if (SwingUtilities.isLeftMouseButton(e))
            drawPanel.useTool(eventX, eventY, 0);
        if (SwingUtilities.isRightMouseButton(e))
            drawPanel.useTool(eventX, eventY, 1);
        drawPanel.render();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int eventX = mouseX = e.getX();
        int eventY = mouseY = e.getY();
        if (SwingUtilities.isLeftMouseButton(e))
            drawPanel.useTool(eventX, eventY, 0);
        if (SwingUtilities.isRightMouseButton(e))
            drawPanel.useTool(eventX, eventY, 1);
        drawPanel.render();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        int onMask = 0;
        int offMask = MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON2_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK;
        if ((e.getModifiersEx() & (onMask | offMask)) == onMask)
            drawPanel.previewTool();
        drawPanel.render();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        drawPanel.clearOverlay();
        drawPanel.render();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int eventX = e.getX();
        int eventY = e.getY();
        if (drawPanel.comparePixel(eventX, eventY))
            return;
        if (SwingUtilities.isMiddleMouseButton(e)) {
            drawPanel.reposition(eventX, eventY, mouseX, mouseY);
            mouseX = eventX;
            mouseY = eventY;
        }
        if (SwingUtilities.isLeftMouseButton(e))
            drawPanel.useTool(eventX, eventY, 0);
        if (SwingUtilities.isRightMouseButton(e))
            drawPanel.useTool(eventX, eventY, 1);
        drawPanel.previewTool();
        drawPanel.render();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        drawPanel.scaleInput(mouseX, mouseY);
        drawPanel.previewTool();
        drawPanel.render();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (SwingUtilities.isMiddleMouseButton(e)) {
            return;
        }
        int resizeAmount = e.getWheelRotation();
        drawPanel.resize(resizeAmount);
        drawPanel.render();
    }

}
