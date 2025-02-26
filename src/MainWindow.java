import javax.swing.JFrame;

public class MainWindow extends JFrame {
    private final int WIDTH = 1000, HEIGHT = 800;

    public MainWindow() {
        setTitle("SimpleSpriteMaker");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DrawPanel dp = new DrawPanel();
        add(dp);
        setVisible(true);
        dp.clear();
    }
}
