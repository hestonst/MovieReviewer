
/**
 * Created by Tom on 2/24/2016.
 */
package com.thundercats50.moviereviewer.json;

import com.thundercats50.moviereviewer.models.SingleMovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.thundercats50.moviereviewer.json.Keys.KEY_AUDIENCE_SCORE;
import static com.thundercats50.moviereviewer.json.Keys.KEY_CAST;
import static com.thundercats50.moviereviewer.json.Keys.KEY_ID;
import static com.thundercats50.moviereviewer.json.Keys.KEY_LINKS;
import static com.thundercats50.moviereviewer.json.Keys.KEY_MOVIES;
import static com.thundercats50.moviereviewer.json.Keys.KEY_POSTERS;
import static com.thundercats50.moviereviewer.json.Keys.KEY_RATINGS;
import static com.thundercats50.moviereviewer.json.Keys.KEY_RELEASE_DATES;
import static com.thundercats50.moviereviewer.json.Keys.KEY_REVIEWS;
import static com.thundercats50.moviereviewer.json.Keys.KEY_SELF;
import static com.thundercats50.moviereviewer.json.Keys.KEY_SIMILAR;
import static com.thundercats50.moviereviewer.json.Keys.KEY_SYNOPSIS;
import static com.thundercats50.moviereviewer.json.Keys.KEY_THEATER;
import static com.thundercats50.moviereviewer.json.Keys.KEY_THUMBNAIL;
import static com.thundercats50.moviereviewer.json.Keys.KEY_TITLE;
public class Parser {
    public static ArrayList<SingleMovie> parseMoviesJSON(JSONObject response) {
        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
        ArrayList<SingleMovie> listMovies = new ArrayList<>();
        if (response != null && response.length() > 0) {
            try {
                JSONArray arrayMovies = response.getJSONArray(KEY_MOVIES);
                for (int i = 0; i < arrayMovies.length(); i++) {
                    long id = -1;
                    String title = "";
                    String releaseDate = "";
                    int audienceScore = -1;
                    JSONObject currentMovie = arrayMovies.getJSONObject(i);

                    if (Utils.contains(currentMovie, KEY_ID)) {
                        id = currentMovie.getLong(KEY_ID);
                    }

                    if (Utils.contains(currentMovie, KEY_TITLE)) {
                        title = currentMovie.getString(KEY_TITLE);
                    }

                    if (Utils.contains(currentMovie, KEY_RELEASE_DATES)) {
                        JSONObject releaseDates = currentMovie.getJSONObject(KEY_RELEASE_DATES);

                        if (Utils.contains(releaseDates, KEY_THEATER)) {
                            releaseDate = releaseDates.getString(KEY_THEATER);
                        }
                    }
                    SingleMovie movie = new SingleMovie();
                    movie.setId(id);
                    movie.setTitle(title);
                    Date date = null;
                    try {
                        date = dateFormat.parse(releaseDate);
                    } catch (ParseException e) {
                        //handle null release date
                    }
                    movie.setReleaseDate(date);
                    //add more variables here once we figure out how it all comes together

                    //check to make sure id and title
                    if (id != -1 && !title.equals(null)) {
                        listMovies.add(movie);
                    }
                }
            } catch (JSONException e) {
                //handle json exception
            }
        }
        return listMovies;
    }
}
