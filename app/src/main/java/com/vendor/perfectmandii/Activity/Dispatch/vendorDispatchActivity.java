package com.vendor.perfectmandii.Activity.Dispatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.Adapter.DashboardAdapter.dashboardAdapter;
import com.vendor.perfectmandii.Adapter.VendorDispatch.dispatchVendor;
import com.vendor.perfectmandii.DBHelper;
import com.vendor.perfectmandii.Dashboard.OPActivity;
import com.vendor.perfectmandii.LoginActivity;
import com.vendor.perfectmandii.Model.dashboardModel.modelDashboard;
import com.vendor.perfectmandii.Model.userVendor;
import com.vendor.perfectmandii.Model.vendorDispatch.vendorDispatchModel;
import com.vendor.perfectmandii.R;
import com.vendor.perfectmandii.profile_Updates.ProfileInformation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SymbolTable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class vendorDispatchActivity extends AppCompatActivity
{
    ArrayList<userVendor> pointModelsArrayList;
    DBHelper dbHandler;
    String storeid;
    List<vendorDispatchModel> data;


    RecyclerView dispatch;
    dispatchVendor DispatchVendor;
    String bill,picture,total;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_dispatch);
        dbHandler = new DBHelper(vendorDispatchActivity.this);
        getValue();
        int_widget();

        Toast.makeText(vendorDispatchActivity.this, storeid, Toast.LENGTH_SHORT).show();
        new AsyncFetch_Add().execute();
    }
    void int_widget()
    {
        dispatch=findViewById(R.id.dispatch_order);
    }


    @Override
    protected void onResume() {
        super.onResume();
       // new AsyncFetch_Add().execute();

    }

    void getValue()
    {
        pointModelsArrayList = dbHandler.readVendor();



        if (pointModelsArrayList.size()>0)
        {
            userVendor pointModel=pointModelsArrayList.get(0);
            storeid=pointModel.storeid;
        }
    }
    private class AsyncFetch_Add extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;
        HttpURLConnection conn;
        ProgressDialog progressDialog;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(vendorDispatchActivity.this);
            progressDialog.setMessage("\tLoading...");
            progressDialog.setTitle("PerfectMandi");
            progressDialog.setIcon(R.drawable.optimizedlogo);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                String liveurl="https://sellerportal.perfectmandi.com/get_fetchTransit.php?id="+storeid;
                url = new URL(liveurl);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try
            {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
                return e1.toString();
            }

            try {

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
                    return (result.toString());
                }
                else
                {
                    return ("unsuccessful"+String.valueOf(response_code));
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


            List<vendorDispatchModel> data=new ArrayList<>();


            try {



                JSONArray jArray = new JSONArray(result);



                for(int i=0;i<jArray.length();i++){
                    //JSONArray jArray = new JSONArray(result);
                    JSONObject json_data = jArray.getJSONObject(i);
                    vendorDispatchModel modeldashboard = new vendorDispatchModel();
                    modeldashboard.orderid= json_data.getString("pi");






                    data.add(modeldashboard);
                }

                // Setup and Handover data to recyclerview

                DispatchVendor = new dispatchVendor(vendorDispatchActivity.this, data);
                progressDialog.dismiss();
                dispatch.setAdapter(DispatchVendor);

                dispatch.setLayoutManager( new LinearLayoutManager(vendorDispatchActivity.this));

            } catch (Exception e)
            {

            }


        }
    }


}
