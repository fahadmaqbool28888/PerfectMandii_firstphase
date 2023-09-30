package com.vendor.perfectmandii;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ForgetPassword extends AppCompatActivity {

    TextView btnnext;

    EditText getnumber;
    String getnumbers;
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        btnnext=findViewById(R.id.btn_next);
        getnumber=findViewById(R.id.input_forget_number);


        btnnext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                getnumbers=getnumber.getText().toString();

                if(getnumbers.equalsIgnoreCase(""))
                {
                    Toast.makeText(ForgetPassword.this,"Please enter number",Toast.LENGTH_LONG).show();
                }
                else
                {
                    new AsyncFetch().execute();
                }



            }
        });
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(ForgetPassword.this);
            progressDialog.setMessage("\tLoading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                String links="https://sellerportal.perfectmandi.com/findvendor.php?id="+getnumbers;
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


            if ("Number exist".equalsIgnoreCase(result))
            {

                Intent intent=new Intent(ForgetPassword.this,confirmPassword.class);
                intent.putExtra("num",getnumbers);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
            else if ("Number not exist".equalsIgnoreCase(result))
            {
                getnumber.setText("");
                getnumber.requestFocus();
            }
            else
            {
                Toast.makeText(ForgetPassword.this,"Please Try Again",Toast.LENGTH_LONG).show();
            }








        }
    }
}