package com.thundercats50.moviereviewer.models;

public class Rating {
    private String textReview;
    private int numericalRating;

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
