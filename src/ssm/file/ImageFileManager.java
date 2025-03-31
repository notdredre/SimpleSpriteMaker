package ssm.file;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;

public class ImageFileManager {
    private static ImageFileManager imageFileManager = null;
    private String target, targetExtension;
    private BufferedImage toWrite, solidWrite;

    private ImageFileManager() {
        target = "";
        toWrite = null;
        solidWrite = null;
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

    public void saveImage() {
        File output = new File(target);
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

    public static ArrayList<BufferedImage> openSpriteSheet(String path, int numRows, int numCols) {
        ArrayList<BufferedImage> images = new ArrayList<>();
        try {
            BufferedImage sheet = ImageIO.read(new File(path));
            int frameWidth = sheet.getWidth() / numCols;
            int frameHeight = sheet.getHeight() / numRows;
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    BufferedImage frame = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D f2 = (Graphics2D) frame.getGraphics();
                    f2.drawImage(sheet, 0, 0, frameWidth, frameHeight, j * frameWidth, i * frameHeight, j * frameWidth + frameWidth, i * frameHeight + frameHeight, null);
                    f2.dispose();
                    images.add(frame);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return images;
    }

    public static BufferedImage openImage(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
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

    public BufferedImage openImage(InputStream in) throws IOException {
        return ImageIO.read(in);
    }
}
