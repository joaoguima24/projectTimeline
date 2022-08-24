package academy.mindswap.server;


import academy.mindswap.card.Card;
import academy.mindswap.util.Util;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game implements Runnable{
    private final ArrayList<Server.ClientHandler> listOfClients;
    private Server.ClientHandler currentClient;
    private Server.ClientHandler winner = null;
    private final ArrayList<Card> gameDeck;

    private final List<Card> timelineDeck;

    /**
     * Constructor method:
     * Start our game list of players, game deck, timeline (add default first and last card)
     *
     */
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

    /**
     * Start everything we need to get our game playing
     * call method to shuffle our game deck, add the first card to the timeline table, call a method to start
     * players deck, choose a random player to start playing, call a method to send a welcome message to all the
     * players, and start the game.
     */
    protected void startGame(){
        shuffleCards(gameDeck);
        timelineDeck.add(timelineDeck.size()-1, giveCard());
        startPlayersDeck();
        currentClient = listOfClients.get((int) Math.abs(Math.random()*listOfClients.size()));
        broadCastMessage(Util.WELCOME_TO_NEW_GAME);
        playRound();
    }

    /**
     *Receive a list of cards and shuffle them
     */
    private void shuffleCards(List<Card> cards){
        Collections.shuffle(cards);
    }

    /**
     * Check if the ClientHandler has is deck of cards, and give them the cards until he has all the cards to the
     * maximum of the game.
     */
    private void startPlayersDeck(){
        int numberOfCardsPerPlayer = getNumberOfCardsPerPlayer();
        for (Server.ClientHandler client : listOfClients){
            if (client.getDeck().isEmpty() || client.getDeck() == null){
                client.preparePlayerDeckForNextGame();
            }
            while (client.getDeck().size() < numberOfCardsPerPlayer){
                client.getDeck().add(giveCard());
            }
        }
    }

    /**
     * Check all the clients number of cards wanted, and get the average
     * @return number of cards
     */
    private int getNumberOfCardsPerPlayer() {
        int numberOfCards=0;
        for (Server.ClientHandler client : listOfClients){
            numberOfCards+=client.getNumberOfCardsWanted();
        }
        numberOfCards /= listOfClients.size();
        return numberOfCards;
    }

    /**
     * Call a method that check if the game ended
     * If not: send the timeline to all the players, send the deck of each player , call a method to change
     * the player turn and call a method that will receive and validate a message from client
     *
     */
    private void playRound(){
        if(checkWinner()){
            broadCastMessage("G I V E   Y O U R   C O N G R A T U L A T I O N S   TO   " + winner.getName().toUpperCase() + "   , H E");
            broadCastMessage(Util.WINNER_MESSAGE);
            doYouWantToPlayAgain();
            return;
        }
        broadCastMessage(Util.TIMELINE_SEPARATOR);
        sendTimeline();
        broadCastMessage(Util.DECK_SEPARATOR);
        sendDecks();
        changeCurrentPlayer();
        receiveMessage();
    }

    /**
     * Check if the game list only has 1 player, if it is false, check if some ClientHandler has his deck empty.
     * if the test is true, save that client to the winner then
     * @return the result of the test (true or false)
     */
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

    /**
     * Use our method broadCastMessage() to send the timeline to everyone
     */
    private void sendTimeline() {
        for (int i = 0; i < timelineDeck.size(); i++) {
            String cardPresentation="||   Position " + i + "   * * *   " + timelineDeck.get(i).getDescription() + "   * * *    Year: " +timelineDeck.get(i).getYearToString()+"   ||";
            String separator = "-";
            while (separator.length()<cardPresentation.length()){
                separator +="-";
            }
            broadCastMessage(cardPresentation);
            broadCastMessage(separator);
        }
    }

    /**
     * Go to our game list and send a message to each player with his deck
     */
    private void sendDecks() {
        listOfClients.forEach(clientHandler -> clientHandler.sendPrivateMessage(clientHandler.getDeck().toString()));
    }

    /**
     * Check if the play is valid remove the card from player deck, if it's valid:
     * add the card to timeline in the index inputted by the player.
     * if it's not , give a card from game deck to client deck
     * then broadcast a message with the result of the players
     * call method to play another round
     */
    private void validatePlay(int indexCard, int position1){
        int cardYear = currentClient.getDeck().get(indexCard).getYear();
        int firstCardYear = timelineDeck.get(position1).getYear();
        int secondCardYear = timelineDeck.get(position1+1).getYear();
        if (cardYear < firstCardYear || cardYear > secondCardYear){
            broadCastMessage(currentClient.getName() + Util.WRONG_PLAY);
            gameDeck.add(gameDeck.size()-1, currentClient.getDeck().remove(indexCard));
            currentClient.getDeck().add(giveCard());
            playRound();
        }
        broadCastMessage(currentClient.getName() + Util.GOOD_PLAY);
        timelineDeck.add(position1+1,currentClient.getDeck().remove(indexCard));
        playRound();
    }

    /**
     * Wait for a message from a client and call the method to validate the message
     * If we can't communicate, close connection and remove the client from game and online players list and
     * call the method to play another round
     */
    private void receiveMessage(){
        try {
            validateMessage(currentClient.listenToClient());
        } catch (IOException e) {
            listOfClients.remove(currentClient);
            currentClient.getPlayersOnline().remove(currentClient);
            broadCastMessage(currentClient.getName() + Util.CLIENT_LOST_CONNECTION);
            playRound();
        }
    }

    /**
     *Check if the message inputted is valid:
     * -not null or empty and get a letter followed by a number
     * Convert the letter to an index (by ascii value) and check if this index is valid
     * (between 0 and players deck size)
     * Check if the number is valid (between 0 and timeline size)
     * When all the validations are done, call method to validate the play
     */
    private void validateMessage(String message) {
        if (message.equals("")){
            invalidPlay();
        }
        String regexBefore = "\\d+";
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
        if (position1 < 0 || (position1+1) > timelineDeck.size()){
            invalidPlay();
        }
        validatePlay(indexCardByAsciiVal, position1);
    }

    /**
     * Send an invalid play message and wait to another message from client (array out of bounds protection)
     */
    private void invalidPlay() {
        currentClient.sendPrivateMessage(Util.INVALID_PLAY_);
        receiveMessage();
    }

    /**
     * Iterate over the game list changing the current player to the next ClientHandler
     * Send a player turn message to the client
     */
    private void changeCurrentPlayer() {
        int currentPlayerIndex = listOfClients.indexOf(currentClient);
        if (currentPlayerIndex + 1 >= listOfClients.size()){
            currentClient = listOfClients.get(0);
        } else {
            currentClient = listOfClients.get(currentPlayerIndex + 1);
        }
        currentClient.sendPrivateMessage(Util.ITS_YOUR_TURN_TO_PLAY);
    }

    /**
     * remove the first card from the game deck
     * @return a card from the game deck
     */
    private Card giveCard(){return gameDeck.remove(0);}

    /**
     * Send a private message to all the players in the game list
     */
    private void broadCastMessage (String message){
        listOfClients.forEach(client -> client.sendPrivateMessage(message));
    }

    /**
     * Ask the client if he wants to play another game, if the answer is yes, add this clientHandler to the
     * game waiting list in the server, if it's different, close connection and remove them from our online list
     */
    private void doYouWantToPlayAgain(){
        listOfClients.forEach(client->{
            client.sendPrivateMessage(Util.DO_YOU_WANT_TO_PLAY_AGAIN);
            try {
                if (!client.listenToClient().equalsIgnoreCase("yes")){
                    client.socket.close();
                } else {client.addMeToNewGame();}

            } catch (IOException e) {
                client.getPlayersOnline().remove(client);
            }
        });
    }
}
