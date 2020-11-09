package com.consumer.perfectmandii;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.consumer.perfectmandii.Adapter.SubjectAdapter;
import com.consumer.perfectmandii.Model.CategoryModel;
import com.consumer.perfectmandii.Model.Chapter;
import com.consumer.perfectmandii.Model.ProductModel.ProductModel;
import com.consumer.perfectmandii.Model.Subject;

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

public class HomeActivity extends AppCompatActivity
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView rvSubject;
    private SubjectAdapter subjectAdapter;
    private ArrayList<Subject> subjects;
    String daya;
    ArrayList<String> data,data1,data2;
    ArrayList<String> newList,rearragename,rearrangepath,rearrangecategory;
    String category,category1,name,path;
    String tempsubject,tempcategory;
    String id;
String res;

    ArrayList<String> imagepath,imagename,imagecategory,identification;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initComponents();
        Intent intent=getIntent();
        daya=intent.getStringExtra("value");
        category=intent.getStringExtra("val");
        preparedat(daya);
        new AsyncFetch().execute();
    }
    private void initComponents()
    {
        rvSubject = findViewById(R.id.rvSubject);
    }

    void preparedat(String result)
    {

        data=new ArrayList<>();
        try
        {
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject json_data = jArray.getJSONObject(i);
                data.add(json_data.getString("name"));
            }
            newList = new ArrayList<String>();
            int size = data.size()-1;
            for(int i=size;i>=0;i--)
            {
                newList.add(data.get(i));
            }

       }
        catch (Exception e)
        {

        }
    }
    private class AsyncFetch extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(HomeActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
            url = new URL("https://staginigserver.perfectmandi.com/loadvendorproductprofile.php?id="+category);
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
            preparedat1(result);
             //Toast.makeText(HomeActivity.this,result,Toast.LENGTH_LONG).show();


       res=result;
        }

    }
    void preparedat1(String result)
    {
        imagepath=new ArrayList<>();
        imagename=new ArrayList<>();
        imagecategory=new ArrayList<>();
        try {
            //Toast.makeText(MainActivity.this,result.toString(),Toast.LENGTH_LONG).show();


            JSONArray jArray1 = new JSONArray(result);
            // Extract data from json and store into ArrayList as class objects
            for(int i=0;i<jArray1.length();i++){
                JSONObject json_data1 = jArray1.getJSONObject(i);
             path=json_data1.getString("image_path");
                 category1=json_data1.getString("image_scategory");
               name=json_data1.getString("image_name");
               id=json_data1.getString("id");

               imagepath.add(path);
               imagename.add(name);
               imagecategory.add(category1);

            }


            subjects = prepareData();

        /*    subjectAdapter = new SubjectAdapter(subjects, HomeActivity.this, newList);
            LinearLayoutManager manager = new LinearLayoutManager(HomeActivity.this);
            rvSubject.setLayoutManager(manager);
            rvSubject.setAdapter(subjectAdapter);
*/

        } catch (Exception e) {
            //  Toast.makeText(Function.this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }



    ArrayList<CategoryModel> prepareproduct()
    {
        String selectcategory;
        ArrayList<CategoryModel> prepareproducts=new ArrayList<CategoryModel>();
        CategoryModel categoryModel=new CategoryModel();
        for(int i=0;i<newList.size();i++)
        {

            selectcategory=newList.get(i);

      if(selectcategory.equalsIgnoreCase("plastic"))
      {

          CategoryModel plastic=new CategoryModel();
          plastic.id=String.valueOf(i);
          plastic.name=imagename.get(i);
          plastic.productModels=new ArrayList<ProductModel>();

          ProductModel plasticmodel=new ProductModel();
          plasticmodel.productName=imagename.get(i);
          plasticmodel.imageUrl=imagepath.get(i);


      }
           else if(selectcategory.equalsIgnoreCase("steel"))
            {
                CategoryModel steel=new CategoryModel();
                steel.id=String.valueOf(i);
                steel.name=imagename.get(i);
                steel.productModels=new ArrayList<ProductModel>();

                ProductModel steelmodel=new ProductModel();
                steelmodel.productName=imagename.get(i);
                steelmodel.imageUrl=imagepath.get(i);


                steel.productModels.add(steelmodel);
            }




        }


















        return prepareproducts;
    }



    private ArrayList<Subject> prepareData()
    {
        String sy;
        int count=0;
        int valueintializer;
        valueintializer=count+1;
        boolean alreadymatched=false;
        ArrayList<Subject> subjects = new ArrayList<Subject>();
        List<String> data=new ArrayList<>();



        int i,j=0;


        System.out.println(newList.size());
        System.out.println(imagecategory.get(j));
        for(i=0;i<newList.size();i++)
        {
            tempsubject=newList.get(i);
            sy=tempsubject;
          for(j=0;j<imagecategory.size();j++)
          {
              tempcategory=imagecategory.get(j);
              if
              (tempsubject.equalsIgnoreCase(tempcategory))
              {

                  System.out.println(tempsubject+" "+"matched"+" "+tempcategory);
                  data.add(tempsubject);
                  count=count+1;
              }
              else
                  {
                      System.out.println(tempsubject+" "+"not matched"+" "+tempcategory);
                  }
          }

        }









        System.out.println("Total Value will be "+ count);




        Toast.makeText(HomeActivity.this,data.toString(),Toast.LENGTH_LONG).show();
        Subject physics = new Subject();
        physics.id = 1;

        physics.chapters = new ArrayList<Chapter>();

        Chapter chapter1 = new Chapter();
        chapter1.id = 1;
        chapter1.chapterName = "Atomic power";
        chapter1.imageUrl = "http://ashishkudale.com/images/phy/atoms.png";

        Chapter chapter2 = new Chapter();
        chapter2.id = 2;
        chapter2.chapterName = "Theory of relativity";
        chapter2.imageUrl = "http://ashishkudale.com/images/phy/sigma.png";

        Chapter chapter3 = new Chapter();
        chapter3.id = 3;
        chapter3.chapterName = "Magnetic field";
        chapter3.imageUrl = "http://ashishkudale.com/images/phy/magnet.png";

        Chapter chapter4 = new Chapter();
        chapter4.id = 4;
        chapter4.chapterName = "Practical 1";
        chapter4.imageUrl = "http://ashishkudale.com/images/phy/caliper.png";

        Chapter chapter5 = new Chapter();
        chapter5.id = 5;
        chapter5.chapterName = "Practical 2";
        chapter5.imageUrl = "http://ashishkudale.com/images/phy/micrometer.png";

        physics.chapters.add(chapter1);
        physics.chapters.add(chapter2);


        Subject chem = new Subject();
        chem.id = 2;
        chem.chapters = new ArrayList<Chapter>();

        Chapter chapter6 = new Chapter();
        chapter6.id = 6;
        chapter6.chapterName = "Chemical bonds";
        chapter6.imageUrl = "http://ashishkudale.com/images/chem/bonds.png";

        Chapter chapter7 = new Chapter();
        chapter7.id = 7;
        chapter7.chapterName = "Sodium";
        chapter7.imageUrl = "http://ashishkudale.com/images/chem/sodium.png";

        Chapter chapter8 = new Chapter();
        chapter8.id = 8;
        chapter8.chapterName = "Periodic table";
        chapter8.imageUrl = "http://ashishkudale.com/images/chem/periodic_table.png";

        Chapter chapter9 = new Chapter();
        chapter9.id = 9;
        chapter9.chapterName = "Acid and Base";
        chapter9.imageUrl = "http://ashishkudale.com/images/chem/chem.png";

        chem.chapters.add(chapter6);
        chem.chapters.add(chapter7);


        Subject bio = new Subject();
        bio.id = 3;
        bio.chapters = new ArrayList<Chapter>();

        Chapter chapter10 = new Chapter();
        chapter10.id = 10;
        chapter10.chapterName = "Bacteria";
        chapter10.imageUrl = "http://ashishkudale.com/images/bio/bacteria.png";

        Chapter chapter11 = new Chapter();
        chapter11.id = 11;
        chapter11.chapterName = "DNA and RNA";
        chapter11.imageUrl = "http://ashishkudale.com/images/bio/dna.png";

        Chapter chapter12 = new Chapter();
        chapter12.id = 12;
        chapter12.chapterName = "Study of microscope";
        chapter12.imageUrl = "http://ashishkudale.com/images/bio/microscope.png";

        Chapter chapter13 = new Chapter();
        chapter13.id = 13;
        chapter13.chapterName = "Protein and fibers";
        chapter13.imageUrl = "http://ashishkudale.com/images/bio/protein.png";

        bio.chapters.add(chapter10);
        bio.chapters.add(chapter11);

        subjects.add(physics);
        subjects.add(chem);
        subjects.add(bio);

        return subjects;
    }
}