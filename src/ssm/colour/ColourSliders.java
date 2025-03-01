package ssm.colour;
import java.awt.Color;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColourSliders extends JPanel implements ColourReading, ChangeListener {
    private JPanel containers[];
    private JLabel labels[];
    private JSlider[] colourSliders;
    private ColourManager colourManager;
    
    public ColourSliders() {
        containers = new JPanel[4];
        labels = new JLabel[4];
        labels[0] = new JLabel("R: ");
        labels[1] = new JLabel("G: ");
        labels[2] = new JLabel("B: ");
        labels[3] = new JLabel("A: ");
        colourManager = ColourManager.getColourManager();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        colourSliders = new JSlider[4];
        for (int i = 0; i < 4; i++) {
            colourSliders[i] = new JSlider(0, 255, 0);
            colourSliders[i].addChangeListener(this);
            containers[i] = new JPanel();
            containers[i].add(labels[i]);
            containers[i].add(colourSliders[i]);
            add(containers[i]);
            add(Box.createVerticalStrut(20));
        }
        colourSliders[3].setValue(255);
    }

    private void setSliders(Color c) {
        colourSliders[0].setValue(c.getRed());
        colourSliders[1].setValue(c.getGreen());
        colourSliders[2].setValue(c.getBlue());
        colourSliders[3].setValue(c.getAlpha());
    }

    public void detailColour(Color c) {
        setSliders(c);
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(colourSliders[0])) {
            colourManager.setRed(colourSliders[0].getValue());
        }
        if (e.getSource().equals(colourSliders[1])) {
            colourManager.setGreen(colourSliders[1].getValue());
        }
        if (e.getSource().equals(colourSliders[2])) {
            colourManager.setBlue(colourSliders[2].getValue());
        }
        if (e.getSource().equals(colourSliders[3])) {
            colourManager.setAlpha(colourSliders[3].getValue());
        }
    }
}
