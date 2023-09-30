package com.vendor.perfectmandii.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.vendor.perfectmandii.Adapter.AdapterProductPromotionPrice;
import com.vendor.perfectmandii.Adapter.AdapterProductUpdatePrice;
import com.vendor.perfectmandii.DBHelper;
import com.vendor.perfectmandii.Dashboard.OPActivity;
import com.vendor.perfectmandii.LoginActivity;
import com.vendor.perfectmandii.Model.StockProductModel;
import com.vendor.perfectmandii.Model.userVendor;
import com.vendor.perfectmandii.R;

import org.json.JSONArray;
import org.json.JSONException;
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

public class PromotionProductActivity extends AppCompatActivity {
    private AdapterProductPromotionPrice mAdapter;
    String json_string;
    ArrayList<userVendor> readVendor;
    String vendor;
    List<String> id_list,price_list,newprice_list,racposition,promotion_status;

    RecyclerView recyclerView;

    String price,newprice,id;

    CardView checkoutforshopping;

    ImageView bax;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_product);
        recyclerView=findViewById(R.id.nope_12);
        dbHelper=new DBHelper(PromotionProductActivity.this);
        //
        init();
        new AsyncFetch().execute();


        id_list=new ArrayList<>();
        price_list=new ArrayList<>();
        newprice_list=new ArrayList<>();
        racposition=new ArrayList<>();
        promotion_status=new ArrayList<>();
        //

        bax=findViewById(R.id.bax);
        bax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
    }


    ArrayList<userVendor> pointModelsArrayList;
    void init()
    {
        pointModelsArrayList = dbHelper.readVendor();



        if (pointModelsArrayList.size()>0)
        {
            userVendor pointModel=pointModelsArrayList.get(0);
            vendor=pointModel.userid;




        }
        else
        {


        }

      /*  userVendor pointModel=pointModelArrayList.get(0);

        tokenValue.setText(pointModel.points);*/
//


    }

    //
    String JsonString() {

        int i;


        json_string=null;



        try {
            //Repeat and loop this until all objects are added (and add try+catch)
            json_string ="{\"upload_fishes\":[";
            for (i=0;i<newprice_list.size();i++)
            {

                JSONObject obj_new = new JSONObject();



                obj_new.put("productid",id_list.get(i));
                obj_new.put("price",newprice_list.get(i));
                obj_new.put("status",promotion_status.get(i));

                json_string = json_string + obj_new.toString() + ",";

//Close JSON string

            }
            json_string = json_string.substring(0, json_string.length()-1);
            json_string += "]}";



        }
        catch (JSONException jsox)
        {
            Toast.makeText(PromotionProductActivity.this,jsox.toString(),Toast.LENGTH_LONG).show();
        }

        return  json_string;
    }
    //
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent



            try {
                String flag=intent.getStringExtra("flag");
                price=intent.getStringExtra("price");
                newprice=intent.getStringExtra("newprice");
                id=intent.getStringExtra("id");
                int pos=intent.getIntExtra("pos",0);

                racposition.add(String.valueOf(pos));


                String string=JsonString();
                System.out.println(string);
                new AsyncFetch_add().execute();
            }
            catch (Exception exception)
            {

            }


        }
    };
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(PromotionProductActivity.this);
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
                url = new URL("https://sellerportal.perfectmandi.com/get_ProductPrice.php?id="+vendor);

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

            //this method will be running on UI thread


            System.out.println(result);
            pdLoading.dismiss();
            // System.out.println(result);
            List<StockProductModel> data=new ArrayList<>();


            try
            {
                JSONArray jArray = new JSONArray(result);

                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);


                    StockProductModel stockProductModel=new StockProductModel();
                    stockProductModel.id=json_data.getString("id");
                    stockProductModel.price=json_data.getString("price");

                    stockProductModel.name=json_data.getString("name");
                    stockProductModel.path=json_data.getString("path");



                    if (json_data.getString("status").equalsIgnoreCase("")||json_data.getString("status").equalsIgnoreCase("no"))
                    {
                        stockProductModel.setChecked(false);
                    }
                    else {
                        stockProductModel.setChecked(true);
                    }



                    // fishData.usid=username;

                    data.add(stockProductModel);
                }

                // Setup and Handover data to recyclerview

                mAdapter = new AdapterProductPromotionPrice(PromotionProductActivity.this, data) {
                    @Override
                    public void toggle(String id, String price, String status) {

                        id_list.add(id);
                        //price_list.add(price);
                        newprice_list.add(price);
                        promotion_status.add(status);
                        String string=JsonString();
                        System.out.println(string);
                        new AsyncFetch_add().execute();
                    }


                };
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager( new LinearLayoutManager(PromotionProductActivity.this));


            } catch (Exception e) {
            }
        }

    }

    private class AsyncFetch_add extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(PromotionProductActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()

        {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String operatingurl="https://sellerportal.perfectmandi.com/promotion_product_set.php?data="+json_string;

                System.out.println(operatingurl);
                url = new URL(operatingurl);

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
        protected void onPostExecute(String result)
        {

            Toast.makeText(PromotionProductActivity.this,result,Toast.LENGTH_LONG).show();
            pdLoading.dismiss();

            if (result.contains("Flash Sale Added"))
            {
                id_list.remove(id_list);
                //price_list.add(price);
                newprice_list.remove(newprice_list);
                promotion_status.remove(promotion_status);
               /* finish();
                startActivity(getIntent());*/
            }
            else {
                id_list.remove(id_list);
                //price_list.add(price);
                newprice_list.remove(newprice_list);
                promotion_status.remove(promotion_status);
            }



        }

    }
}