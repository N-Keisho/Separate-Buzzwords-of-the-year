import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BallFrame extends JFrame {
    public static double COEF = 0.001;

    public static void main(String[] args){
        new BallFrame();
    }

    // Frameの作成
    public BallFrame(){
        // Window名
        super("ボール操作アプリ");
        add(new BallPanel());
        setPreferredSize(new Dimension(500, 500));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Panel(メイン部)
    class BallPanel extends JPanel implements Runnable, KeyListener {
        private double x, y;
        private double vx, vy;
        private Color color;
        
        // コンストラクタ
        public BallPanel(){
            x = Math.random() * getWidth();
            y = Math.random() * getHeight();
            vx = 2;
            vy = 3;
            color = Color.BLUE;
            setVisible(true);
            Thread th = new Thread(this);
            th.start();
            addKeyListener(this);
            setFocusable(true);
        }

        // ボールの描画
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            g.setColor(color);
            g.fillOval((int) x - 20, (int) y - 20, 40, 40);
            g.setColor(Color.BLACK);
            g.drawString("v_x:" + String.format("%.2f", vx) +
                        " v_y:" + String.format("%.2f", vy),
                        (int) x + 20, (int) y + 20);
        }

        public void run(){
            while (true){
                move();
                repaint();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }

        public void keyTyped(KeyEvent e){

        }

        // Keyを話したときの処理
        public void keyReleased(KeyEvent e){
            color = Color.BLUE;
        }

        // Keyを入力したときの処理
        public void keyPressed(KeyEvent e){
            int keycode = e.getKeyCode();
            switch(keycode) {
                case KeyEvent.VK_UP:
                    vy -= 1;
                    color = Color.RED;
                    break;
                case KeyEvent.VK_DOWN:
                    vy += 1;
                    color = Color.GREEN;
                    break;
                case KeyEvent.VK_RIGHT:
                    vx += 1;
                    color = Color.MAGENTA;
                    break;
                case KeyEvent.VK_LEFT:
                    vx -= 1;
                    color = Color.CYAN;
                    break;
            }
        }

        // Ballの移動と色の変更
        private void move(){
            double resist = 1 - COEF * Math.sqrt(vx * vx + vy * vy);
            vx *= resist;
            vy *= resist;
            x += vx;

            // Windowを超えたときの処理
            if ( x < 0 ){
                x += getWidth();
            } else if ( x > getWidth() ){
                x -= getWidth();
            }
            y += vy;
            if ( y < 0 ){
                y += getHeight();
            } else if ( y > getHeight() ){
                y -= getHeight();
            }
        }
    }
}