package com.consumer.perfectmandii;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.consumer.perfectmandii.Activity.InvoiceView;
import com.consumer.perfectmandii.Adapter.OrderConfirmation;
import com.consumer.perfectmandii.Customer.UserModel;
import com.consumer.perfectmandii.LocalDB.SQLiteHelper;
import com.consumer.perfectmandii.Model.OrderModel.ordermodel;
import com.consumer.perfectmandii.Room.EntityClass;

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
import java.util.Calendar;
import java.util.List;

public class OrderViewActivity extends AppCompatActivity
{
    String selected_ff;
    private List<EntityClass> list;

    RecyclerView recyclerView;
    String requestURL="https://sellerportal.perfectmandi.com/oc.php?id=";
    ordermodel orderModel;

    TextView dateandtime,subtotal,discount,totalordervalue,packingcharges,welcomeline;
    OrderConfirmation orderConfirmation;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String session,userid,orderid;
    CardView checkoutforshopping;
    LinearLayout previousbalance,invoicenum,ordernum;
    String total;
    int packingcharge=100;
    int grandtotal,discountvalue;
    String stotal,gtotal,packaging,discountprice,profilepic;
    String name,path;
    String dataanndtime;
    String invoice;
    String customer_name;

    SQLiteHelper sqLiteHelper;
    List<UserModel> userModelList;
    void get_Data_1()
    {
        sqLiteHelper=new SQLiteHelper(OrderViewActivity.this);
        userModelList=sqLiteHelper.readCustomer();

        Toast.makeText(OrderViewActivity.this,String.valueOf(userModelList.size()),Toast.LENGTH_LONG).show();
        if (userModelList.size()>0)
        {
            UserModel pointModel=userModelList.get(0);
            customer_name=pointModel.Name;


        }
        else
        {



        }
    }




    String getvalue_buisnessname,getvalue_primary_phone,getvalue_primary_address;

    void ShowInputDialog()
    {

        StringBuilder sb=new StringBuilder();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_freight_forwarder, null);
        intialize_widget(dialogView);
        dialogBuilder.setView(dialogView);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setLayout(width, height);

        alertDialog.show();

        province=new ArrayList<>();
        sqLiteHelper=new SQLiteHelper(OrderViewActivity.this);
        get_Data();
        step_1_1.setVisibility(View.VISIBLE);




        CardView step1=dialogView.findViewById(R.id.step1);

        step1.setVisibility(View.VISIBLE);

        confirm_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            cts.setText("Continue to Shopping");
            alertDialog.dismiss();
            }
        });

        step_1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                step_1_1.setVisibility(View.GONE);
                new AsyncFetch_1().execute();
                step2.setVisibility(View.VISIBLE);
            }
        });

        submint_click.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                get_value();
                if (getvalue_buisnessname.equalsIgnoreCase("") &&getvalue_primary_phone.equalsIgnoreCase("")&&getvalue_primary_address.equalsIgnoreCase(""))
                {
                    Toast.makeText(OrderViewActivity.this,"Please add value",Toast.LENGTH_LONG).show();
                }
                else
                {
                    cts.setText("Proceed to Pay");
                    selected_ff=getvalue_buisnessname+getvalue_primary_phone+getvalue_primary_address;
                    alertDialog.dismiss();
                }
            }
        });







    }
    public  class AsyncFetch_1 extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        ProgressDialog progressDialog=new ProgressDialog(OrderViewActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                url = new URL("https://sellerportal.perfectmandi.com/get_FreightForwarder.php?id="+city);
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
        protected void onPostExecute(String result)
        {



            System.out.println(result);

            Toast.makeText(OrderViewActivity.this,result,Toast.LENGTH_LONG).show();






            if (result.equalsIgnoreCase("No Record Found"))
            {
                progressDialog.dismiss();
                step3.setVisibility(View.VISIBLE);
                step2.setVisibility(View.GONE);

            }

            else if (result.contains("exception"))
            {
                Toast.makeText(OrderViewActivity.this,"Yes",Toast.LENGTH_LONG).show();
            }
            else
            {



                try
                {
                    spinner.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    JSONArray jArray = new JSONArray(result);

                    province.add("Select Freight Forwarder");
                    for (int i=0;i<jArray.length();i++)
                    {

                        JSONObject json_data = jArray.getJSONObject(i);


                        String sr=json_data.getString("name");



                        province.add(sr);


                    }
                    province.add("Others");
                    pupulate_parentcategory();
           /*         getMeasurement(jsonArray1);
                    getcategory(jsonArray_dealin);
                    getcolor(jsonArray_color);*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        void pupulate_parentcategory()
        {
            //spinner.setVisibility(View.GONE);
            // String [] values =
            //  {"Time at Residence","Under 6 months","6-12 months","1-2 years","2-4 years","4-8 years","8-15 years","Over 15 years",};
            //  Spinner spinner = (Spinner) v.findViewById(R.id.spinner1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(OrderViewActivity.this, android.R.layout.simple_spinner_item, province);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    String text = spinner.getSelectedItem().toString();


                    if (text.equalsIgnoreCase("Others"))
                    {
                        step3.setVisibility(View.VISIBLE);
                        step2.setVisibility(View.GONE);
                    }
                    else
                    {
                        selected_ff=text;
                    }

                }
                public void onNothingSelected(AdapterView<?> parent)
                {
                }
            });



        }





    }


    String select_province;
    String city;
    List<String> province;

    Spinner spinner;
    ConstraintLayout step2,step3;
    TextView step1,cts,submint_click;
    CardView step_1_1,confirm_d;
    EditText buisness_name,primary_phone,primary_address;
    void intialize_widget(View dialog)
    {
        spinner=dialog.findViewById(R.id.Freigh);
        step_1_1=dialog.findViewById(R.id.step1);
        //step1=findViewById(R.id.step1);
        step2=dialog.findViewById(R.id.maincontainer);
        step3=dialog.findViewById(R.id.maincontainer_form);
        confirm_d=dialog.findViewById(R.id.confirm_d);
        buisness_name=dialog.findViewById(R.id.buisness_name);
        primary_phone=dialog.findViewById(R.id.primary_phone);
        primary_address=dialog.findViewById(R.id.primary_address);
        submint_click=dialog.findViewById(R.id.submint_click);
    }


    void get_value()
    {
        getvalue_buisnessname=buisness_name.getText().toString();
        getvalue_primary_phone=primary_phone.getText().toString();
        getvalue_primary_address=primary_address.getText().toString();

    }


    void get_Data()
    {
        userModelList=sqLiteHelper.readCustomer();
        if (userModelList.size()>0)
        {
            UserModel pointModel=userModelList.get(0);
            city=pointModel.city;

        }
        else
        {



        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);

        intializewidget();
        get_Data_1();

        cts.setText("Proceed");
        welcomeline.setText("Dear ("+customer_name+"), thank you for your order!");
        dataanndtime = datatimeget();
        dateandtime.setText(dataanndtime);


        packingcharges.setText("Rs : "+ packingcharge);
        previousbalance.setVisibility(View.INVISIBLE);
        invoicenum.setVisibility(View.INVISIBLE);
        ordernum.setVisibility(View.INVISIBLE);

        Intent intent=getIntent();
      session = intent.getStringExtra("session");
      userid=intent.getStringExtra("userid");
      orderid=intent.getStringExtra("orderid");

      System.out.println(orderid);
      total=intent.getStringExtra("total");
      name=intent.getStringExtra("name");
      profilepic=intent.getStringExtra("path");

      grandtotal=Integer.parseInt(total)+packingcharge;
      subtotal.setText("Rs : "+total);
      discount.setText("Rs : "+"0");
      totalordervalue.setText("Rs : "+ grandtotal);


      getData();
     // welcomeline.setText("Dear ("+name+"), thank you for your order!");



      gtotal=String.valueOf(grandtotal);
      stotal=String.valueOf(total);
      packaging=String.valueOf(packingcharge);
      discountprice="0";




    //

      new AsyncFetch_add().execute();


   //   new AsyncFetch().execute();




      checkoutforshopping.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {


              if (cts.getText().equals("Proceed"))
              {
                  ShowInputDialog();
              }
              else
              {
                  Intent intent1=new Intent(OrderViewActivity.this, InvoiceView.class);
                  intent1.putExtra("session",session);
                  intent1.putExtra("userid",userid);
                  intent1.putExtra("name",name);
                  intent1.putExtra("path",profilepic);
                  intent1.putExtra("orderid",orderid);
                  intent1.putExtra("status","unpaid");
                  intent1.putExtra("gtotal",gtotal);
                  intent1.putExtra("stotal",stotal);
                  intent1.putExtra("packaging",packaging);
                  intent1.putExtra("f",selected_ff);

                  startActivity(intent1);
              }
            //  System.out.println(orderid);
          }
      });

    }
    private void getData() {



    }




    String  datatimeget()
    {
        Calendar cal = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = sdf.format(c.getTime());
        String  input = strDate;
        return input;
    }




    void intializewidget()
    {
        cts=findViewById(R.id.cts);
        invoicenum=findViewById(R.id.invoiceno);
        ordernum=findViewById(R.id.orderno);
        packingcharges=findViewById(R.id.packingcharges);
        subtotal=findViewById(R.id.subtotal);
        discount=findViewById(R.id.discountprice);
        recyclerView=findViewById(R.id.shoppingbasket);
        checkoutforshopping=findViewById(R.id.checkoutforshopping);
        dateandtime=findViewById(R.id.dateandtime);
        previousbalance=findViewById(R.id.previous_balance);
        welcomeline=findViewById(R.id.welcome_line);
        totalordervalue=findViewById(R.id.totalordervalue);

    }
    private class AsyncFetch_add extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(OrderViewActivity.this);

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


                System.out.println("https://sellerportal.perfectmandi.com/placeorder.php?id="+orderid+"&session="+session+"&userid="+userid+"&stotal="+stotal+"&gtotal="+gtotal+"&discount="+discountprice+"&pack="+packaging);



                url = new URL("https://sellerportal.perfectmandi.com/placeorder.php?id="+orderid+"&session="+session+"&userid="+userid+"&stotal="+stotal+"&gtotal="+gtotal+"&discount="+discountprice+"&pack="+packaging);

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

            new AsyncFetch().execute();
        }

    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        ProgressDialog pdLoading = new ProgressDialog(OrderViewActivity.this);
        HttpURLConnection conn;

        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();


        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String urls=requestURL+orderid;

                url = new URL(urls);

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

            //this method will be running on UI thread
            pdLoading.dismiss();

        }

    }


}