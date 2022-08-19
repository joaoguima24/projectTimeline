package academy.mindswap.card;

import java.util.HashMap;
import java.util.Map;


public class Card {

    private String name ;
    private String description;
    private int year;

    //public Card(String description, Integer year) {
    //this.name = name;
    //this.description = description;
    //this.year = year;

    public static void deck () {
        Map<String, Integer> deck = new HashMap<>();
        deck.put("first computer", 1936);
        deck.put("first transistor", 1947);
        deck.put("IBM foundation", 1953);
        deck.put("first bank computers", 1955);
        deck.put("first computer chip", 1958);
        deck.put("first video games", 1962);
        deck.put("mouse", 1963);
        deck.put("first internet", 1969);
        deck.put("c", 1972);
        deck.put("first personal computer", 1974);
        deck.put("microsoft foundation", 1975);
        deck.put("apple foundation", 1976);
        deck.put("new internet", 1991);
        deck.put("first laptop", 1991);
        deck.put("java", 1995);
        deck.put("world wide convention", 1996);
        deck.put("mini computers", 1999);
        deck.put("first iphone", 2007);

        System.out.println(deck);
    }

}
/*  public final static int first_Computer = 1936;
    public final static int transistor = 1947;
    public final static int IBM = 1953;
    public final static int bank_Computers = 1955;
    public final static int computer_Chip = 1958;
    public final static int video_Games = 1962;
    public final static int mouse = 1963;
    public final static int internet = 1969;
    public final static int c = 1972;
    public final static int personal_Computers = 1974;
    public final static int microsoft = 1975;
    public final static int apple = 1976;
    public final static int new_Internet = 1991;
    public final static int laptop  = 1991;
    public final static int java = 1995;
    public final static int world_Wide_Convention = 1996;
    public final static int mini_Computers = 1999;
    public final static int iphone = 2007;*/

