package com.vendor.perfectmandii.Activity.User.customerprofile.StoreProfile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vendor.perfectmandii.Activity.User.customerprofile.StoreProfile.approvalstage.approvalActivity;
import com.vendor.perfectmandii.Activity.vendor.AddProductActivity;
import com.vendor.perfectmandii.LoginActivity;
import com.vendor.perfectmandii.R;

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
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddStoreActivity extends AppCompatActivity
{
    EditText getfullname, getmobilenumber, getcnic,getshippingaddress, getbillingaddress,getstorename;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    CheckBox sameaddress;
    CardView saverecord;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;

    CircleImageView circleImageView;
    String fullname, mobilenumber, name, shippingaddress, billingaddress, session, userid, username,store, usernumber,provincev,getContactperson;


    ProgressDialog progressDialog;

    Bitmap bitmap;


    String ImagePath = "image_path";
    String Mobilenumber = "mobile";
    String contactperson="contact_person";
    String Billingaddress = "Billing";
    String Shippingaddress = "Shipping";
    String storename="Store_name";
    String user="userid";
    String usession="session";




    String ServerUploadPath = "https://staginigserver.perfectmandi.com/addStoreinfo.php";

    String ConvertImage;



    boolean check = true;

    ImageView sidnav;

    String save_session="save_session";


    LinearLayout city_s, sprovince_s;

    TextView fulk;



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
    String avatar_image="https://staginigserver.perfectmandi.com/images/avatar/qwerty.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);


        Intent getdata = getIntent();

        userid=getdata.getStringExtra("userid");
        name=getdata.getStringExtra("username");
        session=createsession();
        Toast.makeText(AddStoreActivity.this,userid,Toast.LENGTH_LONG).show();






        if(session==null)
        {
            Intent intent1=new Intent(AddStoreActivity.this, LoginActivity.class);
            startActivity(intent1);
        }
        else {

        }

            intializewidget();


        Glide
                .with(this)
                .load(avatar_image)
                .centerCrop()
                .into(circleImageView);



            //







            getmobilenumber.setText(userid);
            getfullname.setText(name);

            getdata();













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
        circleImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
             /*   Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);
*/
                selectImage();


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


   /*         sidnav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });






     */
   private void selectImage() {
       try {
           PackageManager pm = getPackageManager();
           int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
           if (hasPerm == PackageManager.PERMISSION_GRANTED) {
               final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
               AlertDialog.Builder builder = new AlertDialog.Builder(AddStoreActivity.this);
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
                Bitmap locbitmap = BitmapFactory.decodeStream(imageStream);
                //Bitmap locbitmap = (Bitmap) data.getExtras().get("data");
                bitmap=getResizedBitmap(locbitmap,150);
                //circleImageView.setImageBitmap(bitmap);
                circleImageView.setImageBitmap(bitmap);
                imagestring(bitmap);
             //   uploadpic(selectedImage);
                // image_view.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AddStoreActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }
        else if (reqCode == PICK_IMAGE_CAMERA)
        {
            //Uri selectedImage = data.getData();\
            try
            {

               Bitmap locbitmap = (Bitmap) data.getExtras().get("data");
               bitmap=getResizedBitmap(locbitmap,150);
                circleImageView.setImageBitmap(bitmap);

                imagestring(bitmap);
             //   uploadpic(photo);
                //   imageView.setImageBitmap(photo);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(AddStoreActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

            //uploadpic(photo);
        }

    }

    void intializewidget()
    {




        saverecord=findViewById(R.id.saverecord_store);
        //

        getstorename=findViewById(R.id.storename_store);
        //

        getfullname=findViewById(R.id.fullname_store);
        getmobilenumber=findViewById(R.id.phonenumber_store);

        getbillingaddress=findViewById(R.id.billingaddress_store);
        getshippingaddress=findViewById(R.id.shippingaddress_store);
        //
        sameaddress=findViewById(R.id.sbss_store);
        //
        circleImageView=findViewById(R.id.profile_image2_store);
        //


    }
    void getdata()
    {



        store=getstorename.getText().toString();
        getContactperson=getfullname.getText().toString();

        billingaddress=getbillingaddress.getText().toString();
        shippingaddress=getshippingaddress.getText().toString();
        mobilenumber=getmobilenumber.getText().toString();
    }




    void validaterecord()
    {
        if(bitmap==null)
        {
            circleImageView.requestFocus();
            Toast.makeText(AddStoreActivity.this,"Please upload Picture",Toast.LENGTH_LONG).show();
        }
        else if ("".equalsIgnoreCase(store))
        {
            Toast.makeText(AddStoreActivity.this,"Please enter store name",Toast.LENGTH_LONG).show();
        }
        else if("".equalsIgnoreCase(billingaddress))
        {
            Toast.makeText(AddStoreActivity.this,"Please enter store billing address",Toast.LENGTH_LONG).show();
        }
        else if("".equalsIgnoreCase(shippingaddress))
        {
            Toast.makeText(AddStoreActivity.this,"Please enter store shipping address",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(AddStoreActivity.this,"Poor",Toast.LENGTH_SHORT).show();

                        break;
                    case 2:
                        Toast.makeText(AddStoreActivity.this,"Moderate",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(AddStoreActivity.this,"Good",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        ImageUploadToServerFunction();
                        break;

                    default:
                        Toast.makeText(AddStoreActivity.this,"Unknown",Toast.LENGTH_SHORT).show();
                        break;





                }




            }
            else
            {
                boolean as=isNetworkConnected();
                Toast.makeText(AddStoreActivity.this,String.valueOf(as),Toast.LENGTH_SHORT).show();
            }
            //
        }
    }

    void validateRecord()
    {

        if(store.equalsIgnoreCase("")||getContactperson.equalsIgnoreCase("")||billingaddress.equalsIgnoreCase("")||shippingaddress.equalsIgnoreCase("")||mobilenumber.equalsIgnoreCase("")||ConvertImage.equalsIgnoreCase(""))
        {

        }
        else
        {

        }
    }



    String imagestring(Bitmap bitmap)
    {
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
        return ConvertImage;
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



        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(AddStoreActivity.this,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);
               // Toast.makeText(AddStoreActivity.this,string1,Toast.LENGTH_SHORT).show();



                if ("Store Created".equalsIgnoreCase(string1))
                {
                    progressDialog.dismiss();
                    Intent intent=new Intent(AddStoreActivity.this, approvalActivity.class);
                    intent.putExtra("userid",userid);
                    intent.putExtra("session",session);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();



                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(AddStoreActivity.this,string1,Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();





                HashMapParams.put(ImagePath,


                        ConvertImage);
                HashMapParams.put(contactperson, getContactperson); //fullname
                HashMapParams.put(storename,store);
                HashMapParams.put(Mobilenumber, mobilenumber); //mobile Number
                HashMapParams.put(user,userid);
                HashMapParams.put(usession,createsession());

                HashMapParams.put(Billingaddress, billingaddress); //BA
                HashMapParams.put(Shippingaddress, shippingaddress); //SA


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


}