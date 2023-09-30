package com.vendor.perfectmandii;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;

import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.poovam.pinedittextfield.CirclePinField;
import com.poovam.pinedittextfield.PinField;
import com.vendor.perfectmandii.Activity.Services.ServiceOrder;
import com.vendor.perfectmandii.Activity.User.customerprofile.EditProfileActivity;
import com.vendor.perfectmandii.Activity.User.customerprofile.StoreProfile.AddStoreActivity;
import com.vendor.perfectmandii.Dashboard.OPActivity;
import com.vendor.perfectmandii.Model.userVendor;
import com.vendor.perfectmandii.profile_Updates.ProfileInformation;


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
import java.util.Calendar;


public class LoginActivity extends AppCompatActivity
{
    ProgressDialog progressDialog;
    EditText login,password;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    String name,image_path,storeid,status;

    String uss;
    String login_user,password_user;
    TextView login_btn;
    CardView signup;
    TextView signbtn,forgetpassword;
 

    StringBuilder sb=new StringBuilder();
    CirclePinField linePinField;
    String username,usernumber,usersession;
    TextView forgetpass,eyop;
    String one,two,storename;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHandler = new DBHelper(LoginActivity.this);

        Intent intent = new Intent(LoginActivity.this, ServiceOrder.class);
        startService(intent);

        intializewidget();
        init();





        linePinField = findViewById(R.id.circleField);


        linePinField.setOnTextCompleteListener(new PinField.OnTextCompleteListener() {
            @Override
            public boolean onTextComplete (String enteredText) {
                password_user=enteredText;

                // Toast.makeText(LoginActivity.this,enteredText,Toast.LENGTH_SHORT).show();
                return true; // Return false to keep the keyboard open else return true to close the keyboard
            }
        });

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getdata();

            }
        });
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(LoginActivity.this,ForgetPassword.class);
                startActivity(intent);
            }
        });
        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ShowInputDialog();
            }
        });
        signbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(LoginActivity.this,"In-Active",Toast.LENGTH_LONG).show();
                ShowInputDialog();
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    void intializewidget()
    {
        eyop=findViewById(R.id.eyop);
        signup=findViewById(R.id.lpsignup);
        signbtn=findViewById(R.id.btn_signup_byn);
        forgetpassword=findViewById(R.id.fpassword);
        login=findViewById(R.id.input_phone_r);

        login_btn=findViewById(R.id.btn_login);
        forgetpass=findViewById(R.id.fpassword);

    }

    void getdata()
    {



        login_user=login.getText().toString();



        if(login_user.equalsIgnoreCase("")&&password_user.equalsIgnoreCase(""))
        {
            Toast.makeText(LoginActivity.this,"Please enter login detail",Toast.LENGTH_LONG).show();
        }
        else
        {
            uss=createsession();
            new AsyncFetch().execute();
        }





    }


    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("\tLoading...");
            progressDialog.setIcon(R.drawable.optimizedlogo);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                String links="https://sellerportal.perfectmandi.com/sessionexist.php?id="+login_user+"&pass="+password_user;
                url = new URL(links);

            }
            catch (MalformedURLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try
            {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK)
                {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        result.append(line);
                    }
                    return (result.toString());
                }
                else
                {
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
            if("Profile Cretional Flag".equalsIgnoreCase(result))
            {
                Toast.makeText(LoginActivity.this,result,Toast.LENGTH_LONG).show();
                Intent intent=new Intent(LoginActivity.this, ProfileInformation.class);
                intent.putExtra("session",createsession());
                intent.putExtra("userid",login_user);
                intent.putExtra("flag","new");
                startActivity(intent);
            }
            else if (result.equalsIgnoreCase("Invalid Login Id and Password"))
            {
                Toast.makeText(LoginActivity.this,result,Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("Please contact admin"))
            {

                eyop.setText("Your account is in approval stage, please try after 30 minutes");
                Toast.makeText(LoginActivity.this,result,Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("Invalid Login"))
            {

                Toast.makeText(LoginActivity.this,"Please enter correct Log In and/or Pin",Toast.LENGTH_LONG).show();
            }
            else
            {
                // List<DataFish> data=new ArrayList<>();


                try {



                    JSONArray jArray = new JSONArray(result);

                    // Extract data from json and store into ArrayList as class objects
                    for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        // DataFish fishData = new DataFish();
                        image_path= json_data.getString("image_path");
                        name= json_data.getString("name");
                        storename=json_data.getString("store_name");
                        storeid=json_data.getString("id");
                        status=json_data.getString("status");
                    }
                    Intent intent=new Intent(LoginActivity.this, OPActivity.class);
                    dbHandler.addNewUser(login_user,password_user,createsession(),name,storename,storeid,image_path,status);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                catch (Exception e)
                {
                }
            }


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

    void ShowInputDialog()
    {

        StringBuilder sb=new StringBuilder();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.temporaryuser_add, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        EditText inputusername = (EditText) dialogView.findViewById(R.id.inputusername);
        EditText inputusernumber = (EditText) dialogView.findViewById(R.id.inputusernumber);



        TextView getotp=(TextView)dialogView.findViewById(R.id.getotp);
        //  EditText inputpin=(EditText)dialogView.findViewById(R.id.inputuserpin);


        CirclePinField linePinField1,linePinField2;

        linePinField1 = dialogView.findViewById(R.id.circleField5);



        linePinField2 = dialogView.findViewById(R.id.circleField6);
//
   /*     inputusernumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                if (start==3)
                {
                    inputusernumber.setText(inputusernumber.getText()+"-");
                    inputusernumber.setSelection(inputusernumber.getText().length());
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/




        linePinField1.setOnTextCompleteListener(new PinField.OnTextCompleteListener() {
            @Override
            public boolean onTextComplete (String enteredText) {
                one =enteredText;

                // Toast.makeText(LoginActivity.this,enteredText,Toast.LENGTH_SHORT).show();
                return true; // Return false to keep the keyboard open else return true to close the keyboard
            }
        });
        linePinField2.setOnTextCompleteListener(new PinField.OnTextCompleteListener() {
            @Override
            public boolean onTextComplete (String enteredText) {
                two=enteredText;

                // Toast.makeText(LoginActivity.this,enteredText,Toast.LENGTH_SHORT).show();
                return true; // Return false to keep the keyboard open else return true to close the keyboard
            }
        });


        Button btn=dialogView.findViewById(R.id.taskadd);


        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                username=inputusername.getText().toString();
                usernumber=inputusernumber.getText().toString();
                usersession=createsession();








                if(one.equals(two))
                {
                    new AsyncFetch_Add().execute();
                    alertDialog.dismiss();
                }
                else
                {

                    Toast.makeText(LoginActivity.this,"Pin doesn’t match, please try again",Toast.LENGTH_LONG).show();
                }










            }
        });

    }


    ArrayList<userVendor> pointModelsArrayList;
    DBHelper dbHandler;
    private class AsyncFetch_Add extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("\tLoading...");
            progressDialog.setTitle("PerfectMandi");
            progressDialog.setIcon(R.drawable.optimizedlogo);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                String liveurl="https://sellerportal.perfectmandi.com/adduser_mb.php?id="+username+"&pin="+one+"&ph="+usernumber+"&se="+usersession;
                url = new URL(liveurl);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try
            {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK)
                {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        result.append(line);
                    }
                    return (result.toString());
                }
                else
                {
                    return ("unsuccessful"+String.valueOf(response_code));
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
            Toast.makeText(LoginActivity.this,result,Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            if(result.equalsIgnoreCase("User Created"))
            {
                Toast.makeText(LoginActivity.this,"User Created",Toast.LENGTH_LONG).show();
                Toast.makeText(LoginActivity.this,result,Toast.LENGTH_LONG).show();
                Intent intent=new Intent(LoginActivity.this, ProfileInformation.class);
                intent.putExtra("userid",usernumber);
                intent.putExtra("username",username);
                intent.putExtra("session",createsession());
                intent.putExtra("flag","new");
                startActivity(intent);
            }
            else
            {

            }

        }
    }



    void init()
    {
        pointModelsArrayList = dbHandler.readVendor();



        if (pointModelsArrayList.size()>0)
        {
            userVendor pointModel=pointModelsArrayList.get(0);



            Intent intent=new Intent(LoginActivity.this, OPActivity.class);
            intent.putExtra("session",createsession());
            intent.putExtra("userid",pointModel.userid);
            intent.putExtra("username",pointModel.username);
            intent.putExtra("path",pointModel.storepath);
            intent.putExtra("store",pointModel.storename);
            intent.putExtra("storeid",pointModel.storeid);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }
        else
        {


        }
    }

}

