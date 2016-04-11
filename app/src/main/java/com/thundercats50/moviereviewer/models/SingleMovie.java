package com.thundercats50.moviereviewer.models;

import android.graphics.Bitmap;

import java.util.HashMap;

public class SingleMovie {

    private String title, synopsis, imageURL;
    private long id;
    private final HashMap<String, Rating> userRatings;

    public SingleMovie() {
        userRatings = new HashMap<>();
    }

    public int getUserRating(String email) {
        return userRatings.get(email).getNumericalRating();
    }

    public boolean hasRatingByUser(String email) {
        return userRatings.containsKey(email);
    }

    public String getUserReview(String email) {
        return userRatings.get(email).getTextReview();
    }
    public void addUserRating(String email, Rating rating) {
        this.userRatings.put(email, rating);
    }

    // holds ImageView for thumbnail
    private Bitmap thumbnail;

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getThumbnailURL() { return imageURL; }

    public void setThumbnailURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public Bitmap getThumbnail() { return thumbnail; }

    public void setThumbnail(Bitmap image) {
        thumbnail = image;
    }


    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }


    public void setId(long id) { this.id = id; }

    public long getId() {
        return id;
    }

    public boolean equals(Object object) {
        if (!(object instanceof SingleMovie)) {
            return false;
        }
        SingleMovie movie = (SingleMovie) object;
        return movie.getId() == id;
    }

}
