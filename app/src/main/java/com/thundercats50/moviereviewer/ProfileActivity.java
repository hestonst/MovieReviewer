package com.thundercats50.moviereviewer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;

import com.thundercats50.moviereviewer.models.MemberManager;
import com.thundercats50.moviereviewer.models.Member;
import com.thundercats50.moviereviewer.models.User;

public class ProfileActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText gender;
    private EditText major;
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
        member.setUsername(name.getText().toString());
        member.setGender(gender.getText().toString());
        member.setMajor(major.getText().toString());
        manager.updateMember(email.getText().toString(), member);
        startActivity(new Intent(this, LoggedInActivity.class));
    }

    public void cancel(View view) {
        startActivity(new Intent(this, LoggedInActivity.class));
    }

}
