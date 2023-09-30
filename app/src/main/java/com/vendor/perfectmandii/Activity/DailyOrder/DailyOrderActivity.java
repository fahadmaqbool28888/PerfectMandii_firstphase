package com.vendor.perfectmandii.Activity.DailyOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.vendor.perfectmandii.Activity.Order.fetch.UserOrderActivity;
import com.vendor.perfectmandii.Adapter.DailyOrderAdapter;
import com.vendor.perfectmandii.Adapter.Order.AdapterPlacedOrder;
import com.vendor.perfectmandii.Model.DailyOrder.orderDaily;
import com.vendor.perfectmandii.Model.OrderCartModel;
import com.vendor.perfectmandii.R;

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

public class DailyOrderActivity extends AppCompatActivity
{
    DailyOrderAdapter mAdapter;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    RecyclerView daily_order;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_order);
        init();
        new AsyncFetch().execute();
    }
    void init()
    {
        daily_order=findViewById(R.id.daily_order);
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(DailyOrderActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()

        {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setIcon(R.drawable.optimizedlogo);
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {


                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("https://sellerportal.perfectmandi.com/orderdetail_1.php?id=25");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

           // System.out.println(result);
            pdLoading.dismiss();

            List<orderDaily> data=new ArrayList<>();

            pdLoading.dismiss();
            try
            {
                JSONArray jArray = new JSONArray(result);

                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);


                    orderDaily daily = new orderDaily();
                    daily.purchaseInvoice=json_data.getString("purchaseInvoice");
                    daily.customer_order=json_data.getString("customer_order");
                    daily.order_place=json_data.getString("order_place");
                    daily.Tentative_Date=json_data.getString("Tentative_Date");
                    daily.jsonArray=json_data.getJSONArray("order_detail");
                    daily.order_Description=json_data.getJSONArray("order_Description");
                    daily.deliver_Address=json_data.getJSONArray("deliver_Address");
                    //daily.orderdetail=json_data.getJSONObject("orderdetail");



                    data.add(daily);
                }

                // Setup and Handover data to recyclerview

                mAdapter = new DailyOrderAdapter(DailyOrderActivity.this, data);
                daily_order.setAdapter(mAdapter);
                daily_order.setLayoutManager( new LinearLayoutManager(DailyOrderActivity.this));


            } catch (Exception e) {
            }

        }

    }
}