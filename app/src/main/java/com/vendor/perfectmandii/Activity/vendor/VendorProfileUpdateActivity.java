package com.vendor.perfectmandii.Activity.vendor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.vendor.perfectmandii.Adapter.AdapterProductPrime;
import com.vendor.perfectmandii.DBHelper;
import com.vendor.perfectmandii.Dashboard.OPActivity;
import com.vendor.perfectmandii.LoginActivity;
import com.vendor.perfectmandii.Model.CategoyByProductModel;
import com.vendor.perfectmandii.Model.userVendor;
import com.vendor.perfectmandii.ProfileFragment.AttachDFragment;
import com.vendor.perfectmandii.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
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

public class VendorProfileUpdateActivity extends AppCompatActivity
{
    String ServerUploadPath = "https://sellerportal.perfectmandi.com/update_record_vendor.php";
    EditText store_description,store_address,return_address,store_contact;
    CardView update_profile;
    ArrayList<userVendor> pointModelsArrayList;
    DBHelper dbHandler;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    ImageView viewimage,backbutton;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    String vendor_id,storeid;

    CheckBox same_Asabove;

    String cphoto,description,billingAddress,ShippingAddress,contact_detail_1;

    //
    String Profile_Pic = "profile_pic";
    String Cnic_Front="cnic_front";
    String Cnic_Back="cnic_back",Utility_Bill="utility_bill",contact_person="contact_person",Mobilenumber="mobile",Mobilenumber1="mobile1";
    String Billingaddress = "Billing";
    String Shippingaddress = "Shipping";
    String store_name = "Store_name";
    String store_desc = "Store_desc";
    String store_id = "store_id";
    String store_abbr = "store_abbr";
    String store_session = "store_session";
    void init()
    {
        pointModelsArrayList = dbHandler.readVendor();



        if (pointModelsArrayList.size()>0)
        {
            userVendor pointModel=pointModelsArrayList.get(0);

            vendor_id=pointModel.userid;

            storeid=pointModel.storeid;

        }
        else
        {
      //  return null;

        }
    }

    void initialize_widget()
    {
        backbutton=findViewById(R.id.backbutton);
        same_Asabove=findViewById(R.id.same_Asabove);
        store_description=findViewById(R.id.store_Update_desc);
        store_address=findViewById(R.id.update_warehouse_address);
        return_address=findViewById(R.id.update_shipping_address);
        store_contact=findViewById(R.id.update_contact_sphone);
        viewimage=findViewById(R.id.update_viewimage);
        update_profile=findViewById(R.id.update_profile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_profile_update);

        initialize_widget();
        dbHandler=new DBHelper(VendorProfileUpdateActivity.this);

        init();
        // vendor_id=init();
        new AsyncFetch().execute();


        viewimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkAndRequestPermissions(VendorProfileUpdateActivity.this)) {
                    chooseImage(VendorProfileUpdateActivity.this);
                } else {
                    chooseImage(VendorProfileUpdateActivity.this);
                }

            }
        });

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_Data();
                ImageUploadToServerFunction();


            }
        });


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        same_Asabove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (same_Asabove.isChecked()) {
                    return_address.setText(store_address.getText().toString());
                } else {
                    return_address.setText("");
                }
            }
        });


       // Toast.makeText(VendorProfileUpdateActivity.this,vendor_id,Toast.LENGTH_LONG).show();
    }

    void get_Data()
    {
        contact_detail_1=store_contact.getText().toString();
        cphoto=convertPhoto(bitmap);
        description=store_description.getText().toString();
        billingAddress=store_address.getText().toString();
        ShippingAddress=return_address.getText().toString();
    }








    private class AsyncFetch extends AsyncTask<String, String, String> {

        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;
        ProgressDialog progressDialog=new ProgressDialog(VendorProfileUpdateActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog.setTitle("Perfect Mandi");
            progressDialog.setIcon(R.drawable.optimizedlogo);
            // progressDialog.setMessage("Data is Loading...");
            progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params) {
            try {


                url = new URL("https://sellerportal.perfectmandi.com/get_storeData.php?id="+vendor_id);

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

            progressDialog.dismiss();


            if(result.equalsIgnoreCase("unsuccessful"))
            {



            }
            else
            {

                try {


                    JSONArray jArray = new JSONArray(result);


                    // Extract data from json and store into ArrayList as class objects
                    for (int i = 0; i < jArray.length(); i++) {


                        JSONObject json_data = jArray.getJSONObject(i);
                        store_description.setText(json_data.getString("description"));

                        store_address.setText(json_data.getString("billingAddress"));
                        return_address.setText(json_data.getString("ShippingAddress"));
                        store_contact.setText(json_data.getString("contact_detail_1"));

                      String path=json_data.getString("image_url");


                      if (path.contains("https://"))
                      {
                          Picasso.get()
                                  .load(path)
                                  .into(viewimage);
                      }
                      else
                      {
                          String protoc="https://sellerportal.perfectmandi.com/"+path;
                          Picasso.get()
                                  .load(protoc)
                                  .into(viewimage);
                      }










                    }
                }
                catch (Exception exception)
                {


                }


            }

        }

    }


    private void chooseImage(Context context) {
        //final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array
        final CharSequence[] optionsMenu = {"Take Photo","Choose from Gallery", "Exit"};
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
                } else if (optionsMenu[i].equals("Choose from Gallery")) {
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                } else if (optionsMenu[i].equals("Exit")) {
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
                if (ContextCompat.checkSelfPermission(VendorProfileUpdateActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(VendorProfileUpdateActivity.this,
                                    "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(VendorProfileUpdateActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(VendorProfileUpdateActivity.this,
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else if (ContextCompat.checkSelfPermission(VendorProfileUpdateActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(VendorProfileUpdateActivity.this,
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(VendorProfileUpdateActivity.this);
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
                        viewimage.setImageBitmap(bitmap);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                       /* Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                bitmap=BitmapFactory.decodeFile(picturePath);
                                viewimage.setImageBitmap(bitmap);
                                cursor.close();
                            }
                        }*/
                        Uri selectedImage = data.getData();
                        String   imageEncoded = getRealPathFromURI(VendorProfileUpdateActivity.this, selectedImage);
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
    private String getFilename(Context context) {
        File mediaStorageDir = new File(context.getExternalFilesDir(""), "patient_data");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }

        String mImageName = "IMG_" + String.valueOf(System.currentTimeMillis()) + ".png";
        return mediaStorageDir.getAbsolutePath() + "/" + mImageName;

    }
    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    String convertPhoto(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
       String storecphoto = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return storecphoto;
    }


    public void ImageUploadToServerFunction(){



        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(VendorProfileUpdateActivity.this,"\"Documents are uplodaing","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);


                progressDialog.dismiss();

                if (string1.equalsIgnoreCase("Record Updated"))
                {

                    Intent intent=new Intent(VendorProfileUpdateActivity.this,OPActivity.class);
                    startActivity(intent);



                }




            }

            @Override
            protected String doInBackground(Void... params) {

              ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();



/*    contact_detail_1=store_contact.getText().toString();
        cphoto=convertPhoto(bitmap);
        description=store_description.getText().toString();
        billingAddress=store_address.getText().toString();
        ShippingAddress=return_address.getText().toString();*/



                HashMapParams.put(Profile_Pic, cphoto);
                HashMapParams.put(store_desc,description);
                HashMapParams.put(Mobilenumber1, contact_detail_1);
                HashMapParams.put(store_id,storeid);
                HashMapParams.put(Mobilenumber, vendor_id); //mobile Number
                HashMapParams.put(Billingaddress, billingAddress); //BA
                HashMapParams.put(Shippingaddress, ShippingAddress); //SA


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