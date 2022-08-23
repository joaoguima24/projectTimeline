package academy.mindswap.server;

import academy.mindswap.util.Util;

import java.io.IOException;

public class Login implements Runnable{
    private final Server.ClientHandler client ;
    private String name;
    private boolean newPlayer;
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

    /**
     * call the methods that will validate the login (Nickname , password , number of players per game , number of cards)
     * after that validations, update all the data collected to our client
     * if this is a new client, call the method to save him to our database
     * add client to our game waiting list
     * @throws IOException
     */
    private void startLoginValidations() throws IOException {
        client.sendPrivateMessage(Util.INSERT_NICKNAME);
        analyseName(client.listenToClient());
        client.sendPrivateMessage(Util.INSERT_PASSWORD);
        analysePassword(client.listenToClient());
        client.sendPrivateMessage(Util.INSERT_NUMBER_OF_PLAYERS);
        analyseNumberOfPlayers(client.listenToClient());
        client.sendPrivateMessage(Util.INSERT_NUMBER_OF_CARDS);
        analyseNumberOfCards(client.listenToClient());
        updateClient();
        if (newPlayer){
            savePlayerOnDb();
        }
        client.addClientToLobby(client);
    }

    /**
     * Update all the data collected in the validations to our clientHandler
     * (name / totalWins / number of players per game / number of cards in the deck)
     */
    private void updateClient() {
        client.setName(this.name);
        client.setNumberOfCardsWanted(this.wantedDeckLength);
        client.setNumberOfPlayersWanted(this.wantedNumberOfPlayersPerGame);
    }

    /**
     *Analyse the name inputted by client:
     * Check if is not null , contains spaces , length <= 25 , if he is on-line
     * If all the tests check negative , we accept the name and save it
     */
    private void analyseName(String nameToAnalyse) throws IOException {
        if (nameToAnalyse.equalsIgnoreCase("")
                || nameToAnalyse.length()>25
                || nameToAnalyse.contains(" ")){
            invalidParameter(Util.PARAMETER_NICKNAME);
        }
        for (int i = 0; i < client.importDataBaseFromFileToList().size(); i++) {
            String[] lineFromDBSSplit = client.importDataBaseFromFileToList().get(i).split(" ");
            if (lineFromDBSSplit[0].equals(nameToAnalyse)){
                for (int j = 0; j < client.getPlayersOnline().size(); j++) {
                    if (client.getPlayersOnline().get(j).getName().equals(nameToAnalyse)){
                        invalidParameter(Util.PARAMETER_NICKNAME);
                    }
                }
                this.name = nameToAnalyse;
                return;
            }
        }
        this.name=nameToAnalyse;
        this.newPlayer = true;
    }

    /**
     * Analyse the password inputted by client and if it's not a new client compares them to the password
     * on the Database
     * Check if (it's empty, length > 25 or contains spaces
     * If all tests passes, and it's a new player encrypt the password and save it in our ClientHandler properties
     * If it's not a new player compares the password encrypted in the database with the password inputted
     * by the client (encrypted)
     */
    private void analysePassword(String passwordToAnalyse) throws IOException {
        if (passwordToAnalyse.equalsIgnoreCase("")
                || passwordToAnalyse.length()>25
                || passwordToAnalyse.contains(" ")){
            invalidParameter(Util.PARAMETER_PASSWORD);
            return;
        }
        if (newPlayer){
            this.newPassword = encryptPassword(passwordToAnalyse);
            return;
        }
        client.importDataBaseFromFileToList().forEach(lineFromDB->{
            String[] lineSplitBySpace = lineFromDB.split(" ");
            if (lineSplitBySpace[0].trim().equals(name)){
                if (!lineSplitBySpace[1].trim().equals(encryptPassword(passwordToAnalyse))){
                    try {
                        invalidParameter(Util.PARAMETER_PASSWORD);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
    /**
     * This method is to encrypt the password, we will change the ascii value of every char of our password
     * return password with changed char value
     */
    private String encryptPassword(String passwordToAnalyse) {
        char[] chars = passwordToAnalyse.toCharArray();
        StringBuilder encryptedPassword= new StringBuilder();
        for(char charAtIndex : chars){
            charAtIndex+=3;
            encryptedPassword.append(checkCharPosition(charAtIndex));
        }
        return encryptedPassword.toString();
    }

    /**
     * We don't want that char value of encrypted password be bigger than 122 or lower than 33, so here
     * we test it and protect our encryptPassword method
     *
     */
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

    /**
     * Check if the players inputted by client , has a value between 2 and 8
     * Convert the parameter from string to integer
     * Save it in ClientHandler properties
     */
    private void analyseNumberOfPlayers(String numberToAnalyse) throws IOException {
        if (numberToAnalyse.equalsIgnoreCase("") || !numberToAnalyse.matches("[2-8]")){
            invalidParameter(Util.PARAMETER_NUMBER_OF_PLAYERS);
            return;
        }
        this.wantedNumberOfPlayersPerGame = Integer.parseInt(numberToAnalyse);
    }

    /**
     * Check if the number of cards inputted by client , has a value between 3 and 6
     * Convert the parameter from string to integer
     * Save it in ClientHandler properties
     */
    private void analyseNumberOfCards(String numberOfCardsToAnalyse) throws IOException {
        if (numberOfCardsToAnalyse.equalsIgnoreCase("") || !numberOfCardsToAnalyse.matches("[3-6]")){
            invalidParameter(Util.PARAMETER_NUMBER_OF_CARDS);
            return;
        }
        this.wantedDeckLength = Integer.parseInt(numberOfCardsToAnalyse);
    }

    /**
     * Send a message to our client that he inputted invalid parameter
     * Call the method to listen and analyse again client input
     * @param parameter
     * @throws IOException
     */
    private void invalidParameter(String parameter) throws IOException {
        client.sendPrivateMessage(Util.INVALID_PARAMETER_LOGIN_TRY_AGAIN);
        switch (parameter) {
            case Util.PARAMETER_NICKNAME -> analyseName(client.listenToClient());
            case Util.PARAMETER_PASSWORD -> analysePassword(client.listenToClient());
            case Util.INSERT_NUMBER_OF_PLAYERS -> analyseNumberOfPlayers(client.listenToClient());
            case Util.PARAMETER_NUMBER_OF_CARDS -> analyseNumberOfCards(client.listenToClient());
        }
    }

    /**
     * Send the parameter name and password to our method that will save that on our database
     */
    private void savePlayerOnDb() {
        client.addAndExportDataBaseFromListToFile(name +" "+ newPassword);
    }
}
