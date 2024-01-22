package src;

import java.awt.event.*;
import java.awt.Component;
import java.io.IOException;

import javax.swing.*;
import java.util.ArrayList;

// ランダムに年を選び、その年のアカデミー賞の情報を表示するパネル
public class RandPanel extends JPanel {
    private ArrayList<JLabel> labels = new ArrayList<JLabel>();
    private JButton button = new JButton("Generate Random");

    public RandPanel() {
        try {
            int year = 1984 + (int)Math.round(Math.random() * 39);
            ArrayList<String> awardData = OperateJson.BestAward(year);
            for (String data : awardData)
                labels.add(new JLabel(data));

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            for (JLabel label : labels){
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                add(label);
            }
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(button);
            button.addActionListener(new MyListener());
        } catch (IOException e) {
            System.err.println(e);
        }

    }

    class MyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try{
                int year = 1984 + (int)Math.round(Math.random() * 40);
                ArrayList<String> awardData = OperateJson.BestAward(year);
                for (int i = 0 ; i < labels.size() ; i++){
                    JLabel label = labels.get(i);
                    label.setText(awardData.get(i));
                }
            } catch (IOException error){
                System.err.println(error);;
            }
        }
    }
}

// public class App extends JFrame{
//     public static void main(String[] args) {
//         new App();
//         // JFrame frame = new JFrame("RandPanelApp");
//         // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         // frame.setContentPane(new RandPanel());
//         // frame.setSize(720, 480);
//         // // frame.setLocationRelativeTo(null);
//         // frame.setVisible(true);
    
//     }
// }