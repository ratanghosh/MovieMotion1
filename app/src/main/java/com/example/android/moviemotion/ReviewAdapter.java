package com.example.android.moviemotion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(Context context, List<Review> reviews) {
        super(context, 0, reviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).
                    inflate(R.layout.item_review, parent, false);
        }
        // Get the position of the item on the listView
        Review currentReview = getItem(position);

        // titleView for current news casted in the TextView
        TextView authorView = listItemView.findViewById(R.id.author_textView);
        authorView.setText(currentReview.getAuthor());

        // sectionView for current news casted in the TextView
        TextView contentView = listItemView.findViewById(R.id.review_textView);
        contentView.setText(currentReview.getContent());



        // return listItemView
        return listItemView;
    }
}
