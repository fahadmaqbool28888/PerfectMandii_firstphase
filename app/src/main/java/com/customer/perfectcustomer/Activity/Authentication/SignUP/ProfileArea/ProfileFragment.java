package com.customer.perfectcustomer.Activity.Authentication.SignUP.ProfileArea;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.customer.perfectcustomer.Activity.Authentication.ProfileInformationActivity;
import com.customer.perfectcustomer.Activity.Home.ProductScreen.DisplayProduct.ProductProfileActivity;
import com.customer.perfectcustomer.Model.Customer.UserModel;

import com.customer.perfectcustomer.MainActivity_OP;
import com.customer.perfectcustomer.Model.Product_Before_Login;
import com.customer.perfectcustomer.R;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class ProfileFragment extends Fragment
{
    String ServerUploadPath = "https://sellerportal.perfectmandi.com/createcustomer.php";
    String Profile_Pic = "profile_pic";
    String Cnic_Front="cnic_front";
    String Cnic_Back="cnic_back",Utility_Bill="utility_bill";

    String Buisness_Name="Buisness_Name";
    String Primary_Phone="Primary_Phone";
    String primary_address_1="primary_address",secondary_address_1="secondary_address",Name="Name";

    String Secondary_Phone="Secondary_Phone";
    String Pin="Pin";
    String Cniccustomer="cnic_customer";
    String OTP="OTP";
    String PROVICE="province";
    String CITY="city";
    Spinner spinner;

    ConstraintLayout uploadcustomer_pic;

    EditText buisness_name,primary_phone,secondary_phone,primary_address,secondary_address;
    String otp,storecphoto,buisnessname,primaryphone,secondaryphone,primaryaddress,secondaryaddress,name,phone,pin,cniccustomer;
    //
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    Bitmap bitmap;
    ConstraintLayout uploadpic;
    ImageView customer_pic;
    int keyDel = 0;
    HttpURLConnection conn;
    URL url = null;
    Spinner province_spinner,city_spinner_1;
    EditText cnic_customer;

    CardView submit_12;
    List<String> provinces,cities;
    CheckBox same_Asabove;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String code;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<String> province;
    List<String> city;
    HashMap<String, JSONArray> capitalCities;
    String select_province,select_city;
    public ProfileFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
       View view =inflater.inflate(R.layout.fragment_profile, container, false);

       uploadcustomer_pic=view.findViewById(R.id.uploadcustomer_pic);
        capitalCities = new HashMap<String, JSONArray>();
 /*      sqLiteHelper=new SQLiteHelper(getContext());*/
        name = getArguments().getString("name");

        phone = getArguments().getString("phone");

        otp=getArguments().getString("otp");
        code=getArguments().getString("code");
      //  sqLiteHelper.addNewOTP(otp);



        pin = getArguments().getString("pin");

        province=new ArrayList<>();
        cities=new ArrayList<>();
       init_widget(view);

       new AsyncFetch().execute();

        primary_phone.setText(phone);
        primary_phone.setEnabled(false);


        cnic_customer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                cnic_customer.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if (i == KeyEvent.KEYCODE_DEL)
                            keyDel = 1;
                        return false;
                    }
                });

                if (keyDel == 0) {
                    int len = cnic_customer.getText().length();
                    if(len == 5 || len == 13) {
                        cnic_customer.setText(cnic_customer.getText() + "-");
                        cnic_customer.setSelection(cnic_customer.getText().length());
                    }
                } else {
                    keyDel = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        uploadpic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (checkAndRequestPermissions(getActivity())) {
                    chooseImage(getContext());
                } else {
                    chooseImage(getContext());
                }
            }
        });

        submit_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checkEmpty();
            }
        });

        same_Asabove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondary_address.setText(primary_address.getText().toString());
            }
        });


       return view;
    }


    void checkEmpty()
    {
        if (buisness_name.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(getContext(),"Buisness Name is empty",Toast.LENGTH_LONG).show();
        }
        else  if (cnic_customer.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(getContext(),"CNIC Number is must",Toast.LENGTH_LONG).show();
        }
        else  if (primary_address.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(getContext(),"Please enter shop address",Toast.LENGTH_LONG).show();
        }
        else  if (secondary_address.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(getContext(),"Please enter return address",Toast.LENGTH_LONG).show();
        }
        else if (bitmap==null)
        {
            Toast.makeText(getContext(),"Please take pic",Toast.LENGTH_LONG).show();
        }
        else if (secondaryphone==null)
        {
            secondary_phone.setText("03");
            getData();
        }
        else
        {
            getData();
        }

    }
    void init_widget(View view)
    {
        same_Asabove=view.findViewById(R.id.same_Asabove);
        submit_12=view.findViewById(R.id.submit_12);
        cnic_customer=view.findViewById(R.id.cnic_customer);
        uploadpic=view.findViewById(R.id.uploadcustomer_pic);
        customer_pic=view.findViewById(R.id.customer_pic);
        //
        buisness_name=view.findViewById(R.id.buisness_name);
        primary_phone=view.findViewById(R.id.primary_phone);
        secondary_phone=view.findViewById(R.id.secondary_phone);
        primary_address=view.findViewById(R.id.primary_address);
        secondary_address=view.findViewById(R.id.secondary_address);

        spinner=view.findViewById(R.id.province_spinner_1);
        city_spinner_1=view.findViewById(R.id.city_spinner_1);
    }

    void getData()
    {
        buisnessname=buisness_name.getText().toString();
        primaryphone=primary_phone.getText().toString();
        secondaryphone=secondary_phone.getText().toString();
        primaryaddress=primary_address.getText().toString();
        secondaryaddress=secondary_address.getText().toString();
        storecphoto=convertImage(bitmap);
        cniccustomer=cnic_customer.getText().toString();




   if (primaryphone.equalsIgnoreCase(secondaryphone))
   {
       Toast.makeText(getContext(),"Seconday Phone number is match above",Toast.LENGTH_LONG).show();
   }
   else if (secondaryphone.equalsIgnoreCase(""))
   {

       if (otp.equalsIgnoreCase(""))
       {

       }
       else
       {


           FragmentManager fragmentManager = getFragmentManager();
           FragmentTransaction transaction = fragmentManager.beginTransaction();
           AttachDocument hallsForState = new AttachDocument();
           Bundle args = new Bundle();
           args.putString("name", name);
           args.putString("phone",phone);
           args.putString("pin",pin);

           System.out.println("otp"+otp);
           args.putString("otp",otp);
           args.putString("buisnessname", buisnessname);
           args.putString("cniccustomer",cniccustomer);
           args.putString("primaryphone",primaryphone);
           args.putString("secondaryphone",secondaryphone);
           args.putString("primaryaddress",primaryaddress);
           args.putString("secondaryaddress",secondaryaddress);
           args.putString("storecphoto",storecphoto);
           args.putString("code",code);
           hallsForState.setArguments(args);
           transaction.replace(((ViewGroup)getView().getParent()).getId(), hallsForState);
           transaction.addToBackStack(null);
           transaction.commit();
           ProfileInformationActivity profileInformation = (ProfileInformationActivity) getActivity();
           profileInformation.Recieve_Data("Done_2");
       }
   }
   else
   {

       FragmentManager fragmentManager = getFragmentManager();
       FragmentTransaction transaction = fragmentManager.beginTransaction();
       AttachDocument hallsForState = new AttachDocument();
       Bundle args = new Bundle();
       args.putString("name", name);
       args.putString("phone",phone);
       args.putString("pin",pin);
       args.putString("otp",otp);
       args.putString("buisnessname", buisnessname);
       args.putString("cniccustomer",cniccustomer);
       args.putString("primaryphone",primaryphone);
       args.putString("secondaryphone",secondaryphone);
       args.putString("primaryaddress",primaryaddress);
       args.putString("secondaryaddress",secondaryaddress);
       args.putString("storecphoto",storecphoto);
       args.putString("province",select_province);
       args.putString("city",select_city);
       hallsForState.setArguments(args);
       transaction.replace(((ViewGroup)getView().getParent()).getId(), hallsForState);
       transaction.addToBackStack(null);
       transaction.commit();
       ProfileInformationActivity profileInformation = (ProfileInformationActivity) getActivity();
       profileInformation.Recieve_Data("Done_2");
   }

    }
    String convertImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        storecphoto = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return storecphoto;
    }
    private void chooseImage(Context context) {
        //final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array
        final CharSequence[] optionsMenu = {"Take Photo", "Exit"};
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (optionsMenu[i].equals("Take Photo"))
                {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if (optionsMenu[i].equals("Exit"))
                {
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
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(),
                                "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                        .show();
            } else if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(),
                        "FlagUp Requires Access to Your Storage.",
                        Toast.LENGTH_SHORT).show();
            } else if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(),
                        "FlagUp Requires Access to Your Storage.",
                        Toast.LENGTH_SHORT).show();
            } else {
                chooseImage(getContext());
            }
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
                       customer_pic.setImageBitmap(bitmap);
                       uploadcustomer_pic.setBackgroundColor(Color.TRANSPARENT);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null)
                    {
                        Uri selectedImage = data.getData();
                        String   imageEncoded = getRealPathFromURI(getActivity(), selectedImage);
                        bitmap = BitmapFactory.decodeFile(imageEncoded);

                        customer_pic.setImageBitmap(bitmap);
                        uploadcustomer_pic.setBackgroundColor(Color.TRANSPARENT);
                        Glide.with(this).load(bitmap).into(customer_pic);
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
                InputStream iStream = context.getContentResolver().openInputStream(contentUri);
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

    private String getFilename(Context context) {
        File mediaStorageDir = new File(context.getExternalFilesDir(""), "patient_data");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }

        String mImageName = "IMG_" + System.currentTimeMillis() + ".png";
        return mediaStorageDir.getAbsolutePath() + "/" + mImageName;

    }
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private class AsyncFetch extends AsyncTask<String, String, String>
    {
        HttpURLConnection conn;
        URL url = null;

        ProgressDialog progressDialog=new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                url = new URL("https://sellerportal.perfectmandi.com/get_regiondetail.php");
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

                try
                {
                    spinner.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    JSONArray jArray = new JSONArray(result);

                    province.add("Select Province");
                    for (int i=0;i<jArray.length();i++)
                    {

                        JSONObject json_data = jArray.getJSONObject(i);


                        String sr=json_data.getString("province_name");


                        province.add(sr);

                        capitalCities.put(sr,json_data.getJSONArray("cities"));
                    }
                    pupulate_parentcategory();
           /*         getMeasurement(jsonArray1);
                    getcategory(jsonArray_dealin);
                    getcolor(jsonArray_color);*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
        void pupulate_parentcategory()
        {
            //spinner.setVisibility(View.GONE);
           // String [] values =
                  //  {"Time at Residence","Under 6 months","6-12 months","1-2 years","2-4 years","4-8 years","8-15 years","Over 15 years",};
            //  Spinner spinner = (Spinner) v.findViewById(R.id.spinner1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, province);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    String text = spinner.getSelectedItem().toString();
                    try {
                        if (text.equalsIgnoreCase("Select Province"))
                        {

                        }
                        else
                        {
                            select_province=text;
                            city_province(text);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                public void onNothingSelected(AdapterView<?> parent)
                {
                }
            });
        }


        void city_province(String as) throws JSONException
        {
            if (cities.size()>1)
            {
                cities.clear();
            }


            cities.add("Select city");
            if (as!=null)
            {
                JSONArray jsonArray=capitalCities.get(as);
                for (int j=0;j<jsonArray.length();j++)
                {
                    JSONObject json_data = jsonArray.getJSONObject(j);
                    String city=json_data.getString("city_name");

                    cities.add(city);
                }
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cities);
                adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                city_spinner_1.setAdapter(adapter1);
            }



            city_spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    String text = city_spinner_1.getSelectedItem().toString();

                        if (text.equalsIgnoreCase("Select city"))
                        {

                        }
                        else
                        {

                            select_city=text;
                            //city_province(text);
                        }


                }
                public void onNothingSelected(AdapterView<?> parent)
                {
                }
            });



        }


    }





    //
    public void ImageUploadToServerFunction(){



        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String>
        {
            ProgressDialog progressDialog;

            String code;

            ArrayList<Product_Before_Login> codearray;

            void showDialog()
            {
                new AlertDialog.Builder(getContext())
                        .setTitle("title")
                        .setMessage("message")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
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

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(getContext(),"\"Documents are uplodaing","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);
                 Toast.makeText(getContext(),string1,Toast.LENGTH_SHORT).show();

         /*       get_Data_3();*/
                progressDialog.dismiss();
                System.out.println(string1);



                if ("profile not been created".equalsIgnoreCase(string1))
                {
                    //  Toast.makeText(getContext(),string1,Toast.LENGTH_SHORT).show();



                    Intent intent=new Intent(getContext(), MainActivity_OP.class);

                    intent.putExtra("session",createsession());

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();


//

                    System.out.println(string1);


                }
                if ("".equalsIgnoreCase(string1))
                {
                    //  Toast.makeText(getContext(),string1,Toast.LENGTH_SHORT).show();




                    Toast.makeText(getContext(),"Server Busy Please try again",Toast.LENGTH_SHORT).show();

                }
                else
                {

                    try {
                        JSONArray jArray = new JSONArray(string1);
                        for(int i=0;i<jArray.length();i++)
                        {
                            JSONObject json_data = jArray.getJSONObject(i);
                            UserModel userModel =new UserModel();
                            userModel.accountid=json_data.getString("account_id");
                            userModel.Name=json_data.getString("Name");
                            userModel.contact=json_data.getString("contact");
                            userModel.profilepic=json_data.getString("ProfilePic");
                            userModel.shop=json_data.getString("shop");
                            userModel.city=json_data.getString("city");
                            userModel.session=createsession();
                           // sqLiteHelper.addNewCustomer(userModel.accountid,userModel.Name,userModel.contact,userModel.session,userModel.shop,userModel.profilepic,userModel.city);







                            if (code!=null)
                            {
                                Intent intent=new Intent(getContext(), ProductProfileActivity.class);
                                intent.putExtra("code",code);
                                intent.putExtra("new","yes");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent=new Intent(getContext(), MainActivity_OP.class);
                                intent.putExtra("new","yes");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(intent);
                            }
                        }
                    }
                    catch (Exception exception)
                    {

                    }



                }



            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();
                HashMapParams.put(Profile_Pic, storecphoto);
                HashMapParams.put(Cnic_Front, "");
                HashMapParams.put(Cnic_Back, "");
                HashMapParams.put(Utility_Bill, "");
                HashMapParams.put(Buisness_Name, buisnessname); //fullname
                HashMapParams.put(Name, name);
                HashMapParams.put(secondary_address_1,secondaryaddress);
                HashMapParams.put(primary_address_1, primaryaddress); //mobile Number
                HashMapParams.put(Primary_Phone, primaryphone);
                HashMapParams.put(Secondary_Phone, secondaryphone);
                HashMapParams.put(Pin, pin);
                HashMapParams.put(OTP, otp);
                HashMapParams.put(Cniccustomer, cniccustomer);
                HashMapParams.put(PROVICE, select_province);
                HashMapParams.put(CITY, select_city);

                //BA

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

                        new OutputStreamWriter(OutPutStream, StandardCharsets.UTF_8));

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

            } catch (SocketException ex) {
                ex.printStackTrace();
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