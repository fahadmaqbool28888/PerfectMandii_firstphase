package com.consumer.perfectmandii.Adapter.Slider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.consumer.perfectmandii.R;

import java.net.URL;

public class ImageAdapter extends PagerAdapter {
    Context context;
    private LayoutInflater layoutInflater;
    String Image_path;
    Bitmap mIcon_val;

  public   ImageAdapter()
    {

    }

    public ImageAdapter(Context context,String Image_path)
    {
        this.context=context;
        this.Image_path=Image_path;
    }

    private final int[] sliderImageId = new int[]{
            R.drawable.optimizedlogo, R.drawable.main_bg,R.drawable.profile,
    };


    @Override
    public int getCount() {
        return sliderImageId.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return  view == object;
    }



    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        //imageView.setImageResource(sliderImageId[position]);

        try
        {
            URL newurl = new URL(Image_path);
            mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            imageView.setImageBitmap(mIcon_val);
        }catch (Exception ex)
        {
            Toast.makeText(context,ex.toString(),Toast.LENGTH_LONG).show();
        }

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
