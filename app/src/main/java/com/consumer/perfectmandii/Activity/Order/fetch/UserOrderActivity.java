package com.consumer.perfectmandii.Activity.Order.fetch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.consumer.perfectmandii.Adapter.Order.AdapterPlacedOrder;
import com.consumer.perfectmandii.Customer.UserModel;
import com.consumer.perfectmandii.LocalDB.SQLiteHelper;
import com.consumer.perfectmandii.LoginActivity;
import com.consumer.perfectmandii.Model.OrderCartModel;
import com.consumer.perfectmandii.R;

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

public class UserOrderActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    AdapterPlacedOrder mAdapter;
    //
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    //
    String session,userid;

    ImageView back;
    SQLiteHelper sqLiteHelper;
    List<UserModel> list;

    void get_Data()
    {
        sqLiteHelper=new SQLiteHelper(UserOrderActivity.this);
        list=sqLiteHelper.readCustomer();

        if (list.size()>0)
        {
            UserModel pointModel=list.get(0);
            //textname_1.setText(pointModel.Name);



            session=pointModel.profilepic;
            userid=pointModel.contact;

        }
        else
        {



        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);



        IntializeWidget();
        get_Data();
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        if(session==null)
        {
            Intent intent1=new Intent(UserOrderActivity.this, LoginActivity.class);
            intent1.putExtra("flag","flag6");
            startActivity(intent1);
        }



        new AsyncFetch().execute();


    }


    @Override
    protected void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    void IntializeWidget()
    {
        back=findViewById(R.id.bax);
        recyclerView=findViewById(R.id.Order_basket);
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(UserOrderActivity.this);
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

                System.out.println("https://sellerportal.perfectmandi.com/getorderdetail.php?id="+userid);

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("https://sellerportal.perfectmandi.com/fetch_orderdetail.php?id="+userid);

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
            pdLoading.dismiss();
            List<OrderCartModel> data=new ArrayList<>();

            pdLoading.dismiss();
            try
            {
                JSONArray jArray = new JSONArray(result);

                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    OrderCartModel fishData = new OrderCartModel();
                    fishData.id=json_data.getString("id");
                    fishData.order_id=json_data.getString("order_id");
                    fishData.ordersession=json_data.getString("ordersession");
                    fishData.userid=json_data.getString("userid");
                    fishData.customer_order=json_data.getString("customer_order");
                    fishData.invoice_order=json_data.getString("invoice_order");
                    fishData.subtotal=json_data.getString("subtotal");
                    fishData.grandtotal=json_data.getString("grandtotal");
                    fishData.discount=json_data.getString("discount");
                    fishData.packaging=json_data.getString("packaging");
                    fishData.order_place=json_data.getString("order_place");
                    fishData.paid_status=json_data.getString("status");
                    fishData.session=session;

                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview

                mAdapter = new AdapterPlacedOrder(UserOrderActivity.this, data);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager( new LinearLayoutManager(UserOrderActivity.this));


            } catch (Exception e) {
            }

        }

    }
}