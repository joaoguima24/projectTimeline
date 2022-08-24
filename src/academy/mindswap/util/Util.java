package academy.mindswap.util;

import academy.mindswap.server.Server;

public class Util {
    public final static String WAITING_CONNECTIONS = "We are waiting for players to start the game.";
    public final static String WELCOME_NEW_PLAYER = " , welcome to our game!";
    public final static String TIMELINE_SEPARATOR = "\n ----------------------------------------------- TIMELINE ------------------------------------------------- \n";
    public final static String DECK_SEPARATOR = "\n ----------------------------------------------- YOUR DECK ------------------------------------------------ ";
    public final static String WELCOME_TO_NEW_GAME = "WELCOME TO OUR LOBBY, LET'S HAVE SOME FUN !!";
    public final static String WELCOME_MESSAGE = "\n" +
            "____    __    ____  _______  __        ______   ______   .___  ___.  _______    .___________.  ______      .___________. __  .___  ___.  _______       \n" +
            "\\   \\  /  \\  /   / |   ____||  |      /      | /  __  \\  |   \\/   | |   ____|   |           | /  __  \\     |           ||  | |   \\/   | |   ____|      \n" +
            " \\   \\/    \\/   /  |  |__   |  |     |  ,----'|  |  |  | |  \\  /  | |  |__      `---|  |----`|  |  |  |    `---|  |----`|  | |  \\  /  | |  |__         \n" +
            "  \\            /   |   __|  |  |     |  |     |  |  |  | |  |\\/|  | |   __|         |  |     |  |  |  |        |  |     |  | |  |\\/|  | |   __|        \n" +
            "   \\    /\\    /    |  |____ |  `----.|  `----.|  `--'  | |  |  |  | |  |____        |  |     |  `--'  |        |  |     |  | |  |  |  | |  |____       \n" +
            "    \\__/  \\__/     |_______||_______| \\______| \\______/  |__|  |__| |_______|       |__|      \\______/         |__|     |__| |__|  |__| |_______|_____ \n" +
            "                                                                                                                                               |______|\n";
    public final static String WINNER_MESSAGE = "\n" +
            " __       _______.   .___________. __    __   _______    ____    __    ____  __  .__   __. .__   __.  _______ .______      \n" +
            "|  |     /       |   |           ||  |  |  | |   ____|   \\   \\  /  \\  /   / |  | |  \\ |  | |  \\ |  | |   ____||   _  \\     \n" +
            "|  |    |   (----`   `---|  |----`|  |__|  | |  |__       \\   \\/    \\/   /  |  | |   \\|  | |   \\|  | |  |__   |  |_)  |    \n" +
            "|  |     \\   \\           |  |     |   __   | |   __|       \\            /   |  | |  . `  | |  . `  | |   __|  |      /     \n" +
            "|  | .----)   |          |  |     |  |  |  | |  |____       \\    /\\    /    |  | |  |\\   | |  |\\   | |  |____ |  |\\  \\----.\n" +
            "|__| |_______/           |__|     |__|  |__| |_______|       \\__/  \\__/     |__| |__| \\__| |__| \\__| |_______|| _| `._____|\n" +
            "                                                                                                                           \n";
    public final static String NEW_CONNECTION = " Joined our room...";
    public final static String CLIENT_LOST_CONNECTION = " lost connection.";
    public final static String DATABASE_FILE_PATH = "src/academy/mindswap/server/dataBase/db.txt";
    public final static String PARAMETER_NICKNAME = "nickname";
    public final static String PARAMETER_PASSWORD = "password";
    public final static String PARAMETER_NUMBER_OF_PLAYERS = "number of players";
    public final static String PARAMETER_NUMBER_OF_CARDS = "number of cards";
    public final static String WRONG_PLAY = " failed, don't give up ! Fail again, fail better :D";
    public final static String GOOD_PLAY = " is being on fire !!!";
    public final static String ITS_YOUR_TURN_TO_PLAY = "It's your turn to play (Type a letter A = 1st card , B = 2nd card... + the position number)";
    public final static String INSERT_NICKNAME = "Insert your nickname (max 25 characters):";
    public final static String INSERT_PASSWORD = "Insert your password (max 20 characters):";
    public final static String INSERT_NUMBER_OF_PLAYERS = "Insert the number of players that you would like to play, between 2 and 8 inclusive";
    public final static String INSERT_NUMBER_OF_CARDS = "Insert the number of cards that you would like to have in the deck, between 3 and 6 inclusive";
    public final static String INVALID_PARAMETER_LOGIN_TRY_AGAIN = "Invalid parameter, please try again";
    public final static String INVALID_PLAY_ = "Invalid play, please pay attention to the game rules";
    public final static String DO_YOU_WANT_TO_PLAY_AGAIN = "If you want to play again, type yes";
}
