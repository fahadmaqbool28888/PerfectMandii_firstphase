package com.consumer.perfectmandii;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.consumer.perfectmandii.Adapter.AdapterProductPrime;
import com.consumer.perfectmandii.Adapter.SubCatogories.subcategoriesAdapter;
import com.consumer.perfectmandii.Model.SubCategories.ProductSub;
import com.consumer.perfectmandii.Model.SubCategories.SubCategories;

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


public class Product extends Fragment
{
    subcategoriesAdapter subcategoriesadapter;
    ProgressDialog pdLoading;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    private AdapterProductPrime mAdapter;
    ImageView imageView;
    int progress = 0;
    ProgressBar progressBar;
    String session,category,userid,urls,id,name,profilepic;
    List<SubCategories> datas;
    List<ProductSub> data;
    public Product(String session,String category,String userid,String id,String name,String profilepic)
    {

        this.id=id;
        this.session=session;
        this.category=category;
        this.userid=userid;
        this.name=name;
        this.profilepic=profilepic;

    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.fragment_product, container, false);

        intializeWidget(rootview);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        datas=new ArrayList<>();
        data=new ArrayList<>();


        new AsyncFetch().execute();
        return rootview;
    }
    void intializeWidget(View view)
    {
        imageView=view.findViewById(R.id.error_Res);
        mRVFishPrice=view.findViewById(R.id.product_perfect_mandi_c);

        //
        //List Intializer

    }





    private void assignadapter(List<SubCategories> datas)
    {
        subcategoriesadapter=new subcategoriesAdapter(getContext(),datas);
        mRVFishPrice.setAdapter(subcategoriesadapter);
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private class AsyncFetch extends AsyncTask<String, String, String>
    {
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog.setTitle("Perfect Mandi");
            progressDialog.setIcon(R.drawable.optimizedlogo);
            // progressDialog.setMessage("Data is Loading...");
            progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params) {
            try {



                url = new URL("https://sellerportal.perfectmandi.com/_sbproduct_cat.php?id="+category);

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

            progressDialog.dismiss();
            System.out.println(result);

            if(result.equalsIgnoreCase("unsuccessful"))
            {

            }
            else
            {



                try {



                    JSONArray jArray = new JSONArray(result);



                    // Extract data from json and store into ArrayList as class objects
                    for(int i=0;i<jArray.length();i++)
                    {
                        JSONObject json_data = jArray.getJSONObject(i);
                        SubCategories subCategories=new SubCategories();
                        subCategories.id=json_data.getString("id");
                        subCategories.name=json_data.getString("name");
                        subCategories.parent_Category=json_data.getString("l1_product_name");
                        subCategories.image_url=json_data.getString("image_url");
                        subCategories.status=json_data.getString("status");
                        subCategories.userid=userid;
                        subCategories.session=session;
                        subCategories.jsonArray=json_data.getJSONArray("catalog");
                        datas.add(subCategories);
                    }

                    assignadapter(datas);


                } catch (Exception e) {
                    //  Toast.makeText(Function.this, e.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }

    }
}
