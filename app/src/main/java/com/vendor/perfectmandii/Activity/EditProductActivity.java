package com.vendor.perfectmandii.Activity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vendor.perfectmandii.R;
import com.vendor.perfectmandii.profile_Updates.ProductInformationActivity;

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
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class EditProductActivity extends AppCompatActivity {

    EditText product_name,product_description,unit_quantity,unit_moq,unit_price;
    String productname=null,productdescription=null,unitquantity=null,unitmoq=null,unitprice=null;
    ImageView product_pic,cancel_action_pic,upload_pi;
    Bitmap bitmap;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    //
    String ServerUploadPath = "https://sellerportal.perfectmandi.com/upload_product_mb_1.php";


    CardView uploadRecord;

    String _product_name = "product_name";//9
    String product_Desc = "product_Desc";//10
    String product_unit = "product_unit";//11
    String product_price = "product_price";//12
    String product_MOQ = "product_moq";//12
    String image_path = "image_path";//13
    String accountid = "account_id";
    String image_paths;
    ConstraintLayout iconc;

    String flag=null;




    String recieve_name,recieve_desc,recieve_price,recieve_quan,recieve_moq,recieve_img,recieve_id,recieve_pro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Intent intent=getIntent();
        recieve_id=intent.getStringExtra("id");

        recieve_name=intent.getStringExtra("name");
        recieve_desc=intent.getStringExtra("desc");
        recieve_price=intent.getStringExtra("price");
        recieve_quan=intent.getStringExtra("quan");
        recieve_moq=intent.getStringExtra("moq");
        //recieve_img=intent.getStringExtra("img");
        recieve_pro=intent.getStringExtra("pro");

        init_widget();
        init_widget_value();


        product_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                if (checkAndRequestPermissions(EditProductActivity.this)) {
                    selectImage();
                    //chooseImage(ProductInformationActivity.this);
                } else {
                    selectImage();
                    //    chooseImage(ProductInformationActivity.this);
                }

            }
        });



        if (product_pic.getDrawable() == null
        )
        {

            product_pic.setBackgroundResource(R.drawable.uploadicon);
             cancel_action_pic.setVisibility(View.INVISIBLE);
        }
        else
        {
            cancel_action_pic.setVisibility(View.VISIBLE);
        }

        cancel_action_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               upload_pi.setImageResource(0);
               cancel_action_pic.setVisibility(View.INVISIBLE);
               product_pic.setVisibility(View.VISIBLE);
               product_pic.setImageResource(0);
               product_pic.setBackgroundResource(R.drawable.uploadicon);
               flag=null;
            }
        });


        uploadRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                getEmpty();

            }
        });


    }

    private static final int PERMISSION_REQUEST_CODE = 101;
    void init_widget()
    {
        uploadRecord=findViewById(R.id.uploadRecord);
        upload_pi=findViewById(R.id.upload_pi);
        iconc=findViewById(R.id.icon_c);
        product_name=findViewById(R.id.product_name);
        product_description=findViewById(R.id.product_description);
        unit_quantity=findViewById(R.id.unit_quantity);
        unit_moq=findViewById(R.id.unit_moq);
        unit_price=findViewById(R.id.unit_price);
        product_pic=findViewById(R.id.product_pic);
        cancel_action_pic=findViewById(R.id.cancel_action_pic);
    }


    void init_widget_value()
    {
        product_name.setText(recieve_name);
        product_description.setText(recieve_desc);
        unit_quantity.setText(recieve_quan);
        unit_moq.setText(recieve_moq);
        unit_price.setText(recieve_price);



   /*     if (recieve_img.contains("https://sellerportal.perfectmandi.com/"))
        {
            Glide.with(EditProductActivity.this).load(recieve_img).into(upload_pi);

        }
        else {

            recieve_img="https://sellerportal.perfectmandi.com/"+recieve_img;
            Glide.with(EditProductActivity.this).load(recieve_img).into(upload_pi);


        }*/








        //



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
    String convertImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String   storecphoto = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return storecphoto;
    }
    void getData()
    {

        productname=product_name.getText().toString();
        productdescription=product_description.getText().toString();
        unitquantity=unit_quantity.getText().toString();
        unitmoq=unit_moq.getText().toString();
        unitprice=unit_price.getText().toString();


        if (upload_pi.getDrawable() == null)
        {

            image_paths=null;
            ImageUploadToServerFunction();
        }
        else
        {

          Drawable d= upload_pi.getDrawable();
          bitmap = ((BitmapDrawable)d).getBitmap();

          image_paths=convertImage(bitmap);
            ImageUploadToServerFunction();

        }


    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProductActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1)
            {
                bitmap = (Bitmap) data.getExtras().get("data");
                // photostring=convertImage(bitmap);
                // viewimage.setImageBitmap(bitmap);
              /*  product_pic.setImageBitmap(
                        decodeSampledBitmapFromResource(getResources(), R.id.product_pic, 100, 100));
*/
                upload_pi.setImageBitmap(bitmap);
                //product_pic.setImageBitmap(bitmap);
                product_pic.setImageResource(0);
                product_pic.setVisibility(View.INVISIBLE);
                cancel_action_pic.setVisibility(View.VISIBLE);
               // Glide.with(this).load(bitmap).into(product_pic);
                flag="done";

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                bitmap = (BitmapFactory.decodeFile(picturePath));
                upload_pi.setImageBitmap(bitmap);
                //product_pic.setImageBitmap(bitmap);
                product_pic.setImageResource(0);
                product_pic.setVisibility(View.INVISIBLE);
                cancel_action_pic.setVisibility(View.VISIBLE);
                flag="done";
            }
        }
    }





    public void ImageUploadToServerFunction(){



        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String>
        {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(EditProductActivity.this,"Product Information is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);
                // Toast.makeText(AddStoreActivity.this,string1,Toast.LENGTH_SHORT).show();

                System.out.println(string1);

                Toast.makeText(EditProductActivity.this, string1, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();


                if (string1.equalsIgnoreCase("Product Updated"))
                {

                    finish();
                }

             /*   if ("Store Created".equalsIgnoreCase(string1))
                {
                    //  Toast.makeText(getContext(),string1,Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    System.out.println(string1);

                  *//*  Intent intent=new Intent(ProductInformationActivity.this, MainActivity.class);
                    intent.putExtra("userid",storeid);
                    intent.putExtra("session",storesession);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();*//*




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


                if (image_paths==null)
                {
                    HashMapParams.put("id",recieve_id);
                    HashMapParams.put(_product_name,productname); //mobile Number
                    HashMapParams.put(product_Desc, productdescription);
                    HashMapParams.put(product_unit,unitquantity);
                    HashMapParams.put(product_price,unitprice); //BA
                    HashMapParams.put(product_MOQ, unitmoq);//SA
                    HashMapParams.put(accountid,recieve_pro);
                }
                else {
                    HashMapParams.put("id",recieve_id);
                    HashMapParams.put(_product_name,productname); //mobile Number
                    HashMapParams.put(product_Desc, productdescription);
                    HashMapParams.put(product_unit,unitquantity);
                    HashMapParams.put(product_price,unitprice); //BA
                    HashMapParams.put(image_path, image_paths); //SA
                    HashMapParams.put(product_MOQ, unitmoq);//SA
                    HashMapParams.put(accountid,recieve_pro);
                }



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



    void getEmpty()
    {
       if (product_name.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(EditProductActivity.this,"Please enter product name",Toast.LENGTH_LONG).show();
        }
        else if (product_description.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(EditProductActivity.this,"Please enter product description",Toast.LENGTH_LONG).show();
        }
        else if (unit_quantity.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(EditProductActivity.this,"Please Enter Units",Toast.LENGTH_LONG).show();
        }
        else if (unit_price.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(EditProductActivity.this,"Please Enter Price",Toast.LENGTH_LONG).show();
        }
       else if (unit_moq.getText().toString().equalsIgnoreCase(""))
       {
           Toast.makeText(EditProductActivity.this,"Please Enter Price",Toast.LENGTH_LONG).show();
       }
        else if (upload_pi.getDrawable()==null)
        {
            getData();
          //  Toast.makeText(EditProductActivity.this,"Please upload picture",Toast.LENGTH_LONG).show();
        }
        else
        {
            getData();
           // ImageUploadToServerFunction();
        }


    }





}