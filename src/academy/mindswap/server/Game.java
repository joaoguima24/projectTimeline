package academy.mindswap.server;

import academy.mindswap.card.Card;
import academy.mindswap.server.Server;

import java.util.ArrayList;
import java.util.HashMap;

//gm3nd3s code
public class Game implements Runnable{
    private static final int NUMBER_OF_CARDS_PER_PLAYER = 4;
    private ArrayList<Server.ClientHandler> listOfClients;
    private Server.ClientHandler winner = null;
    private void shuffleCards(HashMap cards){

    };
    private void createClientDeck(HashMap cards){

    };

    private void playRound(){
        if(checkWinner()){

        };
        sendTimeline();
        sendDecks();
        sendMessageToPlayerTurn();
        receiveMessage(){
           if(!validateMessage()){
               sendMessageToPlayerTurn();
               receiveMessage();
           }
        }
        if (validatePlay()){
            updateDeck();
            updateTimeline();
            changeCurrentPlayer();
            playRound();
        }
    }

    private void sendDecks() {
        for (Server.ClientHandler client : listOfClients){
            client.deck = giveCard().repeat(NUMBER_OF_CARDS_PER_PLAYER);
        }
    }

    private Object giveCard() {
        Card.deck();
    }

    private boolean checkWinner() {
        for (Server.ClientHandler client : listOfClients){
            if(client.getDeck().size() == 0){
                winner = client;
                return true;
            }
        }
        return false;
    }



    private void createTimeline(){}
    @Override
    public void run() {

    }
}
