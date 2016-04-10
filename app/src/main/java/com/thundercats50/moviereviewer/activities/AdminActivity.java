package com.thundercats50.moviereviewer.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.thundercats50.moviereviewer.R;
import com.thundercats50.moviereviewer.listview.UserFragment;

public class AdminActivity extends AppCompatActivity {

    /**
     * user fragment for user list
     */
    private UserFragment userFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        userFrag = (UserFragment) getSupportFragmentManager().findFragmentById(R.id.fragment3);
    }

    /**
     * gets a user list
     * @param view view on the app
     */
    public void listUsers(View view) {
        userFrag.getUserList();
    }
}
