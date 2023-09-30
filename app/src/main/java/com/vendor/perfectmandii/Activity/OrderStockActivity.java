package com.vendor.perfectmandii.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.vendor.perfectmandii.Adapter.EditOrder.editAdapter;
import com.vendor.perfectmandii.Adapter.InvoiceProductDetail;
import com.vendor.perfectmandii.Adapter.profile.order.ViewOrderAdapter;
import com.vendor.perfectmandii.R;
import com.vendor.perfectmandii.ViewOrderDetailActivity;

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

public class OrderStockActivity extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    editAdapter mAdapter;
    String pi;
    RecyclerView recyclerView;
    String flag;
    String orderid;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_stock);
        recyclerView=findViewById(R.id.nope_12_12);
        Intent intent=getIntent();
        pi=intent.getStringExtra("pi");
        orderid=intent.getStringExtra("pi");

        new AsyncFetch().execute();
    }

    private class AsyncFetch extends AsyncTask<String, String, String>
    {
        HttpURLConnection conn;
        URL url = null;
        ProgressDialog progressDialog=new ProgressDialog(OrderStockActivity.this);
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog.show();
            progressDialog.setIcon(R.drawable.optimizedlogo);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                System.out.println("https://sellerportal.perfectmandi.com/getordersub_api.php?id="+pi);
                url = new URL("https://sellerportal.perfectmandi.com/getordersub_api.php?id="+pi);

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

            } catch (IOException e1)
            {
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
            progressDialog.dismiss();
            //pdLoading.dismiss();
            List<InvoiceProductDetail> data=new ArrayList<>();


            try {
                //Toast.makeText(MainActivity.this,result.toString(),Toast.LENGTH_LONG).show();


                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    InvoiceProductDetail invoiceProductDetail=new InvoiceProductDetail();
                    invoiceProductDetail.id=json_data.getString("id");
                    invoiceProductDetail.Product_Name=json_data.getString("Product_Name");
                    invoiceProductDetail.order_quantity=json_data.getString("order_quantity");
                    invoiceProductDetail.selling_price=json_data.getString("selling_price");
                    invoiceProductDetail.total_price=json_data.getString("total_price");
                    invoiceProductDetail.image_path=json_data.getString("image_path");


                    System.out.println(invoiceProductDetail.id);
                    System.out.println(invoiceProductDetail.Product_Name);
                    System.out.println(invoiceProductDetail.order_quantity);
                    System.out.println(invoiceProductDetail.selling_price);
                    System.out.println(invoiceProductDetail.total_price);
                    System.out.println(invoiceProductDetail.image_path);
                    invoiceProductDetail.oid=pi;
                    data.add(invoiceProductDetail);
                }

                // Setup and Handover data to recyclerview

                mAdapter = new editAdapter(OrderStockActivity.this, data);
                recyclerView.setAdapter(mAdapter);

                recyclerView.setLayoutManager( new LinearLayoutManager(OrderStockActivity.this,LinearLayoutManager.VERTICAL,false));

            } catch (Exception e) {
                //  Toast.makeText(Function.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
}