package com.application.zomatoapi.serverutil;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.zomatoapi.MainActivity;
import com.application.zomatoapi.Restaurant;
import com.application.zomatoapi.RestaurantActivity;
import com.application.zomatoapi.model.ReviewModel;
import com.application.zomatoapi.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerInterface {

    private static final String TAG = ServerInterface.class.getCanonicalName();
    private MainActivity.ServerResponse serverResponse;
    private RestaurantActivity.ServerResponse reviewServerResponse;
    double lat;
    double lon;

    public ServerInterface() {
    }

    public void setReviewServerResponse(RestaurantActivity.ServerResponse reviewServerResponse) {
        this.reviewServerResponse = reviewServerResponse;
    }

    public void setServerResponse(MainActivity.ServerResponse serverResponse) {
        this.serverResponse = serverResponse;
    }

    public void getRestaurantList(final Context context, double lat, double lon) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final List<Restaurant> resList = new ArrayList<>();
        String url = "https://developers.zomato.com/api/v2.1/search?lat=" +
                lat + "&lon=" +
                lon + "&radius=2000";
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonresponse = new JSONObject(response);
                            JSONArray restaurantList = jsonresponse.getJSONArray("restaurants");

                            int resCount = restaurantList.length();
                            for (int i = 0; i < resCount; i++) {
                                JSONObject restaurant = restaurantList.getJSONObject(i);
                                JSONObject res = restaurant.getJSONObject("restaurant");
                                String name = res.getString("name");
                                String id = res.getString("id");
                                String add = res.getJSONObject("location").getString("address");
                                String url = res.getString("thumb");
                                Restaurant restaurantObj = new Restaurant();
                                restaurantObj.setName(name);
                                restaurantObj.setAddress(add);
                                restaurantObj.setUrl(url);
                                restaurantObj.setResId(id);
                                resList.add(restaurantObj);
                            }
                            serverResponse.onSuccess(resList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "error => " + error.toString());
                        serverResponse.onError(error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user-key", Constants.ZOMATO_API);
                return params;
            }
        };
        queue.add(getRequest);
    }

    public void getRestaurantDetails(final Context context, String id) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final List<ReviewModel> resList = new ArrayList<>();
        String url = "https://developers.zomato.com/api/v2.1/reviews?res_id=" + id;
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonresponse = new JSONObject(response);
                            JSONArray reviewList = jsonresponse.getJSONArray("user_reviews");

                            int resCount = reviewList.length();
                            for (int i = 0; i < resCount; i++) {
                                JSONObject restaurant = reviewList.getJSONObject(i);
                                JSONObject res = restaurant.getJSONObject("review");
                                String review = res.getString("review_text");
                                String rating = res.getString("rating");
                                String name = res.getJSONObject("user").getString("name");
                                String url = res.getJSONObject("user").getString("profile_image");
                                ReviewModel reviewModel = new ReviewModel();
                                reviewModel.setUserName(name);
                                reviewModel.setReview(review);
                                reviewModel.setUrl(url);
                                reviewModel.setRating(rating);
                                resList.add(reviewModel);
                            }
                            reviewServerResponse.onSuccess(resList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "error => " + error.toString());
                        reviewServerResponse.onError(error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user-key", Constants.ZOMATO_API);
                return params;
            }
        };
        queue.add(getRequest);
    }
}
