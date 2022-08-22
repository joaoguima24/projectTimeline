package academy.mindswap.client;
import academy.mindswap.server.Server;
import academy.mindswap.util.Util;

import java.io.*;
import java.net.Socket;

public class Client {


        private BufferedWriter output;
        private Socket socket;
        private BufferedReader keyboardReader;
        private BufferedReader input;
        private String name;

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
