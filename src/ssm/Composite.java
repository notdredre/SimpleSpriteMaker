package ssm;

import java.awt.Container;
import java.awt.image.BufferedImage;

public class Composite {
    private static Composite composite = null;
    private BufferedImage compositeBuffer;
    private static int parentWidth, parentHeight;
    private Composite(Container parent) {
        parentWidth = parent.getWidth();
        parentHeight = parent.getHeight();
        compositeBuffer = new BufferedImage(parentWidth, parentHeight, BufferedImage.TYPE_3BYTE_BGR);    
    }

    public static Composite getComposite(Container parent) {
        if (composite == null) {
            composite = new Composite(parent);
        }
        if (parent.getWidth() != parentWidth || parent.getHeight() != parentHeight) {
            composite = new Composite(parent);
        }
        return composite;
    }

    public BufferedImage getBuffer() {
        return compositeBuffer;
    }
}
