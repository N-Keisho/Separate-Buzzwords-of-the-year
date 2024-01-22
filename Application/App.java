import javax.swing.*;
import java.awt.*;
import src.*;

public class App extends JFrame{
    public static void main(String[] args) {
        new App();
    }

    public App() {
        // Window名
        super("ボール操作アプリ");
        add(new AwardCardPanel());
        setPreferredSize(new Dimension(AwardCardPanel.WIDTH, AwardCardPanel.HEIGHT));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

}