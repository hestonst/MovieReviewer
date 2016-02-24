package com.thundercats50.moviereviewer.json;

import com.thundercats50.moviereviewer.models.SingleMovie;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Tom on 2/24/2016.
 */
public class Parser {
    public static ArrayList<SingleMovie> parseMoviesJSON(JSONObject response) {
        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
        ArrayList<SingleMovie> listMovies = new ArrayList<>();
    }
}
