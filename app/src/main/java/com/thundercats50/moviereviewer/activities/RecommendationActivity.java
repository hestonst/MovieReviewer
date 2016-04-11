package com.thundercats50.moviereviewer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import com.thundercats50.moviereviewer.listview.MovieFragment;

import com.thundercats50.moviereviewer.R;
import com.thundercats50.moviereviewer.models.UserManager;
import com.thundercats50.moviereviewer.models.User;

public class RecommendationActivity extends AppCompatActivity {

    /**
     * movie fragment
     */
    private MovieFragment movieFragment;
    /**
     * genre
     */
    private String genre;
    /**
     * major
     */
    private String major;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        final Spinner dropdown = (Spinner)findViewById(R.id.genre);
        final String[] items = new String[]{"Action & Adventure", "Animation", "Art House & International", "Classics", "Comedy", "Drama", "Horror", "Kids & Family", "Mystery & Suspense", "Romance", "Science Fiction & Fantasy", "Documentary", "Musical & Performing Arts", "Special Interest", "Sports & Fitness", "Television", "Western"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        this.genre = dropdown.getSelectedItem().toString();

        // get the movie fragment to send the queries to
        movieFragment = (MovieFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
        final UserManager manager = (UserManager) getApplicationContext();
        final User user = manager.getCurrentMember();
        major = user.getMajor();

    }

    /**
     * search movies by genre
     * @param view recommendation view
     */
    /*
    public void searchByGenre(View view) {
        movieFragment.searchByGenre(genre);
    }
    */

    /**
     * search movies by major
     * @param view recommendation view
     */
    public void searchByMajor(View view) {movieFragment.searchByMajor(major);}

    /**
     * go to homepage
     * @param view recommendation view
     */
    public void goHome(View view) {
        startActivity(new Intent(this, LoggedInActivity.class));
    }

}
