package com.customer.perfectcustomer.Activity.PasswordChange;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.customer.perfectcustomer.Activity.Authentication.LoginActivity;
import com.customer.perfectcustomer.R;
import com.poovam.pinedittextfield.CirclePinField;
import com.poovam.pinedittextfield.PinField;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EnterYourNewPasswordActivity extends AppCompatActivity {


    TextView btnnext;
    EditText pin1,pin2,pin3,pin4,pin5,pin6,pin7,pin8;
    StringBuilder sb=new StringBuilder();
    String number,code;
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String icons_pin,text1,text2;
    CirclePinField linePinField1,linePinField2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_your_new_password);

        intializeWidget();

        Intent intent=getIntent();
        number=intent.getStringExtra("num");
        code=intent.getStringExtra("code");

        Toast.makeText(EnterYourNewPasswordActivity.this,number+" "+code,Toast.LENGTH_LONG).show();
        //
        //confirm1


        //
        btnnext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String icom=gettext();
                if ("".equalsIgnoreCase(icom))
                {
                    Toast.makeText(EnterYourNewPasswordActivity.this,"Pin is empty",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (" ".equalsIgnoreCase(icom))
                    {
                       // Toast.makeText(EnterYourNewPasswordActivity.this,systempin+" "+icom,Toast.LENGTH_LONG).show();
                        Toast.makeText(EnterYourNewPasswordActivity.this,"Please enter pin",Toast.LENGTH_LONG).show();
                    }
                    else if ("Incorrect Pin".equalsIgnoreCase(icom))
                    {
                        Toast.makeText(EnterYourNewPasswordActivity.this,"Pin is incorrect",Toast.LENGTH_LONG).show();
                    }

                    else
                    {

                        icons_pin=icom;
                        new AsyncFetch().execute();

                /*        Intent intent1=new Intent(EnterYourNewPasswordActivity.this,EnterYourNewPasswordActivity.class);
                        intent1.putExtra("num",number);
                        startActivity(intent1);*/

                    }


                }



            }
        });




        linePinField1.setOnTextCompleteListener(new PinField.OnTextCompleteListener() {
            @Override
            public boolean onTextComplete (@NotNull String enteredText) {
                text1=enteredText;

                // Toast.makeText(LoginActivity.this,enteredText,Toast.LENGTH_SHORT).show();
                return true; // Return false to keep the keyboard open else return true to close the keyboard
            }
        });
        linePinField2.setOnTextCompleteListener(new PinField.OnTextCompleteListener() {
            @Override
            public boolean onTextComplete (@NotNull String enteredText) {
                text2=enteredText;

                return true;
            }
        });
    }

    String gettext()
    {


        if ("".equalsIgnoreCase(text1)||"".equalsIgnoreCase(text2))
        {
            return null;
        }
        else
        {


            if (text1.equalsIgnoreCase(text2))
            {
                return text1;
            }
            else
            {
                return "Incorrect Pin";
            }

        }




    }
    private void intializeWidget()
    {
        btnnext = findViewById(R.id.btn_next);
        linePinField1 = findViewById(R.id.circleFiel3);



        linePinField2 = findViewById(R.id.circleField4);



    }


    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(EnterYourNewPasswordActivity.this);
            progressDialog.setMessage("\tLoading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                String links="https://sellerportal.perfectmandi.com/update_password.php?id="+number+"&value="+code+"&pass="+icons_pin;
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


            if ("Pin Updated".equalsIgnoreCase(result))
            {

                Intent intent=new Intent(EnterYourNewPasswordActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
            else
            {
                Toast.makeText(EnterYourNewPasswordActivity.this,"Please Try Again",Toast.LENGTH_LONG).show();
            }








        }
    }
}