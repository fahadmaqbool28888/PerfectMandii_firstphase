package com.consumer.perfectmandii.Activity.SubCategories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.consumer.perfectmandii.Adapter.SubCatogories.subcategoriesAdapter;
import com.consumer.perfectmandii.Model.SubCategories.ProductSub;
import com.consumer.perfectmandii.Model.SubCategories.SubCategories;
import com.consumer.perfectmandii.R;

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

public class SubcategoreiesActivity extends AppCompatActivity
{
    ProgressDialog pdLoading;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    RecyclerView categoryContainer;
    List<SubCategories> datas;
    List<ProductSub> data;
    List<JSONArray> listJson;

    subcategoriesAdapter subcategoriesadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcategoreies);
        datas=new ArrayList<>();
        data=new ArrayList<>();
        intializeWidget();


        new AsyncFetch().execute();
    }

    private void assignadapter(List<SubCategories> datas)
    {

        subcategoriesadapter=new subcategoriesAdapter(SubcategoreiesActivity.this,datas);
        categoryContainer.setAdapter(subcategoriesadapter);
        categoryContainer.setLayoutManager(new LinearLayoutManager(SubcategoreiesActivity.this));
    }

    void intializeWidget()
    {
        categoryContainer=findViewById(R.id.categoryContainer);
        //
        //List Intializer

    }



    private class AsyncFetch extends AsyncTask<String, String, String> {


        ProgressDialog progressDialog=new ProgressDialog(SubcategoreiesActivity.this);
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


                url = new URL("https://staginigserver.perfectmandi.com/sbproduct_Cat.php");

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
                Toast.makeText(SubcategoreiesActivity.this,"Sorry,,Try Again",Toast.LENGTH_LONG).show();
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


                        subCategories.jsonArray=json_data.getJSONArray("catalog");


/*

                        JSONArray array=json_data.getJSONArray("catalog");

                        for(int k=0;k<array.length();k++)
                        {
                            JSONObject pr_data = array.getJSONObject(k);
                            ProductSub productSub=new ProductSub();
                            productSub.product_id=pr_data.getString("Product_Name");
                            productSub.product_name=pr_data.getString("Product_Name");
                            productSub.product_description=pr_data.getString("Product_Name");
                            productSub.product_price=pr_data.getString("Product_Name");
                            productSub.product_image_path=pr_data.getString("image_url");

                            data.add(productSub);
                        }
*/

                       // subCategories.data=data;
                        datas.add(subCategories);







                  /*      JSONObject jsonObject=json_data.getJSONObject("catalog");
                        System.out.println(jsonObject.getString("Product_Name"));*/
                       /* JSONObject catalogObject = json_data.getJSONObject("catalog");
                        System.out.println(catalogObject.getString("Product_Name"));*/


                    }

                    assignadapter(datas);


                } catch (Exception e) {
                    //  Toast.makeText(Function.this, e.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }

    }




}