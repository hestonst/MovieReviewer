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

public class ReviewActivity extends AppCompatActivity {

    private final SingleMovie movie = MovieManager.movie;
    private EditText mReviewView;
    private EditText mRatingView;
    private View mReviewFormView;
    private View mProgressView;
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
     * method to add rating to movie
     * @param view review page
     */
    public void addRating(View view) {
        showProgress(true);
        final AddReviewTask ratingTask = new AddReviewTask(manager, mReviewView, mRatingView);
        try {
            final boolean successfulFinish = ratingTask.execute().get();
            Log.d("Returned:", Boolean.toString(successfulFinish));
            if (successfulFinish) {
                finish();
            } else {
                showProgress(false);
                mRatingView.setError(getString(R.string.rating_range));
                mRatingView.requestFocus();
            }
        } catch (Exception e) {
            Log.d("Write to DB", "Concurrent exception in ReviewAct");
        }
    }


    public class AddReviewTask extends AsyncTask<Void, Void, Boolean> {

        private final UserManager manager;
        private final EditText review;
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
            boolean retVal = false;
            Exception error;
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
                retVal = rpc.setRating(email, movie, Integer
                        .parseInt(rating.getText().toString()), review.getText().toString());
                Log.d("DB setRating Finished", "doInBackground method returned: "
                        + Boolean.toString(retVal));
                rpc.disconnect();
                return retVal;
            } catch (InputMismatchException imee) {
                error = imee;
            } catch (ClassNotFoundException cnfe) {
                error = cnfe;
                Log.d("Dependency Error", "Check if MySQL library is present.");
            } catch (SQLException sqle) {
                error = sqle;
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
            }// finally {
            //    return retVal;
            //}
            return retVal;
        }


    }


    public class UserReviewTask extends AsyncTask<Void, Void, Boolean> {

        private final String mReview;
        private final int mRating;
        private final long mId;
        private final UserManager manager = (UserManager) getApplicationContext();
        private boolean internetAccessExists = true;
        private SingleMovie movie;

        private final EditText mReviewView;
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
            } catch (InputMismatchException imee) {
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
            final String email = manager.getCurrentMember().getEmail();
            if (movie != null && movie.hasRatingByUser(email)) {
                mReviewView.setText(movie.getUserReview(email));
                mRatingView.setText(Integer.toString(movie.getUserRating(email)));
            }
        }


    }
}
