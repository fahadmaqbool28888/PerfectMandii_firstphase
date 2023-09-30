package com.vendor.perfectmandii.Adapter.EditOrder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vendor.perfectmandii.Activity.OrderStockActivity;
import com.vendor.perfectmandii.Adapter.InvoiceProductDetail;
import com.vendor.perfectmandii.OrderDashboardActivity;
import com.vendor.perfectmandii.R;

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

public class editAdapter extends RecyclerView.Adapter<editAdapter.EditOrderV>
{
    Context context;
    List<InvoiceProductDetail> data=new ArrayList<>();

    LayoutInflater layoutInflater;
    String value,c,p,proid,rid;
    private final boolean[] mCheckedStateA;
    public editAdapter(Context context,List<InvoiceProductDetail> data)
    {

        this.context=context;
        this.data=data;
        layoutInflater= LayoutInflater.from(context);
        mCheckedStateA = new boolean[data.size()];

    }


    @Override
    public EditOrderV onCreateViewHolder( ViewGroup parent, int viewType)
    {
        View view=layoutInflater.inflate(R.layout.list_rowv212, parent,false);
        return new EditOrderV(view);
    }

    @Override
    public void onBindViewHolder(EditOrderV holder, int position)
    {
        InvoiceProductDetail productDetail=data.get(position);
        holder.available_stock.setText(productDetail.order_quantity);
        holder.nameofitem.setText(productDetail.Product_Name);
        Glide.with(context)
                .load(productDetail.image_path)
                .centerCrop()
                .into(holder.produ_image);

        holder.checkBox.setChecked(false);

        if (!mCheckedStateA[position]) {

            holder.checkBox.setChecked(false);
        } else {
            holder.checkBox.setChecked(true);

        }


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.checkBox.isChecked())
                {
                    c=holder.stock_Value.getText().toString();
                    p=holder.available_stock.getText().toString();

                    Toast.makeText(context,c+" "+p,Toast.LENGTH_LONG).show();

                    if (c.equalsIgnoreCase(""))
                    {
                        Toast.makeText(context,"Please add stock",Toast.LENGTH_LONG).show();
                        holder.checkBox.setChecked(false);

                    }
                    else
                    {

                        holder.stock_Value.setEnabled(false);
                    }
                }
                else {
                    holder.stock_Value.setEnabled(true);
                }
            }
        });



     /*   holder.update_order_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                c=holder.stock_Value.getText().toString();
               p=holder.available_stock.getText().toString();


                proid=productDetail.id;
                rid=productDetail.oid;
                new AsyncFetch().execute();
            }
        });*/


    }


    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public class EditOrderV extends RecyclerView.ViewHolder
    {
        EditText stock_Value, available_stock ,product_moq;
        TextView stock_a,stock_e,miq_moq,nameofitem;
        ImageView produ_image;
        CheckBox checkBox;


        TextView btn_price_;
        CardView ckick_button,update_order_line;
        ConstraintLayout ckick_button_u;

        public EditOrderV(View itemView) {
            super(itemView);
            stock_a=itemView.findViewById(R.id.stock_a);


            produ_image=itemView.findViewById(R.id.ivFish_1);
            stock_Value=itemView.findViewById(R.id.stock_value_1);


            btn_price_=itemView.findViewById(R.id.btn_price_);


            available_stock=itemView.findViewById(R.id.available_stock);
            product_moq=itemView.findViewById(R.id.product_moq);
            nameofitem=itemView.findViewById(R.id.nameofitem);

            checkBox=itemView.findViewById(R.id.value_click);
          //  update_order_line=itemView.findViewById(R.id.update_order_line);
        }
    }


    private class AsyncFetch extends AsyncTask<String, String, String>
    {
        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;
        HttpURLConnection conn;
        URL url = null;
        ProgressDialog progressDialog=new ProgressDialog(context);
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog.show();
            progressDialog.setIcon(R.drawable.optimizedlogo);
        }

        @Override
        protected String doInBackground(String... params) {
            try {



                String i="https://sellerportal.perfectmandi.com/eop.php?rid="+rid+"&pid="+proid+"&os="+p+"&as="+c;
                System.out.println(i);
                url = new URL("https://sellerportal.perfectmandi.com/eop.php?rid="+rid+"&pid="+proid+"&os="+p+"&as="+c);


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

            } catch (IOException e1)
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
        protected void onPostExecute(String result) {


            System.out.println(result);
            progressDialog.dismiss();
            context.startActivity(new Intent(context, OrderDashboardActivity.class));

        }

    }
}
