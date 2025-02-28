package ssm;

import javax.swing.SwingUtilities;

public class App implements Runnable {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new App());
    }

    public void run() {
        try {
            new MainWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}