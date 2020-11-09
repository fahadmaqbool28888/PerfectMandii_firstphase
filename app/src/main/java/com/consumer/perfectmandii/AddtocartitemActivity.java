package com.consumer.perfectmandii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.consumer.perfectmandii.Adapter.AdapterProductAddtoitem;
import com.consumer.perfectmandii.Model.OrderCartModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class AddtocartitemActivity extends AppCompatActivity
{
    String ServerUploadPath ="https://staginigserver.perfectmandi.com/addcartitem.php" ;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    private AdapterProductAddtoitem mAdapter;
    Button ConfirmOrder,ContinueShopping;
    String session,userid;
    boolean check = true;
    String ImageName = "image_name" ;
    String Imagepurchase = "purchaser_name" ;
    String Imagepurseller = "vendor_name" ;
    String Imagestatus = "image_status" ;
    String Imagesession = "image_session" ;
    String proid,id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtocartitem);
        Intent intent=getIntent();
        session=intent.getStringExtra("session");
        userid=intent.getStringExtra("userid");
        proid=intent.getStringExtra("proid");
        id=intent.getStringExtra("id");
        new AsyncFetch().execute();

        ConfirmOrder=findViewById(R.id.addtocart);
        ContinueShopping=findViewById(R.id.addtocontshopping);
        ConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                new AsyncTaskUploadClass().execute();
               // startActivity(new Intent(AddtocartitemActivity.this,OrderstatusActivity.class));

            }
        });
        ContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent1=new Intent(AddtocartitemActivity.this,MainActivity.class);
                intent1.putExtra("userid",userid);
                intent1.putExtra("session",session);
                startActivity(intent1);
            }
        });
    }
    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(AddtocartitemActivity.this);
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

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("https://staginigserver.perfectmandi.com/cartitem.php?id="+session);

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
            List<OrderCartModel> data=new ArrayList<>();

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

             /*   // Setup and Handover data to recyclerview
                mRVFishPrice = (RecyclerView)findViewById(R.id.addtocartitem);
                mAdapter = new AdapterProductAddtoitem(AddtocartitemActivity.this, data);
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager( new LinearLayoutManager(AddtocartitemActivity.this));
*/
            } catch (Exception e) {
                Toast.makeText(AddtocartitemActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }


    private  class AsyncTaskUploadClass extends AsyncTask<Void,Void,String>
    {
        ProgressDialog progressDialog1;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog1 = ProgressDialog.show(AddtocartitemActivity.this,"Image Added to Cart","Please Wait",false,false);
            // Intent intent=new Intent(detailProductActivity.this,sellerproductActivity.class);
            //startActivity(intent);
            // imageView.setImageResource(R.drawable.imagepic);
        }
        @Override
        protected void onPostExecute(String string1)
        {
            super.onPostExecute(string1);

            progressDialog1.dismiss();
            Intent intent=new Intent(AddtocartitemActivity.this, ConfirmOrderitem.class);
            intent.putExtra("session",session);
            intent.putExtra("userid",userid);
            startActivity(intent);
        }

        @Override
        protected String doInBackground(Void... params)
        {
            ImageProcessClass imageProcessClass = new ImageProcessClass();
            HashMap<String,String> HashMapParams = new HashMap<String,String>();




            HashMapParams.put(ImageName, proid);
            HashMapParams.put(Imagepurchase,userid);
            HashMapParams.put(Imagepurseller,id);
            HashMapParams.put(Imagestatus,"Order_Transit");
            HashMapParams.put(Imagesession,session);
            String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);
            return FinalData;
        }
    }
    public class ImageProcessClass
    {

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();
            try
            {
                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;
                url = new URL(requestURL);
                httpURLConnectionObject = (HttpURLConnection) url.openConnection();
                httpURLConnectionObject.setReadTimeout(19000);
                httpURLConnectionObject.setConnectTimeout(19000);
                httpURLConnectionObject.setRequestMethod("POST");
                httpURLConnectionObject.setDoInput(true);
                httpURLConnectionObject.setDoOutput(true);
                OutPutStream = httpURLConnectionObject.getOutputStream();
                bufferedWriterObject = new BufferedWriter(
                        new OutputStreamWriter(OutPutStream, StandardCharsets.UTF_8));
                bufferedWriterObject.write(bufferedWriterDataFN(PData));
                bufferedWriterObject.flush();
                bufferedWriterObject.close();
                OutPutStream.close();
                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }

}