package com.thundercats50.moviereviewer.models;

import android.app.Application;


public class MovieManager extends Application{
    public static SingleMovie movie;
    public SingleMovie getCurrentMovie() {
        return movie;
    }
    public void setCurrentMovie(SingleMovie currentMovie) {
        movie = currentMovie;
    }

}
