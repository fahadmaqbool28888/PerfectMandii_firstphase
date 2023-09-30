package com.vendor.perfectmandii.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vendor.perfectmandii.Adapter.AdapterProductUpdatePrice;
import com.vendor.perfectmandii.Model.StockProductModel;
import com.vendor.perfectmandii.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoadProductsActivity extends AppCompatActivity {
    // creating a variable for our array list, adapter class,
    // recycler view, progressbar, nested scroll view
    private List<StockProductModel> userModalArrayList;
    private AdapterProductUpdatePrice userRVAdapter;
    private RecyclerView userRV;
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;

    // creating a variable for our page and limit as 2
    // as our api is having highest limit as 2 so
    // we are setting a limit = 2
    int page = 0, limit = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_products);


        // creating a new array list.
        userModalArrayList = new ArrayList<>();

        // initializing our views.
        userRV = findViewById(R.id.idRVUsers);
        loadingPB = findViewById(R.id.idPBLoading);
        nestedSV = findViewById(R.id.idNestedSV);

        // calling a method to load our api.
        getDataFromAPI(page, limit);

        // adding on scroll change listener method for our nested scroll view.
        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    loadingPB.setVisibility(View.VISIBLE);
                    getDataFromAPI(page, limit);
                }
            }
        });


    }
    private void getDataFromAPI(int page, int limit) {
        if (page > limit) {
            // checking if the page number is greater than limit.
            // displaying toast message in this case when page>limit.
            Toast.makeText(this, "That's all the data..", Toast.LENGTH_SHORT).show();

            // hiding our progress bar.
            loadingPB.setVisibility(View.GONE);
            return;
        }
        // creating a string variable for url .
        String url = "https://sellerportal.perfectmandi.com/get_ProductPrice_withphp.php?id=03005094664&page=" + page;

        System.out.println(url);
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(LoadProductsActivity.this);

        // creating a variable for our json object request and passing our url to it.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    // on below line we are extracting data from our json array.
                    JSONArray dataArray = response.getJSONArray("data");

                    // passing data from our json array in our array list.
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject jsonObject = dataArray.getJSONObject(i);

                        // on below line we are extracting data from our json object.
                        userModalArrayList.add(new StockProductModel(jsonObject.getString("id"), jsonObject.getString("name"), jsonObject.getString("price"), jsonObject.getString("path")));

                        // passing array list to our adapter class.
                        userRVAdapter = new AdapterProductUpdatePrice(LoadProductsActivity.this,userModalArrayList);

                        // setting layout manager to our recycler view.
                        userRV.setLayoutManager(new LinearLayoutManager(LoadProductsActivity.this));

                        // setting adapter to our recycler view.
                        userRV.setAdapter(userRVAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // handling on error listener method.
                Toast.makeText(LoadProductsActivity.this, "Fail to get data..", Toast.LENGTH_SHORT).show();
            }
        });
        // calling a request queue method
        // and passing our json object
        queue.add(jsonObjectRequest);
    }
}