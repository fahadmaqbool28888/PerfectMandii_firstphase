package com.vendor.perfectmandii.CategoryDetail.Controller;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.vendor.perfectmandii.profile_Updates.ProductInformationActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public abstract class bundleUpload extends AsyncTask<String,String,String>
{
    String ServerUploadPath = "https://sellerportal.perfectmandi.com/updatespecific.php";

    String times;
    public bundleUpload(Context context, ProgressBar progressBar, String data, String sellerid, String price, String maincatgory, String mainsub_category, String subcategory,String times) {
        this.context = context;
        this.progressBar = progressBar;
        this.data = data;
        this.sellerid = sellerid;
        this.price = price;
        this.maincatgory = maincatgory;
        this.mainsub_category = mainsub_category;
        this.subcategory = subcategory;
        this.times=times;
    }

    Context context;
    ProgressBar progressBar;
    String data ,sellerid ,price,maincatgory,mainsub_category,subcategory;


    String Data = "data"; //1
    String Seller_id = "pid";//2
    String Price = "price";//3
    String main_catgory = "s1";//4
    String main_sub_category = "s2";//5
    String _sub_category = "s3";//6
    String time="time";

    boolean check = true;
    public abstract void data_fetch(String s);
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        data_fetch(s);
    }

    @Override
    protected String doInBackground(String... strings) {


        HashMap<String,String> HashMapParams = new HashMap<String,String>();
//  String data ,sellerid ,price,maincatgory,mainsub_category,subcategory;
        HashMapParams.put(Data, data);
        HashMapParams.put(Seller_id, sellerid);
        HashMapParams.put(Price, price);
        HashMapParams.put(main_catgory, maincatgory);
        HashMapParams.put(main_sub_category, mainsub_category); //fullname
        HashMapParams.put(_sub_category, subcategory);
        HashMapParams.put(time,times);

        String FinalData = ImageHttpRequest(ServerUploadPath, HashMapParams);

        return FinalData;
    }



    public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

        StringBuilder stringBuilder = new StringBuilder();

        try {

            URL url;
            HttpURLConnection httpURLConnectionObject ;
            OutputStream OutPutStream;
            BufferedWriter bufferedWriterObject ;
            BufferedReader bufferedReaderObject ;
            int RC ;

            url = new URL(requestURL);

            httpURLConnectionObject = (HttpURLConnection) url.openConnection();

            httpURLConnectionObject.setReadTimeout(19000);

            httpURLConnectionObject.setConnectTimeout(19000);

            httpURLConnectionObject.setRequestMethod("POST");

            httpURLConnectionObject.setDoInput(true);

            httpURLConnectionObject.setDoOutput(true);

            OutPutStream = httpURLConnectionObject.getOutputStream();

            bufferedWriterObject = new BufferedWriter(

                    new OutputStreamWriter(OutPutStream, "UTF-8"));

            bufferedWriterObject.write(bufferedWriterDataFN(PData));

            bufferedWriterObject.flush();

            bufferedWriterObject.close();

            OutPutStream.close();

            RC = httpURLConnectionObject.getResponseCode();

            if (RC == HttpsURLConnection.HTTP_OK) {

                bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                stringBuilder = new StringBuilder();

                String RC2;

                while ((RC2 = bufferedReaderObject.readLine()) != null){

                    stringBuilder.append(RC2);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

        StringBuilder stringBuilderObject;

        stringBuilderObject = new StringBuilder();

        for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

            if (check)

                check = false;
            else
                stringBuilderObject.append("&");

            stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

            stringBuilderObject.append("=");

            stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
        }

        return stringBuilderObject.toString();
    }
}
