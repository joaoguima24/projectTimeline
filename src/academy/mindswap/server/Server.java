package academy.mindswap.server;

import academy.mindswap.card.Card;

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

    public Server(int playersToStart) {
        try {
            this.playersNeededToStart = playersToStart;
            this.serverSocket = new ServerSocket(8080);
            this.listOfClients = new ArrayList<>();
            acceptClient();
        } catch (IOException e) {
            throw new RuntimeException("Client disconnected...");
        }
    }

    public static Object getCurrentClient() {
        return currentClient;
    }

    public static void changeCurrentClient(ClientHandler client) {
        currentClient = client;
    }

    private void acceptClient() throws IOException {
        System.out.println("Waiting for players to start the game");
        clientSocket = serverSocket.accept();
        ClientHandler client = new ClientHandler(clientSocket , "Player"+(++numberOfPlayers));
        listOfClients.add(client);
        client.sendPrivateMessage(client.getName() + " , welcome to our game!");
        System.out.println(client.getName() + " Joined our room...");
        if (listOfClients.size() == playersNeededToStart){
            Game game = new Game();
            game.startGame();
            currentClient.sendPrivateMessage("It's your turn:");
            System.out.println(currentClient.listenToClient());
        }
        acceptClient();
    }


    public class ClientHandler {
        private BufferedReader input;
        private BufferedWriter output;
        private final Socket socket;
        private String name;
        protected List<Card> deck;

        public ClientHandler(Socket socket, String name) {
            this.socket = socket;
            this.name = name;
            startBuffers();

        }
        private void startBuffers() {
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

        protected void sendPrivateMessage(String message) {
            try {
                output.write(message);
                output.newLine();
                output.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

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
