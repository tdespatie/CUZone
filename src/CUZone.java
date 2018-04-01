import javax.swing.*;

public class CUZone extends JFrame {

    public static void main(String[] string) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setTitle("CUZone Authentication System");
                frame.setSize(450,700);
                frame.setResizable(false);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationByPlatform(true);
                frame.setContentPane(new CUZoneView());
                frame.setVisible(true);
            }
        });
    }
}
