package com.thundercats50.moviereviewer.models;

/**
 * Created by sheston on 13/03/2016.
 */
public class Rating {
    private String user, textReview;
    private int numericalRating;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTextReview() {
        return textReview;
    }

    public void setTextReview(String textReview) {
        this.textReview = textReview;
    }

    public int getNumericalRating() {
        return numericalRating;
    }

    public void setNumericalRating(int numericalRating) {
        this.numericalRating = numericalRating;
    }

}
