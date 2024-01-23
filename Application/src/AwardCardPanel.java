// 2023オブジェクト指向設計演習 最終課題 J222403 永江恵尚
package src;

import javax.swing.*;
import jaco.mp3.player.MP3Player;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AwardCardPanel extends JPanel implements Runnable {
    // 大きさの定義
    public static final int WIDTH = 600; // パネルの幅
    public static final int HEIGHT = 500; // パネルの高さ
    private static final int CARD_WIDTH = 370; // カードの幅
    private static final int CARD_HEIGHT = 300; // カードの高さ

    // 定数の定義
    private static final int CARDS_NUM = 10; // カードの枚数
    private static final int CARD_BUFFER = 8; // カードの間隔
    private static final int MOVE_SPEED = 25; // カードの移動速度

    // フォントの定義
    private static final Font Ftitle = new Font("Serif", Font.BOLD, 45);
    private static final Font Fyear = new Font("Serif", Font.PLAIN, 30);
    private static final Font Faward = new Font("Serif", Font.PLAIN, 10);
    private static final Font Fword = new Font("Serif", Font.BOLD, 25);
    private static final Font Fwinner = new Font("Serif", Font.PLAIN, 17);
    private static final Font Ftext = new Font("Serif", Font.PLAIN, 16);
    private static final Font[] FONTS = { Fyear, Faward, Fword, Fwinner, Ftext };

    // 効果音の定義
    private static final MP3Player correctSound = new MP3Player(new File("./sound/Correct.mp3"));
    private static final MP3Player wrongSound = new MP3Player(new File("./sound/Wrong.mp3"));
    private static final MP3Player resultSound = new MP3Player(new File("./sound/Result.mp3"));
    private static final MP3Player bgm = new MP3Player(new File("./sound/BGM.mp3"));

    // 変数の定義
    private double speed = 0; // カードの移動速度を保存
    private int currentCardIndex = CARDS_NUM - 1; // 現在のカードのインデックス
    private int standartYear = 1984 + (int) Math.round(Math.random() * 39); // 答えの基準となる年
    private ArrayList<Double> Xs = new ArrayList<Double>(); // カードのX座標
    private ArrayList<Double> Ys = new ArrayList<Double>(); // カードのY座標
    private ArrayList<ArrayList<String>> awardDataList = new ArrayList<ArrayList<String>>(); // カードの中身
    private ArrayList<Integer> correctAnswers = new ArrayList<Integer>(); // カードの答え
    private ArrayList<Integer> userAnswers = new ArrayList<Integer>(); // ユーザーの答え
    private boolean isResult = false; // 結果を表示するかどうか
    private int gameState = 0; // ゲームの状態

    ////////////////////////////////
    // コンストラクタ
    public AwardCardPanel() {
        gameSetting();

        // BGMの再生
        bgm.setRepeat(true);
        bgm.play();

        // Panelの初期化
        setBackground(new Color(241, 238, 216));
        setVisible(true);
        Thread th = new Thread(this);
        th.start();
        addKeyListener(new MyKeyListener());
        setFocusable(true);
    }

    ////////////////////////////////
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

    // 描画処理
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 解答領域の描画
        // BEFORE
        g.setFont(FONTS[4]);
        g.setColor(new Color(66, 133, 244));
        g.fillRect((int) 0, (int) 0, 20, HEIGHT);
        String before = "BRFORE";

        g.setColor(Color.WHITE);
        for (int i = 0; i < before.length(); i++) {
            g.drawString(before.substring(i, i + 1), (int) 5, (int) HEIGHT / 2 - 80 + 20 * i);
        }
        // AFTER
        g.setColor(new Color(234, 67, 53));
        g.fillRect((int) WIDTH - 35, (int) 0, 20, HEIGHT);
        String after = "AFTER";
        g.setColor(Color.WHITE);
        for (int i = 0; i < after.length(); i++) {
            g.drawString(after.substring(i, i + 1), (int) WIDTH - 30, HEIGHT / 2 - 120 + (int) 20 + 20 * i);
        }

        if (gameState == 0) {
            paintTitle(g);
        } else if (gameState == 1) {
            paintGame(g);
        }

    }

    // タイトルの描画
    private void paintTitle(Graphics g){
        // タイトルの描画
        g.setColor(Color.BLACK);
        g.setFont(Ftitle);
        String title = "流行語大賞を仕分けろ！";
        int w = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (int) WIDTH / 2 - w / 2, (int) HEIGHT / 2 - 50);

        int buttonX = (int) WIDTH / 2 - 115;
        int buttonY = (int) HEIGHT / 2;
        
        // リトライボタンの描画
        g.setColor(new Color(251, 188, 5));
        g.fillRect(buttonX, buttonY, 230, 50);
        g.setColor(Color.BLACK);
        g.setFont(FONTS[3]);
        String start = "Space でスタート";
        w = g.getFontMetrics().stringWidth(start);
        g.drawString(start, WIDTH/2 - w / 2, buttonY + 30);

        // 終了ボタンの描画
        buttonY += 65;
        g.setColor(new Color(52, 168, 83));
        g.fillRect(buttonX, buttonY, 230, 50);
        g.setColor(Color.BLACK);
        String fin = "Escape で終了";
        w = g.getFontMetrics().stringWidth(fin);
        g.drawString(fin, WIDTH/2 - w / 2, buttonY + 30);
    }

    // ゲームの描画
    private void paintGame(Graphics g) {
        // 問題文の描画
        g.setColor(Color.BLACK);
        g.setFont(FONTS[0]);
        String standerdYearText = Integer.toString(standartYear) + "年より前？後？";
        int w = g.getFontMetrics().stringWidth(standerdYearText);
        g.drawString(standerdYearText, (int) WIDTH / 2 - w / 2, (int) 35);

        // サポートテキストの描画
        String supportText = "矢印キーで移動";
        g.setColor(Color.BLACK);
        g.setFont(FONTS[4]);
        g.drawString(supportText, (int) WIDTH / 2 - 50, (int) HEIGHT - 50);

        if (currentCardIndex >= 0) {
            // カードの描画
            for (int i = 0; i < currentCardIndex + 1; i++) {
                if (currentCardIndex == i)
                    continue;
                double X = Xs.get(i);
                double Y = Ys.get(i);
                paintCard(g, X, Y, i);
            }
            double X = Xs.get(currentCardIndex);
            double Y = Ys.get(currentCardIndex);
            paintCard(g, X, Y, currentCardIndex);

            // 選択枠の描画
            BasicStroke bs = new BasicStroke(3);
            ((Graphics2D) g).setStroke(bs);
            g.setColor(new Color(52, 168, 83));
            g.drawRect((int) X - 3, (int) Y - 3, CARD_WIDTH + 6, CARD_HEIGHT + 6);
        } 
        else { 
            // 結果を表示
            // 正解数の計算
            int correctNum = 0;
            for (int i = 0; i < userAnswers.size(); i++) {
                if (userAnswers.get(i) == 1)
                    correctNum++;
            }

            // 結果の描画
            g.setColor(Color.BLACK);
            g.setFont(FONTS[0]);
            String result = "正解数: " + Integer.toString(correctNum) + " / " + Integer.toString(CARDS_NUM);
            w = g.getFontMetrics().stringWidth(result);
            g.drawString(result, (int) WIDTH / 2 - w / 2, (int) HEIGHT / 2 - 50);

            int buttonX = (int) WIDTH / 2 - 115;
            int buttonY = (int) HEIGHT / 2 - 30;

            // リトライボタンの描画
            g.setColor(new Color(251, 188, 5));
            g.fillRect(buttonX, buttonY, 230, 50);
            g.setColor(Color.BLACK);
            g.setFont(FONTS[3]);
            String retry = "Space でリトライ";
            w = g.getFontMetrics().stringWidth(retry);
            g.drawString(retry, WIDTH/2 - w / 2, buttonY + 30);

            // タイトルボタンの描画
            buttonY += 65;
            g.setColor(new Color(52, 168, 83));
            g.fillRect(buttonX, buttonY, 230, 50);
            g.setColor(Color.BLACK);
            String title = "T でタイトルへ";
            w = g.getFontMetrics().stringWidth(title);
            g.drawString(title, WIDTH/2 - w / 2, buttonY + 30);

            // 終了ボタンの描画
            buttonY += 65;
            g.setColor(new Color(251, 188, 5));
            g.fillRect(buttonX, buttonY, 230, 50);
            g.setColor(Color.BLACK);
            String fin = "Escape で終了";
            w = g.getFontMetrics().stringWidth(fin);
            g.drawString(fin, WIDTH/2 - w / 2, buttonY + 30);

            // 結果の音楽を再生
            if (isResult == false) {
                resultSound.play();
                isResult = true;
            }
        }
    }

    // カードの描画
    private void paintCard(Graphics g, double X, double Y, int index) {
        // カードの枠
        g.setColor(Color.BLACK);
        g.drawRect((int) X - 1, (int) Y - 1, CARD_WIDTH + 1, CARD_HEIGHT + 1);
        g.setColor(new Color(251, 188, 5));
        g.fillRect((int) X, (int) Y, CARD_WIDTH, CARD_HEIGHT);

        // カードの中身
        int content = 0;
        int row = 0;
        int buffer = 0;
        for (String data : awardDataList.get(index)) {
            g.setColor(Color.BLACK);
            if (content < FONTS.length)
                g.setFont(FONTS[content++]);
            if (data == null || row == 0) {
                // g.drawString(data, (int) X + 20, (int) Y + 30 * row +20);
                row++;
                continue;
            } else if (data.length() > 20) { // 20文字以上の場合は改行
                String message = "";
                for (int j = 0; j < data.length(); j++) {
                    message += data.charAt(j);
                    if (j == 100 - 1) {
                        message += "...";
                        g.drawString(message, (int) X + 20, (int) Y + 30 * row++ + buffer);
                        break;
                    } else if (j % 20 == 0 && j != 0 || j == data.length() - 1) {
                        g.drawString(message, (int) X + 20, (int) Y + 30 * row++ + buffer);
                        message = "";
                    }
                }
            } else
                g.drawString(data, (int) X + 20, (int) Y + 30 * row++ + buffer);
            if (content == 4) {
                g.setColor(new Color(165, 160, 67));
                g.drawLine((int) X + 20, (int) Y + 30 * (row - 1) + 15, (int) X + CARD_WIDTH - 20,
                        (int) Y + 30 * (row - 1) + 15);
                buffer = 15;
            }
        }
    }

    // カードの移動
    private void move() {
        if (currentCardIndex < 0)
            return;
        double x = Xs.get(currentCardIndex) + speed;
        Xs.set(currentCardIndex, x);
        if (x < (0 - CARD_WIDTH)) {
            if (correctAnswers.get(currentCardIndex) == -1) {
                correctSound.play();
                userAnswers.add(1);
            } else {
                wrongSound.play();
                userAnswers.add(0);
            }
            refreshCardPos();
        } else if (x > WIDTH) {
            if (correctAnswers.get(currentCardIndex) == 1) {
                correctSound.play();
                userAnswers.add(1);
            } else {
                wrongSound.play();
                userAnswers.add(0);
            }
            refreshCardPos();
        }
    }

    // カードの位置を更新
    private void refreshCardPos() {
        currentCardIndex--;
        speed = 0;
        for (int i = 0; i < CARDS_NUM; i++) {
            Xs.set(i, ((double) (WIDTH - CARD_WIDTH) / 2 - (double) (currentCardIndex + 1) / 2 * CARD_BUFFER)
                    + i * CARD_BUFFER);
            Ys.set(i, ((double) (HEIGHT - CARD_HEIGHT) / 2 - (double) (currentCardIndex + 1) / 2 * CARD_BUFFER)
                    + i * CARD_BUFFER - 10);
        }
    }

    // ゲームの初期化
    private void gameSetting() {
        speed = 0; // カードの移動速度を保存
        currentCardIndex = CARDS_NUM - 1; // 現在のカードのインデックス
        standartYear = 2000 + (int) Math.round(Math.random() * 10); // 答えの基準となる年
        Xs = new ArrayList<Double>(); // カードのX座標
        Ys = new ArrayList<Double>(); // カードのY座標
        awardDataList = new ArrayList<ArrayList<String>>(); // カードの中身
        correctAnswers = new ArrayList<Integer>(); // カードの答え
        userAnswers = new ArrayList<Integer>(); // ユーザーの答え
        isResult = false; // 結果を表示するかどうか

        // フィールドの初期化
        for (int i = 0; i < CARDS_NUM; i++) {
            Xs.add((double) ((WIDTH - CARD_WIDTH) / 2 - CARDS_NUM / 2 * CARD_BUFFER) + i * CARD_BUFFER);
            Ys.add((double) ((HEIGHT - CARD_HEIGHT) / 2 - CARDS_NUM / 2 * CARD_BUFFER) + i * CARD_BUFFER - 10);
        }

        ArrayList<Integer> years = new ArrayList<Integer>();
        // awardDataListの初期化
        for (int i = 0; i < CARDS_NUM; i++) {
            try {
                // ランダムな年のデータを取得
                int year;
                while (true) {
                    year = 1984 + (int) Math.round(Math.random() * 39);
                    if (!years.contains(year) && year != standartYear && year != 2001) {
                        years.add(year);
                        break;
                    }
                }
                awardDataList.add(OperateJson.BestAward(year));

                // 正解の答えを保存
                if (year > standartYear)
                    correctAnswers.add(1);
                else
                    correctAnswers.add(-1);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    ////////////////////////////////////////
    // キー入力の処理を行うクラス
    class MyKeyListener implements KeyListener {
        // キーがタイプされたとき
        public void keyTyped(KeyEvent e) {
        }

        // キーが押されたとき
        public void keyPressed(KeyEvent e) {
            int keycode = e.getKeyCode();
            switch (keycode) {
                case KeyEvent.VK_RIGHT:
                    if(gameState == 1)
                        speed = MOVE_SPEED;
                    break;
                case KeyEvent.VK_LEFT:
                    if(gameState == 1)
                        speed = -1 * MOVE_SPEED;
                    break;
                case KeyEvent.VK_SPACE:
                    if(gameState == 0 || isResult == true){
                        gameState = 1;
                        gameSetting();
                    }
                    break;
                case KeyEvent.VK_T:
                    if(gameState == 1)
                        gameState = 0;
                    break;
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
                    break;
            }
        }

        // キーが離されたとき
        public void keyReleased(KeyEvent e) {
        }
    }
}