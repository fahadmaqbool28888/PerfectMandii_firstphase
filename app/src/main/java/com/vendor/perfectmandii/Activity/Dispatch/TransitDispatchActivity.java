package com.vendor.perfectmandii.Activity.Dispatch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vendor.perfectmandii.Adapter.VendorDispatch.dispatchVendor;
import com.vendor.perfectmandii.DBHelper;
import com.vendor.perfectmandii.Dashboard.OPActivity;
import com.vendor.perfectmandii.Model.userVendor;
import com.vendor.perfectmandii.Model.vendorDispatch.vendorDispatchModel;
import com.vendor.perfectmandii.ProfileFragment.AttachDFragment;
import com.vendor.perfectmandii.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class TransitDispatchActivity extends AppCompatActivity
{
    public String ServerUploadPath="https://sellerportal.perfectmandi.com/addTO_mobile.php";
EditText total,pi;
TextView Submit;
String totalbill,purchaseinvoice,imagestring,createdid;
Bitmap bitmap;

//
    ArrayList<userVendor> pointModelsArrayList;
    DBHelper dbHandler;
    String storeid;
ImageView viewimage;
    String shopping_invoice = "shopping_invoice";
    String total_bill = "total_bill";
    String image_string = "dispatch_note";
    String Store_id = "store_id";
    String createdby = "created_by";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit_dispatch);
        init();
        dbHandler = new DBHelper(TransitDispatchActivity.this);
        getValue();
        Toast.makeText(TransitDispatchActivity.this, "This is "+createdid, Toast.LENGTH_SHORT).show();
        Intent intent=getIntent();
        purchaseinvoice=intent.getStringExtra("pi");
        pi.setText(purchaseinvoice);

        new AsyncFetch_Add().execute();
        
        
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData();
            }
        });

        viewimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions(TransitDispatchActivity.this)) {
                    chooseImage(TransitDispatchActivity.this);
                } else {
                    chooseImage(TransitDispatchActivity.this);
                }
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUploadToServerFunction();
            }
        });
    }
    void getValue()
    {
        pointModelsArrayList = dbHandler.readVendor();



        if (pointModelsArrayList.size()>0)
        {
            userVendor pointModel=pointModelsArrayList.get(0);
            storeid=pointModel.storeid;
            createdid=pointModel.userid;
            Toast.makeText(TransitDispatchActivity.this, createdid, Toast.LENGTH_SHORT).show();
        }
    }

    void init()
    {
        Submit=findViewById(R.id.Submit);
        pi=findViewById(R.id.pi_transit);
        total=findViewById(R.id.total_transitbill);
        viewimage=findViewById(R.id.viewimage_dV);
    }
    void GetData()
    {

        if(imagestring!=null&&totalbill!=null&&purchaseinvoice!=null)
        {
            Toast.makeText(TransitDispatchActivity.this,"Good to go",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(TransitDispatchActivity.this,"No Way",Toast.LENGTH_LONG).show();
        }
    }

    private class AsyncFetch_Add extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;
        HttpURLConnection conn;
        ProgressDialog progressDialog;
        URL url = null;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog=new ProgressDialog(TransitDispatchActivity.this);
            progressDialog.setMessage("\tLoading...");
            progressDialog.setTitle("PerfectMandi");
            progressDialog.setIcon(R.drawable.optimizedlogo);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                String liveurl="https://sellerportal.perfectmandi.com/put_DispatchDetail.php?id="+purchaseinvoice;
                url = new URL(liveurl);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try
            {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
                return e1.toString();
            }

            try {

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
                    return (result.toString());
                }
                else
                {
                    return ("unsuccessful"+String.valueOf(response_code));
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

            progressDialog.dismiss();

          //  List<vendorDispatchModel> data=new ArrayList<>();


            try {



                JSONArray jArray = new JSONArray(result);



                for(int i=0;i<jArray.length();i++){
                    //JSONArray jArray = new JSONArray(result);
                    JSONObject json_data = jArray.getJSONObject(i);

                    total.setText(json_data.getString("totalbill"));
                    totalbill=json_data.getString("totalbill");
                }

                // Setup and Handover data to recyclerview

            } catch (Exception e)
            {

            }


        }
    }


    //
    private void chooseImage(Context context) {
        //final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array
        final CharSequence[] optionsMenu = {"Take Photo", "Exit"};
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (optionsMenu[i].equals("Take Photo")) {
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }/* else if (optionsMenu[i].equals("Choose from Gallery")) {
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                }*/ else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    // function to check permission
    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    // Handled permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(TransitDispatchActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(TransitDispatchActivity.this,
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(TransitDispatchActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(TransitDispatchActivity.this,
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                }
                else if (ContextCompat.checkSelfPermission(TransitDispatchActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(TransitDispatchActivity.this,
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                }else {
                    chooseImage(TransitDispatchActivity.this);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                        imagestring=convert_Image(bitmap);
                        viewimage.setImageBitmap(bitmap);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String   imageEncoded = getRealPathFromURI(TransitDispatchActivity.this, selectedImage);
                        bitmap = BitmapFactory.decodeFile(imageEncoded);

                        viewimage.setImageBitmap(bitmap);
                        Glide.with(this).load(bitmap).into(viewimage);
                        //

                    }
                    break;
            }
        }
    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
        OutputStream out;
        File file = new File(getFilename(context));

        try {
            if (file.createNewFile()) {
                InputStream iStream = context != null ? context.getContentResolver().openInputStream(contentUri) : context.getContentResolver().openInputStream(contentUri);
                byte[] inputData = getBytes(iStream);
                out = new FileOutputStream(file);
                out.write(inputData);
                out.close();
                return file.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getBytes(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1)
        {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private String getFilename(Context context)
    {
        File mediaStorageDir = new File(context.getExternalFilesDir(""), "patient_data");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists())
        {
            mediaStorageDir.mkdirs();
        }

        String mImageName = "IMG_" + String.valueOf(System.currentTimeMillis()) + ".png";
        return mediaStorageDir.getAbsolutePath() + "/" + mImageName;

    }
    String convert_Image(Bitmap bitmap)
    {
        String image = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return image;
    }

    //
    public void ImageUploadToServerFunction(){



        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {

                super.onPreExecute();
               //progressDialog.setIcon(R.drawable.optimizedlogo);
                progressDialog = ProgressDialog.show(TransitDispatchActivity.this,"Documents are uplodaing","Please Wait",false,false);


            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);
                // Toast.makeText(AddStoreActivity.this,string1,Toast.LENGTH_SHORT).show();

                System.out.println(string1);
                progressDialog.dismiss();
                Toast.makeText(TransitDispatchActivity.this,string1,Toast.LENGTH_SHORT).show();

                if (string1.equalsIgnoreCase("Dispatch Note created"))
                {
                    finish();
                }
                else
                {

                }
               // finish();
                /*
                if ("Store Created".equalsIgnoreCase(string1))
                {
                    //  Toast.makeText(getContext(),string1,Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    System.out.println(string1);

                    Intent intent=new Intent(getContext(), OPActivity.class);
                    intent.putExtra("userid",Storeid);
                    intent.putExtra("session",storesession);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();




                }
                else
                {
                    progressDialog.dismiss();
                    System.out.println(string1);
                    Toast.makeText(getContext(),string1,Toast.LENGTH_SHORT).show();
                }*/



            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();







                HashMapParams.put(Store_id, storeid);

                HashMapParams.put(total_bill, totalbill);

                HashMapParams.put(shopping_invoice, purchaseinvoice);
                HashMapParams.put(image_string, imagestring);
                HashMapParams.put(createdby,createdid);
             //SA


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