package com.vendor.perfectmandii.profile_Updates;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentCallbacks2;
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
import android.util.LruCache;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;

import com.vendor.perfectmandii.R;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ProductInformationActivity extends AppCompatActivity implements ComponentCallbacks2
{
    LinearLayout avg_container;
    private static final int PERMISSION_REQUEST_CODE = 101;
    String ServerUploadPath = "https://sellerportal.perfectmandi.com/add_product_vb.php";
    String storecphoto,photostring;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    JSONArray json_measure;
    List<String> parentcategory;
    List<String> childcategory;
    List<String> subchildcategory,meausrecategory_list,colorcategory_list;
    HashMap<String, JSONArray> ExpListData,ExpListData1;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Spinner spinner,scategory,sccategory,meausrecategory,colorcategory;
     String userid,name,session;
    LinearLayout category_container,profile_container,attachment_container;
    EditText  accountid_PI,serial_id_PI,sku_PI,product_avgweight;
    String serialid,sku,account_id,maincategory,sub_catgory,sub_child_category,color_selection,measurement_selection,_name,_description,_unit,_price;
    ImageView viewimage;
    Bitmap bitmap;
    EditText product_description,product_name,unit_quantity,unit_price;
    TextView next_button;
    String serial_id = "serial_id"; //1
    String accountid = "account_id";//2
    String accpontsku= "sku";//3
    String main_catgory = "main_catgory";//4
    String main_sub_category = "main_sub_category";//5
    String _sub_category = "_sub_category";//6
    String _measure_category = "_measure_category";//7
    String _color_category = "_color_category";//8
    String _product_name = "product_name";//9
    String product_Desc = "product_Desc";//10
    String product_unit = "product_unit";//11
    String product_price = "product_price";//12
    String image_path = "image_path";//13
    String average_weight = "average_weight";//13
    String averageweight=null;
    //
    private LruCache<String, Bitmap> memoryCache;
    ImageView backspace;
    // this event will enable the back
    // function to the button on press


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_information);
        parentcategory=new ArrayList<>();
        childcategory=new ArrayList<>();
        subchildcategory=new ArrayList<>();
        meausrecategory_list=new ArrayList<>();
        colorcategory_list=new ArrayList<>();
        intiazlize_widget();
        avg_container.setVisibility(View.GONE);

       // ActionBar actionBar = getSupportActionBar();
        intialize_widgetState();
        Intent getdata = getIntent();

        userid=getdata.getStringExtra("userid");
        name=getdata.getStringExtra("username");
        session=createsession();


        //
        new AsyncFetch().execute();


        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
        viewimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions(ProductInformationActivity.this)) {
                    chooseImage(ProductInformationActivity.this);
                } else {
                    chooseImage(ProductInformationActivity.this);
                }
            }
        });
       // actionBar.setDisplayHomeAsUpEnabled(true);

        backspace.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });


 /*       meausrecategory.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {


            }
        });*/

        meausrecategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String valToSet = meausrecategory.getSelectedItem().toString();
                Toast.makeText(ProductInformationActivity.this,valToSet,Toast.LENGTH_LONG).show();
                if(valToSet.equalsIgnoreCase("Kilogram"))
                {

                    avg_container.setVisibility(View.VISIBLE);
                }
                else
                {
                    avg_container.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }





    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key)
    {
        return memoryCache.get(key);
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
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(ProductInformationActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ProductInformationActivity.this,
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(ProductInformationActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ProductInformationActivity.this,
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                }
                else if (ContextCompat.checkSelfPermission(ProductInformationActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ProductInformationActivity.this,
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                }else {
                    chooseImage(ProductInformationActivity.this);
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
                       // photostring=convertImage(bitmap);
                       // viewimage.setImageBitmap(bitmap);
                        viewimage.setImageBitmap(
                                decodeSampledBitmapFromResource(getResources(), R.id.product_pic, 100, 100));

                        Glide.with(this).load(bitmap).into(viewimage);
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
                              //  viewimage.setImageBitmap(bitmap);
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    public void onTrimMemory(int level) {

        // Determine which lifecycle or system event was raised.
        switch (level) {

            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:

                /*
                   Release any UI objects that currently hold memory.

                   The user interface has moved to the background.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:

                /*
                   Release any memory that your app doesn't need to run.

                   The device is running low on memory while the app is running.
                   The event raised indicates the severity of the memory-related event.
                   If the event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will
                   begin killing background processes.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:

                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */

                break;

            default:
                /*
                  Release any non-critical data structures.

                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                break;
        }
    }
    // Get a MemoryInfo object for the device's current memory status.
    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
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
    void intiazlize_widget()
    {
        product_avgweight=findViewById(R.id.product_avgweight);
        avg_container=findViewById(R.id.avg_container);
        backspace=findViewById(R.id.backspace);
        next_button=findViewById(R.id.next_button);
        product_description=findViewById(R.id.product_description);
        product_name=findViewById(R.id.product_name);
        unit_quantity=findViewById(R.id.unit_quantity);
        unit_price=findViewById(R.id.unit_price);
        category_container=findViewById(R.id.category_container);
        profile_container=findViewById(R.id.profile_container);
        attachment_container=findViewById(R.id.attachment_container);
        spinner=findViewById(R.id.category_spinner);
        scategory=findViewById(R.id.scategory);
        sccategory=findViewById(R.id.sccategory);
        accountid_PI=findViewById(R.id.accountid_PI);
        serial_id_PI=findViewById(R.id.serial_id_PI);
        sku_PI=findViewById(R.id.sku_PI);
        meausrecategory=findViewById(R.id.meausrecategory);
        colorcategory=findViewById(R.id.colorcategory);
        viewimage=findViewById(R.id.product_pic);
    }
    void intialize_widgetState()
    {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position>0)
                {
                    String str = String.valueOf(spinner.getSelectedItemPosition());
                  String a=  parentcategory.get(position);
                    JSONArray jsonArray=ExpListData.get(a);
                    System.out.println(a+str+jsonArray);
                    try {
                        fetchSubcategory(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        scategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0)
                {


                    String a=  childcategory.get(position);

                    JSONArray jsonArray=ExpListData1.get(a);

                    System.out.println(a+jsonArray);
                    try {


                        fetchSubCcategory(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sccategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0)
                {
                    profile_container.setVisibility(View.VISIBLE);
                    attachment_container.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   if (checkAndRequestPermissions(ProductInformationActivity.this)) {
                    chooseImage(ProductInformationActivity.this);
                } else {
                    chooseImage(ProductInformationActivity.this);
                }*/
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                getEmpty();
            }
        });
    }
    void fetchSubCcategory(JSONArray jsonArray) throws JSONException
    {

       JSONArray jArray =jsonArray;




       if (subchildcategory.size()>1)
       {
           subchildcategory.clear();
       }
       subchildcategory.add("Select sub Category");

       for (int i=0;i<jArray.length();i++)
       {
           JSONObject json_data = jArray.getJSONObject(i);
           String Name=json_data.getString("name");
           subchildcategory.add(Name);
           System.out.println(Name);
       }
       populate_subchilcategory();



    }
    String convertImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        storecphoto = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return storecphoto;
    }
    private void populate_subchilcategory()
    {
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(ProductInformationActivity.this,R.layout.item_list,subchildcategory);

        sccategory.setAdapter(arrayAdapter);
        sccategory.setVisibility(View.VISIBLE);
    }
    private void populate_meausurement()
    {
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(ProductInformationActivity.this,R.layout.item_list,meausrecategory_list);

        meausrecategory.setAdapter(arrayAdapter);
        meausrecategory.setVisibility(View.VISIBLE);
    }
    private void populate_color()
    {
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(ProductInformationActivity.this,R.layout.item_list,colorcategory_list);

        colorcategory.setAdapter(arrayAdapter);
        colorcategory.setVisibility(View.VISIBLE);
    }

    void fetchSubcategory(JSONArray jsonArray) throws JSONException {
        JSONArray jArray =jsonArray;
        childcategory.add("Select sub Category");

        for (int i=0;i<jArray.length();i++)
        {
            JSONObject json_data = jArray.getJSONObject(i);
            String Name=json_data.getString("Name");
            childcategory.add(Name);
            System.out.println(Name);
        }
        populate_subcategory();
    }

    private void populate_subcategory()
    {
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(ProductInformationActivity.this,R.layout.item_list,childcategory);

        scategory.setAdapter(arrayAdapter);
        scategory.setVisibility(View.VISIBLE);
    }

    void getEmpty()
    {
        //&&&&&&&&&&&&&&
        if (spinner.getSelectedItem().toString().equalsIgnoreCase("Select Category"))
        {
            Toast.makeText(ProductInformationActivity.this, "Please Select Category", Toast.LENGTH_SHORT).show();
        }
        else if(scategory.getSelectedItem().toString().equalsIgnoreCase("Select sub Category"))
        {
            Toast.makeText(ProductInformationActivity.this, "Please Select child Category", Toast.LENGTH_SHORT).show();
        }
        else if(sccategory.getSelectedItem().toString().equalsIgnoreCase("Select sub Category"))
        {
            Toast.makeText(ProductInformationActivity.this, "Please Select child Category", Toast.LENGTH_SHORT).show();
        }
        else if(meausrecategory.getSelectedItem().toString().equalsIgnoreCase("Select Criteria"))
        {
            Toast.makeText(ProductInformationActivity.this, "Please select measurement criteria", Toast.LENGTH_SHORT).show();
        }
        else if(meausrecategory.getSelectedItem().toString().equalsIgnoreCase("Select Criteria"))
        {
            Toast.makeText(ProductInformationActivity.this, "Please select measurement criteria", Toast.LENGTH_SHORT).show();
        }
        else if(colorcategory.getSelectedItem().toString().equalsIgnoreCase("Select color"))
        {
            Toast.makeText(ProductInformationActivity.this, "Please select color", Toast.LENGTH_SHORT).show();
        }
        else if (product_name.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(ProductInformationActivity.this,"Please enter product name",Toast.LENGTH_LONG).show();
        }
        else if (product_description.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(ProductInformationActivity.this,"Please enter product description",Toast.LENGTH_LONG).show();
        }
        else if (unit_quantity.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(ProductInformationActivity.this,"Please Enter Units",Toast.LENGTH_LONG).show();
        }
        else if (unit_price.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(ProductInformationActivity.this,"Please Enter Price",Toast.LENGTH_LONG).show();
        }
        else if (viewimage.getDrawable()==null)
        {
            Toast.makeText(ProductInformationActivity.this,"Please upload picture",Toast.LENGTH_LONG).show();
        }
        else
        {
           takeData();
            ImageUploadToServerFunction();
        }


    }

    void takeData()
    {
        averageweight=product_avgweight.getText().toString();
        serialid=serial_id_PI.getText().toString();
        sku=sku_PI.getText().toString();
        account_id=accountid_PI.getText().toString();
        maincategory=spinner.getSelectedItem().toString();
        sub_catgory=scategory.getSelectedItem().toString();
        sub_child_category=sccategory.getSelectedItem().toString();
        color_selection=colorcategory.getSelectedItem().toString();
        measurement_selection=meausrecategory.getSelectedItem().toString();
        _name=product_name.getText().toString();
        _description=product_description.getText().toString();
        _unit=unit_quantity.getText().toString();
        _price=unit_price.getText().toString();
        photostring=convertImage(bitmap);







    }


    private class AsyncFetch extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        ProgressDialog progressDialog=new ProgressDialog(ProductInformationActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
              progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                url = new URL("https://sellerportal.perfectmandi.com/get_createdID.php?id="+userid);
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









            if (result!=null)
            {

                try {
                    progressDialog.dismiss();
                     JSONArray jArray = new JSONArray(result);
                    JSONArray jsonArray1 = null,jsonArray_dealin=null,jsonArray_color=null;
                     for (int i=0;i<jArray.length();i++)
                     {

                         JSONObject json_data = jArray.getJSONObject(i);
                         System.out.println(json_data.getString("store_abbr"));
                         System.out.println(json_data.getString("store_provider"));
                         System.out.println(json_data.getString("product_upload"));
                         String productupload=json_data.getString("product_upload");
                         String abbr=json_data.getString("store_abbr");
                         generate_code(productupload,abbr);
                         json_measure=json_data.getJSONArray("measurement");
                         accountid_PI.setText(json_data.getString("store_provider"));






                   jsonArray1=json_data.getJSONArray("measurement");
                         getMeasurement(jsonArray1);

                         //Deal_In
                         jsonArray_dealin=json_data.getJSONArray("Deal_In");


                         //color_family
                         jsonArray_color=json_data.getJSONArray("color_family");

                     }
                    getMeasurement(jsonArray1);
                    getcategory(jsonArray_dealin);
                    getcolor(jsonArray_color);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
        void generate_code(String str,String str1)
        {
            String createstring = null;
            int num=0;
            if (str.equalsIgnoreCase("null"))
            {
                num=0;
                createstring=str1+"0000"+"0";

            }
            else
            {
                num=Integer.parseInt(str);
            }
            if (num>0&&num<10)
            {

                createstring=str1+"0000"+Integer.toString(num);
            }
            else if (num>10&&num<100)
            {
                createstring=str1+"000"+Integer.toString(num);
            }
            else if (num>100&&num<1000)
            {
                createstring=str1+"00"+Integer.toString(num);
            }
            else if (num>1000&&num<10000)
            {
                createstring=str1+"0"+Integer.toString(num);
            }
            else if (num>10000&&num<100000)
            {
                createstring=str1+Integer.toString(num);
            }

            //sku_PI//serial_id_PI
            serial_id_PI.setText(createstring);

            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
            String formattedDate = df.format(date);
            System.out.println(formattedDate);
            sku_PI.setText(createstring+formattedDate);

        }
        void getMeasurement(JSONArray jsonArray) throws JSONException {

            if (meausrecategory_list.size()>1)
            {

            }
            else
            {
                meausrecategory_list.add("Select Selling Criteria");
                for (int j=0;j<jsonArray.length();j++)
                {

                    JSONObject json_measurement = jsonArray.getJSONObject(j);
                    String name=json_measurement.getString("pm_name");
                    String sy=json_measurement.getString("symbol");

                    meausrecategory_list.add(name);

                }
                populate_meausurement();
                meausrecategory.setVisibility(View.VISIBLE);
            }

        }
        void getcolor(JSONArray jsonArray) throws JSONException {
        if (colorcategory_list.size()>1)
        {

        }
        else
        {
            colorcategory_list.add("Select color");
            for (int j=0;j<jsonArray.length();j++)
            {

                JSONObject json_measurement = jsonArray.getJSONObject(j);

                String color=json_measurement.getString("name");
                colorcategory_list.add(color);
            }
            populate_color();
            colorcategory.setVisibility(View.VISIBLE);
        }

        }
        void getcategory(JSONArray jsonArray) throws JSONException
        {
            ExpListData = new HashMap<String,JSONArray>();
            parentcategory.add("Select Category");
            JSONArray json_catalog=null;
            for (int j=0;j<jsonArray.length();j++)
            {

                JSONObject json_measurement = jsonArray.getJSONObject(j);
               String name=json_measurement.getString("name");
              json_catalog=json_measurement.getJSONArray("catalog");
              parentcategory.add(name);
              ExpListData.put(name, json_catalog);


            }
            getcategory_child(json_catalog);
            pupulate_parentcategory();

        }
        void pupulate_parentcategory()
        {
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(ProductInformationActivity.this,R.layout.item_list,parentcategory);

            spinner.setAdapter(arrayAdapter);
        }

        void getcategory_child(JSONArray jsonArray) throws JSONException
        {

            ExpListData1 = new HashMap<String,JSONArray>();
            for (int j=0;j<jsonArray.length();j++)
            {


                JSONObject json_measurement = jsonArray.getJSONObject(j);
                String Name=json_measurement.getString("Name");
                JSONArray json_catalog=json_measurement.getJSONArray("type");
                System.out.println(Name+" "+json_catalog);
                ExpListData1.put(Name,json_catalog);

            }


        }
    }


    //sendDatatoServer

    public void ImageUploadToServerFunction(){



        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String>
        {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(ProductInformationActivity.this,"Product Information is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);
                // Toast.makeText(AddStoreActivity.this,string1,Toast.LENGTH_SHORT).show();


                Toast.makeText(ProductInformationActivity.this, string1, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                if ("Product Added,Wait for Approval".equalsIgnoreCase(string1))
                {

                 product_name.setText("");
                 product_description.setText("");
                 unit_price.setText("");
                 unit_quantity.setText("");
                 parentcategory.clear();

                    parentcategory.clear();
                    childcategory.clear();
                    subchildcategory.clear();
                    meausrecategory_list.clear();
                    colorcategory_list.clear();
                    viewimage.setImageResource(0);
                    new AsyncFetch().execute();
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
            protected String doInBackground(Void... params)
            {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(serial_id, serialid);
                HashMapParams.put(accountid, account_id);
                HashMapParams.put(accpontsku, sku);
                HashMapParams.put(main_catgory, maincategory);
                HashMapParams.put(main_sub_category, sub_catgory); //fullname
                HashMapParams.put(_sub_category, sub_child_category);
                HashMapParams.put(_measure_category, measurement_selection);
                HashMapParams.put(_color_category,color_selection);
                HashMapParams.put(_product_name,_name); //mobile Number
                HashMapParams.put(product_Desc, _description);
                HashMapParams.put(product_unit,_unit);
                HashMapParams.put(product_price,_price); //BA
                HashMapParams.put(image_path, photostring); //SA
              HashMapParams.put(average_weight, averageweight);//SA


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