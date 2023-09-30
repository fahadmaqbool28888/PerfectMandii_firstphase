package com.vendor.perfectmandii;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vendor.perfectmandii.Adapter.AdapterProductAddtoitem;
import com.vendor.perfectmandii.Model.OrderCartModel;
import com.vendor.perfectmandii.Model.StockProductModel;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    private AdapterProductAddtoitem mAdapter;
    int order;

    public ShoppingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoppingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingFragment newInstance(String param1, String param2) {
        ShoppingFragment fragment = new ShoppingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.fragment_shopping, container, false);

        mRVFishPrice=rootview.findViewById(R.id.addtocartitem);
        //  progressBar =  rootview.findViewById(R.id.my_progressBar);

        // setProgressValue(progress);
        //new AsyncFetch().execute();

        return rootview;
    }
   /* private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getContext());
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

                System.out.println("https://staginigserver.perfectmandi.com/cartitem.php?id="+mParam1);
                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("https://staginigserver.perfectmandi.com/cartitem.php?id="+mParam1);

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


            pdLoading.dismiss();
            List<StockProductModel> data=new ArrayList<>();

            pdLoading.dismiss();
            try
            {
                JSONArray jArray = new JSONArray(result);

                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);


                    OrderCartModel fishData = new OrderCartModel();
                    fishData.id=json_data.getString("id");
                    fishData.image_urlname= json_data.getString("image_url");
                    fishData.name= json_data.getString("name");
                    fishData.Accountid= json_data.getString("Accountid");
                    fishData.l2_product_name= json_data.getString("l2_product_name");
                    fishData.category_provider= json_data.getString("category_provider");
                    fishData.status= json_data.getString("status");
                    // fishData.usid=username;

                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview

                mAdapter = new AdapterProductAddtoitem(getContext(), data);
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager( new LinearLayoutManager(getContext()));

            } catch (Exception e) {
            }

        }

    }*/
}