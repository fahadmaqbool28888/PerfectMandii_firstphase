package com.vendor.perfectmandii.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vendor.perfectmandii.Adapter.Order.AdaptergetdetailPlacedOrder;
import com.vendor.perfectmandii.Model.getorder;
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

public class InvoiceView extends AppCompatActivity
{

    RecyclerView OrderItem;
    AdaptergetdetailPlacedOrder mAdapter;
    //
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String userid,orderid,packaging,subtotal,grandtotal,discount,invoice,session,status,sorder,id,name,pic;
    ImageView back;
    TextView getsubtotal,packagingCost,getgrandtotal,submitbill,getstatus,invoicenum,ordernum,order_cancel_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_view);


        IntializeWidget();
        /*            intent.putExtra("packaging",current.packaging);
                intent.putExtra("subtotal",current.subtotal);
                intent.putExtra("grandtotal",current.grandtotal);
                intent.putExtra("discount",current.discount);*/

        Intent intent = getIntent();
        // id=intent.getStringExtra("id");
        userid = intent.getStringExtra("userid");
        orderid = intent.getStringExtra("orderid");
        packaging=intent.getStringExtra("packaging");
        subtotal=intent.getStringExtra("subtotal");
        grandtotal=intent.getStringExtra("grandtotal");
        //

        new AsyncFetch().execute();





        new AsyncFetch().execute();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //

        getsubtotal.setText(subtotal);
        packagingCost.setText("Packing Charges: " + packaging);
        getgrandtotal.setText("Total Payable: " + grandtotal);
        invoicenum.setText("Invoice Number: " + invoice);
        ordernum.setText("Order Number: " + orderid);




    }


    void IntializeWidget()
    {
        //

        //subtotal_Bill


        OrderItem=findViewById(R.id.Order_Detail_1);

        getsubtotal=findViewById(R.id.getsubtotal_i);
        packagingCost=findViewById(R.id.packagingcost);
        getgrandtotal=findViewById(R.id.Grandtotal);

        back=findViewById(R.id.bax_q);

        invoicenum=findViewById(R.id.itext);
        ordernum=findViewById(R.id.otext);

    }
    private class AsyncFetch extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(InvoiceView.this);
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
            try
            {
                url = new URL("https://staginigserver.perfectmandi.com/orderdetail.php?id="+userid+"&oid="+orderid);
            }
            catch (MalformedURLException e)
            {
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
            pdLoading.dismiss();
            List<getorder> data=new ArrayList<>();


            System.out.println(result);
            //     pdLoading.dismiss();
            try
            {
                JSONArray jArray = new JSONArray(result);

                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    getorder fishData = new getorder();
                    fishData.image_path=json_data.getString("image_path");
                    fishData.Product_Name=json_data.getString("Product_Name");
                    fishData.Product_Description=json_data.getString("Product_Description");
                    fishData.selling_price=json_data.getString("selling_price");
                    fishData.total_price=json_data.getString("total_price");
                    fishData.order_quantity=json_data.getString("order_quantity");

                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview

                mAdapter = new AdaptergetdetailPlacedOrder(InvoiceView.this, data);
                OrderItem.setAdapter(mAdapter);
                OrderItem.setLayoutManager( new LinearLayoutManager(InvoiceView.this));


            } catch (Exception e) {
            }
        }

    }

}