package com.thundercats50.moviereviewer.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.UserManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.thundercats50.moviereviewer.R;
import com.thundercats50.moviereviewer.database.BlackBoardConnector;
import com.thundercats50.moviereviewer.database.RepositoryConnector;
import com.thundercats50.moviereviewer.models.MemberManager;
import com.thundercats50.moviereviewer.models.MovieManager;
import com.thundercats50.moviereviewer.models.SingleMovie;
import com.thundercats50.moviereviewer.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {
    private static final String TAG = "RecyclerViewExample";
    private static final String apiKey = "?apikey=yedukp76ffytfuy24zsqk7f5";
    private static final String baseURL = "http://api.rottentomatoes.com/api/public/v1.0/movies/";
    private final String jsonEnd = ".json?apikey=";
    private SingleMovie movie = MovieManager.movie;
    private List<SingleMovie> ratedMovies;
    private List<Integer> ratings = new ArrayList();
    private List<String> reviews = new ArrayList();
    private TextView name;
    private EditText review;
    private EditText movieRating;
    private MemberManager manager;
    private UserReviewTask reviewTask = null;
    private UserReviewTask reviewTask2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        name = (TextView) findViewById(R.id.movie_title);
        SingleMovie movie = MovieManager.movie;
        name.setText(movie.getTitle());
        manager = (MemberManager) getApplicationContext();
        //getRating((int) movie.getId(), manager.getCurrentEmail());
        review = (EditText) findViewById(R.id.movie_review);
        movieRating = (EditText) findViewById(R.id.movie_rating);
        reviewTask = new UserReviewTask(null, 0, (int) movie.getId(), review, movieRating);

        reviewTask.execute();
        review = (EditText) findViewById(R.id.movie_review);
        movieRating = (EditText) findViewById(R.id.movie_rating);
        Log.d("The size of array is ", "" + reviews.size());
//        if (reviews.get(0) != null) {
//            review.setText(reviews.get(0));
//            movieRating.setText(ratings.get(0));
//        }
    }

    public boolean addRating(String email, int movieID, int score, String review) {

        //TODO: Hook up to UI and getMovieByID method
        Exception error;
        try {
            RepositoryConnector rpc = new RepositoryConnector();
            boolean retVal = rpc.setRating(email, movieID, score, review);
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

    public ArrayList getRating(int movieID, String email) {
        ArrayList aList = new ArrayList();
        Exception error;
        try {
            RepositoryConnector rpc = new RepositoryConnector();
            ResultSet retVal = rpc.getMovieRatings(movieID);
            Log.d("DB getRating finished", "doInBackground method returned:" + retVal.toString());
            if (retVal != null) {
                while (retVal.next()) {
                    //if (retVal.getString(1).equals(manager.getCurrentEmail()))
                    //Log.d("Checking for retVal", "the value at table is " + retVal.getInt(5));
                    //ratings.add(retVal.getInt(1));
                    aList.add(retVal.getString(4));
                    aList.add(retVal.getInt(3));

                    Log.d("Is this string stored", "plz help " + aList.size());
                    Log.d("Is this string stored", "plz help " + aList.get(0));
                    Log.d("Is this string stored", "plz help " + aList.get(1));

                }
                rpc.disconnect();
                return aList;

            }
        } catch (InputMismatchException imee) {
            error = imee;
            return aList;
        } catch (ClassNotFoundException cnfe) {
            Log.d("Dependency Error", "Check if MySQL library is present.");
            return aList;
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
            return aList;
        }
        return aList;
    }

    public void submitReview(View view) {
        review = (EditText) findViewById(R.id.movie_review);
        String aReview = review.getText().toString();
        movieRating = (EditText) findViewById(R.id.movie_rating);
        String aRating = movieRating.getText().toString();
        int rating = Integer.parseInt(aRating);
        reviewTask2 = new UserReviewTask(null, 0, (int) movie.getId(), review, movieRating);
        reviewTask2.updateVars(aReview, rating, (int) movie.getId());
        reviewTask2.execute();
        //addRating(rating, aReview, (int)movie.getId(), manager.getCurrentEmail());
        //reviewTask = new UserReviewTask(aReview, rating, (int) movie.getId());
        startActivity(new Intent(this, SearchActivity.class));
        //reviewTask.doInBackground();

    }

    public class UserReviewTask extends AsyncTask<Void, Void, ArrayList> {

        private String mReview;
        private int mRating;
        private int mId;
        private final MemberManager manager = (MemberManager) getApplicationContext();
        private boolean internetAccessExists = true;
        EditText review;
        EditText rating;

        UserReviewTask(String mReview, int mRating, int mId, EditText review, EditText rating) {
            this.mReview = mReview;
            this.mRating = mRating;
            this.mId = mId;
            this.review = review;
            this.rating = rating;
        }

        public void updateVars(String mReview, int mRating, int mId) {
            this.mReview = mReview;
            this.mRating = mRating;
            this.mId = mId;
        }
        @Override
        protected ArrayList doInBackground(Void... params) {
            //addRating(mRating, mReview, (int)movie.getId(), manager.getCurrentEmail());
            ArrayList arrayList = new ArrayList();
            Log.e("Got to section", "yay1");
            arrayList = getRating(mId, manager.getCurrentEmail());
            if (mReview != null) {
                addRating(manager.getCurrentEmail(), mId, mRating, mReview);
                Log.e("Got to section", "yay");
            }
            return arrayList;
        }
        protected void onPostExecute(ArrayList aList) {
            if (aList.size() > 0) {
                String aReview = ((String) aList.get(0));
                int aRating = ((Integer) aList.get(1));
                String aRating1 = Integer.toString(aRating);
                review.setText(aReview);
                rating.setText(aRating1);
            }
        }
    }

}
