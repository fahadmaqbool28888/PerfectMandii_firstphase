package com.consumer.perfectmandii.ProfileArea;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.consumer.perfectmandii.ProfileFragment;
import com.consumer.perfectmandii.ProfileInformationActivity;
import com.consumer.perfectmandii.R;
import com.poovam.pinedittextfield.CirclePinField;
import com.poovam.pinedittextfield.PinField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class CreditionalsFragment extends Fragment
{
    String ad,otp_number,number,otp;
    TextView next_text;
    boolean start_=false;

    EditText customer_name,customer_phone,customer_otp;
    String name,phone,pin,cpin,assignPin;
    CirclePinField pi,pi2;
    CardView next_12;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    // TODO: Rename and change types and number of parameters
    public static CreditionalsFragment newInstance(String param1, String param2) {
        CreditionalsFragment fragment = new CreditionalsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    void init_state(View view)
    {

        customer_otp.setVisibility(View.GONE);
        if (start_!=true)
        {
            next_text.setText("Get OTP");
        }
        else
        {

        }
    }


    void getnumber()
    {
     number=customer_phone.getText().toString();
       // String mobNumberAfter = number.replace("/^0+/","");
     if (number.startsWith("0"))
     {

         ad=number.replaceFirst("0","");

         otp_number="92"+ad;
         System.out.println(otp_number);


     }



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_creditionals, container, false);
        initt_widget(view);
        init_state(view);


       next_12.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {


               if (next_text.getText().toString().equalsIgnoreCase("Get OTP"))
               {
                   if (customer_phone.getText().toString().equalsIgnoreCase(""))
                   {
                       Toast.makeText(getContext(),"Enter number",Toast.LENGTH_LONG).show();
                   }
                   else
                   {
                       getnumber();
                       new AsyncFetch().execute();

                   }

               }
               else

               {
                   check_empty();
               }



               //getData();
            //


           }
       });




       pi.setOnTextCompleteListener(new PinField.OnTextCompleteListener()
       {
           @Override
           public boolean onTextComplete(@NonNull String s) {
               pin=s;
               return true;
           }
       });

       pi2.setOnTextCompleteListener(new PinField.OnTextCompleteListener()
       {
           @Override
           public boolean onTextComplete(@NonNull String s) {
               cpin=s;
               return true;
           }
       });
       return view;
    }

    void initt_widget(View view)
    {
        customer_otp=view.findViewById(R.id.customer_otp);
        next_12=view.findViewById(R.id.next_12);
        pi=view.findViewById(R.id.pin_customer);
        pi2=view.findViewById(R.id.pin_confirm);
        //customer_name,customer_phone;
        customer_name=view.findViewById(R.id.customer_name);
        customer_phone=view.findViewById(R.id.customer_phone);
        next_text=view.findViewById(R.id.next_button);
    }

    void  check_empty()
    {

   if (customer_name.getText().toString().equalsIgnoreCase(""))
   {
       Toast.makeText(getContext(),"Customer Name is empty",Toast.LENGTH_LONG).show();
   }
   else  if (customer_phone.getText().toString().equalsIgnoreCase(""))
   {
       Toast.makeText(getContext(),"Customer Phone is empty",Toast.LENGTH_LONG).show();
   }
   else  if (customer_otp.getText().toString().equalsIgnoreCase(""))
   {
       Toast.makeText(getContext(),"OTP is empty",Toast.LENGTH_LONG).show();
   }
   else if (pin==null)
   {
       Toast.makeText(getContext(),"Pin is empty",Toast.LENGTH_LONG).show();
   }
   else if (cpin==null)
   {
       Toast.makeText(getContext(),"Confirm Pin is empty",Toast.LENGTH_LONG).show();
   }

   else
   {
       if (pin.equalsIgnoreCase(cpin))
       {
           Toast.makeText(getContext(),"Pin Match",Toast.LENGTH_LONG).show();
           assignPin=pin;
           getData();
       }
       else
       {
           Toast.makeText(getContext(),"Pin not Match",Toast.LENGTH_LONG).show();
       }
   }

    }
    void getData()
    {
        name=customer_name.getText().toString();
        phone=customer_phone.getText().toString();
        otp=customer_otp.getText().toString();


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ProfileFragment hallsForState = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("phone",phone);
        args.putString("pin",assignPin);
        args.putString("otp",otp);
        hallsForState.setArguments(args);
        transaction.replace(((ViewGroup)getView().getParent()).getId(), hallsForState);
        transaction.addToBackStack(null);
        transaction.commit();
        ProfileInformationActivity profileInformation = (ProfileInformationActivity) getActivity();
        profileInformation.Recieve_Data("Done_1");

    }
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private class AsyncFetch extends AsyncTask<String, String, String> {


        ProgressDialog progressDialog = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Perfect Mandi");
            progressDialog.setIcon(R.drawable.optimizedlogo);
            // progressDialog.setMessage("Data is Loading...");
            progressDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String urls="https://sellerportal.perfectmandi.com/otp_customer.php?id="+otp_number+"&oid="+number;

                System.out.println(urls);
                url = new URL(urls);

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

            progressDialog.dismiss();
            customer_otp.setVisibility(View.VISIBLE);
            next_text.setText("Next");





        }

    }
}