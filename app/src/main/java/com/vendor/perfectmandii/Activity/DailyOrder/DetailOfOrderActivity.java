package com.vendor.perfectmandii.Activity.DailyOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vendor.perfectmandii.Adapter.DailyOrderAdapter;
import com.vendor.perfectmandii.Adapter.DetailOrderAdapter;
import com.vendor.perfectmandii.Adapter.OrderDetailsAdapter;
import com.vendor.perfectmandii.DetailOrderActivity;
import com.vendor.perfectmandii.DetailOrderModel;
import com.vendor.perfectmandii.Model.DailyOrder.orderDaily;
import com.vendor.perfectmandii.OrderDetailAdapter;
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

public class DetailOfOrderActivity extends AppCompatActivity
{
    ArrayList<DetailOrderModel> list;

    String id,order;
    ListView listView;
    TextView itemCount,orderValue,order_num_,detailaddress,confirm_function;
    ImageView sidnav;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_order);
        init();
        listView = (ListView) findViewById(R.id.lvItems);
        list=new ArrayList<>();
        Intent intent=getIntent();
        order=intent.getStringExtra("order");
        String data=intent.getStringExtra("data");
        String data1=intent.getStringExtra("data1");
        String data2=intent.getStringExtra("data2");

        System.out.println("Detail of order activity "+data);
        System.out.println("Detail of order activity "+data1);
        System.out.println("Detail of order activity "+data2);


        interpret_Json(data);
        interpret_Item(data2);
        interpret_Address(data1);
        order_num_.setText(order);
        //list.add()
        //
        sidnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //

        confirm_function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncFetch(order).execute();
            }
        });
    }


    void init()
    {
        sidnav=findViewById(R.id.sidnav);
        itemCount=findViewById(R.id.itemCount);
        orderValue=findViewById(R.id.orderValue);
        order_num_=findViewById(R.id.order_num_);
        detailaddress=findViewById(R.id.detailaddress);
        confirm_function=findViewById(R.id.confirm_function);
    }
    void interpret_Address(String jsonData2)
    {


        try
        {
            JSONArray jArray = new JSONArray(jsonData2);

            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);


                DetailOrderModel daily = new DetailOrderModel();
                daily.shippingAddress=json_data.getString("shippingAddress");


                detailaddress.setText(daily.shippingAddress);



                //daily.orderdetail=json_data.getJSONObject("orderdetail");




            }



        } catch (Exception e) {
        }
    }
    void interpret_Json(String jsonData)
    {

        try
        {
            JSONArray jArray = new JSONArray(jsonData);

            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);


                DetailOrderModel daily = new DetailOrderModel();
                daily.Product_Name=json_data.getString("Product_Name");
                daily.order_quantity=json_data.getString("order_quantity");
                daily.selling_price=json_data.getString("selling_price");
                daily.total_price=json_data.getString("total_price");

                System.out.println(daily.Product_Name+"\n");
                System.out.println(daily.order_quantity+"\n");
                System.out.println(daily.selling_price+"\n");
                System.out.println(daily.total_price+"\n");
                //daily.orderdetail=json_data.getJSONObject("orderdetail");

list.add(daily);
//list.add(daily.Product_Name);
               // data.add(daily);
            }

            OrderDetailsAdapter adapter = new OrderDetailsAdapter(this, list);

            listView.setAdapter(adapter);


        } catch (Exception e) {
        }


    }
    void interpret_Item(String jsonData1)
    {



        System.out.println("This ite"+jsonData1);
        /*  pdLoading.dismiss();*/
        try
        {
            JSONArray jArray = new JSONArray(jsonData1);

            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);


                DetailOrderModel daily = new DetailOrderModel();
                daily.ItemCount=json_data.getString("ItemCount");
                daily.totalvalue=json_data.getString("totalvalue");


                itemCount.setText(daily.ItemCount);
                orderValue.setText("PKR "+daily.totalvalue);

                //daily.orderdetail=json_data.getJSONObject("orderdetail");




            }



        } catch (Exception e) {
        }
    }
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(DetailOfOrderActivity.this);
        HttpURLConnection conn;
        URL url = null;

        String po;
        public AsyncFetch(String po)
        {
            this.po=po;
        }

        @Override
        protected void onPreExecute() {
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
String urls="https://sellerportal.perfectmandi.com/process_order_mv.php?id="+po;
System.out.println(urls);

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(urls);

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


            //List<orderDaily> data=new ArrayList<>();


            if (result.equalsIgnoreCase("Record updated successfully")) {
                pdLoading.dismiss();
                finish();
            }


        }
    }

}
