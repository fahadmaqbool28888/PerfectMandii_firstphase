package com.vendor.perfectmandii.ProfileFragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
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
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vendor.perfectmandii.Model.StoreData.StoreModel;
import com.vendor.perfectmandii.R;
import com.vendor.perfectmandii.profile_Updates.ProfileInformation;

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


public class ContactFragment extends Fragment
{
    String storecperson,storecphone,storesphone,storecphoto;
    boolean check = true;
    EditText contact_person,contact_phone,contact_sphone;
    ImageView viewimage;
    CardView upload_card;
    List<StoreModel> store_list;
    Bitmap bitmap;
    String convert_image;
    String Storename,StorenAbbr,StoreDesc,StoreSAddr,StoreWAddr;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String ImageName = "image_name" ;

    String ImagePath = "image_path" ;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView next_button_contact, previous_button_contact;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;

    public ContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
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
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        intializeWidget(view);
        /*
        *            args.putString("storename", store_name.getText().toString());
           args.putString("sa", store_abbr.getText().toString());
           args.putString("storedesc", store_desc.getText().toString());
           args.putString("ShippingAddress", shipping_address.getText().toString());
           args.putString("WarehouseAddress", warehouse_address.getText().toString());*/

        @SuppressLint("WrongConstant") SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show
        String s1 = sh.getString("storename", "");
        String a = sh.getString("sa", "");

// We can then use the data


        Storename = getArguments().getString("storename");
        StorenAbbr = getArguments().getString("sa");
        StoreDesc = getArguments().getString("storedesc");
        StoreSAddr = getArguments().getString("ShippingAddress");
        StoreWAddr = getArguments().getString("WarehouseAddress");

        Toast.makeText(getContext(), s1+a, Toast.LENGTH_SHORT).show();
       // Toast.makeText(getContext(), Storename+StorenAbbr+StoreDesc+StoreSAddr+StoreWAddr, Toast.LENGTH_SHORT).show();

        viewimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions(getActivity())) {
                    chooseImage(getContext());
                } else {
                    chooseImage(getContext());
                }
            }
        });
        upload_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmptyData();
            }
        });
        next_button_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                EmptyData();
       /*         FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                AddressFragment hallsForState = new AddressFragment();
                transaction.replace(((ViewGroup) getView().getParent()).getId(), hallsForState);
                transaction.addToBackStack(null);
                transaction.commit();
                ProfileInformation profileInformation = (ProfileInformation) getActivity();
                profileInformation.Recieve_Data("Done_2", store_list);*/
            }
        });
        previous_button_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }


    void intializeWidget(View view) {
        next_button_contact = view.findViewById(R.id.next_button_contact);
        previous_button_contact = view.findViewById(R.id.previous_button_contact);
        viewimage = view.findViewById(R.id.viewimage);
        contact_person=view.findViewById(R.id.contact_person);
        contact_phone=view.findViewById(R.id.contact_phone);
        contact_sphone=view.findViewById(R.id.contact_sphone);
        //
        upload_card=view.findViewById(R.id.upload_card);
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
                }
                else if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                }else {
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
                        bitmap = (Bitmap) data.getExtras().get("data");
                        viewimage.setImageBitmap(bitmap);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                  /*      Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
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
                        String   imageEncoded = getRealPathFromURI(getActivity(), selectedImage);
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
    void GetData()
    {

        if(storecphone!=null&&storecphone!=null&&storecphoto!=null)
        {
            saveInPrefrence();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            AttachDFragment hallsForState = new AttachDFragment();
            transaction.replace(((ViewGroup) getView().getParent()).getId(), hallsForState);
            transaction.addToBackStack(null);
            transaction.commit();
            ProfileInformation profileInformation = (ProfileInformation) getActivity();
            profileInformation.Recieve_Data("Done_2", store_list);
        }
    }

    private void saveInPrefrence()
    {
        // Storing data into SharedPreferences
        @SuppressLint("WrongConstant") SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref",MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
      //  storecphone!=null&&storecphone!=null&&storecphoto
// Storing the key and its value as the data fetched from edittext
        myEdit.putString("storecperson", storecperson);
        myEdit.putString("storecphone", storecphone);
        myEdit.putString("storecphoto", storecphoto);

        myEdit.putString("storesphone", storesphone);

// Once the changes have been made,
// we need to commit to apply those changes made,
// otherwise, it will throw an error
        myEdit.commit();
    }


    void EmptyData()
    {
        if (contact_person.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(getContext(),"Enter Contact Person name",Toast.LENGTH_LONG).show();
        }
        else if (contact_phone.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(getContext(),"Enter contact person phone",Toast.LENGTH_LONG).show();
        }
        else if (viewimage.getDrawable() == null)
        {
            Toast.makeText(getContext(),"Enter contact person photo",Toast.LENGTH_LONG).show();
        }
        else
        {

            storecperson=contact_person.getText().toString();
            storecphone=contact_phone.getText().toString();
            storesphone=contact_sphone.getText().toString();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            storecphoto = Base64.encodeToString(byteArray, Base64.DEFAULT);
            GetData();
        }


    }





}