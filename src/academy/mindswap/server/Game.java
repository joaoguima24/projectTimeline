package academy.mindswap.server;


import academy.mindswap.card.Card;
import academy.mindswap.util.Util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//gm3nd3s code
public class Game implements Runnable{
    private static final int NUMBER_OF_CARDS_PER_PLAYER = 1;
    private ArrayList<Server.ClientHandler> listOfClients;
    private Server.ClientHandler currentClient;
    private Server.ClientHandler winner = null;
    private ArrayList<Card> gameDeck;
    private List<Card> timelineDeck;

    public Game(ArrayList<Server.ClientHandler> listOfClients) {
        this.listOfClients = listOfClients;
        this.gameDeck = (ArrayList<Card>) Card.deck();
        this.timelineDeck = new ArrayList<>();
        timelineDeck.add(new Card ("Timeline:",0));
        timelineDeck.add(new Card ("End:",4000));
    }

    protected void startGame(){
        shuffleCards(gameDeck);
        timelineDeck.add(timelineDeck.size()-1, giveCard());
        startPlayersDeck();
        currentClient = listOfClients.get((int) Math.abs(Math.random()*listOfClients.size()));
        playRound();
    }
    private void shuffleCards(List<Card> cards){
        Collections.shuffle(cards);
    };

    private void playRound(){
        if(checkWinner()){
            winner = listOfClients.stream().filter(client->client.getDeck().isEmpty()).findFirst().get();
            broadCastMessage("The Winner is: " + winner.getName());
            return;
        }
        broadCastMessage("\n ~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~. TIMELINE ~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~. \n");
        sendTimeline();
        broadCastMessage("\n ~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~. YOUR DECK ~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~. \n");
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

    private void validatePlay(int indexCard, int position1, int position2){
        //update timeline and deck and call playRound()
        int cardYear = currentClient.getDeck().get(indexCard).getYear();
        int firstCardYear = timelineDeck.get(position1).getYear();
        int secondCardYear = timelineDeck.get(position2).getYear();

        if (cardYear < firstCardYear || cardYear > secondCardYear){
            currentClient.sendPrivateMessage("You played: " + cardYear + " and failed");
            currentClient.getDeck().remove(indexCard);
            currentClient.getDeck().add(giveCard());
            playRound();
        }
        currentClient.sendPrivateMessage("Well played...");
        timelineDeck.add(position2,currentClient.getDeck().remove(indexCard));
        playRound();
    }

    private void receiveMessage() {
        validateMessage(currentClient.listenToClient());
    }

    private void validateMessage(String message) {
        currentClient.sendPrivateMessage(message);
        if (message.equals("")){
            invalidPlay();
        }
        //First letter(Card) + positions= A 1,2
        String regexBefore = "[0-9]*(?=,)";
        String regexAfter = "[0-9]*(?!,)";
        Pattern patternBefore = Pattern.compile(regexBefore);
        Pattern patternAfter = Pattern.compile(regexAfter);
        Matcher matcherBefore = patternBefore.matcher(message);
        Matcher matcherAfter = patternAfter.matcher(message);
        //LETTERS
        String messageCardPosition = message.trim().toLowerCase().substring(0,1);
        int indexCardByAsciiVal = messageCardPosition.charAt(0) - 97;
        if (indexCardByAsciiVal < 0 || indexCardByAsciiVal >= currentClient.getDeck().size()){
            invalidPlay();
        }
        int position1 = 0;
        int position2 = 0;// parse to int

        if(matcherBefore.find()){
            position1 = Integer.parseInt(matcherBefore.group());
        }
        if(matcherAfter.find()){
            position2 = Integer.parseInt(matcherAfter.group());
        }

        // while regex is not working !!!!

        position2 = position1 +1;

        //ATTENTION TO THIS !!!!

        if (position1 < 0 || position2 > timelineDeck.size()|| (position2 - position1 != 1)){
            invalidPlay();
        }
        validatePlay(indexCardByAsciiVal, position1, position2);
    }

    private void invalidPlay() {
        currentClient.sendPrivateMessage("Invalid play, please try again");
        receiveMessage();
    }

    private void sendMessageToPlayerTurn() {
        currentClient.sendPrivateMessage(Util.ITS_YOUR_TURN_TO_PLAY);
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
        broadCastMessage(timelineDeck.toString());
    }

    private void sendDecks() {
        for (Server.ClientHandler client : listOfClients){
            client.sendPrivateMessage(client.getDeck().toString());
        }
    }
    private void startPlayersDeck(){
        for (Server.ClientHandler client : listOfClients){
            if (client.getDeck() == null){
                client.preparePlayerDeckForNextGame();
            }
            for (int i = 0; client.getDeck().size() < NUMBER_OF_CARDS_PER_PLAYER; i++){
                client.getDeck().add(giveCard());
            }
        }
    }
    private Card giveCard(){
        return gameDeck.remove(0);
    }

    private boolean checkWinner() {
        return listOfClients.stream()
                .anyMatch(client->client.getDeck().isEmpty());

        /*for (Server.ClientHandler client : listOfClients){
            if(client.getDeck().size() == 0){
                this.winner = client;
                return;
            }
        }

         */
    }
    protected void broadCastMessage (String message){
        listOfClients.forEach(client -> client.sendPrivateMessage(message));
    }

    @Override
    public void run() {
        startGame();

    }
}
/*
________________________________
| Index 0                  | | Index 1                  || Index 2                  |
| first personal computer  | | first personal computer  || first personal computer  |
| Year                     | | Year                     || Year                     |
__________________________________

----------------
|Position 0     |
|     ^         |dsaldjasdhasjdhaskdjlhalskdhasjdk
|    /  \       |
|    1906       |
_________________
----------------
|Position 0     |
|     ^         |dsaldjasdhasjdhaskdjlhalskdhasjdk
|    /  \       |
|    1906       |
_________________
----------------
|Position 0     |
|     ^         |dsaldjasdhasjdhaskdjlhalskdhasjdk
|    /  \       |
|    1906       |
_________________

 */

