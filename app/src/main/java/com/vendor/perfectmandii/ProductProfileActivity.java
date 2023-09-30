package com.vendor.perfectmandii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vendor.perfectmandii.Adapter.AdapterProductCart;
import com.squareup.picasso.Picasso;

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
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ProductProfileActivity extends AppCompatActivity
{
    Button cardView,continueshipping;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    private AdapterProductCart mAdapter;
    boolean check = true;
    String ServerUploadPath ="https://staginigserver.perfectmandi.com/addcartitem.php" ;
    ProgressDialog progressDialog1;

    String ImageName = "image_name" ;
    String Imagepurchase = "purchaser_name" ;
    String Imagepurseller = "vendor_name" ;
    String Imagestatus = "image_status" ;
    String Imagesession = "image_session" ;
    String product_quantity="product_quantity";
    String selling_price="selling_price";
    String total_price="total_price";
    String order_num="order_num";
    String imgurl,id,proid,session,userid;
    ImageView imageView,backbutton,addup,minusup,bucket;

    String ProductPrice,productname,productDescription;
    TextView productprice,Productname,atcte,quanvalue,subtotal,total,ppdesc;
    CardView atc,ptc;
    String categoryname;
    String ordernum;
    String newOrder;

int count,value,subtotalamount;
    int gtotal;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_profile);



        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        //
        intialize_Widget();
        subtotal.setText("Rs :0");
        total.setText("Rs :0");






        Intent intent=getIntent();
        imgurl=intent.getStringExtra("value");
        id=intent.getStringExtra("id");
        proid=intent.getStringExtra("proid");
        session=intent.getStringExtra("session");
        userid=intent.getStringExtra("userid");
        ProductPrice=intent.getStringExtra("price");
        productname=intent.getStringExtra("name");
        categoryname = intent.getStringExtra("category");
        productDescription=intent.getStringExtra("description");
        checkorder();


        Picasso.get().load(imgurl).into(imageView);
        Productname.setText(productname);
        productprice.setText(ProductPrice);
        ppdesc.setText(productDescription);


        bucket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent1=new Intent(ProductProfileActivity.this,shoppingBasket.class);
                intent1.putExtra("userid",userid);
                intent1.putExtra("session",session);
                startActivity(intent1);

            }
        });
        ptc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent1=new Intent(ProductProfileActivity.this,shoppingBasket.class);
                intent1.putExtra("userid",userid);
                intent1.putExtra("session",session);
                startActivity(intent1);

            }
        });

        atc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        atcte=findViewById(R.id.atcte);
        atcte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
           String getvalue=quanvalue.getText().toString();
           if(getvalue.equalsIgnoreCase("0"))
           {
               Toast.makeText(ProductProfileActivity.this,"Please add quantity",Toast.LENGTH_LONG).show();
           }
           else
           {
               int neworder=Integer.parseInt(ordernum)+1;
               newOrder=String.valueOf(neworder);
               new AsyncTaskUploadClass().execute();
           }

            }
        });


        backbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
             finish();

            }
        });



        addup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                count=count+1;
                calculate(0);
                quanvalue.setText(String.valueOf(count));

            }
        });

        minusup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(count<=0)
                {

                }
                else
                {
                    count=count-1;
                    calculate(0);
                    quanvalue.setText(String.valueOf(count));
                }
            }
        });


    }

    void calculate(int delivery)
    {
        int price=Integer.parseInt(productprice.getText().toString());
        int quantity=count;
        int stotal=price*quantity;
        int Delivery=delivery;
       gtotal=stotal+Delivery;
        subtotal.setText("Rs :"+""+String.valueOf(stotal));
        total.setText("Rs :"+""+String.valueOf(gtotal));

    }

        void intialize_Widget()
        {
            bucket=findViewById(R.id.bucket);
            Productname=findViewById(R.id.ppname);
            imageView=findViewById(R.id.ivFish);
            addup=findViewById(R.id.add_up);
            minusup=findViewById(R.id.minus_up);
            backbutton=findViewById(R.id.backbutton);
            atc=findViewById(R.id.atc);
            quanvalue=findViewById(R.id.value_cart);
            productprice=findViewById(R.id.ppric);
            subtotal=findViewById(R.id.subtotal);
            total=findViewById(R.id.total);
            ppdesc=findViewById(R.id.ppdesc);
            ptc=findViewById(R.id.ptco);
        }


    private  class AsyncTaskUploadClass extends AsyncTask<Void,Void,String>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog1 = ProgressDialog.show(ProductProfileActivity.this,"Image Added to Cart","Please Wait",false,false);
        }
        @Override
        protected void onPostExecute(String string1)
        {
            super.onPostExecute(string1);
            progressDialog1.dismiss();



        }

        @Override
        protected String doInBackground(Void... params)
        {
            ImageProcessClass imageProcessClass = new ImageProcessClass();
            HashMap<String,String> HashMapParams = new HashMap<String,String>();
            HashMapParams.put(ImageName, proid);
            HashMapParams.put(Imagepurchase,userid);
            HashMapParams.put(Imagepurseller,id);
            HashMapParams.put(Imagestatus,"Order_Open");
            HashMapParams.put(Imagesession,session);
            HashMapParams.put(product_quantity,String.valueOf(count));
            HashMapParams.put(selling_price,ProductPrice);
            HashMapParams.put(total_price,String.valueOf(gtotal));
            HashMapParams.put(order_num,newOrder);
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
                        new OutputStreamWriter(OutPutStream, "UTF-8"));
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

    void checkorder()
    {
        if(session==null)
        {

        }
        else
        {
            new AsyncFetch_check().execute();

        }
    }


    private class AsyncFetch_check extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            try {


                url = new URL("https://staginigserver.perfectmandi.com/ordersessionuser.php?id="+userid);

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
            ordernum = result;
        }

    }
}