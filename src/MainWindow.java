import java.awt.FlowLayout;

import javax.swing.JFrame;

public class MainWindow extends JFrame {
    private final int WIDTH = 1000, HEIGHT = 800;

    public MainWindow() {
        setTitle("SimpleSpriteMaker");
        setSize(WIDTH, HEIGHT);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        DrawPanel dp = new DrawPanel();
        setContentPane(dp);
        dp.clear();
    }
}
