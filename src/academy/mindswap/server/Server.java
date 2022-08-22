package academy.mindswap.server;
import academy.mindswap.card.Card;
import academy.mindswap.util.Util;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

public class Server {
    private final ServerSocket serverSocket;

    private Socket clientSocket;
    private ArrayList<ClientHandler> listOfClients;
    private static int numberOfPlayers = 0;
    private final int playersNeededToStart;

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
        if (listOfClients.size() >= playersNeededToStart){
                new Thread(new Game(listOfClients)).start();
                prepareServerForNewGame();
        }
    }

    /**
     * Create another list of players that will wait for the new game
     */
    private void prepareServerForNewGame() {
        System.out.println("Game launched, starting a new waiting list");
        listOfClients = new ArrayList<>();
    }


    public class ClientHandler {
        private BufferedReader input;
        private BufferedWriter output;
        final Socket socket;
        private String name;
        private List<Card> deck;

        /**
         * Constructor for our client
         * That will hold the socket we will use to communicate
         * And the name of the user
         */
        public ClientHandler(Socket socket, String name) {
            this.socket = socket;
            this.name = name;
            startBuffers();
            preparePlayerDeckForNextGame();
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
         */
        protected void sendPrivateMessage(String message) {
            try {
                output.write(message);
                output.newLine();
                output.flush();
            } catch (IOException e) {
                checkClientConnections();
                listOfClients.forEach(client-> client.sendPrivateMessage( "Client lost connection."));
            }
        }

        private void checkClientConnections() {
            listOfClients.removeIf(clientHandler -> clientHandler.socket.isClosed());
        }


        /**
         * Using our input buffer to listen a communication of a client
         * @return message
         */
        protected String listenToClient() throws IOException {
            return input.readLine();
        }
        public void preparePlayerDeckForNextGame(){
            this.deck = new ArrayList<>();
        }

        public List<Card> getDeck() {
            return deck;
        }
        protected void addMeToNewGame(){
            listOfClients.add(this);
            areWeReadyToStart();
        }

    }
}
