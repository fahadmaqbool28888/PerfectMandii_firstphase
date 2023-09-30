package com.vendor.perfectmandii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.vendor.perfectmandii.Adapter.Payment.Bank.bankPayment;
import com.vendor.perfectmandii.Model.Bank.BankModel;
import com.vendor.perfectmandii.Model.OrderModel.ordermodel;

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

public class BankDetailActivity extends AppCompatActivity
{

    RecyclerView recyclerView;
    String requestURL = "https://staginigserver.perfectmandi.com/bd.php";
    ordermodel orderModel;

    bankPayment orderConfirmation;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String session, userid, orderid;
    CardView checkoutforshopping;
    BankModel bankModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_detail);
        intializewidget();

        Intent intent = getIntent();
        session = intent.getStringExtra("session");
        userid = intent.getStringExtra("userid");
        orderid = intent.getStringExtra("orderid");





        new AsyncFetch().execute();

    }

    void intializewidget() {
        recyclerView = findViewById(R.id.shoppingbasket);
        checkoutforshopping = findViewById(R.id.checkoutforshopping);
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        ProgressDialog pdLoading = new ProgressDialog(BankDetailActivity.this);
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
                url = new URL(requestURL);
               /* url = new URL(requestURL + orderid);*/

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
            List<BankModel> data = new ArrayList<>();
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++)
                {
                    JSONObject json_data = jArray.getJSONObject(i);bankModel = new BankModel();
                   bankModel.name=json_data.getString("name");
                   bankModel.branhcode=json_data.getString("branchcode");
                   bankModel.branchAccNum=json_data.getString("branchAccNum");
                   bankModel.branchAccTitle=json_data.getString("branchAccTitle");
                   bankModel.banklogo = json_data.getString("branchlogo");
                   data.add(bankModel);
                }
                orderConfirmation = new bankPayment(BankDetailActivity.this, data);
                recyclerView.setAdapter(orderConfirmation);
                recyclerView.setLayoutManager(new LinearLayoutManager(BankDetailActivity.this));
            } catch (Exception e)
            {
                Toast.makeText(BankDetailActivity.this,e.toString(),Toast.LENGTH_LONG).show();
            }

        }
    }
}