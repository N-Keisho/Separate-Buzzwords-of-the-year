package src;

import java.awt.event.*;
import javax.swing.*;

public class RandPanel extends JPanel {
    private JLabel label = new JLabel("random value");
    private JButton button = new JButton("Generate Random");

    public RandPanel(){
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(label);
        add(button);
        button.addActionListener(new MyListener());
    }

    class MyListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            label.setText(Double.toString(Math.random()));
        }
    }
}
