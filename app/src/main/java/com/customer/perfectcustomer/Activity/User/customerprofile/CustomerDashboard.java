package com.customer.perfectcustomer.Activity.User.customerprofile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.customer.perfectcustomer.Activity.Authentication.LoginActivity;
import com.customer.perfectcustomer.Model.DataFish;
import com.customer.perfectcustomer.Model.ProfileModel;
import com.customer.perfectcustomer.R;
import com.customer.perfectcustomer.Activity.Authentication.UpdateProfileActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerDashboard extends AppCompatActivity {

    ImageView editprofile,sidnav_cd;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    TextView getfullname,getmobilenumber,getcnic,getprovince,getcity,getareacode,getfsa,getfba,editprofileaction;
    String session,userid;
    String fullname,mobilenumber,cnic,province,city,areacode,fsa,fba,image_path,status;
  CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);


        Intent getdata=getIntent();
        session=getdata.getStringExtra("session");
        userid=getdata.getStringExtra("userid");
        editprofileaction=findViewById(R.id.editprofileaction);


        if (session==null)
        {
            Intent intent1=new Intent(CustomerDashboard.this, LoginActivity.class);
            startActivity(intent1);
        }
        else
        {
            intializewidget();

            editprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    if (status.equalsIgnoreCase("update"))
                    {
                        Intent intent=new Intent(CustomerDashboard.this, UpdateProfileActivity.class);
                        intent.putExtra("session",session);
                        intent.putExtra("userid",userid);

                        //
                        intent.putExtra("fullname",fullname);
                        intent.putExtra("mobilenumber",mobilenumber);
                        intent.putExtra("cnic",cnic);
                        intent.putExtra("province",province);
                        intent.putExtra("city",city);
                        intent.putExtra("areacode",areacode);
                        intent.putExtra("fsa",fsa);
                        intent.putExtra("fba",fba);
                        intent.putExtra("image_path",image_path);
                        intent.putExtra("status",status);

                        startActivity(intent);
                    }
                    else
                    {
            /*            Intent intent=new Intent(CustomerDashboard.this,EditProfileActivity.class);
                        intent.putExtra("session",session);
                        intent.putExtra("userid",userid);

                        //
                        intent.putExtra("fullname",fullname);
                        intent.putExtra("mobilenumber",mobilenumber);
                        intent.putExtra("cnic",cnic);
                        intent.putExtra("province",province);
                        intent.putExtra("city",city);
                        intent.putExtra("areacode",areacode);
                        intent.putExtra("fsa",fsa);
                        intent.putExtra("fba",fba);
                        intent.putExtra("image_path",image_path);
                        intent.putExtra("status",status);

                        startActivity(intent);*/
                    }


                }
            });




        }

        new AsyncFetch().execute();
        sidnav_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        editprofileaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equalsIgnoreCase("update"))
                {
                    Intent intent=new Intent(CustomerDashboard.this, UpdateProfileActivity.class);
                    intent.putExtra("session",session);
                    intent.putExtra("userid",userid);

                    //
                    intent.putExtra("fullname",fullname);
                    intent.putExtra("mobilenumber",mobilenumber);
                    intent.putExtra("cnic",cnic);
                    intent.putExtra("province",province);
                    intent.putExtra("city",city);
                    intent.putExtra("areacode",areacode);
                    intent.putExtra("fsa",fsa);
                    intent.putExtra("fba",fba);
                    intent.putExtra("image_path",image_path);
                    intent.putExtra("status",status);

                    startActivity(intent);
                }
                else
                {
          /*          Intent intent=new Intent(CustomerDashboard.this,EditProfileActivity.class);
                    intent.putExtra("session",session);
                    intent.putExtra("userid",userid);

                    //
                    intent.putExtra("fullname",fullname);
                    intent.putExtra("mobilenumber",mobilenumber);
                    intent.putExtra("cnic",cnic);
                    intent.putExtra("province",province);
                    intent.putExtra("city",city);
                    intent.putExtra("areacode",areacode);
                    intent.putExtra("fsa",fsa);
                    intent.putExtra("fba",fba);
                    intent.putExtra("image_path",image_path);
                    intent.putExtra("status",status);*/

                    //startActivity(intent);
                }
            }
        });
    }

    private void intializewidget()

    {
        sidnav_cd=findViewById(R.id.sidnav_cd);
        circleImageView=findViewById(R.id.disp);
        //
        editprofile=findViewById(R.id.editprofile);
        //getfullname,getmobilenumber,getcnic,getprovince,getcity,getareacode,getfsa,getfba

        getfullname=findViewById(R.id.getfullname);
        getmobilenumber=findViewById(R.id.getmobilenumber);
        getcnic=findViewById(R.id.getcnic);
        getprovince=findViewById(R.id.getprovince);
        getcity=findViewById(R.id.getcity);
        getareacode=findViewById(R.id.getareacode);
        getfsa=findViewById(R.id.getfsa);
        getfba=findViewById(R.id.getfba);
    }
    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        ProgressDialog progressDialog=new ProgressDialog(CustomerDashboard.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params) {
            try {


                url = new URL("http://staginigserver.perfectmandi.com/getprofile.php?num="+userid);

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
        protected void onPostExecute(String result) {

            if (result.equalsIgnoreCase(""))
            {

            }
            else
            {
                //editprofile.setVisibility(View.INVISIBLE);
                List<DataFish> data=new ArrayList<>();


                try {


                    JSONArray jArray = new JSONArray(result);
                    for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        ProfileModel profileModel=new ProfileModel();
                        profileModel.fullname= json_data.getString("fname");
                        fullname= json_data.getString("fname");
                        profileModel.mobilenumber= json_data.getString("fmobile");
                        mobilenumber= json_data.getString("fmobile");
                        profileModel.cnic= json_data.getString("fcnic");
                        cnic= json_data.getString("fcnic");
                        profileModel.province=json_data.getString("province");
                        province=json_data.getString("province");
                        profileModel.city= json_data.getString("fcity");
                        city= json_data.getString("fcity");
                        profileModel.areacode= json_data.getString("areacode");
                        areacode= json_data.getString("areacode");
                        profileModel.fsa= json_data.getString("fsa");
                        fsa= json_data.getString("fsa");
                        status=json_data.getString("status");


                        profileModel.fba=json_data.getString("fba");
                        fba=json_data.getString("fba");
                        profileModel.image_path=json_data.getString("image_path");
                        image_path=json_data.getString("image_path");
/*String fullname,mobilenumber,cnic,province,city,areacode,fsa,fba;*/
                        getfullname.setText(profileModel.fullname);
                        getmobilenumber.setText(profileModel.mobilenumber);
                        getcnic.setText(profileModel.cnic);
                        getprovince.setText(profileModel.province);
                        getcity.setText(profileModel.city);
                        getareacode.setText(profileModel.areacode);
                        getfsa.setText(profileModel.fsa);
                        getfba.setText(profileModel.fba);


                        if("".equalsIgnoreCase(profileModel.image_path))
                        {
                            Picasso.get().load(R.drawable.optimizedlogo).into(circleImageView);
                        }
                        else
                        {
                            Picasso.get().load(profileModel.image_path).into(circleImageView);
                        }



                        // fishData.usid=username;

                        //data.add(fishData);
                    }

                    progressDialog.dismiss();
                    // Setup and Handover data to recyclerview


                } catch (Exception e) {
                    //  Toast.makeText(Function.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }




        }

    }

}