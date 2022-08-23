package academy.mindswap.server;
import academy.mindswap.card.Card;
import academy.mindswap.util.Util;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    private final ServerSocket serverSocket;
    private Socket clientSocket;
    private final ArrayList<ClientHandler> playersOnline;
    private ArrayList<ClientHandler> listOfClients;

    /**
     * Open a ServerSocket that will wait for players connection
     * Prepare a list of clients waiting for the game
     * Prepare a list of clients Online
     * Call a method that will listen the players trying to connect to server (acceptClient())
     */

    public Server() {
        try {
            this.serverSocket = new ServerSocket(8080);
            this.listOfClients = new ArrayList<>();
            this.playersOnline = new ArrayList<>();
            System.out.println(Util.WELCOME_MESSAGE);
            acceptClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Waiting for a connection with a blocking method (serverSocket.accept())
     * Create a new object of the class ClientHandler for each player that connects with our server
     * Add this ClientHandler to our list of clients waiting for the game
     * Create a new Thread for the player to login
     * Start again this logic
     *
     */

    private void acceptClient() throws IOException {
        System.out.println(Util.WAITING_CONNECTIONS);
        clientSocket = serverSocket.accept();
        ClientHandler client = new ClientHandler(clientSocket);
        new Thread(new Login(client)).start();
        acceptClient();
    }



    /**
     * If we had the players that we need to start(average of players choices):
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

    /**
     * get the average of the number of players per game choose by players
     *
     */
    private int getPlayersNeededToStart() {
        int playersNeededToStart = 0;
        for (ClientHandler clientHandler : playersOnline) {
           playersNeededToStart += clientHandler.numberOfPlayersWanted;
        }
        playersNeededToStart /= playersOnline.size();
        return Math.max(playersNeededToStart, 2);
    }

    /**
     * Create another game waiting list
     */
    private void prepareServerForNewGame() {
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
         */
        public ClientHandler(Socket socket) {
            this.socket = socket;
            startBuffers();
            preparePlayerDeckForNextGame();
        }

        public void setName(String name) {
            this.name = name;
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

        /**
         * After the login we will update the information of our client:
         * add him to the game waiting list
         * add him to players on-line list
         * call the method that checks if we are ready to start
         */
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
        public String getName() {
            return name;
        }

        /**
         * Using our output buffer to send private messages to the client
         * If we can't communicate, we call a method that checks if this client lost connection
         * And send a client lost connection message to the clients
         */
        protected void sendPrivateMessage(String message) {
            try {
                output.write(message);
                output.newLine();
                output.flush();
            } catch (IOException e) {
                checkClientConnections();
                listOfClients.forEach(client-> client.sendPrivateMessage(Util.CLIENT_LOST_CONNECTION));
            }
        }

        /**
         * if the client socket is closed, we remove them from the game list and the players on-line list
         */
        private void checkClientConnections() {
            listOfClients.removeIf(clientHandler -> clientHandler.socket.isClosed());
            playersOnline.removeIf(clientHandler -> clientHandler.socket.isClosed());
        }


        /**
         * Using our input buffer to listen a communication of a client
         * @return message
         */
        protected String listenToClient() throws IOException {
            return input.readLine();
        }
        /**
         * Start a new list , to receive cards
         */
        public void preparePlayerDeckForNextGame(){this.deck = new ArrayList<>();}

        public List<Card> getDeck() {return deck;}

        /**
         * Add this client to the game waiting list
         */
        protected void addMeToNewGame(){
            listOfClients.add(this);
            areWeReadyToStart();
        }

        /**
         * Read all the lines of our db.txt and pass them for a new list
         * @return list
         */
        protected synchronized List<String> importDataBaseFromFileToList(){
            File dataBase = new File(Util.DATABASE_FILE_PATH);
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

        /**
         * call the method to read our file and give us a list
         * Add a client to this list
         * write the list updated to our file
         */
        protected synchronized void addAndExportDataBaseFromListToFile(String clientToAdd){
            try {
                List<String> db = importDataBaseFromFileToList();
                db.add(clientToAdd);
                Files.write(Path.of(Util.DATABASE_FILE_PATH),db);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
