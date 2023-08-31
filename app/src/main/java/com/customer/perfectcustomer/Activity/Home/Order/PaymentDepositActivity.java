package com.customer.perfectcustomer.Activity.Home.Order;


import androidx.annotation.RequiresApi;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.customer.perfectcustomer.Model.Customer.UserModel;
import com.customer.perfectcustomer.LocalDB.DatabaseClass;

import com.customer.perfectcustomer.MainActivity_OP;
import com.customer.perfectcustomer.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
            /* bitmap=getResizedBitmap(rbitmap,10);*/
                bitmap=rbitmap;
                if(bitmap==null)
                {

                }
                else
                {
                    paslip.setImageBitmap(bitmap);
                    uploadsignal.setVisibility(View.VISIBLE);
                    paymentdeposit.setVisibility(View.GONE);

                }



              //  imageView.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public void ImageUploadToServerFunction(){

        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStreamObject);

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
