package com.vendor.perfectmandii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.vendor.perfectmandii.Adapter.AdapterHome;
import com.vendor.perfectmandii.Adapter.ProductProperties.priceAdapter;
import com.vendor.perfectmandii.Model.ProductProperties.price;
import com.vendor.perfectmandii.Model.vendor.vendorServiceModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class vendor_update_product extends AppCompatActivity
{
RecyclerView recyclerView;
priceAdapter priceadapter;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_update_product);
        intializeWidget();
        new AsyncFetch().execute();
    }

    void intializeWidget()
    {
        recyclerView=findViewById(R.id.productList_);

    }
    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;
        String apiUrl = "https://staginigserver.perfectmandi.com/get_product_detail.php";
        ProgressDialog progressDialog=new ProgressDialog(vendor_update_product.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(vendor_update_product.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();



        }
        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(apiUrl);

                    urlConnection = (HttpURLConnection) url
                            .openConnection();

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();
                        System.out.print(current);

                    }
                    // return the data to onPostExecute method
                    return current;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }


        @Override
        protected void onPostExecute(String result)
        {

            progressDialog.dismiss();
            Toast.makeText(vendor_update_product.this, result, Toast.LENGTH_SHORT).show();
            List<price> productproperties=new ArrayList<>();

            try {



                JSONArray jArray = new JSONArray(result);



                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    price p_price=new price();
                    //
                  //  "name":"Glass Plain Mt","description":"1 dozen glass plain magnet","price":"540"
                    p_price.name= json_data.getString("name");
                    p_price.description= json_data.getString("description");
                    p_price.currentprice= json_data.getString("price");


                    productproperties.add(p_price);
                }

                // Setup and Handover data to recyclerview

                priceadapter = new priceAdapter(vendor_update_product.this, productproperties);
                progressDialog.dismiss();
                recyclerView.setAdapter(priceadapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(vendor_update_product.this));
               /* mRVFishPrice.setAdapter(mAdapter);
                progressDialog.dismiss();
                mRVFishPrice.setLayoutManager( new GridLayoutManager(MainActivity.this,4));*/

            } catch (Exception e)
            {

            }



        }

    }
}