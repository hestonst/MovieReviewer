package com.thundercats50.moviereviewer.models;

/**
 * Created by sheston on 13/03/2016.
 */
public class Rating {
    private String user, textReview;
    private int numericalRating;
    private long movieId;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Get the text review of the current movie
     * @return a string that is the text review
     */
    public String getTextReview() {
        return textReview;
    }


    /**
     * sets the text review for the current movie
     * @param textReview the review
     */
    public void setTextReview(String textReview) {
        this.textReview = textReview;
    }


    /**
     * get the numerical rating fot the current movie
     * @return the numerical rating
     */
    public int getNumericalRating() {
        return numericalRating;
    }

    /**
     * set the current movie's numerical rating
     * @param numericalRating the rating
     */
    public void setNumericalRating(int numericalRating) {
        this.numericalRating = numericalRating;
    }

    public void setMovieId(long id) {
        movieId = id;
    }

    public long getMovieId() {
        return movieId;
    }

}
