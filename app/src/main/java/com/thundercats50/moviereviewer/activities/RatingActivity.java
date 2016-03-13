package com.thundercats50.moviereviewer.activities;

import com.thundercats50.moviereviewer.database.RepositoryConnector;
import com.thundercats50.moviereviewer.models.SingleMovie;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import java.sql.SQLException;

import java.util.InputMismatchException;
import java.util.List;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by neilbarooah on 05/03/16.
 */
public class RatingActivity {

    private static final String TAG = "RecyclerViewExample";
    private static final String apiKey = "?apikey=yedukp76ffytfuy24zsqk7f5";
    private static final String baseURL = "http://api.rottentomatoes.com/api/public/v1.0/movies/";
    private final String jsonEnd = ".json?apikey=";
    private SingleMovie movie;
    private List<SingleMovie> ratedMovies;
    private List<Integer> ratings;
    private List<String> reviews;

    /**
     * This method allows looking up movies by ID
     *
     * @param queue the volley queue
     * @param id    the movie id
     */
    private void getMovieByID(RequestQueue queue, String id) {
        String query = baseURL + id + jsonEnd + apiKey;
        Log.d(TAG, query);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, query, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());

                // Parse json data.
                // Do the json parse in a try catch block to catch the exceptions
                try {

                    JSONArray title = response.getJSONArray("movies");
                    JSONObject currentMovie = title.getJSONObject(0);
                    SingleMovie item = new SingleMovie();
                    item.setTitle(currentMovie.getString("title"));
                    movie = item;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        queue.add(jsObjRequest);
    }

    public boolean addRating(int score, String review, int movieID, String username) {

        //TODO: Hook up to UI and getMovieByID method
        Exception error;
        try {
            RepositoryConnector rpc = new RepositoryConnector();
            String imageURL = ""; //TODO: pass url in from JSON object
            boolean retVal = rpc.setRating(username, movieID, score, review, imageURL);
            Log.d("DB setRating Finished", "doInBackground method returned: "
                    + Boolean.toString(retVal));
            rpc.disconnect();
            return retVal;
        } catch (InputMismatchException imee) {
            error = imee;
            return false;
        } catch (ClassNotFoundException cnfe) {
            Log.d("Dependency Error", "Check if MySQL library is present.");
            return false;
        } catch (SQLException sqle) {
            Log.d("Connection Error", "Check internet for MySQL access." + sqle.getMessage() + sqle.getSQLState());
            for (Throwable e : sqle) {
                e.printStackTrace(System.err);
                Log.d("Connection Error", "SQLState: " +
                        ((SQLException) e).getSQLState());

                Log.d("Connection Error", "Error Code: " +
                        ((SQLException) e).getErrorCode());

                Log.d("Connection Error", "Message: " + e.getMessage());

                Throwable t = sqle.getCause();
                while (t != null) {
                    Log.d("Connection Error", "Cause: " + t);
                    t = t.getCause();
                }
            }

            return false;
        }
    }

    public boolean getRating(int movieID, String username) {

        Exception error;
        try {
            RepositoryConnector rpc = new RepositoryConnector();
            ResultSet retVal = rpc.getMovieRatings(movieID);
            Log.d("DB getRating finished", "doInBackground method returned: ");
            while (retVal.next()) {
                ratings.add(retVal.getInt("NumericalRating"));
                reviews.add(retVal.getString("TextReview"));
            }
            rpc.disconnect();
        } catch (InputMismatchException imee) {
            error = imee;
            return false;
        } catch (ClassNotFoundException cnfe) {
            Log.d("Dependency Error", "Check if MySQL library is present.");
            return false;
        } catch (SQLException sqle) {
            Log.d("Connection Error", "Check internet for MySQL access." + sqle.getMessage() + sqle.getSQLState());
            for (Throwable e : sqle) {
                e.printStackTrace(System.err);
                Log.d("Connection Error", "SQLState: " +
                        ((SQLException) e).getSQLState());

                Log.d("Connection Error", "Error Code: " +
                        ((SQLException) e).getErrorCode());

                Log.d("Connection Error", "Message: " + e.getMessage());

                Throwable t = sqle.getCause();
                while (t != null) {
                    Log.d("Connection Error", "Cause: " + t);
                    t = t.getCause();
                }
            }
            return false;
        }
        return false;
    }
}