package com.vendor.perfectmandii.Activity.vendor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.vendor.perfectmandii.Adapter.AdapterHome;
import com.vendor.perfectmandii.Adapter.Vendor.vendoraddcolor;
import com.vendor.perfectmandii.Adapter.Vendor.vendoraddproduct;
import com.vendor.perfectmandii.LoginActivity;

import com.vendor.perfectmandii.MainActivity;
import com.vendor.perfectmandii.Model.vendor.vendorServiceModel;
import com.vendor.perfectmandii.Model.vendor.vendorcoloradd;
import com.vendor.perfectmandii.Model.vendor.vendorproadd;
import com.vendor.perfectmandii.R;
import com.vendor.perfectmandii.shoppingBasket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
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

public class AddProductActivity extends AppCompatActivity
{
    boolean check = true;
    int count, count1;
    String productname, description, category, subcategory, unityquantity, bundlequantity, unitprice, bundleprice;
    CardView addunit, minusunit, addunit_1, minusunit_1;
    String imageStrings;
    ProgressDialog progressDialog;
    CardView addproduct_;
    List<vendorcoloradd> data1;
    List<vendorproadd> data;
    List<Bitmap> bitmapslist;
    List<String> colorlist;
    List<String> imageString;
    AppCompatSpinner spinner, subspinner, colorspinner;
    RecyclerView addpic, color_add;
    ArrayList<String> colorshade;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    String ImagePath = "image_path";
    String product_provider = "product_provider";
    String productprovider ;

    String string;
    ImageView bax;

    vendoraddproduct vendoraddproduct1;
    vendoraddcolor Vendoraddcolor;
    Object[] mStringArray;
    String smallcolor;
    int RESULT_LOAD_IMG = 100;
    String json_string;
    EditText qunvalue, quanvalue_1;

    String image1, image2, image3 = null;
    String ServerUploadPath = "https://staginigserver.perfectmandi.com/add_vendor_pr.php";
    EditText nameforproduct, descriptionforproduct, unitprice_, bundleprice_;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    String userid,id;
    String session="user_session";
    String recievesession;
    String lc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Intent intent=getIntent();
        /*id=intent.getStringExtra("id");*/
        userid=intent.getStringExtra("userid");
        recievesession=createsession();

        if (recievesession==null)
        {
            Intent intent1=new Intent(AddProductActivity.this, LoginActivity.class);
            startActivity(intent1);
        }
        else
        {
            colorshade = new ArrayList<>();
            data = new ArrayList<>();
            data1 = new ArrayList<>();
            bitmapslist = new ArrayList<>();
            colorlist = new ArrayList<>();
            imageString = new ArrayList<>();
            IntializeWidget();


            lc=createsession();
            bax.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            String[] category = new String[]{"Select Category", "Home & Kitchen"};
            String[] subcategory = new String[]{"Select sub category", "1 dollar","Kitchen Accessories","Silver","Plastic","Steel & Iron","Crockery","Electronics & Gifts","Melamin","Glass"};
            String[] availablecolor = new String[]{"Select Color", "Red","Yellow","Pink", "Blue", "Green","Black","White","Silver", "Multi Color"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_spinner_dropdown_item, category);
//set the spinners adapter to the previously created one.
            spinner.setAdapter(adapter);


            //
            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_spinner_dropdown_item, subcategory);
            subspinner.setAdapter(adapter1);

            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_spinner_dropdown_item, availablecolor);
//set the spinners adapter to the previously created one.
            colorspinner.setAdapter(adapter2);


            colorspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    smallcolor = colorspinner.getSelectedItem().toString();
                    colorlist.add(smallcolor);
                    upload_bla(smallcolor);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });


            uploadpic(null);


            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                    new IntentFilter("custom-message"));


            addproduct_.setOnClickListener(new View.OnClickListener()
                                           {
                                               @Override
                                               public void onClick(View v)
                                               {
                                                   getdata();
                                                   boolean value = validate_data();
                                                   if (value == true)
                                                   {
                                                       string = JsonString();
                                                       System.out.println(string);

                                                       // System.out.println(string);
                                                       ImageUploadToServerFunction();


                                                   }
                                                   else
                                                   {
                                                       Toast.makeText(AddProductActivity.this, "Hello Falase", Toast.LENGTH_LONG).show();
                                                   }

                                               }

                                           }

            );






        }


    }
    void init()
    {
        new AsyncFetch().execute();
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        ProgressDialog progressDialog=new ProgressDialog(AddProductActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                url = new URL("https://staginigserver.perfectmandi.com/getservice_V.php");
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



           /* List<vendorServiceModel> data=new ArrayList<>();


            try {



                JSONArray jArray = new JSONArray(result);



                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    vendorServiceModel fishData = new vendorServiceModel();
                    fishData.serviceicon= json_data.getString("serviceicon");
                    fishData.servicename= json_data.getString("servicename");
                    fishData.usertype= json_data.getString("usertype");
                    fishData.servicestatus=json_data.getString("status");
                    fishData.userid=userid;
                    fishData.name=name;
                    fishData.session=session;



                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview

                mAdapter = new AdapterHome(MainActivity.this, data);
                progressDialog.dismiss();
                mRVFishPrice.setAdapter(mAdapter);
                progressDialog.dismiss();
                mRVFishPrice.setLayoutManager( new GridLayoutManager(MainActivity.this,4));

            } catch (Exception e)
            {

            }*/

        }

    }
    // Select image from camera and gallery
    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductActivity.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }




    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String flag = intent.getStringExtra("flag");

            if (flag.equalsIgnoreCase("one"))
            {
              selectImage();
            }


        }
    };

    void IntializeWidget() {

     bax = findViewById(R.id.bax_store);
        qunvalue = findViewById(R.id.valuecart);
        quanvalue_1 = findViewById(R.id.valuecart1);
        spinner = findViewById(R.id.categorySpinner);

        subspinner = findViewById(R.id.subcategorySpinner);
        colorspinner = findViewById(R.id.colorSpinner);
        addpic = findViewById(R.id.addpicpor);
        color_add = findViewById(R.id.color_add);
        addproduct_ = findViewById(R.id.addproductbtn_);

        nameforproduct = findViewById(R.id.nameforproduct);
        descriptionforproduct = findViewById(R.id.descriptionforproduct);



        //
        unitprice_ = findViewById(R.id.unityprice_1);
        bundleprice_ = findViewById(R.id.bundleprice_1);
    }

    void getdata()
    {
        category = spinner.getSelectedItem().toString();
        subcategory = subspinner.getSelectedItem().toString();
        productname = nameforproduct.getText().toString();
        description = descriptionforproduct.getText().toString();
        unityquantity = qunvalue.getText().toString();
        bundlequantity = quanvalue_1.getText().toString();

        unitprice = unitprice_.getText().toString();
        bundleprice = bundleprice_.getText().toString();


    }




    public String BitMapToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    boolean validate_data() {
        if (productname.equalsIgnoreCase("")) {
            nameforproduct.requestFocus();
            Toast.makeText(AddProductActivity.this, "Please add product name", Toast.LENGTH_LONG).show();
        } else {
            if (description.equalsIgnoreCase("")) {
                descriptionforproduct.requestFocus();
                Toast.makeText(AddProductActivity.this, "Please add product description", Toast.LENGTH_LONG).show();
            } else {

                if (category.toString().equalsIgnoreCase("select category")) {
                    spinner.requestFocus();
                    Toast.makeText(AddProductActivity.this, "Please add Category", Toast.LENGTH_LONG).show();

                } else {
                    if (subcategory.toString().equalsIgnoreCase("select sub category")) {
                        subspinner.requestFocus();
                        Toast.makeText(AddProductActivity.this, "Please add sub Category", Toast.LENGTH_LONG).show();

                    } else {
                        if (unitprice.equalsIgnoreCase("")) {
                            unitprice_.requestFocus();
                            Toast.makeText(AddProductActivity.this, "Please add unit price", Toast.LENGTH_LONG).show();

                        } else {
                            if (bundleprice.equalsIgnoreCase("")) {
                                bundleprice_.requestFocus();
                                Toast.makeText(AddProductActivity.this, "Please add bundle price", Toast.LENGTH_LONG).show();

                            } else {
                                if (unityquantity.equalsIgnoreCase("0")) {
                                    qunvalue.requestFocus();
                                    Toast.makeText(AddProductActivity.this, "Please add unity quantity", Toast.LENGTH_LONG).show();

                                } else {
                                    if (bundlequantity.equalsIgnoreCase("0")) {
                                        quanvalue_1.requestFocus();


                                    } else {
                                        if (colorshade.size() > 0) {

                                                 /*      getdata();
                                                                String sxtr=JsonString();
                                                                System.out.println(sxtr);*/
                                            if (bitmapslist.size() > 0) {
                                                return true;
                                            } else {
                                                Toast.makeText(AddProductActivity.this, "Please add Picture", Toast.LENGTH_LONG).show();

                                            }

                                        } else {
                                            Toast.makeText(AddProductActivity.this, "Please add color", Toast.LENGTH_LONG).show();

                                        }

                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    void upload_bla(String color) {
        vendorcoloradd Vendorcolor = new vendorcoloradd();
        if (color.equalsIgnoreCase("Select Color"))
        {


        } else {

            if (data1.size() < 3) {
                //   Vendorcolor.name = color;
                // data1.add(Vendorcolor);
                if (colorshade.contains(color))
                {
                    Toast.makeText(AddProductActivity.this,"Color Already Selected",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    colorshade.add(color);
                }


                Vendoraddcolor = new vendoraddcolor(AddProductActivity.this, colorshade);
                color_add.setAdapter(Vendoraddcolor);

                color_add.setLayoutManager(new GridLayoutManager(AddProductActivity.this, 2));
                Vendoraddcolor.notifyDataSetChanged();

                // notify();


            } else {
                Toast.makeText(AddProductActivity.this, "Enough..", Toast.LENGTH_LONG).show();
            }

            for (int i=0;i<data1.size();i++)
            {

                System.out.println(data1.get(i));
            }
        }
    }


    void getdatap()
    {
        colorshade.removeAll(colorshade);
        Vendoraddcolor = new vendoraddcolor(AddProductActivity.this, colorshade);
        color_add.setAdapter(Vendoraddcolor);
        color_add.setLayoutManager(new GridLayoutManager(AddProductActivity.this, 2));
        Vendoraddcolor.notifyDataSetChanged();



        bitmapslist.removeAll(bitmapslist);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.addplus);
        bitmapslist.add(icon);
               /* vendorproadd.id = "0";
                data.add(vendorproad);*/
        vendoraddproduct1 = new vendoraddproduct(AddProductActivity.this, bitmapslist);
        addpic.setAdapter(vendoraddproduct1);
        addpic.setLayoutManager(new GridLayoutManager(AddProductActivity.this, 4));

        reset();

    }


    void reset()
    {
        spinner.setSelection(0);
        subspinner.setSelection(0);
        colorspinner.setSelection(0);
        nameforproduct.setText(" ");
        descriptionforproduct.setText(" ");
        qunvalue.setText(" ");
        quanvalue_1.setText(" ");
        unitprice_.setText(" ");
        bundleprice_.setText("");

        nameforproduct.requestFocus();

    }


    void uploadpic(Bitmap pic) {
        // data=new ArrayList<>();
        vendorproadd vendorproad = new vendorproadd();
        if (data.size() < 4)
        {


            if (pic == null)
            {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.addplus);
                bitmapslist.add(icon);
               /* vendorproadd.id = "0";
                data.add(vendorproad);*/
                vendoraddproduct1 = new vendoraddproduct(AddProductActivity.this, bitmapslist);
                addpic.setAdapter(vendoraddproduct1);
                addpic.setLayoutManager(new GridLayoutManager(AddProductActivity.this, 4));
            }
            else
                {
                vendorproad.id = String.valueOf(data.size());
                vendorproad.bitmapsrc = pic;
                bitmapslist.add(pic);

                Bitmap rb=getResizedBitmap(pic,150);

                imageString.add(BitMapToString(rb));

                data.add(vendorproad);
                vendoraddproduct1 = new vendoraddproduct(AddProductActivity.this, bitmapslist);
                addpic.setAdapter(vendoraddproduct1);
                addpic.setLayoutManager(new GridLayoutManager(AddProductActivity.this, 4));
            }

        } else {
            Toast.makeText(AddProductActivity.this, "You Dont add more then this", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (reqCode == PICK_IMAGE_GALLERY)
        {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                uploadpic(selectedImage);
                // image_view.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AddProductActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }
        else if (reqCode == PICK_IMAGE_CAMERA)
        {
            //Uri selectedImage = data.getData();\
            try
            {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                uploadpic(photo);
            //   imageView.setImageBitmap(photo);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(AddProductActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

            //uploadpic(photo);
        }

    }


    String JsonString() {

        int i;


        json_string = null;


        try {
            //Repeat and loop this until all objects are added (and add try+catch)
            json_string = "{\"upload_fishes\":[";


            JSONObject obj_new = new JSONObject();
            JSONObject obj_color = new JSONObject();
            for (int j = 0; j < colorshade.size(); j++) {
                obj_color.put(String.valueOf(j + 1), colorshade.get(j));
            }

            JSONObject obj_pic = new JSONObject();
            for (int k = 0; k < imageString.size(); k++) {
                obj_pic.put(String.valueOf(k + 1), String.valueOf(imageString.get(k)));
            }


            obj_new.put("productname", productname);
            obj_new.put("Description", description);
            obj_new.put("product kprovider",userid);
            obj_new.put("category", category);
            obj_new.put("subcategory", subcategory);
            obj_new.put("color", obj_color);
            obj_new.put("pic", obj_pic);


            obj_new.put("unitquantity", unityquantity);
            obj_new.put("bundlequantity", bundlequantity);
            obj_new.put("unitprice", unitprice);
            obj_new.put("bundleprice", bundleprice);
            obj_new.put("colorvariation", String.valueOf(colorshade.size()));
            obj_new.put("productpic",String.valueOf(imageString.size()));


            //  obj_new.put(productprovider,userid);
            obj_new.put("date", "");
            json_string = json_string + obj_new.toString() + ",";


            json_string = json_string.substring(0, json_string.length() - 1);
            json_string += "]}";


        } catch (JSONException jsox) {
            Toast.makeText(AddProductActivity.this, jsox.toString(), Toast.LENGTH_LONG).show();
        }

        return json_string;
    }








    public void ImageUploadToServerFunction(){

        Bitmap bitmap=bitmapslist.get(0);
        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(AddProductActivity.this,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                if(string1.contains("Data Updated Successfully"))
                {

                    Toast.makeText(AddProductActivity.this,string1,Toast.LENGTH_LONG).show();
                    getdatap();
                 /*   Intent intent=new Intent(AddProductActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();*/

                }
                else
                {
                    getdatap();
                    Toast.makeText(AddProductActivity.this,string1,Toast.LENGTH_LONG).show();
                }



            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                //  HashMapParams.put(ImageName, GetInvoiceNumber);

                HashMapParams.put("data",json_string);
                HashMapParams.put(ImagePath, ConvertImage);
                HashMapParams.put(product_provider,id);
                HashMapParams.put(session,recievesession);
                //HashMapParams.put(productprovider,userid);
 /*               HashMapParams.put(deposit_amount, GetDepositamount);
                HashMapParams.put(user_id, Getuserid);*/

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
}
/*
package com.vendor.perfectmandii.Activity.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vendor.perfectmandii.Activity.Order.fetch.UserOrderActivity;
import com.vendor.perfectmandii.Activity.PaymentDepositActivity;
import com.vendor.perfectmandii.Adapter.AdapterHome;
import com.vendor.perfectmandii.Adapter.Vendor.vendoraddcolor;
import com.vendor.perfectmandii.Adapter.Vendor.vendoraddproduct;
import com.vendor.perfectmandii.MainActivity;
import com.vendor.perfectmandii.Model.vendor.vendorServiceModel;
import com.vendor.perfectmandii.Model.vendor.vendorcoloradd;
import com.vendor.perfectmandii.Model.vendor.vendorproadd;
import com.vendor.perfectmandii.R;
import com.vendor.perfectmandii.shoppingBasket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
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

public class AddProductActivity extends AppCompatActivity
{
    boolean check = true;
    int count, count1;
    String productname, description, category, subcategory, unityquantity, bundlequantity, unitprice, bundleprice;
    CardView addunit, minusunit, addunit_1, minusunit_1;
    String imageStrings;
    ProgressDialog progressDialog;
    CardView addproduct_;
    List<vendorcoloradd> data1;
    List<vendorproadd> data;
    List<Bitmap> bitmapslist;
    List<String> colorlist;
    List<String> imageString;
    AppCompatSpinner spinner, subspinner, colorspinner;
    RecyclerView addpic, color_add;
    ArrayList<String> colorshade;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    String ImagePath = "image_path";
    String product_provider = "product_provider";
    String productprovider ;

    String string;


    vendoraddproduct vendoraddproduct1;
    vendoraddcolor Vendoraddcolor;
    Object[] mStringArray;
    String smallcolor;
    int RESULT_LOAD_IMG = 100;
    String json_string;
    TextView qunvalue, quanvalue_1;

    String image1, image2, image3 = null;
    String ServerUploadPath = "https://staginigserver.perfectmandi.com/add_vendor_pr.php";
    EditText nameforproduct, descriptionforproduct, unitprice_, bundleprice_;

    String userid,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        userid=intent.getStringExtra("userid");
        colorshade = new ArrayList<>();
        data = new ArrayList<>();
        data1 = new ArrayList<>();
        bitmapslist = new ArrayList<>();
        colorlist = new ArrayList<>();
        imageString = new ArrayList<>();
        IntializeWidget();


        String[] category = new String[]{"Select Category", "Home & Kitchen"};
        String[] subcategory = new String[]{"Select sub category", "1 dollar","Kitchen Accessories","Silver","Plastic","Steel & Iron","Crockery","Electronics & Gifts","Melamin","Glass"};
        String[] availablecolor = new String[]{"Select Color", "Red","Yellow","Pink", "Blue", "Green","Black","White","Silver", "Multi Color"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_spinner_dropdown_item, category);
//set the spinners adapter to the previously created one.
        spinner.setAdapter(adapter);


        //
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_spinner_dropdown_item, subcategory);
        subspinner.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_spinner_dropdown_item, availablecolor);
//set the spinners adapter to the previously created one.
        colorspinner.setAdapter(adapter2);


        colorspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                smallcolor = colorspinner.getSelectedItem().toString();
                colorlist.add(smallcolor);
                upload_bla(smallcolor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        uploadpic(null);


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));


        addproduct_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata();
                boolean value = validate_data();
                if (value == true)
                {
                    string = JsonString();
                    System.out.println(string);

                   // System.out.println(string);
                    ImageUploadToServerFunction();


                }
                else
                    {
                    Toast.makeText(AddProductActivity.this, "Hello Falase", Toast.LENGTH_LONG).show();
                }

            }
        });

        addunit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count = count + 1;

                qunvalue.setText(String.valueOf(count));

            }
        });

        minusunit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count <= 0) {

                } else {
                    count = count - 1;

                    qunvalue.setText(String.valueOf(count));
                }
            }
        });

        addunit_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count1 = count1 + 1;

                quanvalue_1.setText(String.valueOf(count1));

            }
        });

        minusunit_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count <= 0) {

                } else {
                    count1 = count1 - 1;

                    quanvalue_1.setText(String.valueOf(count1));
                }
            }
        });


    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String flag = intent.getStringExtra("flag");

            if (flag.equalsIgnoreCase("one")) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }


        }
    };

    void IntializeWidget() {

        qunvalue = findViewById(R.id.valuecart);
        quanvalue_1 = findViewById(R.id.valuecart1);
        spinner = findViewById(R.id.categorySpinner);

        subspinner = findViewById(R.id.subcategorySpinner);
        colorspinner = findViewById(R.id.colorSpinner);
        addpic = findViewById(R.id.addpicpor);
        color_add = findViewById(R.id.color_add);
        addproduct_ = findViewById(R.id.addproductbtn_);

        nameforproduct = findViewById(R.id.nameforproduct);
        descriptionforproduct = findViewById(R.id.descriptionforproduct);

        addunit = findViewById(R.id.addone);
        minusunit = findViewById(R.id.minusone);


        addunit_1 = findViewById(R.id.addone_1);
        minusunit_1 = findViewById(R.id.minusone_1);

        //
        unitprice_ = findViewById(R.id.unityprice_1);
        bundleprice_ = findViewById(R.id.bundleprice_1);
    }

    void getdata()
    {
        category = spinner.getSelectedItem().toString();
        subcategory = subspinner.getSelectedItem().toString();
        productname = nameforproduct.getText().toString();
        description = descriptionforproduct.getText().toString();
        unityquantity = qunvalue.getText().toString();
        bundlequantity = quanvalue_1.getText().toString();

        unitprice = unitprice_.getText().toString();
        bundleprice = bundleprice_.getText().toString();


    }




    public String BitMapToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    boolean validate_data() {
        if (productname.equalsIgnoreCase("")) {
            nameforproduct.requestFocus();
            Toast.makeText(AddProductActivity.this, "Please add product name", Toast.LENGTH_LONG).show();
        } else {
            if (description.equalsIgnoreCase("")) {
                descriptionforproduct.requestFocus();
                Toast.makeText(AddProductActivity.this, "Please add product description", Toast.LENGTH_LONG).show();
            } else {

                if (category.toString().equalsIgnoreCase("select category")) {
                    spinner.requestFocus();
                    Toast.makeText(AddProductActivity.this, "Please add Category", Toast.LENGTH_LONG).show();

                } else {
                    if (subcategory.toString().equalsIgnoreCase("select sub category")) {
                        subspinner.requestFocus();
                        Toast.makeText(AddProductActivity.this, "Please add sub Category", Toast.LENGTH_LONG).show();

                    } else {
                        if (unitprice.equalsIgnoreCase("")) {
                            unitprice_.requestFocus();
                            Toast.makeText(AddProductActivity.this, "Please add unit price", Toast.LENGTH_LONG).show();

                        } else {
                            if (bundleprice.equalsIgnoreCase("")) {
                                bundleprice_.requestFocus();
                                Toast.makeText(AddProductActivity.this, "Please add bundle price", Toast.LENGTH_LONG).show();

                            } else {
                                if (unityquantity.equalsIgnoreCase("0")) {
                                    qunvalue.requestFocus();
                                    Toast.makeText(AddProductActivity.this, "Please add unity quantity", Toast.LENGTH_LONG).show();

                                } else {
                                    if (bundlequantity.equalsIgnoreCase("0")) {
                                        quanvalue_1.requestFocus();


                                    } else {
                                        if (colorshade.size() > 0) {

                                                 */
/*      getdata();
                                                                String sxtr=JsonString();
                                                                System.out.println(sxtr);*//*

                                            if (bitmapslist.size() > 0) {
                                                return true;
                                            } else {
                                                Toast.makeText(AddProductActivity.this, "Please add Picture", Toast.LENGTH_LONG).show();

                                            }

                                        } else {
                                            Toast.makeText(AddProductActivity.this, "Please add color", Toast.LENGTH_LONG).show();

                                        }

                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    void upload_bla(String color) {
        vendorcoloradd Vendorcolor = new vendorcoloradd();
        if (color.equalsIgnoreCase("Select Color"))
        {


        } else {

            if (data1.size() < 3) {
             //   Vendorcolor.name = color;
               // data1.add(Vendorcolor);
               if (colorshade.contains(color))
               {
                   Toast.makeText(AddProductActivity.this,"Color Already Selected",Toast.LENGTH_SHORT).show();
               }
               else
               {
                   colorshade.add(color);
               }


                Vendoraddcolor = new vendoraddcolor(AddProductActivity.this, colorshade);
                color_add.setAdapter(Vendoraddcolor);

                color_add.setLayoutManager(new GridLayoutManager(AddProductActivity.this, 2));
                Vendoraddcolor.notifyDataSetChanged();

               // notify();


            } else {
                Toast.makeText(AddProductActivity.this, "Enough..", Toast.LENGTH_LONG).show();
            }

            for (int i=0;i<data1.size();i++)
            {

                System.out.println(data1.get(i));
            }
        }
    }


    void getdatap()
    {

    }
    void uploadpic(Bitmap pic) {
        // data=new ArrayList<>();
        vendorproadd vendorproad = new vendorproadd();
        if (data.size() < 4) {
            if (pic == null) {

                vendorproadd.id = "0";
                data.add(vendorproad);
                vendoraddproduct1 = new vendoraddproduct(AddProductActivity.this, data);
                addpic.setAdapter(vendoraddproduct1);
                addpic.setLayoutManager(new GridLayoutManager(AddProductActivity.this, 4));
            } else {
                vendorproad.id = String.valueOf(data.size());
                vendorproad.bitmapsrc = pic;
                bitmapslist.add(pic);

                Bitmap rb=getResizedBitmap(pic,150);

                imageString.add(BitMapToString(rb));

                data.add(vendorproad);
                vendoraddproduct1 = new vendoraddproduct(AddProductActivity.this, data);
                addpic.setAdapter(vendoraddproduct1);
                addpic.setLayoutManager(new GridLayoutManager(AddProductActivity.this, 4));
            }

        } else {
            Toast.makeText(AddProductActivity.this, "You Dont add more then this", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                uploadpic(selectedImage);
                // image_view.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AddProductActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(AddProductActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }


    String JsonString() {

        int i;


        json_string = null;


        try {
            //Repeat and loop this until all objects are added (and add try+catch)
            json_string = "{\"upload_fishes\":[";


            JSONObject obj_new = new JSONObject();
            JSONObject obj_color = new JSONObject();
            for (int j = 0; j < colorshade.size(); j++) {
                obj_color.put(String.valueOf(j + 1), colorshade.get(j));
            }

            JSONObject obj_pic = new JSONObject();
            for (int k = 0; k < imageString.size(); k++) {
                obj_pic.put(String.valueOf(k + 1), String.valueOf(imageString.get(k)));
            }


            obj_new.put("productname", productname);
            obj_new.put("Description", description);
            obj_new.put("product kprovider",userid);
            obj_new.put("category", category);
            obj_new.put("subcategory", subcategory);
            obj_new.put("color", obj_color);
            obj_new.put("pic", obj_pic);


            obj_new.put("unitquantity", unityquantity);
            obj_new.put("bundlequantity", bundlequantity);
            obj_new.put("unitprice", unitprice);
            obj_new.put("bundleprice", bundleprice);
            obj_new.put("colorvariation", String.valueOf(colorshade.size()));
            obj_new.put("productpic",String.valueOf(imageString.size()));


          //  obj_new.put(productprovider,userid);
            obj_new.put("date", "");
            json_string = json_string + obj_new.toString() + ",";


            json_string = json_string.substring(0, json_string.length() - 1);
            json_string += "]}";


        } catch (JSONException jsox) {
            Toast.makeText(AddProductActivity.this, jsox.toString(), Toast.LENGTH_LONG).show();
        }

        return json_string;
    }








    public void ImageUploadToServerFunction(){

        Bitmap bitmap=bitmapslist.get(0);
        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(AddProductActivity.this,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

             if(string1.equalsIgnoreCase("Data Updated SuccessfullyData Updated Successfully"))
             {

                 Toast.makeText(AddProductActivity.this,string1,Toast.LENGTH_LONG).show();
                 Intent intent=new Intent(AddProductActivity.this,MainActivity.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                 startActivity(intent);
                 finish();

             }
             else if("Data Updated Successfully".equalsIgnoreCase(string1))
             {

                 Toast.makeText(AddProductActivity.this,string1,Toast.LENGTH_LONG).show();
                 Intent intent=new Intent(AddProductActivity.this,MainActivity.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                 startActivity(intent);
                 finish();

             }
             else
             {
                 Toast.makeText(AddProductActivity.this,string1,Toast.LENGTH_LONG).show();
             }



            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

              //  HashMapParams.put(ImageName, GetInvoiceNumber);

                HashMapParams.put("data",json_string);
                HashMapParams.put(ImagePath, ConvertImage);
                HashMapParams.put(product_provider,id);
                //HashMapParams.put(productprovider,userid);
 */
/*               HashMapParams.put(deposit_amount, GetDepositamount);
                HashMapParams.put(user_id, Getuserid);*//*


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
}*/
