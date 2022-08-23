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
        deck.add(new Card("Alternating current by Tesla.", 1888));
        deck.add(new Card("First Television.", 1927));
        deck.add(new Card("Solar cells.", 1930));
        deck.add(new Card("First computer.", 1936));
        deck.add(new Card("Communications satellite.", 1945));
        deck.add(new Card("First transistor.", 1946));
        deck.add(new Card("First Bug computer.", 1947));
        deck.add(new Card("IBM foundation.", 1953));
        deck.add(new Card("Advent of the fiber optics.", 1954));
        deck.add(new Card("First bank computers.", 1955));
        deck.add(new Card("Turing test.", 1956));
        deck.add(new Card("First computer chip.", 1958));
        deck.add(new Card("IBM and General Motors develop first Computer graphics.", 1959));
        deck.add(new Card("Theodore Maiman invents the ruby laser.", 1960));
        deck.add(new Card("The first commercially available IC's (integrated circuits) .", 1961));
        deck.add(new Card("First video games.", 1962));
        deck.add(new Card("Advent of the mouse.", 1963));
        deck.add(new Card("E-commerce from IBM.", 1964));
        deck.add(new Card("Dynamic random access memory (DRAM).", 1966));
        deck.add(new Card("GPS becomes available for commercial use.", 1967));
        deck.add(new Card("The movie \"2001: A Space Odyssey\" was released.", 1968));
        deck.add(new Card("First internet.", 1969));
        deck.add(new Card("First computer virus.", 1971));
        deck.add(new Card("C programming language.", 1972));
        deck.add(new Card("First mobile.", 1973));
        deck.add(new Card("First personal computer.", 1974));
        deck.add(new Card("Microsoft foundation.", 1975));
        deck.add(new Card("Apple foundation.", 1976));
        deck.add(new Card("Atari 2600 are launched.", 1977));
        deck.add(new Card("Introduced DOS 3.1, the first operating system for the Apple computers.", 1978));
        deck.add(new Card("The first commercial version of SQL by Oracle", 1979));
        deck.add(new Card("The MS-DOS Epoch time was set to start", 1980));
        deck.add(new Card("The first internal computer speaker was invented by IBM ", 1981));
        deck.add(new Card("Jack Kilby was inducted into the National Inventors Hall of Fame. ", 1982));
        deck.add(new Card("Compact discs (CDs) are launched.", 1983));
        deck.add(new Card("The first IOCCC (Internet Obfuscated C Code Contest) was held.", 1984));
        deck.add(new Card("Steve Jobs quits Apple.", 1985));
        deck.add(new Card("The Hacker Manifesto was published in Phrack.", 1986));
        deck.add(new Card("Final Fantasy role playing video game was released", 1987));
        deck.add(new Card("Microsoft Office was first introduced", 1988));
        deck.add(new Card("Tim Berners-Lee invents the World Wide Web.", 1989));
        deck.add(new Card("The Internet Movie Database (IMDb) was launched.", 1990));
        deck.add(new Card("Linus Torvalds creates the first version of Linux.", 1991));
        deck.add(new Card("The phrase \"surfing the Internet\" was coined.", 1992));
        deck.add(new Card("NVIDIA was founded.", 1993));
        deck.add(new Card("Advent of the scanning systems.", 1994));
        deck.add(new Card("Java programming language.", 1995));
        deck.add(new Card("World wide convention.", 1996));
        deck.add(new Card("Electronics companies agree to make Wi-Fi a worldwide standard for wireless Internet.", 1997));
        deck.add(new Card("GOOGLE was founded.", 1998));
        deck.add(new Card("The first USB flash drives were developed.", 1999));
        deck.add(new Card("MThe Nokia 3310 mobile phone was released.", 2000));
        deck.add(new Card("Apple revolutionizes music listening by unveiling its iPod MP3 music player.", 2001));
        deck.add(new Card("iRobot Corporation releases the first version of its Roomba® vacuum cleaning robot.", 2002));
        deck.add(new Card("Tesla, Inc foundation.", 2003));
        deck.add(new Card("Ubuntu was first released.", 2004));
        deck.add(new Card("YouTube was founded.", 2005));
        deck.add(new Card("jQuery was first introduced.", 2006));
        deck.add(new Card("First iphone.", 2007));
        deck.add(new Card("Android system from Google.", 2008));
        deck.add(new Card("Go programming language.", 2009));
        deck.add(new Card("First iPad.", 2010));
        deck.add(new Card("Steve Jobs passed away.", 2011));
        deck.add(new Card("Tinder was introduced.", 2012));
        deck.add(new Card("Aaron Swartz passed away.", 2013));
        deck.add(new Card("The Imitation Game was released.", 2014));
        deck.add(new Card("HTTP/2 was officially released.", 2015));
        deck.add(new Card("TikTok was released.", 2016));
        deck.add(new Card("Fortnite was released.", 2017));
        deck.add(new Card("Stephen Hawking passed away", 2018));
        return deck;
    }

    @Override
    public String toString() {
        return  "||   "+description +"    ||";
    }
    public String getDescription() {
        return description;
    }

    public int getYear() {
        return year;
    }
    public String getYearToString() {
        return String.valueOf(year);
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

