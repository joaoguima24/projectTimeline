package academy.mindswap.server;

import academy.mindswap.card.Card;
import academy.mindswap.server.Server;

import java.util.*;

//gm3nd3s code
public class Game implements Runnable{
    private static final int NUMBER_OF_CARDS_PER_PLAYER = 4;
    private ArrayList<Server.ClientHandler> listOfClients;
    private Server.ClientHandler winner = null;
    private List<Card> timelineDeck = new ArrayList<>();
    private String currentMessage = "";

    protected void startGame(){
        shuffleCards(Card.deck());
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
        sendMessageToPlayerTurn();
        receiveMessage();

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

    private void receiveMessage() {
        String message = listOfClients.get(listOfClients.indexOf(Server.getCurrentClient())).listenToClient();
        if(!validateMessage(message1)){
            listOfClients.get(listOfClients.indexOf(Server.getCurrentClient())).sendPrivateMessage("Invalid inout. Try again!");
            sendMessageToPlayerTurn();
            receiveMessage();
        }
        currentMessage = message;
    }

    private boolean validateMessage(String message) {
    }

    private void sendMessageToPlayerTurn() {
        Server.ClientHandler currentPlayer = (Server.ClientHandler) Server.getCurrentClient();
        currentPlayer.sendPrivateMessage("It's your turn:");
    }

    private void changeCurrentPlayer() {
        int currentPlayerIndex = listOfClients.indexOf(Server.getCurrentClient());
        int nextPlayerIndex = (currentPlayerIndex + 1) % listOfClients.size();
        Server.changeCurrentClient(listOfClients.get(nextPlayerIndex));;
    }

    private void sendTimeline() {
        timelineDeck.add(giveCard(Card.deck()));
    }

    private void removeCardFromDeck(Server.ClientHandler client, int i){
        client.deck.remove(i);
    }

    private void sendDecks() {
        for (Server.ClientHandler client : listOfClients){
            makeDek(client);
        }
    }
    private void makeDek(Server.ClientHandler client){
        for (int i = 0; client.deck.size() < NUMBER_OF_CARDS_PER_PLAYER; i++){
            client.deck.add(giveCard(Card.deck()));
        }
    }
    private Card giveCard(List<Card> cards){
        Card card = cards.get(0);
        cards.remove(0);
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
