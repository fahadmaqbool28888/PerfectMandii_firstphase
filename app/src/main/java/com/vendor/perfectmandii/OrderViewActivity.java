package com.vendor.perfectmandii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vendor.perfectmandii.Adapter.OrderConfirmation;
import com.vendor.perfectmandii.Model.OrderModel.ordermodel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderViewActivity extends AppCompatActivity
{


    RecyclerView recyclerView;
    String requestURL="https://staginigserver.perfectmandi.com/oc.php?id=";
    ordermodel orderModel;

    TextView dateandtime,subtotal,discount,totalordervalue,packingcharges;
    OrderConfirmation orderConfirmation;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String session,userid,orderid;
    CardView checkoutforshopping;
    LinearLayout previousbalance,invoicenum,ordernum;
    String total;
    int packingcharge=100;
    int grandtotal,discountvalue;
    String stotal,gtotal,packaging,discountprice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);

        intializewidget();

        setdateandtime();
        packingcharges.setText("Rs : "+String.valueOf(packingcharge));
        previousbalance.setVisibility(View.INVISIBLE);
        invoicenum.setVisibility(View.INVISIBLE);
        ordernum.setVisibility(View.INVISIBLE);

        Intent intent=getIntent();
      session = intent.getStringExtra("session");
      userid=intent.getStringExtra("userid");
      orderid=intent.getStringExtra("orderid");
      total=intent.getStringExtra("total");
      grandtotal=Integer.parseInt(total)+packingcharge;
      subtotal.setText("Rs : "+total);
      discount.setText("Rs : "+"0");
      totalordervalue.setText("Rs : "+String.valueOf(grandtotal));


      gtotal=String.valueOf(grandtotal);
      stotal=String.valueOf(total);
      packaging=String.valueOf(packingcharge);
      discountprice="0";




    //

      new AsyncFetch_add().execute();


   //   new AsyncFetch().execute();




      checkoutforshopping.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent1=new Intent(OrderViewActivity.this,MainActivity.class);
              intent1.putExtra("session",session);
              intent1.putExtra("userid",userid);
              startActivity(intent1);
            //  System.out.println(orderid);
          }
      });

    }
    void setdateandtime()  {
      try {
          String dateStr = "04/05/2010";

          SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
          Date dateObj = curFormater.parse(dateStr);
          SimpleDateFormat postFormater = new SimpleDateFormat("MMMM dd, yyyy");

          String newDateStr = postFormater.format(dateObj);
          dateandtime.setText(newDateStr);
      }
      catch (Exception ex)
      {
          Toast.makeText(OrderViewActivity.this,ex.toString(),Toast.LENGTH_LONG).show();
      }
    }


    void intializewidget()
    {
        invoicenum=findViewById(R.id.invoiceno);
        ordernum=findViewById(R.id.orderno);
        packingcharges=findViewById(R.id.packingcharges);
        subtotal=findViewById(R.id.subtotal);
        discount=findViewById(R.id.discountprice);
        recyclerView=findViewById(R.id.shoppingbasket);
        checkoutforshopping=findViewById(R.id.checkoutforshopping);
        dateandtime=findViewById(R.id.dateandtime);
        previousbalance=findViewById(R.id.previous_balance);
        totalordervalue=findViewById(R.id.totalordervalue);

    }
    private class AsyncFetch_add extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(OrderViewActivity.this);

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



                url = new URL("https://staginigserver.perfectmandi.com/placeorder.php?id="+orderid+"&session="+session+"&userid="+userid+"&stotal="+stotal+"&gtotal="+gtotal+"&discount="+discountprice+"&pack="+packaging);

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
            pdLoading.dismiss();
            //Toast.makeText(OrderViewActivity.this,result,Toast.LENGTH_LONG).show();
            new AsyncFetch().execute();
        }

    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        ProgressDialog pdLoading = new ProgressDialog(OrderViewActivity.this);
        HttpURLConnection conn;

        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();


        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String urls=requestURL+orderid;

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
        protected void onPostExecute(String result)
        {

            //this method will be running on UI thread
            pdLoading.dismiss();

        }

    }


}