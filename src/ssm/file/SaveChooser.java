package ssm.file;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;

public class SaveChooser extends JFileChooser {
    private final FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG Image", "png");
    private final FileNameExtensionFilter gifFilter = new FileNameExtensionFilter("GIF Image", "gif");
    private final FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("JPG/JPEG Image", "jpg", "jpeg");
    private String targetExtension;

    protected JDialog createDialog(Component parent) {
        JDialog dlg = super.createDialog(parent);
        dlg.setLocationRelativeTo(null);
        return dlg;
    }

    public SaveChooser() {
        setAcceptAllFileFilterUsed(false);
        addChoosableFileFilter(gifFilter);
        addChoosableFileFilter(jpgFilter);
        addChoosableFileFilter(pngFilter);
        setSelectedFile(new File("Untitled"));
        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                FileNameExtensionFilter filter = (FileNameExtensionFilter) getFileFilter();
                targetExtension = filter.getExtensions()[0];
            }
        });
    }

    public String getTargetPath() throws FileNotFoundException {
        if (getSelectedFile() != null)
            return getSelectedFile().getAbsolutePath();
        throw new FileNotFoundException();
    }

    public String getTargetExtension() {
        return targetExtension;
    }

}
