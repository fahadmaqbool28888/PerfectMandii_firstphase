package com.consumer.perfectmandii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class confirmPassword extends AppCompatActivity {
    TextView btnnext;
    TextView fpassword_text;
    ProgressDialog progressDialog;
    String number,number1,number2,result,systempin;
    EditText pin1,pin2,pin3,pin4,pin5,pin6,pin7,pin8;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    StringBuilder sb=new StringBuilder();
    SmsVerifyCatcher smsVerifyCatcher;
    char[] charArray ;
    String code, icom;
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

        //
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                code = parseCode(message);//Parse verification code

                charArray=code.toCharArray();
                pin1.setText(String.valueOf(charArray[0]));
                pin2.setText(String.valueOf(charArray[1]));
                pin3.setText(String.valueOf(charArray[2]));
                pin4.setText(String.valueOf(charArray[3]));

               // Toast.makeText(confirmPassword.this,String.valueOf(charArray[1]),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(confirmPassword.this,EnterYourNewPasswordActivity.class);
               intent.putExtra("num",number);
                intent.putExtra("code",code);
                startActivity(intent);
                // etCode.setText(code);//set code in edit text
                //then you can send verification code to server
            }
        });
        smsVerifyCatcher.setPhoneNumberFilter("8583");


        // smsVerifyCatcher.setFilter("<regexp>");

        // Toast.makeText(confirmPassword.this,"Your System generated pin is "+systempin,Toast.LENGTH_LONG).show();
        fpassword_text.setText("Enter the code that we send to "+result );


    /*    btnnext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(confirmPassword.this,EnterYourNewPasswordActivity.class);
                startActivity(intent);
            }
        });*/




        btnnext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               icom=gettext();
                if ("".equalsIgnoreCase(icom))
                {
                    Toast.makeText(confirmPassword.this,"Pin is empty",Toast.LENGTH_LONG).show();
                }
                else

                {
                    System.out.println(code);

                    System.out.println(icom);
                   // Toast.makeText(confirmPassword.this,icom+" "+code,Toast.LENGTH_LONG).show();
                    if (code.contains(icom))
                    {
                        Toast.makeText(confirmPassword.this,"This"+icom,Toast.LENGTH_LONG).show();

                    }
                    else
                    {

                        //Toast.makeText(confirmPassword.this,code+" "+icom,Toast.LENGTH_LONG).show();
                        //Toast.makeText(confirmPassword.this,"Pin is incorrect",Toast.LENGTH_LONG).show();
                    }


                }

            }
        });


    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }
    String gettext()
    {
        String a=pin1.getText().toString();
        String b=pin2.getText().toString();
        String c=pin3.getText().toString();
        String d=pin4.getText().toString();

        if ("".equalsIgnoreCase(a)||"".equalsIgnoreCase(b)||"".equalsIgnoreCase(c)||"".equalsIgnoreCase(d))
        {
            return null;
        }
        else
        {
            String consolidatestring=a+b+c+d;

            return consolidatestring;
        }




    }
    private void intializeWidget()
    {

        btnnext=findViewById(R.id.btn_next);
        fpassword_text=findViewById(R.id.fpassword_text);
        pin1=findViewById(R.id.num_2);
        pin2=findViewById(R.id.num_3);
        pin3=findViewById(R.id.num_4);
        pin4=findViewById(R.id.num_1);
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



    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
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
