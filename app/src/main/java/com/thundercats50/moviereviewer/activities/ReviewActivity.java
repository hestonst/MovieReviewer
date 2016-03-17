package com.thundercats50.moviereviewer.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.thundercats50.moviereviewer.R;
import com.thundercats50.moviereviewer.database.BlackBoardConnector;
import com.thundercats50.moviereviewer.database.RepositoryConnector;
import com.thundercats50.moviereviewer.models.UserManager;
import com.thundercats50.moviereviewer.models.MovieManager;
import com.thundercats50.moviereviewer.models.SingleMovie;
import com.thundercats50.moviereviewer.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
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
    private EditText mReviewView;
    private EditText mRatingView;
    private UserManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        name = (TextView) findViewById(R.id.movie_title);
        mReviewView = (EditText) findViewById(R.id.movie_review);
        mRatingView = (EditText) findViewById(R.id.movie_rating);
        SingleMovie movie = MovieManager.movie;
        name.setText(movie.getTitle());
        manager = (UserManager) getApplicationContext();
        //getRating((int) movie.getId(), manager.getCurrentEmail());
        UserReviewTask reviewTask = new UserReviewTask(null, 0, movie.getId(), mReviewView, mRatingView);
        reviewTask.execute();
        //getRating(movie.getId(), manager.getCurrentMember().getEmail());

    }

    public void addRating(View view) {
        AddReviewTask ratingTask = new AddReviewTask(manager, mReviewView, mRatingView);
        ratingTask.execute();
        startActivity(new Intent(this, SearchActivity.class));
    }


    public class AddReviewTask extends AsyncTask<Void, Void, Void> {
        private UserManager manager;
        private EditText review;
        private EditText rating;

        AddReviewTask(UserManager manager, EditText review, EditText rating) {
            this.manager = manager;
            this.review = review;
            this.rating = rating;
        }

        @Override
        protected Void doInBackground(Void... params) {
            return addRating();
        }

        public Void addRating() {
            Exception error;
            try {
                RepositoryConnector rpc = new RepositoryConnector();
                UserManager manager = (UserManager) getApplicationContext();
                String email = manager.getCurrentMember().getEmail();
                EditText review = (EditText) findViewById(R.id.movie_review);
                EditText rating = (EditText) findViewById(R.id.movie_rating);
                boolean retVal = rpc.setRating(email, movie, Integer.parseInt(rating.getText().toString()), review.getText().toString());
                Log.d("DB setRating Finished", "doInBackground method returned: "
                        + Boolean.toString(retVal));
                rpc.disconnect();
            } catch (InputMismatchException imee) {
                error = imee;
            } catch (ClassNotFoundException cnfe) {
                Log.d("Dependency Error", "Check if MySQL library is present.");
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
            }
            return null;
        }
    }


    public class UserReviewTask extends AsyncTask<Void, Void, Boolean> {

        private String mReview;
        private int mRating;
        private long mId;
        private final UserManager manager = (UserManager) getApplicationContext();
        private boolean internetAccessExists = true;
        private SingleMovie movie;

        private EditText mReviewView;
        private EditText mRatingView;


        UserReviewTask(String mReview, int mRating, long mId, EditText mReviewView,
                       EditText mRatingView) {
            this.mReview = mReview;
            this.mRating = mRating;
            this.mId = mId;
            this.mReviewView = mReviewView;
            this.mRatingView = mRatingView;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Exception error;
            long movieID = mId;
            String email = manager.getCurrentMember().getEmail();

            try {
                RepositoryConnector rpc = new RepositoryConnector();
                movie = rpc.getRating(email, movieID);
                //Log.d("Contains Tag", Boolean.toString(movie.hasRatingByUser(email)));
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

        @Override
        protected void onPostExecute(final Boolean success) {
            String email = manager.getCurrentMember().getEmail();
            if (movie != null && movie.hasRatingByUser(email)) {
                mReviewView.setText(movie.getUserReview(email));
                mRatingView.setText(Integer.toString(movie.getUserRating(email)));
            }
        }


    }
}
