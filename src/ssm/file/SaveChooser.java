package ssm.file;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import java.awt.Component;

public class SaveChooser extends JFileChooser {
    protected JDialog createDialog(Component parent) {
        JDialog dlg = super.createDialog(parent);
        dlg.setLocationRelativeTo(null);
        return dlg;
    }
}
