package com.consumer.perfectmandii.Activity.User.customerprofile;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.consumer.perfectmandii.LoginActivity;
import com.consumer.perfectmandii.R;


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

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileActivity extends AppCompatActivity {
    String ConvertImage;
    private boolean isFormatting;
    private boolean deletingHyphen;
    private int hyphenStart;
    private boolean deletingBackward;
    //
    EditText getfullname, getmobilenumber, getcnic, getcity, getshippingaddress, getbillingaddress;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    CheckBox sameaddress;
    CardView saverecord;
    Spinner provincespinner, city_spinner;

    CircleImageView circleImageView;
    String fullname, mobilenumber, cnic, city, shippingaddress, billingaddress, same, session, userid, username, usernumber,latv,lngv,areav,provincev;


    ProgressDialog progressDialog;

    Bitmap bitmap;

    String full_name = "full_name";
    String ImagePath = "image_path";
    String Mobilenumber = "mobile";
    String Cnic = "cnic";
    String City = "city";
    String province = "province";
    String Billingaddress = "Billing";
    String Shippingaddress = "Shipping";


    String ServerUploadPath = "https://staginigserver.perfectmandi.com/addUserIfo_.php";


    String selected_city;
    String selected;
    String fullname_u, mobilenumber_u, cnic_u, province_u, city_u, areacode_u, fsa_u, fba_u, image_path_u, status_u;

    boolean check = true;

    ImageView sidnav;

    TextView provinceText,getCityText;

    LinearLayout city_s, sprovince_s;

    TextView fulk;

    String avatar_image="https://staginigserver.perfectmandi.com/images/avatar/qwerty.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        Intent getdata = getIntent();
        session = getdata.getStringExtra("session");
        userid = getdata.getStringExtra("userid");
        Toast.makeText(this,userid,Toast.LENGTH_SHORT).show();
        username = getdata.getStringExtra("username");


        fullname_u = getdata.getStringExtra("fullname");
        mobilenumber_u = getdata.getStringExtra("mobilenumber");
        cnic_u = getdata.getStringExtra("cnic");
        province_u = getdata.getStringExtra("province");
        city_u = getdata.getStringExtra("city");
        areacode_u = getdata.getStringExtra("areacode");
        fsa_u = getdata.getStringExtra("fsa");
        fba_u = getdata.getStringExtra("fba");
        image_path_u = getdata.getStringExtra("image_path");
        status_u = getdata.getStringExtra("status");





        if(session==null)
        {
            Intent intent1=new Intent(EditProfileActivity.this, LoginActivity.class);
            startActivity(intent1);
        }
        else
        {

            intializewidget();

            Glide
                    .with(this)
                    .load(avatar_image)
                    .centerCrop()
                    .into(circleImageView);
            setvalue();

            update_provinces();
            selected=provincespinner.getSelectedItem().toString();
            update_cities(selected);




            //

            provincespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selected_city=provincespinner.getSelectedItem().toString();
                    if (selected_city.equalsIgnoreCase("Select province"))
                    {

                    }
                    else
                    {
                        update_cities(selected_city);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });







            getdata();












            sidnav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            sameaddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked)
                    {

                        shippingaddress=getbillingaddress.getText().toString();
                        getshippingaddress.setText(shippingaddress);
                    }
                    else
                    {
                        getshippingaddress.setText("");
                    }


                }
            });


            saverecord.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getdata();
                    validaterecord();
                }
            });
        }

        circleImageView.setOnClickListener(new View.OnClickListener()
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


    }

    void setvalue()
    {
        getmobilenumber.setText(userid);
        getfullname.setText(username);
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

                Bitmap returnbitmap=MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                bitmap = getResizedBitmap(returnbitmap,150);

                if(bitmap==null)
                {

                }
                else
                {
                    circleImageView.setImageBitmap(bitmap);
                    //uploadsignal.setVisibility(View.VISIBLE);
                }



                //  imageView.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    void intializewidget()
    {

     /*   provinceText=findViewById(R.id.provinceText);
        getCityText=findViewById(R.id.getCityText);*/

        //
        city_s=findViewById(R.id.city_s);
        sprovince_s=findViewById(R.id.province_spin);

        //
        sidnav=findViewById(R.id.sidnav);
        //

        saverecord=findViewById(R.id.saverecord);
        //

        getfullname=findViewById(R.id.fullname);
        getmobilenumber=findViewById(R.id.phonenumber_r);
        getcnic=findViewById(R.id.cnic);
        //getcity=findViewById(R.id.city);
        getbillingaddress=findViewById(R.id.billingaddress);
        getshippingaddress=findViewById(R.id.shippingaddress);
        //
        sameaddress=findViewById(R.id.sbss);
        //
      //  circleImageView=findViewById(R.id.profile_image2);
        //
        provincespinner=findViewById(R.id.province_spinner);
        city_spinner=findViewById(R.id.city_spinner);

    }
    void getdata()
    {




        getmobilenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (start == 3) {
                    getmobilenumber.setText(getmobilenumber.getText() + "-");
                    getmobilenumber.setSelection(getmobilenumber.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        getcnic.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {

            }
            public void beforeTextChanged(CharSequence s, int start, int count,int after)

            {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                if (start == 4 || start == 12) {
                    getcnic.setText(getcnic.getText() + "-");
                    getcnic.setSelection(getcnic.getText().length());
                }


            }
        });
        cnic=getcnic.getText().toString();
//        mobilenumber=getmobilenumber.getText().toString();
//       city=getCityText.getText().toString();
        billingaddress=getbillingaddress.getText().toString();
        shippingaddress=getshippingaddress.getText().toString();
        fullname=getfullname.getText().toString();
        mobilenumber=getmobilenumber.getText().toString();
//        provincev=provinceText.getText().toString();
        selected=provincespinner.getSelectedItem().toString();
        selected_city=city_spinner.getSelectedItem().toString();
    }
    void update_provinces()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.province_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        provincespinner.setAdapter(adapter);





    }
    void update_cities(String citied)
    {
        Toast.makeText(EditProfileActivity.this,citied,Toast.LENGTH_LONG).show();
        if("punjab".equalsIgnoreCase(citied))
        {
            city_spinner.setEnabled(true);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.punjab_city_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
            city_spinner.setAdapter(adapter);
        }
        else if("sindh".equalsIgnoreCase(citied))
        {
            city_spinner.setEnabled(false);
            Toast.makeText(EditProfileActivity.this,"We don't operate in this province",Toast.LENGTH_LONG).show();

           /* city_spinner.setEnabled(true);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.punjab_city_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
            city_spinner.setAdapter(adapter);*/
        }
        else if("Balochistan".equalsIgnoreCase(citied))
        {
            city_spinner.setEnabled(false);
            Toast.makeText(EditProfileActivity.this,"We don't operate in this province",Toast.LENGTH_LONG).show();

           /* city_spinner.setEnabled(true);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.punjab_city_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
            city_spinner.setAdapter(adapter);*/
        }
        else if("khyber pakhtunkhwa".equalsIgnoreCase(citied))
        {
            city_spinner.setEnabled(false);
            Toast.makeText(EditProfileActivity.this,"We don't operate in this province",Toast.LENGTH_LONG).show();

           /* city_spinner.setEnabled(true);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.punjab_city_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
            city_spinner.setAdapter(adapter);*/
        }
        else
        {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.kashmir_city_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
            city_spinner.setAdapter(adapter);
        }
    }




    void validaterecord()
    {
        if(fullname.equalsIgnoreCase("")&&mobilenumber.equalsIgnoreCase("")&&cnic.equalsIgnoreCase("")&& city.equalsIgnoreCase("")&&billingaddress.equalsIgnoreCase("")&&shippingaddress.equalsIgnoreCase(""))
        {
            Toast.makeText(EditProfileActivity.this,"Please fill the form",Toast.LENGTH_LONG).show();
        }
        else
        {
            if (isNetworkConnected())
            {
                int a=getWifiLevel();
                      /*

                POOR // Bandwidth under 150 kbps.
                MODERATE // Bandwidth between 150 and 550 kbps.
                GOOD // Bandwidth over 2000 kbps.
                EXCELLENT // Bandwidth over 2000 kbps.
                UNKNOWN // connection quality cannot be found.

*/
                switch (a){
                    case 1:
                        Toast.makeText(EditProfileActivity.this,"Poor",Toast.LENGTH_SHORT).show();

                        break;
                    case 2:
                        Toast.makeText(EditProfileActivity.this,"Moderate",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(EditProfileActivity.this,"Good",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        validate_record();
                        break;

                    default:
                        Toast.makeText(EditProfileActivity.this,"Unknown",Toast.LENGTH_SHORT).show();
                        break;





                }




            }
            else
            {
                boolean as=isNetworkConnected();
                Toast.makeText(EditProfileActivity.this,String.valueOf(as),Toast.LENGTH_SHORT).show();
            }
            //
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
    public void ImageUploadToServerFunction(){

        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();
        if (bitmap!=null)
        {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
            byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

            ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
        }
        else
        {
            Bitmap bm=((BitmapDrawable)circleImageView.getDrawable()).getBitmap();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
            byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

            ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        }





        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(EditProfileActivity.this,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                if ("Profile Uploaded".equalsIgnoreCase(string1))
                {
                    progressDialog.dismiss();
                    Intent intent=new Intent(EditProfileActivity.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();



                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this,string1,Toast.LENGTH_SHORT).show();
                }




            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();



                System.out.println(fullname+mobilenumber+cnic+city+provincev+billingaddress+shippingaddress);

                HashMapParams.put(ImagePath, ConvertImage);
                HashMapParams.put(full_name, fullname); //fullname
                HashMapParams.put(Mobilenumber, mobilenumber); //mobile Number
                HashMapParams.put(Cnic, cnic); //Cnic
                HashMapParams.put(City, selected_city); //City
                HashMapParams.put(province,selected);
                HashMapParams.put(Billingaddress, billingaddress); //BA
                HashMapParams.put(Shippingaddress, shippingaddress); //SA


         /*       HashMapParams.put(lat,latv);
                HashMapParams.put(lng,lngv);
                HashMapParams.put(area,areav);*/

      /*          HashMapParams.put(deposit_amount, GetDepositamount);
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
    void validate_record()
    {

        if (fullname.equalsIgnoreCase(""))
        {
            Toast.makeText(EditProfileActivity.this,"Please add your full name",Toast.LENGTH_LONG).show();
        }
        else if (mobilenumber.equalsIgnoreCase(""))
        {
            Toast.makeText(EditProfileActivity.this,"Please add your mobile number",Toast.LENGTH_LONG).show();
        }
        else if (cnic.equalsIgnoreCase(""))
        {
            Toast.makeText(EditProfileActivity.this,"Please add your cnic",Toast.LENGTH_LONG).show();
        }
        else if (billingaddress.equalsIgnoreCase(""))
        {
            Toast.makeText(EditProfileActivity.this,"Please add your billing address",Toast.LENGTH_LONG).show();
        }
        else if (shippingaddress.equalsIgnoreCase(""))
        {
            Toast.makeText(EditProfileActivity.this,"Please add your shipping address",Toast.LENGTH_LONG).show();
        }
        else if (selected_city.equalsIgnoreCase(""))
        {
            Toast.makeText(EditProfileActivity.this,"Please select city",Toast.LENGTH_LONG).show();
        }
        else if (bitmap==null)
        {
            Toast.makeText(EditProfileActivity.this,"Please upload Picture",Toast.LENGTH_LONG).show();
        }
        else
        {
            ImageUploadToServerFunction();
        }






    }


}
/*
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.customer.perfectmandii.Activity.Order.fetch.UserOrderActivity;
import com.customer.perfectmandii.Activity.PaymentDepositActivity;
import com.customer.perfectmandii.Adapter.AdapterHome;
import com.customer.perfectmandii.Adapter.Payment.Bank.bankPayment;
import com.customer.perfectmandii.BankDetailActivity;
import com.customer.perfectmandii.LoginActivity;
import com.customer.perfectmandii.MainActivity;
import com.customer.perfectmandii.Model.Bank.BankModel;
import com.customer.perfectmandii.Model.DataFish;
import com.customer.perfectmandii.R;
import com.google.android.gms.common.internal.Objects;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    EditText getfullname,getmobilenumber,getcnic,getcity,getshippingaddress,getbillingaddress;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    CheckBox sameaddress;
    CardView saverecord;
    Spinner provincespinner,city_spinner;

    CircleImageView circleImageView;
    String fullname,mobilenumber,cnic,city,shippingaddress,billingaddress,same,session,userid,username,usernumber;


    ProgressDialog progressDialog;

    Bitmap bitmap;


    String Customername = "customer_name" ;
    String ImagePath="image_path";
    String Mobilenumber="mobile";
    String Cnic="cnic";
    String City="city";
    String Billingaddress="Billing";
    String Shippingaddress="Shipping";


    String ServerUploadPath="https://staginigserver.perfectmandi.com/adduserinfo.php";


    String selected_city;

    String fullname_u,mobilenumber_u,cnic_u,province_u,city_u,areacode_u,fsa_u,fba_u,image_path_u,status_u;

    boolean check = true;

    ImageView sidnav;


    LinearLayout city_s,sprovince_s;

    TextView fulk;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Intent getdata=getIntent();
        session=getdata.getStringExtra("session");
        username=getdata.getStringExtra("username");
        usernumber=getdata.getStringExtra("userid");



        Toast.makeText(EditProfileActivity.this,usernumber,Toast.LENGTH_SHORT).show();






        if(session==null)
        {
            Intent intent1=new Intent(EditProfileActivity.this, LoginActivity.class);
            startActivity(intent1);
        }
        else
        {

            intializewidget();
            update_provinces();
            String selected=provincespinner.getSelectedItem().toString();
            update_cities(selected);



         //

            provincespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selected_city=provincespinner.getSelectedItem().toString();
                    if (selected_city.equalsIgnoreCase("Select province"))
                    {

                    }
                    else
                    {
                        update_cities(selected_city);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });





            getmobilenumber.setText(usernumber);
            getfullname.setText(username);

            getdata();
            StringBuilder sb=new StringBuilder();

            getmobilenumber.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub
                    if(sb.length()==0&getmobilenumber.length()==5)
                    {
                        sb.append(s+"-");
                       // ed24.clearFocus();
                   */
/* ed24.requestFocus();
                    ed24.setCursorVisible(true);*//*


                    }
                }
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                    if(sb.length()==1)
                    {

                        sb.deleteCharAt(0);

                    }

                }

                public void afterTextChanged(Editable s) {
                    if(sb.length()==0)
                    {

                        //ed24.requestFocus();
                    }

                }
            });

            sidnav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            sameaddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked)
                    {

                        shippingaddress=getbillingaddress.getText().toString();
                        getshippingaddress.setText(shippingaddress);
                    }
                    else
                    {
                        getshippingaddress.setText("");
                    }


                }
            });


            saverecord.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getdata();
                    validate_record();
                }
            });
        }

        circleImageView.setOnClickListener(new View.OnClickListener()
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

        getcnic.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {

            }
            public void beforeTextChanged(CharSequence s, int start, int count,int after)

            {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                if (start == 4 || start == 12) {
                    getcnic.setText(getcnic.getText() + "-");
                    getcnic.setSelection(getcnic.getText().length());
                }


            }
        });
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
                    circleImageView.setImageBitmap(bitmap);
                    //uploadsignal.setVisibility(View.VISIBLE);
                }



                //  imageView.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
    void intializewidget()
    {


        //
        city_s=findViewById(R.id.city_s);
        sprovince_s=findViewById(R.id.province_spin);

        //
        sidnav=findViewById(R.id.sidnav);
        //

        saverecord=findViewById(R.id.saverecord);
        //

        getfullname=findViewById(R.id.fullname_r);
        getmobilenumber=findViewById(R.id.phonenumber_r);
        getcnic=findViewById(R.id.cnic_r);
        //getcity=findViewById(R.id.city);
        getbillingaddress=findViewById(R.id.billingaddress);
        getshippingaddress=findViewById(R.id.shippingaddress);
        //
        sameaddress=findViewById(R.id.sbss);
        //
        circleImageView=findViewById(R.id.profile_image2);
        //
        provincespinner=findViewById(R.id.province_spinner);
        city_spinner=findViewById(R.id.city_spinner);

    }
    void getdata()
    {
        fullname=getfullname.getText().toString();
        mobilenumber=getmobilenumber.getText().toString();
        cnic=getcnic.getText().toString();
//        city=getcity.getText().toString();
        billingaddress=getbillingaddress.getText().toString();
        shippingaddress=getshippingaddress.getText().toString();
        selected_city=provincespinner.getSelectedItem().toString();
    }
    void update_provinces()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.province_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        provincespinner.setAdapter(adapter);





    }
    void update_cities(String citied)
    {
        Toast.makeText(EditProfileActivity.this,citied,Toast.LENGTH_LONG).show();
        if("punjab".equalsIgnoreCase(citied))
    {
        city_spinner.setEnabled(true);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.punjab_city_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        city_spinner.setAdapter(adapter);
    }
        else if("sindh".equalsIgnoreCase(citied))
        {
            city_spinner.setEnabled(false);
            Toast.makeText(EditProfileActivity.this,"We don't operate in this province",Toast.LENGTH_LONG).show();

           */
/* city_spinner.setEnabled(true);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.punjab_city_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
            city_spinner.setAdapter(adapter);*//*

        }
        else if("Balochistan".equalsIgnoreCase(citied))
        {
            city_spinner.setEnabled(false);
            Toast.makeText(EditProfileActivity.this,"We don't operate in this province",Toast.LENGTH_LONG).show();

           */
/* city_spinner.setEnabled(true);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.punjab_city_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
            city_spinner.setAdapter(adapter);*//*

        }
        else if("khyber pakhtunkhwa".equalsIgnoreCase(citied))
        {
            city_spinner.setEnabled(false);
            Toast.makeText(EditProfileActivity.this,"We don't operate in this province",Toast.LENGTH_LONG).show();

           */
/* city_spinner.setEnabled(true);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.punjab_city_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
            city_spinner.setAdapter(adapter);*//*

        }
    else
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.kashmir_city_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        city_spinner.setAdapter(adapter);
    }
    }

  */
/*  void validaterecord()
    {
        if(fullname.equalsIgnoreCase("")&&mobilenumber.equalsIgnoreCase("")&&cnic.equalsIgnoreCase("")&& city.equalsIgnoreCase("")&&billingaddress.equalsIgnoreCase("")&&shippingaddress.equalsIgnoreCase(""))
        {
            Toast.makeText(EditProfileActivity.this,"Please fill the form",Toast.LENGTH_LONG).show();
        }
        else
        {
            new AsyncFetch().execute();
        }
    }*//*

*/
/*
     System.out.println(fullname+" "+mobilenumber+" "+cnic+" "+city+" "+billingaddress+" "+shippingaddress);

*//*



    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        ProgressDialog progressDialog=new ProgressDialog(EditProfileActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params) {
            try {




                url = new URL("http://staginigserver.perfectmandi.com/adduserinfo.php?customer_name="+fullname+"&&mobile="+mobilenumber+"&&cnic="+cnic+"&&city="+city+"&&Billing="+billingaddress+"&&Shipping="+shippingaddress);

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

            if (result.equalsIgnoreCase("Record insert successfully"))
            {
                Intent intent=new Intent(EditProfileActivity.this,MainActivity.class);
                */
/* session=intent.getStringExtra("session");
        userid=intent.getStringExtra("userid");*//*

                intent.putExtra("session",session);
                intent.putExtra("userid",userid);
                intent.putExtra("name",fullname);
                intent.putExtra("path","");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            else
            {

                Toast.makeText(EditProfileActivity.this,result,Toast.LENGTH_LONG).show();
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


    void validate_record()
    {

        if (fullname.equalsIgnoreCase(""))
        {
            Toast.makeText(EditProfileActivity.this,"Please add your full name",Toast.LENGTH_LONG).show();
        }
        else if (mobilenumber.equalsIgnoreCase(""))
        {
            Toast.makeText(EditProfileActivity.this,"Please add your mobile number",Toast.LENGTH_LONG).show();
        }
        else if (cnic.equalsIgnoreCase(""))
        {
            Toast.makeText(EditProfileActivity.this,"Please add your cnic",Toast.LENGTH_LONG).show();
        }
        else if (billingaddress.equalsIgnoreCase(""))
        {
            Toast.makeText(EditProfileActivity.this,"Please add your billing address",Toast.LENGTH_LONG).show();
        }
        else if (shippingaddress.equalsIgnoreCase(""))
        {
            Toast.makeText(EditProfileActivity.this,"Please add your shipping address",Toast.LENGTH_LONG).show();
        }
        else if (selected_city.equalsIgnoreCase(""))
        {
            Toast.makeText(EditProfileActivity.this,"Please select city",Toast.LENGTH_LONG).show();
        }
        else if (bitmap==null)
        {
            Toast.makeText(EditProfileActivity.this,"Please upload Picture",Toast.LENGTH_LONG).show();
        }
        else
        {
            new AsyncFetch().execute();
        }






    }





}*/
