package com.vendor.perfectmandii.Activity.RFQ.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vendor.perfectmandii.Activity.RFQ.DetailRFQActivity;
import com.vendor.perfectmandii.Activity.RFQ.adapter.DetailRFQIAdapter;
import com.vendor.perfectmandii.Activity.RFQ.adapter.VendorADDAdapter;
import com.vendor.perfectmandii.Activity.RFQ.model.DetailRFQ;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowProductFragment extends DialogFragment
{


    ShowAnInterface listener;
    int position;
    BroadcastReceiver br;
    TextView header_r_title;
    VendorADDAdapter detailRFQIAdapter;
    RecyclerView rfq_rclr;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";

    private static final String ARG_PARAM6 = "param6";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    private String mParam5;

    private int mParam6;
    String pr_id,pr_quan,pr_price;


    public ShowProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowProductFragment newInstance(String param1, String param2,String param3,String param4,String param5,int position) {
        ShowProductFragment fragment = new ShowProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4,param4);
        args.putString(ARG_PARAM5,param5);
        args.putInt(ARG_PARAM6,position);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener= (ShowAnInterface) context;

        }
        catch (ClassCastException exception)
        {
            throw new ClassCastException(context.toString()+"");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4=getArguments().getString(ARG_PARAM4);
            mParam5=getArguments().getString(ARG_PARAM5);
            mParam6=getArguments().getInt(ARG_PARAM6);
        }

    }




    ImageView close_rfq;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_show_product, container, false);

        intilaze(view);

        header_r_title.setText(mParam2+" > "+mParam3);
        new AsyncFetch().execute();


        close_rfq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getView().setVisibility(View.GONE);
            }
        });

        return view;
    }



    BroadcastReceiver confirmmessage=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            getView().setVisibility(View.GONE);

        }
    };

    void intilaze(View view)
    {
        rfq_rclr=view.findViewById(R.id.rfq_rclr);
        close_rfq=view.findViewById(R.id.close_rfq);
        header_r_title=view.findViewById(R.id.header_r_title);
    }
    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()

        {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setIcon(R.drawable.optimizedlogo);
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {


               // System.out.println("https://sellerportal.perfectmandi.com/detailrfq_api.php?id="+oid);
                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data

                System.out.println("https://sellerportal.perfectmandi.com/get_sameproduct.php?id="+mParam1+"&cat="+mParam2+"&&scat="+mParam3);
                url = new URL("https://sellerportal.perfectmandi.com/get_sameproduct.php?id="+mParam1+"&cat="+mParam2+"&&scat="+mParam3);

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


            System.out.println(result);
          //  Toast.makeText(getContext(),result,Toast.LENGTH_LONG).show();
            pdLoading.dismiss();

            Log.d("pm",result);
            // System.out.println(result);
            List<DetailRFQ> data=new ArrayList<>();

            if (data.size()==1)
            {
            Toast.makeText(getActivity(),"Just",Toast.LENGTH_LONG).show();
            }


            try
            {
                JSONArray jArray = new JSONArray(result);

                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);


                  /*{"id":"27","Product_Name":"Tokri Set","Product_Description":"Set of 2 Pieces Plastic Tokri","image_path":"https:\/\/sellerportal.perfectmandi.com\/uploads\/9\/product\/2 Pieces Plastic Tokri.jpg","selling_price":"90","Provider":"9","product_measure_in":"","measure_category":"Set","MOQ":"5"}
                  */  DetailRFQ stockProductModel=new DetailRFQ();

                    stockProductModel.id=json_data.getString("id");
                    stockProductModel.name=json_data.getString("Product_Name");
                    stockProductModel.des=json_data.getString("Product_Description");
                    stockProductModel.img=json_data.getString("image_path");
                 //   stockProductModel.quan=json_data.getString("order_quantity");
                    stockProductModel.price=json_data.getString("selling_price");
                //    stockProductModel.pid=json_data.getString("pid");
                    stockProductModel.Provider=json_data.getString("Provider");
                    stockProductModel.stock=mParam4;







                    // fishData.usid=username;

                    data.add(stockProductModel);
                }
                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rfq_rclr.setLayoutManager(llm);
                detailRFQIAdapter= new VendorADDAdapter(getContext(), data) {
                    @Override
                    public void sendData(DetailRFQ detailRFQ, int position) {
                        pr_id=detailRFQ.id;
                        pr_quan=detailRFQ.getstock;
                        pr_price=detailRFQ.price;

                        //position=intent.getIntExtra("pos",0);



                        new Add_replace().execute();
                    }
                };
                rfq_rclr.setAdapter(detailRFQIAdapter);
                // recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                // Setup and Handover data to recyclerview



            } catch (Exception e) {
            }
        }

    }

    private class Add_replace extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()

        {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setIcon(R.drawable.optimizedlogo);
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {


                // System.out.println("https://sellerportal.perfectmandi.com/detailrfq_api.php?id="+oid);
                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data

                url = new URL("https://sellerportal.perfectmandi.com/add_replacement.php?id="+pr_id+"&quan="+pr_quan+"&&price="+pr_price+"&&ato="+mParam5);

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


            pdLoading.dismiss();


            if (result.equalsIgnoreCase("Record updated successfully"))
            {

                listener.sendData(mParam6);
                //getView().setVisibility(View.GONE);
                //Toast.makeText(getActivity(),String.valueOf(position),Toast.LENGTH_LONG).show();
               // detailRFQIAdapter.removeItem(position);
            }
        }

    }


    public interface ShowAnInterface{
        void sendData(int position);
}
}