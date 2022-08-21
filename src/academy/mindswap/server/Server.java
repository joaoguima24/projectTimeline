package academy.mindswap.server;


import academy.mindswap.card.Card;
import academy.mindswap.util.Util;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Server {
    private ServerSocket serverSocket;

    private Socket clientSocket;
    private ArrayList<ClientHandler> listOfClients;
    private static int numberOfPlayers = 0;
    private static ClientHandler currentClient;
    private int playersNeededToStart;

    /**
     * Create a server with the number of players needed to start a new game
     * Open a ServerSocket that will wait for players connection
     * Prepare a list of clients waiting for the game
     * Call a method that will listen the players trying to connect to server (acceptClient())
     */

    public Server(int playersToStart) {
        try {
            this.playersNeededToStart = playersToStart;
            this.serverSocket = new ServerSocket(8080);
            this.listOfClients = new ArrayList<>();
            acceptClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getCurrentClient() {
        return currentClient;
    }

    /**
     * Waiting for a connection with a blocking method (serverSocket.accept())
     * Create a new object of the class ClientHandler for each player that connects with our server
     * Add this ClientHandler to our list of clients waiting for the game
     * Test if we have the room with the number of players we need to start the game
     * Start again this logic
     *
     */

    private void acceptClient() throws IOException {
        System.out.println(Util.WAITING_CONNECTIONS);
        clientSocket = serverSocket.accept();
        ClientHandler client = new ClientHandler(clientSocket , Util.GENERIC_PLAYER_NAME+(++numberOfPlayers));
        listOfClients.add(client);
        client.sendPrivateMessage(client.getName() + Util.WELCOME_NEW_PLAYER);
        System.out.println(client.getName() + Util.NEW_CONNECTION);
        areWeReadyToStart();
        acceptClient();
    }

    /**
     * If we had the players that we need to start:
     * we will start the game in a new Thread
     * And we will call a method that prepares the main thread for a new game
     */
    private void areWeReadyToStart() {
        if (listOfClients.size() == playersNeededToStart){

            Game game = new Game(listOfClients);
            new Thread(game).start();
            prepareServerForNewGame();

        }
    }

    /**
     * Create another list of players that will wait for the new game
     */
    private void prepareServerForNewGame() {
        listOfClients = new ArrayList<>();
        numberOfPlayers = 0;
    }


    public class ClientHandler {
        private BufferedReader input;
        private BufferedWriter output;
        private final Socket socket;
        private String name;
        protected List<Card> deck;

        /**
         * Constructor for our client
         * That will hold the socket we will use to communicate
         * And the name of the user
         * @param socket
         * @param name
         */
        public ClientHandler(Socket socket, String name) {
            this.socket = socket;
            this.name = name;
            startBuffers();

        }

        /**
         * Start the way of receive (input) and send (output) communication with our client (socket)
         */
        protected void startBuffers() {
            try {
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * give public access to players name
         * @return name
         */
        public String getName() {
            return name;
        }

        /**
         * Using our output buffer to send private messages to the client
         * @param message
         */
        protected void sendPrivateMessage(String message) {
            try {
                output.write(message);
                output.newLine();
                output.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Broadcasting a message to everyone in our list of clients
         * @param message
         */


        /**
         * Using our input buffer to listen a communication of a client
         * @return message
         */
        protected String listenToClient() {
            try {
                return input.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        public List<Card> getDeck() {
            return deck;
        }


    }
}
