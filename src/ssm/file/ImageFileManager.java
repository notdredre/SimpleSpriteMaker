package ssm.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import ssm.draw.DrawPanel;

public class ImageFileManager {
    private static ImageFileManager imageFileManager = null;
    private String target, targetExtension;
    private BufferedImage toWrite;
    private DrawPanel drawPanel;

    private ImageFileManager() {
        target = "";
        toWrite = null;
        drawPanel = null;
    }

    public static ImageFileManager getImageFileManager() {
        if (imageFileManager == null)
            imageFileManager = new ImageFileManager();
        return imageFileManager;
    }

    public void setToWrite(BufferedImage toWrite) {
        this.toWrite = toWrite;
    }

    public void setTarget(File target) {
        this.target = target.getAbsolutePath();
        String split = target.getName();
        targetExtension = split.substring(split.indexOf('.') + 1);
    }

    public void setDrawPanel(DrawPanel drawPanel) {
        this.drawPanel = drawPanel;
    }

    public void saveImage() {
        File output = new File(target);
        try {
            ImageIO.write(toWrite, targetExtension, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveImage(File target) {
        setTarget(target);
        saveImage();
    }

    public void newDrawing(Number x, Number y) {
        if (drawPanel != null) {
            drawPanel.createNewDrawing(x.intValue(), y.intValue());
        }
    }

    public BufferedImage openImage(InputStream in) throws IOException {
        return ImageIO.read(in);
    }
}
