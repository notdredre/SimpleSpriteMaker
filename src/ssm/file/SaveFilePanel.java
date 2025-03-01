package ssm.file;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
                saveChooser = new JFileChooser();
                saveChooser.addChoosableFileFilter(gifFilter);
                saveChooser.addChoosableFileFilter(jpgFilter);
                saveChooser.addChoosableFileFilter(pngFilter);
                int result = saveChooser.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    currentTarget = saveChooser.getSelectedFile();
                    imageFileManager.saveImage(currentTarget);
                }
                return;
            }
            imageFileManager.saveImage(currentTarget);
        }
        if (e.getActionCommand().equals("Save As")) {
            saveChooser = new JFileChooser();
            saveChooser.addChoosableFileFilter(gifFilter);
            saveChooser.addChoosableFileFilter(jpgFilter);
            saveChooser.addChoosableFileFilter(pngFilter);
            int result = saveChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                imageFileManager.saveImage(saveChooser.getSelectedFile());
            }
        }
    }

}
