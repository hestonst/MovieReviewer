package com.thundercats50.moviereviewer.models;

import android.graphics.Bitmap;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import java.io.InputStream;

/**
 * Created by scottheston on 23/02/16.
 */
public class SingleMovie {

    private String title, mpaaRating, synopsis, criticReview, imageURL;
    private List<String> genres, cast;
    private long id;
    private Integer year, runtime;

    // holds ImageView for thumbnail
    protected Bitmap thumbnail;

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getThumbnailURL() { return imageURL; }

    public void setThumbnailURL(String imageURL) {
        imageURL = imageURL;
    }


    public Bitmap getThumbnail() { return thumbnail; }

    public void setThumbnail(Bitmap image) {
        thumbnail = image;
    }

    public String getMpaaRating() { return mpaaRating; }

    public void setMpaaRating(String mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setGenres(List<String> list) {
        for (String genre : list) {
            genres.add(genre);
        }
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setCast(List<String> list) {
        for (String actor : list) {
            cast.add(actor);
        }
    }

    public List<String> getCast() {
        return cast;
    }

    public void setCriticReview(String criticReview) {
        this.criticReview = criticReview;
    }

    public String getCriticReview() {
        return criticReview;
    }

    public void setId(long id) { this.id = id; }

    public long getId() {
        return id;
    }
}
