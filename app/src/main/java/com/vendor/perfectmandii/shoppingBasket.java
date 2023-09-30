package com.vendor.perfectmandii;

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
import android.widget.TextView;
import android.widget.Toast;

import com.vendor.perfectmandii.Adapter.AdapterProductAddtoitem;
import com.vendor.perfectmandii.Model.OrderCartModel;
import com.vendor.perfectmandii.Model.StockProductModel;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class
shoppingBasket extends AppCompatActivity
{
    String json_string;
    int count=0;
    RecyclerView shoppingbasket;
    String session,userid;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private AdapterProductAddtoitem mAdapter;
    TextView textView,totalbill;
    CardView checkoutforshopping;
    ImageView backsc;
    int total=0;
    int total1;
    String ordernum;
    ArrayList<String> productid,productprice,totalamount,productquantity,vendorid,cu,productionidentif;
    String value;

    String op;

    String currentDate;
    String vendor;
    int ordernumber;
    String nextorderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_basket);

        Intent intent=getIntent();
        session=intent.getStringExtra("session");
        userid=intent.getStringExtra("userid");
        if(session==null)
        {
            Intent intent1=new Intent(shoppingBasket.this, LoginActivity.class);
            startActivity(intent1);
        }
        checkorder();
        //
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        productid=new ArrayList<>();

        productprice=new ArrayList<>();
        totalamount=new ArrayList<>();
        productquantity=new ArrayList<>();
        vendorid=new ArrayList<>();
        cu=new ArrayList<>();
        productionidentif=new ArrayList<>();
        //
        intializewidget();
        shoppingbasket.setNestedScrollingEnabled(false);
        backsc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();

            }
        });
        new AsyncFetch().execute();



        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
        checkoutforshopping.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {




                String str=JsonString();
                if(str.equalsIgnoreCase("{\"upload_fishes\":]}"))
                {
                    Toast.makeText(shoppingBasket.this,"Please Add Item",Toast.LENGTH_LONG).show();
                }
                else
                {
                 ordernumber=Integer.parseInt(ordernum)+1;
                 nextorderNumber=String.valueOf(ordernumber);
                 new AsyncFetch_add().execute();
                }






            }
        });
    }




    String JsonString() {

        int i;


json_string=null;



        try {
            //Repeat and loop this until all objects are added (and add try+catch)
            json_string ="{\"upload_fishes\":[";
            for (i=0;i<productprice.size();i++)
            {

                JSONObject obj_new = new JSONObject();

                int v1=Integer.parseInt(ordernum);
                int v2=v1+1;
                op=String.valueOf(v2);

                obj_new.put("orderid",String.valueOf(v2));
                obj_new.put("productid",productionidentif.get(i));
                obj_new.put("productprice",productprice.get(i));
                obj_new.put("quan",productquantity.get(i));

                obj_new.put("vendor",vendorid.get(i));
                obj_new.put("customer",cu.get(i));

                obj_new.put("date",currentDate);
                json_string = json_string + obj_new.toString() + ",";

//Close JSON string

            }
            json_string = json_string.substring(0, json_string.length()-1);
            json_string += "]}";



        }
        catch (JSONException jsox)
        {
            Toast.makeText(shoppingBasket.this,jsox.toString(),Toast.LENGTH_LONG).show();
        }

        return  json_string;
    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent

            String id=intent.getStringExtra("id");
       productionidentif.add(id);
            String qty = intent.getStringExtra("quantity");
            productquantity.add(qty);
            String price=intent.getStringExtra("price");

             vendor=intent.getStringExtra("vendorid");
            vendorid.add(vendor);
            String con=intent.getStringExtra("sellerid");
            cu.add(con);


            int quan=Integer.parseInt(qty);
            int temp1=quan;
            int prices=Integer.parseInt(price);
            int temp2=prices;
            total1=temp1*temp2;



            String flag=intent.getStringExtra("flag");

            if(flag.equalsIgnoreCase("exclude"))
            {

                count=count-1;
                value=String.valueOf(count);
                textView.setText("Selected Item ("+value+")");
                total=total-total1
                ;
                totalbill.setText("Total Amount:"+String.valueOf(total));




            }
            else
            {

                count=count+1;
                value=String.valueOf(count);



                productprice.add(price);



                total1=temp1*temp2;
                total=total1+total;

                textView.setText("Selected Item ("+value+")");
                totalbill.setText("Total Amount:"+String.valueOf(total));
            }

        }
    };
    void intializewidget()
    {
        shoppingbasket=findViewById(R.id.shoppingbasket);
        textView=findViewById(R.id.selecteditem);
        totalbill=findViewById(R.id.totalbill);
        checkoutforshopping=findViewById(R.id.checkoutforshopping);
        backsc=findViewById(R.id.bax);
    }






    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(shoppingBasket.this);
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
                url = new URL("https://staginigserver.perfectmandi.com/get_ProductStock.php?id="+userid);

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




                    // fishData.usid=username;

                    data.add(stockProductModel);
                }

                // Setup and Handover data to recyclerview

                mAdapter = new AdapterProductAddtoitem(shoppingBasket.this, data);
                shoppingbasket.setAdapter(mAdapter);
                shoppingbasket.setLayoutManager( new LinearLayoutManager(shoppingBasket.this));


            } catch (Exception e) {
            }
        }

    }



    private class AsyncFetch_add extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(shoppingBasket.this);
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

                String operatingurl="https://staginigserver.perfectmandi.com/addmultipleitem.php?data="+json_string+"&id="+nextorderNumber+"&pid="+userid+"&sess="+session;

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

            pdLoading.dismiss();


            Intent intent=new Intent(shoppingBasket.this,OrderViewActivity.class);
            intent.putExtra("session",session);
            intent.putExtra("userid",userid);
            intent.putExtra("orderid",nextorderNumber);
            intent.putExtra("total",String.valueOf(total));
            startActivity(intent);

        }

    }

    void checkorder()
    {
        if(session==null)
        {

        }
        else
        {
            new AsyncFetch_check().execute();

        }
    }


    private class AsyncFetch_check extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            try {


                url = new URL("https://staginigserver.perfectmandi.com/ordersessionuser.php?id="+userid);

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


           ordernum = result;
           // Toast.makeText(shoppingBasket.this,"This is"+ordernum,Toast.LENGTH_LONG).show();

        }

    }





}