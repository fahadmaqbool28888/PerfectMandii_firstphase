package com.vendor.perfectmandii.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vendor.perfectmandii.Adapter.AdapterHome;
import com.vendor.perfectmandii.Adapter.Vendor.myorder;
import com.vendor.perfectmandii.Adapter.Vendor.paidorder;
import com.vendor.perfectmandii.HomeFragment;
import com.vendor.perfectmandii.Model.ModelPaid.OrderModel;
import com.vendor.perfectmandii.Model.vendor.VendorConfirmoRDER;
import com.vendor.perfectmandii.Model.vendor.neworderinfo;
import com.vendor.perfectmandii.Model.vendor.vendorServiceModel;
import com.vendor.perfectmandii.R;

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


public class NewOrderFragment extends Fragment {

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
    private paidorder mAdapter;
    String Action,number;

    public NewOrderFragment(String Action,String number)
    {
        this.Action=Action;
        this.number=number;
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.fragment_new_order, container, false);
        mRVFishPrice=rootview.findViewById(R.id.my_order);
        // Toast.makeText(getContext(),mParam1+" "+mParam2,Toast.LENGTH_LONG).show();
        new AsyncFetch().execute();

        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        new AsyncFetch().execute();
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        ProgressDialog progressDialog=new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Fetching Order Detail...");
            progressDialog.setIcon(R.drawable.optimizedlogo);
            progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String urls=null;

                if (Action.equalsIgnoreCase("paid"))
                {
                    urls="https://staginigserver.perfectmandi.com/admin_folder/fetchcorder.php";
                //   urls="https://staginigserver.perfectmandi.com/getVendororder.php?action="+Action+"&num="+number;
                }
                else if (Action.equalsIgnoreCase("transit"))
                {
                    urls="https://staginigserver.perfectmandi.com/merchant_folder/fetchtransitorder.php";
                  //  urls="https://staginigserver.perfectmandi.com/getVendororder.php?action="+Action+"&num="+number;
                }
                else if (Action.equalsIgnoreCase("complete"))
                {
                    urls="https://staginigserver.perfectmandi.com/admin_folder/fetchcorder.php";
                   // urls="https://staginigserver.perfectmandi.com/getVendororder.php?action="+Action+"&num="+number;
                }




                url = new URL(urls);

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

            progressDialog.dismiss();

          //  Toast.makeText(getContext(),result,Toast.LENGTH_LONG).show();
            List<OrderModel> data=new ArrayList<>();


            try {
/*    "customer_order": "73539862",
        "userid": "03085588820",
        "subtotal": "600",
        "invoice_order": "#F6044727d2e8de",
        "shipping_address": "house 7 street 6 Raja  Iqbal Town Rawalpindi"*/


                JSONArray jArray = new JSONArray(result);


                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    OrderModel cpm = new OrderModel();


                    cpm.customer_order=json_data.getString("customer_order");
                    cpm.date=json_data.getString("Date");
                    cpm.orderto=json_data.getString("orderto");
                    cpm.orderby=json_data.getString("orderby");
                    cpm.Destination=json_data.getString("Destination");
                    cpm.orderid=json_data.getString("orderid");
                    cpm.userid=json_data.getString("userid");
                    cpm.image_path=json_data.getString("image_path");
                    cpm.deposit_amount=json_data.getString("deposit_amount");
                    cpm.depositdate=json_data.getString("depositdate");

                        /*    public String grandtotal;
    public String discount;
    public String packaging;
    public String subtotal;*/


                        /*   "grandtotal": "600",
        "dicount": "0",
        "packaging": "100",
        "subtotal": "500"*/
                    cpm.grandtotal=json_data.getString("grandtotal");
                    cpm.subtotal=json_data.getString("subtotal");
                    cpm.packaging=json_data.getString("packaging");
                    cpm.discount=json_data.getString("dicount");


                    data.add(cpm);
                }

                // Setup and Handover data to recyclerview

                mAdapter = new paidorder(getContext(), data);
                progressDialog.dismiss();
                mRVFishPrice.setAdapter(mAdapter);
                progressDialog.dismiss();
                mRVFishPrice.setLayoutManager( new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

            } catch (Exception e) {
                //  Toast.makeText(Function.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }
}