package com.vendor.perfectmandii;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.vendor.perfectmandii.Adapter.AdapterHome;
import com.vendor.perfectmandii.Adapter.BankDetail;
import com.vendor.perfectmandii.Model.Bank.BankModel;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link bankinformation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class bankinformation extends Fragment
{

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    RecyclerView recyclerView;
    Spinner dataspinner;
    BankDetail bankDetail;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public bankinformation()
    {
        // Required empty public constructor
    }

    public static bankinformation newInstance(String param1, String param2)
    {
        bankinformation fragment = new bankinformation();
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
        View view=inflater.inflate(R.layout.fragment_bankinformation, container, false);
        // Inflate the layout for this fragment
        intialzeWidget(view);
        new AsyncFetch().execute();

        return view;
    }
    void intialzeWidget(View view)
    {
        dataspinner=view.findViewById(R.id.data_Bank);

        //recyclerView=view.findViewById(R.id.bankDetails);
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
        protected String doInBackground(String... params)
        {
            try
            {
                url = new URL("https://staginigserver.perfectmandi.com/getBankDetail_Api.php");
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
        protected void onPostExecute(String result)
        {
            progressDialog.dismiss();

            List<BankModel> data=new ArrayList<>();
            List<String> bank_name=new ArrayList<>();
            List<String> bank_desc=new ArrayList<>();


            try {


                JSONArray jArray = new JSONArray(result);



                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    BankModel fishData = new BankModel();
                    fishData.name=json_data.getString("branch_name");
                    bank_name.add(json_data.getString("branch_name"));
                    fishData.ba_dec=json_data.getString("bank_details");
                    bank_desc.add(json_data.getString("bank_details"));


                    data.add(fishData);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                        (getContext(), android.R.layout.simple_spinner_item,bank_name );

                dataAdapter.setDropDownViewResource
                        (android.R.layout.simple_spinner_dropdown_item);


                dataspinner.setAdapter(dataAdapter);


                dataspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        String sd=bank_desc.get(pos);
                        Toast.makeText(getContext(), sd.toString(), Toast.LENGTH_SHORT).show();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

         /*       // Setup and Handover data to recyclerview

                bankDetail = new BankDetail(getContext(), data);
                progressDialog.dismiss();
                recyclerView.setAdapter(bankDetail);
                progressDialog.dismiss();
                recyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));*/

            } catch (Exception e)
            {

            }




        }

    }
}