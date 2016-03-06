package com.thundercats50.moviereviewer.activities;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.thundercats50.moviereviewer.database.BlackBoardConnector;
import com.thundercats50.moviereviewer.database.DBConnector;
import com.thundercats50.moviereviewer.models.SingleMovie;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;

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


    /**
     * The meat of the JSON handling. Takes the API call, gets the JSON response, parses it,
     * creates a movie object, and sets its Title and Thumbnail attributes
     * @param queue the volley queue
     * @param id the movie id
     */
    private void runQuery(RequestQueue queue, String id) {
        String query = baseURL + id + jsonEnd + apiKey;
        Log.d(TAG, query);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, query, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());

                // Parse json data.
                // Declare the json objects that we need and then for loop through the children array.
                // Do the json parse in a try catch block to catch the exceptions
                try {
                    //JSONObject data = response.getJSONObject();
                    //after_id = data.getString("after");
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

    public static void addRating(int score, String review, String movieID, String username) {

        PreparedStatement preparedStatement = null;
        // TODO: connect to database and add rating based on movieID
    }

}
