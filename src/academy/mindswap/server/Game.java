package academy.mindswap.server;

import academy.mindswap.card.Card;
import academy.mindswap.server.Server;

import java.util.*;

//gm3nd3s code
public class Game implements Runnable{
    private static final int NUMBER_OF_CARDS_PER_PLAYER = 4;
    private ArrayList<Server.ClientHandler> listOfClients;
    private Server.ClientHandler currentClient;
    private Server.ClientHandler winner = null;
    private ArrayList<Card> gameDeck = new ArrayList<>(Card.deck());
    private List<Card> timelineDeck = new ArrayList<>();
    private String currentMessage = "";


    protected void startGame(){
        shuffleCards(gameDeck);
        timelineDeck.add(giveCard());
        makeDek();
        currentClient = listOfClients.get((int) Math.abs(Math.random()*listOfClients.size()));
        playRound();
    }
    private void shuffleCards(List<Card> cards){
        Collections.shuffle(cards);
    };

    private void playRound(){
        if(checkWinner() != null){
            System.out.println(checkWinner().getName() + " is the winner!");
            return;
        };

        sendTimeline();
        sendDecks();
        changeCurrentPlayer();
        if(!receiveMessage();

        if (validatePlay(currentMessage)){
            updateDeck();
            updateTimeline();
            changeCurrentPlayer();
            playRound();
        }
    }

    private boolean validatePlay(String message) {
        if (message.equals("")){
            return false;
        }
        return true;
    }

    private boolean receiveMessage() {
        String message = currentClient.listenToClient();

        if(!validateMessage(message)){
            currentClient.sendPrivateMessage("Invalid inout. Try again!");
            sendMessageToPlayerTurn();
            receiveMessage();
        }
        currentMessage = message;
    }

    private boolean validateMessage(String message) {
        if (message.equals("")){
            return false;
        }
        //First letter(Card) + positions= A 1,2
        String regex = "^[a-z]";
        String messageCardPosition = message.trim().toLowerCase().substring(0,1);
        String positions = message.trim().toLowerCase().substring(1);
        if(){
            return false;
        }
        return true;
    }

    private void sendMessageToPlayerTurn() {
        currentClient.sendPrivateMessage("It's your turn:");
    }

    private void changeCurrentPlayer() {
        int currentPlayerIndex = listOfClients.indexOf(currentClient);
        if (currentPlayerIndex + 1 >= listOfClients.size()){
            currentClient = listOfClients.get(0);
        } else {
            currentClient = listOfClients.get(currentPlayerIndex + 1);
        }
        sendMessageToPlayerTurn();
    }

    private void sendTimeline() {
        Server.ClientHandler.broadcastMessage(timelineDeck.toString());
    }

    private void removeCardFromDeck(Server.ClientHandler client, int i){
        client.deck.remove(i);
    }

    private void sendDecks() {
        for (Server.ClientHandler client : listOfClients){
            client.sendPrivateMessage(client.deck.toString());
        }
    }
    private void makeDek(){
        for (Server.ClientHandler client : listOfClients){
            for (int i = 0; client.deck.size() < NUMBER_OF_CARDS_PER_PLAYER; i++){
                client.deck.add(giveCard());
            }
        }
    }
    private Card giveCard(){
        Card card = gameDeck.get(0);
        gameDeck.remove(0);
        return card;
    }

    private Server.ClientHandler checkWinner() {
        for (Server.ClientHandler client : listOfClients){
            if(client.getDeck().size() == 0){
                winner = client;
                return client;
            }
        }
        return null;
    }



    private void createTimeline(){}
    @Override
    public void run() {

    }
}
