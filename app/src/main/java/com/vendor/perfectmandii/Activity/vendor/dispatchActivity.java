package com.vendor.perfectmandii.Activity.vendor;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vendor.perfectmandii.Adapter.AdapterDispatch;
import com.vendor.perfectmandii.Adapter.AdapterHome;
import com.vendor.perfectmandii.DashboardActivity;
import com.vendor.perfectmandii.Fragment.NewOrderFragment;
import com.vendor.perfectmandii.MainActivity;
import com.vendor.perfectmandii.Model.getorder;
import com.vendor.perfectmandii.Model.vendor.dispatchitem;
import com.vendor.perfectmandii.Model.vendor.vendorServiceModel;
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

public class dispatchActivity extends AppCompatActivity
{
    RecyclerView dispatchitem;
    CheckBox dispatchall;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private AdapterDispatch mAdapter;
    TextView dispatchnoitem,selecteditem_get,totalorderamountget_,totaldamountget;
    String userid,orderid,packaging,subtotal,grandtotal,discount,invoice,session,status,sorder,id,name,pic;
    int totalcounted;

    CardView dispatchorder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch);

        intializeWidget();
        Intent intent = getIntent();
        // id=intent.getStringExtra("id");
        userid = intent.getStringExtra("userid");
        orderid = intent.getStringExtra("orderid");
        packaging=intent.getStringExtra("packaging");
        subtotal=intent.getStringExtra("subtotal");
        grandtotal=intent.getStringExtra("grandtotal");


        dispatchorder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(dispatchActivity.this,userid+" "+orderid,Toast.LENGTH_LONG).show();
                new AsyncFetch_Dispatch().execute();
            }
        });


        totalorderamountget_.setText("Rs. "+subtotal);
        totaldamountget.setText("Rs. "+subtotal);

        dispatchall.isActivated();
        dispatchall.setVisibility(View.INVISIBLE);

        new AsyncFetch("one").execute();



        dispatchall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    new AsyncFetch("two").execute();
                    mAdapter.notifyDataSetChanged();
                }
                else
                {
                    new AsyncFetch("one").execute();
                    mAdapter.notifyDataSetChanged();
                }

            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            String flag=intent.getStringExtra("flag");

            if(flag.equalsIgnoreCase("exclude"))
            {
                Toast.makeText(dispatchActivity.this,"exclude",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(dispatchActivity.this,"include  ",Toast.LENGTH_LONG).show();
            }

        }
    };
    void intializeWidget()
    {
        dispatchorder=findViewById(R.id.dispatchorder);
        dispatchitem=findViewById(R.id.dispatch_basket);
        dispatchall=findViewById(R.id.dispatchall_);
        dispatchnoitem=findViewById(R.id.dispatchnoitem);
        selecteditem_get=findViewById(R.id.selecteditem_get);
        totalorderamountget_=findViewById(R.id.totalorderamountget_);
                totaldamountget=findViewById(R.id.totaldamountget);
    }


    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        boolean flag=false;
        String checkflag;

        AsyncFetch(String checkflag)
        {

            this.checkflag=checkflag;
        }


        ProgressDialog progressDialog=new ProgressDialog(dispatchActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params) {
            try {


                url = new URL("https://staginigserver.perfectmandi.com/orderdetail.php?id="+userid+"&oid="+orderid);

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
            List<getorder> data=new ArrayList<>();


            try {



                JSONArray jArray = new JSONArray(result);



                // Extract data from json and store into ArrayList as class objects
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

                dispatchnoitem.setText("Total Items: "+String.valueOf(data.size()));
                selecteditem_get.setText("Selected Item ("+String.valueOf(data.size())+")");
                totalcounted=data.size();
                // Setup and Handover data to recyclerview

                mAdapter = new AdapterDispatch(dispatchActivity.this, data);
                progressDialog.dismiss();
                dispatchitem.setAdapter(mAdapter);
                progressDialog.dismiss();
                dispatchitem.setLayoutManager( new LinearLayoutManager(dispatchActivity.this));

            } catch (Exception e) {
                //  Toast.makeText(Function.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }



    private class AsyncFetch_Dispatch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        boolean flag=false;
        String checkflag;



        ProgressDialog progressDialog=new ProgressDialog(dispatchActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params) {
            try {


                url = new URL("https://staginigserver.perfectmandi.com/merchant_folder/csorder.php?id="+userid+"&oid="+orderid);

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


            if ("Data Dispatch Successfully".equalsIgnoreCase(result))
            {
                progressDialog.dismiss();
                Intent intent =new Intent(dispatchActivity.this, MainActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();




            }
        }

    }
}