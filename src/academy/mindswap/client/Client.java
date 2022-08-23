package academy.mindswap.client;
import academy.mindswap.server.Server;
import academy.mindswap.server.dataBase.gui.Gui;
import academy.mindswap.util.Util;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.text.ParseException;

public class Client {


        private BufferedWriter output;
        private Socket socket;
        private BufferedReader keyboardReader;
        private BufferedReader input;
        private String name;
        private JFrame frame;
        private JTextField textField;
        private JLayeredPane layeredPanel;
        private JButton button;
        private JLabel label;
        private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    /**
     *Starting our client side
     */
    public static void main(String[] args) {
            Client client = new Client();
        }

    /**
     * Using our default constructor to connect with the server
     * And start waiting for communication
     */
    public Client() {
        JFrame frame = new JFrame("Timeline");
        startGuiWindow(frame);

        connectToServer();
        listenMessage();
        }



    private void startGuiWindow(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(1000, 1000));
        frame.setResizable(true);
        frame.setLayout(new BorderLayout());
        layeredPanel = new JLayeredPane();
        frame.add(layeredPanel);

        label = new JLabel(Gui.BACKGROUND_IMAGE_TIMELINE);
        label.setVisible(true);
        label.setSize(1000,1000);

        button = new JButton(Gui.START_BUTTON_IMAGE);
        button.setVisible(true);
        button.setBounds(350, 600, 300, 100);
        layeredPanel.add(label, 1);
        layeredPanel.add(button, 0);

        frame.setVisible(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcomeScreen(frame);

                try {
                    output.write("SEND_RULES");
                    output.newLine();
                    output.flush();
                } catch (IOException ex) {
                    layeredPanel.add(new JPopupMenu().add(new JMenuItem("Error")), 0);
                }
            }
        });
    }

    private void welcomeScreen(JFrame frame) {
        layeredPanel.removeAll();
        label = new JLabel();
        layeredPanel.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel();
        JTextPane rulesLabel = new JTextPane();
        button = new JButton("Let's Start!");



        label.setPreferredSize(new Dimension(frame.getWidth(), 700));
        label.setBackground(Color.GRAY);
        label.setOpaque(true);
        label.setBounds(0, 0, frame.getWidth(), frame.getHeight());

        createWelcomeLabel(welcomeLabel);
        createRulesLabel(rulesLabel);
        createLetsPlayButton(button);


        label.add(rulesLabel);
        label.add(button, BorderLayout.SOUTH);
        label.add(welcomeLabel, BorderLayout.NORTH);
        label.setVisible(true);
        layeredPanel.add(label, BorderLayout.CENTER);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLoginPage(frame);
            }
        });


    }

    private void setLoginPage(JFrame frame) {
        layeredPanel.removeAll();
        label = new JLabel();
        label.setBackground(Color.GRAY);
        label.setOpaque(true);
        label.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        JLabel loginLabel = new JLabel();
        loginLabel.setText("In order to Play you need to login first");
        loginLabel.setBounds(100, 100, 800, 200);
        loginLabel.setFont(new Font("Comic sans", Font.BOLD, 30));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setVerticalAlignment(SwingConstants.TOP);
        loginLabel.setVisible(true);
        label.add(loginLabel);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.TOP);
        label.setVisible(true);
        usernameAndPasswordLabel(frame, layeredPanel);


        layeredPanel.add(label, BorderLayout.CENTER);
    }

    private void usernameAndPasswordLabel(JFrame frame,JLayeredPane layeredPanel) {
        JLabel usernameLabel = new JLabel("Username");
        JLabel passwordLabel = new JLabel("Password");

        usernameLabel.setBounds(350,250, 300, 60);
        passwordLabel.setBounds(350,350, 300, 60);
        usernameLabel.setFont(new Font("Comic sans", Font.BOLD, 20));
        passwordLabel.setFont(new Font("Comic sans", Font.BOLD, 20));
        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        usernameLabel.setVisible(true);
        passwordLabel.setVisible(true);


        JTextField usernameField = new JTextField("");
        JTextField passwordField = new JTextField("");
        usernameField.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField.setHorizontalAlignment(SwingConstants.CENTER);
        usernameField.setBounds(350,300, 300, 60);
        passwordField.setBounds(350,400, 300, 60);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(425,500, 150, 60);
        loginButton.setHorizontalAlignment(SwingConstants.CENTER);
        loginButton.setFont(new Font("Comic sans", Font.BOLD, 20));
        loginButton.setVisible(true);


        label.add(usernameField);
        label.add(passwordField);
        label.add(usernameLabel);
        label.add(passwordLabel);
        label.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                if(username.equals("") || password.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                } else {
                    try {
                        lobbyGame(frame,layeredPanel);
                        output.write("LOGIN");
                        output.newLine();
                        output.write(username);
                        output.newLine();
                        output.write(password);
                        output.newLine();
                        output.flush();

                    } catch (IOException ex) {
                        layeredPanel.add(new JPopupMenu().add(new JMenuItem("Error")), 0);
                    }
                }

        }
        });
    }

    private void lobbyGame(JFrame frame,JLayeredPane layeredPanel) {
        frame.setSize(new Dimension((int)screenSize.getWidth(),(int) screenSize.getHeight()-50));
        layeredPanel.removeAll();
        JPanel panel = new JPanel();
        layeredPanel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
        layeredPanel.setBounds(0,0, frame.getWidth(), frame.getHeight());
        layeredPanel.setLayout(null);
        layeredPanel.add(panel,0);
        timelineWindow(panel);
        frame.add(panel, BorderLayout.CENTER);







    }

    private void timelineWindow(JPanel panel) {
        panel.setOpaque(true);
        panel.setSize(new Dimension((int)screenSize.getWidth(),(int) screenSize.getHeight()));
        panel.setBackground(Color.BLACK);
        panel.setToolTipText("Timeline");
        panel.setLayout(new BorderLayout());
        JPanel timelinePanel = new JPanel();
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        topPanel.setBackground(Color.BLACK);
        topPanel.setLayout(new BorderLayout());



        JPanel topLeftPanel = new JPanel();
        JPanel topRightPanel = new JPanel();
        JPanel topCenterPanel = new JPanel();
        topCenterPanel.setBounds(50,50, 200,100);



        topPanel.setBackground(Color.BLACK);
        bottomPanel.setBackground(Color.BLACK);
        leftPanel.setBackground(Color.gray);
        rightPanel.setBackground(Color.gray);
        topPanel.add(topLeftPanel, BorderLayout.WEST);
        topPanel.add(topRightPanel, BorderLayout.EAST);
        topPanel.add(topCenterPanel, BorderLayout.CENTER);




        topPanel.setVisible(true);
        timelinePanel.setBackground(Color.WHITE);



        panel.add(timelinePanel, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

    }

    private void createCardTextAndYear(JLabel card, String s, String yearText) {
        JTextArea text = new JTextArea(s);
        text.setFont(new Font("Comic sans", Font.BOLD, 20));
        text.setOpaque(true);
        text.setBackground(Color.gray);
        text.setForeground(Color.WHITE);
        text.setBorder(BorderFactory.createLineBorder(Color.black,2));
        text.setBounds(50,60,this.label.getWidth(), card.getHeight()/2);
        text.setVisible(true);
        card.add(text);

        JTextArea year = new JTextArea(yearText);
        year.setFont(new Font("Comic sans", Font.BOLD, 30));
        year.setOpaque(true);
        year.setBackground(Color.gray);
        year.setForeground(Color.WHITE);
        year.setBorder(BorderFactory.createLineBorder(Color.black,2));
        year.setBounds(50,200,this.label.getWidth(), card.getHeight()/2);
        year.setVisible(true);
        card.add(year);
    }

    private void createCardCar(JLabel card){
        card.setHorizontalAlignment(SwingConstants.CENTER);
        card.setHorizontalTextPosition(SwingConstants.CENTER);
        card.setLayout(null);
        card.setBackground(Color.gray);
        card.setForeground(Color.WHITE);
        card.setHorizontalAlignment(SwingConstants.CENTER);
        card.setBorder(BorderFactory.createLineBorder(Color.WHITE,10));
        card.setBorder(BorderFactory.createLineBorder(Color.BLUE,2,true));
        card.setFont(new Font("Comic sans", Font.BOLD, 10));
        card.setOpaque(true);
        card.setMaximumSize(new Dimension(300,200));
        card.setBounds(50,50,200,100);
        card.setVisible(true);
    }




    private void createRulesLabel(JTextPane label) {
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        label.setOpaque(false);
        label.setStyledDocument(new DefaultStyledDocument());
        label.setBounds(100,100,800,600);
        label.setText(Util.RULES);
        label.setVisible(true);

    }

    private void createLetsPlayButton(JButton button) {
        button.setFont(new Font("Comic sans", Font.BOLD, 30));
        button.setBounds((1000 - 200) / 2, 800, 200,70);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setVisible(true);

    }

    private void createWelcomeLabel(JLabel welcomeLabel) {
        welcomeLabel.setSize(label.getWidth(), 70);
        welcomeLabel.setBackground(new Color(0, 0,30));
        welcomeLabel.setForeground(new Color(0, 50, 255));
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        welcomeLabel.setText("Welcome to Timeline");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setVerticalAlignment(SwingConstants.CENTER);
        welcomeLabel.setOpaque(true);
        welcomeLabel.setVisible(true);
    }

    /**
     * Connect to the Server
     * Start our form of communication with server (input/output)
     */
    private void connectToServer() {
            try {
                this.socket = new Socket("localhost",8080);
                startBuffers();

            } catch (IOException e) {
                throw new RuntimeException("Not connected.");
            }
        }

    /**
     * Starting our forms of communication with the server:
     * input: the way we read a message from server
     * kerboardReader: the way we communicate with our computer through the keyboard
     * output: the way we send the message from keyboardReader to the server
     */
        private void startBuffers() {
            try {
                this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.keyboardReader = new BufferedReader(new InputStreamReader(System.in));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    /**
     * Wait for a communication of the server with the blocking method readLine()
     * Print the communication on our console
     * Then test if the server asked us a question
     * Start this logic again
     */
        private void listenMessage() {
            try {
                String inputFromServer = input.readLine();
                if (inputFromServer == null){
                    System.out.println("You have been disconnected...");
                    return;
                }
                System.out.println(inputFromServer);
                canIPlay(inputFromServer);
                listenMessage();
            } catch (IOException e) {
                throw new RuntimeException("No message");
            }
        }

    /**
     * Test if the server asked me a question, if it's true:
     * I need to answer to play my move using the method sendMessageToServer()
     * @param inputFromServer
     */
    private void canIPlay(String inputFromServer) {
        if (inputFromServer.equalsIgnoreCase(Util.ITS_YOUR_TURN_TO_PLAY)
                || inputFromServer.equalsIgnoreCase("Invalid play, please try again")
                || inputFromServer.equalsIgnoreCase("Do you want to play again?")){
            sendMessageToServer();
        }
    }

    /**
     * We need to use our keyboardReader method to read our message from the keyboard
     * Then we will send the message to the Server
     */
    private void sendMessageToServer() {
        String message = keyboardReader();
        try {
            output.write(message);
            output.newLine();
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendMessageToServer(String message) {

        try {
            output.write(message);
            output.newLine();
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * We will use our buffer to read the message from the keyboard
     * @return keyboard message
     */
    private String keyboardReader(){
            try {
                return keyboardReader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


}
