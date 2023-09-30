package com.vendor.perfectmandii.CategoryDetail.Controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.vendor.perfectmandii.CategoryDetail.Model.modelclass;
import com.vendor.perfectmandii.R;
import com.vendor.perfectmandii.datapass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class demandData extends AsyncTask<String,String,String>
{

    String level0_string,level1_string,level2_string;
    List<String> level0_list;
    HashMap<String, JSONArray> data;
    HashMap<String, JSONArray> level0;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    // ProgressDialog pdLoading = new ProgressDialog(getContext());
    HttpURLConnection conn;
    URL url = null;

    ProgressDialog progressDialog;
    Context context;
    Spinner level,level1, level2;


    public demandData(Context context)
    {
        this.context=context;
        progressDialog=new ProgressDialog(context);
    }
    public demandData(Context context, Spinner level,Spinner level1,Spinner level2)
    {

        this.context=context;
        progressDialog=new ProgressDialog(context);
        this.level=level;
        this.level1=level1;
        this.level2=level2;

    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings)
    {
        try
        {
            url = new URL("https://sellerportal.perfectmandi.com/get_cat_Detail.php");
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
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);


        if (s!=null)
        {
        //    datapass.hashmap=new HashMap<>();
            level0_list=new ArrayList<>();
            level0_list.clear();
            level0_list.add("Select Category");
            try {
                modelclass cmodal=new modelclass();
                progressDialog.dismiss();
                JSONArray jArray = new JSONArray(s);
                JSONArray jsonArray1 = null,jsonArray_dealin=null,jsonArray_color=null;
                for (int i=0;i<jArray.length();i++)
                {

                    JSONObject json_data = jArray.getJSONObject(i);

                    cmodal.level1_category=json_data.getString("Category");
                    cmodal.array=json_data.getJSONArray("arr");

                    String key=json_data.getString("Category");


                    passmethod(key,cmodal.array);

                }


          /*      getMeasurement(jsonArray1);
                getcategory(jsonArray_dealin);
                getcolor(jsonArray_color);*/

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    protected abstract void passmethod(String key, JSONArray jsonArray);

    private void populate_meausurement()
    {
 /*       ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(context, R.layout.item_list,meausrecategory_list);

        meausrecategory.setAdapter(arrayAdapter);
        meausrecategory.setVisibility(View.VISIBLE);*/
    }

    void level_2(String array) throws JSONException
    {
        data=new HashMap<String, JSONArray>();
        JSONArray jArray=level0.get(array);
       // System.out.println(array+" "+jArray.toString());
        //JSONArray jArray = new JSONArray(data.get(array));
        for (int i=0;i<jArray.length();i++)
        {

            JSONObject json_data = jArray.getJSONObject(i);

            String d=json_data.getString("Category");
            JSONArray jsonArray=json_data.getJSONArray("arr");

            data.put(d,jsonArray);
         //   System.out.println(d +" "+jsonArray);







        }

        System.out.println(String.valueOf(data.size()));
    }
    void level_3()
    {

    }
    private void populate_level0()
    {
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(context, R.layout.item_list,level0_list);

       level.setAdapter(arrayAdapter);
        level.setVisibility(View.VISIBLE);
    }

}
