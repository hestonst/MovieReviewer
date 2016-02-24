package com.thundercats50.moviereviewer;

import android.app.ListActivity;
import android.app.SearchManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import android.view.View;

public class SearchActivity extends AppCompatActivity {
    // holds the query the user enters
    private String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        TextView searchDialogue = (TextView) findViewById(R.id.searchPrompt);
        searchDialogue.setText(getString(R.string.search_hint));
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
        // Get the intent, verify the action and get the query
        // TODO Have the query show up dynamically in the searchDialogue rather than widget
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            searchQuery = intent.getStringExtra(SearchManager.QUERY);
            searchRTBasic();
        }
    }

    public void searchRTBasic() {
        // stores number of results from API call
        int count = 0;
        // implement regular API search here using searchQuery
        TextView searchDialogue = (TextView) findViewById(R.id.searchPrompt);
        searchDialogue.setText(getString(R.string.search_return, count, searchQuery));
    }

    public void searchNewReleases(View view) {
        // stores number of results from API call
        int count = 0;
        // implement API call to find new releases
        TextView searchDialogue = (TextView) findViewById(R.id.searchPrompt);
        searchDialogue.setText(getString(R.string.search_return, count, "New Releases"));
    }

    public void searchNewDVD(View view) {
        // stores number of results from API call
        int count = 0;
        // implement API call to find new dvd releases
        TextView searchDialogue = (TextView) findViewById(R.id.searchPrompt);
        searchDialogue.setText(getString(R.string.search_return, count, "New DVDs"));
    }

}
