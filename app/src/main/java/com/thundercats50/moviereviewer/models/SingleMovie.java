package com.thundercats50.moviereviewer.models;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

public class SingleMovie {

    private String title, synopsis, imageURL;
    private long id;

    // not final as movie can have rating updated
    private Map<String, Rating> userRatings;

    /**
     * Create an empty movie object
     */
    public SingleMovie() {
        userRatings = new HashMap<>();
    }

    /**
     * get the rating of this movie based on the user
     * @param email the users email
     * @return the rating that user made
     */
    public int getUserRating(String email) {
        return userRatings.get(email).getNumericalRating();
    }

    /**
     * see if a user made a rating
     * @param email the users email
     * @return true if the user made a rating for this movie, false otherwise
     */
    public boolean hasRatingByUser(String email) {
        return userRatings.containsKey(email);
    }

    /**
     * get the review a user
     * @param email the users email
     * @return the review
     */
    public String getUserReview(String email) {
        return userRatings.get(email).getTextReview();
    }

    /**
     * add a rating for this movie
     * @param email email of the user wgi write ut
     * @param rating the rating itself
     */
    public void addUserRating(String email, Rating rating) {
        this.userRatings.put(email, rating);
    }

    // holds ImageView for thumbnail
    private Bitmap thumbnail;

    /**
     * get the title of the movie
     * @return the title
     */
    public String getTitle() { return title; }

    /**
     * set the title of the movie
     * @param title the title of the movie
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * get the thumbnail url
     * @return String that is the thumbnail url
     */
    public String getThumbnailURL() { return imageURL; }

    /**
     * set the thumbnail URL
     * @param imageURL set the thumbnail URL
     */
    public void setThumbnailURL(String imageURL) {
        this.imageURL = imageURL;
    }


    /**
     * get bitmap of thumbnail
     * @return the bitmap of the thumbnail
     */
    public Bitmap getThumbnail() { return thumbnail; }

    /**
     * directly set bitmap thumbnail
     * @param image the bitmap of the thumbnail
     */
    public void setThumbnail(Bitmap image) {
        thumbnail = image;
    }

    /**
     * get the movie synopsis
     * @return the String of the movie synopsis
     */
    public String getSynopsis() {
        return synopsis;
    }

    /**
     * set the movie synopsis
     * @param synopsis the synopsis
     */
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    /**
     * set the movie ID
     * @param id the ID
     */
    public void setId(long id) { this.id = id; }

    /**
     * get the movie ID
     * @return the movie ID
     */
    public long getId() {
        return id;
    }

    /**
     * see if two movies are equivalent
     * @param object the object to be compared
     * @return true if the same, false otherwise
     */
    public boolean equals(Object object) {
        if (!(object instanceof SingleMovie)) {
            return false;
        }
        SingleMovie movie = (SingleMovie) object;
        return movie.hashCode() == id;
    }

    /**
     * hashcode representation of an instance of movie
     * @return an int hash code
     */
    @Override
    public int hashCode() {
        return (int) id;
    }
}
