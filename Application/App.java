// 2023オブジェクト指向設計演習 最終課題 J222403 永江恵尚
import src.*;
import javax.swing.*;
import java.awt.*;

public class App extends JFrame{
    public static void main(String[] args) {
        new App();
    }

    public App() {
        // Window名
        super("流行語大賞を仕分けろ！");
        add(new AwardCardPanel());
        setPreferredSize(new Dimension(AwardCardPanel.WIDTH, AwardCardPanel.HEIGHT));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

}