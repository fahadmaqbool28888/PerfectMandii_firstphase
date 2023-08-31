package com.customer.perfectcustomer.Activity.FreightForwarder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.customer.perfectcustomer.R;

import org.json.JSONArray;
import org.json.JSONException;
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

public class FreightForwarderActivity extends AppCompatActivity
{
    String select_province;
    String city;
    List<String> province;

    Spinner spinner;
    ConstraintLayout step2,step3;
    TextView step1;
    CardView step_1_1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freight_forwarder);

        intialize_widget();
        province=new ArrayList<>();
/*        sqLiteHelper=new SQLiteHelper(FreightForwarderActivity.this);
        get_Data();*/
        step_1_1.setVisibility(View.VISIBLE);

        

        step_1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                step_1_1.setVisibility(View.GONE);
                new AsyncFetch().execute();
                step2.setVisibility(View.VISIBLE);
            }
        });



    }

    void intialize_widget()
    {
        spinner=findViewById(R.id.Freigh);
        step_1_1=findViewById(R.id.step1);
        //step1=findViewById(R.id.step1);
        step2=findViewById(R.id.maincontainer);
        step3=findViewById(R.id.maincontainer_form);
    }


/*    List<UserModel> userModelList;
    void get_Data()
    {
        userModelList=sqLiteHelper.readCustomer();
        if (userModelList.size()>0)
        {
            UserModel pointModel=userModelList.get(0);
            city=pointModel.city;

        }
        else
        {



        }
    }*/

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    public  class AsyncFetch extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        ProgressDialog progressDialog=new ProgressDialog(FreightForwarderActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                url = new URL("https://sellerportal.perfectmandi.com/get_FreightForwarder.php?id="+city);
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
                return e.toString();
            }
            try
            {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            }
            catch (IOException e1)
            {
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




            Toast.makeText(FreightForwarderActivity.this,result,Toast.LENGTH_LONG).show();






            if (result!=null)
            {

                try
                {
                    spinner.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    JSONArray jArray = new JSONArray(result);

                    province.add("Select Freight Forwarder");
                    for (int i=0;i<jArray.length();i++)
                    {

                        JSONObject json_data = jArray.getJSONObject(i);


                        String sr=json_data.getString("name");



                        province.add(sr);


                    }
                    province.add("Others");
                    pupulate_parentcategory();
           /*         getMeasurement(jsonArray1);
                    getcategory(jsonArray_dealin);
                    getcolor(jsonArray_color);*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
        void pupulate_parentcategory()
        {
            //spinner.setVisibility(View.GONE);
            // String [] values =
            //  {"Time at Residence","Under 6 months","6-12 months","1-2 years","2-4 years","4-8 years","8-15 years","Over 15 years",};
            //  Spinner spinner = (Spinner) v.findViewById(R.id.spinner1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(FreightForwarderActivity.this, android.R.layout.simple_spinner_item, province);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    String text = spinner.getSelectedItem().toString();


                    if (text.equalsIgnoreCase("Others"))
                    {
                        step3.setVisibility(View.VISIBLE);
                        step2.setVisibility(View.GONE);
                    }

                }
                public void onNothingSelected(AdapterView<?> parent)
                {
                }
            });
        }





    }
}