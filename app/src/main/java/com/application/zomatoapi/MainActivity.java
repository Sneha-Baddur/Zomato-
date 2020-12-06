package com.application.zomatoapi;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.zomatoapi.adapter.RestaurantAdapter;
import com.application.zomatoapi.serverutil.GpsTracker;
import com.application.zomatoapi.serverutil.ServerInterface;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private List<Restaurant> restaurantList;
    LocationManager locationManager;
    String latitude, longitude;
    private ServerInterface serverInterface;
    public double lat;
    public double lon;
    private GpsTracker gpsTracker;
    private TextView tvLatitude,tvLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        initCollapsingToolbar();


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        restaurantList = new ArrayList<>();
        adapter = new RestaurantAdapter(this, restaurantList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

//        prepareAlbums();

        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            OnGPS();
//        } else {
//            getLocation();
//        }
        serverInterface = new ServerInterface();
        serverInterface.setServerResponse(serverResponse);
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }else
            {
                getLocation();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getLocation(){
        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            lat = latitude;
            lon = longitude;
            serverInterface.getRestaurantList(MainActivity.this, lat, lon);
        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    public interface ServerResponse {
        public void onSuccess(List<Restaurant> restaurantList);

        public void onError(String error);
    }

    ServerResponse serverResponse = new ServerResponse() {
        @Override
        public void onSuccess(List<Restaurant> restaurantList) {
            MainActivity.this.restaurantList = restaurantList;
            adapter.setRestaurantList(restaurantList);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onError(String error) {
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
//    private void initCollapsingToolbar() {
////        final CollapsingToolbarLayout collapsingToolbar =
////                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
////        collapsingToolbar.setTitle(" ");
////        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
////        appBarLayout.setExpanded(true);
//
//        // hiding & showing the title when toolbar expanded & collapsed
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            boolean isShow = false;
//            int scrollRange = -1;
//
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (scrollRange == -1) {
//                    scrollRange = appBarLayout.getTotalScrollRange();
//                }
//                if (scrollRange + verticalOffset == 0) {
//                    collapsingToolbar.setTitle(getString(R.string.app_name));
//                    isShow = true;
//                } else if (isShow) {
//                    collapsingToolbar.setTitle(" ");
//                    isShow = false;
//                }
//            }
//        });
//    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,
                R.drawable.album9,
                R.drawable.album10,
                R.drawable.album11};

        Restaurant a = new Restaurant("True Romance", 13, covers[0]);
        restaurantList.add(a);

        a = new Restaurant("Xscpae", 8, covers[1]);
        restaurantList.add(a);

        a = new Restaurant("Maroon 5", 11, covers[2]);
        restaurantList.add(a);

        a = new Restaurant("Born to Die", 12, covers[3]);
        restaurantList.add(a);

        a = new Restaurant("Honeymoon", 14, covers[4]);
        restaurantList.add(a);

        a = new Restaurant("I Need a Doctor", 1, covers[5]);
        restaurantList.add(a);

        a = new Restaurant("Loud", 11, covers[6]);
        restaurantList.add(a);

        a = new Restaurant("Legend", 14, covers[7]);
        restaurantList.add(a);

        a = new Restaurant("Hello", 11, covers[8]);
        restaurantList.add(a);

        a = new Restaurant("Greatest Hits", 17, covers[9]);
        restaurantList.add(a);

        adapter.notifyDataSetChanged();
    }

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
