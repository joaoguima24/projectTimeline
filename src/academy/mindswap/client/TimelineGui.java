package academy.mindswap.client;

import academy.mindswap.util.Util;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import java.awt.*;

public class TimelineGui {
    JFrame frame;
    JPanel panel;
    public TimelineGui() {
        // TODO Auto-generated constructor stub
    }


    public static void main(String[] args) {
        TimelineGui gui = new TimelineGui();
        gui.frame = new JFrame();
        gui.frame.setPreferredSize(new Dimension(1000,1000));
        gui.frame.setSize(new Dimension(1000,1000));
        gui.frame.setLayout(new BorderLayout());
        gui.panel = new JPanel();



        JMenuBar menuBar = new JMenuBar();
        gui.setMenuBar(menuBar);
        gui.frame.add(gui.panel, BorderLayout.CENTER);
        gui.frame.setJMenuBar(menuBar);
        gui.setLogin(gui.panel);
        gui.frame.setVisible(true);

    }

    private void setMenuBar(JMenuBar menuBar) {
        // TODO Auto-generated method stub
        JMenu menu = new JMenu("Menu");
        menu.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        menu.setFocusPainted(true);
        menu.setFocusable(true);
        menu.setOpaque(true);
        JMenuItem menuItemLogin = new JMenuItem("Login");
        JMenuItem menuItemLobby = new JMenuItem("Lobby");
        JMenuItem menuItemQuit = new JMenuItem("Quit");
        menuItemLogin.addActionListener(e ->
                panel.setVisible(true));
        menu.add(menuItemLogin);
        menu.add(menuItemLobby);
        menu.add(menuItemQuit);


        menuBar.add(menu);
        menuBar.setBackground(Color.LIGHT_GRAY);
        menuBar.setOpaque(true);

    }
    private void setLogin(JPanel loginPanel){
        loginPanel.setToolTipText("Login");
        loginPanel.setBackground(Color.LIGHT_GRAY);
        loginPanel.setOpaque(true);
        loginPanel.setLayout(null);
        JLabel titleLabel = new JLabel("Login");
        JPanel userLabels = new JPanel();
        JLabel usernameLabel = new JLabel("Username");
        JLabel passwordLabel = new JLabel("Password");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Submit");
        JPanel loginPanelRules = new JPanel();
        JTextArea rules = new JTextArea("Rules");

        userLabels.setLayout(null);
        userLabels.setSize(500,500);
        userLabels.setBackground(Color.LIGHT_GRAY);
        titleLabel.setBounds(250,150,100,50);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        loginButton.setBounds(200,300,100,50);
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if(username.equals("") || password.equals("")){
                JOptionPane.showMessageDialog(null, "Please fill in all fields");
            } else{
                JOptionPane.showMessageDialog(null, "Login Successful");
                usernameField.setText("");
                passwordField.setText("");

                panel.setVisible(false);
            }

        });
        usernameLabel.setBounds(100,200,100,40);
        usernameField.setBounds(200,200,150,40);
        passwordLabel.setBounds(100,250,100,40);
        passwordField.setBounds(200,250,150,40);

        loginPanelRules.setLayout(null);
        loginPanelRules.setOpaque(false);
        rules.setText("By clicking login you " +
                "agree to the rules and " +
                "regulations of the game\n");
        rules.setFont(new Font("Arial", Font.PLAIN, 11));
        rules.setOpaque(false);

        rules.setBounds(0,0,500,200);
        rules.setVisible(true);
        loginPanelRules.add(rules);

        loginPanelRules.setOpaque(false);
        loginPanelRules.setBounds(80,350,500,100);
        loginPanelRules.setVisible(true);



        loginPanel.add(titleLabel);
        userLabels.add(usernameLabel);
        userLabels.add(usernameField);
        userLabels.add(passwordLabel);
        userLabels.add(passwordField);
        userLabels.add(titleLabel);
        userLabels.add(loginPanelRules);
        userLabels.add(loginButton);
        loginPanel.add(userLabels, BorderLayout.CENTER);
        loginPanel.setVisible(true);

    }
}
