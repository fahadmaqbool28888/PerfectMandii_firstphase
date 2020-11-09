package com.consumer.perfectmandii.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.consumer.perfectmandii.Model.CategoyByProductModel;
import com.consumer.perfectmandii.Model.CategoyByvendorModel;
import com.consumer.perfectmandii.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterCategoryByProduct extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        {
            public static final int CONNECTION_TIMEOUT = 10000;
            public static final int READ_TIMEOUT = 15000;

private final Context context;
private final LayoutInflater inflater;
        List<CategoyByProductModel> data= Collections.emptyList();
            CategoyByvendorModel current;
        int currentPos=0;
        String category;
        String scategory;

        String[] av;
        String setca;
// create constructor to innitilize context and data sent from MainActivity
public AdapterCategoryByProduct(Context context, List<CategoyByProductModel> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        }

// Inflate the layout when viewholder created
@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_fish, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
        }

// Bind data
@Override
public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
{
        MyHolder myHolder= (MyHolder) holder;
        CategoyByProductModel current=data.get(position);
        category=current.name;
        Picasso.get().load(current.image_url).into(myHolder.ivFish);
        myHolder.textFishName.setText(current.name);



        myHolder.ivFish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
  /*              setca=current.name;
                Toast.makeText(context,String.valueOf(current.name)+current.vendorid,Toast.LENGTH_LONG).show();


                Intent intent=new Intent(context, ProductCatalog.class);
                intent.putExtra("vendor",current.vendorid);
                intent.putExtra("name",current.name);
                context.startActivity(intent);*/
            }
        });



        }




// return total item from List
@Override
public int getItemCount() {
        return data.size();
        }


class MyHolder extends RecyclerView.ViewHolder{

    TextView textFishName;
    CircleImageView ivFish;


    // create constructor to get widget reference
    public MyHolder(View itemView) {
        super(itemView);

        ivFish= (CircleImageView) itemView.findViewById(R.id.ivFish);
        textFishName=itemView.findViewById(R.id.textname);

    }

}





            private class AsyncFetch extends AsyncTask<String, String, String> {
                ProgressDialog pdLoading = new ProgressDialog(context);
                HttpURLConnection conn;
                URL url = null;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    //this method will be running on UI thread
                    pdLoading.setMessage("\tLoading...");
                    pdLoading.setCancelable(false);
                    pdLoading.show();

                }

                @Override
                protected String doInBackground(String... params) {
                    try {

                        // Enter URL address where your json file resides
                        // Even you can make call to php file which returns json data
                        url = new URL("https://staginigserver.perfectmandi.com/l3pcategory.php?id="+setca);

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

                    //this method will be running on UI thread




                    pdLoading.dismiss();
                     Toast.makeText(context,result,Toast.LENGTH_LONG).show();
  /*

                    Intent intent=new Intent(context, ProductCatalog.class);
                    intent.putExtra("value",result);
                    intent.putExtra("val",setca);
                    context.startActivity(intent);
                    */

                }

            }



}