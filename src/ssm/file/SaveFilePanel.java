package ssm.file;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class SaveFilePanel extends JPanel implements ActionListener {
    private File currentTarget;
    private JFileChooser saveChooser;
    private final FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG Image", "png");
    private final FileNameExtensionFilter gifFilter = new FileNameExtensionFilter("GIF Image", "gif");
    private final FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("JPG/JPEG Image", "jpg", "jpeg");
    private JButton saveButton, saveAsButton, newButton;
    private NewDrawingDialog newDialog;
    private ImageFileManager imageFileManager;
    private String targetExtension;

    public SaveFilePanel() {
        setPreferredSize(new Dimension(200, 100));
        imageFileManager = ImageFileManager.getImageFileManager();
        currentTarget = null;
        newDialog = new NewDrawingDialog();
        newButton = new JButton("New Drawing");
        saveButton = new JButton("Save");
        saveAsButton = new JButton("Save As");
        add(newButton);
        add(saveButton);
        add(saveAsButton);
        newButton.addActionListener(this);
        saveButton.addActionListener(this);
        saveAsButton.addActionListener(this);
    }

    private void setUpFileChooser() {
        if (saveChooser == null) {
            saveChooser = new JFileChooser();
            saveChooser.setAcceptAllFileFilterUsed(false);
            saveChooser.addChoosableFileFilter(gifFilter);
            saveChooser.addChoosableFileFilter(jpgFilter);
            saveChooser.addChoosableFileFilter(pngFilter);
            saveChooser.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    FileNameExtensionFilter filter = (FileNameExtensionFilter) saveChooser.getFileFilter();
                    targetExtension = filter.getExtensions()[0];
                }
            });
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("New Drawing")) {
            if (!newDialog.isVisible()) {
                newDialog.setLocationRelativeTo(null);
                newDialog.setVisible(true);
            } else {
                newDialog.toFront();
            }

        }
        if (e.getActionCommand().equals("Save")) {
            if (currentTarget == null) {
                setUpFileChooser();
                int result = saveChooser.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    currentTarget = saveChooser.getSelectedFile();
                    imageFileManager.saveImage(currentTarget.getAbsolutePath(), targetExtension);
                }
                return;
            }
        }
        if (e.getActionCommand().equals("Save As")) {
            setUpFileChooser();
            int result = saveChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                imageFileManager.saveImage(saveChooser.getSelectedFile().getAbsolutePath(), targetExtension);
            }
        }
    }

}
