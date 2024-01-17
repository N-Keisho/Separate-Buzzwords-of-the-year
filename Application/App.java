import javax.swing.*;

import src.*;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("RandPanelApp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new RandPanel());
        frame.setSize(720, 480);
        // frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}