package com.thundercats50.moviereviewer.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thundercats50.moviereviewer.R;
import com.thundercats50.moviereviewer.database.BlackBoardConnector;
import com.thundercats50.moviereviewer.models.UserManager;
import com.thundercats50.moviereviewer.models.User;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class ProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserDataWriteTask mWriteTask = null;

    /**
     * user manager
     */
    private UserManager manager;
    /**
     * user
     */
    private User member;

    /**
     * UI reference
     */
    private View mProgressView;
    /**
     * UI reference
     */
    private View mLoginFormView;
    /**
     * first name text box
     */
    private EditText mFirstNameView;
    /**
     * last name text box
     */
    private EditText mLastNameView;
    /**
     * email text box
     */
    private EditText mEmailView;
    /**
     * gender text box
     */
    private EditText mGenderView;
    /**
     * major text box
     */
    private EditText mMajorView;
    /**
     * password view
     */
    private EditText mPasswordView;
    /**
     * verify password text box
     */
    private EditText mVerifyPasswordView;
    /**
     * old password text box
     */
    private EditText mOldPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = (UserManager) getApplicationContext();
        member = manager.getCurrentMember();

        setContentView(R.layout.activity_profile);

        // Set up the form.
        mFirstNameView = (EditText) findViewById(R.id.editFirstName);
        mLastNameView = (EditText) findViewById(R.id.editLastName);
        mEmailView = (EditText) findViewById(R.id.editEmail);
        mGenderView = (EditText) findViewById(R.id.editGender);
        mMajorView = (EditText) findViewById(R.id.editMajor);
        mPasswordView = (EditText) findViewById(R.id.editPassword);
        mVerifyPasswordView = (EditText) findViewById(R.id.editVerifyPassword);
        mOldPasswordView = (EditText) findViewById(R.id.editOldPassword);

        //set the user's email for them; is not changeable
        mEmailView.setText((manager.getCurrentMember()).getEmail(), TextView.BufferType.NORMAL);

        //set other data, if it exists
        if (!member.getFirstname().equals("")) {
            mFirstNameView.setText(member.getFirstname(), TextView.BufferType.EDITABLE);
        }
        if (!member.getLastname().equals("")) {
            mLastNameView.setText(member.getLastname(), TextView.BufferType.EDITABLE);
        }
        if (!member.getGender().equals("")) {
            mGenderView.setText(member.getGender(), TextView.BufferType.EDITABLE);
        }
        if (!member.getMajor().equals("")) {
            mMajorView.setText(member.getMajor(), TextView.BufferType.EDITABLE);
        }


        mLoginFormView = findViewById(R.id.edit_info_form);
        mProgressView = findViewById(R.id.edit_info_progress);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.editVerifyPassword || id == EditorInfo.IME_NULL) {
                    attemptWrite();
                    return true;
                }
                return false;
            }
        });

        Button mWriteDataButton = (Button) findViewById(R.id.update_info_button);
        mWriteDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptWrite();
            }
        });

        mLoginFormView = findViewById(R.id.email_login_form);
        mProgressView = findViewById(R.id.edit_info_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptWrite() {
        if (mWriteTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mEmailView.setError(null);
        mGenderView.setError(null);
        mMajorView.setError(null);
        mPasswordView.setError(null);
        mVerifyPasswordView.setError(null);
        mOldPasswordView.setError(null);


        // Store values at the time of the login attempt.
        final String firstName = mFirstNameView.getText().toString();
        final String lastName = mLastNameView.getText().toString();
        final String email = manager.getCurrentMember().getEmail();
        final String gender = mGenderView.getText().toString();
        final String major = mMajorView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String verifyPassword = mVerifyPasswordView.getText().toString();
        final String oldPassword = mOldPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (!verifyPassword.equals(password)) {
            mVerifyPasswordView.setError(getString(R.string.passwords_dont_match));
            focusView = mVerifyPasswordView;
            cancel = true;
        }
        if (!isPasswordValid(oldPassword)) {
            mOldPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView = mOldPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            showProgress(true);
            mWriteTask = new UserDataWriteTask(firstName, lastName, email, gender, major, password,
                    verifyPassword, oldPassword);
            mWriteTask.execute();


            try {
                if (!mWriteTask.get()) {
                    cancel = true;
                    mOldPasswordView.setError(getString(R.string.error_incorrect_password));
                }
            } catch (ExecutionException exception) {
                Log.d("Execution Exception", "Error thrown");
            } catch (InterruptedException exception) {
                Log.d("Interrupted Exception", "Error thrown");
            }

            if (!cancel) {
                finish();
            }
        }
    }

    /**
     * cancel button
     * @param view profile screen
     */
    public void cancel(View view) {
        finish();
    }

    /**
     * checks if password entered is valid
     * @param password password entered
     * @return boolean
     */
    private boolean isPasswordValid(String password) {
        return (password.matches("[a-zA-Z0-9]+") && password.length() >= 6);
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
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }



    private interface ProfileQuery {
        /**
         * array projection
         */
        String[] PROJECTION = {ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract.CommonDataKinds.Email.IS_PRIMARY,};
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserDataWriteTask extends AsyncTask<Void, Void, Boolean> {

        /**
         * email
         */
        private final String mEmail;
        /**
         * password
         */
        private final String mPassword;
        /**
         * old password
         */
        private final String mOldPassword;
        /**
         * gender
         */
        private String mGender;
        /**
         * first name
         */
        private String mFirstName;
        /**
         * last name
         */
        private String mLastName;
        /**
         * major
         */
        private String mMajor;
        /**
         * user manager
         */
        private final UserManager manager = (UserManager) getApplicationContext();
        /**
         * internet access
         */
        private boolean internetAccessExists = true;

        /**
         * write user data
         * @param firstName first name
         * @param lastName last name
         * @param email email
         * @param gender gender
         * @param major major
         * @param password password
         * @param verifyPassword verify password
         * @param oldPassword old password
         */
        UserDataWriteTask(String firstName, String lastName, String email, String gender,
                          String major, String password, String verifyPassword, String oldPassword) {
            mFirstName = firstName;
            mLastName = lastName;
            mMajor = major;
            mGender = gender;
            mEmail = email;
            mPassword = password;
            mOldPassword = oldPassword;
        }

        @Override
        protected Boolean doInBackground(Void...params) {
            BlackBoardConnector bbc = null;
            try {
                Log.d("Test", "Reached -1.");
                bbc = new BlackBoardConnector();
                Log.d("Test", "Reached 0.");
                BlackBoardConnector.UserStatus check = bbc.verifyUser(mEmail, mOldPassword);
                if (!check.equals(BlackBoardConnector.UserStatus.VERIFIED)) {
                    return false; //old password was not correct
                }
                Log.d("Test", "Reached 1.");
                boolean retVal = bbc.setUserData(mFirstName, mLastName, mMajor, mGender, mEmail);
                Log.d("Test", "Reached 2.");
                boolean retVal2 = true;
                if (!mOldPassword.equals(mPassword) && !mPassword.equals("")) {
                    retVal2 = bbc.changePass(mEmail, mPassword, mOldPassword);
                    Log.d("Test", "Reached 3.");
                }
                bbc.disconnect();
                manager.getCurrentMember().setMajor(mMajor);
                manager.getCurrentMember().setFirstname(mFirstName);
                manager.getCurrentMember().setLastname(mLastName);
                manager.getCurrentMember().setGender(mGender);
                return retVal && retVal2;
            }  catch (SQLException sqlException) {
                Log.d("Connection Error", "Check internet for MySQL access." + sqlException.getMessage() + sqlException.getSQLState());
                internetAccessExists = false;
                cancel(true);
            } finally {
                bbc.disconnect();
                /*
                try {
                    bbc.disconnect();
                } catch (NullPointerException exception) {
                    Log.d("Null Pointer Exception", "Thrown null pointer exception");
                }
                */
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mWriteTask = null;
            showProgress(false);

            if (success) {
                Log.d("Debug", "Reached 6");
                finish();
            } else {
                if (internetAccessExists) {
                    mOldPasswordView.setError(getString(R.string.error_incorrect_password));
                    mOldPasswordView.requestFocus();
                } else {
                    mPasswordView.setError(getString(R.string.no_internet));
                    mPasswordView.requestFocus();
                }
                Log.d("Debug", "Reached 7");

            }
        }

        @Override
        protected void onCancelled() {
            mWriteTask = null;
            showProgress(false);
        }
    }
}

