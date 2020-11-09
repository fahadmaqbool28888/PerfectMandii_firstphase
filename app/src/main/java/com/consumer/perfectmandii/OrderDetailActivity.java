package com.consumer.perfectmandii;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.consumer.perfectmandii.Adapter.ContentAdapter;
import com.consumer.perfectmandii.Model.ModelDas;

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


public class OrderDetailActivity extends AppCompatActivity
{    int oc,tdeliv;
    String customer_order,invoice_order,order_place,grandtotal;
    List<ModelTemp> modelTemps;
    String deliver_url,complete_url;
    OrderConsigneeAdapter orderConsigneeAdapter;
    RecyclerView recyclerView;
    /*     intent.putExtra("customer_order",current.customer_order);
                    intent.putExtra("invoice_order",current.invoice_order);
                    intent.putExtra("order_place",current.order_place);
                    intent.putExtra("paid_status",current.paid_status);
                    intent.putExtra("grandtotal",current.grandtotal);*/


    TextView customerorder,invoiceorder,orderplace,grand_total,in,osp,td;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_OrderDetailActivity);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //
        modelTemps=new ArrayList<>();
        //
        Intent intent=getIntent();
        customer_order=intent.getStringExtra("customer_order");
        invoice_order=intent.getStringExtra("invoice_order");
        order_place=intent.getStringExtra("order_place");
        grandtotal=intent.getStringExtra("grandtotal");
        init_widget();



        customerorder.setText(customer_order);
        invoiceorder.setText(invoice_order);
        orderplace.setText(order_place);
        grand_total.setText(grandtotal);


        new AsyncFetch().execute();


        if (oc!=tdeliv)
        {

        }
        else
        {

            }


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
    }


    void  init_widget()
    {
        //customer_order,invoice_order,order_place,grandtotal
        customerorder=findViewById(R.id.customer_order);
        invoiceorder=findViewById(R.id.invoice_order);
        orderplace=findViewById(R.id.order_place);
        grand_total=findViewById(R.id.grandtotal);
        recyclerView=findViewById(R.id.rec_pl);
        //in,osp,td;
        in=findViewById(R.id.in);
        osp=findViewById(R.id.osp);
        td=findViewById(R.id.td);
    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String s=intent.getStringExtra("in");


            try {
              show_Dialog(s,customer_order);
            }
            catch (WindowManager.BadTokenException e) {
                //use a log message
            }





        }
    };



    void show_Dialog(String s,String customer_order)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
        builder.setIcon(R.drawable.optimizedlogo);
        builder.setTitle("PerfectMandi");
        builder.setMessage("Did you recieve your order?");

        builder.create();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                Toast.makeText(OrderDetailActivity.this, s+" "+customer_order, Toast.LENGTH_SHORT).show();


                deliver_url="https://sellerportal.perfectmandi.com/process_DeliverOrder.php?pi="+s+"&oi="+customer_order;
                new Async_deliver().execute();



            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        builder.show();
    }
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private class AsyncFetch extends AsyncTask<String, String, String> {


        ProgressDialog progressDialog = new ProgressDialog(OrderDetailActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Perfect Mandi");
            progressDialog.setIcon(R.drawable.optimizedlogo);
            // progressDialog.setMessage("Data is Loading...");
            progressDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            try
            {


                url = new URL("https://sellerportal.perfectmandi.com/process_orderDetail.php?id="+customer_order);

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
                conn.setDoOutput(true);
            }
            catch (IOException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }
            try
            {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK)
                {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
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
            progressDialog.dismiss();
            try
            {
                JSONArray jArray = new JSONArray(result);
                for(int i=0;i<jArray.length();i++)
                {
                    JSONObject json_data = jArray.getJSONObject(i);

                    ModelTemp modelTemp=new ModelTemp();
                    osp.setText(json_data.getString("oc_total"));
                    td.setText(json_data.getString("delivered"));
                    in.setText(json_data.getString("intransit"));

                  oc=Integer.parseInt(json_data.getString("oc_total"));
                  tdeliv=Integer.parseInt(json_data.getString("delivered"));
                    if (oc!=tdeliv)
                    {
                        modelTemp.OrderArray=json_data.getJSONArray("deliv");
                        modelTemps.add(modelTemp);
                        orderConsigneeAdapter=new OrderConsigneeAdapter(OrderDetailActivity.this,modelTemp.OrderArray);
                        recyclerView.setAdapter(orderConsigneeAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this));
                    }
                    else
                    {

                        modelTemp.OrderArray=json_data.getJSONArray("deliv");
                        modelTemps.add(modelTemp);
                        orderConsigneeAdapter=new OrderConsigneeAdapter(OrderDetailActivity.this,modelTemp.OrderArray);
                        recyclerView.setAdapter(orderConsigneeAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this));

                        showD();

                    }



                }


            }
            catch (Exception exception)
            {
            }

        }

    }

    void showD()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
        builder.setIcon(R.drawable.optimizedlogo);
        builder.setTitle("PerfectMandi");
        builder.setMessage("Did you recieve your order?");

        builder.create();
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {



                complete_url="https://sellerportal.perfectmandi.com/process_completeOrder.php?oi="+customer_order;

                new Async_Complete().execute();



            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        builder.show();
    }


    //
    private class Async_deliver extends AsyncTask<String, String, String> {


        ProgressDialog progressDialog = new ProgressDialog(OrderDetailActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Perfect Mandi");
            progressDialog.setIcon(R.drawable.optimizedlogo);
            // progressDialog.setMessage("Data is Loading...");
            progressDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            try
            {


                url = new URL(deliver_url);

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
                conn.setDoOutput(true);
            }
            catch (IOException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }
            try
            {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK)
                {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
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
            Toast.makeText(OrderDetailActivity.this, result, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            if (result.equalsIgnoreCase("Record Update Successfully"))
            {

                finish();
                startActivity(getIntent());
            }



        }

    }


    private class Async_Complete extends AsyncTask<String, String, String> {


        ProgressDialog progressDialog = new ProgressDialog(OrderDetailActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Perfect Mandi");
            progressDialog.setIcon(R.drawable.optimizedlogo);
            // progressDialog.setMessage("Data is Loading...");
            progressDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            try
            {


                url = new URL(complete_url);

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
                conn.setDoOutput(true);
            }
            catch (IOException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }
            try
            {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK)
                {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
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
            Toast.makeText(OrderDetailActivity.this, result, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            if (result.equalsIgnoreCase("Record Update Successfully"))
            {


            }



        }

    }

}