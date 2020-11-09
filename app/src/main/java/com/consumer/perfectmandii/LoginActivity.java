package com.consumer.perfectmandii;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.consumer.perfectmandii.Customer.UserModel;
import com.consumer.perfectmandii.LocalDB.SQLiteHelper;
import com.consumer.perfectmandii.Model.Product_Before_Login;
import com.consumer.perfectmandii.Room.EntityClass;
import com.poovam.pinedittextfield.CirclePinField;
import com.poovam.pinedittextfield.PinField;

import org.jetbrains.annotations.NotNull;
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
import java.util.List;


public class LoginActivity extends AppCompatActivity
{
    private List<EntityClass> list;
    String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    ProgressDialog progressDialog;
    EditText login,password;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    String name,image_path;

    String uss;
    String login_user,password_user;
    TextView login_btn;
    Constant constant;
    CardView signup;
    TextView signbtn,forgetpassword;


    StringBuilder sb=new StringBuilder();
    CirclePinField linePinField;
    String username,usernumber,usersession;
    TextView forgetpass,eyop;
    String one,two,product_id;
    SQLiteHelper sqLiteHelper;


void showDialog()
{
    new AlertDialog.Builder(LoginActivity.this)
            .setTitle("title")
            .setMessage("message")
            .setPositiveButton(android.R.string.ok, null)
            .show();
}


String flag;


    String code;

    ArrayList<Product_Before_Login> codearray;
   // SQLiteHelper sqLiteHelper;
    void get_Data_3()
    {
        sqLiteHelper=new SQLiteHelper(LoginActivity.this);
        codearray=sqLiteHelper.readBeforeProduct();



        if (codearray.size()>0)
        {
            Product_Before_Login product_before_login=codearray.get(0);
            //textname_1.setText(pointModel.Name);
            System.out.println("This is "+product_before_login.code+"\n");

            code=product_before_login.code;


            if (code!=null)
            {
                Toast.makeText(LoginActivity.this,code,Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(LoginActivity.this,"code",Toast.LENGTH_LONG).show();

            }





        }
        else
        {

            Toast.makeText(LoginActivity.this,"No DB",Toast.LENGTH_LONG).show();

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        intializewidget();

        Intent intent=getIntent();




        //Toast.makeText(LoginActivity.this,"Code is"+product_id,Toast.LENGTH_LONG).show();
        flag=intent.getStringExtra("flag");
        product_id=intent.getStringExtra("product_id");




        sqLiteHelper=new SQLiteHelper(LoginActivity.this);


        sqLiteHelper.addbeforelogin(product_id);


        if (("pp").equalsIgnoreCase(flag))

        {
            Toast.makeText(LoginActivity.this,product_id,Toast.LENGTH_LONG).show();
            get_Data_3();
        }

      //  readBeforeProduct()addbeforelogin();
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                    this, // Activity
                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }


        login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        linePinField = findViewById(R.id.circleField);


        linePinField.setOnTextCompleteListener(new PinField.OnTextCompleteListener() {
            @Override
            public boolean onTextComplete (@NotNull String enteredText) {
                password_user=enteredText;

                // Toast.makeText(LoginActivity.this,enteredText,Toast.LENGTH_SHORT).show();
                return true; // Return false to keep the keyboard open else return true to close the keyboard
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getdata();
                emptyData();

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
            public void onClick(View v)
            {
                Intent intent=new Intent(LoginActivity.this,ProfileInformationActivity.class);
                startActivity(intent);
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (flag.equalsIgnoreCase("flag5"))
        {
            Intent intent=new Intent(LoginActivity.this,MainActivity_OP.class);
            startActivity(intent);
        }
        else  if (flag.equalsIgnoreCase("flag6"))
        {
            Intent intent=new Intent(LoginActivity.this,MainActivity_OP.class);
            startActivity(intent);
        }
        else if (("pp").equalsIgnoreCase(flag))
        {
            Intent intent=new Intent(LoginActivity.this,MainActivity_OP.class);
            startActivity(intent);
        }
        else
        {
            Intent intent=new Intent(LoginActivity.this,MainActivity_OP.class);
            startActivity(intent);
        }

       // finish();
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
    }

    void emptyData()
    {

        if ((login_user!=null)&&password_user!=null)
        {
            uss=createsession();
            new AsyncFetch().execute();
        }
        else
        {
            Toast.makeText(LoginActivity.this, "Please add login id!", Toast.LENGTH_SHORT).show();
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
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                String links="https://sellerportal.perfectmandi.com/login_customer.php?id="+login_user+"&pass="+password_user;
                System.out.println(links);
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

                Intent intent=new Intent(LoginActivity.this, ProfileInformationActivity.class);
                intent.putExtra("session",createsession());

                intent.putExtra("userid",login_user);
                intent.putExtra("flag","new");
                startActivity(intent);
            }
            else if("Invalid User name,Registered yourself!".equalsIgnoreCase(result))
            {
                Toast.makeText(LoginActivity.this,"Invalid User name,Register yourself!",Toast.LENGTH_LONG).show();

            }
            else if (result.contains("Your Account has been locked"))
            {
                Toast.makeText(LoginActivity.this,"Your Account has been locked,please contact our customer services",Toast.LENGTH_LONG).show();

            }

            else if("Password you entered for login is incorrect".equalsIgnoreCase(result))
            {
                Toast.makeText(LoginActivity.this,"Password you entered for login is incorrect",Toast.LENGTH_LONG).show();

            }
            else if ("Your Account has been locked.We've detected some activity that violates our policy and have locked your account To unlock your account,please contact our customer services".contains(result))
            {


                Toast.makeText(LoginActivity.this,"Please contact customer Service",Toast.LENGTH_LONG).show();
            }
            else
            {
                try {
                    JSONArray jArray = new JSONArray(result);
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
                        sqLiteHelper.addNewCustomer(userModel.accountid,userModel.Name,userModel.contact,userModel.session,userModel.shop,userModel.profilepic,userModel.city);


                        showDialog();

                        if (product_id!=null)
                        {
                            Intent intent=new Intent(LoginActivity.this, ProductProfileActivity.class);
                            intent.putExtra("code",product_id);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent=new Intent(LoginActivity.this, MainActivity_OP.class);
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
        inputusernumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {




            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        linePinField1.setOnTextCompleteListener(new PinField.OnTextCompleteListener() {
            @Override
            public boolean onTextComplete (@NotNull String enteredText) {
                one =enteredText;

                // Toast.makeText(LoginActivity.this,enteredText,Toast.LENGTH_SHORT).show();
                return true; // Return false to keep the keyboard open else return true to close the keyboard
            }
        });
        linePinField2.setOnTextCompleteListener(new PinField.OnTextCompleteListener() {
            @Override
            public boolean onTextComplete (@NotNull String enteredText) {
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

                    Toast.makeText(LoginActivity.this,"Invalid data",Toast.LENGTH_LONG).show();
                }










            }
        });

    }




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
                String liveurl="https://staginigserver.perfectmandi.com/adduser.php?id="+username+"&pin="+one+"&ph="+usernumber+"&se="+usersession;
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
                    return ("unsuccessful"+ response_code);
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
                Intent intent=new Intent(LoginActivity.this, ProfileInformationActivity.class);
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





}
