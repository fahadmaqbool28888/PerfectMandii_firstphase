package com.customer.perfectcustomer.Activity.Home.ProductScreen.DisplayProduct;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customer.perfectcustomer.Adapter.AdapterProductPrime;
import com.customer.perfectcustomer.Adapter.SubCatogories.subcategoriesAdapter;
import com.customer.perfectcustomer.Model.SubCategories.ProductSub;
import com.customer.perfectcustomer.Model.SubCategories.SubCategories;
import com.customer.perfectcustomer.R;

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

import de.hdodenhof.circleimageview.CircleImageView;


public class CategoryByVendorActivity extends AppCompatActivity
{
subcategoriesAdapter subcategoriesadapter;
    ProgressDialog pdLoading;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;

    ImageView back_button;

    String session,userid,storename;
    List<SubCategories> datas;
    List<ProductSub> data;
    String vi,cat,url;
    TextView vendor_prov,store_address;
    CircleImageView logo_vendor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_by_vendor);

        intializeWidget();

        Intent intent=getIntent();
        vi=intent.getStringExtra("vi");
        storename=intent.getStringExtra("storename");
        cat=intent.getStringExtra("category");
        url=intent.getStringExtra("url");


        store_address.setText(cat);
        Glide.with(CategoryByVendorActivity.this).load("https://sellerportal.perfectmandi.com/"+url).into(logo_vendor);
        datas=new ArrayList<>();
        data=new ArrayList<>();

        vendor_prov.setText(storename);


        new AsyncFetch().execute();

        back_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });



    }
    void intializeWidget()
    {
       /* imageView=findViewById(R.id.error_Res);*/
        mRVFishPrice=findViewById(R.id.rec_cat_ven);

        vendor_prov=findViewById(R.id.vendor_prov);
        //
        back_button=findViewById(R.id.backbutton1);
        //List Intializer
        logo_vendor=findViewById(R.id.logo_vendor);
        //
        store_address=findViewById(R.id.store_address);

    }





    private void assignadapter(List<SubCategories> datas)
    {

        subcategoriesadapter=new subcategoriesAdapter(CategoryByVendorActivity.this,datas);
        mRVFishPrice.setAdapter(subcategoriesadapter);
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(CategoryByVendorActivity.this));
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {


        ProgressDialog progressDialog=new ProgressDialog(CategoryByVendorActivity.this);
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




                url = new URL("https://sellerportal.perfectmandi.com/subCat_va.php?id="+vi+"&cat="+cat);


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
                Toast.makeText(CategoryByVendorActivity.this,"Sorry,,Try Again",Toast.LENGTH_LONG).show();
                //imageView.setImageResource(R.drawable.optimizedlogo);

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
                        subCategories.isa=vi;



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