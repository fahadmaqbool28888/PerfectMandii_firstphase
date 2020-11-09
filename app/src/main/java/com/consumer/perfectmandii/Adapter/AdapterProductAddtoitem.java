package com.consumer.perfectmandii.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.consumer.perfectmandii.Model.OrderCartModel;
import com.consumer.perfectmandii.R;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;



public class AdapterProductAddtoitem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    int selectedPosition=-1;
    String link;
    int deletepostion;
    boolean editflag=false;
    private final Context context;
    private final LayoutInflater inflater;
    List<OrderCartModel> data= Collections.emptyList();
    int count=0;
    String imagepath;
    String ocid;
    String editquantity,flag;
    int rposition;


    String current_price;

    private final boolean[] mCheckedStateA;



    public AdapterProductAddtoitem(Context context, List<OrderCartModel> data,String flag)
    {
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.flag=flag;
        this.rposition=rposition;
        mCheckedStateA = new boolean[data.size()];




    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view=inflater.inflate(R.layout.list_rowv1, parent,false);
        MyHolder holder=new MyHolder(view);



        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position)
    {




        MyHolder myHolder= (MyHolder) holder;
        OrderCartModel current=data.get(position);



        //Picasso.get().load(current.image_urlname).into(myHolder.ivFish);
        Glide
                .with(context)
                .load(current.image_urlname)
                .centerCrop()
                .into(myHolder.ivFish);
        imagepath=current.image_urlname;



        ocid=current.ocid;

        myHolder.sellingprice.setText("Price: "+current.selling_price);
        myHolder.quantityofitem.setText("Quantity: "+current.order_quantity);
        myHolder.productname.setText(current.name);
        myHolder.ordertotalprice.setText("Total: "+current.st_price);

        myHolder.checkBox.setChecked(false);


        if (!mCheckedStateA[position])
        {

            myHolder.checkBox.setChecked(false);
        }
        else {
            myHolder.checkBox.setChecked(true);


        }

        myHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


            }
        });

        myHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("custom-message");
                if (!(mCheckedStateA[position])) {

                    myHolder.checkBox.setChecked(true);
                    mCheckedStateA[position]=true;
                    myHolder.editfun.setVisibility(View.INVISIBLE);
                    myHolder.delefun.setVisibility(View.INVISIBLE);
                    intent.putExtra("nat","ca");
                    intent.putExtra("flag","include");
                    intent.putExtra("pos",position);
                    intent.putExtra("total",current.st_price);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


                } else {
                    myHolder.editfun.setVisibility(View.VISIBLE);
                    myHolder.delefun.setVisibility(View.VISIBLE);
                    myHolder.checkBox.setChecked(false);
                    mCheckedStateA[position]=false;
                    intent.putExtra("nat","ca");
                    intent.putExtra("pos",position);
                    intent.putExtra("total",current.st_price);
                    intent.putExtra("flag","exclude");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
        });


      myHolder.editfun.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              Intent intent = new Intent("custom-message");
              intent.putExtra("nat","ea");
              intent.putExtra("id",current.id);

              intent.putExtra("position",position);
              intent.putExtra("editflag","1");
              intent.putExtra("stock",current.stock);
              intent.putExtra("moq",current.MOQ);
              intent.putExtra("product_measure_in",current.product_measure_in);
              intent.putExtra("measure_category",current.measure_category);
              intent.putExtra("price",Integer.parseInt(current.price));
              intent.putExtra("oquan",Integer.parseInt(current.order_quantity));
              LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
          }
      });

      myHolder.delefun.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {


              AlertDialog alertDialog = new AlertDialog.Builder(context)
//set icon
                      .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                      .setTitle("Are you sure to Exit")
//set message
                      .setMessage("Are you sure to delete from this cart?")
                      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                              //set what would happen when positive button is clicked
                              deletepostion=position;
                              ocid=current.ocid;
                              int total=Integer.parseInt(current.order_quantity)+Integer.parseInt(current.stock);
                              link="https://sellerportal.perfectmandi.com/delete_order_item.php?id="+ocid+"&uquan="+total+"&pid="+current.id;
                              new AsyncFetch_Del().execute();
                          }
                      })

                      .setNegativeButton("No", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i)
                          {



                          }
                      })
                      .show();

          }
      });










    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    // return total item from List
    @Override
    public int getItemCount()
    {
        return data.size();
    }

    void  showquan()
    {



    }
    class MyHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        TextView priceitem,nameofitem,quanvalue,quantityofitem,productdes_1;

        ImageView ivFish;


        ImageView editfun,delefun;
        TextView productQuantity,sellingprice,productname,ordertotalprice;
        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            //
            editfun=itemView.findViewById(R.id.ele);
            delefun=itemView.findViewById(R.id.dele);
            //
            checkBox=itemView.findViewById(R.id.idcheck_1);
            ivFish=itemView.findViewById(R.id.ivFish_1);
            sellingprice=itemView.findViewById(R.id.priceofitem);
            quantityofitem=itemView.findViewById(R.id.quantityofitem);
            productname=itemView.findViewById(R.id.nameofitem);
            ordertotalprice=itemView.findViewById(R.id.ordertotalprice);

        }


    }
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private class AsyncFetch_Del extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(context);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()

        {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try
            {

               url = new URL(link);
            }
            catch (MalformedURLException e)
            {
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
            pdLoading.dismiss();

            if ("".equalsIgnoreCase(result))
            {

            }
            else if ("Records were deleted successfully.".equalsIgnoreCase(result))
            {

                Intent intent = new Intent("custom-message");


                intent.putExtra("nat","da");
                intent.putExtra("value_del",current_price);
                intent.putExtra("position",deletepostion);
                // intent.putExtra("number",saas);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                //context.finish();
            }


        }

    }

}

