package academy.mindswap.card;

import java.util.ArrayList;
import java.util.List;



public class Card {
    private final String description;
    private final int year;
    public Card(String description, int year) {
        this.description = description;
        this.year = year;
    }

    public static List<Card> deck () {
        List<Card> deck = new ArrayList<>();
        deck.add(new Card("first computer", 1936));
        deck.add(new Card("first transistor", 1947));
        deck.add(new Card("IBM foundation", 1953));
        deck.add(new Card("first bank computers", 1955));
        deck.add(new Card("first computer chip", 1958));
        deck.add(new Card("first video games", 1962));
        deck.add(new Card("mouse", 1963));
        deck.add(new Card("first internet", 1969));
        deck.add(new Card("c", 1972));
        deck.add(new Card("first personal computer", 1974));
        deck.add(new Card("microsoft foundation", 1975));
        deck.add(new Card("apple foundation", 1976));
        deck.add(new Card("new internet", 1991));
        deck.add(new Card("first laptop", 1991));
        deck.add(new Card("java", 1995));
        deck.add(new Card("world wide convention", 1996));
        deck.add(new Card("mini computers", 1999));
        deck.add(new Card("first iphone", 2007));
        return deck;
    }

    @Override
    public String toString() {
        return  description + '\'' +
                ", year=" + year +
                '}';
    }

    public int getYear() {
        return year;
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

