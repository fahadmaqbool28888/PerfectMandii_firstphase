package com.vendor.perfectmandii;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vendor.perfectmandii.Adapter.AdapterHome;
import com.vendor.perfectmandii.Model.DataFish;
import com.vendor.perfectmandii.Model.vendor.vendorServiceModel;

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


public class HomeFragment extends Fragment
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    Context context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    private AdapterHome mAdapter;


    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2,String param3) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3,param3);

        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3=getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.fragment_home, container, false);
        mRVFishPrice=rootview.findViewById(R.id.pperfect_mandi);
       // Toast.makeText(getContext(),mParam1+" "+mParam2,Toast.LENGTH_LONG).show();
        new AsyncFetch().execute();

        return rootview;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }




    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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



        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                url = new URL("https://staginigserver.perfectmandi.com/getservice_V.php");
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
                return e.toString();
            }
            try
            {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            }
            catch (IOException e1)
            {
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



            List<vendorServiceModel> data=new ArrayList<>();


            try {



                JSONArray jArray = new JSONArray(result);



                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    vendorServiceModel fishData = new vendorServiceModel();
                    fishData.serviceicon= json_data.getString("serviceicon");
                    fishData.servicename= json_data.getString("servicename");
                    fishData.usertype= json_data.getString("usertype");
                    fishData.servicestatus=json_data.getString("status");
                    fishData.usernumber=mParam2;


                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview

                mAdapter = new AdapterHome(getContext(), data);
                progressDialog.dismiss();
                mRVFishPrice.setAdapter(mAdapter);
                progressDialog.dismiss();
                mRVFishPrice.setLayoutManager( new GridLayoutManager(getContext(),4));

            } catch (Exception e)
            {

            }

        }

    }
}