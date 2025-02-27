package ssm.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageFileManager {
    private static ImageFileManager imageFileManager = null;
    private String target, targetExtension;
    private BufferedImage toWrite;

    private ImageFileManager() {
        target = "";
        toWrite = null;
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

    public BufferedImage openImage(String path) throws IOException {
        File in = new File(path);
        return ImageIO.read(in);
    }
}
