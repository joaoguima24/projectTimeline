package academy.mindswap.server;


import academy.mindswap.card.Card;
import academy.mindswap.util.Util;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//gm3nd3s code
public class Game implements Runnable{
    private final ArrayList<Server.ClientHandler> listOfClients;
    private Server.ClientHandler currentClient;
    private Server.ClientHandler winner = null;
    private final ArrayList<Card> gameDeck;
    private final List<Card> timelineDeck;

    public Game(ArrayList<Server.ClientHandler> listOfClients) {
        this.listOfClients = listOfClients;
        this.gameDeck = (ArrayList<Card>) Card.deck();
        this.timelineDeck = new ArrayList<>();
        timelineDeck.add(new Card ("Timeline:",0));
        timelineDeck.add(new Card ("End:",4000));
    }
    @Override
    public void run() {
        startGame();
    }

    protected void startGame(){
        shuffleCards(gameDeck);
        timelineDeck.add(timelineDeck.size()-1, giveCard());
        startPlayersDeck();
        currentClient = listOfClients.get((int) Math.abs(Math.random()*listOfClients.size()));
        broadCastMessage("WELCOME TO OUR GAME, LET'S HAVE SOME FUN !!");
        playRound();
    }
    private void shuffleCards(List<Card> cards){
        Collections.shuffle(cards);
    };
    private void startPlayersDeck(){
        int numberOfCardsPerPlayer = getNumberOfCardsPerPlayer();
        for (Server.ClientHandler client : listOfClients){
            if (client.getDeck().isEmpty() || client.getDeck() == null){
                client.preparePlayerDeckForNextGame();
            }
            for (int i = 0; client.getDeck().size() < numberOfCardsPerPlayer; i++){
                client.getDeck().add(giveCard());
            }
        }
    }

    private int getNumberOfCardsPerPlayer() {
        int numberOfCards=0;
        for (Server.ClientHandler client : listOfClients){
            numberOfCards+=client.getNumberOfCardsWanted();
        }
        numberOfCards /= listOfClients.size();
        return numberOfCards;
    }

    private void playRound(){
        if(checkWinner()){
            broadCastMessage("The Winner is: " + winner.getName());
            doYouWantToPlayAgain();
            return;
        }
        broadCastMessage("\n ~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~. TIMELINE ~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~. \n");
        sendTimeline();
        broadCastMessage("\n ~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~. YOUR DECK ~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~.~.~^~. \n");
        sendDecks();
        changeCurrentPlayer();
        receiveMessage();
    }
    private boolean checkWinner() {
        if (listOfClients.size() <= 1){
            winner = listOfClients.get(0);
            return true;
        }
        if (listOfClients.stream().anyMatch(client->client.getDeck().isEmpty())){
            winner = listOfClients.stream().filter(client->client.getDeck().isEmpty()).findFirst().get();
            return true;
        }
        return false;
    }
    private void sendTimeline() {
        broadCastMessage(timelineDeck.toString());
    }

    private void sendDecks() {
        for (Server.ClientHandler client : listOfClients){
            client.sendPrivateMessage(client.getDeck().toString());
        }
    }

    private void validatePlay(int indexCard, int position1, int position2){
        int cardYear = currentClient.getDeck().get(indexCard).getYear();
        int firstCardYear = timelineDeck.get(position1).getYear();
        int secondCardYear = timelineDeck.get(position2).getYear();
        if (cardYear < firstCardYear || cardYear > secondCardYear){
            currentClient.sendPrivateMessage("You played: " + gameDeck.get(indexCard) + " and failed");
            currentClient.getDeck().remove(indexCard);
            currentClient.getDeck().add(giveCard());
            playRound();
        }
        currentClient.sendPrivateMessage("Well played...");
        timelineDeck.add(position2,currentClient.getDeck().remove(indexCard));
        playRound();
    }

    private void receiveMessage(){
        try {
            validateMessage(currentClient.listenToClient());
        } catch (IOException e) {
            listOfClients.remove(currentClient);
            broadCastMessage(currentClient.getName() + " Lost connection");
            playRound();
        }
    }

    private void validateMessage(String message) {
        currentClient.sendPrivateMessage(message);
        if (message.equals("")){
            invalidPlay();
        }
        String regexBefore = "[0-9]+";
        Pattern patternBefore = Pattern.compile(regexBefore);
        Matcher matcherBefore = patternBefore.matcher(message);
        String messageCardPosition = message.trim().toLowerCase().substring(0,1);
        int indexCardByAsciiVal = messageCardPosition.charAt(0) - 97;
        if (indexCardByAsciiVal < 0 || indexCardByAsciiVal >= currentClient.getDeck().size()){
            invalidPlay();
        }
        int position1 = 0;
        if(matcherBefore.find()){
            position1 = Integer.parseInt(matcherBefore.group());
        }
        int position2 = position1 + 1;
        if (position1 < 0 || position2 > timelineDeck.size()){
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



    private Card giveCard(){
        return gameDeck.remove(0);
    }


    private void broadCastMessage (String message){
        listOfClients.forEach(client -> client.sendPrivateMessage(message));
    }
    private void doYouWantToPlayAgain(){
        listOfClients.forEach(client->{
            client.sendPrivateMessage("Do you want to play again?");
            try {
                if (!client.listenToClient().equalsIgnoreCase("yes")){
                    client.socket.close();
                }
                client.addMeToNewGame();
            } catch (IOException e) {
                throw new RuntimeException("Did not respond, so get timed out");
            }
        });
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
