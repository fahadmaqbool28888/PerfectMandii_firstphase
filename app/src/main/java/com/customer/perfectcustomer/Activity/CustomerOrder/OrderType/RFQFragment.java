package com.customer.perfectcustomer.Activity.CustomerOrder.OrderType;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.customer.perfectcustomer.Adapter.AdapterProductAddtoRFQ;
import com.customer.perfectcustomer.Model.Customer.UserModel;
import com.customer.perfectcustomer.LocalDB.DatabaseClass;
import com.customer.perfectcustomer.MainActivity_OP;
import com.customer.perfectcustomer.Model.OrderCartModel;
import com.customer.perfectcustomer.Model.RFQMODEL.rfq_model;
import com.customer.perfectcustomer.Activity.Home.Order.Place.OrderViewActivity;
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

import javax.net.ssl.HttpsURLConnection;


public class RFQFragment extends Fragment
{
    LinearLayout container;
    TextView textViews,qpro1;
    List<OrderCartModel> orderQueue;
    boolean isallchecked=false;
    int sizeoflist,sumoftotal,sizeconstant;
    int totalvalue=0;
    public String ServerUploadPath="https://sellerportal.perfectmandi.com/addmultipleItem_2.php";

    ArrayList<Integer> total_value;
    int update_quantity;





    String ocid,product_id;

    String json_string;


    RecyclerView shoppingbasket;
    String session,userid;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private AdapterProductAddtoRFQ mAdapter;

    TextView textView,totalbill,cts;
    CardView checkoutforshopping;
    ImageView backsc;


    String ordernum;
    String profilepic;

    String nextorderNumber;
    CheckBox sa;
    String ordernum_1;
    String name;
    List<OrderCartModel> data;
    List<OrderCartModel> datas;

    TextView as;
    int sq;
    int totalprice;




    List<UserModel> userModelList;
    DatabaseClass sqLiteHelper;
    void get_Data_1()
    {
        sqLiteHelper=new DatabaseClass(getActivity().getApplicationContext());


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
    int sum;

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
                    int val=orderCartModel.val.intValue();
                    sumoftotal=sumoftotal+val;
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
                    int val=CartModel.val.intValue();
                    sumoftotal=sumoftotal+val;
                    calculate_Panel(sizeoflist,sumoftotal);
                }

            }
            else
            {
                if(orderque(operation,CartModel))
                {
                    int val=CartModel.val.intValue();
                    sumoftotal=sumoftotal-val;
                    sizeoflist = sizeoflist - 1;

                    calculate_Panel(sizeoflist, sumoftotal);
                }
            }
        }

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

    }
    int val=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_r_f_q, container, false);

        datas=new ArrayList<>();

        orderQueue=new ArrayList<>();
        //WorkManager

        intializewidget(view);


        get_Data_1();
        new AsyncFetch_check().execute();
        new AsyncFetch().execute();

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



                    for (int i=0;i<data.size();i++)
                    {
                        OrderCartModel order=orderQueue.get(i);

                        System.out.println(String.valueOf(order.val));
                    }

                    calculateorder("add","yes",null);


                }
                else
                {

                    mAdapter.unselectAllItems();

                    calculateorder("minus","yes",null);


                }

                for (int i=0;i<data.size();i++)
                {
                    OrderCartModel order=data.get(i);

                    System.out.println(order.id+" "+order.ocid+" "+order.order_quantity);

                }

                System.out.println(String.valueOf(orderQueue.size()));
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


                  //  System.out.println(string);

                    if ("{\"upload_fishes\":]}".equalsIgnoreCase(string))
                    {
                        Toast.makeText(getActivity().getApplicationContext(), "Please Add item", Toast.LENGTH_LONG).show();
                    }
                    else
                    {


                   //    System.out.println(string);
                        ImageUploadToServerFunction();
                         orderQueue.clear();
                    }
                }



                //  checklist();



            }

        });





/*
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("rfq-message"));*/
        return view;
    }

    String JsonString() {

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


                OrderCartModel orderCartModel=orderQueue.get(i);
                //  obj_new.put("orderid",String.valueOf(v2));

               // System.out.println("Please this way "+orderCartModel.id+" "+orderCartModel.ocid+" "+orderCartModel.order_quantity);
                obj_new.put("ocid",orderCartModel.ocid);
                obj_new.put("productid",orderCartModel.id);
                // obj_new.put("productprice",OproductPrice.get(i));
                obj_new.put("quan",orderCartModel.order_quantity);
                //  obj_new.put("vendor",cProvider.get(i));
                // obj_new.put("customer",userid);
                //  obj_new.put("date",currentDate);

                json_string = json_string + obj_new.toString() + ",";


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


    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;



        @Override
        protected String doInBackground(String... params) {
            try
            {
                // url = new URL("https://sellerportal.perfectmandi.com/cartitem_2.php?id=03115122587");

                 url = new URL("https://sellerportal.perfectmandi.com/cartitem_2.php?id="+userid);
            }
            catch (MalformedURLException e)
            {
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
            System.out.println("Result is "+result);
            if (result.equalsIgnoreCase(""))
            {
                container.setVisibility(View.GONE);
                textViews.setVisibility(View.VISIBLE);
            ArrayList<rfq_model>  rfq_modelList=sqLiteHelper.getRFQlist();
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
                cts.setText("Continue for Shopping");
            }
            else {
                container.setVisibility(View.VISIBLE);
                textViews.setVisibility(View.GONE);
                qpro1.setVisibility(View.GONE);
            }

            total_value=new ArrayList<>();
            pdLoading.dismiss();
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
                    fishData.quantity=json_data.getString("quantity");
                    fishData.order_quantity=json_data.getString("cq");
                    fishData.selling_price=json_data.getString("cp");
                    fishData.st_price=json_data.getString("total_price");

                    totalvalue=Integer.parseInt(fishData.st_price);

                    fishData.productDescription=json_data.getString("Product_Description");
                    fishData.ocid=json_data.getString("ocid");
                    fishData.stock=json_data.getString("stock");
                    fishData.MOQ=json_data.getString("MOQ");
                    fishData.replaceData=json_data.getJSONArray("replace");
                    fishData.measure_category=json_data.getString("measure_category");
                    fishData.product_measure_in=json_data.getString("product_measure_in");
                    fishData.oos=json_data.getString("oos");
                    fishData.rp=json_data.getString("rp");
                    fishData.setSelected(false);

                    String oos=json_data.getString("oos");

                    String mc=json_data.getString("measure_category");
                    String quan=json_data.getString("cq");
                    String price=json_data.getString("cp");
                    String pmi=json_data.getString("product_measure_in");
                    Double total=0.0,quantity=0.0,pkr=0.0,pmis=0.0;

                    if (mc.equalsIgnoreCase("Kilogram"))
                    {
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
                    fishData.val=total;
                    if (oos.equalsIgnoreCase("yes"))
                    {

                    }
                    else {
                        total_value.add(Integer.parseInt(fishData.st_price));
                        data.add(fishData);

                    }

                }

               /* getCount=data.size();
                size=data.size();*/

                sizeconstant=data.size();
                mAdapter = new AdapterProductAddtoRFQ(getActivity().getApplicationContext(), data, "single") {
                    @Override
                    public void flag(String flag)
                    {


                        System.out.println(String.valueOf(data.size()));



                    }

                    @Override
                    public void passitem(String flag, OrderCartModel orderCartModel) {




                        if (flag.equalsIgnoreCase("include"))
                        {

                            calculateorder("add","no",orderCartModel);





                        }
                        else
                        {

                            calculateorder("minus","no",orderCartModel);



                        }




                    }


                    @Override
                    public void removeItems(int pos, OrderCartModel orderCartModel) {
                        sa.setChecked(false);

                        calculateorder("minus","no",orderCartModel);
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

                progressDialog = ProgressDialog.show(getActivity(),"\"Order Placing","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);



                progressDialog.dismiss();



                System.out.println("Data Value is "+string1);
                //Toast.makeText(ShoppingRFQActivity.this,"End",Toast.LENGTH_LONG).show();

                if(string1.equalsIgnoreCase("order placed"))
                {
                    //cts.setText("Continue for Shopping");
                    //new AsyncFetch().execute();
                    Intent intent=new Intent(getActivity(), OrderViewActivity.class);
                    int as=Integer.parseInt(ordernum)+1;

                    String asd=String.valueOf(as);



                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("session",session);
                    intent.putExtra("userid",userid);
                    intent.putExtra("orderid",asd);
                    intent.putExtra("total",String.valueOf(sumoftotal));
                    intent.putExtra("name",name);
                    intent.putExtra("path",profilepic);
                    startActivity(intent);
                    getActivity().finish();
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
                HashMapParams.put("noid",ordernum_1);


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