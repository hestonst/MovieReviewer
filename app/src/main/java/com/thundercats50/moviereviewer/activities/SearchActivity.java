package com.thundercats50.moviereviewer.activities;

import android.app.SearchManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import android.view.View;

import com.thundercats50.moviereviewer.listview.MovieFragment;

import com.thundercats50.moviereviewer.R;

public class SearchActivity extends AppCompatActivity {
    /**
     * search query
     */
    private String searchQuery;
    /**
     * movie fragment
     */
    private MovieFragment movieFragment;
    /**
     * new DVD RT key
     */
    private static final String newDVDSubKey = "lists/dvds/new_releases.json";
    /**
     *  new releases RT key
     */
    private static final String newReleasesSubKey = "lists/movies/in_theaters.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final TextView searchDialogue = (TextView) findViewById(R.id.searchPrompt);
        searchDialogue.setText(getString(R.string.search_hint));
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

        // get the movie fragment to send the queries to
        movieFragment = (MovieFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);

        // Get the intent, verify the action and get the query
        final Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            searchQuery = intent.getStringExtra(SearchManager.QUERY);
            searchQuery = searchQuery.replace(" ", "+");
            movieFragment.updateList(0, searchQuery);
        }
    }

    /**
     * search for new released
     * @param view search view
     */
    public void searchNewReleases(View view) {
        movieFragment.updateList(1, newReleasesSubKey);
    }

    /**
     * search for new dvds
     * @param view search view
     */
    public void searchNewDVD(View view) {
        movieFragment.updateList(1, newDVDSubKey);
    }

    /**
     * go to homepage
     * @param view search view
     */
    public void goHome(View view){
        startActivity(new Intent(this, WelcomeActivity.class));
    }

}
