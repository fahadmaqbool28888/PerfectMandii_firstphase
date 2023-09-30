package com.vendor.perfectmandii;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.vendor.perfectmandii.Adapter.InvoiceProductDetail;
import com.vendor.perfectmandii.Adapter.profile.order.MyOrderAdapter;
import com.vendor.perfectmandii.Adapter.profile.order.ViewOrderAdapter;

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

public class ViewOrderDetailActivity extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    ViewOrderAdapter mAdapter;
    String pi;
    RecyclerView recyclerView;
    TextView action_button;
    CardView actionbutton;
    boolean flags=false;
    String flag;
    List<InvoiceProductDetail> data;

    int totalsum=0;

    TextView order_total,confirm_order_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_detail);

        recyclerView=findViewById(R.id.getProduct);

        order_total=findViewById(R.id.order_total);

        Intent intent=getIntent();
        pi=intent.getStringExtra("pi");

        new AsyncFetch().execute();

    }

    private class AsyncFetch extends AsyncTask<String, String, String>
    {
        HttpURLConnection conn;
        URL url = null;
        ProgressDialog progressDialog=new ProgressDialog(ViewOrderDetailActivity.this);
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

            progressDialog.dismiss();
            //pdLoading.dismiss();
            data=new ArrayList<>();


            try {
                //Toast.makeText(MainActivity.this,result.toString(),Toast.LENGTH_LONG).show();


                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    InvoiceProductDetail invoiceProductDetail=new InvoiceProductDetail();
                    /*        "id": "646",
        "Product_Name": "Water Set Magnate 800 gms",
        "order_quantity": "1",
        "selling_price": "600",
        "total_price": "600",
        "image_path": "https://sellerportal.perfectmandi.com/uploads/9/Product/Water Set Mt 800 gms.jpg"
   */
                    invoiceProductDetail.id=json_data.getString("id");
                    invoiceProductDetail.Product_Name=json_data.getString("Product_Name");
                    invoiceProductDetail.order_quantity=json_data.getString("order_quantity");
                    invoiceProductDetail.selling_price=json_data.getString("selling_price");
                    invoiceProductDetail.total_price=json_data.getString("total_price");
                    String str=json_data.getString("total_price");
                    totalsum=totalsum+Integer.parseInt(str);
                    invoiceProductDetail.image_path=json_data.getString("image_path");
              /*      invoiceProductDetail.product_measure_in=json_data.getString("product_measure_in");
                    invoiceProductDetail.measure_category=json_data.getString("measure_category");
*/
                    data.add(invoiceProductDetail);
                }

                // Setup and Handover data to recyclerview

                order_total.setText("Your total order value is "+String.valueOf(totalsum));
                mAdapter = new ViewOrderAdapter(ViewOrderDetailActivity.this, data);
                recyclerView.setAdapter(mAdapter);

                recyclerView.setLayoutManager( new LinearLayoutManager(ViewOrderDetailActivity.this,LinearLayoutManager.VERTICAL,false));

            } catch (Exception e) {
                //  Toast.makeText(Function.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
}