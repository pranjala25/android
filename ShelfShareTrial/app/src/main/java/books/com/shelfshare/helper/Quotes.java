package books.com.shelfshare.helper;

import java.util.ArrayList;
import java.util.Random;

public class Quotes {

    private Random randomGenerator;
    private static final String q1 = " \"It's the possibility of having a dream come true that makes life interesting.\" ~The Alchemist";
    private static final String q2 = " \"How wonderful it is that nobody need wait a single moment before starting to improve the world.\" ~The Diary of a Young Girl";
    private static final String q3 = " \"Memories warm you up from the inside. But they also tear you apart.\" ~Kafka on the Shore";
    private static final String q4 = " \"Get busy living or get busy dying.\" ~The Shawshank Redemption";
    private static final String q5 = " \"Whatever our souls are made of, his and mine are the same.\" ~Wuthering Heights";
    private int size = 5;
    private ArrayList<String> quotes ;
    public Quotes(){
        randomGenerator = new Random();
        quotes = new ArrayList<>();
        quotes.add(q1);
        quotes.add(q2);
        quotes.add(q3);
        quotes.add(q4);
        quotes.add(q5);
    }

    public String getQuote(){
        String randomQuote = "";
        int index = randomGenerator.nextInt(quotes.size());
        randomQuote = quotes.get(index);
        return randomQuote;
    }
}
