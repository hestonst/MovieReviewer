package com.thundercats50.moviereviewer.activities;
import com.thundercats50.moviereviewer.database.RepositoryConnector;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.thundercats50.moviereviewer.R;
import com.thundercats50.moviereviewer.database.BlackBoardConnector;
import com.thundercats50.moviereviewer.database.DBConnector;
import com.thundercats50.moviereviewer.models.MemberManager;
import com.thundercats50.moviereviewer.models.SingleMovie;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;


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
     * This method allows looking up movies by ID
     * @param queue the volley queue
     * @param id the movie id
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

    public boolean addRating(int score, String review, long movieID, String username) {

        //TODO: Hook up to UI and getMovieByID method
        Exception error;
        try {
            RepositoryConnector rpc = new RepositoryConnector();
            boolean retVal = rpc.setRating(username, movieID, score, review);
            Log.d("DB setRating Finished", "doInBackground method returned: "
                    + Boolean.toString(retVal));
            rpc.disconnect();
            return retVal;
        } catch(InputMismatchException imee) {
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
                while(t != null) {
                    Log.d("Connection Error", "Cause: " + t);
                    t = t.getCause();
                }
            }

            return false;
        }
    }
}