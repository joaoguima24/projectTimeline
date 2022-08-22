package academy.mindswap.server;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.Socket;

public class Login implements Runnable{
    private Server.ClientHandler client ;
    private String name;
    private boolean newPlayer;
    private int totalWins;
    private int wantedDeckLength;
    private int wantedNumberOfPlayersPerGame;
    private JFrame frame;


    public Login(Server.ClientHandler client) {
        this.client = client;
        JFrame frame = new JFrame("Login");
    }
    public Login() {
    }



    @Override
    public void run() {

        try {
            client.sendPrivateMessage("Insert your nickname (max 25 characters):");
            analyseName(client.listenToClient());
            client.sendPrivateMessage("Insert your password (max 20 characters):");
            analysePassword(client.listenToClient());
            client.sendPrivateMessage("Insert the number of players that you would like to play, between 2 and 8 inclusive");
            analyseNumberOfPlayers(client.listenToClient());
            client.sendPrivateMessage("Insert the number of cards that you would like to have in the deck, between 3 and 6 inclusive");
            analyseNumberOfCards(client.listenToClient());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void analyseName(String nameToAnalyse) throws IOException {
        if (name == null || name.equalsIgnoreCase("") || nameToAnalyse.length()>25 || nameToAnalyse.contains(" ")){
            invalidParameter("nickname");
        }
        /*
        if (name does not exists in the document){
            this.newPlayer = true
        }

        nickname password 1
         */
        this.name = nameToAnalyse;
    }
    private void invalidParameter(String parameter) throws IOException {
        client.sendPrivateMessage("Invalid " + parameter + " please try again.");
        switch (parameter) {
            case "nickname" -> analyseName(client.listenToClient());
            case "password" -> analysePassword(client.listenToClient());
            case "number" -> analyseNumberOfPlayers(client.listenToClient());
            case "number of cards" -> analyseNumberOfCards(client.listenToClient());
        }
    }
    private void analysePassword(String passwordToAnalyse) throws IOException {
        if (passwordToAnalyse == null || passwordToAnalyse.equalsIgnoreCase("") || passwordToAnalyse.length()>25 || passwordToAnalyse.contains(" ")){
            invalidParameter("password");
            return;
        }
        if (newPlayer){
            savePassword(encryptPassword(passwordToAnalyse));
            return;
        }
        String passwordFromDB = "";
        // read password from our txt
        if (!passwordFromDB.equals(encryptPassword(passwordToAnalyse))){
            invalidParameter("password");
        }
    }

    private void savePassword(String encryptedPassword) {

    }

    private String encryptPassword(String passwordToAnalyse) {
        char[] chars = passwordToAnalyse.toCharArray();
        String encryptedPassword="";
        String encryptedPassword2="";
        String encryptedPassword3="";
        for(char charAtIndex : chars){
            charAtIndex += 11;
            charAtIndex = checkCharPosition(charAtIndex);
            encryptedPassword += charAtIndex;
            charAtIndex += 20;
            charAtIndex = checkCharPosition(charAtIndex);
            encryptedPassword2 += charAtIndex;
            charAtIndex += 19;
            charAtIndex = checkCharPosition(charAtIndex);
            encryptedPassword3 += charAtIndex;
        }
        encryptedPassword+=encryptedPassword2+encryptedPassword3;
        return encryptedPassword;
    }

    private char checkCharPosition(char charAtIndex) {
        if (charAtIndex < 33){
            charAtIndex+=33;
        }
        if (charAtIndex > 126){
            charAtIndex-= 126;
            checkCharPosition(charAtIndex);
        }
        return charAtIndex;
    }

    private void analyseNumberOfPlayers(String numberToAnalyse) throws IOException {
        if (numberToAnalyse == null || numberToAnalyse.equalsIgnoreCase("")){
            invalidParameter("number");
            return;
        }
        if (!numberToAnalyse.matches("[2-8]")){
            invalidParameter("number");
            return;
        }
        this.wantedNumberOfPlayersPerGame = Integer.parseInt(numberToAnalyse);
    }
    private void analyseNumberOfCards(String numberOfCardsToAnalyse) throws IOException {
        if (numberOfCardsToAnalyse == null || numberOfCardsToAnalyse.equalsIgnoreCase("")){
            invalidParameter("number of cards");
            return;
        }
        if (!numberOfCardsToAnalyse.matches("[3-6]")){
            invalidParameter("number of cards");
            return;
        }
        this.wantedDeckLength = Integer.parseInt(numberOfCardsToAnalyse);
    }
}
