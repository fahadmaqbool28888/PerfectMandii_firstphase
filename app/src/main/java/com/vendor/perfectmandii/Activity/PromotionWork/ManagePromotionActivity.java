package com.vendor.perfectmandii.Activity.PromotionWork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.Adapter.DashboardAdapter.dashboardAdapter;
import com.vendor.perfectmandii.Adapter.Vendor.productofvendor;
import com.vendor.perfectmandii.DBHelper;
import com.vendor.perfectmandii.Dashboard.OPActivity;
import com.vendor.perfectmandii.LoginActivity;
import com.vendor.perfectmandii.Model.ProductModel.ProductModel;
import com.vendor.perfectmandii.Model.dashboardModel.modelDashboard;
import com.vendor.perfectmandii.Model.userVendor;
import com.vendor.perfectmandii.R;
import com.vendor.perfectmandii.profile_Updates.ProfileInformation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

public class ManagePromotionActivity extends AppCompatActivity
{
    boolean listing=false;
    productofvendor vendorProduct;
    ProgressDialog progressDialog;
    ArrayList<userVendor> pointModelsArrayList;
    DBHelper dbHandler;

    String id;
    RecyclerView recyclerView;
    CardView dropdown;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_promotion);
        dbHandler = new DBHelper(ManagePromotionActivity.this);
        init_widget();
        init();
        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Setup and Handover data to recyclerview



            if (listing)
            {
               recyclerView.setVisibility(View.GONE);
               listing=false;
            }
            else
            {
                recyclerView.setAdapter(vendorProduct);
                recyclerView.setVisibility(View.VISIBLE);
                //progressDialog.dismiss();
                recyclerView.setLayoutManager( new LinearLayoutManager(ManagePromotionActivity.this));
                listing=true;
            }


            }
        });
    }
    void init_widget()
    {
        dropdown=findViewById(R.id.dropdown);
        recyclerView=findViewById(R.id.teamlistright);
    }
    private class AsyncFetch_Add extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;

        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(ManagePromotionActivity.this);
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
                String liveurl="https://sellerportal.perfectmandi.com/get_VendorProductList.php?id="+id;
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

            progressDialog.dismiss();

            List<ProductModel> data=new ArrayList<>();


            try {



                JSONArray jArray = new JSONArray(result);



                for(int i=0;i<jArray.length();i++){
                    //JSONArray jArray = new JSONArray(result);
                    JSONObject json_data = jArray.getJSONObject(i);
                    ProductModel productModel = new ProductModel();
                    productModel.productName= json_data.getString("name");
                    productModel.imageUrl= json_data.getString("path");



                    System.out.println(productModel.productName);



                    data.add(productModel);
                }
                vendorProduct = new productofvendor(ManagePromotionActivity.this, data);
                progressDialog.dismiss();

            } catch (Exception e)
            {

            }


        }
    }

    void init()
    {
        pointModelsArrayList= dbHandler.readVendor();

        if (pointModelsArrayList.size()>0)
        {
            userVendor pointModel=pointModelsArrayList.get(0);

            System.out.println(pointModel.storeid+"\n");

 id=pointModel.storeid;

 new AsyncFetch_Add().execute();


        }
        else
        {
            Toast.makeText(ManagePromotionActivity.this,"No Recorfd", Toast.LENGTH_SHORT).show();

        }



    }
}