package com.consumer.perfectmandii.Activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.consumer.perfectmandii.Adapter.ColorAdapter.ColorAssignAdapter;
import com.consumer.perfectmandii.Adapter.TopRelevantAdapter.relevantAdapter;
import com.consumer.perfectmandii.Model.SubCategories.ProductSub;
import com.consumer.perfectmandii.Model.SubCategories.SubCategoriesRelevant;
import com.consumer.perfectmandii.R;

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

public class Product_Profile_extend extends AppCompatActivity
{
    CardView atc_dialog;
    AlertDialog alertDialog;
    boolean check = true;
    String ServerUploadPath ="https://staginigserver.perfectmandi.com/addcartitem.php" ;
    TextView editFlag,atc_extend,ppname_dialog,ppdesc_dialog,ppric_Dialog,total_dialog,subtotal_dialog;
    LinearLayout colorblock_dialog;
    ColorAssignAdapter colorAssignAdapter;
    LayoutInflater inflater;
    List<String> list=new ArrayList<>();
    ImageView closeddialog,ivFish_Dialog;

   String appointsession=null;
    String getUserid=null;
    int count=0,gtotal=0;
    //
    ProgressDialog pdLoading;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    RecyclerView categoryContainer;
    List<SubCategoriesRelevant> datas;
    List<ProductSub> data;
    List<JSONArray> listJson;

    relevantAdapter subcategoriesadapter;



    String imgurl,id,proid,session,userid;
    String ProductPrice,productname,productDescription,username,profilepic,categoryname,ordernum_dialog,newOrder;
//
    String ImageName = "image_name" ;
    String Imagepurchase = "purchaser_name" ;
    String Imagepurseller = "vendor_name" ;
    String Imagestatus = "image_status" ;
    String Imagesession = "image_session" ;
    String product_quantity="product_quantity";
    String selling_price="selling_price";
    String total_price="total_price";
    String order_num="order_num";

    String pr;
    String categoryName;

    private NumberPicker picker1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__profile_extend);
        checkorder();


/*        //
        subtotal.setText("Rs :0");
        total.setText("Rs :0");*/






        Intent intent=getIntent();
        categoryName=intent.getStringExtra("categoryName");
        imgurl=intent.getStringExtra("value");
        id=intent.getStringExtra("seller");
        proid=intent.getStringExtra("proid");
        session=intent.getStringExtra("session");
        /*  final String appointsession=null;
    final String getUserid=null;*/
        userid=intent.getStringExtra("userid");
        getUserid=userid;

        appointsession=session;
        ProductPrice=intent.getStringExtra("price");
        productname=intent.getStringExtra("name");
        categoryname = intent.getStringExtra("category");
        productDescription=intent.getStringExtra("description");
        username=intent.getStringExtra("username");
        profilepic=intent.getStringExtra("path");


        pr=intent.getStringExtra("pr");


        list.add("Red");
        list.add("Blue");
        list.add("Black");
        list.add("Red");
        list.add("Red");
        list.add("Blue");
        list.add("Black");
        list.add("Red");
        list.add("Red");
        list.add("Blue");



        showDialog();
        datas=new ArrayList<>();
        data=new ArrayList<>();
        intializeWidget();


        new AsyncFetch().execute();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message_as"));
    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            categoryName=intent.getStringExtra("categoryName");
            imgurl=intent.getStringExtra("value");
            id=intent.getStringExtra("seller");
            proid=intent.getStringExtra("proid");
            session=intent.getStringExtra("session");
            userid=intent.getStringExtra("userid");
            ProductPrice=intent.getStringExtra("price");
            productname=intent.getStringExtra("name");
            categoryname = intent.getStringExtra("category");
            productDescription=intent.getStringExtra("description");
            username=intent.getStringExtra("username");
            profilepic=intent.getStringExtra("path");


            pr=intent.getStringExtra("pr");
            showDialog();





        }
    };




    void showDialog()
    {
        session=appointsession;
        userid=getUserid;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Product_Profile_extend.this);

        inflater = LayoutInflater.from(Product_Profile_extend.this);

        // LayoutInflater inflater = this.context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_product_dialog, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();








        alertDialog.show();




        colorAssignAdapter=new ColorAssignAdapter(Product_Profile_extend.this,list);

        colorblock_dialog=alertDialog.findViewById(R.id.colorblock_dialog);
        ppname_dialog=alertDialog.findViewById(R.id.ppname_dialog);
        closeddialog=alertDialog.findViewById(R.id.closeddialog);
        ivFish_Dialog=alertDialog.findViewById(R.id.ivFish_Dialog);
        ppdesc_dialog=alertDialog.findViewById(R.id.ppdesc_dialog);
        ppric_Dialog=alertDialog.findViewById(R.id.ppric_Dialog);
        total_dialog=alertDialog.findViewById(R.id.total_dialog);
        subtotal_dialog=alertDialog.findViewById(R.id.subtotal_dialog);
        atc_extend=alertDialog.findViewById(R.id.atc_extend);

        picker1=alertDialog.findViewById(R.id.numberpicker);
        picker1.setMaxValue(100);
        picker1.setMinValue(0);
        picker1.setValue(0);
        picker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                count=picker.getValue();
                editFlag.setText("Your Quantity: "+String.valueOf(picker.getValue()));
                calculate(0);
                atc_extend.setText("Add to Cart");
            }
        });
        atc_dialog=alertDialog.findViewById(R.id.atc_dialog);

        if (count==0)
        {

            atc_extend.setText("Select Quantity");
        }
        else
        {
            atc_extend.setText("Add to Cart");
        }

        subtotal_dialog.setText("Rs :0");
        total_dialog.setText("Rs :0");
        Glide
                .with(Product_Profile_extend.this)
                .load(imgurl)


                .into(ivFish_Dialog);
        ppname_dialog.setText(productname);
        ppdesc_dialog.setText(productDescription);
        ppric_Dialog.setText(ProductPrice);

//        colorblock_dialog.setVisibility(View.GONE);
        closeddialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });



      /*  RecyclerView recyclerView=alertDialog.findViewById(R.id.colorpallete);*/
      editFlag=alertDialog.findViewById(R.id.cquan);
      atc_extend=alertDialog.findViewById(R.id.atc_extend);
      atc_extend.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              int neworder=Integer.parseInt(ordernum_dialog)+1;
              if (atc_extend.getText().equals("Select Quantity"))
              {
                  //showquan(v);
              }
              else
              {
       if (neworder>0)
              {
                  newOrder=String.valueOf(neworder);
                  new AsyncTaskUploadClass().execute();
              }
              else
              {
                  Toast.makeText(Product_Profile_extend.this,ordernum_dialog,Toast.LENGTH_SHORT).show();
              }
              }


          }
      });
       /* recyclerView.setAdapter(colorAssignAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));*/

        editFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //showquan(v);
            }
        });

    }

    //


    //


    void calculate(int delivery)
    {

//        Toast.makeText(ProductProfileActivity.this,count,Toast.LENGTH_LONG).show();
        int price=Integer.parseInt(ppric_Dialog.getText().toString());
        int quantity=count;
        int stotal=price*quantity;
        int Delivery=delivery;
        gtotal=stotal+Delivery;
        subtotal_dialog.setText("Rs :"+""+ stotal);
        total_dialog.setText("Rs :"+""+ gtotal);

    }

  /* void  showquan(View v)
    {
        try {
            final NumberPicker numberPicker = new NumberPicker(Product_Profile_extend.this);
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(100);
            numberPicker.setValue(1);

            AlertDialog.Builder builder = new AlertDialog.Builder(Product_Profile_extend.this);
            builder.setIcon(R.drawable.optimizedlogo);
            builder.setTitle("Update your order");
            builder.setMessage("Select Quantity :");
            builder.setView(numberPicker);
            builder.create();
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {



                    count=numberPicker.getValue();
                    editFlag.setText("Your Quantity: "+String.valueOf(numberPicker.getValue()));
                    calculate(0);
                    atc_extend.setText("Add to Cart");
                    *//*   Toast.makeText(shoppingBasket.this, totalprice +" "+ price +" "+ ocid +" "+ numberPicker.getValue(),Toast.LENGTH_LONG).show();*//*
                }
            });

            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {


                }
            });
            builder.show();

        }
        catch (Exception ecx)
        {

        }


    }*/



    private void assignadapter(List<SubCategoriesRelevant> datas)
    {

        subcategoriesadapter=new relevantAdapter(Product_Profile_extend.this,datas,categoryName,userid,session);
        categoryContainer.setAdapter(subcategoriesadapter);
        categoryContainer.setLayoutManager(new LinearLayoutManager(Product_Profile_extend.this));
    }

    void intializeWidget()
    {
        categoryContainer=findViewById(R.id.categoryContainer_extend);
        //
        //List Intializer

    }



    private class AsyncFetch extends AsyncTask<String, String, String> {


        ProgressDialog progressDialog=new ProgressDialog(Product_Profile_extend.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog.setTitle("Perfect Mandi");
            progressDialog.setIcon(R.drawable.optimizedlogo);
            // progressDialog.setMessage("Data is Loading...");
          //  progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params) {
            try {


                url = new URL("https://staginigserver.perfectmandi.com/sub_product_cat.php?id="+pr+"&cname="+categoryName);

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
                Toast.makeText(Product_Profile_extend.this,"Sorry,,Try Again",Toast.LENGTH_LONG).show();
                //imageView.setImageResource(R.drawable.optimizedlogo);

            }
            else
            {



                try {



                    JSONArray jArray = new JSONArray(result);



                    // Extract data from json and store into ArrayList as class objects
                    for(int i=0;i<jArray.length();i++)
                    {
/*
*  "id": "1",
        "search_type": "relevant",
        "typo": "relevancy",
        "orientation": "vertical",
        "status": "active",
        "catalog":*/


                        JSONObject json_data = jArray.getJSONObject(i);
                        SubCategoriesRelevant subCategories=new SubCategoriesRelevant();
                        subCategories.id=json_data.getString("id");
                        subCategories.search_type=json_data.getString("search_type");
                        subCategories.typo=json_data.getString("typo");
                        subCategories.orientation=json_data.getString("orientation");
                        subCategories.status=json_data.getString("status");


                        subCategories.jsonArray=json_data.getJSONArray("catalog");





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
    void checkorder()
    {
            new AsyncFetch_check().execute();
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
            ordernum_dialog = result;

            System.out.println();
            Toast.makeText(Product_Profile_extend.this,ordernum_dialog,Toast.LENGTH_SHORT).show();
        }

    }


    private  class AsyncTaskUploadClass extends AsyncTask<Void,Void,String>
    {
        ProgressDialog progressDialog=new ProgressDialog(Product_Profile_extend.this);
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
        protected void onPostExecute(String string1)
        {
            super.onPostExecute(string1);
            Toast.makeText(Product_Profile_extend.this,string1,Toast.LENGTH_SHORT).show();
            count=0;
            progressDialog.dismiss();
            alertDialog.dismiss();







        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... params)
        {
            ImageProcessClass imageProcessClass = new ImageProcessClass();
            HashMap<String,String> HashMapParams = new HashMap<String,String>();



            System.out.println("proid"+" "+proid+" "+"userid"+" "+userid+" "+"id"+" "+id+" "+"session"+" "+session+""+"count"+" "+String.valueOf(count)+" "+"Product Price"+" "+ProductPrice+""+"gtotal"+" "+String.valueOf(gtotal)+" "+"newOrder"+" "+newOrder);
            HashMapParams.put(ImageName, proid);
            HashMapParams.put(Imagepurchase,userid);
            HashMapParams.put(Imagepurseller,id);
            HashMapParams.put(Imagestatus,"Order_Open");
            HashMapParams.put(Imagesession,session);
            HashMapParams.put(product_quantity,String.valueOf(count));
            HashMapParams.put(selling_price,ProductPrice);
            HashMapParams.put(total_price,String.valueOf(gtotal));
            HashMapParams.put(order_num,newOrder);
            //  HashMapParams.put(fav_item,favp);
            String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);
            return FinalData;
        }
    }
    public class ImageProcessClass
    {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {

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
