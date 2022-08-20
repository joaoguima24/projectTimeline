package academy.mindswap.server;

import academy.mindswap.server.Server;

import java.util.ArrayList;

public class Game implements Runnable{
    private ArrayList<Server.ClientHandler> listOfClients;
    private Server.ClientHandler currentPlayer;

    public Game(ArrayList<Server.ClientHandler> listOfClients) {
        this.listOfClients = listOfClients;
    }

    @Override
    public void run() {
        playGame();


    }

    private void playGame() {
        currentPlayer = listOfClients.get(0);
    }
}
