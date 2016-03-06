import java.util.Date;

/**
 * Created by scottheston on 23/02/16.
 */
public class SingleMovie {
    //TODO: adapt the below coad into somthing useable to hold movie info, or scrap
    private String title;
    private float runTime;
    private String synopsis;
    private String[] actors;
    private int releaseYear;
    private int totalRentals;
    private String[] genres;
    private String userReview;
    private Date releaseDate;
    private long id;

    public SingleMovie() {

    }

    public SingleMovie(String title, String synopsis) {
        this(title, 0f, synopsis, new String[5], 0, 0, new String[5]);
    }

    public SingleMovie(String title, float runTime, String synopsis, String[] actors
            , int releaseYear, int totalRentals, String[] genres) {
        this.title = title;
        this.runTime = runTime;
        this.synopsis = synopsis;
        this.actors = actors;
        this.releaseYear = releaseYear;
        this.totalRentals = totalRentals;
        this.genres = genres;
    }



    public String getTitle() {
        return title;
    }

    public float getRunTime() {
        return runTime;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String[] getActors() {
        return actors;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public int getTotalRentals() {
        return totalRentals;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setUserReview(String userReview) {
        this.userReview = userReview;
    }

    public String getUserReview() {
        return userReview;
    }

    // this begins data required for reddit integration:
    //TODO: delete or remake
    private String thumbnail, url, subreddit, author;


    public void setTitle(String title) { this.title = title; }

    public String getThumbnail() { return thumbnail; }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getSubreddit() { return subreddit; }

    public void setSubreddit(String subreddit) { this.subreddit = subreddit; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public void setReleaseDate(Date aDate) {this.releaseDate = aDate; }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setId(long id) { this.id = id; }

    public long getId() {
        return id;
    }
}