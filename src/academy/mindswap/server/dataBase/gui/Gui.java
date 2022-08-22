package academy.mindswap.server.dataBase.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui {
    public final static ImageIcon BACKGROUND_IMAGE_TIMELINE = new ImageIcon("src/Artboard 1000x1000.png");
    public final static ImageIcon START_BUTTON_IMAGE = new ImageIcon("src/Artboard START.png");

    public static void main(String[] args) {

        JFrame frame = new JFrame("Timeline");
        JLabel label = new JLabel(BACKGROUND_IMAGE_TIMELINE);
        JButton button = new JButton(START_BUTTON_IMAGE);
        //JTextField textField = new JTextField(20);
        JLayeredPane layeredPanel = new JLayeredPane();
        JLabel panel1 = new JLabel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        frame.setVisible(true);
        frame.setResizable(false);

        label.setVisible(true);
        label.setSize(1000, 1000);
        label.setLayout(null);
        layeredPanel.add(label, 1);
        layeredPanel.add(button, 0);
        layeredPanel.setLayout(null);
        layeredPanel.setSize(frame.getWidth(), frame.getHeight());
        layeredPanel.setVisible(true);
        frame.add(layeredPanel);
        button.setVisible(true);
        button.setBounds(350, 600, 300, 100);
        button.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

        //textField.setSize(300, 200);
        //layeredPanel.add(textField);
        panel1.setLayout(null);
        panel1.setText("Insert your nickname (max 25 characters):");
        panel1.setBounds(350, 500, 300, 30);
        /*button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == button) {
                    System.out.println("Button clicked");
                    layeredPanel.removeAll();
                    layeredPanel.add(label, 3);
                    layeredPanel.add("Insert your nickname (max 25 characters):", textField);
                    JButton buttonSubmitNickname = new JButton("SUBMIT");
                    buttonSubmitNickname.setBounds(350, 600, 300, 100);
                    layeredPanel.add(buttonSubmitNickname, 0);
                    layeredPanel.setLayout(null);
                    layeredPanel.add(textField, 1);
                    layeredPanel.add(panel1, 2);
                    textFirld.setHorizontalAlignment(JTextField.CENTER);
                    textField.setSize(300, 30);
                    textField.setBounds(350,550,300,30);
                    textField.setText("");
                    textField.setVisible(true);
                    textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                }
            }
        }); */





        frame.add(label);


    }
}
