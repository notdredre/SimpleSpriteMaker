package ssm.file;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import ssm.draw.DrawPanel;

public class ImageFileManager {
    private static ImageFileManager imageFileManager = null;
    private String target, targetExtension;
    private BufferedImage toWrite, solidWrite;
    private DrawPanel drawPanel;

    private ImageFileManager() {
        target = "";
        toWrite = null;
        solidWrite = null;
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

    public void saveImage(String targetName, String targetExtension) {
        File output;
        int extensionInName = targetName.lastIndexOf("." + targetExtension);
        if (extensionInName != -1) {
            output = new File(targetName);
        } else {
            output = new File(targetName + "." + targetExtension);
        }
        try {
            if (targetExtension == "jpg" || targetExtension == "jpeg") {
                convertAlpha();
                ImageIO.write(solidWrite, targetExtension, output);
                return;
            }
            ImageIO.write(toWrite, targetExtension, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void convertAlpha() {
        int width = toWrite.getWidth();
        int height = toWrite.getHeight();
        solidWrite = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D s2 = (Graphics2D) solidWrite.getGraphics();
        s2.setColor(Color.WHITE);
        s2.fillRect(0, 0, width, height);
        s2.drawImage(toWrite, 0, 0, null);
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
