package academy.mindswap.server;


import academy.mindswap.card.Card;
import academy.mindswap.server.Server;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//gm3nd3s code
public class Game implements Runnable{
    private static final int NUMBER_OF_CARDS_PER_PLAYER = 4;
    private ArrayList<Server.ClientHandler> listOfClients;
    private Server.ClientHandler currentClient;
    private Server.ClientHandler winner = null;
    private ArrayList<Card> gameDeck = new ArrayList<>(Card.deck());
    private List<Card> timelineDeck = new ArrayList<>();
    private String currentMessage = "";

    public Game(ArrayList<Server.ClientHandler> listOfClients) {
        this.listOfClients = listOfClients;
    }

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
        receiveMessage();

        /*if (validatePlay(currentMessage)){
            updateDeck();
            updateTimeline();
            changeCurrentPlayer();
            playRound();
        }*/
    }

    private void validatePlay(String message, int position1, int position2){
        //update timeline and deck and call playRound()

    }

    private void receiveMessage() {
        validateMessage(currentClient.listenToClient());
    }

    private void validateMessage(String message) {
        if (message.equals("")){
            invalidPlay();
        }
        //First letter(Card) + positions= A 1,2
        String regexBefore = "[0-9]*(?=,)";
        String regexAfter = "0-9]*(?!,)";
        Pattern patternBefore = Pattern.compile(regexBefore);
        Pattern patternAfter = Pattern.compile(regexAfter);
        Matcher matcherBefore = patternBefore.matcher(message);
        Matcher matcherAfter = patternAfter.matcher(message);
        //LETTERS
        String messageCardPosition = message.trim().toLowerCase().substring(0,1);
        int position1 = 0;
        int position2 = 0;// parse to int

        if(matcherBefore.find()){
            position1 = Integer.parseInt(matcherBefore.group());
        }
        if(matcherAfter.find()){
            position2 = Integer.parseInt(matcherAfter.group());
        }
        if(position1 == 0 || position2 == 0){
            invalidPlay();
            validateMessage(currentClient.listenToClient());
        }

        //validate the message and only calls validatePlay(); if it is valid
        validatePlay(messageCardPosition, position1, position2);
    }

    private void invalidPlay() {
        currentClient.sendPrivateMessage("Invalid play, please try again");
        receiveMessage();
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
        for (Server.ClientHandler client : listOfClients){
             client.sendPrivateMessage(timelineDeck.toString());
        }

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
