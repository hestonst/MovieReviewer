package com.thundercats50.moviereviewer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.thundercats50.moviereviewer.models.MemberManager;
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

        EditText name = (EditText) findViewById(R.id.profileName);
        EditText email = (EditText) findViewById(R.id.profileEmail);
        EditText gender = (EditText) findViewById(R.id.profileGender);
        EditText major = (EditText) findViewById(R.id.profileMajor);

        MemberManager manager = (MemberManager) getApplicationContext();
        User user = (User) manager.getCurrentMember();
        name.setText(user.getFirstname());
        email.setText(user.getEmail());
        gender.setText(user.getGender());
        major.setText(user.getMajor());

        name.setKeyListener(null);
        email.setKeyListener(null);
        gender.setKeyListener(null);
        major.setKeyListener(null);
    }


    public void logout(View view) {
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    public void editProfile(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    public void goToSearch(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }
}
