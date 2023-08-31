package com.customer.perfectcustomer.Activity.Home.ProductScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.customer.perfectcustomer.R;

import java.io.InputStream;

public class ViewImageActivity extends AppCompatActivity
{
    String path;
    ImageView product_image_box;
    ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        intialize_Widget();
        Intent intent=getIntent();
        path=intent.getStringExtra("path");
        product_image_box=findViewById(R.id.product_image_box);


        Glide.with(ViewImageActivity.this).load(path).into(product_image_box);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());


        product_image_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadImage().execute(path);

            }
        });

    }

    void intialize_Widget()
    {
        product_image_box=findViewById(R.id.product_image_box);
    }

    // this redirects all touch events in the activity to the gesture detector
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return scaleGestureDetector.onTouchEvent(event);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        // when a scale gesture is detected, use it to resize the image
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            product_image_box.setScaleX(mScaleFactor);
            product_image_box.setScaleY(mScaleFactor);
            return true;
        }
    }


    private class DownloadImage extends AsyncTask<String,String,Bitmap> {

        ProgressDialog mProgressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ViewImageActivity.this);
            mProgressDialog.setTitle("Download Image Tutorial");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }


        @Override
        protected Bitmap doInBackground(String... strings) {
            String imageURL = strings[0];
            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mProgressDialog.dismiss();
        }
    }
}