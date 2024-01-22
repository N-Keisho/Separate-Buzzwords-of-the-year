package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList; 

public class AwardCardPanel extends JPanel implements Runnable {
    // 各種大きさの定義
    public static final int WIDTH = 600;
    public static final int HEIGHT = 500;
    private static final int CARD_WIDTH = 370;
    private static final int CARD_HEIGHT = 300;
    private static final int CARDS_NUM = 10;

    // 各種フォントの定義
    private static final Font Fyear = new Font("Serif" , Font.PLAIN , 20);
    private static final Font Faward = new Font("Serif" , Font.PLAIN , 10);
    private static final Font Fword = new Font("Serif" , Font.BOLD , 25);
    private static final Font Fwinner = new Font("Serif" , Font.PLAIN, 17 );
    private static final Font Ftext = new Font("Serif" , Font.PLAIN , 16);
    private static final Font[] FONTS = {Fyear, Faward, Fword, Fwinner, Ftext};

    // 各種変数の定義
    private ArrayList<Double> Xs = new ArrayList<Double>();
    private ArrayList<Double> Ys = new ArrayList<Double>();
    private double vx, vy;
    private int currentCardIndex = CARDS_NUM - 1;
    private ArrayList<ArrayList<String>> awardDataList = new ArrayList<ArrayList<String>>();
    public static double COEF = 0.001;

    
    // コンストラクタ
    public AwardCardPanel() {
        // フィールドの初期化
        vx = 0;
        vy = 0;
        for (int i = 0; i < CARDS_NUM; i++) {
            Xs.add((double)((WIDTH -  CARD_WIDTH) / 2 - CARDS_NUM/2*10) + i * 10);
            Ys.add((double)((HEIGHT- CARD_HEIGHT)/ 2 - CARDS_NUM/2*10) + i * 10);
        }

        ArrayList<Integer> years = new ArrayList<Integer>();
        // awardDataListの初期化
        for (int i = 0; i < CARDS_NUM; i++){
            try {
                while(true){
                    int year = 1984 + (int) Math.round(Math.random() * 39);
                    if(!years.contains(year)){
                        years.add(year);
                        break;
                    }
                }
                awardDataList.add(OperateJson.BestAward(years.get(i)));
            } catch (IOException e) {
                System.err.println(e);
            }
        }

        // Panelの初期化
        setVisible(true);
        Thread th = new Thread(this);
        th.start();
        addKeyListener(new MyKeyListener());
        setFocusable(true);
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

    // カードの描画
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < CARDS_NUM; i++) {
            if(currentCardIndex == i) continue;
            double X = Xs.get(i);
            double Y = Ys.get(i);
            paintCard(g, X, Y, i);
        }
        double X = Xs.get(currentCardIndex);
        double Y = Ys.get(currentCardIndex);
        paintCard(g, X, Y, currentCardIndex);
        g.setColor(Color.RED);
        g.drawRect((int) X - 10, (int) Y - 10, CARD_WIDTH + 20, CARD_HEIGHT + 20);
    }

    // カードの描画
    private void paintCard(Graphics g, double X, double Y, int index) {
        // カードの枠
        g.setColor(Color.BLACK);
        g.drawRect((int) X - 1, (int) Y - 1, CARD_WIDTH + 1, CARD_HEIGHT + 1);
        g.setColor(new Color(192, 188, 78));
        g.fillRect((int) X, (int) Y, CARD_WIDTH, CARD_HEIGHT);

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
                g.drawLine((int) X + 20, (int) Y + 30 * ( row - 1) + 15, (int) X + CARD_WIDTH - 20, (int) Y + 30 * ( row - 1 ) + 15);
                buffer = 15;
            } 
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


    // キー入力の処理を行うクラス
    class MyKeyListener implements KeyListener {
        // キーがタイプされたとき
        public void keyTyped(KeyEvent e) {
        }

        // キーが押されたとき
        public void keyPressed(KeyEvent e) {
            int keycode = e.getKeyCode();
            switch (keycode) {
                case KeyEvent.VK_UP:
                    // vy -= 3;
                    break;
                case KeyEvent.VK_DOWN:
                    // vy += 3;
                    break;
                case KeyEvent.VK_RIGHT:
                    vx += 3;
                    break;
                case KeyEvent.VK_LEFT:
                    vx -= 3;
                    break;
                case KeyEvent.VK_SPACE:
                    // try {
                    //     int year = 1984 + (int) Math.round(Math.random() * 39);
                    //     ArrayList<String> Data = OperateJson.BestAward(year);
                    //     awardDataList.set(currentCardIndex, Data);
                        
                    // } catch (IOException error) {
                    //     System.err.println(error);
                    // }
                    break;
                case KeyEvent.VK_ENTER:
                    currentCardIndex = (currentCardIndex + 1) % CARDS_NUM;
                    break;
            }
        }

        // キーが離されたとき
        public void keyReleased(KeyEvent e) {
        }
    }
}