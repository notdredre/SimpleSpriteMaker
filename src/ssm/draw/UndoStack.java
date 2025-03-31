package ssm.draw;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class UndoStack {
    private final int STACK_SIZE = 20;
    private BufferedImage[][] stack;
    private int top, bottom;
    private BufferedImage drawCopy, writeCopy;

    public UndoStack() {
        init();
    }

    public void init() {
        stack = new BufferedImage[2][STACK_SIZE];
        top = bottom = -1;
        drawCopy = null;
        writeCopy = null;
    }

    public boolean isFull() {
        return (top + 1) % STACK_SIZE == bottom;
    }

    public boolean isEmpty() {
        return top == -1 && bottom == -1;
    }

    public void push(BufferedImage draw, BufferedImage write) {
        drawCopy = copy(draw);
        writeCopy = copy(write);
        if (isEmpty()) {
            top = bottom = 0;
            stack[0][top] = drawCopy;
            stack[1][top] = writeCopy;
            return;
        }

        if (isFull())
            bottom = (bottom + 1) % STACK_SIZE;

        top = (top + 1) % STACK_SIZE;
        stack[0][top] = drawCopy;
        stack[1][top] = writeCopy;
    }

    public void pop(BufferedImage draw, BufferedImage write) {
        if (isEmpty()) {
            return;
        }

        if (isLast()) {
            top = bottom = -1;
            return;
        }

        top--;
        if (top < 0) {
            top = STACK_SIZE - 1;
        }

        BufferedImage d = stack[0][top];
        BufferedImage w = stack[1][top];

        Graphics2D d2 = (Graphics2D) draw.getGraphics();
        Graphics2D w2 = (Graphics2D) write.getGraphics();
        d2.drawImage(d, 0, 0, null);
        w2.drawImage(w, 0, 0, null);
        d2.dispose();
        w2.dispose();
    }

    public boolean isLast() {
        return top == bottom;
    }

    private BufferedImage copy(BufferedImage src) {
        BufferedImage copy = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        Graphics2D c2 = (Graphics2D) copy.getGraphics();
        c2.drawImage(src, 0, 0, null);
        c2.dispose();
        return copy;
    }
}
