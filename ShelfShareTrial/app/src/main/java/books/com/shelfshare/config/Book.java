package books.com.shelfshare.config;

/**
 * Created by ppranjal on 12/16/2016.
 */
public class Book
{

    private String title, author, genre, condition;

    private boolean isRented;

    private String userId;

    public Book(){
    }

    public Book(String userId,String title,String author,String condition){
        this.userId = userId;
        this.title = title;
        this.author=author;
        this.condition=condition;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

}
