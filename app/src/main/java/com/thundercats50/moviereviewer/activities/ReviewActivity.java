package com.thundercats50.moviereviewer.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.thundercats50.moviereviewer.R;
import com.thundercats50.moviereviewer.database.RepositoryConnector;
import com.thundercats50.moviereviewer.models.UserManager;
import com.thundercats50.moviereviewer.models.MovieManager;
import com.thundercats50.moviereviewer.models.SingleMovie;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.concurrent.ExecutionException;

public class ReviewActivity extends AppCompatActivity {

    /**
     * current movie
     */
    private final SingleMovie movie = MovieManager.movie;
    /**
     * review text box
     */
    private EditText mReviewView;
    /**
     * rating text box
     */
    private EditText mRatingView;
    /**
     * view review
     */
    private View mReviewFormView;
    /**
     * progress view
     */
    private View mProgressView;
    /**
     * user manager
     */
    private UserManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        final TextView name = (TextView) findViewById(R.id.movie_title);
        mReviewView = (EditText) findViewById(R.id.movie_review);
        mRatingView = (EditText) findViewById(R.id.movie_rating);
        mReviewFormView = findViewById(R.id.rating_form);
        mProgressView = findViewById(R.id.rating_progress);
        final SingleMovie movie = MovieManager.movie;
        name.setText(movie.getTitle());
        manager = (UserManager) getApplicationContext();
        //getRating((int) movie.getId(), manager.getCurrentEmail());
        final UserReviewTask reviewTask = new UserReviewTask(null, 0, movie.getId(), mReviewView, mRatingView);
        reviewTask.execute();
        //getRating(movie.getId(), manager.getCurrentMember().getEmail());

    }

    /**
     * Shows the progress UI and hides the login form.
     * @param show android show
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            final int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mReviewFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mReviewFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mReviewFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mReviewFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * add a rating
     * @param view of the rating screen
     */
    public void addRating(View view) {
        showProgress(true);
        AddReviewTask ratingTask = new AddReviewTask(manager, mReviewView, mRatingView);
        try {
            boolean successfulFinish = ratingTask.execute().get();
            Log.d("Returned:", Boolean.toString(successfulFinish));
            if (successfulFinish) {
                finish();
            } else {
                showProgress(false);
                mRatingView.setError(getString(R.string.rating_range));
                mRatingView.requestFocus();
            }
        } catch (ExecutionException exception) {
            Log.d("Execution Exception", "Error thrown");
        } catch (InterruptedException exception) {
            Log.d("Interrupted Exception", "Error thrown");
        }
    }

    public class AddReviewTask extends AsyncTask<Void, Void, Boolean> {

        /**
         * user manager
         */
        private final UserManager manager;
        /**
         * review text box
         */
        private final EditText review;
        /**
         * rating text box
         */
        private final EditText rating;

        /**
         * review task
         * @param manager user manager
         * @param review review written
         * @param rating rating given
         */
        AddReviewTask(UserManager manager, EditText review, EditText rating) {
            this.manager = manager;
            this.review = review;
            this.rating = rating;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return addRating();
        }

        /**
         * add rating to movie
         * @return boolean for success
         */
        public Boolean addRating() {
            RepositoryConnector rpc;
            try {
//                if (rating.getText().toString().equals("")) {
//                    throw new InputMismatchException("Ratings are required.");
//                }
                rpc = new RepositoryConnector();
                final UserManager manager = (UserManager) getApplicationContext();
                final String email = manager.getCurrentMember().getEmail();
                final EditText review = (EditText) findViewById(R.id.movie_review);
                final EditText rating = (EditText) findViewById(R.id.movie_rating);
                Log.d("Int Passed to DB:", rating.getText().toString());
                rpc.setRating(email, movie, Integer
                        .parseInt(rating.getText().toString()), review.getText().toString());
                rpc.disconnect();
            } catch (InputMismatchException exception) {
                Log.d("Input Mismatch Error", "Error thrown");
            } catch (SQLException sqlException) {
                Log.d("Connection Error", "Check internet for MySQL access." + sqlException.getMessage() + sqlException.getSQLState());
                for (Throwable e : sqlException) {
                    e.printStackTrace(System.err);
                    Log.d("Connection Error", "SQLState: " +
                            ((SQLException) e).getSQLState());

                    Log.d("Connection Error", "Error Code: " +
                            ((SQLException) e).getErrorCode());

                    Log.d("Connection Error", "Message: " + e.getMessage());

                    Throwable t = sqlException.getCause();
                    while (t != null) {
                        Log.d("Connection Error", "Cause: " + t);
                        t = t.getCause();
                    }
                }
            }
            return true;
        }


    }


    public class UserReviewTask extends AsyncTask<Void, Void, Boolean> {

        /**
         * review
         */
        private final String mReview;
        /**
         * rating of movie
         */
        private final int mRating;
        /**
         * id of movie
         */
        private final long mId;
        /**
         * user manager
         */
        private final UserManager manager = (UserManager) getApplicationContext();
        /**
         * movie
         */
        private SingleMovie movie;
        /**
         * review text box
         */
        private final EditText mReviewView;
        /**
         * rating text box
         */
        private final EditText mRatingView;

        /**
         * user review task
         * @param mReview user review
         * @param mRating user rating
         * @param mId movie id
         * @param mReviewView view of review
         * @param mRatingView view of rating
         */
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
            final long movieID = mId;
            final String email = manager.getCurrentMember().getEmail();

            try {
                final RepositoryConnector rpc = new RepositoryConnector();
                movie = rpc.getRating(email, movieID);
                //Log.d("Contains Tag", Boolean.toString(movie.hasRatingByUser(email)));
                rpc.disconnect();
            } catch (InputMismatchException exception) {
                return false;
            } catch (SQLException sqlException) {
                Log.d("Connection Error", "Check internet for MySQL access." + sqlException.getMessage() + sqlException.getSQLState());
                for (Throwable e : sqlException) {
                    e.printStackTrace(System.err);
                    Log.d("Connection Error", "SQLState: " +
                            ((SQLException) e).getSQLState());

                    Log.d("Connection Error", "Error Code: " +
                            ((SQLException) e).getErrorCode());

                    Log.d("Connection Error", "Message: " + e.getMessage());

                    Throwable t = sqlException.getCause();
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
            final String email = manager.getCurrentMember().getEmail();
            if (movie != null && movie.hasRatingByUser(email)) {
                mReviewView.setText(movie.getUserReview(email));
                mRatingView.setText(Integer.toString(movie.getUserRating(email)));
            }
        }


    }
}
