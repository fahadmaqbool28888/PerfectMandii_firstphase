package com.customer.perfectcustomer.Activity.PasswordChange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.customer.perfectcustomer.R;
import com.poovam.pinedittextfield.CirclePinField;
import com.poovam.pinedittextfield.PinField;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class confirmPassword extends AppCompatActivity {
    TextView btnnext;
    TextView fpassword_text;
    ProgressDialog progressDialog;
    String number,number1,number2,result;
    EditText pin5,pin6,pin7,pin8;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    StringBuilder sb=new StringBuilder();
    SmsVerifyCatcher smsVerifyCatcher;
    char[] charArray ;
    String code, icom;
    CirclePinField linePinField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);



        intializeWidget();
        Intent intent=getIntent();
        number=intent.getStringExtra("num");
        result = number.substring(0, 6) + "****" + number.substring(10);
        //   Toast.makeText(confirmPassword.this,number,Toast.LENGTH_LONG).show();
        number1=number.substring(4,10);
        number2=number1.replace(number1,"*");





        new AsyncFetch().execute();
        fpassword_text.setText("Enter the code that we send to "+result );





        linePinField.setOnTextCompleteListener(new PinField.OnTextCompleteListener() {
            @Override
            public boolean onTextComplete (@NotNull String enteredText) {
                code=enteredText;
                return true;
            }
        });





        btnnext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               icom=code;
                if ("".equalsIgnoreCase(icom))
                {
                    Toast.makeText(confirmPassword.this,"Pin is empty",Toast.LENGTH_LONG).show();
                }
                else

                {

                    Intent intent=new Intent(confirmPassword.this, EnterYourNewPasswordActivity.class);
                    intent.putExtra("num",number);
                    intent.putExtra("code",code);
                    startActivity(intent);


                }

            }
        });


    }
    private void intializeWidget()
    {

        linePinField = findViewById(R.id.circleField);
        btnnext=findViewById(R.id.btn_next);
        fpassword_text=findViewById(R.id.fpassword_text);

        pin5=findViewById(R.id.num_5);
        pin6=findViewById(R.id.num_6);
        pin7=findViewById(R.id.num_7);
        pin8=findViewById(R.id.num_8);
    }


    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(confirmPassword.this);
            progressDialog.setMessage("\tLoading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                String links="https://sellerportal.perfectmandi.com/forgetPassword.php?id="+number;
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


            if ("0 results".equalsIgnoreCase(result))
            {



            }









        }
    }





    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
