package com.consumer.perfectmandii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {
    String fullname_u,mobilenumber_u,cnic_u,province_u,city_u,areacode_u,fsa_u,fba_u,image_path_u,status_u;
    String session,userid,username,shippingaddress,billingaddress;

    String shippingaddress_u,billingaddress_u;
    TextView fullname,mobilenumber,cnic,province,city;
    //
    CircleImageView circleImageView;
    //
    CardView updaterecord;
    //
    ImageView sidnav;
//
public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    //
    CheckBox sameaddress;
    EditText getbillingaddress,getshippingaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        Intent getdata=getIntent();
        session=getdata.getStringExtra("session");
        userid=getdata.getStringExtra("userid");
        username=getdata.getStringExtra("username");



        fullname_u=getdata.getStringExtra("fullname");
        mobilenumber_u=getdata.getStringExtra("mobilenumber");
        cnic_u=getdata.getStringExtra("cnic");
        province_u=getdata.getStringExtra("province");
        city_u=getdata.getStringExtra("city");
        areacode_u=getdata.getStringExtra("areacode");
        fsa_u=getdata.getStringExtra("fsa");
        fba_u=getdata.getStringExtra("fba");
        image_path_u=getdata.getStringExtra("image_path");
        status_u=getdata.getStringExtra("status");




        //
        IntialWidget();
        //
        allocatedate();
//
        enableWidget();

        //
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

        updaterecord.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getuPdate();
                new AsyncFetch().execute();
            }
        });


        sidnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }







    void IntialWidget()
    {

        sidnav=findViewById(R.id.sidnav_up);
        //
        fullname=findViewById(R.id.fullname_up);
        mobilenumber=findViewById(R.id.mobilenumber_up);
        cnic=findViewById(R.id.cnic_up);
        province=findViewById(R.id.province_up);
        city=findViewById(R.id.city_up);
        circleImageView=findViewById(R.id.profile_image_Up);
        getbillingaddress=findViewById(R.id.billingaddress_up);
        getshippingaddress=findViewById(R.id.shippingaddress_up);
        sameaddress=findViewById(R.id.sbss_up);

        updaterecord=findViewById(R.id.Updaterecord);



    }

    void getuPdate()
    {
        shippingaddress_u=getshippingaddress.getText().toString();
        billingaddress_u=getbillingaddress.getText().toString();

    }

    private void allocatedate()
    {
        fullname.setText("Full Name: "+fullname_u);
        mobilenumber.setText("Mobile Number: "+mobilenumber_u);
        cnic.setText("CNIC: "+cnic_u);
        province.setText("Province: "+province_u);
        city.setText("City: "+city_u);
        //

        if ("".equalsIgnoreCase(image_path_u))
        {
            Picasso.get().load(R.drawable.optimizedlogo).into(circleImageView);
        }
        else
        {
            Picasso.get().load(image_path_u).into(circleImageView);
        }






        getshippingaddress.setText(fsa_u);
        getbillingaddress.setText(fba_u);


    }
    void enableWidget()
    {
        circleImageView.setEnabled(false);
    }
    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        ProgressDialog progressDialog=new ProgressDialog(UpdateProfileActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params) {
            try {


                String urls="https://staginigserver.perfectmandi.com/RecordUp.php?id="+mobilenumber_u+"&fsa="+shippingaddress_u+"&fba="+billingaddress_u;
                System.out.println(urls);
                url = new URL("https://staginigserver.perfectmandi.com/RecordUp.php?id="+mobilenumber_u+"&fsa="+shippingaddress_u+"&fba="+billingaddress_u);

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

            if (result.equalsIgnoreCase("unsuccessful"))
            {
                Toast.makeText(UpdateProfileActivity.this,"Please try again",Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
            }
            else
            {
                Toast.makeText(UpdateProfileActivity.this,result,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                finish();
            }




        }

    }
}