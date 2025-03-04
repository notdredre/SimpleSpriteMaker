package ssm.draw;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DrawingKeyboardListener implements KeyListener {
    private DrawPanel drawPanel;

    public DrawingKeyboardListener(DrawPanel drawPanel) {
        this.drawPanel = drawPanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Z && (e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) == KeyEvent.CTRL_DOWN_MASK)
            drawPanel.undo();

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            drawPanel.moveRight();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}
