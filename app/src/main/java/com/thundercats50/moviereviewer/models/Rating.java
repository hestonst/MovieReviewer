package com.thundercats50.moviereviewer.models;

public class Rating {
    private String user, textReview;
    private int numericalRating;
    private long movieId;

    /**
     * get the user who wrote this rating
     * @return the users email
     */
    public String getUser() {
        return user;
    }

    /**
     * set the user for this rating
     * @param mUser
     */
    public void setUser(String mUser) {
        this.user = mUser;
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
     * @param mTextReview the review
     */
    public void setTextReview(String mTextReview) {
        this.textReview = mTextReview;
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

}
