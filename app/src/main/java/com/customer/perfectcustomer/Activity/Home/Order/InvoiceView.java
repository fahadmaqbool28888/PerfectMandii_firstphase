package com.customer.perfectcustomer.Activity.Home.Order;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.customer.perfectcustomer.Adapter.BankDetailAdapter;
import com.customer.perfectcustomer.Adapter.Order.AdaptergetdetailPlacedOrder;
import com.customer.perfectcustomer.Model.Customer.UserModel;
import com.customer.perfectcustomer.LocalDB.DatabaseClass;
import com.customer.perfectcustomer.MainActivity_OP;
import com.customer.perfectcustomer.Model.Bank.BankModel;
import com.customer.perfectcustomer.Model.FreightForwarder;
import com.customer.perfectcustomer.R;

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
import java.util.ArrayList;
import java.util.List;

public class InvoiceView extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ArrayAdapter<String> spinnerArrayAdapter;

    List<String> strings=new ArrayList<>();
    List<FreightForwarder> freightForwarderList;
    String selected_ff;
    String getvalue_buisnessname,getvalue_primary_phone,getvalue_primary_address;
    RecyclerView OrderItem;
    AdaptergetdetailPlacedOrder mAdapter;
    BankDetailAdapter bankDetailAdapter;
    List<BankModel> bankModels;
    //
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String f,userid,orderid,packaging,subtotal,grandtotal,discount,invoice,session,status,sorder,id,name,pic;
    ImageView back;
    TextView getsubtotal,packagingCost,getgrandtotal,submitbill,getstatus,invoicenum,ordernum,order_cancel_id,submit_bill_later;
    String selectedff;
    int updte;
    String updatevalue;
    //
    String select_province;
    String city;
    List<String> province;

    Spinner spinner;
    ConstraintLayout step2,step3;
    TextView step1,cts,submint_click;
    CardView step_1_1,confirm_d;
    EditText primary_phone,primary_address;

    AutoCompleteTextView buisness_name;
    //
    RecyclerView bankRecycler;

    void get_value()
    {
        getvalue_buisnessname=buisness_name.getText().toString();
        getvalue_primary_phone=primary_phone.getText().toString();
        getvalue_primary_address=primary_address.getText().toString();

    }


    DatabaseClass sqLiteHelper;
    List<UserModel> userModelList;
    CheckBox manage_byself;
    void intialize_widget(View dialog)
    {
        manage_byself=dialog.findViewById(R.id.manage_byself);

        spinner=dialog.findViewById(R.id.spinner1);
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



    DatabaseClass getconn(Context context)
    {
        return new DatabaseClass(context);
    }
    void get_Data_2()
    {
        sqLiteHelper= getconn(InvoiceView.this);


        //userModelList=sqLiteHelper.arrayList();
        freightForwarderList=sqLiteHelper.getFreightList();
        if (freightForwarderList.size()>0)
        {
            for (int i=0;i<freightForwarderList.size();i++)
            {
                FreightForwarder pointModel=freightForwarderList.get(i);

                strings.add(pointModel.name.toString());
                  System.out.println(pointModel.name);
            }

        }
        else
        {

        }



    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }






String flag;


    String fetchBankUrl="https://sellerportal.perfectmandi.com/get_bankDetail.php";

    private class bankFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(InvoiceView.this);
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
                url = new URL(fetchBankUrl);

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

bankModels=new ArrayList<>();

            try
            {
                JSONArray jArray = new JSONArray(result);

                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);


                    BankModel bankModel= new BankModel();
                    //1[{"id":"3","bank_name":"Easy Paisa","account_title":"03345381060","acc_num":"Aqeel Nasir"}]
                    bankModel.id=json_data.getString("id");
                   bankModel.branchAccTitle= json_data.getString("account_title");
                    bankModel.name= json_data.getString("bank_name");
                    bankModel.branchAccNum= json_data.getString("acc_num");

bankModels.add(bankModel);

                }

                bankDetailAdapter=new BankDetailAdapter(InvoiceView.this,bankModels);
                bankRecycler.setAdapter(bankDetailAdapter);
                bankRecycler.setLayoutManager(new LinearLayoutManager(InvoiceView.this));


/*
                mAdapter = new AdapterProductAddtoitem(I.this, data);
                shoppingbasket.setAdapter(mAdapter);
                shoppingbasket.setLayoutManager( new LinearLayoutManager(shoppingBasket.this));*/


            } catch (Exception e) {
            }
        }

    }

    public  class AsyncFetch_1 extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;


        @Override
        protected String doInBackground(String... params) {
            try
            {
                System.out.println("https://sellerportal.perfectmandi.com/get_FreightForwarder.php?id="+city);
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







            if (result.equalsIgnoreCase("No Record Found"))
            {

            }

            else if (result.contains("exception"))
            {
            }
            else
            {



                try
                {
                    spinner.setVisibility(View.VISIBLE);
                    JSONArray jArray = new JSONArray(result);

                    province.add("Select Freight Forwarder");
                    for (int i=0;i<jArray.length();i++)
                    {

                        JSONObject json_data = jArray.getJSONObject(i);


                        String sr=json_data.getString("name");
                        String address=json_data.getString("address");
                        String contact=json_data.getString("contact");

                        FreightForwarder freightForwarder=new FreightForwarder(sr,address,contact,userid);

                        boolean hasbeen=sqLiteHelper.checkAllready(sr);

                        if (!hasbeen)
                        {
                            sqLiteHelper.addFreight(freightForwarder);
                            strings.add(sr);
                        }
                        else {
                          //  Toast.makeText(InvoiceView.this,"Not add",Toast.LENGTH_LONG).show();


                        }



                    }
                    //province.add("Others");
                    //pupulate_parentcategory();
           /*         getMeasurement(jsonArray1);
                    getcategory(jsonArray_dealin);
                    getcolor(jsonArray_color);*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }






    }

    void ShowInputDialog()
    {
        if (strings.size()>0)
        {

        }
        else {
            new AsyncFetch_1().execute();
        }
        String[] languages={"Android ","java","IOS","SQL","JDBC","Web services"};

        strings.clear();
        strings.add("Select Freight Forwarder");
        get_Data_2();
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
       /* sqLiteHelper=new SQLiteHelper(OrderViewActivity.this);
        get_Data();*/
        step_1_1.setVisibility(View.VISIBLE);





        CardView step1=dialogView.findViewById(R.id.step1);
        step1.setVisibility(View.VISIBLE);
        //   populate_list(freightForwarderslist);


        confirm_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
        /*        if (manage_byself_1.isChecked())
                {
                    cts.setText("Proceed to Pay");

                    selected_ff="Managed by Perfect Mandi";
                    alertDialog.dismiss();
                }
                else
                {
                    spinner.setVisibility(View.VISIBLE);

                    cts.setText("Proceed to Pay");
                    alertDialog.dismiss();
                }*/



            }
        });

        step_1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                step_1_1.setVisibility(View.GONE);

                //step2.setVisibility(View.VISIBLE);
                step3.setVisibility(View.VISIBLE);
            }
        });

        submint_click.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                if (manage_byself.isChecked())
                {
                    spinner.setEnabled(false);
                    submitbill.setText("Proceed to Pay");

                    selected_ff="Managed by Perfect Mandi";
                    f=selected_ff;
                    alertDialog.dismiss();
                }
                else
                {
                    get_value();
                    if (getvalue_buisnessname.equalsIgnoreCase("") &&getvalue_primary_phone.equalsIgnoreCase("")&&getvalue_primary_address.equalsIgnoreCase(""))
                    {

                        Toast.makeText(InvoiceView.this,"Please add value",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        submitbill.setText("Proceed to Pay");
                        FreightForwarder freightForwarder=new FreightForwarder(getvalue_buisnessname,getvalue_primary_address,getvalue_primary_phone,userid);

                        boolean checkagain= sqLiteHelper.checkAllready(getvalue_buisnessname);
                        if (!checkagain)
                        {
                            boolean add=sqLiteHelper.addFreight(freightForwarder);
                            if (add)
                            {
                                selected_ff=getvalue_buisnessname+" "+getvalue_primary_phone+" "+getvalue_primary_address;
                                f=selected_ff;
                                alertDialog.dismiss();
                            }

                        }
                        else {
                            selected_ff=getvalue_buisnessname+" "+getvalue_primary_phone+" "+getvalue_primary_address;
                          f=selected_ff;
                            alertDialog.dismiss();
                        }




                    }
                }

            }
        });


        if (strings.size()<1)
        {

            spinner.setVisibility(View.GONE);
        }

        else {

            spinner.setVisibility(View.VISIBLE);
            ArrayAdapter adapter = new
                    ArrayAdapter(this,android.R.layout.simple_list_item_1,strings);

            buisness_name.setAdapter(adapter);
            buisness_name.setThreshold(1);

            buisness_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    String str=buisness_name.getText().toString();
                    ArrayList<FreightForwarder> list=sqLiteHelper.cready(str);
                    //Toast.makeText(OrderViewActivity.this,buisness_name.getText().toString(),Toast.LENGTH_LONG).show();

                    FreightForwarder ff=list.get(0);
                    primary_phone.setText(ff.contact);
                    primary_address.setText(ff.address);

                }
            });



            //           text1.setAdapter(adapter);

            // Spinner click listener
            spinner.setOnItemSelectedListener(this);

            // Spinner Drop down elements


            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strings);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);
            //buisness_name.setAdapter(dataAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                {
                    String text = spinner.getSelectedItem().toString();
                    ArrayList<FreightForwarder> list=sqLiteHelper.cready(text);
                    //Toast.makeText(OrderViewActivity.this,buisness_name.getText().toString(),Toast.LENGTH_LONG).show();

                    if (list.size()>0)
                    {
                        FreightForwarder ff=list.get(0);
                        buisness_name.setText(ff.name);
                        primary_phone.setText(ff.contact);
                        primary_address.setText(ff.address);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }


    }





    void get_Data_1()
    {
        sqLiteHelper=new DatabaseClass(InvoiceView.this);


        userModelList=sqLiteHelper.arrayList();
        freightForwarderList=sqLiteHelper.getFreightList();
        if (userModelList.size()>0)
        {
            UserModel pointModel=userModelList.get(0);

            userid=pointModel.contact;
            session=pointModel.session;
            city=pointModel.city;
        }
        else
        {

        }



    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_view);


          IntializeWidget();
         // new bankFetch().execute();


        get_Data_1();

        Intent intent = getIntent();
         id=intent.getStringExtra("id");


        userid = intent.getStringExtra("userid");
        orderid = intent.getStringExtra("orderid");
     //

        flag=intent.getStringExtra("flag");
        invoice = intent.getStringExtra("invoice");
        sorder = intent.getStringExtra("sorder");
        status = intent.getStringExtra("status");
        grandtotal=intent.getStringExtra("gtotal");
        subtotal=intent.getStringExtra("stotal");
        packaging=intent.getStringExtra("packaging");
        session=intent.getStringExtra("session");
        name=intent.getStringExtra("name");
        pic=intent.getStringExtra("path");
        f=intent.getStringExtra("f");
        //Toast.makeText(InvoiceView.this,f,Toast.LENGTH_LONG).show();



        getsubtotal.setText(subtotal);
        packagingCost.setText(packaging);
        getgrandtotal.setText(grandtotal);
        invoicenum.setText(invoice);

        updte=Integer.parseInt(orderid)-1;
     updatevalue=Integer.toString(updte);

        ordernum.setText("Order Number: " + orderid);

        getsubtotal.setText(subtotal);
        packagingCost.setText(packaging);
        getgrandtotal.setText(grandtotal);
        invoicenum.setText("Invoice Number: " + invoice);
        ordernum.setText("Order Number: " + orderid);
        getstatus.setText(status);
      //  new AsyncFetch().execute();

        if (status.equalsIgnoreCase("in review"))
        {
            getstatus.setTextColor(Color.BLUE);
            submitbill.setVisibility(View.INVISIBLE);
            submit_bill_later.setVisibility(View.INVISIBLE);


            //Toast.makeText(InvoiceView.this,"Slip submit,Under review",Toast.LENGTH_LONG).show();
        } else if (status.equalsIgnoreCase("Paid")) {

            submitbill.setVisibility(View.INVISIBLE);
            getstatus.setText(status);
            getstatus.setTextColor(Color.GREEN);



            //Toast.makeText(InvoiceView.this,"Invoice Paid",Toast.LENGTH_LONG).show();
        } else
        {
            new bankFetch().execute();

            if(f==null)
            {
              submitbill.setText("Add Freight Forwarder");
            }




                submitbill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (submitbill.getText().equals("Add Freight Forwarder"))
                        {
                            ShowInputDialog();
                        }
                        else
                        {
                            System.out.println(userid+" "+session);
                            Intent intent1 = new Intent(InvoiceView.this, PaymentDepositActivity.class);
                            intent1.putExtra("invoice", invoice);
                            intent1.putExtra("grandtotal", grandtotal);
                            intent1.putExtra("userid", userid);
                            intent1.putExtra("orderid", orderid);
                            intent1.putExtra("packaging", packaging);
                            intent1.putExtra("subtotal", subtotal);
                            intent1.putExtra("discount", discount);
                            intent1.putExtra("session", session);
                            intent1.putExtra("name",name);
                            intent1.putExtra("pic",pic);
                            intent1.putExtra("f",f);
                            startActivity(intent1);
                        }

                    }
                });


            submit_bill_later.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    if (flag!=null)
                    {
                        finish();
                    }
                    else
                    {
                        Intent intent1=new Intent(InvoiceView.this, MainActivity_OP.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                    }


                }
            });




           //
            // new AsyncFetch().execute();

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


            //



            order_cancel_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (status.equalsIgnoreCase("in review")) {

                        Toast.makeText(InvoiceView.this, "Sorry This order is in process", Toast.LENGTH_LONG).show();
                    } else if (status.equalsIgnoreCase("Paid")) {


                        Toast.makeText(InvoiceView.this, "Sorry This order is in process", Toast.LENGTH_LONG).show();
                    } else {

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        new AsyncFetch_Del().execute();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceView.this);
                        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();

                        //Toast.makeText(InvoiceView.this,id,Toast.LENGTH_LONG).show();

                    }
                }
            });

        }
    }


    void IntializeWidget()
    {
        bankRecycler=findViewById(R.id.bankRecycler);
        //
        order_cancel_id=findViewById(R.id.order_cancel_id);
        //subtotal_Bill
        getstatus=findViewById(R.id.status);

        OrderItem=findViewById(R.id.Order_Detail_1);

        getsubtotal=findViewById(R.id.getsubtotal_i);
        packagingCost=findViewById(R.id.packagingcost);
        getgrandtotal=findViewById(R.id.Grandtotal);
        submitbill=findViewById(R.id.submit_bill);
        back=findViewById(R.id.bax_q);

        invoicenum=findViewById(R.id.itext);
        ordernum=findViewById(R.id.otext);
        submit_bill_later=findViewById(R.id.submit_bill_later);

    }

 /*   private class AsyncFetch extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(InvoiceView.this);
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
                System.out.println("https://staginigserver.perfectmandi.com/orderdetail.php?id="+userid+"&oid="+orderid);
                url = new URL("https://staginigserver.perfectmandi.com/orderdetail.php?id="+userid+"&oid="+orderid);
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
            List<getorder> data=new ArrayList<>();


            System.out.println(result);
       //     pdLoading.dismiss();
            try
            {
                JSONArray jArray = new JSONArray(result);

                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    getorder fishData = new getorder();
                    fishData.image_path=json_data.getString("image_path");
                    fishData.Product_Name=json_data.getString("Product_Name");
                    fishData.Product_Description=json_data.getString("Product_Description");
                    fishData.selling_price=json_data.getString("selling_price");
                    fishData.total_price=json_data.getString("total_price");
                    fishData.order_quantity=json_data.getString("order_quantity");

                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview

                mAdapter = new AdaptergetdetailPlacedOrder(InvoiceView.this, data);
                OrderItem.setAdapter(mAdapter);
                OrderItem.setLayoutManager( new LinearLayoutManager(InvoiceView.this));


            } catch (Exception e) {
            }
        }

    }
*/

    private class AsyncFetch_Del extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(InvoiceView.this);
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
                String link="https://staginigserver.perfectmandi.com/delete_order.php?id="+id;
                System.out.println(link);
                url = new URL("https://staginigserver.perfectmandi.com/delete_order.php?id="+id);
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
            else if ("Records were deleted successfully.".equalsIgnoreCase(result))
            {

                Toast.makeText(InvoiceView.this,result,Toast.LENGTH_LONG).show();
                finish();
            }


        }

    }
}