package com.thundercats50.moviereviewer.listview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

//If we want to use movie images:
import com.android.volley.toolbox.ImageLoader;
import com.thundercats50.moviereviewer.R;
import com.thundercats50.moviereviewer.WelcomeActivity;
import com.thundercats50.moviereviewer.models.SingleMovie;
//to remove after integration of rotten tomatoes

/**
 * Created by scottheston on 23/02/16.
 * Consulting tutorial: https://www.youtube.com/watch?v=8ePqYGMxdSY
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieViewHolder>  {

    //replace all access to movies with information from database
    private List<SingleMovie> movieList;
    private Context mContext;
    private ImageLoader mImageLoader;

    private int focusedItem = 0;

    public MovieListAdapter(Context context, List<SingleMovie> listItemsList) {
        this.movieList = listItemsList;
        this.mContext = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(final ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        MovieViewHolder holder = new MovieViewHolder(view);

        holder.recLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: forward click to new activity
                Intent intent = new Intent(mContext, WelcomeActivity.class);
                mContext.startActivity(intent);
            }
        });

        return holder;
    }


    @Override
    public void onBindViewHolder(final MovieViewHolder movieViewHolder, int position) {
        SingleMovie movie = movieList.get(position);
        movieViewHolder.itemView.setSelected(focusedItem == position);

        movieViewHolder.getLayoutPosition();

        mImageLoader = MovieRequestQueue.getInstance(mContext).getImageLoader();

        movieViewHolder.thumbnail.setImageUrl(movie.getThumbnail(), mImageLoader);
        movieViewHolder.thumbnail.setDefaultImageResId(R.drawable.rotten_tomato);

        movieViewHolder.title.setText(Html.fromHtml(movie.getTitle()));
        movieViewHolder.synopsis.setText(Html.fromHtml(movie.getAuthor()));
    }

    public void clearAdapter()
    {
        movieList.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != movieList ? movieList.size() : 0);
    }

}

