package com.vendor.perfectmandii.Activity.User.customerprofile.StoreProfile.approvalstage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vendor.perfectmandii.Activity.User.customerprofile.StoreProfile.AddStoreActivity;
import com.vendor.perfectmandii.Adapter.AdapterHome;
import com.vendor.perfectmandii.Adapter.Instruction.PhotoUpload.InstructionsAdapter;
import com.vendor.perfectmandii.LoginActivity;
import com.vendor.perfectmandii.MainActivity;
import com.vendor.perfectmandii.Model.Instruction.instructionmodel;
import com.vendor.perfectmandii.Model.vendor.vendorServiceModel;
import com.vendor.perfectmandii.R;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class approvalActivity extends AppCompatActivity {
    boolean check = true;
    ProgressDialog progressDialog;
    String ServerUploadPath = "https://staginigserver.perfectmandi.com/storeapproval.php";
    String cf_Path = "cf_Path";
    String cb_Path = "cb_Path";
    String ub_Path = "ub_Path";
    String user="userid";
    String user_session="session";
    Bitmap bitmap,bitmap1,bitmap2;

    CardView saverecord;
    RecyclerView recyclerView;

    String userid,session;
    ImageView imageView,imageView1,imageView2;
    private String ConvertImage,ConvertImage1,ConvertImage2;
    InstructionsAdapter adapter;

    //
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);

        Intent intent=getIntent();
        userid=intent.getStringExtra("userid");
        session=intent.getStringExtra("session");
        initialWidget();
        new AsyncFetch().execute();
        imageView.setOnClickListener(new View.OnClickListener()
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
        imageView1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 2);


            }
        });
        imageView2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 3);


            }
        });


        saverecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                validateRecord();
            }
        });



    }


    void validateRecord()
    {
        if (ConvertImage.equalsIgnoreCase("")&&ConvertImage1.equalsIgnoreCase(" ")&&ConvertImage2.equalsIgnoreCase(""))
        {
            Toast.makeText(approvalActivity.this,"Data is null",Toast.LENGTH_SHORT).show();
        }
        else
        {
            ImageUploadToServerFunction();
        }
    }
    private void initialWidget()
    {
        recyclerView=findViewById(R.id.instruction_manual);
        saverecord=findViewById(R.id.saverecord_valid);
        imageView=findViewById(R.id.cnic_front);
        imageView1=findViewById(R.id.cnic_back);
        imageView2=findViewById(R.id.utilitybill);
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
    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                Bitmap returnbitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                bitmap = getResizedBitmap(returnbitmap,150);

                if(bitmap==null)
                {

                }
                else
                {
                    imageView.setImageBitmap(bitmap);
                    ConvertImage=stringimage(bitmap);
                    //uploadsignal.setVisibility(View.VISIBLE);
                }



                //  imageView.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        else
        if (RC == 2 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                Bitmap returnbitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                bitmap1 = getResizedBitmap(returnbitmap,150);

                if(bitmap1==null)
                {

                }
                else
                {
                    imageView1.setImageBitmap(bitmap1);
                    ConvertImage1=stringimage(bitmap1);
                    //uploadsignal.setVisibility(View.VISIBLE);
                }



                //  imageView.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        else if (RC == 3 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                Bitmap returnbitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                bitmap2 = getResizedBitmap(returnbitmap,150);

                if(bitmap==null)
                {

                }
                else
                {
                    imageView2.setImageBitmap(bitmap2);
                    ConvertImage2=stringimage(bitmap2);
                    //uploadsignal.setVisibility(View.VISIBLE);
                }



                //  imageView.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public int getWifiLevel()
    {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int linkSpeed = wifiManager.getConnectionInfo().getRssi();
        int level = WifiManager.calculateSignalLevel(linkSpeed, 5);
        return level;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    String stringimage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);



        return ConvertImage;
    }
    public void ImageUploadToServerFunction(){






        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(approvalActivity.this,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                if ("Details Upload".equalsIgnoreCase(string1))
                {
                    progressDialog.dismiss();
                    Intent intent=new Intent(approvalActivity.this, MainActivity.class);
                    intent.putExtra("userid",userid);
                    intent.putExtra("session",createsession());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();



                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(approvalActivity.this,string1,Toast.LENGTH_SHORT).show();
                }




            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();





                HashMapParams.put(cf_Path, ConvertImage);
                HashMapParams.put(cb_Path, ConvertImage1);
                HashMapParams.put(ub_Path, ConvertImage2);
                HashMapParams.put(user,userid);

                HashMapParams.put(user_session,createsession());




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

    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        ProgressDialog progressDialog=new ProgressDialog(approvalActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                url = new URL("https://staginigserver.perfectmandi.com/merchant_folder/customer_inst.php");
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
        protected void onPostExecute(String result) {



            List<instructionmodel> data=new ArrayList<>();


            try {



                JSONArray jArray = new JSONArray(result);



                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    instructionmodel instruction=new instructionmodel();
                    instruction.id=json_data.getString("id");
                    instruction.text=json_data.getString("instruction_text");




                    data.add(instruction);

                }

                // Setup and Handover data to recyclerview

              /*  mAdapter = new AdapterHome(MainActivity.this, data);
                progressDialog.dismiss();
                mRVFishPrice.setAdapter(mAdapter);
                progressDialog.dismiss();
                mRVFishPrice.setLayoutManager( new GridLayoutManager(MainActivity.this,4));*/

                adapter=new InstructionsAdapter(approvalActivity.this,data);
                progressDialog.dismiss();
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(approvalActivity.this));


            } catch (Exception e)
            {

            }

        }

    }




}