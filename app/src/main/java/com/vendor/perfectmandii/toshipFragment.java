package com.vendor.perfectmandii;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.vendor.perfectmandii.Adapter.profile.order.IntrasnitOrderAdapter;
import com.vendor.perfectmandii.Adapter.profile.order.MyOrderAdapter;
import com.vendor.perfectmandii.Adapter.profile.order.ShippedOrderAdapter;
import com.vendor.perfectmandii.Dashboard.OPActivity;
import com.vendor.perfectmandii.Model.modelProduct;
import com.vendor.perfectmandii.Model.userVendor;

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


public class toshipFragment extends Fragment {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    private ShippedOrderAdapter mAdapter;
    ProgressDialog pdLoading;

    int progress = 0;
    ProgressBar progressBar;
    String session,category,userid;

    public toshipFragment(String session,String category)
    {

        this.session=session;
        this.category=category; // Required empty public constructor
    }

    ArrayList<userVendor> pointModelsArrayList;

    void init()
    {
        DBHelper dbHandler=new DBHelper(getContext());
        pointModelsArrayList = dbHandler.readVendor();



        if (pointModelsArrayList.size()>0)
        {
            userVendor pointModel=pointModelsArrayList.get(0);

            userid=pointModel.userid;




        }
        else
        {


        }

      /*  userVendor pointModel=pointModelArrayList.get(0);

        tokenValue.setText(pointModel.points);*/
//


    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  pdLoading=new ProgressDialog(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.fragment_toship, container, false);
        mRVFishPrice=rootview.findViewById(R.id.productOrder_perfect_mandi);

        init();
        new AsyncFetch().execute();
        return rootview;
    }


    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        @Override
        protected String doInBackground(String... params)
        {

            try {

                System.out.println("https://sellerportal.perfectmandi.com/process_ShippedOrder.php?id="+userid);
                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("https://sellerportal.perfectmandi.com/process_ShippedOrder.php?id="+userid);

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
                mAdapter = new ShippedOrderAdapter(getContext(), data,listener);
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager( new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                mAdapter.notifyDataSetChanged();



            } catch (Exception e)
            {
            }

        }

    }
}