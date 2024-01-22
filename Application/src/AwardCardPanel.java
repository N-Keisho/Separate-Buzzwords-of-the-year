package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList; 

public class AwardCardPanel extends JPanel implements Runnable, KeyListener {
    private ArrayList<Double> Xs = new ArrayList<Double>();
    private ArrayList<Double> Ys = new ArrayList<Double>();
    private double vx, vy;
    private int currentCardIndex = 0;
    private ArrayList<ArrayList<String>> awardDataList = new ArrayList<ArrayList<String>>();
    public static double COEF = 0.001;

    private static final int CARDS_NUM = 6;
    private static final Font Fyear = new Font("Serif" , Font.PLAIN , 20);
    private static final Font Faward = new Font("Serif" , Font.PLAIN , 10);
    private static final Font Fword = new Font("Serif" , Font.BOLD , 25);
    private static final Font Fwinner = new Font("Serif" , Font.PLAIN, 17 );
    private static final Font Ftext = new Font("Serif" , Font.PLAIN , 16);
    private static final Font[] FONTS = {Fyear, Faward, Fword, Fwinner, Ftext};

    // コンストラクタ
    public AwardCardPanel() {
        // フィールドの初期化
        vx = 2;
        vy = 3;
        for (int i = 0; i < CARDS_NUM; i++) {
            Xs.add(0.0);
            Ys.add(0.0);
        }

        // awardDataListの初期化
        for (int i = 0; i < CARDS_NUM; i++){
            try {
                int year = 1984 + (int) Math.round(Math.random() * 39);
                awardDataList.add(OperateJson.BestAward(year));
            } catch (IOException e) {
                System.err.println(e);
            }
        }

        // Panelの初期化
        setVisible(true);
        Thread th = new Thread(this);
        th.start();
        addKeyListener(this);
        setFocusable(true);
    }

    // カードの描画
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < CARDS_NUM; i++) {
            if(currentCardIndex == i) continue;
            double X = Xs.get(i);
            double Y = Ys.get(i);
            if (i == currentCardIndex) {
                g.setColor(Color.RED);
                g.drawRect((int) X - 10, (int) Y - 10, 390, 320);
            }
            paintCard(g, X, Y, i);
        }
        double X = Xs.get(currentCardIndex);
        double Y = Ys.get(currentCardIndex);
        paintCard(g, X, Y, currentCardIndex);
        g.setColor(Color.RED);
        g.drawRect((int) X - 10, (int) Y - 10, 390, 320);
    }

    // カードの描画
    private void paintCard(Graphics g, double X, double Y, int index) {
        // カードの枠
        g.setColor(Color.BLACK);
        g.drawRect((int) X - 1, (int) Y - 1, 370 + 1, 300 + 1);
        g.setColor(new Color(192, 188, 78));
        g.fillRect((int) X, (int) Y, 370, 300);

        // カードの中身
        int content = 0;
        int row = 0;
        int buffer = 0;
        for (String data : awardDataList.get(index)) {
            g.setColor(Color.BLACK);
            if (content < FONTS.length)  g.setFont(FONTS[content++]);
            if (data == null || row == 0){
                row++;
                continue;
            }
            else if(data.length() > 20){
                String message = "";
                for (int j = 0; j < data.length(); j++) {
                    message += data.charAt(j);
                    if (j == 100 - 1){
                        message += "...";
                        g.drawString(message, (int) X + 20, (int) Y + 30 * row++ + buffer); 
                        break;
                    }
                    else if (j % 20 == 0 && j != 0 || j == data.length() - 1) {
                        g.drawString(message, (int) X + 20, (int) Y + 30 * row++ + buffer);
                        message = "";
                    }
                }
            } else 
                g.drawString(data, (int) X + 20, (int) Y + 30 * row++ + buffer);
            if(content == 4){
                g.setColor(new Color(165,160,67));
                g.drawLine((int) X + 20, (int) Y + 30 * ( row - 1) + 15, (int) X + 350, (int) Y + 30 * ( row - 1 ) + 15);
                buffer = 15;
            } 
        }
    }

    // スレッドの処理
    public void run() {
        while (true) {
            move();
            repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    // Keyを話したときの処理
    public void keyReleased(KeyEvent e) {
    }

    // Keyを入力したときの処理
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        switch (keycode) {
            case KeyEvent.VK_UP:
                vy -= 1;
                break;
            case KeyEvent.VK_DOWN:
                vy += 1;
                break;
            case KeyEvent.VK_RIGHT:
                vx += 1;
                break;
            case KeyEvent.VK_LEFT:
                vx -= 1;
                break;
            case KeyEvent.VK_SPACE:
                try {
                    int year = 1984 + (int) Math.round(Math.random() * 39);
                    ArrayList<String> Data = OperateJson.BestAward(year);
                    awardDataList.set(currentCardIndex, Data);
                    
                } catch (IOException error) {
                    System.err.println(error);
                }
                break;
            case KeyEvent.VK_ENTER:
                currentCardIndex = (currentCardIndex + 1) % CARDS_NUM;
                break;
        }
    }

    // Ballの移動と色の変更
    private void move() {
        double resist = 1 - COEF * Math.sqrt(vx * vx + vy * vy);
        vx *= resist;
        vy *= resist;

        double x = Xs.get(currentCardIndex) + vx;
        // Windowを超えたときの処理
        if (x < 0) {
            x += getWidth();
        } else if (x > getWidth()) {
            x -= getWidth();
        }
        Xs.set(currentCardIndex, x);
        
        double y = Ys.get(currentCardIndex) + vy;
        if (y < 0) {
            y += getHeight();
        } else if (y > getHeight()) {
            y -= getHeight();
        }
        Ys.set(currentCardIndex, y);
    }
}