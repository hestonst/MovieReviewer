package com.thundercats50.moviereviewer.listview;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import com.thundercats50.moviereviewer.R;


/**
 * Created by scottheston on 23/02/16.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder {
    protected NetworkImageView thumbnail;
    protected TextView title;
    protected TextView synopsis;
    protected RelativeLayout recLayout;


    public MovieViewHolder(View view) {
        super(view);
        this.thumbnail = (NetworkImageView) view.findViewById(R.id.thumbnail);
        this.title = (TextView) view.findViewById(R.id.title);
        this.synopsis = (TextView) view.findViewById(R.id.synopsis);
        this.recLayout = (RelativeLayout) view.findViewById(R.id.recLayout);

        view.setClickable(true);
    }

}

