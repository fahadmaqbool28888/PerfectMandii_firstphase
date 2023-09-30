package com.vendor.perfectmandii.ProfileFragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.os.ParcelFileDescriptor.MODE_APPEND;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vendor.perfectmandii.Activity.User.customerprofile.StoreProfile.AddStoreActivity;
import com.vendor.perfectmandii.Activity.User.customerprofile.StoreProfile.approvalstage.approvalActivity;
import com.vendor.perfectmandii.Dashboard.OPActivity;
import com.vendor.perfectmandii.MainActivity;
import com.vendor.perfectmandii.Model.StoreData.StoreModel;
import com.vendor.perfectmandii.R;
import com.vendor.perfectmandii.bankinformation;
import com.vendor.perfectmandii.profile_Updates.ProfileInformation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
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
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import kotlin.jvm.internal.CollectionToArray;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AttachDFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttachDFragment extends Fragment {
    String ServerUploadPath = "https://sellerportal.perfectmandi.com/add_vendor_mb.php";

    Bitmap cnic_front, cnic_back, cnic_ub;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    boolean setflag, setflag1, setflag2 = false;
    TextView next_button_attachD,frontsidetext,backsidetext,ubtext;
    ImageView cnicfront,cnicfront_1, cnicback,cnicback_1, utilityBill,utilityBill_1;
    public String storename; //1
    public String storeabbr; //2
    public String storedesc; //3
    public String storeWadr; //4
    public String storeSadr; //5
    public String contactperson; //6
    public String contactphone;  //7
    public String profilepic;    //8
    public String cnicfrontside; //9
    public String cnicbackside;  //10
    public String utilitybill;   //11
    public String contactsphone;
    public String storeid;
    public String storesession;
    //
    ProgressDialog progressDialog;
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<StoreModel> store_list;

    public AttachDFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttachDFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttachDFragment newInstance(String param1, String param2) {
        AttachDFragment fragment = new AttachDFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("myKey", "myValue"); // trivial, but for illustration purposes.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attach_d, container, false);
        intializeWidget(view);
        @SuppressLint("WrongConstant") SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show
        String s1 = sh.getString("storecphone", "");
        // int a = sh.getInt("age", 0);

        Toast.makeText(getContext(), s1, Toast.LENGTH_SHORT).show();
        next_button_attachD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEmpty();
              /*  Toast.makeText(getContext(), "Hello Fahad", Toast.LENGTH_SHORT).show();
                ProfileInformation profileInformation=(ProfileInformation) getActivity();
                profileInformation.Recieve_Data("Done_4",store_list);*/
            }
        });
        cnicfront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setflag = true;
                if (checkAndRequestPermissions(getActivity())) {
                    chooseImage(getContext());
                } else {
                    chooseImage(getContext());
                }
            }
        });
        cnicfront_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setflag = true;
                if (checkAndRequestPermissions(getActivity())) {
                    chooseImage(getContext());
                } else {
                    chooseImage(getContext());
                }
            }
        });
        cnicback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setflag1 = true;
                if (checkAndRequestPermissions(getActivity())) {
                    chooseImage(getContext());
                } else {
                    chooseImage(getContext());
                }
            }
        });
        cnicback_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setflag1 = true;
                if (checkAndRequestPermissions(getActivity())) {
                    chooseImage(getContext());
                } else {
                    chooseImage(getContext());
                }
            }
        });
        utilityBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setflag2 = true;
                if (checkAndRequestPermissions(getActivity())) {
                    chooseImage(getContext());
                } else {
                    chooseImage(getContext());
                }
            }
        });
        utilityBill_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setflag2 = true;
                if (checkAndRequestPermissions(getActivity())) {
                    chooseImage(getContext());
                } else {
                    chooseImage(getContext());
                }
            }
        });

        return view;
    }

    private void intializeWidget(View view) {
        cnicfront = view.findViewById(R.id.cnic_front_side);
        cnicfront_1=view.findViewById(R.id.cnic_front_side_1);
        cnicback = view.findViewById(R.id.cnic_back_side);
        cnicback_1 = view.findViewById(R.id.cnic_back_side_1);
        utilityBill = view.findViewById(R.id.utility_bill);
        utilityBill_1 = view.findViewById(R.id.utility_bill_1);
        next_button_attachD = view.findViewById(R.id.next_button_attachD);
        frontsidetext = view.findViewById(R.id.frontsidetext);
                backsidetext= view.findViewById(R.id.backsidetext);
        ubtext= view.findViewById(R.id.ubtext);
    }

    void CheckEmpty() {
        if (cnicfront.getDrawable() == null) {
            Toast.makeText(getContext(), "Upload Front side", Toast.LENGTH_LONG).show();
        } else if (cnicback.getDrawable() == null) {
            Toast.makeText(getContext(), "Upload Back side", Toast.LENGTH_LONG).show();
        } else if (utilityBill.getDrawable() == null) {
            Toast.makeText(getContext(), "Upload Utility Bill", Toast.LENGTH_LONG).show();
        } else {
            GetData();
         /*   Toast.makeText(getContext(), "Hello Fahad", Toast.LENGTH_SHORT).show();
            ProfileInformation profileInformation=(ProfileInformation) getActivity();
            profileInformation.Recieve_Data("Done_3",store_list);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            bankinformation hallsForState = new bankinformation();
            transaction.replace(((ViewGroup)getView().getParent()).getId(), hallsForState);
            transaction.addToBackStack(null);
            transaction.commit();*/


        }

    }

    /*
    * public String storename; //1
public String storeabbr; //2
public String storedesc; //3
public String storeWadr; //4
public String storeSadr; //5
public String contactperson; //6
public String contactphone;  //7
public String profilepic;    //8
public String cnicfrontside; //9
public String cnicbackside;  //10
public String utilitybill;   //11*/

    private void GetData() {
        if (cnic_front != null && cnic_back != null && cnic_ub != null) {
            @SuppressLint("WrongConstant") SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_APPEND);


            storename = sh.getString("storename", ""); //1
            storeabbr = sh.getString("sa", ""); //2
            storedesc = sh.getString("storedesc", ""); //3
            storeWadr = sh.getString("ShippingAddress", ""); //4
            storeSadr = sh.getString("WarehouseAddress", ""); //5
            contactperson = sh.getString("storecperson", ""); //6
            contactphone = sh.getString("storecphone","" ); //7
            profilepic = sh.getString("storecphoto", "");  //8
            contactsphone=sh.getString("storesphone","");
            storeid=sh.getString("userid","");
            storesession=sh.getString("session","");
            /*
            * myEdit.putString("userid", userid);
        myEdit.putString("name", userid);
        myEdit.putString("session", session);*/

            cnicfrontside=convert_Image(cnic_front);
            cnicbackside=convert_Image(cnic_back);
            utilitybill=convert_Image(cnic_ub);


            if (storename != null && storeabbr != null && storedesc != null && storeWadr != null && storeSadr != null && contactperson != null && contactphone != null && profilepic != null) {
                System.out.println(storeid+" "+storesession+" "+storename+" "+storeabbr+" "+storedesc+" "+storeWadr+" "+storeSadr+" "+contactperson+" "+contactphone);
               //
            /*    HashMapParams.put(Profile_Pic, profilepic);

                HashMapParams.put(Cnic_Front, cnicfrontside);

                HashMapParams.put(Cnic_Back, cnicbackside);
                HashMapParams.put(Utility_Bill, utilitybill);*/
                if (profilepic!=null)
                {
                    if (cnicfrontside!=null)
                    {
                        if (cnicbackside!=null)
                        {
                            if (utilitybill!=null)
                            {

                                if (storeid!=null)
                                {
                                    System.out.println(storeid);

                                    ImageUploadToServerFunction();
                                }
                                else
                                {
                                    Toast.makeText(getContext(),"Store Id is null",Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(),"Cnic back side is null",Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(),"Cnic back side is null",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(),"Cnic Front side is null",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(),"Profile pic is null",Toast.LENGTH_LONG).show();

                }



             //   03266994555 WedFeb02171659GMT05002022 fahad FA faaha faha faha contact_person 03245994806
            }
            else
            {
                Toast.makeText(getContext(),"Hello I am ",Toast.LENGTH_LONG).show();
            }

            // Toast.makeText(getContext(),s1+a, Toast.LENGTH_LONG).show();
        }
    }

    private void chooseImage(Context context) {
      //  final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array

        final CharSequence[] optionsMenu = {"Take Photo", "Exit"}; // create a menuOption Array// create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (optionsMenu[i].equals("Take Photo")) {
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } /*else if (optionsMenu[i].equals("Choose from Gallery")) {
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

    String convert_Image(Bitmap bitmap) {
        String image = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return image;
    }

    // Handled permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
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
                } else {
                    chooseImage(getContext());
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
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        if (setflag == true) {

                            cnic_front = selectedImage;
                            cnicfront.setImageBitmap(selectedImage);
                            frontsidetext.setVisibility(View.GONE);
                            //cnicfrontside = convert_Image(cnic_front);
                            setflag = false;
                        } else if (setflag1 == true) {
                            cnicback.setImageBitmap(selectedImage);
                            cnic_back = selectedImage;
                            backsidetext.setVisibility(View.GONE);
                            //cnicbackside = convert_Image(cnic_back);
                            setflag1 = false;
                        } else {
                            cnic_ub = selectedImage;
                            utilityBill.setImageBitmap(selectedImage);
                            utilityBill_1.setVisibility(View.GONE);
                            ubtext.setVisibility(View.GONE);
                            //utilitybill = convert_Image(cnic_ub);
                            setflag2 = false;
                        }
                        //  viewimage.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null)
                    {
                        if (setflag==true)
                        {

                            Uri selectedImage = data.getData();

                            String   imageEncoded = getRealPathFromURI(getActivity(), selectedImage);
                             cnic_front = BitmapFactory.decodeFile(imageEncoded);
                            int nh = (int) ( cnic_front.getHeight() * (512.0 / cnic_front.getWidth()));
                            Bitmap scaled = Bitmap.createScaledBitmap(cnic_front, 512, nh, true);
                             cnicfront_1.setVisibility(View.GONE);
                            frontsidetext.setVisibility(View.GONE);
                            // cnic_front=compressBitmap(cnic_front);
                           //  cnicfront.setImageResource(0);
                            cnic_front=scaled;
                            cnicfront.setImageBitmap(cnic_front);
                           // cnicfrontside = convert_Image(cnic_front);
                            setflag = false;
                        }
                       else if (setflag1==true)
                        {

                            Uri selectedImage = data.getData();

                            String   imageEncoded = getRealPathFromURI(getActivity(), selectedImage);
                            cnic_back = BitmapFactory.decodeFile(imageEncoded);
                            //cnic_back=compressBitmap(cnic_back);

                            int nh = (int) ( cnic_back.getHeight() * (512.0 / cnic_back.getWidth()));
                            Bitmap scaled = Bitmap.createScaledBitmap(cnic_back, 512, nh, true);
                            //cnic_back.compress(Bitmap.CompressFormat.PNG, 10, out);
                            cnicback_1.setVisibility(View.GONE);
                            backsidetext.setVisibility(View.GONE);
                            cnicback.setImageResource(android.R.color.transparent);
                           // cnicback.setImageResource(0);
                            cnic_back=scaled;
                            cnicback.setImageBitmap(cnic_back);

                            // cnicfrontside = convert_Image(cnic_front);
                            setflag1 = false;
                        }
                       else
                        {
                            Uri selectedImage = data.getData();

                            String   imageEncoded = getRealPathFromURI(getActivity(), selectedImage);
                            cnic_ub = BitmapFactory.decodeFile(imageEncoded);
                            ubtext.setVisibility(View.GONE);
                            int nh = (int) ( cnic_ub.getHeight() * (512.0 / cnic_ub.getWidth()));
                            Bitmap scaled = Bitmap.createScaledBitmap(cnic_ub, 512, nh, true);
                          // cnic_ub=compressBitmap(cnic_ub);
                          // cnic_ub=compressBitmap(cnic_ub);
                           // utilityBill.setImageResource(0);
                            cnic_ub=scaled;
                            utilityBill.setImageBitmap(cnic_ub);
                            utilityBill_1.setVisibility(View.GONE);
                            // cnicfrontside = convert_Image(cnic_front);
                            setflag2 = false;
                        }


                    }
                    break;
            }
        }
    }
    Bitmap compressBitmap(Bitmap bitmap_OR)
    {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap_OR.compress(Bitmap.CompressFormat.PNG, 100, out);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        return decoded;
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

        String mImageName = "IMG_" + String.valueOf(System.currentTimeMillis()) + ".png";
        return mediaStorageDir.getAbsolutePath() + "/" + mImageName;

    }
    public void ImageUploadToServerFunction(){



        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(getContext(),"\"Documents are uplodaing","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);
                // Toast.makeText(AddStoreActivity.this,string1,Toast.LENGTH_SHORT).show();



                if ("Store Created".equalsIgnoreCase(string1))
                {
                  //  Toast.makeText(getContext(),string1,Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    System.out.println(string1);

                    Intent intent=new Intent(getContext(), OPActivity.class);
                    intent.putExtra("userid",storeid);
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
                }



            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();







                HashMapParams.put(Profile_Pic, profilepic);

                HashMapParams.put(Cnic_Front, cnicfrontside);

                HashMapParams.put(Cnic_Back, cnicbackside);
                HashMapParams.put(Utility_Bill, utilitybill);
                HashMapParams.put(contact_person, contactperson); //fullname
                HashMapParams.put(store_name, storename);
                HashMapParams.put(store_abbr, storeabbr);
                HashMapParams.put(store_desc,storedesc);
                HashMapParams.put(Mobilenumber, contactphone); //mobile Number
                HashMapParams.put(Mobilenumber1, contactsphone);
                HashMapParams.put(store_id, storeid);
                HashMapParams.put(store_session, storesession);
                HashMapParams.put(Billingaddress, storeWadr); //BA
                HashMapParams.put(Shippingaddress, storeSadr); //SA


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