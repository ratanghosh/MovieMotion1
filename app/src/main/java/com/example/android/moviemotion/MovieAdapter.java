package com.example.android.moviemotion;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.moviemotion.MovieUtils.posterLinkList;

public class MovieAdapter extends ArrayAdapter<Movie> {

    //ArrayList movieList = new ArrayList<>();

    // Keeps track of the context and list of images to display
    private Context mContext;


    public MovieAdapter(Context context, List<Movie> movie) {
        super(context, 0, movie);
        mContext = context;

    }


    @Override
    public View getView( int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.movie_list_item, null);
        ImageView imageView = (ImageView) v.findViewById(R.id.poster_thumbnail);


        String posterPath = ("http://image.tmdb.org/t/p/w185//" + posterLinkList.get(position));

        Picasso.with(this.getContext())
                .load(posterPath)
                .into(imageView);


        return v;
    }








}








