package com.example.android.moviemotion.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moviemotion.R;
import com.example.android.moviemotion.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Review> reviews;


    public ReviewAdapter(Context context, List<Review> mReviews) {
        this.inflater = LayoutInflater.from(context);
        this.reviews = mReviews;

    }

    public void setTasks(List<Review> reviewList) {
        reviews = reviewList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_review, parent, false);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {

        // bind the data to create thumbnails in the main page
        holder.authorTextView.setText("by " + reviews.get(position).getAuthor());
        holder.reviewTextView.setText(reviews.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView authorTextView;
        TextView reviewTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            authorTextView = itemView.findViewById(R.id.author_textView);
            reviewTextView = itemView.findViewById(R.id.review_textView);


        }


    }



}
