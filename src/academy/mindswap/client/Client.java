package academy.mindswap.client;
import academy.mindswap.util.Util;

import java.io.*;
import java.net.Socket;

public class Client {


        private BufferedWriter output;
        private Socket socket;
        private BufferedReader keyboardReader;
        private BufferedReader input;

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
            connectToServer();
            listenMessage();
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
     * keyboardReader: the way we communicate with our computer through the keyboard
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
     */
    private void canIPlay(String inputFromServer) {
        if (inputFromServer.equalsIgnoreCase(Util.ITS_YOUR_TURN_TO_PLAY)
                || inputFromServer.equalsIgnoreCase(Util.INVALID_PLAY_)
                || inputFromServer.equalsIgnoreCase(Util.DO_YOU_WANT_TO_PLAY_AGAIN)
                || inputFromServer.equalsIgnoreCase(Util.INSERT_PASSWORD)
                || inputFromServer.equalsIgnoreCase(Util.INSERT_NUMBER_OF_PLAYERS)
                || inputFromServer.equalsIgnoreCase(Util.INSERT_NUMBER_OF_CARDS)
                || inputFromServer.equalsIgnoreCase(Util.INVALID_PARAMETER_LOGIN_TRY_AGAIN)
                ||inputFromServer.equalsIgnoreCase(Util.INSERT_NICKNAME)) {
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
