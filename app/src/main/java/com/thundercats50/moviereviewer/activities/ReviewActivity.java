package com.thundercats50.moviereviewer.activities;

import android.os.UserManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.thundercats50.moviereviewer.R;
import com.thundercats50.moviereviewer.database.RepositoryConnector;
import com.thundercats50.moviereviewer.models.MemberManager;
import com.thundercats50.moviereviewer.models.MovieManager;
import com.thundercats50.moviereviewer.models.SingleMovie;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {
    private static final String TAG = "RecyclerViewExample";
    private static final String apiKey = "?apikey=yedukp76ffytfuy24zsqk7f5";
    private static final String baseURL = "http://api.rottentomatoes.com/api/public/v1.0/movies/";
    private final String jsonEnd = ".json?apikey=";
    private SingleMovie movie = MovieManager.movie;
    private List<SingleMovie> ratedMovies;
    private List<Integer> ratings;
    private List<String> reviews;
    private TextView name;
    private EditText review;
    private EditText movieRating;
    private MemberManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        name = (TextView) findViewById(R.id.movie_title);
        SingleMovie movie = MovieManager.movie;
        name.setText(movie.getTitle());
        manager = (MemberManager) getApplicationContext();
        getRating((int) movie.getId(), manager.getCurrentEmail());


    }
    public boolean addRating(int score, String review, int movieID, String username) {

        //TODO: Hook up to UI and getMovieByID method
        Exception error;
        try {
            RepositoryConnector rpc = new RepositoryConnector();
            boolean retVal = rpc.setRating(username, movieID, score, review);
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
    public void submitReview(View view){
        review = (EditText)findViewById(R.id.movie_review);
        String aReview = review.getText().toString();
        movieRating = (EditText)findViewById(R.id.movie_rating);
        String aRating = movieRating.getText().toString();
        int rating = Integer.parseInt(aRating);
        addRating(rating, aReview, (int)movie.getId(), manager.getCurrentEmail());

    }
}
