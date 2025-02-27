package ssm.colour;
import java.awt.Color;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColourSliders extends JPanel implements ColourReading, ChangeListener {
    private JSlider[] colourSliders;
    private ColourManager colourManager;
    
    public ColourSliders() {
        colourManager = ColourManager.getColourManager();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        colourSliders = new JSlider[3];
        for (int i = 0; i < 3; i++) {
            colourSliders[i] = new JSlider(0, 255, 0);
            colourSliders[i].addChangeListener(this);
            add(colourSliders[i]);
            add(Box.createVerticalStrut(20));
        }
    }

    private void setSliders(Color c) {
        colourSliders[0].setValue(c.getRed());
        colourSliders[1].setValue(c.getGreen());
        colourSliders[2].setValue(c.getBlue());
    }

    public void detailColour(Color c) {
        setSliders(c);
    }

    public void stateChanged(ChangeEvent e) {
        int r = colourSliders[0].getValue();
        int g = colourSliders[1].getValue();
        int b = colourSliders[2].getValue();
        colourManager.setColour(r, g, b);
    }
}
