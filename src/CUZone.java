/*
    File: CUZone.java
    COMP 3008 - Project 2
    Group: 11
    Date Modified: April 6, 2018
 */
import javax.swing.*;

public class CUZone extends JFrame {

    public static void main(String[] string) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setTitle("CUZone Authentication System");
            frame.setSize(450,720);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLocationByPlatform(true);
            frame.setContentPane(new CUZoneView());
            frame.setVisible(true);
        });
    }
}
