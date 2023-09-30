package com.vendor.perfectmandii.Adapter.ProductProperties;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.Model.ProductProperties.price;
import com.vendor.perfectmandii.R;
import com.vendor.perfectmandii.vendor_update_quanitity;

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
import java.util.Collections;
import java.util.List;

public class quantityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private int mLowestPosition = -1;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private Context context;
    private LayoutInflater inflater;
    List<price> data= Collections.emptyList();


    String category;



    public quantityAdapter(Context context, List<price> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.update_product_price_tray, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position)
    {


            mLowestPosition = position;
            MyHolder myHolder= (MyHolder) holder;
            price current=data.get(position);
            myHolder.item_name.setText(current.name);
            myHolder.serial_id.setText(Integer.toString(position+1));
            myHolder.product_old_price.setText(current.currentquantity);



            myHolder.update_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String oldquantity=myHolder.product_old_price.getText().toString();


                    String newquantity=myHolder.product_new_price.getText().toString();


                    if (newquantity.isEmpty())
                    {
                        Toast.makeText(context, "Please update quantity", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        int tot=Integer.parseInt(oldquantity)+Integer.parseInt(newquantity);
                        new Update_Quantity(current.productcode,Integer.toString(tot)).execute();
                    }


                }
            });




    }
    @Override
    public int getItemCount()
    {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder
    {

      TextView item_name,serial_id,update_button;
      EditText product_old_price,product_new_price;
      CardView update_button_board;
        public MyHolder(View itemView)
        {
            super(itemView);
            item_name= itemView.findViewById(R.id.item_name);
            serial_id=itemView.findViewById(R.id.serial_id);
            product_old_price=itemView.findViewById(R.id.product_old_price);
            product_new_price=itemView.findViewById(R.id.product_new_price);
            update_button_board=itemView.findViewById(R.id.update_button_board);
            update_button=itemView.findViewById(R.id.update_button);
        }

    }




    private class Update_Quantity extends AsyncTask<String, String, String>
    {

        String product_id,product_OQ,getProduct_NQ;

        Update_Quantity(String product_id,String getProduct_NQ)
        {
            this.product_id=product_id;

            this.getProduct_NQ=getProduct_NQ;

        }
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;


        ProgressDialog progressDialog=new ProgressDialog(context);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
          //  System.out.println(apiUrl);
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();



        }
        @Override
        protected String doInBackground(String... params)
        {
            String apiUrl = "https://staginigserver.perfectmandi.com/update_pq.php?puq="+getProduct_NQ+"&&psqu="+product_id;

            System.out.println(apiUrl);
          //  Toast.makeText(context, apiUrl, Toast.LENGTH_SHORT).show();
            try
            {
                url = new URL(apiUrl);
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

            progressDialog.dismiss();
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();








        }

    }

}