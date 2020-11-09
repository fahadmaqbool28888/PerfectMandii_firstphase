package com.consumer.perfectmandii.Activity;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.consumer.perfectmandii.Customer.UserModel;
import com.consumer.perfectmandii.LocalDB.SQLiteHelper;
import com.consumer.perfectmandii.MainActivity_New;
import com.consumer.perfectmandii.MainActivity_OP;
import com.consumer.perfectmandii.OrderViewActivity;
import com.consumer.perfectmandii.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class PaymentDepositActivity extends AppCompatActivity
{
    String selected_ff;
    Bitmap bitmap;

    boolean check = true;

    Button SelectImageGallery, UploadImageServer;

    ImageView imageView;

    EditText invoioce_number,amountdeposit,getdateandtime;

    ProgressDialog progressDialog ;

    String GetInvoiceNumber,Getuserid,GetDepositamount;

    String ff="ff";
    String ImageName = "image_name" ;

    String ImagePath = "image_path" ;

    String user_id="user_id";
    String deposit_amount="deposit_amount";

    String ServerUploadPath ="https://sellerportal.perfectmandi.com/uploadpayslip_1.php" ;


    ImageView uploadsignal,back_Q;



    TextView paymentdeposit,submitslip,paylater;
    String f,userid,orderid,packaging,subtotal,grandtotal,discount,invoice,session,name,pic;
    //
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    SQLiteHelper sqLiteHelper;
    List<UserModel> userModelList;


    String select_province;
    String city;
    List<String> province;

    Spinner spinner;
    ConstraintLayout step2,step3;
    TextView step1,cts;
    CardView step_1_1,confirm_d;

    void intialize_widget(View dialog)
    {
        spinner=dialog.findViewById(R.id.Freigh);
        step_1_1=dialog.findViewById(R.id.step1);
        //step1=findViewById(R.id.step1);
        step2=dialog.findViewById(R.id.maincontainer);
        step3=dialog.findViewById(R.id.maincontainer_form);
        confirm_d=dialog.findViewById(R.id.confirm_d);
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
        sqLiteHelper=new SQLiteHelper(PaymentDepositActivity.this);
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







    }
    public  class AsyncFetch_1 extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        ProgressDialog progressDialog=new ProgressDialog(PaymentDepositActivity.this);
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




            Toast.makeText(PaymentDepositActivity.this,result,Toast.LENGTH_LONG).show();






            if (result!=null)
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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(PaymentDepositActivity.this, android.R.layout.simple_spinner_item, province);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    String text = spinner.getSelectedItem().toString();


                    if (text.equalsIgnoreCase("Select Freight Forwarder")||text.equalsIgnoreCase("Others"))
                    {
                        step3.setVisibility(View.VISIBLE);
                        step2.setVisibility(View.GONE);
                    }
                    else
                    {
                        selected_ff=text;
                        f=text;
                    }

                }
                public void onNothingSelected(AdapterView<?> parent)
                {
                }
            });



        }





    }
    ImageView paslip;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_deposit);
        init_Widget();
        uploadsignal.setVisibility(View.INVISIBLE);

        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        pic=intent.getStringExtra("pic");
        userid=intent.getStringExtra("userid");
        orderid=intent.getStringExtra("orderid");
        packaging=intent.getStringExtra("packaging");
        subtotal=intent.getStringExtra("subtotal");
        grandtotal=intent.getStringExtra("grandtotal");
        discount=intent.getStringExtra("discount");
        invoice=intent.getStringExtra("invoice");

        f=intent.getStringExtra("f");
        session=intent.getStringExtra("session");
        amountdeposit.setText(grandtotal);
        invoioce_number.setText(invoice);




        System.out.println(invoice);
        invoioce_number.setText(orderid);


        if (f.equalsIgnoreCase(""))
        {
            ShowInputDialog();
        }




        paymentdeposit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);


            }
        });

        submitslip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GetInvoiceNumber=invoioce_number.getText().toString();
                Getuserid=userid;
                GetDepositamount=grandtotal;

                System.out.println(Getuserid);
                ImageUploadToServerFunction();
            }
        });

        paylater.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(PaymentDepositActivity.this, MainActivity_OP.class);
                startActivity(intent);
            }
        });





    }


    void init_Widget()
    {
paslip=findViewById(R.id.paslip);
        paylater=findViewById(R.id.paylater);
        amountdeposit=findViewById(R.id.amountdeposit_);
        submitslip=findViewById(R.id.submits);
        imageView=findViewById(R.id.imageview);
        paymentdeposit=findViewById(R.id.attachfile);
        invoioce_number=findViewById(R.id.saveinvoice);
        uploadsignal=findViewById(R.id.uploadsignal);


    }
    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

             Bitmap    rbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
             bitmap=getResizedBitmap(rbitmap,150);
                if(bitmap==null)
                {

                }
                else
                {
                    paslip.setImageBitmap(bitmap);
                    uploadsignal.setVisibility(View.VISIBLE);
                }



              //  imageView.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    String  createsession()
    {
        Calendar cal = Calendar.getInstance();
        String cs=cal.getTime().toString();
        String  input = cs;
        input = input.replace(" ", "");
        String input1 = input.replace("+", "");
        String input2 = input1.replace(":", "");
        return input2;
    }
    public void ImageUploadToServerFunction(){

        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(PaymentDepositActivity.this,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);
                progressDialog.dismiss();
                Toast.makeText(PaymentDepositActivity.this,string1,Toast.LENGTH_SHORT).show();


                if ("Bank slip uploaded successfully".equalsIgnoreCase(string1))
                {
                    Intent intent=new Intent(PaymentDepositActivity.this, MainActivity_OP.class);


                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }


            }


            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ImageName, GetInvoiceNumber);

                HashMapParams.put(ImagePath, ConvertImage);
                HashMapParams.put(deposit_amount, GetDepositamount);
                HashMapParams.put(user_id, Getuserid);
                HashMapParams.put(ff,f);

                String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {

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

                        new OutputStreamWriter(OutPutStream,StandardCharsets.UTF_8));

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
