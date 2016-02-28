package com.thundercats50.moviereviewer.listview;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thundercats50.moviereviewer.R;
import com.thundercats50.moviereviewer.models.SingleMovie;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response.ErrorListener;


import java.util.ArrayList;
import java.util.List;
//to remove after MovieSet integration


public class MovieFragment extends Fragment {
    private static final String TAG = "RecyclerViewExample";
    private List<SingleMovie> movieList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private MovieListAdapter adapter;

    private int counter = 0;
    private String count;
    private String jsonTomatoes;
    private String after_id;

    //Possibly useful for JSON query: (Originally reddit JSON queries)
    private static final String key = "?apikey=yedukp76ffytfuy24zsfqk75&";
    private static final String baseURL = "http://api.rottentomatoes.com/api/public/v1.0/";
    private static final String jsonEnd = "&page_limit=10";

    //Possibly useful for JSON query:
    private static final String qCount = "?count=";
    private static final String after = "&after=";

    private ProgressDialog progressDialog;

    public MovieFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        //Initialize recycler view
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        // add the line under each row
        // if you are creating a card views, it would be best to delete this decoration
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.BLACK)
                        .build());

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.d("SCROLL PAST UPDATE", "You hit me");

                //maintain scroll position
                int lastFirstVisiblePosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPosition(lastFirstVisiblePosition);

                loadMore(jsonTomatoes);
            }
        });


        //Useful to keep if we want to add buttons to the fragment itself:
//        Button mButton = (Button) rootView.findViewById(R.id.back_button);
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.fragSubreddit = gaming;
//                reloadFragment();
//            }
//        });
//
//        Button nButton = (Button) rootView.findViewById(R.id.reviews_button);
//        nButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.fragSubreddit = pics;
//                reloadFragment();
//
//            }
//        });

        return rootView;
    }

    public void updateList(int type, String subkey) {

        // Set the counter to 0. This counter will be used to create new json urls
        // In the loadMore function we will increase this integer by 25
        counter = 0;

        if (type == 0) {
            subkey = baseURL + "movies.json" + subkey + key + jsonEnd;
        } else {
            subkey = baseURL + subkey + key + jsonEnd;
        }

        // "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=yedukp76ffytfuy24zsfqk75&q=Superman&page_limit=10";

        //declare the adapter and attach it to the recyclerview
        adapter = new MovieListAdapter(getActivity(), movieList);
        mRecyclerView.setAdapter(adapter);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());


        // Clear the adapter because new data is being added from a new subkey
        adapter.clearAdapter();

        showPD();

        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=yedukp76ffytfuy24zsqk7f5&q=Superman&page_limit=10", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());
                hidePD();

                // Parse json data.
                // Declare the json objects that we need and then for loop through the children array.
                // Do the json parse in a try catch block to catch the exceptions
                try {
                    //JSONObject data = response.getJSONObject();
                    //after_id = data.getString("after");
                    JSONArray arrayMovies = response.getJSONArray("movies");

                    for (int i = 0; i < arrayMovies.length(); i++) {

                        JSONObject currentMovie = arrayMovies.getJSONObject(i);
                        SingleMovie item = new SingleMovie();
                        item.setTitle(currentMovie.getString("title"));
//                        item.setThumbnail(currentMovie.getString("thumbnail"));
//                        item.setUrl(currentMovie.getString("url"));
//                        item.setSubreddit(currentMovie.getString("subkey"));
//                        item.setAuthor(currentMovie.getString("author"));
//                        jsonTomatoes = currentMovie.getString("subkey");

                        movieList.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Update list by notifying the adapter of changes
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePD();
            }
        });

        queue.add(jsObjRequest);

    }

    public void loadMore(String subkey) {

        // Add 25 each time the function is called
        // Then convert it to a string to add to other strings to create the new reddit json url.
        counter = counter + 25;
        count = String.valueOf(counter);


        subkey = jsonTomatoes;

        // Create the reddit json url for parsing
        subkey = baseURL + subkey + jsonEnd + qCount + count + after + after_id;

        // Declare the adapter and attach it to the recycler-view
        adapter = new MovieListAdapter(getActivity(), movieList);
        mRecyclerView.setAdapter(adapter);

        showPD();


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, subkey, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                // Log to console the whole json string for debugging
                Log.d(TAG, response.toString());
                hidePD();

                // Parse json data.
                // Declare the json objects that we need and then for loop through the children array.
                // Do the json parse in a try catch block to catch the exceptions
                try {
                    JSONObject data = response.getJSONObject("data");
                    after_id = data.getString("after");
                    JSONArray children = data.getJSONArray("children");

                    for (int i = 0; i < children.length(); i++) {

                        JSONObject post = children.getJSONObject(i).getJSONObject("data");
                        SingleMovie item = new SingleMovie();
                        item.setTitle(post.getString("title"));
                        item.setThumbnail(post.getString("thumbnail"));
                        item.setUrl(post.getString("url"));
                        item.setSubreddit(post.getString("subkey"));
                        item.setAuthor(post.getString("author"));
                        movieList.add(item);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Update list by notifying the adapter of changes
                adapter.notifyDataSetChanged();
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error" + error.getMessage());
                hidePD();

            }
        });

        queue.add(jsObjRequest);
    }

    // Reload the fragment list holding the recyclerviews
    private void reloadFragment() {
        Fragment newFragment = new MovieFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, newFragment);
        transaction.commit();
    }

    private void showPD() {
        if(progressDialog == null) {
            progressDialog  = new ProgressDialog(getActivity());
            progressDialog .setMessage("Loading...");
            progressDialog .setCancelable(false);
            progressDialog .setCanceledOnTouchOutside(false);
            progressDialog .show();
        }
    }

    // function to hide the loading dialog box
    private void hidePD() {
        if (progressDialog  != null) {
            progressDialog .dismiss();
            progressDialog  = null;
        }
    }

    // Stop app from running
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePD();
    }
    
}

