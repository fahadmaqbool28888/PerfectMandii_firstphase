package com.vendor.perfectmandii.Activity.RFQ;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;
import android.widget.Toast;

import com.vendor.perfectmandii.Activity.RFQ.adapter.DetailRFQIAdapter;
import com.vendor.perfectmandii.Activity.RFQ.fragment.ShowProductFragment;
import com.vendor.perfectmandii.Activity.RFQ.model.DetailRFQ;
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

public class DetailRFQActivity extends AppCompatActivity implements ShowProductFragment.ShowAnInterface {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    DetailRFQIAdapter detailRFQIAdapter;
    RecyclerView recyclerView;

    String stock,qid,qstock,qprice;
    int qpos;

    int pos_1;
int width,height;
    String oid,id,pid,provider,category,sub_category,oos;
    int pos;

    ShowProductFragment alertDialog;
    TextView bar_title;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rfqactivity);

        initializaWidget();
        Intent intent = getIntent();
        String jsonArray = intent.getStringExtra("jsonArray");

        List<DetailRFQ> data=returm_Item(jsonArray);


        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        detailRFQIAdapter= new DetailRFQIAdapter(getApplicationContext(), data) {

            @Override
            public void operationPerform(String flag, DetailRFQ detailRFQ, int position) {

                if (flag.equalsIgnoreCase("oos"))
                {
                    oos=detailRFQ.id;
                    //pos=intent.getIntExtra("pos",0);
                    new AsyncFetch_add().execute();

                    pos_1=position;
                }
                else  if (flag.equalsIgnoreCase("replace"))
                {
                    id=detailRFQ.id;
                    pid=detailRFQ.pid;
                    stock=detailRFQ.getstock;
                    provider=detailRFQ.Provider;
                    category=detailRFQ.Category;
                    sub_category=detailRFQ.Sub_Category;
                    pos_1=position;

                    if (id.equalsIgnoreCase("")&&pid.equalsIgnoreCase(""))
                    {

                    }
                    else
                    {
                        showAlertDialog();
                    }
                }
                else  if (flag.equalsIgnoreCase("confirm"))
                {
                    qid=detailRFQ.id;
                    qprice=detailRFQ.getprice;
                    qstock=detailRFQ.getstock;
                    qpos=position;

                    new AsyncFetch_c().execute();
                }
            }
        };
        recyclerView.setAdapter(detailRFQIAdapter);

}
void initializaWidget()
{
    recyclerView=findViewById(R.id.detailRFQ);

}

List<DetailRFQ> returm_Item(String string) {
    List<DetailRFQ> data = new ArrayList<>();

    try {

        JSONArray jArray = new JSONArray(string);

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject json_data = jArray.getJSONObject(i);


            DetailRFQ stockProductModel = new DetailRFQ();
            stockProductModel.sr = String.valueOf(i);

            stockProductModel.id = json_data.getString("id");
            stockProductModel.name = json_data.getString("Product_Name");
            stockProductModel.des = json_data.getString("Product_Description");
            stockProductModel.img = json_data.getString("image_path");
            stockProductModel.quan = json_data.getString("order_quantity");
            stockProductModel.price = json_data.getString("selling_price");
            stockProductModel.pid = json_data.getString("pid");
            stockProductModel.Provider = json_data.getString("Provider");
            stockProductModel.Category = json_data.getString("Category");
            stockProductModel.Sub_Category = json_data.getString("Sub_Category");


            // fishData.usid=username;

            data.add(stockProductModel);
        }

    } catch (JSONException js)
    {

    }
    return data;
}

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    @Override
    public void sendData(int position) {

        alertDialog.dismiss();
        detailRFQIAdapter.removeAt(position);
       // Toast.makeText(DetailRFQActivity.this,String.valueOf(position),Toast.LENGTH_LONG).show();
    }



    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        alertDialog = ShowProductFragment.newInstance(provider,sub_category,category,stock,id,pos_1);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
       // alertDialog.getActivity().getWindow().setLayout((6 * width)/7, (4 * height)/5);

        alertDialog.show(fm, "fragment_alert");
       // alertDialog.getDialog().getWindow().setLayout((6 * width)/7, (4 * height)/5);




    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(DetailRFQActivity.this,"Pause",Toast.LENGTH_LONG).show();
    }

    private class AsyncFetch_add extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(DetailRFQActivity.this);
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

                String operatingurl="https://sellerportal.perfectmandi.com/oos_mobile.php?id="+oos;

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

            System.out.println(result);
            if (result.contains("Record updated successfully"))
            {


                detailRFQIAdapter.removeAt(pos_1);

            }

            Toast.makeText(DetailRFQActivity.this,result,Toast.LENGTH_LONG).show();

        }

    }

    private class AsyncFetch_c extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(getApplicationContext());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()

        {
            super.onPreExecute();

            //this method will be running on UI thread


        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String operatingurl="https://sellerportal.perfectmandi.com/updateQuantityapi.php?id="+qid+"&price="+qprice+"&quantity="+qstock;

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



            System.out.println(result);
            if (result.contains("Product Data Updated"))
            {

                detailRFQIAdapter.removeAt(qpos);

            }

            Toast.makeText(DetailRFQActivity.this,result,Toast.LENGTH_LONG).show();

        }

    }

    private class AsyncFetch_d extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(DetailRFQActivity.this);
        HttpURLConnection conn;
        URL url = null;

        String oid;
        AsyncFetch_d(String oid)
        {
            this.oid=oid;
        }

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

                String operatingurl="https://sellerportal.perfectmandi.com/close_rfq.php?id="+oid;

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

            System.out.println(result);
            if (result.contains("RFQ Closed"))
            {
                finish();
            }


        }

    }
}