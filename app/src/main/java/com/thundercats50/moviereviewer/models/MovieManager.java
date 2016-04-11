package com.thundercats50.moviereviewer.models;

import android.app.Application;


public class MovieManager extends Application{
    public static SingleMovie movie;

    /**
     * get the current movie object being modified
     * @return the current movie object
     */
    public SingleMovie getCurrentMovie() {
        return movie;
    }

    /**
     * set the current movie object to be modified
     * @param currentMovie the movie to be modified
     */
    public void setCurrentMovie(SingleMovie currentMovie) {
        movie = currentMovie;
    }

}
