package com.thundercats50.moviereviewer.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;

import com.thundercats50.moviereviewer.R;
import com.thundercats50.moviereviewer.activities.LoggedInActivity;
import com.thundercats50.moviereviewer.database.BlackBoardConnector;
import com.thundercats50.moviereviewer.models.MemberManager;
import com.thundercats50.moviereviewer.models.User;

import java.sql.SQLException;

public class ProfileActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText gender;
    private EditText major;
    //private EditText password;
    //private EditText oldPassword;
    private UpdatePassword mUpdateTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    public void updateProfile(View view) {
        MemberManager manager = (MemberManager) getApplicationContext();
        User member = (User) manager.getCurrentMember();
        name = (EditText) findViewById(R.id.editName);
        email = (EditText) findViewById(R.id.editEmail);
        gender = (EditText) findViewById(R.id.editGender);
        major = (EditText) findViewById(R.id.editMajor);
        member.setFirstname(name.getText().toString());
        member.setEmail(email.getText().toString());
        member.setGender(gender.getText().toString());
        member.setMajor(major.getText().toString());
        startActivity(new Intent(this, LoggedInActivity.class));
        //String oldPasswordInput = oldPassword.getText().toString();
        //String passwordInput = password.getText().toString();
//        if (passwordInput.equals("")) {
//            startActivity(new Intent(this, LoggedInActivity.class));
//        } else {
//            View focusView = null;
//            boolean cancel = false;
//            if (!isPasswordValid(passwordInput)) {
//                password.setError("Please input a 6 character alphanumeric password");
//                focusView = password;
//                cancel = true;
//            }
//            if (cancel) {
//                focusView.requestFocus();
//            } else {
//                mUpdateTask = new UpdatePassword(manager.getCurrentEmail(), passwordInput, oldPasswordInput);
//                mUpdateTask.execute();
//                Log.d("The method is working", "The method is working");
//                boolean b = mUpdateTask.doInBackground();
//                if (b) {
//                    startActivity(new Intent(this, LoggedInActivity.class));
//                } else {
//                    email.setError("An error occured. Please check the log");
//                    email.requestFocus();
//                }
//            }
//        }
    }

    /**
     * Uses RegEx to verify pass; must be more than 6 chars and alphnumeric
     *
     * @param password
     * @return ifValid
     */
    private boolean isPasswordValid(String password) {
        return (password.matches("[a-zA-Z0-9]{6,30}") && password.length() > 6);
    }

    public void cancel(View view) {
        startActivity(new Intent(this, LoggedInActivity.class));
    }

    public class UpdatePassword extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mOldPassword;

        UpdatePassword(String email, String password, String oldPassword) {
            mEmail = email;
            mPassword = password;
            mOldPassword = oldPassword;
        }
        protected Boolean doInBackground(Void...params) {
            boolean retVal = false;
            try {
                BlackBoardConnector bbc = new BlackBoardConnector();
                retVal = bbc.changePass(mEmail, mPassword, mOldPassword);
                Log.d("DB changePass Finished", "updateProfile method returned: "
                        + Boolean.toString(retVal));
                bbc.disconnect();
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
            return retVal;
        }

    }
}