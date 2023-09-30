package com.vendor.perfectmandii;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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


public class MyorderFragment extends Fragment {



String olp;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    private MyOrderAdapter mAdapter;
    ProgressDialog pdLoading;

    int progress = 0;
    ProgressBar progressBar;
    String session,category,userid;



    public MyorderFragment(String userid,String session,String category)
    {
        this.userid=userid;
        this.session=session;
        this.category=category; // Required empty public constructor

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.fragment_myorder, container, false);
        mRVFishPrice=rootview.findViewById(R.id.product_perfect_mandi);
        //  progressBar =  rootview.findViewById(R.id.my_progressBar);

        // setProgressValue(progress);
        new AsyncFetch().execute();
        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getContext(),"Resume",Toast.LENGTH_LONG).show();

        getFragmentManager().beginTransaction().detach(MyorderFragment.this).attach(MyorderFragment.this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getContext(),"Start",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Toast.makeText(getContext(),"Hello",Toast.LENGTH_LONG).show();

    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        ProgressDialog progressDialog=new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
            progressDialog.setIcon(R.drawable.optimizedlogo);
         /*   pdLoading=new ProgressDialog(getContext());
            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");

            pdLoading.setCancelable(false);
            pdLoading.show();*/

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                System.out.println("https://staginigserver.perfectmandi.com/l2byproduct.php?id="+"OneDollar");

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("https://sellerportal.perfectmandi.com/dailyorder_api.php?id="+userid);

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

            progressDialog.dismiss();
            //pdLoading.dismiss();
            List<OrderPlacedModel> data=new ArrayList<>();


            try {
                //Toast.makeText(MainActivity.this,result.toString(),Toast.LENGTH_LONG).show();


                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    OrderPlacedModel orderPlacedModel=new OrderPlacedModel();
                    //modelProduct fishData = new modelProduct();

                    orderPlacedModel.purchaseInvoice= json_data.getString("purchaseInvoice");
                    //  fishData.status=json_data.getString("status");
                    orderPlacedModel.usersession=session;
                    orderPlacedModel.userid=userid;
                    // fishData.usid=username;

                    data.add(orderPlacedModel);
                }

                // Setup and Handover data to recyclerview
                AdapterInterface listener = new AdapterInterface()
                {
                    @Override
                    public void onClick(String value)
                    {
                        olp=value;
                        new Fetch_UPdate().execute();
                        getFragmentManager().beginTransaction().detach(MyorderFragment.this).attach(MyorderFragment.this);

                    }
                };
                mAdapter = new MyOrderAdapter(getContext(), data,listener);
                mRVFishPrice.setAdapter(mAdapter);

                mRVFishPrice.setLayoutManager( new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

                mAdapter.notifyDataSetChanged();



            } catch (Exception e) {
                //  Toast.makeText(Function.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }


    private class Fetch_UPdate extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        ProgressDialog progressDialog=new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
            progressDialog.setIcon(R.drawable.optimizedlogo);
         /*   pdLoading=new ProgressDialog(getContext());
            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");

            pdLoading.setCancelable(false);
            pdLoading.show();*/

        }

        @Override
        protected String doInBackground(String... params) {
            try
            {

                System.out.println("https://sellerportal.perfectmandi.com/api_processorder.php?id="+olp);
                url = new URL("https://sellerportal.perfectmandi.com/api_processorder.php?id="+olp);

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
            Toast.makeText(getContext(),result,Toast.LENGTH_LONG).show();
            System.out.println(result);

            //this method will be running on UI thread

            progressDialog.dismiss();
            //pdLoading.dismiss();
            if (result.equalsIgnoreCase("Record updated successfully"))
            {
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
            else
            {

            }


        }

    }
}