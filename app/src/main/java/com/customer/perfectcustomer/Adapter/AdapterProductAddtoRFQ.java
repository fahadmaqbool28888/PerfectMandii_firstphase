package com.customer.perfectcustomer.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customer.perfectcustomer.Model.OrderCartModel;
import com.customer.perfectcustomer.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public abstract class AdapterProductAddtoRFQ extends RecyclerView.Adapter<AdapterProductAddtoRFQ.MyHolder> {

    boolean isallselected=false;



    String link;
    replaceProductAdapter adapter;
    int deletepostion;
    boolean editflag = false;
    private final Context context;
    private final LayoutInflater inflater;
    List<OrderCartModel> datas;
    int count = 0;
    String imagepath;
    String ocid;
    String editquantity;
    String flag;
    int rposition;


    String current_price, selling_price, quantityofitem;



    public abstract  void flag(String flag);
    public abstract  void passitem(String flag, OrderCartModel orderCartModel);
    public class MyHolder extends RecyclerView.ViewHolder {

        RecyclerView lower_deck;
        CheckBox checkBox;
        TextView priceitem, nameofitem, quanvalue, quantityofitem, productdes_1;

        ImageView ivFish;


        ImageView editfun, delefun;
        TextView productQuantity, sellingprice, productname, ordertotalprice;
        // create constructor to get widget reference
        LinearLayout heade;

        public MyHolder(View itemView) {
            super(itemView);
            //
            editfun = itemView.findViewById(R.id.ele);
            delefun = itemView.findViewById(R.id.dele);
            //
            checkBox = itemView.findViewById(R.id.idcheck_1);
            ivFish = itemView.findViewById(R.id.ivFish_1);
            sellingprice = itemView.findViewById(R.id.priceofitem);
            quantityofitem = itemView.findViewById(R.id.quantityofitem);
            productname = itemView.findViewById(R.id.nameofitem);
            ordertotalprice = itemView.findViewById(R.id.ordertotalprice);
            lower_deck = itemView.findViewById(R.id.lower_deck);

            heade = itemView.findViewById(R.id.heade);
        }


    }




    public AdapterProductAddtoRFQ(Context context, List<OrderCartModel> data, String  flag) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.datas = data;
        this.flag = flag;
        this.rposition = rposition;
       // mCheckedStateA = new boolean[data.size()];


    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.container_for_rfq, parent, false);
        MyHolder holder = new MyHolder(view);


        return holder;
    }

    Double total;

    boolean isEditflag = false;
    // Bind data


    public void selectAllItems() {
        for (OrderCartModel item : datas) {
            item.setSelected(true);
        }
        isallselected=true;
        notifyDataSetChanged();
    }

    public void unselectAllItems() {
        for (OrderCartModel item : datas) {
            item.setSelected(false);
        }
        isallselected=false;

        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MyHolder holder, @SuppressLint("RecyclerView") int position) {


        OrderCartModel current = datas.get(position);
        holder.productname.setText(current.name);
        holder.checkBox.setChecked(current.isSelected());

        if (isallselected)
        {
            holder.lower_deck.setVisibility(View.GONE);
        }
/*       // Glide
                .with(context)
                .load(current.image_urlname)
                .centerCrop()
                .into(holder.ivFish);*/


        imagepath = current.image_urlname;
        if (imagepath.contains("https://sellerportal.perfectmandi.com/"))
        {
            Picasso.get().load(imagepath).into(holder.ivFish);
        }
        else
        {
            String urls="https://sellerportal.perfectmandi.com/"+imagepath;
            Picasso.get().load(urls).into(holder.ivFish);

        }
        String flags = current.oos;
        String rp = current.rp;
        if (rp.equalsIgnoreCase("yes")) {
            selling_price = current.price;
            quantityofitem = current.quantity;
        } else {
            selling_price = current.selling_price;
            quantityofitem = current.order_quantity;
        }

        holder.sellingprice.setText("Price: "+selling_price);
        holder.quantityofitem.setText("Quantity: "+quantityofitem);
        if (current.measure_category.equalsIgnoreCase("Kilogram")) {
            Double total = Double.parseDouble(selling_price) * Double.parseDouble(quantityofitem) * Double.parseDouble(current.product_measure_in);

            holder.ordertotalprice.setText("Total: " + total);
        } else {
            Double total = Double.parseDouble(selling_price) * Double.parseDouble(quantityofitem);
            holder.ordertotalprice.setText("Total: " + total);

        }

        holder.checkBox.setOnCheckedChangeListener(null); // Remove previous listener to prevent unwanted triggering


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked())
                {
                    passitem("include",current);

                    current.setSelected(true);
                    holder.lower_deck.setVisibility(View.GONE);
                }
                else
                {
                    if (isallselected)
                    {
                        current.setSelected(false);

                        removeItems(position,current);
                        holder.lower_deck.setVisibility(View.VISIBLE);

                    }
                    else {
                        passitem("exclude",current);
                        current.setSelected(false);

                        holder.lower_deck.setVisibility(View.VISIBLE);

                    }

                }
            }
        });

        if (current.isSelected())
        {
            holder.checkBox.setChecked(true);
        }
        else {
            holder.checkBox.setChecked(false);
        }
/*        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (isChecked)
            {
                holder.lower_deck.setVisibility(View.GONE);

            }
            else {
                holder.lower_deck.setVisibility(View.VISIBLE);

            }


            current.setSelected(isChecked);
        });*/



        adapter=new replaceProductAdapter(context,current.replaceData,position) {
            @Override
            public void select_val(int postion, boolean val, int pos, OrderCartModel productSub)
            {
                OrderCartModel orderCartModel1=datas.get(pos);

                //   System.out.println("ocid " +orderCartModel1.ocid);
                String id=productSub.ocid;



                new AsyncFetch_(pos,id,productSub).execute();
            }
        };

        holder.lower_deck.setAdapter(adapter);
        holder.lower_deck.setLayoutManager(new LinearLayoutManager(context));




    }


    public void update(OrderCartModel orderCartModel,int position) {
        datas.remove(position);
        add(orderCartModel);
    }

    public void add (OrderCartModel track) {
        datas.add(track);
        this.notifyDataSetChanged();
    }
    public  abstract void removeItems(int pos,OrderCartModel orderCartModel);



    @Override
    public int getItemCount() {
        return datas.size();
    }

    public int getCount() {
        return datas.size();
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


    private class AsyncFetch_ extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(context.getApplicationContext());
        HttpURLConnection conn;
        URL url = null;

        String id;int pos;
        OrderCartModel orderCartModel;
        AsyncFetch_(int pos,String id,OrderCartModel orderCartModel)
        {
            this.pos=pos;
            this.id=id;
            this.orderCartModel=orderCartModel;
        }
        @Override
        protected void onPreExecute()

        {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
          //  pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                System.out.println("https://sellerportal.perfectmandi.com/order_update.php?id="+id+"&&ocid="+orderCartModel.id+"&&quan="+orderCartModel.order_quantity+"&&price="+orderCartModel.selling_price+"&&total="+orderCartModel.st_price);
                url = new URL("https://sellerportal.perfectmandi.com/order_update.php?id="+id+"&&ocid="+orderCartModel.id+"&&quan="+orderCartModel.order_quantity+"&&price="+orderCartModel.selling_price+"&&total="+orderCartModel.st_price);
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
            else if ("Record updated successfully".equalsIgnoreCase(result))
            {
                datas.set(pos,orderCartModel);


                System.out.println("Before"+String.valueOf(orderCartModel.val));
                  notifyDataSetChanged();
                  flag("yes");
            }


        }

    }


}

