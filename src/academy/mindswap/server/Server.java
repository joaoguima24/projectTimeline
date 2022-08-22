package academy.mindswap.server;
import academy.mindswap.card.Card;
import academy.mindswap.util.Util;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Array;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    private final ServerSocket serverSocket;

    private Socket clientSocket;
    private ArrayList<ClientHandler> playersOnline;
    private ArrayList<ClientHandler> listOfClients;
    private static int numberOfPlayers = 0;

    /**
     * Create a server with the number of players needed to start a new game
     * Open a ServerSocket that will wait for players connection
     * Prepare a list of clients waiting for the game
     * Call a method that will listen the players trying to connect to server (acceptClient())
     */

    public Server() {
        try {
            this.serverSocket = new ServerSocket(8080);
            this.listOfClients = new ArrayList<>();
            this.playersOnline = new ArrayList<>();
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
        new Thread(new Login(client)).start();
        acceptClient();
    }



    /**
     * If we had the players that we need to start:
     * we will start the game in a new Thread
     * And we will call a method that prepares the main thread for a new game
     */
    private void areWeReadyToStart() {
        int playersNeededToStart = getPlayersNeededToStart();
        if (listOfClients.size() >= playersNeededToStart){
                new Thread(new Game(listOfClients)).start();
                prepareServerForNewGame();
        }
    }

    private int getPlayersNeededToStart() {
        int playersNeededToStart = 0;
        for (ClientHandler clientHandler : playersOnline) {
           playersNeededToStart += clientHandler.numberOfPlayersWanted;
        }
        playersNeededToStart /= playersOnline.size();
        return Math.max(playersNeededToStart, 2);
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
        private int numberOfPlayersWanted;
        private int numberOfCardsWanted;
        private String name;
        private int wins;
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

        public void setName(String name) {
            this.name = name;
        }

        public void setWins(int wins) {
            this.wins = wins;
        }

        public int getNumberOfCardsWanted() {
            return numberOfCardsWanted;
        }

        public ArrayList<ClientHandler> getPlayersOnline(){
            return playersOnline;
        }

        public void setNumberOfPlayersWanted(int numberOfPlayersWanted) {
            this.numberOfPlayersWanted = numberOfPlayersWanted;
        }

        public void setNumberOfCardsWanted(int numberOfCardsWanted) {
            this.numberOfCardsWanted = numberOfCardsWanted;
        }

        protected void addClientToLobby(ClientHandler client) {
            listOfClients.add(client);
            playersOnline.add(client);
            client.sendPrivateMessage(client.getName() + Util.WELCOME_NEW_PLAYER);
            System.out.println(client.getName() + Util.NEW_CONNECTION);
            areWeReadyToStart();
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
            playersOnline.removeIf(clientHandler -> clientHandler.socket.isConnected());
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
        protected synchronized List<String> importDataBaseFromFileToList(){
            File dataBase = new File("/Users/guimaj/Documents/Mindswap/projectTimeline/src/academy/mindswap/server/dataBase/db.txt");
            List<String> db = new ArrayList<>();
            try {
                Scanner readFromDB = new Scanner(dataBase);
                while(readFromDB.hasNextLine()){
                    db.add(readFromDB.nextLine());
                }
                readFromDB.close();
                return db;
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        protected synchronized void addAndExportDataBaseFromListToFile(String clientToAdd){
            try {
                List<String> db = importDataBaseFromFileToList();
                db.add(clientToAdd);
                Files.write(Path.of("/Users/guimaj/Documents/Mindswap/projectTimeline/src/academy/mindswap/server/dataBase/db.txt"),db);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
