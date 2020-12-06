package com.application.zomatoapi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.application.zomatoapi.R;
import com.application.zomatoapi.Restaurant;
import com.application.zomatoapi.RestaurantActivity;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {

    private Context mContext;
    private List<Restaurant> restaurantList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail;
        public View view;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
//            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public RestaurantAdapter(Context mContext, List<Restaurant> restaurantList) {
        this.mContext = mContext;
        this.restaurantList = restaurantList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_card, parent, false);
        return new MyViewHolder(itemView);
    }

    public void setRestaurantList(List<Restaurant> resList) {
        this.restaurantList = resList;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Clicked " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, RestaurantActivity.class);
                intent.putExtra("res_name", restaurantList.get(position).getName());
                intent.putExtra("res_address", restaurantList.get(position).getAddress());
                intent.putExtra("res_url", restaurantList.get(position).getUrl());
                intent.putExtra("res_id", restaurantList.get(position).getResId());
                mContext.startActivity(intent);
            }
        });
        Restaurant restaurant = restaurantList.get(position);
        holder.title.setText(restaurant.getName());
        holder.count.setText(restaurant.getAddress());

        // loading album cover using Glide library
        if (restaurant.getUrl() != null && !restaurant.getUrl().equals("")) {
            Glide.with(mContext).load(restaurant.getUrl()).into(holder.thumbnail);
        } else {
            Glide.with(mContext).load(restaurant.getThumbnail()).into(holder.thumbnail);
        }


//        holder.overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(holder.overflow);
//            }
//        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
}
