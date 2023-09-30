package com.vendor.perfectmandii.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.vendor.perfectmandii.Activity.Order.fetch.UserOrderActivity;
import com.vendor.perfectmandii.R;

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
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class PaymentDepositActivity extends AppCompatActivity
{

    Bitmap bitmap;

    boolean check = true;

    Button SelectImageGallery, UploadImageServer;

    ImageView imageView;

    EditText invoioce_number,amountdeposit,getdateandtime;

    ProgressDialog progressDialog ;

    String GetInvoiceNumber,Getuserid,GetDepositamount;

    String ImageName = "image_name" ;

    String ImagePath = "image_path" ;

    String user_id="user_id";
    String deposit_amount="deposit_amount";

    String ServerUploadPath ="https://staginigserver.perfectmandi.com/uploadpayslip_1.php" ;


    ImageView uploadsignal,back_Q;



    TextView paymentdeposit,submitslip;
    String userid,orderid,packaging,subtotal,grandtotal,discount,invoice,session;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_deposit);
        init_Widget();
        uploadsignal.setVisibility(View.INVISIBLE);

        Intent intent=getIntent();
        userid=intent.getStringExtra("userid");
        orderid=intent.getStringExtra("orderid");
        packaging=intent.getStringExtra("packaging");
        subtotal=intent.getStringExtra("subtotal");
        grandtotal=intent.getStringExtra("grandtotal");
        discount=intent.getStringExtra("discount");
        invoice=intent.getStringExtra("invoice");

        session=intent.getStringExtra("session");
        amountdeposit.setText(grandtotal);
        invoioce_number.setText(invoice);
        /*   userid=intent.getStringExtra("userid");
        orderid=intent.getStringExtra("orderid");
        packaging=intent.getStringExtra("packaging");
        subtotal=intent.getStringExtra("subtotal");
        grandtotal=intent.getStringExtra("grandtotal");
        discount=intent.getStringExtra("discount");
        invoice=intent.getStringExtra("invoice");*/





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
                ImageUploadToServerFunction();
            }
        });


    }


    void init_Widget()
    {

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

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                if(bitmap==null)
                {

                }
                else
                {
                    uploadsignal.setVisibility(View.VISIBLE);
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

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();
                uploadsignal.setVisibility(View.INVISIBLE);
                bitmap=null;
                invoioce_number.setText("");

                // Printing uploading success message coming from server on android app.
                Toast.makeText(PaymentDepositActivity.this,string1,Toast.LENGTH_LONG).show();

                // Setting image as transparent after done uploading.
                imageView.setImageResource(android.R.color.transparent);

                Intent intent=new Intent(PaymentDepositActivity.this, UserOrderActivity.class);
                intent.putExtra("userid",userid);
                intent.putExtra("session",session);
                startActivity(intent);


            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ImageName, GetInvoiceNumber);

                HashMapParams.put(ImagePath, ConvertImage);
                HashMapParams.put(deposit_amount, GetDepositamount);
                HashMapParams.put(user_id, Getuserid);

                String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

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
}
