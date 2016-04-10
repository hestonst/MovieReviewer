package com.thundercats50.moviereviewer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.thundercats50.moviereviewer.R;
import com.thundercats50.moviereviewer.models.UserManager;
import com.thundercats50.moviereviewer.models.User;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoggedInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loggedin);

        refillForms();
    }

    /**
     * Fill up the form
     */
    private void refillForms() {
        final EditText firstName = (EditText) findViewById(R.id.profileFirstName);
        final EditText lastName = (EditText) findViewById(R.id.profileLastName);
        final EditText email = (EditText) findViewById(R.id.profileEmail);
        final EditText gender = (EditText) findViewById(R.id.profileGender);
        final EditText major = (EditText) findViewById(R.id.profileMajor);

        final UserManager manager = (UserManager) getApplicationContext();
        final User user = manager.getCurrentMember();
        firstName.setText(user.getFirstname());
        lastName.setText(user.getLastname());
        email.setText(user.getEmail());
        gender.setText(user.getGender());
        major.setText(user.getMajor());

        firstName.setKeyListener(null);
        lastName.setKeyListener(null);
        email.setKeyListener(null);
        gender.setKeyListener(null);
        major.setKeyListener(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refillForms();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserManager manager = (UserManager) getApplicationContext();
        manager.setCurrentMember(new User("",""));
        //delete the current users info as you move up stack
        //as security measure
    }


    /**
     * method to logout
     * @param view view of the profile page
     */
    public void logout(View view) {
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    /**
     * method to edit profile
     * @param view view of the profile page
     */
    public void editProfile(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    /**
     * method to search for movies
     * @param view view of the profile page
     */
    public void goToSearch(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }

    /**
     * method to get recommendations
     * @param view view of the profile page
     */
    public void getRecommendation(View view) { startActivity(new Intent(this, RecommendationActivity.class)); }
}
