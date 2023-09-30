package com.vendor.perfectmandii;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vendor.perfectmandii.Adapter.profile.order.IntrasnitOrderAdapter;
import com.vendor.perfectmandii.Adapter.profile.order.MyOrderAdapter;
import com.vendor.perfectmandii.Model.modelProduct;

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


public class topayFragment extends Fragment {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    private IntrasnitOrderAdapter mAdapter;
    ProgressDialog pdLoading;

    int progress = 0;
    ProgressBar progressBar;
    String session,category,userid;

    public topayFragment(String userid) {
        this.userid = userid;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pdLoading=new ProgressDialog(getContext());

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.fragment_topay, container, false);
        mRVFishPrice=rootview.findViewById(R.id.product_perfect_mandi);
        //  progressBar =  rootview.findViewById(R.id.my_progressBar);

        // setProgressValue(progress);
        new   AsyncFetch().execute();
        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String state = intent.getStringExtra("state");


            if ("refresh".equalsIgnoreCase(state))
            {
                new AsyncFetch().execute();
            }
           /* new AsyncFetch().execute();*/

        }
    };
    private class AsyncFetch extends AsyncTask<String, String, String> {
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

              //  System.out.println("https://sellerportal.perfectmandi.com/process_orderIntransit.php?id="+userid);
                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
               // url = new URL("https://sellerportal.perfectmandi.com/process_orderIntransit.php?id="+userid);

                url = new URL("https://sellerportal.perfectmandi.com/temperfile.php?id="+userid);

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



            List<OrderPlacedModel> data=new ArrayList<>();


            try {


                JSONArray jArray = new JSONArray(result);

                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    OrderPlacedModel orderPlacedModel=new OrderPlacedModel();

                    orderPlacedModel.purchaseInvoice= json_data.getString("purchaseInvoice");
                    orderPlacedModel.total=json_data.getString("total");
                    orderPlacedModel.jsonArray=json_data.getJSONArray("catalog");
                    orderPlacedModel.usersession=session;
                    orderPlacedModel.userid=userid;

                    data.add(orderPlacedModel);
                }

                IntansitAdapterInterface listener = new IntansitAdapterInterface()
                {
                    @Override
                    public void onClick(String value)
                    {
                        Intent intent=new Intent(getContext(),OrderTransitionActivity.class);
                        intent.putExtra("userid",userid);
                        intent.putExtra("pi",value);
                        startActivity(intent);
                    }
                };
                InTransit_UPdate listener1 = new InTransit_UPdate() {
                    @Override
                    public void onClick(String value)
                    {

                    }
                };

                mAdapter = new IntrasnitOrderAdapter(getContext(), data,listener);
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager( new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                mAdapter.notifyDataSetChanged();



            } catch (Exception e)
            {
            }

        }

    }
}