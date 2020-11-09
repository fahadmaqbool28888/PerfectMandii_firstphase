package com.consumer.perfectmandii;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.consumer.perfectmandii.Adapter.AdapterProductPrime;
import com.consumer.perfectmandii.Adapter.SubCatogories.subcategoriesAdapter;
import com.consumer.perfectmandii.Adapter.popULATE_Categories;
import com.consumer.perfectmandii.Model.SubCategories.ProductSub;
import com.consumer.perfectmandii.Model.SubCategories.SubCategories;
import com.google.android.material.tabs.TabLayout;

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

public class MainVendorStoreActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    TextView part1;
    String session,categoryname,id,userid,name,profilepic,Prodcut_Sub_Category;

    ImageView backbutton;
    //
    popULATE_Categories subcategoriesadapter;
    ProgressDialog pdLoading;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    private AdapterProductPrime mAdapter;
    ImageView imageView;
    int progress = 0;
    ProgressBar progressBar;
    String category;
    List<SubCategories> datas;
    List<ProductSub> data;
    CircleImageView logo_vendor;
    TextView store_name,store_address;
    String pro;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_vendor_store);


        Intent get=getIntent();
        pro=get.getStringExtra("pro");
        Prodcut_Sub_Category=get.getStringExtra("Sub_Category");
        load_fragment(Prodcut_Sub_Category,pro);
        Toast.makeText(MainVendorStoreActivity.this,pro+" "+Prodcut_Sub_Category,Toast.LENGTH_LONG).show();
        backbutton=findViewById(R.id.backbutton);
        mRVFishPrice=findViewById(R.id.data_mal);
        logo_vendor=findViewById(R.id.logo_vendor);
        store_name=findViewById(R.id.store_name);
        store_address=findViewById(R.id.store_address);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        new AsyncFetch().execute();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    void load_fragment(String editflag,String provider)
    {
        Fragment addFragment=new CategoryWiseFragment(editflag,provider);
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,addFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {


            String editflag=intent.getStringExtra("flag");

           // Toast.makeText(MainVendorStoreActivity.this,editflag,Toast.LENGTH_LONG).show();



           /* Fragment addFragment=new CategoryWiseFragment(editflag);
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container,addFragment);
            transaction.addToBackStack(null);
            transaction.commit();*/


            FragmentManager fragmentManager = getSupportFragmentManager();

            if (!fragmentManager.isDestroyed()) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, new CategoryWiseFragment(editflag,pro))
                        .commit();
            }







        }
    };



    private void assignadapter(JSONArray datas)
    {
        subcategoriesadapter=new popULATE_Categories(MainVendorStoreActivity.this,datas);
        mRVFishPrice.setAdapter(subcategoriesadapter);
        mRVFishPrice.setLayoutManager(new LinearLayoutManager(MainVendorStoreActivity.this,LinearLayoutManager.HORIZONTAL,false));
    }

    private class AsyncFetch extends AsyncTask<String, String, String>
    {
        ProgressDialog progressDialog=new ProgressDialog(MainVendorStoreActivity.this);
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




                url = new URL("https://sellerportal.perfectmandi.com/store_category.php?id="+pro+"&psc="+Prodcut_Sub_Category);

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

                        String urls="https://sellerportal.perfectmandi.com/"+json_data.getString("url");
                        System.out.println(urls);
                        store_name.setText(json_data.getString("name"));
                        store_address.setText(json_data.getString("city"));
                        Glide.with(MainVendorStoreActivity.this)
                                .load(urls)
                                .placeholder(R.drawable.optimizedlogo)
                                .error(R.drawable.optimizedlogo)
                                .into(logo_vendor);
                      JSONArray jsonArray=json_data.getJSONArray("deal_in");
                      assignadapter(jsonArray);
                      //datas.add(subCategories);
                    }

                   // assignadapter(datas);


                } catch (Exception e) {
                    //  Toast.makeText(Function.this, e.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }

    }


}