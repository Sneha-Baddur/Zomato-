package com.application.zomatoapi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.application.zomatoapi.R;
import com.application.zomatoapi.model.ReviewModel;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<ReviewModel> restaurantList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, review, rating;
        public ImageView thumbnail;
        public View view;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            userName = (TextView) view.findViewById(R.id.rev_user_name);
            review = (TextView) view.findViewById(R.id.res_review);
            thumbnail = (ImageView) view.findViewById(R.id.rev_cover);
            rating = view.findViewById(R.id.res_rate);
        }
    }


    public ReviewAdapter(Context mContext, List<ReviewModel> restaurantList) {
        this.mContext = mContext;
        this.restaurantList = restaurantList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_activity_item, parent, false);
        return new MyViewHolder(itemView);
    }

    public void setRestaurantList(List<ReviewModel> resList) {
        this.restaurantList = resList;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ReviewModel review = restaurantList.get(position);
        holder.userName.setText(review.getUserName());
        holder.review.setText(review.getReview());
        holder.rating.setText(review.getRating());

        // loading album cover using Glide library
        Glide.with(mContext).load(review.getUrl()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
}
