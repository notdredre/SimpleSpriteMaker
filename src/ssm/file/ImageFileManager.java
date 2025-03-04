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

    public void setTarget(String target, String targetExtension) {
        setTargetExtension(targetExtension);
        this.target = parseName(target);
    }

    public void setTargetExtension(String targetExtension) {
        this.targetExtension = targetExtension;
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
        this.targetExtension = targetExtension;
        target = parseName(targetName);
        output = new File(target);
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

    private String parseName(String fileName) {
        int extensionInName = fileName.lastIndexOf("." + targetExtension);
        if (extensionInName != -1) {
            return fileName;
        } else {
            return fileName + "." + targetExtension;
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
            drawPanel.createNewProject(x.intValue(), y.intValue());
        }
    }

    public BufferedImage openImage(InputStream in) throws IOException {
        return ImageIO.read(in);
    }
}
