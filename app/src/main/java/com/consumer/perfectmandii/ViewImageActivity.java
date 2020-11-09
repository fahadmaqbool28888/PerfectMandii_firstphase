package com.consumer.perfectmandii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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
}