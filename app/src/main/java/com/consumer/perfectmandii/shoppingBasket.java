package com.consumer.perfectmandii;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.consumer.perfectmandii.Adapter.AdapterProductAddtoitem;
import com.consumer.perfectmandii.Customer.UserModel;
import com.consumer.perfectmandii.LocalDB.SQLiteHelper;
import com.consumer.perfectmandii.Model.OrderCartModel;
import com.poovam.pinedittextfield.CirclePinField;
import com.poovam.pinedittextfield.PinField;

import org.jetbrains.annotations.NotNull;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class shoppingBasket extends AppCompatActivity
{
    int update_quantity;
    String editflag_="no";

    ArrayList<String> totalprice_array,tempprice_array;
    ArrayList<String> singleprice_array;


    String moq,svailableStock;
    String ocid,product_id;

    int getCount;
    String json_string;


    RecyclerView shoppingbasket;
    String session,userid;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private AdapterProductAddtoitem mAdapter;
    //Customadaptor mAdapter;
    TextView textView,totalbill;
    CardView checkoutforshopping;
    ImageView backsc;
    int total=0;

    String ordernum;

    ArrayList<String> productid,productprice,totalamount,productquantity,vendorid,cu,productionidentif,racposition;
    String value,profilepic;

    String op;

    String currentDate;
    String vendor;

    String nextorderNumber;
    CheckBox sa;
    String ordernum_1;
    String name;
    String flags;
    List<OrderCartModel> data;

    int size;

    ArrayList<String>
            cposition;
    ArrayList<String>
            rcposition;

    TextView as;
    int sq;
    int totalprice;



    ArrayList<String> productPrice;
    ArrayList<String> productQuantity;
    ArrayList<String> productID;
    ArrayList<String> OproductPrice;
    ArrayList<String> OproductQuantity;
    ArrayList<String> OproductID;
    ArrayList<String> cProvider;
    ArrayList<String> cprovider;
    ArrayList<String> occid;




    int ordertotalvalue=0;
    List<UserModel> userModelList;
    SQLiteHelper sqLiteHelper;
    void get_Data_1()
    {
        sqLiteHelper=new SQLiteHelper(shoppingBasket.this);
        userModelList=sqLiteHelper.readCustomer();

        Toast.makeText(shoppingBasket.this,String.valueOf(userModelList.size()),Toast.LENGTH_LONG).show();
        if (userModelList.size()>0)
        {
            UserModel pointModel=userModelList.get(0);
            userid=pointModel.contact;
            session=pointModel.profilepic;
        }
        else
        {

        }
    }

    int oquan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_basket);

        //
        tempprice_array = new ArrayList<>();
        //



        occid=new ArrayList<>();
        singleprice_array = new ArrayList<>();
        totalprice_array = new ArrayList<>();
        productPrice = new ArrayList<>();
        productQuantity = new ArrayList<>();
        productID = new ArrayList<>();
        //
        OproductPrice = new ArrayList<>();
        OproductQuantity = new ArrayList<>();
        OproductID = new ArrayList<>();
        cProvider = new ArrayList<>();
        cprovider = new ArrayList<>();
        //sposition=new String[0];
        cposition = new ArrayList<>();
        racposition = new ArrayList<>();
        get_Data_1();




        if (session == null) {
            Intent intent1 = new Intent(shoppingBasket.this, LoginActivity.class);
            intent1.putExtra("flag","flag5");
            startActivity(intent1);
        }
        checkorder();
        //
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        productid = new ArrayList<>();

        productprice = new ArrayList<>();
        totalamount = new ArrayList<>();
        productquantity = new ArrayList<>();
        vendorid = new ArrayList<>();
        cu = new ArrayList<>();
        productionidentif = new ArrayList<>();
        //
        intializewidget();
        //

        // shoppingbasket.setNestedScrollingEnabled(false);
        backsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        new AsyncFetch().execute();


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

        checkoutforshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                for (int i=0;i<occid.size();i++)
                {

                    System.out.println(occid.get(i));
                }
                checklist();
                String string = JsonString();
                System.out.println(string);

                for (int i=0;i<racposition.size();i++)
                {
                    System.out.println(racposition.get(i));
                }



                if ("{\"upload_fishes\":]}".equalsIgnoreCase(string))
                {
                    Toast.makeText(shoppingBasket.this, "Please Add item", Toast.LENGTH_LONG).show();
                }
                else
                {


                    System.out.println(string);

                    new AsyncFetch_add().execute();
                    OproductPrice.remove(OproductPrice);
                    OproductQuantity.remove(OproductQuantity);
                    OproductID.remove(OproductID);
                    cposition.remove(cposition);
                    cprovider.remove(cprovider);
                    cProvider.remove(cProvider);
                }

            }
        });
    }


    void checklist()
    {

        populateList();



    }





    void populateList()
    {





        for (int val=0;val<racposition.size();val++)
        {
            int takeValue=Integer.parseInt(racposition.get(val));
            OproductPrice.add(productPrice.get(takeValue));
            OproductID.add(productID.get(takeValue));
            OproductQuantity.add(productQuantity.get(takeValue));
            cProvider.add(cprovider.get(takeValue));

        }
    }







    String JsonString() {

        int i;


        json_string=null;



        try {
            //Repeat and loop this until all objects are added (and add try+catch)
            json_string ="{\"upload_fishes\":[";
            for (i=0;i<racposition.size();i++)
            {

                JSONObject obj_new = new JSONObject();

                int v1=Integer.parseInt(ordernum);
                int v2=v1+1;
                op=String.valueOf(v2);
                obj_new.put("orderid",String.valueOf(v2));

                obj_new.put("productid",OproductID.get(i));
                obj_new.put("productprice",OproductPrice.get(i));
                obj_new.put("quan",OproductQuantity.get(i));
                obj_new.put("vendor",cProvider.get(i));
                obj_new.put("customer",userid);
                obj_new.put("date",currentDate);
                json_string = json_string + obj_new.toString() + ",";

//Close JSON string

            }
            json_string = json_string.substring(0, json_string.length()-1);
            json_string += "]}";



        }
        catch (JSONException jsox)
        {
            Toast.makeText(shoppingBasket.this,jsox.toString(),Toast.LENGTH_LONG).show();
        }

        return  json_string;
    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {


            String editflag=intent.getStringExtra("editflag");
            String mode=intent.getStringExtra("nat");

            if (mode.equalsIgnoreCase("ea"))
            {

                product_id=intent.getStringExtra("id");
               String product_stock=intent.getStringExtra("stock");

               String smoq=intent.getStringExtra("moq");
               int moq=Integer.parseInt(smoq);
                String product_measure_in=intent.getStringExtra("product_measure_in");
                String measure_category=intent.getStringExtra("measure_category");

                Toast.makeText(shoppingBasket.this, product_measure_in+""+measure_category, Toast.LENGTH_SHORT).show();
                int position=intent.getIntExtra("position",0);
                ocid=occid.get(position);
                int price=intent.getIntExtra("price",0);
                oquan=intent.getIntExtra("oquan",0);

                if (product_stock.equalsIgnoreCase("")||product_stock.equalsIgnoreCase("0"))
                {
                    Toast.makeText(context,"You can go  with this quantity",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (editflag.equalsIgnoreCase("1"))
                    {
                        try {
                            final NumberPicker numberPicker = new NumberPicker(shoppingBasket.this);
                            numberPicker.setMinValue(0);
                            //moq,svailableStock;
                            int totall=Integer.parseInt(product_stock)+oquan;
                            numberPicker.setMaxValue(totall);
                            numberPicker.setValue(oquan);
                            AlertDialog.Builder builder = new AlertDialog.Builder(shoppingBasket.this);
                            builder.setIcon(R.drawable.optimizedlogo);
                            builder.setTitle("Update your order");
                            builder.setMessage("Select Quantity :");
                            builder.setView(numberPicker);
                            builder.create();
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    editflag_="yes";

                                    sq=numberPicker.getValue();


                                    update_quantity=Integer.parseInt(product_stock)+oquan-sq;
                                    if (sq<moq)
                                    {
                                        Toast.makeText(shoppingBasket.this,"Please add minimum value",Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        if (measure_category.equalsIgnoreCase("Kilogram"))
                                        {
                                            Double mq=Double.parseDouble(product_measure_in);
                                            totalprice= (int) (price*sq*mq);

                                        }
                                        else
                                        {
                                            totalprice=price*sq;
                                        }

                                        totalprice_array.removeAll(totalprice_array);
                                        tempprice_array.removeAll(tempprice_array);
                                        racposition.removeAll(racposition);
                                        occid.removeAll(occid);
                                        //totalprice_array.set(0,String.valueOf(totalprice));

                                        new AsyncFetch_Edi().execute();
                                    }


                                    // Toast.makeText(shoppingBasket.this, totalprice +" "+ price +" "+ ocid +" "+ numberPicker.getValue(),Toast.LENGTH_LONG).show();
                                    // String link="https://staginigserver.perfectmandi.com/edit_order_item.php?id="+ocid+"&quan="+sq+"&tpric="+totalprice;
                                    // System.out.println(link);
                                }
                            });

                            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    editflag_="no";
                                }
                            });
                            builder.show();

                        }
                        catch (Exception ecx)
                        {

                        }


                    }
                }
            }



             if (mode.equalsIgnoreCase("ca"))
            {


                    String st = intent.getStringExtra("total");
                    String flagwe = intent.getStringExtra("flag");
                   int  pos=intent.getIntExtra("pos",0);






                    if (flagwe.equalsIgnoreCase("include"))
                    {

                        racposition.add(String.valueOf(pos));

                        tempprice_array.add(st);

                        ordertotalvalue=0;

                       // checkflag=intent.getStringExtra("checkflag");


                        for (int m=0;m<tempprice_array.size();m++)
                        {
                            ordertotalvalue=ordertotalvalue+Integer.parseInt(tempprice_array.get(m));
                        }
                        calculate_Panel(tempprice_array.size(),ordertotalvalue);
                    }
                    else
                    {
                        racposition.remove(String.valueOf(pos));
                        tempprice_array.remove(st);
                        ordertotalvalue=0;
                        for (int m=0;m<tempprice_array.size();m++)
                        {
                            ordertotalvalue=ordertotalvalue+Integer.parseInt(tempprice_array.get(m));
                        }
                        calculate_Panel(tempprice_array.size(),ordertotalvalue);

                    }





               /* }*/









            }


            else if ("da".equalsIgnoreCase(mode))
            {

                int pos=intent.getIntExtra("position",0);
                racposition.remove(String.valueOf(pos));

                String deleteValue=intent.getStringExtra("value_del");
                tempprice_array.remove(deleteValue);
                totalprice_array.remove(deleteValue);
                ocid=occid.get(pos);
                occid.remove(ocid);

                int removeIndex = pos;
                data.remove(removeIndex);
                mAdapter.notifyItemRemoved(removeIndex);



            }












        }
    };






    void calculate_Panel(int sizeofproduct,int totalValue)
    {

        textView.setText("Selected Item ("+sizeofproduct+")");
        totalbill.setText("Total Amount:"+totalValue);

    }




    void intializewidget()
    {
        shoppingbasket=findViewById(R.id.shoppingbasket);
        textView=findViewById(R.id.selecteditem);
        totalbill=findViewById(R.id.totalbill);
        checkoutforshopping=findViewById(R.id.checkoutforshopping);
        backsc=findViewById(R.id.bax);
        sa=findViewById(R.id.editva);
        as=findViewById(R.id.deleteb);
    }


    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(shoppingBasket.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setIcon(R.drawable.optimizedlogo);
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {


                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("https://sellerportal.perfectmandi.com/cartitem_1.php?id="+userid);

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
            // System.out.println(result);
            data=new ArrayList<>();


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
                    fishData.price=json_data.getString("price");
                    fishData.order_quantity=json_data.getString("quantity");
                    fishData.selling_price=json_data.getString("selling_price");
                    fishData.st_price=json_data.getString("total_price");
                    fishData.productDescription=json_data.getString("Product_Description");
                    fishData.ocid=json_data.getString("ocid");
                    fishData.stock=json_data.getString("stock");
                    fishData.MOQ=json_data.getString("MOQ");
                    fishData.measure_category=json_data.getString("measure_category");
                    fishData.product_measure_in=json_data.getString("product_measure_in");

                    // cposition.add(String.valueOf(i));
                    productPrice.add(fishData.price);
                    productQuantity.add(fishData.order_quantity);
                    productID.add(fishData.id);
                    cprovider.add(fishData.category_provider);

                    occid.add(fishData.ocid);



                    totalprice_array.add(fishData.st_price);
                    data.add(fishData);
                }

                getCount=data.size();
                size=data.size();

                mAdapter = new AdapterProductAddtoitem(shoppingBasket.this, data,flags);
                shoppingbasket.setAdapter(mAdapter);
                shoppingbasket.setLayoutManager( new LinearLayoutManager(shoppingBasket.this));


            } catch (Exception e) {
            }
        }

    }









    private class AsyncFetch_add extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(shoppingBasket.this);
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

                String operatingurl="https://sellerportal.perfectmandi.com/addmultipleitem.php?data="+json_string+"&id="+ordernum_1+"&pid="+userid+"&sess="+session;

                System.out.println(operatingurl);
                url = new URL(operatingurl);

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

            pdLoading.dismiss();

            int as=Integer.parseInt(ordernum)+1;

            String asd=String.valueOf(as);

            /* Toast.makeText(shoppingBasket.this,result,Toast.LENGTH_LONG).show();*/
            Intent intent=new Intent(shoppingBasket.this,OrderViewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("session",session);
            intent.putExtra("userid",userid);
            intent.putExtra("orderid",asd);
            intent.putExtra("total",String.valueOf(ordertotalvalue));
            intent.putExtra("name",name);
            intent.putExtra("path",profilepic);
            startActivity(intent);
            finish();

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


                url = new URL("https://sellerportal.perfectmandi.com/ordersessionuser.php?id="+userid);

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

            int as;

            if (result!=null)
            {

                nextorderNumber=result;
                ordernum=nextorderNumber;
                int asd=Integer.parseInt(result)+1;

                ordernum_1=String.valueOf(asd);
            }


            // Toast.makeText(shoppingBasket.this,"This is"+ordernum,Toast.LENGTH_LONG).show();

        }

    }


    private class AsyncFetch_Edi extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(shoppingBasket.this);
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
            try
            {



                url = new URL("https://sellerportal.perfectmandi.com/edit_order_item.php?id="+ocid+"&quan="+sq+"&tpric="+totalprice+"&uid="+userid+"&POQ="+oquan+"&pid="+product_id+"&uquan="+update_quantity);
            }
            catch (MalformedURLException e)
            {
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
            pdLoading.dismiss();

            if ("".equalsIgnoreCase(result))
            {

            }
            else if ("Records were Updated successfully.".equalsIgnoreCase(result))
            {

                /* Toast.makeText(shoppingBasket.this,result,Toast.LENGTH_LONG).show();*/
                new AsyncFetch().execute();
                //context.finish();

            }


        }

    }


}
