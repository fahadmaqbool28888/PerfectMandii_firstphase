package com.customer.perfectcustomer.Activity.Home.ProductScreen.DisplayProduct;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customer.perfectcustomer.Activity.CustomerOrder.IndexViewActivity;
import com.customer.perfectcustomer.Activity.Home.ProductScreen.DisplayProduct.Home.Fragment.ProductBy.DashboardActivity;
import com.customer.perfectcustomer.Activity.Home.VendorStore.VendorStoreActivity;
import com.customer.perfectcustomer.Adapter.AdapterVendorPrime;
import com.customer.perfectcustomer.Model.Customer.UserModel;
import com.customer.perfectcustomer.LocalDB.DatabaseClass;
import com.customer.perfectcustomer.Activity.Authentication.LoginActivity;
import com.customer.perfectcustomer.Activity.MainVendorStoreActivity;
import com.customer.perfectcustomer.Model.Product_Model;
import com.customer.perfectcustomer.Model.ProductModel.modelProduct;
import com.customer.perfectcustomer.R;
import com.customer.perfectcustomer.Activity.Home.ProductScreen.ViewImageActivity;
import com.squareup.picasso.Picasso;

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

public class ProductProfileActivity extends AppCompatActivity
{



    String id_product;
    modelProduct model;
    CardView cart_circle;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
String accountid,Prodcut_Sub_Category;
    boolean check = true;
    String ServerUploadPath ="https://sellerportal.perfectmandi.com/addcartitem.php" ;
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
    String pro_quan="pro_quan";

    String imgurl,id,proid,session,userid,product_measure_in,measure_category;
    ImageView imageView,backbutton,bucket;
    String ProductPrice,productname,productDescription,product_id;
    TextView value,productprice,Productname,atcte,subtotal,total,ppdesc,ppmeasure,avgwe,avgwn,visit_store;
    CardView atc,ptc;
    String categoryname;
    String ordernum,path;
    String newOrder,contact,flag_Add,flag;
    EditText quanvalue;
    TextView add_but;

    DatabaseClass databaseClass;
    int count;
    int gtotal;
    int mtotal;
    Double avgweight;
    int countclick=0;
    StringBuilder sb=new StringBuilder();

    ImageView fav;
    boolean fav_flag=false;

    String favp=null;
    RecyclerView releated_Store_item;

String contacts=null;
    DatabaseClass sqLiteHelper;
    List<UserModel> list;
    AdapterVendorPrime mAdapter;
    int qunatity,min_quantity;

    NumberPicker numberpicker;
    String quan,moq,provider;


    List<Product_Model> userproList;

    String code;
    TextView cq;


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (flag!=null)
        {
            finish();
        }
        else
        {
            Intent intent=new Intent(ProductProfileActivity.this, DashboardActivity.class);
            intent.putExtra("cname",Prodcut_Sub_Category);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    ArrayList<UserModel> arrayList;
    private void
    get_Data()
    {
        databaseClass=new DatabaseClass(ProductProfileActivity.this);

        arrayList=new ArrayList<>();
        arrayList=databaseClass.arrayList();

        if (arrayList.size()>0)
        {


            UserModel pointModel=arrayList.get(0);

            contact=pointModel.contact;
            System.out.println("Contact is "+contact);
            session=pointModel.session;




        }
        else
        {

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_profile);
       /* sqLiteHelper=new SQLiteHelper(ProductProfileActivity.this);*/
        intialize_Widget();

        model=new modelProduct();


        cart_circle.setVisibility(View.GONE);

        Intent intent=getIntent();
        productname=intent.getStringExtra("name");
        productDescription=intent.getStringExtra("description");
        product_id=intent.getStringExtra("product_id");

        id_product=product_id;
        proid=product_id;
        provider=intent.getStringExtra("pro");
        flag=intent.getStringExtra("flag");
        Prodcut_Sub_Category=intent.getStringExtra("Prodcut_Sub_Category");
        path=intent.getStringExtra("path");
        ProductPrice=intent.getStringExtra("price");
        moq=intent.getStringExtra("moq");
        quan=intent.getStringExtra("quan");
        product_measure_in=intent.getStringExtra("product_measure_in");
        measure_category=intent.getStringExtra("measure_category");


        code=intent.getStringExtra("code");
        get_Data();
        new AsyncFetch().execute();

        System.out.println("From login is"+" "+code);
        if(code!=null)
        {
            new AsyncFetch_open().execute();
          //  sqLiteHelper.deleteApp();
        }
        else
        {






            if (moq!=null)
            {
               // moq=intent.getStringExtra("moq");
            }
            else
            {
                moq="1";
            }



            if (measure_category.equalsIgnoreCase("Kilogram"))
            {
                avgweight=Double.parseDouble(product_measure_in);
                ppmeasure.setVisibility(View.VISIBLE);
                avgwe.setVisibility(View.VISIBLE);
                avgwn.setVisibility(View.VISIBLE);
                avgwn.setText(product_measure_in+""+" kg");
            }
            else
            {
                avgwe.setVisibility(View.INVISIBLE);
                avgwn.setVisibility(View.INVISIBLE);
                ppmeasure.setVisibility(View.INVISIBLE);
                avgweight=0.0;
            }

            if (measure_category.equalsIgnoreCase("Kilogram"))
            {
                avgweight=Double.parseDouble(product_measure_in);
                ppmeasure.setVisibility(View.VISIBLE);
                ppmeasure.setText(" / KG ");
                avgwe.setVisibility(View.VISIBLE);
                avgwn.setVisibility(View.VISIBLE);
                avgwn.setText(product_measure_in+""+" kg");
            }
            else if (measure_category.equalsIgnoreCase("Piece"))
            {
                avgwe.setVisibility(View.INVISIBLE);
                avgwn.setVisibility(View.INVISIBLE);
                ppmeasure.setVisibility(View.VISIBLE);
                ppmeasure.setText(" / Piece ");
                avgweight=0.0;
            }
            else if (measure_category.equalsIgnoreCase("Set"))
            {
                avgwe.setVisibility(View.INVISIBLE);
                avgwn.setVisibility(View.INVISIBLE);
                ppmeasure.setVisibility(View.VISIBLE);
                ppmeasure.setText(" / Set ");
                avgweight=0.0;
            }
            else if (measure_category.equalsIgnoreCase("Carton"))
            {
                avgwe.setVisibility(View.INVISIBLE);
                avgwn.setVisibility(View.INVISIBLE);
                ppmeasure.setVisibility(View.VISIBLE);
                ppmeasure.setText(" / Carton");
                avgweight=0.0;
            }
            else
            {
                avgwe.setVisibility(View.INVISIBLE);
                avgwn.setVisibility(View.INVISIBLE);
                ppmeasure.setVisibility(View.VISIBLE);
                ppmeasure.setText("/ Dozen");
                avgweight=0.0;
            }




            qunatity=Integer.parseInt(quan);
            min_quantity=Integer.parseInt(moq);


            numberpicker.setMinValue(0);
            numberpicker.setMaxValue(100);



       /*     get_Data_1();*/
            checkorder();
           // Glide.with(ProductProfileActivity.this).load(path).into(imageView);

            if (path.contains("https://sellerportal.perfectmandi.com/"))
            {
                Picasso.get().load(path).into(imageView);
                //  Glide.with(context).load(productSub.product_image_path).into(holder.product_img);
            }
            else {
                String url="https://sellerportal.perfectmandi.com/"+path;
                // Glide.with(context).load(url).into(holder.product_img);
                Picasso.get().load(url).into(imageView);

            }
            Productname.setText(productname);
            productprice.setText(ProductPrice);
            ppdesc.setText(productDescription);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent1=new Intent(ProductProfileActivity.this, ViewImageActivity.class);
                    intent1.putExtra("path",path);
                    startActivity(intent1);
                }
            });







            userid=contact;
            id=provider;



    /*        get_Data();*/

            subtotal.setText("Rs :0");
            total.setText("Rs :0");



            mtotal=Integer.parseInt(ProductPrice)*min_quantity;

            if (qunatity<min_quantity)
            {
            /*    atcte.setText("Out of stock");
                atcte.setTextColor(Color.RED);
                numberpicker.setVisibility(View.GONE);
                cq.setVisibility(View.GONE);
                atc.setVisibility(View.GONE);*/
            }
            else
            {
                atcte.setText("Minimum order quantity: "+moq);
            }






            bucket.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent1=new Intent(ProductProfileActivity.this, IndexViewActivity.class);
                    startActivity(intent1);

                }
            });
            backbutton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (flag!=null)
                    {
                        finish();
                    }
                    else
                    {
                        Intent intent=new Intent(ProductProfileActivity.this,DashboardActivity.class);
                        intent.putExtra("cname",Prodcut_Sub_Category);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                }
            });


            ptc.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (session!=null)
                    {
                        if (count>0)
                        {
                        if (count>=min_quantity)
                        {
                            flag_Add="buynow";
                            new AsyncTaskUploadClass().execute();

                        }
                        else
                        {
                            Toast.makeText(ProductProfileActivity.this,"Please add minimum quantity",Toast.LENGTH_LONG).show();

                        }

                        }
                        else
                        {
                            Toast.makeText(ProductProfileActivity.this,"Please add minimum quantity",Toast.LENGTH_LONG).show();

                        }






                    }
                    else
                    {


                           // sqLiteHelper.add_detail(product_id,"","","",Prodcut_Sub_Category,productname,productDescription,"",quan,ProductPrice,provider,"","yes","yes",quan,product_measure_in,measure_category,"","","","",moq,imgurl);
                            Intent intent1=new Intent(ProductProfileActivity.this, LoginActivity.class);
                            intent1.putExtra("flag","pp");

                            System.out.println(product_id+"Plus");
                            intent1.putExtra("code",product_id);
                          //  startActivity(intent1);


                    }
                }
            });

            atc.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    if (session!=null)
                    {

                        if (count>=min_quantity) {

                            flag_Add = "normal";
                            new AsyncTaskUploadClass().execute();
                       /*     if (gtotal >= mtotal) {

                            } else {
                                Toast.makeText(ProductProfileActivity.this, "Please add minimum quantity", Toast.LENGTH_LONG).show();
                            }*/
                        }
                        else
                        {
                            Toast.makeText(ProductProfileActivity.this, "Please add minimum Quantity", Toast.LENGTH_LONG).show();

                        }
                    }
                    else
                    {
                        Intent intent1=new Intent(ProductProfileActivity.this,LoginActivity.class);
                        intent1.putExtra("flag","pp");
                        intent1.putExtra("id_product",id_product);
                        intent1.putExtra("code",id_product);
                        startActivity(intent1);
                    }
                }
            });

            numberpicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
            {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1)
                {
                    count=i1;
                    calculate(i1);
                }
            });


            visit_store.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

            /*        Intent intent1=new Intent(ProductProfileActivity.this,MainVendorStoreActivity.class);
                    intent1.putExtra("pro",provider);
                    intent1.putExtra("Sub_Category",Prodcut_Sub_Category);
                    startActivity(intent1);*/

                    Intent intent1=new Intent(ProductProfileActivity.this, VendorStoreActivity.class);
                    intent1.putExtra("pro",provider);
                    intent1.putExtra("Sub_Category","");
                    startActivity(intent1);

                }
            });
        }
        new AsyncFetch().execute();
    }

    void calculate(int delivery)
    {

        if (measure_category.equalsIgnoreCase("Kilogram"))
        {
            int price=Integer.parseInt(productprice.getText().toString());
            int stotal= (int) (price*delivery*avgweight);
            gtotal= stotal;
            subtotal.setText("Rs :"+""+ stotal);
            total.setText("Rs :"+""+ gtotal);
        }
        else
        {
            int price=Integer.parseInt(productprice.getText().toString());
            int stotal=price*delivery;
            gtotal= stotal;
            subtotal.setText("Rs :"+""+ stotal);
            total.setText("Rs :"+""+ gtotal);
        }


    }
        void intialize_Widget()
        {
            value=findViewById(R.id.cart_n);
            cart_circle=findViewById(R.id.cicle_2);
            visit_store=findViewById(R.id.visit_store);
            cq=findViewById(R.id.cquan);
            atcte=findViewById(R.id.atcte);
            Productname=findViewById(R.id.ppname);
            ppdesc=findViewById(R.id.ppdesc);
            subtotal=findViewById(R.id.subtotal);
            total=findViewById(R.id.total);
            productprice=findViewById(R.id.ppric);
            numberpicker=findViewById(R.id.numberpicker);
            bucket=findViewById(R.id.bucket);
            imageView=findViewById(R.id.PDP_img);
            atc=findViewById(R.id.atc);
            backbutton=findViewById(R.id.backbutton);
            ptc=findViewById(R.id.ptco);
            ppmeasure=findViewById(R.id.ppmeasure);
            avgwe=findViewById(R.id.avgwe);
            avgwn=findViewById(R.id.avgwn);
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

            System.out.println(string1);



            new AsyncFetch().execute();


        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... params)
        {
            ImageProcessClass imageProcessClass = new ImageProcessClass();
            HashMap<String,String> HashMapParams = new HashMap<String,String>();


            System.out.println(ImageName+ proid);
            System.out.println(Imagepurchase+userid);
            System.out.println(Imagepurseller+id);
            System.out.println(Imagestatus+"Order_Open");
            System.out.println(Imagesession+session);
            System.out.println(product_quantity+String.valueOf(count));
            System.out.println(selling_price+ProductPrice);
            System.out.println(total_price+String.valueOf(gtotal));
            System.out.println(order_num+ordernum);
            System.out.println(pro_quan+quan);


            HashMapParams.put(ImageName, proid);
            HashMapParams.put(Imagepurchase,userid);
            HashMapParams.put(Imagepurseller,id);
            HashMapParams.put(Imagestatus,"Order_Open");
            HashMapParams.put(Imagesession,session);
            HashMapParams.put(product_quantity,String.valueOf(count));
            HashMapParams.put(selling_price,ProductPrice);
            HashMapParams.put(total_price,String.valueOf(gtotal));
            HashMapParams.put(order_num,ordernum);


            HashMapParams.put(pro_quan,quan);
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

    List<UserModel> userModelList;

    private class AsyncFetch_check extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;



        @Override
        protected String doInBackground(String... params) {
            try {


                url = new URL("https://sellerportal.perfectmandi.com/ordersessionuser.php?id="+contact);

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
           try {
               if (result.equalsIgnoreCase("java.net.SocketTimeoutException: failed to connect to staginigserver.perfectmandi.com/64.31.43.178 (port 443) from /192.168.0.104 (port 33406) after 10000ms"))
               {

               }
               else
               {
                   int as=Integer.parseInt(result)+1;
                   ordernum = String.valueOf(as);
               }

           }
           catch (Exception exception)
           {
           }


        }

    }



    private class AsyncFetch_open extends AsyncTask<String, String, String> {
         ProgressDialog pdLoading = new ProgressDialog(ProductProfileActivity.this);



        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {


                if (code!=null)
                {
                    String sq="https://sellerportal.perfectmandi.com/get_productDetail.php?id="+code;
                    System.out.println(sq);
                    url = new URL(sq);
                }



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

            System.out.println("Result is"+result);

            if (result!=null)
            {
                pdLoading.dismiss();
                try {
                    JSONArray jArray = new JSONArray(result);
                    for(int i=0;i<jArray.length();i++)
                    {
                        JSONObject json_data = jArray.getJSONObject(i);



                        path=json_data.getString("image_path");
                        ProductPrice=json_data.getString("Product_Unit_Price");
                        moq=json_data.getString("MOQ");
                        quan=json_data.getString("stock");
                        atcte.setText("Minimum order quantity: "+moq);
                        productname=json_data.getString("Product_Name");
                        productDescription=json_data.getString("Product_Description");
                        product_id=json_data.getString("id");
                        proid=product_id;
                        provider=json_data.getString("Product_Provider");
                        Prodcut_Sub_Category=json_data.getString("Prodcut_Sub_Category");
                        measure_category=json_data.getString("measure_category");
                        product_measure_in=json_data.getString("product_measure_in");
                        Glide.with(ProductProfileActivity.this).load(path).into(imageView);

                        Productname.setText(productname);
                        productprice.setText(ProductPrice);
                        ppdesc.setText(productDescription);
                        if (moq!=null)
                        {
                            // moq=intent.getStringExtra("moq");
                        }
                        else
                        {
                            moq="1";
                        }
                        qunatity=Integer.parseInt(quan);
                        min_quantity=Integer.parseInt(moq);


                        numberpicker.setMinValue(0);
                        numberpicker.setMaxValue(Integer.parseInt(quan));


checkorder();




                        if (measure_category.equalsIgnoreCase("Kilogram"))
                        {
                            avgweight=Double.parseDouble(product_measure_in);
                            ppmeasure.setVisibility(View.VISIBLE);
                            avgwe.setVisibility(View.VISIBLE);
                            avgwn.setVisibility(View.VISIBLE);
                            avgwn.setText(product_measure_in+""+" kg");
                        }
                        else
                        {
                            avgwe.setVisibility(View.INVISIBLE);
                            avgwn.setVisibility(View.INVISIBLE);
                            ppmeasure.setVisibility(View.INVISIBLE);
                            avgweight=0.0;
                        }

                        if (measure_category.equalsIgnoreCase("Kilogram"))
                        {
                            avgweight=Double.parseDouble(product_measure_in);
                            ppmeasure.setVisibility(View.VISIBLE);
                            ppmeasure.setText(" / KG ");
                            avgwe.setVisibility(View.VISIBLE);
                            avgwn.setVisibility(View.VISIBLE);
                            avgwn.setText(product_measure_in+""+" kg");
                        }
                        else if (measure_category.equalsIgnoreCase("Piece"))
                        {
                            avgwe.setVisibility(View.INVISIBLE);
                            avgwn.setVisibility(View.INVISIBLE);
                            ppmeasure.setVisibility(View.VISIBLE);
                            ppmeasure.setText(" / Piece ");
                            avgweight=0.0;
                        }
                        else if (measure_category.equalsIgnoreCase("Set"))
                        {
                            avgwe.setVisibility(View.INVISIBLE);
                            avgwn.setVisibility(View.INVISIBLE);
                            ppmeasure.setVisibility(View.VISIBLE);
                            ppmeasure.setText(" / Set ");
                            avgweight=0.0;
                        }
                        else if (measure_category.equalsIgnoreCase("Carton"))
                        {
                            avgwe.setVisibility(View.INVISIBLE);
                            avgwn.setVisibility(View.INVISIBLE);
                            ppmeasure.setVisibility(View.VISIBLE);
                            ppmeasure.setText(" / Carton");
                            avgweight=0.0;
                        }
                        else
                        {
                            avgwe.setVisibility(View.INVISIBLE);
                            avgwn.setVisibility(View.INVISIBLE);
                            ppmeasure.setVisibility(View.VISIBLE);
                            ppmeasure.setText("/ Dozen");
                            avgweight=0.0;
                        }

                        userid=contact;
                        id=provider;



                     /*   get_Data();*/

                        subtotal.setText("Rs :0");
                        total.setText("Rs :0");



                        mtotal=Integer.parseInt(ProductPrice)*min_quantity;

                        if (qunatity<min_quantity)
                        {
                            atcte.setText("Out of stock");
                            atcte.setTextColor(Color.RED);
                            numberpicker.setVisibility(View.GONE);
                            cq.setVisibility(View.GONE);
                            atc.setVisibility(View.GONE);
                        }
                        else
                        {
                            atcte.setText("Minimum order quantity: "+moq);
                        }






                        bucket.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent1=new Intent(ProductProfileActivity.this,IndexViewActivity.class);
                                startActivity(intent1);

                            }
                        });
                        backbutton.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (flag!=null)
                                {
                                    finish();
                                }
                                else
                                {
                                    Intent intent=new Intent(ProductProfileActivity.this,DashboardActivity.class);
                                    intent.putExtra("cname",Prodcut_Sub_Category);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }

                            }
                        });


                        ptc.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                if (session!=null)
                                {
                                    if (count>0)
                                    {
                                        if (count>=min_quantity)
                                        {
                                            flag_Add="buynow";
                                            new AsyncTaskUploadClass().execute();

                                        }
                                        else
                                        {
                                            Toast.makeText(ProductProfileActivity.this,"Please add minimum quantity",Toast.LENGTH_LONG).show();

                                        }

                                    }
                                    else
                                    {
                                        Toast.makeText(ProductProfileActivity.this,"Please add minimum quantity",Toast.LENGTH_LONG).show();

                                    }






                                }
                                else
                                {


                                    // sqLiteHelper.add_detail(product_id,"","","",Prodcut_Sub_Category,productname,productDescription,"",quan,ProductPrice,provider,"","yes","yes",quan,product_measure_in,measure_category,"","","","",moq,imgurl);
                                    Intent intent1=new Intent(ProductProfileActivity.this,LoginActivity.class);
                                    intent1.putExtra("flag","pp");

                                    System.out.println(product_id+"Plus");
                                    intent1.putExtra("code",product_id);
                                    //  startActivity(intent1);


                                }
                            }
                        });

                        atc.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {

                                if (session!=null)
                                {

                                    if (count>=min_quantity) {

                                        flag_Add = "normal";
                                        new AsyncTaskUploadClass().execute();
                       /*     if (gtotal >= mtotal) {

                            } else {
                                Toast.makeText(ProductProfileActivity.this, "Please add minimum quantity", Toast.LENGTH_LONG).show();
                            }*/
                                    }
                                    else
                                    {
                                        Toast.makeText(ProductProfileActivity.this, "Please add minimum Quantity", Toast.LENGTH_LONG).show();

                                    }
                                }
                                else
                                {
                                    Intent intent1=new Intent(ProductProfileActivity.this,LoginActivity.class);
                                    intent1.putExtra("flag","pp");
                                    intent1.putExtra("id_product",id_product);
                                    intent1.putExtra("code",id_product);
                                    startActivity(intent1);
                                }
                            }
                        });

                        numberpicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
                        {
                            @Override
                            public void onValueChange(NumberPicker numberPicker, int i, int i1)
                            {
                                count=i1;
                                calculate(i1);
                            }
                        });


                        visit_store.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {

                                Intent intent1=new Intent(ProductProfileActivity.this, MainVendorStoreActivity.class);
                                intent1.putExtra("pro",provider);
                                intent1.putExtra("Sub_Category",Prodcut_Sub_Category);
                                startActivity(intent1);

                            }
                        });




//
                    }







                    //



                }
                catch (Exception exception)
                {

                }
            }
            else
            {

            }

        }

    }




    String str;
    private class AsyncFetch extends AsyncTask<String, String, String> {


        ProgressDialog progressDialog = new ProgressDialog(ProductProfileActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Perfect Mandi");
            progressDialog.setIcon(R.drawable.optimizedlogo);
            // progressDialog.setMessage("Data is Loading...");
            //    progressDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            try
            {

                System.out.println("https://sellerportal.perfectmandi.com/get_incart.php?id="+contacts);

                url = new URL("https://sellerportal.perfectmandi.com/get_incart.php?id="+contact);

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
                conn.setDoOutput(true);
            }
            catch (IOException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }
            try
            {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK)
                {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
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


            try
            {
                JSONArray jArray = new JSONArray(result);
                for(int i=0;i<jArray.length();i++)
                {
                    JSONObject json_data = jArray.getJSONObject(i);

                    str=json_data.getString("id");

                    int a=Integer.parseInt(str);
                    if (a>0)
                    {
                        cart_circle.setVisibility(View.VISIBLE);
                        value.setText(str);

                    }
                    else
                    {
                        cart_circle.setVisibility(View.GONE);
                    }

                }

            }
            catch (Exception exception)
            {
            }

        }

    }



}