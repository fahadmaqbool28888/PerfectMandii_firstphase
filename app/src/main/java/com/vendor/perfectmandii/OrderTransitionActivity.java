package com.vendor.perfectmandii;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vendor.perfectmandii.Adapter.profile.order.IntrasnitOrderAdapter;
import com.vendor.perfectmandii.Dashboard.OPActivity;
import com.vendor.perfectmandii.Model.userVendor;
import com.vendor.perfectmandii.profile_Updates.ProductInformationActivity;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class OrderTransitionActivity extends AppCompatActivity
{
    String ServerUploadPath = "https://sellerportal.perfectmandi.com/addTO_mobile.php";
    private static final int PERMISSION_REQUEST_CODE = 101;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    ImageView viewimage,product_pic_full;
    CardView submit_bill;
    String bill_no,bill_value,bill_suggested;
    EditText invoice_bill_no,invoice_bill_value,invoice_bill_suggested;
    String userid,pi,bill;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Bitmap bitmap;
    ConstraintLayout constraintLayout;
    String storecphoto,photostring;

    public String username;
    public String storename;
    public String storeid;
    public String storepath;

              String PI="shopping_invoice";
              String UserID="created_by";
              String StoreId="store_id";
              String totalbill="total_bill";

    String ImagePath="dispatch_note";
    ArrayList<userVendor> pointModelsArrayList;
    DBHelper dbHandler;

    void init()
    {
        dbHandler=new DBHelper(OrderTransitionActivity.this);
        pointModelsArrayList = dbHandler.readVendor();



        if (pointModelsArrayList.size()>0)
        {
            userVendor pointModel=pointModelsArrayList.get(0);


            userid=pointModel.userid;
            username=pointModel.username;
            storename=pointModel.storename;
            storeid=pointModel.storeid;
            storepath=pointModel.storepath;




        }
        else
        {


        }

      /*  userVendor pointModel=pointModelArrayList.get(0);

        tokenValue.setText(pointModel.points);*/
//


    }

    String convertImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        storecphoto = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return storecphoto;
    }
    void get_Data()
    {

        bill_no=invoice_bill_no.getText().toString();
        bill_value=invoice_bill_value.getText().toString();
        bill_suggested=invoice_bill_suggested.getText().toString();
        if (bitmap==null)
        {
            photostring=null;

        }
        else
        {
            photostring=convertImage(bitmap);
        }

    }

    private void checkEmpty()
    {
       if (photostring==null)
       {
           Toast.makeText(OrderTransitionActivity.this,"Please add picture",Toast.LENGTH_LONG).show();
       }
       else
       {

           ImageUploadToServerFunction();

       }

    }

    String total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_transition);

        intiazlize_widget();
        init();

        Intent intent=getIntent();
        pi=intent.getStringExtra("pi");
        total=intent.getStringExtra("total");

        bill=total;

        invoice_bill_no.setText(pi);
        invoice_bill_value.setText(total);
        invoice_bill_suggested.setText(total);


        viewimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions(OrderTransitionActivity.this)) {
                    chooseImage(OrderTransitionActivity.this);
                } else {
                    chooseImage(OrderTransitionActivity.this);
                }
            }
        });
        submit_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                get_Data();
                checkEmpty();

                    if (bill_no!=null&&bill_value!=null&&bill_suggested!=null&&photostring!=null)
                    {

                    }
                    else
                    {
                        Toast.makeText(OrderTransitionActivity.this,"Please add value",Toast.LENGTH_LONG).show();

                    }
                }


        });
    }

    private void chooseImage(Context context) {
        //  final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array
        final CharSequence[] optionsMenu = {"Take Photo", "Exit"}; // create a menuOption Array

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
                }  else if (optionsMenu[i].equals("Exit")) {
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(OrderTransitionActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(OrderTransitionActivity.this,
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(OrderTransitionActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(OrderTransitionActivity.this,
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else if (ContextCompat.checkSelfPermission(OrderTransitionActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(OrderTransitionActivity.this,
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(OrderTransitionActivity.this);
                }
                break;
        }
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                        // photostring=convertImage(bitmap);
                        // viewimage.setImageBitmap(bitmap);
                        viewimage.setImageBitmap(
                                decodeSampledBitmapFromResource(getResources(), R.id.product_pic, 100, 100));

                        Glide.with(this).load(bitmap).into(product_pic_full);

                        Glide.with(this).load(bitmap).into(viewimage);
                        viewimage.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                bitmap= BitmapFactory.decodeFile(picturePath);
                                // photostring=convertImage(bitmap);
                                Glide.with(this).load(bitmap).into(viewimage);
                                Glide.with(this).load(bitmap).into(product_pic_full);
                                //  viewimage.setImageBitmap(bitmap);
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    void intiazlize_widget()
    {
        product_pic_full=findViewById(R.id.product_pic_full);
        constraintLayout=findViewById(R.id.icon_c);
        submit_bill=findViewById(R.id.submit_bill);
    invoice_bill_no=findViewById(R.id.invoice_bill_no);
    invoice_bill_value=findViewById(R.id.invoice_bill_value);
    invoice_bill_suggested=findViewById(R.id.invoice_bill_suggested);
        viewimage=findViewById(R.id.product_pic);
    }


    public void ImageUploadToServerFunction(){



        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String>
        {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(OrderTransitionActivity.this,"Product Information is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);
                progressDialog.dismiss();
                System.out.println(string1);

                if (string1.equalsIgnoreCase("Dispatch Note created"))
                {

                    Intent intent = new Intent(OrderTransitionActivity.this, topayFragment.class);

                    overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                 /*   Intent intent = new Intent("custom-message");
                    //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));

                    intent.putExtra("state","refresh");
                    LocalBroadcastManager.getInstance(OrderTransitionActivity.this).sendBroadcast(intent);

                    finish();*/
                }
                else
                {

                }







            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

/*       String PI="shopping_invoice";
              String UserID="created_by";
              String StoreId="store_id";
              String totalbill="total_bill";

    String ImagePath="dispatch_note";*/



                HashMapParams.put(PI,pi);
                HashMapParams.put(UserID,userid); //BA
                HashMapParams.put(ImagePath, photostring); //SA
                HashMapParams.put(StoreId, storeid);
                HashMapParams.put(totalbill, bill);


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