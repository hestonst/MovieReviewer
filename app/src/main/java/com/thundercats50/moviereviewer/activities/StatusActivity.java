package com.thundercats50.moviereviewer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.thundercats50.moviereviewer.R;

public class StatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
    }

    public void lockUser(View view) {
        //TODO: DB method to lock user
    }

    public void unlockUser(View view) {
        //TODO: DB method to unlock user
    }
}
