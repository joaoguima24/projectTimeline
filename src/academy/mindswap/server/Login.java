package academy.mindswap.server;

import java.io.IOException;
import java.util.Arrays;

public class Login implements Runnable{
    private final Server.ClientHandler client ;
    private String name;
    private boolean newPlayer;
    private int totalWins;
    private int wantedDeckLength;
    private int wantedNumberOfPlayersPerGame;
    private String newPassword;

    public Login(Server.ClientHandler client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            startLoginValidations();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startLoginValidations() throws IOException {
        client.sendPrivateMessage("Insert your nickname (max 25 characters):");
        analyseName(client.listenToClient());
        client.sendPrivateMessage("Insert your password (max 20 characters):");
        analysePassword(client.listenToClient());
        client.sendPrivateMessage("Insert the number of players that you would like to play, between 2 and 8 inclusive");
        analyseNumberOfPlayers(client.listenToClient());
        client.sendPrivateMessage("Insert the number of cards that you would like to have in the deck, between 3 and 6 inclusive");
        analyseNumberOfCards(client.listenToClient());
        updateClient();
        if (newPlayer){
            savePlayerOnDb();
        }
        client.addClientToLobby(client);
    }

    private void updateClient() {
        client.setName(this.name);
        client.setWins(this.totalWins);
        client.setNumberOfCardsWanted(this.wantedDeckLength);
        client.setNumberOfPlayersWanted(this.wantedNumberOfPlayersPerGame);
    }


    private void analyseName(String nameToAnalyse) throws IOException {
        //fazer regex para so aceitar letras
        if (nameToAnalyse == null
                || nameToAnalyse.equalsIgnoreCase("")
                || nameToAnalyse.length()>25
                || nameToAnalyse.contains(" ")){
            invalidParameter("nickname");
        }
        for (int i = 0; i < client.importDataBaseFromFileToList().size(); i++) {
            assert nameToAnalyse != null;
            if (client.importDataBaseFromFileToList().get(i).startsWith(nameToAnalyse)){
                for (int j = 0; j < client.getPlayersOnline().size(); j++) {
                    if (client.getPlayersOnline().get(j).getName().equals(nameToAnalyse)){
                        invalidParameter("nickname");
                    }
                }
                this.name = nameToAnalyse;
                return;
            }
        }
        this.name=nameToAnalyse;
        this.newPlayer = true;
    }
    private void invalidParameter(String parameter) throws IOException {
        client.sendPrivateMessage("Invalid " + parameter + " please try again.");
        switch (parameter) {
            case "nickname" -> analyseName(client.listenToClient());
            case "password" -> analysePassword(client.listenToClient());
            case "number of players" -> analyseNumberOfPlayers(client.listenToClient());
            case "number of cards" -> analyseNumberOfCards(client.listenToClient());
        }
    }
    private void analysePassword(String passwordToAnalyse) throws IOException {
        if (passwordToAnalyse == null
                || passwordToAnalyse.equalsIgnoreCase("")
                || passwordToAnalyse.length()>25
                || passwordToAnalyse.contains(" ")){
            invalidParameter("password");
            return;
        }
        if (newPlayer){
            this.newPassword = encryptPassword(passwordToAnalyse);
            return;
        }
        client.importDataBaseFromFileToList().forEach(s->{
            if (s.startsWith(name)){
                String[] x = s.split(" ");
                if (!x[1].equals(encryptPassword(passwordToAnalyse))){
                    try {
                        invalidParameter("password");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }


    private String encryptPassword(String passwordToAnalyse) {
        char[] chars = passwordToAnalyse.toCharArray();
        String encryptedPassword="";
        for(char charAtIndex : chars){
            charAtIndex+=3;
            checkCharPosition(charAtIndex);
            encryptedPassword+=charAtIndex;
        }
        return encryptedPassword;
    }

    private char checkCharPosition(char charAtIndex) {
        if (charAtIndex < 33){
            charAtIndex+=33;
        }
        if (charAtIndex > 122){
            charAtIndex-= 122;
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
    private void savePlayerOnDb() {
        client.addAndExportDataBaseFromListToFile(name +" "+ newPassword);
    }
}
