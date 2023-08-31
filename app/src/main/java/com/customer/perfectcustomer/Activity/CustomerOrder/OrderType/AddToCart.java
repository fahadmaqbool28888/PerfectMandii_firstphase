package com.customer.perfectcustomer.Activity.CustomerOrder.OrderType;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.customer.perfectcustomer.Adapter.AdapterProductAddtoitem;
import com.customer.perfectcustomer.Model.Customer.UserModel;
import com.customer.perfectcustomer.LocalDB.DatabaseClass;
import com.customer.perfectcustomer.Dialog.Fragment.EditOrderFragment;
import com.customer.perfectcustomer.MainActivity_OP;
import com.customer.perfectcustomer.Model.OrderCartModel;
import com.customer.perfectcustomer.Model.RFQMODEL.rfq_model;
import com.customer.perfectcustomer.R;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;


public class AddToCart extends Fragment implements EditOrderFragment.Valueselect
{
    LinearLayout container;
    List<OrderCartModel> orderQueue;

    int valget;
    EditOrderFragment alertDialog;
int val=0;
    String radn;
    int sizeoflist,sumoftotal,sizeconstant;
    int totalvalue=0;
    public String ServerUploadPath="https://sellerportal.perfectmandi.com/addmultipleItem_1.php";
    boolean flagSelectAll;

    ArrayList<Integer> total_value;

    String json_string;


    RecyclerView shoppingbasket;
    String session,userid;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private AdapterProductAddtoitem mAdapter;

    TextView textView,totalbill,cts;
    CardView checkoutforshopping;
    ImageView backsc;


    String ordernum;

    String nextorderNumber;
    CheckBox sa;
    String ordernum_1;

    List<OrderCartModel> data;


    TextView as;

    List<UserModel> userModelList;
    DatabaseClass sqLiteHelper;
    void get_Data_1()
    {
        sqLiteHelper=new DatabaseClass(getActivity());


        userModelList=sqLiteHelper.arrayList();
        if (userModelList.size()>0)
        {
            UserModel pointModel=userModelList.get(0);
            userid=pointModel.contact;
            session=pointModel.session;
        }
        else
        {

        }
    }

    int oquan;

    String string;
    String SESSION="sess";
    String ID="id";
    String PID="pid";
    String DATA="data";

    boolean orderque(String flag,OrderCartModel orderCartModel)
    {
        if (flag.equalsIgnoreCase("add"))
        {
            if(orderQueue.add(orderCartModel))
            {
                return true;
            }

            else
            {
                return false;
            }
        }
        else {
            if(orderQueue.remove(orderCartModel))
            {
                return true;
            }

            else
            {
                return false;
            }
        }

    }

    void calculateorder(String operation,String loop,OrderCartModel CartModel)
    {
        if(loop.equalsIgnoreCase("yes"))
        {
            if (operation.equalsIgnoreCase("add"))
            {


                for (int i=0;i<orderQueue.size();i++)
                {
                    OrderCartModel orderCartModel=orderQueue.get(i);
                    sizeoflist=sizeoflist+1;
                    sumoftotal=sumoftotal+Integer.parseInt(orderCartModel.st_price);
                }
                calculate_Panel(sizeoflist,sumoftotal);
            }
            else if (operation.equalsIgnoreCase("minus"))
            {

                orderQueue.clear();

                if (orderQueue.size()<1)
                {
                    sizeoflist=0;
                    sumoftotal=0;
                }
                calculate_Panel(sizeoflist,sumoftotal);
            }
        }
        else
        {
            if (operation.equalsIgnoreCase("add"))
            {
                if(orderque(operation,CartModel))
                {
                    sizeoflist=sizeoflist+1;
                    sumoftotal=sumoftotal+Integer.parseInt(CartModel.st_price);
                    calculate_Panel(sizeoflist,sumoftotal);
                }

            }
            else
            {
                if(orderque(operation,CartModel)) {
                    sizeoflist = sizeoflist - 1;
                    sumoftotal = sumoftotal - Integer.parseInt(CartModel.st_price);
                    calculate_Panel(sizeoflist, sumoftotal);
                }
            }
        }

    }

    TextView textViews,qpro1;
    List<rfq_model> rfq_modelList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_to_cart, container, false);



        orderQueue=new ArrayList<>();

        //WorkManager


        intializewidget(view);


        textViews.setVisibility(View.GONE);
        qpro1.setVisibility(View.GONE);
        get_Data_1();

        new AsyncFetch_check().execute();
        new AsyncFetch().execute();

        // Step 6: Set OnClickListener for "Select All" checkbox
        // Step 6: Set OnClickListener for "Select All" checkbox
        sa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {



                //datas.clear();

                if (sa.isChecked())
                {
                    if (orderQueue.size()>0)
                    {
                        orderQueue.clear();
                    }
                    sumoftotal=0;
                    sizeoflist=0;
                    val=0;
                    mAdapter.selectAllItems();

                    for (int i=0;i<data.size();i++)
                    {
                        OrderCartModel order=data.get(i);

                        orderque("add",order);
                    }



                    Toast.makeText(getActivity().getApplicationContext(),String.valueOf(orderQueue.size()),Toast.LENGTH_LONG).show();
                    calculateorder("add","yes",null);


                }
                else
                {

                    mAdapter.unselectAllItems();

                    calculateorder("minus","yes",null);


                }
            }
        });





        checkoutforshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (cts.getText().equals("Continue for Shopping"))
                {
                    startActivity(new Intent(getActivity().getApplicationContext(), MainActivity_OP.class));
                }
                else {
                    //       OproductID.size();
                    string = JsonString();
         /*       System.out.println(string);
                string=null;*/


                    System.out.println(" RfQ String is "+string);

                    if ("{\"upload_fishes\":]}".equalsIgnoreCase(string))
                    {
                        Toast.makeText(getActivity().getApplicationContext(), "Please Add item", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        System.out.println("That Data "+string);
                        //   Toast.makeText(getApplicationContext(),string,Toast.LENGTH_LONG).show();
                        ImageUploadToServerFunction();

                        orderQueue.clear();
                    }
                }



                //  checklist();



            }

        });








        return view;
    }




    String JsonString() {
        Random rand = new Random();
        int rand_int2 = rand.nextInt(1000000);
        radn=String.valueOf(rand_int2);
        new AsyncFetch_check().execute();

        //checklist();
        int i;


        json_string=null;



        try {
            //Repeat and loop this until all objects are added (and add try+catch)
            json_string ="{\"upload_fishes\":[";
            for (i=0;i<orderQueue.size();i++)
            {

                JSONObject obj_new = new JSONObject();




                //  obj_new.put("orderid",String.valueOf(v2));

                OrderCartModel orderCartModel=orderQueue.get(i);
                //  obj_new.put("orderid",String.valueOf(v2));
                rfq_model rfqmodel=new rfq_model();
                rfqmodel.ocid=orderCartModel.ocid;
                rfqmodel.i_d=orderCartModel.id;
                rfqmodel.r_f_q=String.valueOf(rand_int2);
                obj_new.put("ocid",orderCartModel.ocid);
                obj_new.put("productid",orderCartModel.id);
                // obj_new.put("productprice",OproductPrice.get(i));
                obj_new.put("quan",orderCartModel.order_quantity);
                obj_new.put("rfq",String.valueOf(rand_int2));
                //  obj_new.put("vendor",cProvider.get(i));
                // obj_new.put("customer",userid);
                //  obj_new.put("date",currentDate);

                if (!sqLiteHelper.yes_RFQ(rfqmodel))
                {

                    boolean add= sqLiteHelper.addRFQ(rfqmodel);
                    if (add)
                    {
                        json_string = json_string + obj_new + ",";
                    }
                }
                else
                {

                }


//Close JSON string

            }
            json_string = json_string.substring(0, json_string.length()-1);
            json_string += "]}";



        }
        catch (JSONException jsox)
        {
            Toast.makeText(getActivity().getApplicationContext(),jsox.toString(),Toast.LENGTH_LONG).show();
        }

        return  json_string;
    }

    Double totals;
    void setcalculate(int update_quantity,OrderCartModel orderCartModel)
    {
        if (orderCartModel.measure_category.equalsIgnoreCase("Kilogram"))
        {
            totals =Double.parseDouble(orderCartModel.selling_price)*update_quantity*Double.parseDouble(orderCartModel.product_measure_in);
        }
        else
        {
            totals =Double.parseDouble(orderCartModel.selling_price)*update_quantity;
        }

        int total=totals.intValue();
        Log.d(TAG,"onclick"+ String.valueOf(total)+" "+String.valueOf(update_quantity)+" "+orderCartModel.ocid);

        new AsyncFetch_Edi(String.valueOf(total),String.valueOf(update_quantity),orderCartModel.ocid).execute();

    }
    AlertDialog alertDialogs;
    private void showAlertDialog(OrderCartModel orderCartModel)
    {



        final AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.number_picker_dialog, null);
        d.setTitle("PerfectMANDI");
        d.setMessage("Change Quantity");
        d.setIcon(R.drawable.optimizedlogo);
        d.setView(dialogView);
        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker);
        numberPicker.setMaxValue(50);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Log.d(TAG, "onValueChange: ");
            }
        });
        d.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

                valget=numberPicker.getValue();

                setcalculate(valget,orderCartModel);
                //new AsyncFetch_Edi(valget,orderCartModel);
              //  Log.d(TAG, "onClick: " +totalprice+""+ numberPicker.getValue());
            }
        });
        d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = d.create();
        alertDialog.show();


    }
    private void showDeleteDialog(OrderCartModel orderCartModel)
    {



        final AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
       // LayoutInflater inflater = this.getLayoutInflater();
      //  View dialogView = inflater.inflate(R.layout.number_picker_dialog, null);
        d.setTitle("PerfectMANDI");
        d.setMessage("Are you sure you want to delete this item?");
        d.setIcon(R.drawable.optimizedlogo);

        d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String id=orderCartModel.ocid;
                new AsyncFetch_Del(id).execute();

                //new AsyncFetch_Edi(valget,orderCartModel);
                //  Log.d(TAG, "onClick: " +totalprice+""+ numberPicker.getValue());
            }
        });
        d.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                alertDialogs.dismiss();
            }
        });
        alertDialogs = d.create();
        alertDialogs.show();


    }










    void calculate_Panel(int sizeofproduct,int totalValue)
    {

        textView.setText("Selected Items ("+sizeofproduct+")");
        totalbill.setText("Total Amount:"+totalValue);

    }




    void intializewidget(View view)
    {
        container=view.findViewById(R.id.container);
        textViews=view.findViewById(R.id.qpro);
        qpro1=view.findViewById(R.id.qpro1);
        shoppingbasket=view.findViewById(R.id.shoppingbasket);
        textView=view.findViewById(R.id.selecteditem);
        totalbill=view.findViewById(R.id.totalbill);
        checkoutforshopping=view.findViewById(R.id.checkoutforshopping);
        backsc=view.findViewById(R.id.bax);
        sa=view.findViewById(R.id.editva);
        as=view.findViewById(R.id.deleteb);
        cts=view.findViewById(R.id.cts);
    }

    @Override
    public void selectData(int val) {
        alertDialog.dismiss();
        Toast.makeText(getActivity().getApplicationContext(),String.valueOf(val),Toast.LENGTH_LONG).show();
    }


    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setIcon(R.drawable.optimizedlogo);
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(true);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String urls="https://sellerportal.perfectmandi.com/cartitem_1.php?id="+userid;

                System.out.println(urls);

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
        protected void onPostExecute(String result)
        {
            if (result.equalsIgnoreCase(""))
            {
                cts.setText("Continue for Shopping");
                rfq_modelList=sqLiteHelper.getRFQlist();
                textViews.setVisibility(View.VISIBLE);

                if (rfq_modelList.size()>0)
                {
textViews.setText("Your quotation is in process");
qpro1.setVisibility(View.VISIBLE);
                }
                else
                {
                    textViews.setText("Add items in cart to get Quotation");
                    qpro1.setVisibility(View.GONE);

                }
                container.setVisibility(View.GONE);
            }
            else {
                container.setVisibility(View.VISIBLE);
                cts.setText("Get Quotation");

            }

            //this method will be running on UI thread


            total_value=new ArrayList<>();
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

                    System.out.println("From shopping "+json_data.getString("name"));
                    fishData.Accountid= json_data.getString("Accountid");
                    fishData.l2_product_name= json_data.getString("l2_product_name");
                    fishData.category_provider= json_data.getString("category_provider");
                    fishData.status= json_data.getString("status");
                    fishData.price=json_data.getString("price");
                    fishData.order_quantity=json_data.getString("quantity");
                    fishData.selling_price=json_data.getString("selling_price");
                    fishData.st_price=json_data.getString("total_price");

                    totalvalue=Integer.parseInt(fishData.st_price);
                    total_value.add(totalvalue);
                    fishData.productDescription=json_data.getString("Product_Description");
                    fishData.ocid=json_data.getString("ocid");
                    fishData.stock=json_data.getString("stock");
                    fishData.MOQ=json_data.getString("MOQ");
                    fishData.measure_category=json_data.getString("measure_category");
                    fishData.product_measure_in=json_data.getString("product_measure_in");

                    fishData.setSelected(false);


                    data.add(fishData);
                }

               /* getCount=data.size();
                size=data.size();*/

                sizeconstant=data.size();
                mAdapter = new AdapterProductAddtoitem(getActivity().getApplicationContext(), data, flagSelectAll) {


                    @Override
                    public void update_val(String flag, OrderCartModel orderCartModel) {
                        if (flag.equalsIgnoreCase("edit"))
                        {

                            showAlertDialog(orderCartModel);
                        }
                        else {

                            showDeleteDialog(orderCartModel);
                       /*     String id=orderCartModel.ocid;
                            new AsyncFetch_Del(id).execute();*/
                        }
                    }

                    @Override
                    public void removeItems(int pos, OrderCartModel orderCartModel) {





                        sa.setChecked(false);


                        calculateorder("minus","no",orderCartModel);
                    }

                    @Override
                    public void passitem(String flag, OrderCartModel orderCartModel)
                    {
                        if (flag.equalsIgnoreCase("include"))
                        {
                            System.out.println(orderCartModel.id+" "+orderCartModel.ocid+" "+orderCartModel.selling_price+" "+orderCartModel.order_quantity+" "+orderCartModel.st_price+" "+orderCartModel.measure_category+" "+orderCartModel.product_measure_in);


                            String id=orderCartModel.id;
                            String quan=orderCartModel.order_quantity;
                            String ocid=orderCartModel.ocid;

                            String mc=orderCartModel.measure_category;
                            String pmi=orderCartModel.product_measure_in;
                            String price=orderCartModel.selling_price;
                            Double total=0.0,quantity=0.0,pkr=0.0,pmis=0.0;

                            if (mc.equalsIgnoreCase("Kilogram"))
                            {
                                System.out.println("quan"+quan);System.out.println("price"+price);System.out.println("pmi"+pmi);
                                quantity=Double.parseDouble(quan);
                                pkr=Double.parseDouble(price);
                                pmis=Double.parseDouble(pmi);
                                total=quantity*pkr*pmis;
                                // Toast.makeText(ShoppingRFQActivity.this,)
                                // total=Integer.parseInt(quan)*Integer.parseInt(price)*Integer.parseInt(pmi);
                            }
                            else if (mc.equalsIgnoreCase("Piece"))
                            {
                                quantity=Double.parseDouble(quan);
                                pkr=Double.parseDouble(price);

                                total=quantity*pkr;

                                System.out.println(total+"total");
                                // Toast.makeText(ShoppingRFQActivity.this,)
                                // total=Integer.parseInt(quan)*Integer.parseInt(price)*Integer.parseInt(pmi);
                            }
                            else if (mc.equalsIgnoreCase("Set"))
                            {
                                quantity=Double.parseDouble(quan);
                                pkr=Double.parseDouble(price);

                                total=quantity*pkr;

                                System.out.println(total+"total");
                                // Toast.makeText(ShoppingRFQActivity.this,)
                                // total=Integer.parseInt(quan)*Integer.parseInt(price)*Integer.parseInt(pmi);
                            }
                            else {
                                total=quantity*pkr;
                            }
                            calculateorder("add","no",orderCartModel);





                        }
                        else
                        {
                            System.out.println(orderCartModel.id+" "+orderCartModel.ocid+" "+orderCartModel.order_quantity+" "+orderCartModel.st_price+" "+orderCartModel.measure_category+" "+orderCartModel.product_measure_in);

                            String id=orderCartModel.id;
                            String quan=orderCartModel.order_quantity;
                            String ocid=orderCartModel.ocid;
                            String mc=orderCartModel.measure_category;
                            String pmi=orderCartModel.product_measure_in;
                            String price=orderCartModel.selling_price;
                            Double total=0.0,quantity=0.0,pkr=0.0,pmis=0.0;

                            if (mc.equalsIgnoreCase("Kilogram"))
                            {
                                System.out.println("quan"+quan);System.out.println("price"+price);System.out.println("pmi"+pmi);
                                quantity=Double.parseDouble(quan);
                                pkr=Double.parseDouble(price);
                                pmis=Double.parseDouble(pmi);
                                total=quantity*pkr*pmis;
                                // Toast.makeText(ShoppingRFQActivity.this,)
                                // total=Integer.parseInt(quan)*Integer.parseInt(price)*Integer.parseInt(pmi);
                            }
                            else if (mc.equalsIgnoreCase("Piece"))
                            {
                                quantity=Double.parseDouble(quan);
                                pkr=Double.parseDouble(price);

                                total=quantity*pkr;

                                System.out.println(total+"total");
                                // Toast.makeText(ShoppingRFQActivity.this,)
                                // total=Integer.parseInt(quan)*Integer.parseInt(price)*Integer.parseInt(pmi);
                            }
                            else if (mc.equalsIgnoreCase("Set"))
                            {
                                quantity=Double.parseDouble(quan);
                                pkr=Double.parseDouble(price);

                                total=quantity*pkr;

                                System.out.println(total+"total");
                                // Toast.makeText(ShoppingRFQActivity.this,)
                                // total=Integer.parseInt(quan)*Integer.parseInt(price)*Integer.parseInt(pmi);
                            }
                            else {
                                total=quantity*pkr;
                            }


                            calculateorder("minus","no",orderCartModel);



                        }

                    }
                };
                shoppingbasket.setAdapter(mAdapter);
                shoppingbasket.setLayoutManager( new LinearLayoutManager(getActivity().getApplicationContext()));


            } catch (Exception e) {
            }
        }

    }



    //
    public void ImageUploadToServerFunction(){



        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String>
        {

            ProgressDialog progressDialog;


            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(getActivity(),"\"RFQ Request is sending","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);
                progressDialog.dismiss();



                Toast.makeText(getActivity().getApplicationContext(),string1,Toast.LENGTH_LONG).show();

                if(string1.equalsIgnoreCase("RFQ placed"))
                {
                    getActivity().finish();
                    startActivity(getActivity().getIntent());

                }



            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                //data="+json_string+"&id="+ordernum_1+"&pid="+userid+"&sess="+session
                HashMapParams.put(SESSION, session);
                HashMapParams.put(DATA, json_string);
                HashMapParams.put(ID, ordernum_1);
                HashMapParams.put(PID, userid);
                HashMapParams.put("rfq",radn);


                System.out.println(session);
                System.out.println(json_string);
                System.out.println(ordernum_1);
                System.out.println(userid);
                System.out.println(radn);
                //BA

                String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{
        boolean check = true;
        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

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






    private class AsyncFetch_add extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(getActivity().getApplicationContext());
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


            System.out.println("Return Data is : "+result);
    /*        int as=Integer.parseInt(ordernum)+1;

            String asd=String.valueOf(as);

            *//* Toast.makeText(getActivity().getApplicationContext(),result,Toast.LENGTH_LONG).show();*//*
            Intent intent=new Intent(getActivity().getApplicationContext(),OrderViewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("session",session);
            intent.putExtra("userid",userid);
            intent.putExtra("orderid",asd);
            intent.putExtra("total",String.valueOf(ordertotalvalue));
            intent.putExtra("name",name);
            intent.putExtra("path",profilepic);
            startActivity(intent);
            finish();*/

        }

    }



    private class AsyncFetch_check extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;


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


            // Toast.makeText(getActivity().getApplicationContext(),"This is"+ordernum,Toast.LENGTH_LONG).show();

        }

    }


    private class AsyncFetch_Edi extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(getActivity().getApplicationContext());
        HttpURLConnection conn;
        URL url = null;
        String total, quantity, productid;
        OrderCartModel orderCartModel;
        AsyncFetch_Edi(String total,String quantity,String productid)
        {
           this.total=total;
           this.quantity=quantity;
           this.productid=productid;
        }


        @Override
        protected String doInBackground(String... params) {
            try
            {



                url = new URL("https://sellerportal.perfectmandi.com/edit_order_item.php?id="+productid+"&quan="+quantity+"&tpric="+total);
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


                getActivity().finish();
                startActivity(getActivity().getIntent());



            }


        }

    }

    private class AsyncFetch_Del extends AsyncTask<String, String, String>
    {
        String id;
        public AsyncFetch_Del(String id)
        {
            this.id=id;
        }
        HttpURLConnection conn;
        URL url = null;



        @Override
        protected String doInBackground(String... params) {
            try
            {

                url = new URL("https://sellerportal.perfectmandi.com/delete_order_item.php?id="+id);
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

            if ("".equalsIgnoreCase(result))
            {

            }
            else if ("Records were deleted successfully.".equalsIgnoreCase(result))
            {
                alertDialogs.dismiss();


                data.clear();
       getActivity().finish();
       getActivity().startActivity(getActivity().getIntent());

            }


        }

    }

}