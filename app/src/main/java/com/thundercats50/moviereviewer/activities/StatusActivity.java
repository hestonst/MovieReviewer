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
import android.widget.Button;

import com.thundercats50.moviereviewer.R;
import com.thundercats50.moviereviewer.database.BlackBoardConnector;
import com.thundercats50.moviereviewer.models.UserManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.concurrent.ExecutionException;

public class StatusActivity extends AppCompatActivity {

    /** UI reference
     *
     */
    private View mReviewFormView;
    /** UI reference
     *
     */
    private View mProgressView;
    /** UI reference
     *
     */
    private UserManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        manager = (UserManager) getApplicationContext();
        //getRating((int) movie.getId(), manager.getCurrentEmail());

        mReviewFormView = findViewById(R.id.status_form);
        mProgressView = findViewById(R.id.status_progress);

        final Button mBanButton = (Button) findViewById(R.id.ban);
        mBanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                banUser();
            }
        });

        final Button mUnbanButton = (Button) findViewById(R.id.unban);
        mUnbanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unbanUser();
            }
        });

        final Button mUnlockButton = (Button) findViewById(R.id.unlock);
        mUnlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unlockUser();
            }
        });



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
     * method to ban user
     */
    private void banUser() {
        showProgress(true);
        final BanUserTask banTask = new BanUserTask(manager);
        try {
            final boolean successfulFinish = banTask.execute().get();
            Log.d("Returned:", Boolean.toString(successfulFinish));
            if (successfulFinish) {
                finish();
            } else {
                showProgress(false);
            }
        } catch (ExecutionException except) {
            Log.d("Task Error", "Cannot create logged in view.");
        } catch (InterruptedException except) {
            Log.d("Task Error", "Cannot create logged in view.");
        }
    }

    public class BanUserTask extends AsyncTask<Void, Void, Boolean> {
        /**
         * user manager
         */
        private final UserManager manager;

        /**
         * ban user task
         * @param manager user manager
         */
        BanUserTask(UserManager manager) {
            this.manager = manager;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return banUser();
        }

        /**
         * ban user
         * @return boolean for success
         */
        public Boolean banUser() {
            BlackBoardConnector bbc;
            boolean retVal = false;
            try {
                bbc = new BlackBoardConnector();
                final UserManager manager = (UserManager) getApplicationContext();
                final String email = manager.getCurrentMember().getEmail();

                retVal = bbc.setBanned(email, true);
                Log.d("DB setBanned Finished", "doInBackground method returned: "
                        + Boolean.toString(retVal));
                bbc.disconnect();
                return retVal;
            } catch (InputMismatchException exception) {
                Log.d("Input Mismatch Error", "check message:" + exception.getMessage());
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
            } // finally {
            //    return retVal;
            //}
            return retVal;
        }


    }

    /**
     * unlock user
     */
    private void unlockUser() {
        showProgress(true);
        final UnlockUserTask unlockTask = new UnlockUserTask(manager);
        try {
            final boolean successfulFinish = unlockTask.execute().get();
            Log.d("Returned:", Boolean.toString(successfulFinish));
            if (successfulFinish) {
                finish();
            } else {
                showProgress(false);
            }
        } catch (ExecutionException except) {
            Log.d("Task Error", "Cannot create logged in view.");
        } catch (InterruptedException except) {
            Log.d("Task Error", "Cannot create logged in view.");
        }
    }

    public class UnlockUserTask extends AsyncTask<Void, Void, Boolean> {
        /**
         * user manager
         */
        private final UserManager manager;

        /**
         * unlock user task
         * @param manager user manager
         */
        UnlockUserTask(UserManager manager) {
            this.manager = manager;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return unlockUser();
        }

        /**
         * unlock user
         * @return boolean success code
         */
        public Boolean unlockUser() {
            BlackBoardConnector bbc;
            boolean retVal = false;
            try {
                bbc = new BlackBoardConnector();
                final UserManager manager = (UserManager) getApplicationContext();
                final String email = manager.getCurrentMember().getEmail();

                retVal = bbc.resetLoginAttempts(email);
                Log.d("DB reset Finished", "doInBackground method returned: "
                        + Boolean.toString(retVal));
                bbc.disconnect();
                return retVal;
            } catch (InputMismatchException exception) {
                Log.d("Input Mismatch Error", "check message:" + exception.getMessage());
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
            } // finally {
            //    return retVal;
            //}
            return retVal;
        }
    }

    /**
     * unban user
     */
    private void unbanUser() {
        showProgress(true);
        final UnbanUserTask unbanTask = new UnbanUserTask(manager);
        try {
            final boolean successfulFinish = unbanTask.execute().get();
            Log.d("Returned:", Boolean.toString(successfulFinish));
            if (successfulFinish) {
                finish();
            } else {
                showProgress(false);
            }
        } catch (ExecutionException except) {
            Log.d("Task Error", "Cannot create logged in view.");
        } catch (InterruptedException except) {
            Log.d("Task Error", "Cannot create logged in view.");
        }
    }

    public class UnbanUserTask extends AsyncTask<Void, Void, Boolean> {
        /**
         * user manager
         */
        private final UserManager manager;

        /**
         * unban user task
         * @param manager user manager
         */
        UnbanUserTask(UserManager manager) {
            this.manager = manager;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return unbanUser();
        }

        /**
         * unban user
         * @return boolean for success
         */
        public Boolean unbanUser() {
            BlackBoardConnector bbc;
            boolean retVal = false;
            try {
                bbc = new BlackBoardConnector();
                final UserManager manager = (UserManager) getApplicationContext();
                final String email = manager.getCurrentMember().getEmail();

                retVal = bbc.setBanned(email, false);
                Log.d("DB setBanned Finished", "doInBackground method returned: "
                        + Boolean.toString(retVal));
                bbc.disconnect();
                return retVal;
            } catch (InputMismatchException exception) {
                Log.d("Input Mismatch Error", "error thrown");
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
            } // finally {
            //    return retVal;
            //}
            return retVal;
        }

    }
}
