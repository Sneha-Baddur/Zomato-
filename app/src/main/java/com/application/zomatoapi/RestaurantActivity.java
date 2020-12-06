package com.application.zomatoapi;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.zomatoapi.adapter.ReviewAdapter;
import com.application.zomatoapi.model.ReviewModel;
import com.application.zomatoapi.serverutil.ServerInterface;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends Activity {

    //    private TextView mResName;
//    private TextView mResAdd;
//    private ImageView mResCover;
//    private TextView mResReview;
//
    private List<ReviewModel> reviewModelList;
    private RecyclerView recyclerView;
    private ReviewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_activity);
//        mResName = findViewById(R.id.res_name);
//        mResAdd = findViewById(R.id.res_add);
//        mResReview = findViewById(R.id.res_rate);
//        mResCover = findViewById(R.id.res_cover);
        String resName = getIntent().getStringExtra("res_name");
        String resAddress = getIntent().getStringExtra("res_address");
        String resUrl = getIntent().getStringExtra("res_url");
        String id = getIntent().getStringExtra("res_id");
        recyclerView = findViewById(R.id.res_recycler_view);
        reviewModelList = new ArrayList<>();
        adapter = new ReviewAdapter(this, reviewModelList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
//        mResName.setText(resName);
//        mResAdd.setText(resAddress);
//        Glide.with(this).load(resUrl).into(mResCover);
        ServerInterface serverInterface = new ServerInterface();
        serverInterface.setReviewServerResponse(serverResponse);
        serverInterface.getRestaurantDetails(this, id);
    }


    public interface ServerResponse {
        public void onSuccess(List<ReviewModel> reviewModelList);

        public void onError(String error);
    }

    ServerResponse serverResponse = new ServerResponse() {
        @Override
        public void onSuccess(List<ReviewModel> restaurantList) {
            restaurantList = restaurantList;
            adapter.setRestaurantList(restaurantList);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onError(String error) {
            Toast.makeText(RestaurantActivity.this, error, Toast.LENGTH_SHORT).show();
        }
    };


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
